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

package au.id.loone.util.reflists;

import java.util.Collection;
import java.util.Set;

import au.id.loone.util.tracing.TraceUtil;
import au.id.loone.util.reflist.ExternalRefItem;
import au.id.loone.util.reflist.ExternalRefListSource;
import au.id.loone.util.reflist.RefItem;
import au.id.loone.util.reflist.RefItemInstance;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Reference list for planets of the solar system.
 *
 * @author David G Loone
 */
@ExternalRefListSource(
        sourceClass = au.id.loone.util.reflist.XMLResourceRefListSource.class
)
public final class PlanetRI
        extends ExternalRefItem
        implements RefItem, IsSerializable
{

    @RefItemInstance
    @SuppressWarnings({"UnusedDeclaration"})
    public static PlanetRI MERCURY;

    @RefItemInstance
    @SuppressWarnings({"UnusedDeclaration"})
    public static PlanetRI VENUS;

    @RefItemInstance
    @SuppressWarnings({"UnusedDeclaration"})
    public static PlanetRI EARTH;

    @RefItemInstance
    @SuppressWarnings({"UnusedDeclaration"})
    public static PlanetRI MARS;

    @RefItemInstance
    @SuppressWarnings({"UnusedDeclaration"})
    public static PlanetRI JUPITER;

    @RefItemInstance
    @SuppressWarnings({"UnusedDeclaration"})
    public static PlanetRI SATURN;

    @RefItemInstance
    @SuppressWarnings({"UnusedDeclaration"})
    public static PlanetRI URANUS;

    @RefItemInstance
    @SuppressWarnings({"UnusedDeclaration"})
    public static PlanetRI NEPTUNE;

    @RefItemInstance
    @SuppressWarnings({"UnusedDeclaration"})
    public static PlanetRI PLUTO;

//    static {
//        for (final PlanetRI planet : PlanetRI.getAllItems()) {
//            for (final MoonRI moon : planet.getMoons()) {
//                moon.planet = planet;
//            }
//        }
//    }

    /**
     * An empty array.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static final PlanetRI[] EMPTY_ARRAY = new PlanetRI[] {};

    /**
     * Current value of the <b>diameter</b> property.
     */
    private double diameter;

    /**
     * Current value of the <b>mass</b> property.
     */
    private double mass;

    /**
     * Current value of the <b>moons</b> property.
     */
    private MoonRI[] moons;

    /**
     * Current value of the <b>orbit</b> property.
     */
    private double orbit;

    /**
     */
    public PlanetRI(
            final String code,
            final String description,
            final boolean obsolete,
            final Set<String> aliases
    )
    {
        super(code, description, obsolete, aliases);
    }

    /**
     */
    public static PlanetRI factory(
            final String code
    )
    {
        return factory(PlanetRI.class, code);
    }

    /**
     */
    public String toString()
    {
        return PlanetRI.class.getName() + "{" +
                super.toString() + ", " +
                TraceUtil.formatObj(diameter, "diameter") + ", " +
                TraceUtil.formatObj(mass, "mass") + ", " +
                TraceUtil.formatObj(moons, "moons") + ", " +
                TraceUtil.formatObj(orbit, "orbit") + "}";
    }

    /**
     */
    public static Collection<PlanetRI> getItems()
    {
        return getItems(PlanetRI.class);
    }

    /**
     */
    public static Collection<PlanetRI> getItems(
            final PlanetRI currentValue
    )
    {
        return getItems(PlanetRI.class, currentValue);
    }

    /**
     */
    public static Collection<PlanetRI> getAllItems()
    {
        return getAllItems(PlanetRI.class);
    }

    /**
     * Support for serialization.
     */
    private Object readResolve()
    {
        return factory(getCode());
    }

    /**
     * Pseudo-setter for the <b>diameter</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public void setDiameter(
        final double diameter
    )
    {
        this.diameter = diameter;
    }

    /**
     * Getter method for the <b>diameter</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public double getDiameter()
    {
        return diameter;
    }

    /**
     * Pseudo-setter for the <b>mass</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public void setMass(
        final double mass
    )
    {
        this.mass = mass;
    }

    /**
     * Getter method for the <b>mass</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public double getMass()
    {
        return mass;
    }

    /**
     * Pseudo-setter for the <b>moons</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public void setMoons(
        final MoonRI[] moons
    )
    {
        this.moons = moons;
    }

    /**
     * Getter method for the <b>moons</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public MoonRI[] getMoons()
    {
        return moons;
    }

    /**
     * Pseudo-setter for the <b>orbit</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public void setOrbit(
        final double orbit
    )
    {
        this.orbit = orbit;
    }

    /**
     * Getter method for the <b>orbit</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public double getOrbit()
    {
        return orbit;
    }

}