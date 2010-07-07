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

package au.id.loone.util.exception;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import au.id.loone.util.ConfigResource;
import au.id.loone.util.DGLStringUtil;
import au.id.loone.util.xml.DGLXMLUtils;
import au.id.loone.util.ResourceNotFoundException;
import au.id.loone.util.tracing.TraceUtil;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *  A repository of error message information.
 *
 *  <p>Each error messages repository is associated with a <b>subsystem</b>,
 *      which is an arbitrary distinction.
 *      This is to allow decoupling of information in packages.
 *      It is possible to specify a "default" error message repository,
 *      so that a hierarchy of error message repositories can be be built.
 *      That is,
 *      error message repositories further down the tree can inherit messages
 *      from nearer the root of the tree.
 *      For example,
 *      a common part of the application can define some error messages that are
 *      inherited by various other subsystems within the application.
 *      Each subsystem usually supplies its error messages repository statically
 *      from a central location,
 *      possibly via a constants class.</p>
 *
 *  <p>When an exception is thrown for a given message key,
 *      the {@link ErrorMessagesRepository.Message message} associated with that message key
 *      should be placed into the exception
 *      rather than the substituted message text.
 *      The {@link ErrorMessagesRepository.Message} and {@link ErrorMessagesRepository.Fault} xstreamClasses
 *      are marked as {@link Serializable serializable} for this reason.
 *      If error message substitution is done as late as possible in this way,
 *      it allows for substitution fragments to be created after the exception is thrown
 *      and still be available to the error message.</p>
 *
 *  <p>Error messages are loaded from an XML file,
 *      whose structure is specified by
 *      <code><a href="doc-files/errorMessagesRepository.dtd">errorMessagesRepository.dtd</a></code>.</p>
 *
 *  @author David G Loone
 */
public class ErrorMessagesRepository
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = Logger.getLogger(ErrorMessagesRepository.class);

    /**
     *  An empty error messages repository.
     */
    public final static ErrorMessagesRepository EMPTY = new ErrorMessagesRepository("UNKNOWN", null);

    /**
     *  The default error messages catalog.
     */
    public static final ErrorMessagesRepository INSTANCE;
    static {
        ErrorMessagesRepository instance;
        try {
            instance = ErrorMessagesRepository.factory("SYSTEM",
                    ErrorMessagesRepository.class, "errorMessagesRepository.xml");
        }
        catch (final Throwable e) {
            LOG.warn("(static): exception while creating error message repository: " + TraceUtil.formatObj(e), e);
            instance = EMPTY;
        }
        INSTANCE = instance;
    }

    /**
     *  The map of message keys to messages.
     *  Keys of this map are objects of type {@link String},
     *  being the message keys.
     *  Values of this map are objects of type {@link Message},
     *  being the message corresponding to the keys.
     */
    private final Map<String, Message> messagesMap;

    /**
     *  The subsystem to which the error messages repository belongs.
     */
    private final String subsystem;

    /**
     *  Make an empty error messages repository.
     *
     *  @param subsystem
     *      The subsystem to which the error messages belongs.
     *  @param defaultMessages
     *      A messages repository to augment this one for when a message cannot be found
     *      for a given message id.
     *      A value of <code>null</code> indicates that there is no default messages object.
     */
    private ErrorMessagesRepository(
            final String subsystem,
            final ErrorMessagesRepository defaultMessages
    )
            throws ResourceNotFoundException
    {
        this(subsystem, null, defaultMessages);
    }

    /**
     *  Make a new error messages repository from an XML document config resource.
     *
     *  <p>If there is an error reading the resource,
     *      the application logs a warning message
     *      and continues with an empty error messages repository.
     *
     *  @param subsystem
     *      The subsystem to which the error messages belongs.
     *  @param referenceClass
     *      The reference class for loading the XML document config resource.
     *  @param configResourceName
     *      The name of the XML config resource.
     *  @return
     *      A reference to the error messages repository.
     *  @see ConfigResource#factory(Class, String)
     */
    public static ErrorMessagesRepository factory(
            final String subsystem,
            final Class referenceClass,
            final String configResourceName
    )
            throws ResourceNotFoundException
    {
        return factory(subsystem, referenceClass, configResourceName, null);
    }

    /**
     *  Make a new error messages repository from an XML document config resource.
     *
     *  <p>If there is an error reading the resource,
     *      the application logs a warning message
     *      and continues with an empty error messages repository
     *      (except for the the messages in <code>defaultMessaes</code>).
     *
     *  @param subsystem
     *      The subsystem to which the error messages belongs.
     *  @param referenceClass
     *      The reference class for loading the XML document config resource.
     *  @param configResourceName
     *      The name of the XML config resource.
     *  @param defaultMessages
     *      A messages repository to augment this one for when a message cannot be found
     *      for a given message id.
     *      A value of <code>null</code> indicates that there is no default messages object.
     *  @return
     *      A reference to the error messages repository.
     *  @see ConfigResource#factory(Class, String)
     */
    public static ErrorMessagesRepository factory(
            final String subsystem,
            final Class referenceClass,
            final String configResourceName,
            final ErrorMessagesRepository defaultMessages
    )
            throws ResourceNotFoundException
    {
        LOG.info("factory(" +
                TraceUtil.formatObj(subsystem) + ", " +
                TraceUtil.formatObj(referenceClass) + ", " +
                TraceUtil.formatObj(configResourceName) + ", ...)");

        // The value to return.
        ErrorMessagesRepository result;

        try {
            result = new ErrorMessagesRepository(subsystem,
                    ConfigResource.factory(referenceClass, configResourceName).asXMLDocument(true),
                    defaultMessages);
        }
        catch (final ResourceNotFoundException e) {
            LOG.warn("factory: cannot find error messages repository file, using empty repository", e);
            result = (defaultMessages == null) ? EMPTY : defaultMessages;
        }
        catch (final SAXException e) {
            LOG.warn("factory: exception while parsing error messages repository file, using empty repository", e);
            result = (defaultMessages == null) ? EMPTY : defaultMessages;
        }
        catch (final Throwable e) {
            LOG.warn("factory: exception while parsing error messages repository file, using empty repository", e);
            result = (defaultMessages == null) ? EMPTY : defaultMessages;
        }

        return result;
    }

