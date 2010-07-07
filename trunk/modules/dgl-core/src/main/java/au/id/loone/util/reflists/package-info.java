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
        refItemClasses = {
                BooleanRI.class,
                CountryRI.class,
                DayOfWeekRI.class,
                MeridiemRI.class,
                MonthOfYearRI.class,
                MoonRI.class,
                NoYesRI.class,
                PlanetRI.class
        }
)
@org.hibernate.annotations.TypeDefs(
        value = {
                @org.hibernate.annotations.TypeDef(
                        name = "au.id.loone.util.reflists.BooleanRI",
                        parameters = {
                                @org.hibernate.annotations.Parameter(
                                        name = "refItemClassName",
                                        value = "au.id.loone.util.reflists.BooleanRI"
                                )
                        },
                        typeClass = au.id.loone.util.reflist.HibernateUserType.class
                ),
                @org.hibernate.annotations.TypeDef(
                        name = "au.id.loone.util.reflists.CountryRI",
                        parameters = {
                                @org.hibernate.annotations.Parameter(
                                        name = "refItemClassName",
                                        value = "au.id.loone.util.reflists.CountryRI"
                                )
                        },
                        typeClass = au.id.loone.util.reflist.HibernateUserType.class
                ),
                @org.hibernate.annotations.TypeDef(
                        name = "au.id.loone.util.reflists.DayOfWeekRI",
                        parameters = {
                                @org.hibernate.annotations.Parameter(
                                        name = "refItemClassName",
                                        value = "au.id.loone.util.reflists.DayOfWeekRI"
                                )
                        },
                        typeClass = au.id.loone.util.reflist.HibernateUserType.class
                ),
                @org.hibernate.annotations.TypeDef(
                        name = "au.id.loone.util.reflists.MeridiemRI",
                        parameters = {
                                @org.hibernate.annotations.Parameter(
                                        name = "refItemClassName",
                                        value = "au.id.loone.util.reflists.MeridiemRI"
                                )
                        },
                        typeClass = au.id.loone.util.reflist.HibernateUserType.class
                ),
                @org.hibernate.annotations.TypeDef(
                        name = "au.id.loone.util.reflists.MonthOfYearRI",
                        parameters = {
                                @org.hibernate.annotations.Parameter(
                                        name = "refItemClassName",
                                        value = "au.id.loone.util.reflists.MonthOfYearRI"
                                )
                        },
                        typeClass = au.id.loone.util.reflist.HibernateUserType.class
                ),
                @org.hibernate.annotations.TypeDef(
                        name = "au.id.loone.util.reflists.MoonRI",
                        parameters = {
                                @org.hibernate.annotations.Parameter(
                                        name = "refItemClassName",
                                        value = "au.id.loone.util.reflists.MoonRI"
                                )
                        },
                        typeClass = au.id.loone.util.reflist.HibernateUserType.class
                ),
                @org.hibernate.annotations.TypeDef(
                        name = "au.id.loone.util.reflists.PlanetRI",
                        parameters = {
                                @org.hibernate.annotations.Parameter(
                                        name = "refItemClassName",
                                        value = "au.id.loone.util.reflists.PlanetRI"
                                )
                        },
                        typeClass = au.id.loone.util.reflist.HibernateUserType.class
                ),
                @org.hibernate.annotations.TypeDef(
                        name = "au.id.loone.util.reflists.YesNoRI",
                        parameters = {
                                @org.hibernate.annotations.Parameter(
                                        name = "refItemClassName",
                                        value = "au.id.loone.util.reflists.YesNoRI"
                                )
                        },
                        typeClass = au.id.loone.util.reflist.HibernateUserType.class
                )
        }
)
package au.id.loone.util.reflists;
