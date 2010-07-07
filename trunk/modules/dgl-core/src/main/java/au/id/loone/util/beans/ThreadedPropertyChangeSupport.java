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

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.util.Map;
import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import au.id.loone.util.DGLUtil;
import au.id.loone.util.tracing.TraceUtil;
import org.apache.log4j.Logger;

/**
 * A version of {@link PropertyChangeSupport}
 * that dispatches property change events
 * in a separater dispatcher thread.
 *
 * <p>When a property change event is generated,
 *      an instance of this class places it into a queue,
 *      where it is later dispatched by a dispatcher thread.
 *      Property change events thus dispatched
 *      conform to the requirement that they be delivered in exact order.
 *      However,
 *      since the property change event is dispatched in a thread
 *      different to the one that caused the property change,
 *      any locks that the original thread had will not be applicable
 *      to the dispatch.
 *      This may be useful in preventing thread deadlocks.</p>
 *
 * <p>This type of property change support is not suitable for constrained properties.</p>
 *
 * <p>The current version of this class delivers a given property change even to all listeners
 *      in sequence from a single dispatch thread.
 *      However,
 *      furture versions of this class may dispatch to each listener in its own thread.
 *      Such a future version will however,
 *      ensure that all listeners of a given property change event have been notified
 *      before it begins delivery of the next property change event.</p>
 *
 * @author David G Loone
 */