//    /**
//     *  Make a new error messages repository from an XML document in a file.
//     *
//     *  <p>If there is an error reading the file,
//     *      the application logs a warning message
//     *      and continues with an empty error messages repository
//     *      (except for the the messages in <code>defaultMessaes</code>).
//     *
//     *  @param subsystem
//     *      The subsystem to which the error messages belongs.
//     *  @param docFile
//     *      The file that contains the XML document.
//     *  @param defaultMessages
//     *      A messages repository to augment this one for when a message cannot be found
//     *      for a given message id.
//     *      A value of <code>null</code> indicates that there is no default messages object.
//     *  @return
//     *      A reference to the error messages repository.
//     *  @see XMLUtils#createDocument(File, boolean)
//     */
//    public static ErrorMessagesRepository factory(
//            final String subsystem,
//            final File docFile,
//            final ErrorMessagesRepository defaultMessages
//    )
//            throws FileNotFoundException
//    {
//        // The value to return.
//        ErrorMessagesRepository result;
//
//        try {
//            result = new ErrorMessagesRepository(subsystem,
//                    DGLXMLUtils.createDocument(docFile, true), defaultMessages);
//        }
//        catch (final IOException e) {
//            // Log a warning, but continue with an empty messages repository.
//            LOG.warn("factory: error reading messages file " +
//                    TraceUtil.formatObj(docFile) + ": " +
//                    e.getClass().getName() + ":" + e.getMessage(), e);
//            result = new ErrorMessagesRepository(subsystem, defaultMessages);
//        }
//        catch (final SAXException e) {
//            // Log a warning, but continue with an empty messages repository.
//            LOG.warn("factory: error reading messages file " +
//                    TraceUtil.formatObj(docFile) + ": " +
//                    e.getClass().getName() + ":" + e.getMessage(), e);
//            result = new ErrorMessagesRepository(subsystem, defaultMessages);
//        }
//
//        return result;
//    }

    /**
     *  Make a new error messages repository from an XML document.
     *
     *  @param subsystem
     *      The subsystem to which the error messages belongs.
     *  @param messagesDoc
     *      The XML DOM object containing the message mappings.
     *  @param defaultMessages
     *      A messages repository to augment this one for when a message cannot be found
     *      for a given message id.
     *      A value of <code>null</code> indicates that there is no default messages object.
     */
    protected ErrorMessagesRepository(
            final String subsystem,
            final Document messagesDoc,
            final ErrorMessagesRepository defaultMessages
    )
    {
        super();
        LOG.info("ErrorMessagesRepository(" +
                TraceUtil.formatObj(subsystem) + ", " +
                "..., ...)");
        this.subsystem = subsystem;

        // Extract the info from the document.
        messagesMap = new HashMap<String, Message>();
        final List<String> contextKeys = new LinkedList<String>();
        if (messagesDoc != null) {
            final Element docElement = messagesDoc.getDocumentElement();
            if (!docElement.getNodeName().equals("errorMessagesRepository")) {
                throw new ResourceNotFoundException(
                        "Error messages XML file has incorrect document element " +
                        TraceUtil.formatObj(docElement.getNodeName()) + ".");
            }
            final String contextKeysStr = docElement.getAttribute("contextKeys");
            for (final StringTokenizer st = new StringTokenizer(contextKeysStr, ",");
                    st.hasMoreTokens(); ) {
                contextKeys.add(st.nextToken());
            }
            for (final Iterator i = DGLXMLUtils.DOM.childElements(docElement, "message"); i.hasNext(); ) {
                final Element messageElement = (Element)i.next();
                final String messageKey = messageElement.getAttribute("id");
                messagesMap.put(messageKey, new Message(messageElement, contextKeys));
            }
        }

        // Fill in any defaults from the defaults messages repository, if there is one.
        if (defaultMessages != null) {
            for (final String messageKey: defaultMessages.messagesMap.keySet()) {
                if (messagesMap.get(messageKey) == null) {
                    final Message message = defaultMessages.messagesMap.get(messageKey);
                    messagesMap.put(messageKey, message);
                }
            }
        }
        LOG.info("~ErrorMessagesRepository");
    }

    /**
     *  Make a new error messages repository from an XML document.
     *
     *  @param subsystem
     *      The subsystem to which the error messages belongs.
     *  @param messagesDoc
     *      The XML DOM object containing the message mappings.
     *  @param defaultMessages
     *      A messages repository to augment this one for when a message cannot be found
     *      for a given message id.
     *      A value of <code>null</code> indicates that there is no default messages object.
     *  @return
     *      A reference to the error messages repository.
     */
    public static ErrorMessagesRepository factory(
            final String subsystem,
            final Document messagesDoc,
            final ErrorMessagesRepository defaultMessages
    )
    {
        return new ErrorMessagesRepository(subsystem, messagesDoc, defaultMessages);
    }

    /**
     */
    public String toString()
    {
        // The value to return.
        final StringBuffer result = new StringBuffer();

        result.append(subsystem);
        result.append(":[");
        for (final Iterator i = messagesMap.keySet().iterator(); i.hasNext(); ) {
            final String messageKey = (String)i.next();
            result.append(messageKey);
            if (i.hasNext()) {
                result.append(",");
            }
        }
        result.append("]");

        return result.toString();
    }

    /**
     *  Get the message associated with a key.
     *
     *  @param messageKey
     *      The message key to retrieve the message for.
     *  @return
     *      The message associated with <code>messageKey</code>.
     *      This will never be equal to <code>null</code>.
     *      If a message is requested for a non-existent key,
     *      a message is returned with the message text equal to the key.
     */
    public Message getMessage(
            final String messageKey
    )
    {
        // The value to return.
        Message result;

        result = messagesMap.get(messageKey);
        if (result == null) {
            result = new Message(messageKey, messageKey, null);
        }

        return result;
    }

    /**
     *  Get the message associated with a numeric id.
     *
     *  <p>A a message for a numeric message id is stored in the XML file with a message key
     *      constructed by prepending "<code>ID_</code>" to the message id.
     *      The message id cannot be used verbatim
     *      because the XML construction for <code>ID</code> is
     *      a <code>Name</code>,
     *      which must begin with an alphabetic character
     *      (<i>ie</i>, an integer is not a valid XML element id attribute value).</p>
     *
     *  @param messageId
     *      The message id to retrieve the message for.
     *  @return
     *      The message associated with <code>messageId</code>.
     *      This will never be equal to <code>null</code>.
     *      If a message is requested for a non-existent id,
     *      a message is returned with the message text equal to the key.
     */
    public Message getMessage(
            final int messageId
    )
    {
        // The value to return.
        Message result;

        result = messagesMap.get("ID_" + messageId);
        if (result == null) {
            result = new Message(String.valueOf(messageId), String.valueOf(messageId), null);
        }

        return result;
    }

    /**
     *  Get the name of the subsystem for which this error messages repository applies.
     *
     *  @return
     *      The name of the subsystem.
     */
    public String getSubsystem()
    {
        return subsystem;
    }

    /**
     */
    private static String substituteFragments(
            final String rawMessage,
            final Object[] fragments
    )
    {
        // The value to return.
        final String result;

        if ((rawMessage == null) || (fragments == null)) {
            result = null;
        }
        else {
            // The fragments as a map.
            final Map<String, String> fragmentsMap = new HashMap<String, String>();

            for (int i = 0; i < fragments.length; i++) {
                fragmentsMap.put(String.valueOf(i),
                        (fragments[i] == null) ? null : String.valueOf(fragments[i]));
            }

            result = substituteFragments(rawMessage, fragmentsMap);
        }

        return result;
    }

    /**
     */
    private static String substituteFragments(
            final String rawMessage,
            final Map fragments
    )
    {
        // The value to return.
        final String result;

        if ((rawMessage == null) || (fragments == null)) {
            result = rawMessage;
        }
        else {
            final StringBuffer buf = new StringBuffer(rawMessage.length());
            // The substitution start index.
            int startIndex;
            // The substitution end index.
            int endIndex = -1;

            startIndex = rawMessage.indexOf("{", endIndex + 1);
            while (startIndex != -1) {
                buf.append(rawMessage.substring(endIndex + 1, startIndex));
                endIndex = rawMessage.indexOf("}", startIndex + 1);
                if (endIndex == -1) {
                    LOG.warn("substituteFragments: malformed message: " +
                            TraceUtil.formatObj(rawMessage));
                    buf.append("{");
                    endIndex = startIndex;
                }
                else {
                    final String fragmentName = rawMessage.substring(startIndex + 1, endIndex);
                    final String fragmentValue = String.valueOf(fragments.get(fragmentName));
                    if (fragmentValue == null) {
                        buf.append("{");
                        buf.append(fragmentName);
                        buf.append("}");
                    }
                    else {
                        buf.append(fragmentValue);
                    }
                }
                startIndex = rawMessage.indexOf("{", endIndex + 1);
            }
            buf.append(rawMessage.substring(endIndex + 1));
            result = buf.toString();
        }

        return result;
    }

    /**
     *  Models a single message.
     *
     *  <p>Each message contains a {@link #getMessageKey() message key},
     *      the {@link #getRawMessage() raw message}
     *      and a number of {@link #getFaults() faults}.</p>
     */
    public static final class Message
    implements Serializable
    {

        /**
         *  Log4j logger for this class.
         */
        @SuppressWarnings({"UnusedDeclaration"})
        final private static Logger LOG = TraceUtil.getLogger(Message.class);

        /**
         *  The current value of the <b>faults</b> property.
         */
        private final List<Fault> faults;

        /**
         *  The current value of the <b>messageKey</b> property.
         */
        private final String messageKey;

        /**
         *  The context keys for each audience.
         *  Keys of this map are objects of type {@link ErrorMessagesRepository.Audience}.
         *  Values of this map are objects of type {@link List},
         *  being the set of context keys associated with the key.
         *  Elements of the list are objects of type {@link String}.
         */
        private final Map<Audience, List<String>> contextKeysMap;

        /**
         *  The raw messages.
         *  Keys of this map are objects of type {@link ErrorMessagesRepository.Audience}.
         *  Values of this map are objects of type {@link String}.
         */
        private final Map<Audience, String> rawMessages;

        /**
         *  Create a new message object.
         *
         *  @param messageElement
         *      The XML element representing the message.
         */
        private Message(
                final Element messageElement,
                final List<String> contextKeys
        )
        {
            faults = new LinkedList<Fault>();
            messageKey = messageElement.getAttribute("id");
            rawMessages = new HashMap<Audience, String>();
            contextKeysMap = new HashMap<Audience, List<String>>();
            for (final Iterator i = DGLXMLUtils.DOM.childElements(messageElement); i.hasNext(); ) {
                final Element element = (Element)i.next();
                final String elementName = element.getNodeName();
                if (elementName.equals("messageText")) {
                    Audience audience = Audience.factory(element.getAttribute("audience"));
                    if (audience == null) {
                        audience = Audience.BOTH;
                    }
                    if (audience.equals(Audience.BOTH)) {
                        rawMessages.put(Audience.OPERATIONS,
                                DGLXMLUtils.DOM.extractText(element).trim());
                        rawMessages.put(Audience.USER,
                                DGLXMLUtils.DOM.extractText(element).trim());
                    }
                    else {
                        rawMessages.put(audience, DGLXMLUtils.DOM.extractText(element).trim());
                    }
                    final String contextKeysStr = element.getAttribute("contextKeys");
                    final List<String> myContextKeys = new LinkedList<String>(contextKeys);
                    for (final StringTokenizer st = new StringTokenizer(contextKeysStr, ",");
                            st.hasMoreTokens(); ) {
                        myContextKeys.add(st.nextToken().trim());
                    }
                    if (audience.equals(Audience.BOTH)) {
                        contextKeysMap.put(Audience.OPERATIONS, myContextKeys);
                        contextKeysMap.put(Audience.USER, myContextKeys);
                    }
                    else {
                        contextKeysMap.put(audience, myContextKeys);
                    }
                }
                else if (elementName.equals("fault")) {
                    faults.add(new Fault(element));
                }
            }
        }

        /**
         *  Create a new message object.
         *
         *  @param messageKey
         *      The message key of this message.
         *  @param rawMessage
         *      The raw message string (before substitution).
         *  @param faults
         *      The list of faults associated with this message.
         */
        private Message(
                final String messageKey,
                final String rawMessage,
                final List<ErrorMessagesRepository.Fault> faults
        )
        {
            if (faults == null) {
                this.faults = Collections.emptyList();
            }
            else {
                this.faults = faults;
            }
            this.messageKey = messageKey;
            rawMessages = new HashMap<ErrorMessagesRepository.Audience, String>();
            rawMessages.put(Audience.OPERATIONS, rawMessage);
            contextKeysMap = new HashMap<ErrorMessagesRepository.Audience, List<String>>();
        }

        /**
         *  Get the non-service-specific faults.
         *
         *  @return
         *      A list of the faults.
         *      This list contains objects of type {@link ErrorMessagesRepository.Fault}.
         *      No element of this list will be equal to <code>null</code>.
         */
        public List<ErrorMessagesRepository.Fault> getFaults()
        {
            return getFaults(null);
        }

        /**
         *  Get a list of the faults relevant to a particular service.
         *
         *  @param serviceName
         *      The name of the service to get faults for.
         *      A value of <code>null</code> retrieves faults that are not service specific.
         *  @return
         *      A list of the faults associated with <code>service</code>.
         *      This list contains objects of type {@link ErrorMessagesRepository.Fault}.
         *      No element of this list will be equal to <code>null</code>.
         */
        public List<ErrorMessagesRepository.Fault> getFaults(
                final String serviceName
        )
        {
            // The value to return.
            final List<ErrorMessagesRepository.Fault> result;

            if (DGLStringUtil.isNullOrEmpty(serviceName)) {
                result = faults;
            }
            else {
                result = new LinkedList<ErrorMessagesRepository.Fault>();
                for (Fault fault : faults) {
                    if ((fault.getServiceName() == null) ||
                            DGLStringUtil.equals(serviceName, fault.getServiceName())) {
                        result.add(fault);
                    }
                }
            }

            return Collections.unmodifiableList(result);
        }

        /**
         *  Getter method for the <b>rawMessage</b> property.
         *
         *  @return
         *      The current value of the <b>rawMessage</b> property.
         */
        public String getRawMessage()
        {
            return getRawMessage(Audience.OPERATIONS);
        }

        /**
         *  Getter method for the <b>rawMessage</b> property.
         *
         *  @param audience
         *      The audience for the required message.
         *  @return
         *      The current value of the <b>rawMessage</b> property.
         */
        public String getRawMessage(
                final Audience audience
        )
        {
            return rawMessages.get(audience);
        }

        /**
         *  Get the substituted message string.
         *
         *  @param fragments
         *      The fragments to substitute into the string.
         *  @return
         *      The message.
         */
        public String getMessage(
                final Object[] fragments
        )
        {
            return getMessage(Audience.OPERATIONS, fragments);
        }

        /**
         *  Get the substituted message string.
         *
         *  @param audience
         *      The audience for the required message.
         *  @param fragments
         *      The fragments to substitute into the string.
         *  @return
         *      The message.
         */
        public String getMessage(
                final Audience audience,
                final Object[] fragments
        )
        {
            return substituteFragments(getRawMessage(audience), fragments);
        }

        /**
         *  Get the substituted message string.
         *
         *  @param fragments
         *      The fragments to substitute into the string.
         *  @return
         *      The message.
         */
        public String getMessage(
                final Map fragments
        )
        {
            return getMessage(Audience.OPERATIONS, fragments);
        }

        /**
         *  Get the substituted message string.
         *
         *  @param audience
         *      The audience for the required message.
         *  @param fragments
         *      The fragments to substitute into the string.
         *  @return
         *      The message.
         */
        public String getMessage(
                final Audience audience,
                final Map fragments
        )
        {
            return substituteFragments(getRawMessage(audience), fragments);
        }

        /**
         *  Get the message key.
         *
         *  @return
         *      The message key.
         */
        public String getMessageKey()
        {
            return messageKey;
        }

        /**
         *  Get the context keys set.
         *
         *  @param audience
         *      The audience to get the context keys for.
         *  @return
         *      The list of context keys for his error message.
         *      Elements of this list are objects of type {@link String}.
         */
        public List<String> getContextKeys(
                final Audience audience
        )
        {
            return contextKeysMap.get(audience);
        }

    }

    /**
     *  Models a single fault.
     *
     *  <p>A single fault contains a {@link #getCause(Map) cause} and
     *      an {@link #getAction(Map) action}.
     *      They describe the possible cause for the error,
     *      and what action might be taken to correct the problem.</p>
     */
    public static final class Fault
            implements Serializable
    {

        /**
         *  Log4j logger for this class.
         */
        @SuppressWarnings({"UnusedDeclaration"})
        final private static Logger LOG = TraceUtil.getLogger(Fault.class);

        /**
         *  The current value of the <b>serviceName</b> property.
         */
        private final String serviceName;

        /**
         *  The current value of the <b>cause</b> property.
         */
        private final String cause;

        /**
         *  The current value of the <b>action</b> property.
         */
        private final String action;

        /**
         *  Make a new fault object.
         *
         *  @param faultElement
         *      The DOM element that defines the fault.
         */
        private Fault(
                final Element faultElement
        )
        {
            String cause = null;
            String action = null;
            serviceName = faultElement.getAttribute("service");
            for (final Iterator i = DGLXMLUtils.DOM.childElements(faultElement); i.hasNext(); ) {
                final Element element = (Element)i.next();
                final String elementName = element.getNodeName();
                if (elementName.equals("cause")) {
                    cause = DGLXMLUtils.DOM.extractText(element).trim();
                }
                else if (elementName.equals("action")) {
                    action = DGLXMLUtils.DOM.extractText(element).trim();
                }
            }
            this.action = action;
            this.cause = cause;
        }

        /**
         *  Getter method for the <b>action</b> property.
         *
         *  @param fragments
         *      The fragments to substitute into the string.
         *  @return
         *      The current value of the <b>action</b> property.
         *      This will never be equal to <code>null</code>.
         */
        public String getAction(
                final Map fragments
        )
        {
            return substituteFragments(action, fragments);
        }

        /**
         *  Getter method for the <b>cause</b> property.
         *
         *  @param fragments
         *      The fragments to substitute into the string.
         *  @return
         *      The current value of the <b>cause</b> property.
         *      This will never be equal to <code>null</code>.
         */
        public String getCause(
                final Map fragments
        )
        {
            return substituteFragments(cause, fragments);
        }

        /**
         *  Getter method for the <b>serviceName</b> property.
         *
         *  @return
         *      The current value of the <b>serviceName</b> property.
         *      A value of <code>null</code> indicates that this fault applies to all services.
         */
        public String getServiceName()
        {
            return serviceName;
        }

    }

    /**
     *  Enumeration of message audiences.
     */
    public static final class Audience
            implements Serializable
    {

        /**
         *  Log4j logger for this class.
         */
        @SuppressWarnings({"UnusedDeclaration"})
        private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(Audience.class);

        /**
         *  The ordinal value of {@link #OPERATIONS}.
         */
        public static final int OPERATIONS_ORD = 0;

        /**
         *  Represents the operations staff.
         */
        public static final Audience OPERATIONS = new Audience(OPERATIONS_ORD, "OPERATIONS");

        /**
         *  The ordinal value of {@link #USER}.
         */
        public static final int USER_ORD = 1;

        /**
         *  Represents the end users.
         */
        public static final Audience USER = new Audience(USER_ORD, "USER");

        /**
         *  The ordinal value of {@link #BOTH}.
         */
        public static final int BOTH_ORD = 2;

        /**
         *  Represents both audiences.
         */
        public static final Audience BOTH = new Audience(BOTH_ORD, "BOTH");

        /**
         *  The list of known values.
         */
        private static final List<Audience> KNOWN_VALUES;
        static {
            final List<Audience> knownValues = new LinkedList<Audience>();
            knownValues.add(OPERATIONS);
            knownValues.add(USER);
            knownValues.add(BOTH);
            KNOWN_VALUES = Collections.unmodifiableList(knownValues);
        }

        /**
         *  The ordinal value of this audience object.
         */
        private final int ord;

        /**
         *  The name of this audience object.
         */
        private final String name;

        /**
         *  Make a new audience object.
         *
         *  @param ord
         *      The ordinal value.
         *  @param name
         *      The name of the audience object.
         */
        public Audience(
                final int ord,
                final String name
        )
        {
            this.ord = ord;
            this.name = name;
        }

        /**
         */
        public boolean equals(
                final Object other
        )
        {
            return (other != null) &&
                    other.getClass().equals(getClass()) &&
                    (((Audience)other).ord == ord);
        }

        /**
         *  @return
         *      The ordinal value.
         */
        public int hashCode()
        {
            return ord;
        }

        /**
         *  Get the ordinal value of the audience.
         *
         *  @return
         *      The ordinal value of this object.
         */
        @SuppressWarnings({"UnusedDeclaration"})
        public int ord()
        {
            return ord;
        }

        /**
         *  Get the name of this audience object.
         *
         *  @return
         *      The name of this audience object.
         */
        public String getName()
        {
            return name;
        }

        /**
         *  @return
         *      The name of this audience object.
         */
        public String toString()
        {
            return name;
        }

        /**
         *  Get a list of the known values.
         *
         *  @return
         *      A list of the known values.
         *      Elements of this list are objects of type {@link ErrorMessagesRepository.Audience}.
         */
        public static List<Audience> getValues()
        {
            return KNOWN_VALUES;
        }

        /**
         *  Get an audience object from a string.
         *
         *  @param name
         *      The name of the audience object to return.
         *  @return
         *      The audience object corresponding to <code>name</code>
         *      or <code>null</code> if there is no such audience object.
         */
        public static Audience factory(
                final String name
        )
        {
            // The value to return.
            Audience result = null;

            for (final Audience audience : getValues()) {
                if (audience.getName().equals(name)) {
                    result = audience;
                    break;
                }
            }

            return result;
        }

    }

}
