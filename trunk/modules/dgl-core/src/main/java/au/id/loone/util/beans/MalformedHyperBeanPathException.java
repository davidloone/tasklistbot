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

package au.id.loone.util.beans;

import au.id.loone.util.tracing.TraceUtil;

/**
 * @author David G Loone
 */
public final class MalformedHyperBeanPathException
        extends HyperBeanPathException
{

    /**
     * Log4j logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(MalformedHyperBeanPathException.class);

    /**
     */
    public MalformedHyperBeanPathException(
            final String path,
            final Integer position
    )
    {
        super(path, position);
    }

}
