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

/**
 *  General purpose runtime exception.
 *
 *  @author David G Loone
 */
public class DGLRuntimeException
extends RuntimeException
{

    /**
     *  Make a new runtime exception object.
     */
    protected DGLRuntimeException()
    {
        super();
    }

    /**
     *  Make a new runtime exception object.
     *
     *  @return
     *      The exception object.
     */
    public static DGLRuntimeException factory()
    {
        // The value to return.
        DGLRuntimeException result;

        result = new DGLRuntimeException();
        result.fillInStackTrace();

        return result;
    }

    /**
     *  Make a new runtime exception object.
     *
     *  @param message
     *      The exception message.
     */
    protected DGLRuntimeException(
            String message
    )
    {
        super(message);
    }

    /**
     *  Make a new runtime exception object.
     *
     *  @param message
     *      The exception message.
     *  @return
     *      The exception object.
     */
    public static DGLRuntimeException factory(
            String message
    )
    {
        // The value to return.
        DGLRuntimeException result;

        result = new DGLRuntimeException(message);
        result.fillInStackTrace();

        return result;
    }

    /**
     *  Make a new runtime exception object.
     *
     *  @param rootCause
     *      The root cause of this exception,
     *      or <code>null</code> if there is no known root cause exception.
     */
    protected DGLRuntimeException(
            Throwable rootCause
    )
    {
        super(rootCause);
    }

    /**
     *  Make a new runtime exception object.
     *
     *  @param rootCause
     *      The root cause of this exception,
     *      or <code>null</code> if there is no known root cause exception.
     *  @return
     *      The exception object.
     */
    public static DGLRuntimeException factory(
            Throwable rootCause
    )
    {
        // The value to return.
        DGLRuntimeException result;

        result = new DGLRuntimeException(rootCause);
        result.fillInStackTrace();

        return result;
    }

    /**
     *  Make a new runtime exception object.
     *
     *  @param message
     *      The exception message.
     *  @param rootCause
     *      The root cause of this exception,
     *      or <code>null</code> if there is no known root cause exception.
     */
    protected DGLRuntimeException(
            String message,
            Throwable rootCause
    )
    {
        super(message, rootCause);
    }

    /**
     *  Make a new runtime exception object.
     *
     *  @param message
     *      The exception message.
     *  @param rootCause
     *      The root cause of this exception,
     *      or <code>null</code> if there is no known root cause exception.
     *  @return
     *      The exception object.
     */
    public static DGLRuntimeException factory(
            String message,
            Throwable rootCause
    )
    {
        // The value to return.
        DGLRuntimeException result;

        result = new DGLRuntimeException(message, rootCause);
        result.fillInStackTrace();

        return result;
    }

}