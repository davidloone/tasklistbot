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

import au.id.loone.util.reflist.RefItem;
import au.id.loone.util.reflist.StaticRefItem;
import au.id.loone.util.reflist.RefItemInstance;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Reference list for moons of the solar system.
 *
 * @author David G Loone
 */
public final class MoonRI
        extends StaticRefItem
        implements RefItem, IsSerializable
{

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI MOON = new MoonRI("MOON", "Moon", 384400e3, 3476e3, 7.35e25); // EARTH

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI PHOBOS = new MoonRI("PHOBOS", "Phobos", 9378e3, 22.2e3, 1.08e19); // MARS

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI DEIMOS = new MoonRI("DEIMOS", "Deimos", 23459e3, 12.6e3, 1.8e18); // MARS

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI METIS = new MoonRI("METIS", "Metis", 128000e3, 40e3, 9.56e19); // JUPITER

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI ADRASTEA = new MoonRI("ADRASTEA", "Adrastea", 129000e3, 20e3, 1.91e19); // JUPITER

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI AMALTHEA = new MoonRI("AMALTHEA", "Amalthea", 181300e3, 189e3, 3.5e21); // JUPITER

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI THEBE = new MoonRI("THEBE", "Thebe", 222000e3, 100e3, 7.77e20); // JUPITER

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI IO = new MoonRI("IO", "Io", 422000e3, 3630e3, 8.93e25); // JUPITER

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI EUROPA = new MoonRI("EUROPA", "Europa", 670900e3, 3138e3, 4.80e25); // JUPITER

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI GANYMEDE = new MoonRI("GANYMEDE", "Ganymede", 1070000e3, 5262e3, 1.48e26); // JUPITER

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI CALLISTO = new MoonRI("CALLISTO", "Callisto", 1883000e3, 4800e3, 1.08e26); // JUPITER

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI LEDA = new MoonRI("LEDA", "Leda", 11094000e3, 16e3, 5.68e18); // JUPITER

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI HIMALIA = new MoonRI("HIMALIA", "Himalia", 11480000e3, 186e3, 9.56e21); // JUPITER

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI LYSITHEA = new MoonRI("LYSITHEA", "Lysithea", 11720000e3, 36e3, 7.77e19); // JUPITER

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI ELARA = new MoonRI("ELARA", "Elara", 11737000e3, 76e3, 7.77e20); // JUPITER

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI ANANKE = new MoonRI("ANANKE", "Ananke", 2200000e3, 30e3, 3.82e19); // JUPITER

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI CARME = new MoonRI("CARME", "Carme", 22600000e3, 40e3, 9.56e19); // JUIPITER

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI PASIPHAE = new MoonRI("PASIPHAE", "Pasiphae", 23500000e3, 50e3, 1.91e20); // JUPITER

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI SINOPE = new MoonRI("SINOPE", "Sinope", 23700000e3, 36e3, 7.77e19); // JUIPTER

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI PAN = new MoonRI("PAN", "Pan", 133583e3, 20e3, null); // SATURN

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI ATLAS = new MoonRI("ATLAS", "Atlas", 137670e3, 30e3, null); // SATURN

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI PROMETHEUS = new MoonRI("PROMETHEUS", "Prometheus", 139350e3, 91e3, 2.7e20); // SATURN

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI PANDORA = new MoonRI("PANDORA", "Pandora", 141700e3, 84e3, 2.2e20); // SATURN

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI EPIMETHEUS = new MoonRI("EPIMETHEUS", "Epimetheus", 151422e3, 115e3, 5.6e20); // SATURN

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI JANUS = new MoonRI("JANUS", "Janus", 151472e3, 178e3, 2.01e21); // SATURN

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI MIMAS = new MoonRI("MIMAS", "Mimas", 185520e3, 392e3, 3.80e22); // SATURN

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI ENCELADUS = new MoonRI("ENCELADUS", "Enceladus", 238020e3, 498e3, 7.30e22); // SATURN

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI TETHYS = new MoonRI("TETHYS", "Tethys", 294660e3, 1060e3, 6.22e23); // SATURN

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI TELESTO = new MoonRI("TELESTO", "Telesto", 294660e3, 29e3, null); // SATURN

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI CALYPSO = new MoonRI("CALYPSO", "Calypso", 294660e3, 26e3, null); // SATURN

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI DIONE = new MoonRI("DIONE", "Dione", 377400e3, 1120e3, 1.05e24); // SATURN

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI HELENE = new MoonRI("HELENE", "Helene", 377400e3, 33e3, null); // SATURN

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI RHEA = new MoonRI("RHEA", "Rhea", 527040e3, 1530e3, 2.49e24); // SATURN

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI TITAN = new MoonRI("TITAN", "Titan", 1221830e3, 5150e3, 1.35e26); // SATURN

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI HYPERION = new MoonRI("HYPERION", "Hyperion", 1481100e3, 286e3, 1.77e22); // SATURN

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI IAPETUS = new MoonRI("IAPETUS", "Iapetus", 3561300e3, 1460e3, 1.88e24); // SATURN

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI PHOEBE = new MoonRI("PHOEBE", "Phoebe", 12952000e3, 220e3, 4.0e21); // SATURN

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI CORDELIA = new MoonRI("CORDELIA", "Cordelia", 49752e3, 26e3, null); // URANUS

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI OPHELIA = new MoonRI("OPHELIA", "Ophelia", 53764e3, 32e3, null); // URANUS

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI BIANCA = new MoonRI("BIANCA", "Bianca", 59165e3, 44e3, null); // URANUS

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI CRESSIDA = new MoonRI("CRESSIDA", "Cressida", 61767e3, 66e3, null); // URANUS

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI DESDEMONA = new MoonRI("DESDEMONA", "Desdemona", 62659e3, 58e3, null); // URANUS

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI JULIET = new MoonRI("JULIET", "Juliet", 64358e3, 84e3, null); // URANUS

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI PORTIA = new MoonRI("PORTIA", "Portia", 66097e3, 110e3, null); // URANUS

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI ROSALIND = new MoonRI("ROSALIND", "Rosalind", 69927e3, 54e3, null); // URANUS

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI BELINDA = new MoonRI("BELINDA", "Belinda", 75255e3, 68e3, null); // URANUS

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI PUCK = new MoonRI("PUCK", "Puck", 86006e3, 154e3, null); // URANUS

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI MIRANDA = new MoonRI("MIRANDA", "Miranda", 129850e3, 472e3, 6.3e22); // URANUS

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI ARIEL = new MoonRI("ARIEL", "Ariel", 190930e3, 1158e3, 1.27e24); // URANUS

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI UMBRIEL = new MoonRI("UMBRIEL", "Umbriel", 265980e3, 1170e3, 1.27e24); // URANUS

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI TITANIA = new MoonRI("TITANIA", "Titania", 436270e3, 1578e3, 3.49e24); // URANUS

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI OBERON = new MoonRI("OBERON", "Oberon", 583420e3, 1523e3, 3.03e24); // URANUS

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI NAIAD = new MoonRI("NAIAD", "Naiad", 48200e3, 58e3, null); // NEPTUNE

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI THALASSA = new MoonRI("THALASSA", "Thalassa", 50000e3, 80e3, null); // NEPTUNE

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI DESPINA = new MoonRI("DESPINA", "Despina", 52600e3, 148e3, null); // NEPTUNE

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI GALATEA = new MoonRI("GALATEA", "Galatea", 62000e3, 158e3, null); // NEPTUNE

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI LARISSA = new MoonRI("LARISSA", "Larissa", 73600e3, 193e3, null); // NEPTUNE

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI PROTEUS = new MoonRI("PROTEUS", "Proteus", 117600e3, 418e3, null); // NEPTUNE

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI TRITON = new MoonRI("TRITON", "Triton", 354760e3, 2700e3, 2.14e25); // NEPTUNE

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI NEREID = new MoonRI("NEREID", "Nereid", 5513400e3, 340e3, null); // NEPTUNE

    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MoonRI CHARON = new MoonRI("CHARON", "Charon", 19640e3, 1206e3, 1.52e24); // PLUTO

    /**
     * An empty array.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static final MoonRI[] EMPTY_ARRAY = new MoonRI[] {};

    /**
     * Current value of the <b>diameter</b> property.
     */
    private double diameter;

    /**
     * Current value of the <b>mass</b> property.
     */
    private Double mass;

    /**
     * Current value of the <b>planet</b> property.
     */
    PlanetRI planet;

    /**
     * Current value of the <b>orbit</b> property.
     */
    private double orbit;

    /**
     */
    private MoonRI(
            final String code,
            final String description,
            final double orbit,
            final double diameter,
            final Double mass
    )
    {
        super(code, description);

        this.orbit = orbit;
        this.diameter = diameter;
        this.mass = mass;
    }

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private MoonRI(
            final String code,
            final String description,
            final String[] aliases,
            final double orbit,
            final double diameter,
            final Double mass
    )
    {
        super(code, description, aliases);

        this.orbit = orbit;
        this.diameter = diameter;
        this.mass = mass;
    }

    /**
     */
    public static MoonRI factory(
            final String code
    )
    {
        return factory(MoonRI.class, code);
    }

    /**
     */
    public static Collection<MoonRI> getItems()
    {
        return getItems(MoonRI.class);
    }

    /**
     */
    public static Collection<MoonRI> getItems(
            final MoonRI currentValue
    )
    {
        return getItems(MoonRI.class, currentValue);
    }

    /**
     */
    public static Collection<MoonRI> getAllItems()
    {
        return getAllItems(MoonRI.class);
    }

    /**
     * Support for serialization.
     */
    private Object readResolve()
    {
        return factory(getCode());
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
     * Getter method for the <b>mass</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public Double getMass()
    {
        return mass;
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