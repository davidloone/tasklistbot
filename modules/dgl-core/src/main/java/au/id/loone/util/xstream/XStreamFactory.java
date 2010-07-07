/*
 * Copyright 2010, David G Loone
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

package au.id.loone.util.xstream;

import au.id.loone.util.DGLStringUtil;
import au.id.loone.util.PackageScanner;
import au.id.loone.util.tracing.TraceUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * A factory for configured {@link XStream} instances.
 *
 * <h3>JavaBean Properties</h3>
 *
 * <table align="center" border="1" cellspacing="0" cellpadding="2" width="90%">
 *      <tr>
 *          <th>name</th>
 *          <th>description</th>
 *      </tr>
 *      <tr>
 *          <td align="left" valign="top"><b>annotatedClasses</b></td>
 *          <td align="left" valign="top">A list of xstreamClasses that have XStream annotations.</td>
 *      </tr>
 *      <tr>
 *          <td align="left" valign="top"><b>annotatedPackageNames</b></td>
 *          <td align="left" valign="top">A list of names of packages
 *              that have the {@link au.id.loone.util.PackageData} annotation.</td>
 *      </tr>
 *      <tr>
 *          <td align="left" valign="top"><b>refItemClasses</b></td>
 *          <td align="left" valign="top">A list of reference item xstreamClasses.
 *              Each such class will have an XStream converter registered.</td>
 *      </tr>
 * </table>
 *
 * @author David G Loone
 */
@SuppressWarnings({"UnusedDeclaration"})
public final class XStreamFactory
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(XStreamFactory.class);

    /**
     * The singleton instance.
     */
    private static XStreamFactory INSTANCE;

    /**
     * Current value of the <b>mode</b> property.
     */
    private String mode;

    /**
     * Current value of the <b>packageScanner</b> property.
     */
    private PackageScanner packageScanner;

    /**
     */
    public XStreamFactory()
    {
        super();

        INSTANCE = this;
    }

    /**
     * An alternative way of accessing the previously constructed instance.
     */
    public static XStreamFactory factory()
    {

        return INSTANCE;
    }

    /**
     */
    public XStream getXstream()
    {
        final XStream result;

        result = new XStream();
        if (DGLStringUtil.equals(mode, "XPATH_RELATIVE_REFERENCES")) {
            result.setMode(XStream.XPATH_RELATIVE_REFERENCES);
        }
        else if (DGLStringUtil.equals(mode, "XPATH_ABSOLUTE_REFERENCES")) {
            result.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        }
        else if (DGLStringUtil.equals(mode, "ID_REFERENCES")) {
            result.setMode(XStream.ID_REFERENCES);
        }
        else if (DGLStringUtil.equals(mode, "NO_REFERENCES")) {
            result.setMode(XStream.NO_REFERENCES);
        }

        if (packageScanner.getXstreamClasses() != null) {
            for (final Class xstreamClass : packageScanner.getXstreamClasses()) {
                result.processAnnotations(xstreamClass);
            }
            for (final Class<? extends Converter> xstreamConverterClass : packageScanner.getXstreamConverterClasses()) {
                try {
                    result.registerConverter(xstreamConverterClass.newInstance());
                }
                catch (final InstantiationException e) {
                    LOG.warn("getXstream: " + TraceUtil.formatObj(e), e);
                }
                catch (final IllegalAccessException e) {
                    LOG.warn("getXstream: " + TraceUtil.formatObj(e), e);
                }
            }
        }

        return result;
    }

    /**
     * Setter for the <b>mode</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public void setMode(final String mode) {this.mode = mode;}

    /**
     * Getter method for the <b>mode</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public String getMode() {return mode;}

    /**
     * Setter for the <b>packageScanner</b> property.
     */
    @Required
    public void setPackageScanner(final PackageScanner packageScanner) {this.packageScanner = packageScanner;}

}