/*
 * Copyright 2008, David G Loone
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package au.id.loone.util.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;

import au.id.loone.util.tracing.TraceUtil;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author David G Loone
 */
public class ThreadedPropertyChangeSupport_Test
{

    static {
        TraceUtil.init();
    }

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = Logger.getLogger(ThreadedPropertyChangeSupport_Test.class);

    /**
     */
    private ThreadedPropertyChangeSupport propertyChangeSupport;

    /**
     */
    @Before
    public void setup()
            throws Exception
    {
        propertyChangeSupport = new ThreadedPropertyChangeSupport(this);
    }

    /**
     */
    @After
    public void tearDown()
            throws Exception
    {
//        propertyChangeSupport.stop();
    }

    /**
     */
    public String toString()
    {
        return "me";
    }

    /**
     */
    @Test
    public void testFirePropertyChangeEvent()
            throws Exception
    {
        final Listener l = new Listener();
        propertyChangeSupport.addPropertyChangeListener(l);
        propertyChangeSupport.firePropertyChange("test", 2, 3);
        LOG.debug("testFirePropertyChangeEvent: " + TraceUtil.formatObj(l, "l.events"));
        Thread.sleep(1000);
        LOG.debug("testFirePropertyChangeEvent: " + TraceUtil.formatObj(l, "l.events"));
    }

    /**
     */
    public static class Listener
            implements PropertyChangeListener
    {

        @SuppressWarnings({"UnusedDeclaration"})
        private static final Logger LOG = Logger.getLogger(Listener.class);

        /**
         * Current value of the <b>events</b> property.
         */
        private List<PropertyChangeEvent> events = new LinkedList<PropertyChangeEvent>();

        /**
         */
        public Listener()
        {
            super();
        }

        /**
         */
        public void propertyChange(
                final PropertyChangeEvent event
        )
        {
            LOG.debug("propertyChange(" + TraceUtil.formatObj(event) + ")");
            events.add(event);
        }

        /**
         * Getter method for the <b>events</b> property.
         */
        public List<PropertyChangeEvent> getEvents()
        {
            return events;
        }

    }

}
