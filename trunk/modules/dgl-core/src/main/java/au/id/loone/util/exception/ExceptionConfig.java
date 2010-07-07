/*
 * Copyright 2009, David G Loone
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

import au.id.loone.util.tracing.TraceUtil;
import au.id.loone.util.config.ConfigData;

/**
 * Exception handling configuration data.
 *
 * @author David G Loone
 */
public final class ExceptionConfig
        extends ConfigData
{

    /**
     * Log4j logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(ExceptionConfig.class);

    /**
     * Current value of the <b>stackTracePrefixAtom</b> property.
     */
    private String stackTracePrefixAtom;

    /**
     */
    public ExceptionConfig()
    {
        super();
    }

    /**
     */
    public static ExceptionConfig factory()
    {
        return new ExceptionConfig();
    }

    /**
     * Setter for the <b>stackTracePrefixAtom</b> property.
     */
    public void setStackTracePrefixAtom(
        final String stackTracePrefixAtom
    )
    {
        this.stackTracePrefixAtom = stackTracePrefixAtom;
    }

    /**
     * Getter method for the <b>stackTracePrefixAtom</b> property.
     */
    public String getStackTracePrefixAtom()
    {
        return stackTracePrefixAtom;
    }

}
