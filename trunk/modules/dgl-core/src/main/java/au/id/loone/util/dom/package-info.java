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

@au.id.loone.util.PackageData(
        beanUtilConverters = {
                @au.id.loone.util.PackageData.BeanUtilConverterSpec(
                        converterClass = TimeOfDay_Converter.class,
                        targetClass = TimeOfDay.class
                ),
                @au.id.loone.util.PackageData.BeanUtilConverterSpec(
                        converterClass = Duration_Converter.class,
                        targetClass = Duration.class
                )
        },
        hibernateAnnotatedClasses = {
                Duration_HibernateUserType.class,
                TimeOfDay_HibernateUserType.class
        },
        xstreamClasses = {
                Duration_XStreamConverter.class,
                TimeOfDay_XStreamConverter.class
        }
)
@org.hibernate.annotations.TypeDefs(
        value = {
                @org.hibernate.annotations.TypeDef(
                        name = "au.id.loone.util.dom.TimeOfDay",
                        typeClass = au.id.loone.util.dom.TimeOfDay.class
                ),
                @org.hibernate.annotations.TypeDef(
                        name = "au.id.loone.util.dom.Duration",
                        typeClass = au.id.loone.util.dom.Duration.class
                )
        }
)
package au.id.loone.util.dom;
