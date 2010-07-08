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
        LOG.trace("onDocumentChanged(" + TraceUtil.formatObj(event) + ")");
        LOG.trace("onDocumentChanged: " + TraceUtil.formatObj(event, "event.modifiedBy"));
        LOG.trace("onDocumentChanged: " + TraceUtil.formatObj(event, "event.blip.content"));
        LOG.trace("onDocumentChanged: " + TraceUtil.formatObj(event, "event.blip.elements.size"));
        final Blip blip = event.getBlip();
        for (final Integer offset : blip.getElements().keySet()) {
            final Element el = blip.getElements().get(offset);
            LOG.trace("onDocumentChanged: " + TraceUtil.formatObj(offset, "offset"));
            LOG.trace("onDocumentChanged: " + TraceUtil.formatObj(el, "el.type"));
            LOG.trace("onDocumentChanged: " + TraceUtil.formatObj(el, "el.indent"));
            LOG.trace("onDocumentChanged: " + TraceUtil.formatObj(el, "el.text"));
            LOG.trace("onDocumentChanged: " + TraceUtil.formatObj(el, "el.lineType"));
        }

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
                final Gadget gadget = new Gadget(CONFIG.getTaskTrackerGadgetUrl().toString());
                gadget.getProperties().put("taskTrackerId", String.valueOf(DGLDateTimeUtil.now().getTime()));
                gadget.getProperties().put("progress", String.valueOf(0));
                gadget.getProperties().put("readonly", Boolean.FALSE.toString());
                gadget.getProperties().put("version", String.valueOf(1));
                blip.range(idx, idx + 4).replace(gadget);
            }

            // Find the next one.
            idx = blip.getContent().indexOf("[[]]", idx + 4);
            LOG.trace("onDocumentChanged: " + TraceUtil.formatObj(idx, "idx"));
        }

        checkTrackers(blip);
    }

    /**
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
     */
    private void checkTrackers(
            final Blip blip
    )
    {
            final List<TrackerSpec> trackers = new LinkedList<TrackerSpec>();
            int indent = 0;
            for (final Integer offset : blip.getElements().keySet()) {
                final Element el = blip.getElements().get(offset);
                LOG.trace("checkTrackers: --------------------------------------");
                LOG.trace("checkTrackers: " + TraceUtil.formatObj(offset, "offset"));
                LOG.trace("checkTrackers: " + TraceUtil.formatObj(el, "el.class.name"));
                LOG.trace("checkTrackers: " + TraceUtil.formatObj(el, "el.type"));
                LOG.trace("checkTrackers: " + TraceUtil.formatObj(el, "el.indent"));
                LOG.trace("checkTrackers: " + TraceUtil.formatObj(el, "el.text"));
                LOG.trace("checkTrackers: " + TraceUtil.formatObj(el, "el.lineType"));

                if (el instanceof Line) {
                    LOG.trace("checkTrackers: line");
                    final Line lineEl = (Line)el;
                    LOG.trace("checkTrackers: line 1");
                    indent = DGLStringUtil.isNullOrEmpty(lineEl.getIndent()) ? 0 : Integer.valueOf(lineEl.getIndent());
                    LOG.trace("checkTrackers: line 2");
                }
                else if (el instanceof Gadget) {
                    LOG.trace("checkTrackers: gadget");
                    trackers.add(new TrackerSpec(indent, (Gadget)el));
                }
            }

            processTrackersTree(blip, trackers);
    }

    /**
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
            int idx = 0;
            int total = 0;
            int numNodes = 0;
            while (idx < trackers.size()) {
                LOG.trace("processTrackersTree: " + TraceUtil.formatObj(idx, "idx"));
                final int indent = trackers.get(idx).getIndent();
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
                    total += trackers.get(idx).getProgress();
                    numNodes++;

                    // We have a leaf node, it shouldn't be readonly. It might be though if things have been
                    // rearranged.
                    if (trackers.get(idx).getReadonly()) {
                        setTrackerState(blip, trackers.get(idx).getGadget(), false, trackers.get(idx).getProgress());
                    }
                }
                else {
                    final int childrenProgress = processTrackersTree(blip, children);

                    setTrackerState(blip, trackers.get(idx).getGadget(), true, childrenProgress);

                    total += childrenProgress;
                    numNodes++;
                }

                idx += (children.size() + 1);
            }
            result = total / numNodes;
        }

        LOG.trace("~processTrackersTree = " + TraceUtil.formatObj(result));
        return result;
    }

    /**
     */
    private void setTrackerState(
            final Blip blip,
            final Gadget tracker,
            final boolean readonly,
            final int progress
    )
    {
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