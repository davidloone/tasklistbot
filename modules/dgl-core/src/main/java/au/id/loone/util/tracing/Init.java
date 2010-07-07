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

package au.id.loone.util.tracing;

import java.net.URL;

import au.id.loone.util.ConfigResource;
import au.id.loone.util.ResourceNotFoundException;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

/**
 *  Provides initialisation for the trace logging facility.
 *
 *  <p>Loading this class initialises the trace logging facility.
 *      See the package documentation for recommendations
 *      on how to instrument the source code to perform trace logging.</p>
 *
 *  @author David G Loone
 */
public final class Init
{

    /**
     *  The name of the config file,
     *  "<code>config.xml</code>".
     */
    public static final String[] CONFIG_RESOURCE_NAME = new String[] {
            "/log4j.xml",
            "/Log4j.xml",
            "config.xml",
            "/log4j.properties",
            "/Log4j.properties",
            "config.properties"
    };

    /**
     *  Initialise the trace logging facility.
     */
    static {
        boolean configured = false;

        for (final String configResourceName: CONFIG_RESOURCE_NAME) {
            try {
                final URL configResource = ConfigResource.factory(Init.class, configResourceName).getUrl();
                if (configResourceName.endsWith(".xml")) {
                    DOMConfigurator.configure(configResource);
                }
                else if (configResourceName.endsWith(".properties")) {
                    PropertyConfigurator.configure(configResource);
                }
                else {
                    // Something else, do nothing.
                }
                configured = true;
            }
            catch (final ResourceNotFoundException e) {
                // Do nothing.
            }
            catch (final Throwable e) {
                // If anything untoward happens here, print the stacktrace.
                System.out.println("*** Error during Log4j initialisation: " + e.getClass() + ":" + e.getMessage());
                e.printStackTrace(System.out);
                System.out.println("*** Error ignored, Log4j not initialised.");
            }

            if (configured) {
                break;
            }
        }
    }

    /**
     *  This class only contains static methods,
     *  so cannot be instantiated.
     */
    public Init ()
    {
        super();
    }

}