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

import au.id.loone.util.config.ConfigData;
import au.id.loone.util.tracing.TraceUtil;

/**
 *  Constants for the reflist subsystem.
 *
 *  @author David G Loone
 */
public class RefListConstants
        extends ConfigData
{

    /**
     *  Log4j logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(RefListConstants.class);

    /**
     *  Current value of the <b>validateRefListDoc</b> property.
     */
    private boolean validateRefListDoc;

    /**
     */
    private RefListConstants()
    {
        super();
    }

    /**
     *  Setter method for the <b>validateRefListDoc</b> property.
     */
    public void setValidateRefListDoc(
            final boolean validateRefListDoc
    )
    {
        this.validateRefListDoc = validateRefListDoc;
    }

    /**
     *  Getter method for the <b>validateRefListDoc</b> property.
     */
    public boolean getValidateRefListDoc()
    {
        return validateRefListDoc;
    }

    /**
     */
    public static RefListConstants factory()
    {
        return new RefListConstants();
    }

}
