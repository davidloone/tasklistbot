/*
 * Copyright (C) 2010  David G Loone
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package au.id.loone.apps.tasklistbot.bot;

import java.util.LinkedList;
import java.util.List;

import au.id.loone.apps.tasklistbot.TaskListBotConfig;
import au.id.loone.util.DGLDateTimeUtil;
import au.id.loone.util.DGLStringUtil;
import au.id.loone.util.DGLUtil;
import au.id.loone.util.config.ConfigData;
import au.id.loone.util.tracing.TraceUtil;

import com.google.wave.api.AbstractRobot;
import com.google.wave.api.Blip;
import com.google.wave.api.BlipContentRefs;
import com.google.wave.api.Context;
import com.google.wave.api.Element;
import com.google.wave.api.ElementType;
import com.google.wave.api.Gadget;
import com.google.wave.api.Line;
import com.google.wave.api.event.BlipSubmittedEvent;
import com.google.wave.api.event.DocumentChangedEvent;
import com.google.wave.api.event.GadgetStateChangedEvent;
import com.google.wave.api.event.WaveletSelfAddedEvent;
import org.apache.log4j.Logger;

/**
 * Wave Robot servlet for the task list robot.
 *
 * @author David G Loone
 */
public final class TaskListBot
        extends AbstractRobot
{

    static {
        new ConfigData.Log4jConfigurator();
    }

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(TaskListBot.class);

    /**
     * Config bean.
     */
    private static final TaskListBotConfig CONFIG = TaskListBotConfig.factory();

    /**
     */
    public TaskListBot()
    {
        super();

        LOG.trace("TaskListBot()");

        // Only need to do this once.
        setupVerificationToken(CONFIG.getVerificationToken(), CONFIG.getSecurityToken());

//        setupOAuth(CONFIG.getConsumerKey(), CONFIG.getConsumerKey());
    }

    /**
     * Getter method for the <b>robotName</b> property.
     */
    @Override
    protected String getRobotName()
    {
        LOG.trace("getRobotName()");

        return CONFIG.getRobotName();
    }

    /**
     * Getter method for the <b>robotAvatarUrl</b> property.
     */
    @Override
    protected String getRobotAvatarUrl()
    {
        LOG.trace("getRobotAvatarUrl()");

        return (CONFIG.getRobotAvatarUrl() == null) ? super.getRobotAvatarUrl() :
                CONFIG.getRobotAvatarUrl().toString();
    }

    /**
     * Getter method for the <b>robotProfilePageUrl</b> property.
     */
    @Override
    protected String getRobotProfilePageUrl()
    {
        LOG.trace("getRobotProfilePageUrl()");

        return (CONFIG.getRobotProfilePageUrl() == null) ? null :
                CONFIG.getRobotProfilePageUrl().toString();
    }

    /**
     */
    @Override
    public void onWaveletSelfAdded(
            final WaveletSelfAddedEvent event
    )
    {
        LOG.trace("onWaveletSelfAdded(" + TraceUtil.formatObj(event) + ")");
    }

    /**
     */
    @Override
    public void onBlipSubmitted(
            final BlipSubmittedEvent event
    )
    {
        LOG.trace("onBlipSubmitted(" + TraceUtil.formatObj(event) + ")");

        checkTrackers(event.getBlip());
    }

    /**
     * Document change events.
     *
     * <p>We filter on the special character sequence that we want to convert to a task tracker,
     *      but only respond to it when it occurs at the beginning of an "li" line.
     *      This kind of works,
     *      but we won't see changes to indenting structure, etc,
     *      nor deletion of task trackers.</p>
     */
    @Capability(
            contexts = {
                    Context.SELF
            },
            filter="\\[\\[\\]\\]"
    )
    @Override
    public void onDocumentChanged(
            final DocumentChangedEvent event
    )
    {
        final Blip blip = event.getBlip();

        // Search for, and replace, the target string.
        int idx = blip.getContent().indexOf("[[]]", 0);
        LOG.trace("onDocumentChanged: " + TraceUtil.formatObj(idx, "idx"));
        while (idx != -1) {
            // The string must be at the start of a line of type "li". Look for an element that starts
            // right before the string, and check that is right kind, etc.
            final Element myElement = blip.getElements().get(idx - 1);
            if ((myElement != null) &&
                    DGLUtil.equals(myElement.getType(), ElementType.LINE) &&
                    DGLStringUtil.equals(((Line)myElement).getLineType(), "li")) {
                // Create the task tracker gadget.
                final Gadget gadget = new Gadget(CONFIG.getTaskTrackerGadgetUrl().toString());
                gadget.getProperties().put("taskTrackerId", String.valueOf(DGLDateTimeUtil.now().getTime()));
                gadget.getProperties().put("progress", String.valueOf(0));
                gadget.getProperties().put("readonly", Boolean.FALSE.toString());
                gadget.getProperties().put("version", String.valueOf(1));
                // And replace.
                blip.range(idx, idx + 4).replace(gadget);
            }

            // Find the next instance of the target string.
            idx = blip.getContent().indexOf("[[]]", idx + 4);
            LOG.trace("onDocumentChanged: " + TraceUtil.formatObj(idx, "idx"));
        }

        checkTrackers(blip);
    }

    /**
     * When user modifies (<i>ie</i> checks or unchecks) a task tracker.
     */
    @Override
    public void onGadgetStateChanged(
            final GadgetStateChangedEvent event
    )
    {
        try {
            LOG.trace("onGadgetStateChanged(" + TraceUtil.formatObj(event) + ")");

            checkTrackers(event.getBlip());
        }
        catch (final RuntimeException e) {
            LOG.warn("onGadgetStateChanged: " + TraceUtil.formatObj(e), e);
            throw e;
        }
    }

    /**
     * Performs an idempotent check of all the task trackers in the document.
     *
     * @param blip
     *      The blip being processed.
     */
    private void checkTrackers(
            final Blip blip
    )
    {
        // Iterate over the elements in the document. The line elements give us an indent level, which we
        // remember for to attach to a gadget that we find in that line. The result is a list of gadget/indent
        // pairs, which is a kind of tree.
        final List<TrackerSpec> trackers = new LinkedList<TrackerSpec>();
        int indent = 0;
        for (final Integer offset : blip.getElements().keySet()) {
            final Element el = blip.getElements().get(offset);

            if (el instanceof Line) {
                final Line lineEl = (Line)el;
                indent = DGLStringUtil.isNullOrEmpty(lineEl.getIndent()) ? 0 : Integer.valueOf(lineEl.getIndent());
            }
            else if (el instanceof Gadget) {
                trackers.add(new TrackerSpec(indent, (Gadget)el));
            }
        }

        // Process the tree.
        processTrackersTree(blip, trackers);
    }

    /**
     * Recursively process a tree of gadgets,
     * and return the completeness value.
     *
     * @param blip
     *      The blip being processed.
     * @param trackers
     *      The list of gadget/indent pairs.
     * @return
     *      The progress level of the list (as a percentage).
     */
    private int processTrackersTree(
            final Blip blip,
            final List<TrackerSpec> trackers
    )
    {
        LOG.trace("processTrackersTree(" +
                TraceUtil.formatObj(blip) + ", " +
                "..." + ")");
        for (final TrackerSpec trackerSpec : trackers) {
            LOG.trace("processTrackersTree: " + TraceUtil.formatObj(trackerSpec, "trackerSpec"));
        }

        final int result;

        if (trackers.size() == 1) {
            // Single tracker, just return its progress.
            result = trackers.get(0).getProgress();
        }
        else {
            // Process the trackers in the list, keeping enough data (ie cumulative total and number of nodes)
            // to calculate the average at the end.
            int idx = 0;
            int total = 0;
            int numNodes = 0;
            while (idx < trackers.size()) {
                LOG.trace("processTrackersTree: " + TraceUtil.formatObj(idx, "idx"));
                final int indent = trackers.get(idx).getIndent();
                // Make a list of all the children (in fact, all the descendants) of this tracker gadget. We
                // define this as all the gadgets in the document up until the indent gets back to the current
                // indent. There are various edge cases that might be a bit dodgy (eg if the indent of the next
                // gadget is two or more than the current indent), but they are sufficiently ill-defined that
                // the definition contained herein will probably suffice.
                final List<TrackerSpec> children = new LinkedList<TrackerSpec>();
                if (idx < trackers.size() - 1) {
                    int childIdx = idx + 1;
                    while ((childIdx < trackers.size()) &&
                            (trackers.get(childIdx).getIndent() > indent)) {
                        LOG.trace("processTrackersTree: " + TraceUtil.formatObj(childIdx, "childIdx"));
                        children.add(trackers.get(childIdx));
                        childIdx++;
                    }
                }
                if (children.size() == 0) {
                    // Leaf node, so the progress level can be read from the tracker itself.
                    total += trackers.get(idx).getProgress();
                    numNodes++;

                    // A leaf node should be readonly. It might be though if things have been rearranged.
                    if (trackers.get(idx).getReadonly()) {
                        setTrackerState(blip, trackers.get(idx).getGadget(), false, trackers.get(idx).getProgress());
                    }
                }
                else {
                    // This tracker has children. Process them to get the completed value.
                    final int childrenProgress = processTrackersTree(blip, children);

                    setTrackerState(blip, trackers.get(idx).getGadget(), true, childrenProgress);

                    total += childrenProgress;
                    numNodes++;
                }

                idx += (children.size() + 1);
            }

            // Calculate the average to return.
            result = total / numNodes;
        }

        LOG.trace("~processTrackersTree = " + TraceUtil.formatObj(result));
        return result;
    }

    /**
     * @param blip
     *      The blip being processed.
     * @param tracker
     *      The task tracker gadget to set the state of.
     * @param readonly
     *      Whether the tracker gadget should be set as readonly.
     * @param progress
     *      The progress value to assign to the tracker gadget (as a percentage).
     */
    private void setTrackerState(
            final Blip blip,
            final Gadget tracker,
            final boolean readonly,
            final int progress
    )
    {
        // We use the hack of replacing the gadget if its state has to be changed. Only replace the gadget if
        // the state has actually changed.
        final boolean oldReadonly = DGLStringUtil.isNullOrEmpty(tracker.getProperty("readonly")) ? false :
                Boolean.valueOf(tracker.getProperty("readonly"));
        final int oldProgress = DGLStringUtil.isNullOrEmpty(tracker.getProperty("progress")) ? 0 :
                Integer.valueOf(tracker.getProperty("progress"));
        if ((oldReadonly != readonly) ||
                (oldProgress != progress)) {
            LOG.trace("setTrackerState: replacing tracker: " + TraceUtil.formatObj(tracker));
            final BlipContentRefs trackerRefs = blip.all(ElementType.GADGET,
                    Gadget.restrictByUrl(CONFIG.getTaskTrackerGadgetUrl().toString()),
                    Gadget.restrictByProperty("taskTrackerId", tracker.getProperty("taskTrackerId")));
            final Gadget newTracker = new Gadget(CONFIG.getTaskTrackerGadgetUrl().toString());
            newTracker.getProperties().put("taskTrackerId", tracker.getProperty("taskTrackerId"));
            newTracker.getProperties().put("progress", String.valueOf(progress));
            newTracker.getProperties().put("readonly", String.valueOf(readonly));
            newTracker.getProperties().put("version", tracker.getProperty("version"));
            trackerRefs.replace(newTracker);
        }
    }

    /**
     * A gadget/indent pair.
     */
    private static class TrackerSpec
    {

        @SuppressWarnings({"UnusedDeclaration"})
        private static final Logger LOG = TraceUtil.getLogger(TrackerSpec.class);

        /**
         * Current value of the <b>gadget</b> property.
         */
        private Gadget gadget;

        /**
         * Current value of the <b>indent</b> property.
         */
        private int indent;

        /**
         */
        public TrackerSpec(
                final int indent,
                final Gadget gadget
        )
        {
            super();

            this.gadget = gadget;
            this.indent = indent;
        }

        /**
         */
        public String toString()
        {
            return "TrackerSpec{" +
                    TraceUtil.formatObj(gadget, "gadget") + ", " +
                    TraceUtil.formatObj(indent, "indent") + "}";
        }

        /**
         * Getter method for the <b>gadget</b> property.
         */
        @SuppressWarnings({"UnusedDeclaration"})
        public Gadget getGadget() {return gadget;}

        /**
         * Getter method for the <b>indent</b> property.
         */
        @SuppressWarnings({"UnusedDeclaration"})
        public int getIndent() {return indent;}

        /**
         * Getter method for the <b>progress</b> property.
         */
        @SuppressWarnings({"UnusedDeclaration"})
        public int getProgress()
        {
            final String progressStr = gadget.getProperty("progress");
            return DGLStringUtil.isNullOrEmpty(progressStr) ? 0 : Integer.parseInt(progressStr);
        }

        /**
         * Getter method for the <b>readonly</b> property.
         */
        @SuppressWarnings({"UnusedDeclaration"})
        public boolean getReadonly()
        {
            return Boolean.parseBoolean(gadget.getProperty("readonly"));
        }

    }

}