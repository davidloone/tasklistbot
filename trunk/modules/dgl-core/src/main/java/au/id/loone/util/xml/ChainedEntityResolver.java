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

package au.id.loone.util.xml;

import org.xml.sax.EntityResolver;

/**
 *  Interface for entity resolvers that can be chained together.
 *
 *  <p>A chained entity resolver will,
 *      if it cannot resolver a given entity,
 *      delegate to the next entity resolver in the chain.
 *      This interface defines setter and getter methods
 *      for manipulating this next entity resolver in the chain.</p>
 *
 *  @author David G Loone
 */
public interface ChainedEntityResolver
        extends EntityResolver
{

    /**
     *  Setter method for the <b>entityResolver</b> property.
     */
    public void setEntityResolver(
            EntityResolver entityResolver
    );

    /**
     *  Getter method for the <b>entityResolver</b> property.
     */
    public EntityResolver getEntityResolver();

}