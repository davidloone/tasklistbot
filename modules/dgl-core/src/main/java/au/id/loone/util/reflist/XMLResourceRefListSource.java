/*
 * Copyright 2007, David G Loone
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

package au.id.loone.util.reflist;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;

import au.id.loone.util.ConfigResource;
import au.id.loone.util.DGLStringUtil;
import au.id.loone.util.DGLUtil;
import au.id.loone.util.ResourceLoadException;
import au.id.loone.util.reflist.transobj.RefListItemIndexedPropertyTO;
import au.id.loone.util.reflist.transobj.RefListItemNonIndexedPropertyTO;
import au.id.loone.util.reflist.transobj.RefListItemPropertyTO;
import au.id.loone.util.reflist.transobj.RefListItemTO;
import au.id.loone.util.reflist.transobj.RefListPropertyDefTO;
import au.id.loone.util.reflist.transobj.RefListTO;
import au.id.loone.util.tracing.TraceUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * A reference list source for values stored in XML resources.
 *
 * @author David G Loone
 */
public final class XMLResourceRefListSource
        implements ExternalRefListSourceDef
{

    /**
     * Log4j logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(XMLResourceRefListSource.class);

    /**
     * The namespace URI.
     */
    public static final String NAMESPACE_URI = "http://loone.id.au/xml/refList_v1.xsd";

    /**
     */
    public XMLResourceRefListSource()
    {
        super();
    }

    /**
     * @param refItemClass
     *      The reference item class being initialised or refreshed.
     */
    public RefListTO retrieve(
            final Class<? extends ExternalRefItem> refItemClass
    )
    {
        if (LOG.isDebugEnabled()) {
            LOG.trace("refresh(" + TraceUtil.formatObj(refItemClass) + ")");
        }

        final RefListConstants constants = RefListConstants.factory();

        final RefListTO refList;

        final ConfigResource configResource = ConfigResource.factory(refItemClass, refItemClass, "xml");
        try {
            refList = new RefListTO();
            refList.setImplClass(refItemClass);

            // Read the XML document. We could do this with a higher level tool (like XML Beans), but we do it
            // manually instead to avoid extra dependencies.
            final Document doc = configResource.asXMLDocument(constants.getValidateRefListDoc());
            final Element refListEl = doc.getDocumentElement();
            final String refListNS = refListEl.getNamespaceURI();
            refList.setDescription(refListEl.getAttribute("description"));
            refList.setName(refListEl.getAttribute("name"));
            refList.setVersion(refListEl.getAttribute("version"));
            if (LOG.isDebugEnabled()) {
                LOG.trace("refresh: " + TraceUtil.formatObj(refList, "refList.description"));
                LOG.trace("refresh: " + TraceUtil.formatObj(refList, "refList.name"));
                LOG.trace("refresh: " + TraceUtil.formatObj(refList, "refList.version"));
            }

            // Read the "implementation" element, and the Java implementation class contained therein (there could
            // be other implementation elements, but we don't care about them).
            final Element implementationEl =
                    (Element)refListEl.getElementsByTagNameNS(refListNS, "implementation").item(0);
            final NodeList javaNodes = implementationEl.getElementsByTagNameNS(refListNS, "java");
            if (javaNodes.getLength() == 0) {
                refList.setImplClassName(null);
            }
            else {
                final Element javaEl = (Element)javaNodes.item(0);
                refList.setImplClassName(javaEl.getAttribute("class"));
                if (!DGLUtil.equals(refList.getImplClassName(), refItemClass.getName())) {
                    throw new ResourceLoadException("The reference item class being loaded (" +
                            refItemClass.getName() +
                            ") is different to the implementation class specified in the content file (" +
                            refList.getImplClassName() + ").");
                }
            }
            LOG.trace("refresh: " + TraceUtil.formatObj(refList, "refList.implClassName"));

            // Read the "propertyDefs" element and the enclosed property definitions.
            final List<RefListPropertyDefTO> propertyDefs = new LinkedList<RefListPropertyDefTO>();
            final Element propertyDefsEl =
                    (Element)refListEl.getElementsByTagNameNS(refListNS, "propertyDefs").item(0);
            final NodeList propertyDefNodes = propertyDefsEl.getElementsByTagNameNS(refListNS, "propertyDef");
            for (int i = 0; i < propertyDefNodes.getLength(); i++) {
                final Element propertyDefEl = (Element)propertyDefNodes.item(i);
                final RefListPropertyDefTO propertyDef = new RefListPropertyDefTO();
                propertyDef.setIsIndexed(DGLStringUtil.parseBoolean(propertyDefEl.getAttribute("indexed")));
                propertyDef.setName(propertyDefEl.getAttribute("name"));
                propertyDef.setType(new QName(propertyDefEl.getAttribute("type")));
                LOG.trace("refresh: " + TraceUtil.formatObj(propertyDef, "propertyDef.name"));
                LOG.trace("refresh: " + TraceUtil.formatObj(propertyDef, "propertyDef.isIndexed"));
                LOG.trace("refresh: " + TraceUtil.formatObj(propertyDef, "propertyDef.type"));
                propertyDefs.add(propertyDef);
            }
            refList.setPropertyDefs(propertyDefs);

            // Read the "item" elements.
            final List<RefListItemTO> items = new LinkedList<RefListItemTO>();
            final NodeList itemNodes = refListEl.getElementsByTagNameNS(refListNS, "item");
            for (int i = 0; i < itemNodes.getLength(); i++) {
                final Element itemEl = (Element)itemNodes.item(i);
                final RefListItemTO item = new RefListItemTO();
                item.setCode(itemEl.getAttribute("code"));
                item.setDescription(itemEl.getAttribute("description"));
                item.setObsolete(DGLStringUtil.parseBoolean(itemEl.getAttribute("obsolete")));
                item.setOrd(i);

                // Read the item sub-elements.
                final Set<RefListItemPropertyTO> properties = new HashSet<RefListItemPropertyTO>();
                final Set<String> aliases = new HashSet<String>();
                final NodeList itemChildren = itemEl.getChildNodes();
                for (int j = 0; j < itemChildren.getLength(); j++) {
                    final Node itemChildNode = itemChildren.item(j);
                    if ((itemChildNode instanceof Element) &&
                            DGLStringUtil.equals(itemChildNode.getNamespaceURI(), NAMESPACE_URI) &&
                            DGLStringUtil.equals(itemChildNode.getLocalName(), "property")) {
                        // Have a non-indexed property.
                        final Element propertyEl = (Element)itemChildNode;
                        final RefListItemNonIndexedPropertyTO property = new RefListItemNonIndexedPropertyTO();
                        property.setName(propertyEl.getAttribute("name"));

                        // Read the property attribute.
                        String propertyValue = propertyEl.getAttribute("value");
                        if (propertyValue == null) {
                            // Read the property sub-element. The DTD says there should be exactly one of them.
                            final NodeList propertyValueEls = propertyEl.getElementsByTagNameNS(refListNS, "propertyValue");
                            final Element propertyValueEl = (Element)propertyValueEls.item(0);
                            if (propertyValueEl != null) {
                                propertyValue = propertyValueEl.getTextContent();
                            }
                        }
                        property.setValue(DGLStringUtil.trim(propertyValue));
                        properties.add(property);
                        LOG.trace("refresh: " + TraceUtil.formatObj(property, "property"));
                    }
                    else if ((itemChildNode instanceof Element) &&
                            DGLStringUtil.equals(itemChildNode.getNamespaceURI(), NAMESPACE_URI) &&
                            DGLStringUtil.equals(itemChildNode.getLocalName(), "indexedProperty")) {
                        // Have an indexed property.
                        final Element propertyEl = (Element)itemChildNode;
                        final RefListItemIndexedPropertyTO property = new RefListItemIndexedPropertyTO();
                        property.setName(propertyEl.getAttribute("name"));

                        // Read the property sub-elements. The DTD says there should be zero or more of them.
                        final NodeList propertyValueEls = propertyEl.getElementsByTagNameNS(refListNS, "propertyValue");
                        final String[] propertyValues = new String[propertyValueEls.getLength()];
                        for (int k = 0; k < propertyValueEls.getLength(); k++) {
                            final Element propertyValueEl = (Element)propertyValueEls.item(k);
                            propertyValues[k] = DGLStringUtil.trim(propertyValueEl.getTextContent());
                        }
                        property.setValues(propertyValues);
                        properties.add(property);
                        LOG.trace("refresh: " + TraceUtil.formatObj(property, "property"));
                    }
                    else if ((itemChildNode instanceof Element) &&
                            DGLStringUtil.equals(itemChildNode.getNamespaceURI(), NAMESPACE_URI) &&
                            DGLStringUtil.equals(itemChildNode.getLocalName(), "alias")) {
                        // Have an alias.
                        final Element aliasEl = (Element)itemChildNode;
                        final String aliasValue = aliasEl.getAttribute("value");
                        LOG.trace("refresh: " + TraceUtil.formatObj(aliasValue, "aliasValue"));
                        aliases.add(aliasValue);
                    }
                }
                item.setProperties(properties);
                item.setAliases(aliases);
                items.add(item);

                LOG.trace("refresh: " + TraceUtil.formatObj(item, "item.code"));
                LOG.trace("refresh: " + TraceUtil.formatObj(item, "item.description"));
                LOG.trace("refresh: " + TraceUtil.formatObj(item, "item.properties"));
                LOG.trace("refresh: " + TraceUtil.formatObj(item, "item.aliases"));
            }
            refList.setItems(items);
        }
        catch (final IOException e) {
            throw new ResourceLoadException(configResource.getUrl().toString(), e);
        }
        catch (final SAXException e) {
            throw new ResourceLoadException(configResource.getUrl().toString(), e);
        }

        LOG.trace("~refresh = ...");
        return refList;
    }

}
