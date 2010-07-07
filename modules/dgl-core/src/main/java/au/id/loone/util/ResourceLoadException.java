package au.id.loone.util;

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

import au.id.loone.util.tracing.TraceUtil;

/**
 *  Runtime exception thrown when an exception occurs while loading or processing a resource.
 *
 *  @author David G Loone
 */
public final class ResourceLoadException
        extends DGLRuntimeException
{

    /**
     *  The path to the resource that could not be found.
     */
    private String itsPath;

    /**
     *  Don't allow null instantiations.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private ResourceLoadException()
    {
        super();
    }

    /**
     *  Create a new exception object.
     *
     *  @param path
     *      The name of the resource that we were looking for.
     */
    public ResourceLoadException(
            String path
    )
    {
        super();
        itsPath = path;
    }

    /**
     *  Create a new exception object.
     *
     *  @param path
     *      The name of the resource that we were looking for.
     *  @param rootCause
     *      The root cause exception.
     */
    public ResourceLoadException(
            String path,
            Throwable rootCause
    )
    {
        super(rootCause);
        itsPath = path;
    }

    /**
     *  Get the exception message.
     */
    public String getMessage()
    {
        return "Could not load resource " + TraceUtil.formatObj(itsPath);
    }

}
