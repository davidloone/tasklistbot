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

package au.id.loone.util;

import java.util.Properties;

import au.id.loone.util.tracing.TraceUtil;

import org.apache.log4j.Logger;

/**
 *  Environment detection/naming.
 *
 *  @author David G Loone
 */
public final class Env
{

    /**
     *  Logger for this class.
     */
    final private static Logger LOG = TraceUtil.getLogger(Env.class);

    /**
     *  The name of the environment configuration properties resource,
     *  "<code>envConfig.properties</code>".
     */
    public final static String ENV_CONFIG_PROPSFILE = "envConfig.properties";

    /**
     *  The property name of the environment name,
     *  "<code>au.id.loone.tracing.envName</code>".
     */
    public final static String ENV_NAME_PROPNAME = "au.id.loone.util.envName";

    /**
     *  The environment name.
     */
    private final static String ENV_NAME;

    /**
     *  Initialise the environment name.
     */
    static {
        // The environment name.
        String envName;
        // The environment config properties.
        Properties envProps;

        try {
            envProps = ConfigResource.factory(TraceUtil.class, ENV_CONFIG_PROPSFILE).getProperties();

            envName = System.getProperty(ENV_NAME_PROPNAME);
            if (envName == null) {
                envName = envProps.getProperty(ENV_NAME_PROPNAME);
            }
        }
        catch (final ResourceNotFoundException e) {
            LOG.info("(static): resource " +
                    TraceUtil.formatObj(ENV_CONFIG_PROPSFILE) +
                    " not found, setting environment to \"dev\"");
            envName = "dev";
        }

        ENV_NAME = envName;
        LOG.info("(static): ENV_NAME = " + TraceUtil.formatObj(ENV_NAME));
    }

    /**
     *  This class only contains static methods,
     *  so cannot be instantiated.
     */
    private Env()
    {
        super();
    }

    /**
     *  Get the environment name.
     *
     *  @return
     *      The environment name.
     */
    public static String getEnvName()
    {
        return ENV_NAME;
    }

}