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

package au.id.loone.util.reflist;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Annotates an object as an instnace of a static reference item.
 *
 * <p>This annotation should be applied to any field of a {@linkplain RefItem reference item} class
 *      that is an instance of the reference list.
 *      Where the field is not set explicitly,
 *      it tells the reference item framework to set the value of the field
 *      after all the reference items have been loaded,
 *      and optionally provides the code of the reference item to set it to.</p>
 *
 * @author David G Loone
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RefItemInstance
{

    /**
     * @return
     *      The {@linkplain RefItem#getCode() code} of the reference item that is to be assigned to this field.
     *      This is ignored if the field is assigned explicitly.
     *      If the value is an empty string,
     *      then the name of the field is used as the code of the item to be assigned to it.
     */
    String code() default "";

}
