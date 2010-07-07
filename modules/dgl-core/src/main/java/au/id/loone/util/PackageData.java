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

package au.id.loone.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import au.id.loone.util.reflist.RefItem;

/**
 * A version of {@link com.thoughtworks.xstream.annotations.XStreamInclude}
 * that can be placed on a package.
 *
 * @author David G Loone
 */
@Retention(
        value = RetentionPolicy.RUNTIME
)
@SuppressWarnings({"UnusedDeclaration"})
@Target(
        value = {ElementType.PACKAGE}
)
public @interface PackageData
{

    /**
     * Bean util converter classes.
     */
    BeanUtilConverterSpec[] beanUtilConverters()
            default {};

    /**
     * Classes that have hibernate annotations.
     */
    Class[] hibernateAnnotatedClasses()
            default {};

    /**
     * Names of packages that contain this annotation.
     */
    String[] packageNames()
            default {};

    /**
     * Reference item classes.
     */
    Class<? extends RefItem>[] refItemClasses()
            default {};

    /**
     * Classes that contain XStream annotations.
     */
    Class[] xstreamClasses()
            default {};

//    /**
//     * Classes that should be registered as XStream converters.
//     */
//    Class<? extends Converter>[] xstreamConverterClasses()
//            default {};

    /**
     */
    @Retention(
            value = RetentionPolicy.RUNTIME
    )
    @Target(
            value = {ElementType.PACKAGE}
    )
    public @interface BeanUtilConverterSpec
    {

        /**
         * The converter class.
         */
        Class<? extends org.apache.commons.beanutils.Converter> converterClass();

        /**
         * The target class.
         */
        Class targetClass();

    }

}