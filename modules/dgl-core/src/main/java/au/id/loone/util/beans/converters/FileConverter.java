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

package au.id.loone.util.beans.converters;

import java.io.File;

import au.id.loone.util.tracing.TraceUtil;

import org.apache.commons.beanutils.Converter;

/**
 * Property converter to convert string to {@link java.io.File},
 * that understands the Unix home directory notation (ie, ~).
 *
 * @author David G Loone
 */
public class FileConverter
        implements Converter
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(FileConverter.class);

    /**
     * The user home directory.
     */
    private static final File USER_HOME;
    static {
        // The user.home system property might not be present, eg GAE.
        final String userHome = System.getProperty("user.home");
        USER_HOME = (userHome == null) ? null : new File(userHome);
    }

    /**
     */
    public FileConverter()
    {
        super();
    }

    /**
     */
    public Object convert(
            final Class type,
            final Object value
    )
    {
        final File result;

        final String path = (String)value;
        if (path.startsWith("~")) {
            if (USER_HOME == null) {
                throw new IllegalArgumentException("User home directory (~) not available for path \"" +
                        path + "\".");
            }
            else {
                result = new File(USER_HOME, path.substring(1));
            }
        }
        else {
            result = new File(path);
        }

        return result;
    }

}