public class ThreadedPropertyChangeSupport
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = Logger.getLogger(ThreadedPropertyChangeSupport.class);

    /**
     * The amount by which to reduce the priority of the dispatcher thread.
     */
    public static int DISPATCHER_THREAD_PRIORITY_DELTA = 0;

    /**
     * The list of property change listeners for all properties.
     */
    private transient List<PropertyChangeListener> listeners = new LinkedList<PropertyChangeListener>();

    /**
     * Map of property names to lists of property change listeners for that property.
     */
    private Map<String, List<PropertyChangeListener>> propertySpecificListeners =
            new HashMap<String, List<PropertyChangeListener>>();

    /**
     */
    private Object sourceBean;

    /**
     * The thread for dispatching property change events.
     */
    private Thread propertyChangeEventDispatcherThread;

    /**
     * The property change event queue.
     */
    private final BlockingQueue<PropertyChangeEvent> propertyChangeEventQueue =
            new LinkedBlockingQueue<PropertyChangeEvent>();

    /**
     */
    public ThreadedPropertyChangeSupport(
            final Object sourceBean
    )
    {
        this(sourceBean, null);
    }

    /**
     */
    public ThreadedPropertyChangeSupport(
            final Object sourceBean,
            final String sourceBeanName
    )
    {
        super();

        if (sourceBean == null) {
            throw new NullPointerException("sourceBean");
        }
        this.sourceBean = sourceBean;

        String threadName;
        if (sourceBeanName == null) {
            final String classShortName =
                    sourceBean.getClass().getName().substring(sourceBean.getClass().getName().lastIndexOf('.') + 1);
            threadName = "PropertyChangeDispatcher_" + classShortName;
            int i = 0;
            while (threadExists(threadName)) {
                threadName = "PropertyChangeDispatcher_" + classShortName + '_' + i;
                i++;
            }
        }
        else {
            threadName = "PropertyChangeDispatcher_" + sourceBeanName;
        }
        final Runnable runner = new PropertyChangeEventDispatchRunner();
        propertyChangeEventDispatcherThread = new Thread(runner, threadName);
        propertyChangeEventDispatcherThread.setDaemon(false);
        propertyChangeEventDispatcherThread.setPriority(propertyChangeEventDispatcherThread.getPriority() -
                DISPATCHER_THREAD_PRIORITY_DELTA);
        propertyChangeEventDispatcherThread.start();
    }

    /**
     */
    private boolean threadExists(
            final String threadName
    )
    {
        boolean result;

        result = false;
        final Map<Thread, StackTraceElement[]> threadsMap = Thread.getAllStackTraces();
        for (final Thread thread: threadsMap.keySet()) {
            if (DGLUtil.equals(thread.getName(), threadName)) {
                result = true;
                break;
            }
        }

        return result;
    }

    /**
     * Performs an orderly shutdown of the dispatcher thread.
     */
    public void stop()
    {
        propertyChangeEventDispatcherThread.interrupt();
    }

    /**
     */
    public synchronized void addPropertyChangeListener(
            final PropertyChangeListener listener
    )
    {
        if (listener != null) {
            if (listener instanceof PropertyChangeListenerProxy) {
                final PropertyChangeListenerProxy proxy = (PropertyChangeListenerProxy)listener;
                // Call two argument add method.
                addPropertyChangeListener(proxy.getPropertyName(), (PropertyChangeListener)proxy.getListener());
            }
            else {
                listeners.add(listener);
            }
        }
    }

    /**
     */
    public synchronized void removePropertyChangeListener(
            final PropertyChangeListener listener
    )
    {
        if (listener == null) {
            return;
        }

        if (listener instanceof PropertyChangeListenerProxy) {
            final PropertyChangeListenerProxy proxy = (PropertyChangeListenerProxy)listener;
            // Call two argument remove method.
            removePropertyChangeListener(proxy.getPropertyName(), (PropertyChangeListener)proxy.getListener());
        }
        else {
            listeners.remove(listener);
        }
    }

    /**
     */
    public synchronized PropertyChangeListener[] getPropertyChangeListeners()
    {
        final List<PropertyChangeListener> result = new LinkedList<PropertyChangeListener>();

        // Add all the PropertyChangeListeners
        for (final PropertyChangeListener listener: listeners) {
            result.add(listener);
        }

        // Add all the PropertyChangeListenerProxys
        for (final String propertyName: propertySpecificListeners.keySet()) {
            final List<PropertyChangeListener> propertyListeners = propertySpecificListeners.get(propertyName);
            for (final PropertyChangeListener listener: propertyListeners) {
                result.add(listener);
            }
        }

        return result.toArray(new PropertyChangeListener[result.size()]);
    }

    /**
     */
    public synchronized void addPropertyChangeListener(
            final String propertyName,
            final PropertyChangeListener listener
    )
    {
        if ((listener != null) && (propertyName != null)) {
            List<PropertyChangeListener> propertyListeners = propertySpecificListeners.get(propertyName);
            if (propertyListeners == null) {
                propertyListeners = new LinkedList<PropertyChangeListener>();
                propertySpecificListeners.put(propertyName, propertyListeners);
            }
            propertyListeners.add(listener);
        }
    }

    /**
     */
    public synchronized void removePropertyChangeListener(
            final String propertyName,
            final PropertyChangeListener listener
    )
    {
        if ((listener != null) && (propertyName != null)) {
            final List<PropertyChangeListener> propertyListeners = propertySpecificListeners.get(propertyName);
            if (propertyListeners != null) {
                propertyListeners.remove(listener);
            }
        }
    }

    /**
     */
    public synchronized PropertyChangeListener[] getPropertyChangeListeners(
            final String propertyName
    )
    {
        final List<PropertyChangeListener> result;

        result = new LinkedList<PropertyChangeListener>();
        final List<PropertyChangeListener> propertyListeners = propertySpecificListeners.get(propertyName);
        if (propertyListeners != null) {
            for (final PropertyChangeListener propertyListener: propertyListeners) {
                result.add(propertyListener);
            }
        }

        return result.toArray(new PropertyChangeListener[result.size()]);
    }

    /**
     */
    public synchronized boolean hasListeners(
            final String propertyName
    )
    {
        final List<PropertyChangeListener> propertyListeners = propertySpecificListeners.get(propertyName);
        return (propertyListeners != null) &&
                (propertyListeners.size() != 0);
    }

    /**
     */
    public void firePropertyChange(
            final String propertyName,
            final boolean oldValue,
            final boolean newValue
    )
    {
        if (!DGLUtil.equals(oldValue, newValue)) {
            firePropertyChange(propertyName, oldValue ? Boolean.TRUE : Boolean.FALSE, newValue ? Boolean.TRUE : Boolean.FALSE);
        }
    }

    /**
     */
    public void fireIndexedPropertyChange(
            final String propertyName,
            final int index,
            final boolean oldValue,
            final boolean newValue
    )
    {
        if (!DGLUtil.equals(oldValue, newValue)) {
            fireIndexedPropertyChange(propertyName, index, oldValue ? Boolean.TRUE : Boolean.FALSE,
                    newValue ? Boolean.TRUE : Boolean.FALSE);
        }
    }

    /**
     */
    public void firePropertyChange(
            final String propertyName,
            final char oldValue,
            final char newValue
    )
    {
        if (!DGLUtil.equals(oldValue, newValue)) {
            firePropertyChange(propertyName, new Character(oldValue), new Character(newValue));
        }
    }

    /**
     */
    public void fireIndexedPropertyChange(
            final String propertyName,
            final int index,
            final char oldValue,
            final char newValue
    )
    {
        if (!DGLUtil.equals(oldValue, newValue)) {
            fireIndexedPropertyChange(propertyName, index, new Character(oldValue), new Character(newValue));
        }
    }

    /**
     */
    public void firePropertyChange(
            final String propertyName,
            final byte oldValue,
            final byte newValue
    )
    {
        if (!DGLUtil.equals(oldValue, newValue)) {
            firePropertyChange(propertyName, new Byte(oldValue), new Byte(newValue));
        }
    }

    /**
     */
    public void fireIndexedPropertyChange(
            final String propertyName,
            final int index,
            final byte oldValue,
            final byte newValue
    )
    {
        if (!DGLUtil.equals(oldValue, newValue)) {
            fireIndexedPropertyChange(propertyName, index, new Byte(oldValue), new Byte(newValue));}
    }

    /**
     */
    public void firePropertyChange(
            final String propertyName,
            final short oldValue,
            final short newValue
    )
    {
        if (!DGLUtil.equals(oldValue, newValue)) {
            firePropertyChange(propertyName, new Short(oldValue), new Short(newValue));
        }
    }

    /**
     */
    public void fireIndexedPropertyChange(
            final String propertyName,
            final int index,
            final short oldValue,
            final short newValue
    )
    {
        if (!DGLUtil.equals(oldValue, newValue)) {
            fireIndexedPropertyChange(propertyName, index, new Short(oldValue), new Short(newValue));
        }
    }

    /**
     */
    public void firePropertyChange(
            final String propertyName,
            final int oldValue,
            final int newValue
    )
    {
        if (!DGLUtil.equals(oldValue, newValue)) {
            firePropertyChange(propertyName, new Integer(oldValue), new Integer(newValue));
        }
    }

    /**
     */
    public void fireIndexedPropertyChange(
            final String propertyName,
            final int index,
            final int oldValue,
            final int newValue
    )
    {
        if (!DGLUtil.equals(oldValue, newValue)) {
            fireIndexedPropertyChange(propertyName, index, new Integer(oldValue), new Integer(newValue));
        }
    }

    /**
     */
    public void firePropertyChange(
            final String propertyName,
            final long oldValue,
            final long newValue
    )
    {
        if (!DGLUtil.equals(oldValue, newValue)) {
            firePropertyChange(propertyName, new Long(oldValue), new Long(newValue));
        }
    }

    /**
     */
    public void fireIndexedPropertyChange(
            final String propertyName,
            final int index,
            final long oldValue,
            final long newValue
    )
    {
        if (!DGLUtil.equals(oldValue, newValue)) {
            fireIndexedPropertyChange(propertyName, index, new Long(oldValue), new Long(newValue));
        }
    }

    /**
     */
    public void firePropertyChange(
            final String propertyName,
            final float oldValue,
            final float newValue
    )
    {
        if (!DGLUtil.equals(oldValue, newValue)) {
            firePropertyChange(propertyName, new Float(oldValue), new Float(newValue));
        }
    }

    /**
     */
    public void fireIndexedPropertyChange(
            final String propertyName,
            final int index,
            final float oldValue,
            final float newValue
    )
    {
        if (!DGLUtil.equals(oldValue, newValue)) {
            fireIndexedPropertyChange(propertyName, index, new Float(oldValue), new Float(newValue));
        }
    }

    /**
     */
    public void firePropertyChange(
            final String propertyName,
            final double oldValue,
            final double newValue
    )
    {
        if (!DGLUtil.equals(oldValue, newValue)) {
            firePropertyChange(propertyName, new Double(oldValue), new Double(newValue));
        }
    }

    /**
     */
    public void fireIndexedPropertyChange(
            final String propertyName,
            final int index,
            final double oldValue,
            final double newValue
    )
    {
        if (!DGLUtil.equals(oldValue, newValue)) {
            fireIndexedPropertyChange(propertyName, index, new Double(oldValue), new Double(newValue));
        }
    }

    /**
     */
    public void firePropertyChange(
            final String propertyName,
            final Object oldValue,
            final Object newValue
    )
    {
        if (!DGLUtil.equals(oldValue, newValue)) {
            firePropertyChange(new PropertyChangeEvent(sourceBean, propertyName, oldValue, newValue));
        }
    }

    /**
     */
    public void fireIndexedPropertyChange(
            final String propertyName,
            final int index,
            final Object oldValue,
            final Object newValue
    )
    {
        if (!DGLUtil.equals(oldValue, newValue)) {
            firePropertyChange(new IndexedPropertyChangeEvent(sourceBean, propertyName, oldValue, newValue, index));
        }
    }

    /**
     * This implementation simply places the event into a queue and returns immediately.
     */
    public void firePropertyChange(
            final PropertyChangeEvent event
    )
    {
        if (!DGLUtil.equals(event.getOldValue(), event.getNewValue())) {
            propertyChangeEventQueue.add(event);
        }
    }

    /**
     */
    private synchronized void deliver(
            final PropertyChangeEvent event
    )
    {
        final String propertyName = event.getPropertyName();

        for (final PropertyChangeListener listener: listeners) {
            try {
                listener.propertyChange(event);
            }
            catch (final RuntimeException e) {
                // Log a warning, but continue delivering events.
                LOG.warn("delier: exception while delivering property change event: " + TraceUtil.formatObj(e), e);
            }
        }

        final List<PropertyChangeListener> propertyListeners = propertySpecificListeners.get(propertyName);
        if (propertyListeners != null) {
            for (final PropertyChangeListener propertyListener: propertyListeners) {
                try {
                    propertyListener.propertyChange(event);
                }
                catch (final RuntimeException e) {
                    // Log a warning, but continue delivering events.
                    LOG.warn("delier: exception while delivering property change event: " + TraceUtil.formatObj(e), e);
                }
            }
        }
    }

    /**
     *
     */
    private final class PropertyChangeEventDispatchRunner
            implements Runnable
    {

        /**
         */
        public PropertyChangeEventDispatchRunner()
        {
            super();
        }

        /**
         */
        public void run()
        {
            LOG.trace("PropertyChangeEventDispatchRunner.run()");

            boolean isInterrupted = false;
            while (!isInterrupted || (propertyChangeEventQueue.size() != 0)) {
                if (propertyChangeEventDispatcherThread == null) {
                    LOG.warn("run: emergency thread shutdown");
                    break;
                }

                try {
                    final PropertyChangeEvent event = propertyChangeEventQueue.take();
                    deliver(event);
                }
                catch (final InterruptedException e) {
                    isInterrupted = true;
                }
            }

            LOG.trace("~PropertyChangeEventDispatchRunner.run");
        }

    }

}
