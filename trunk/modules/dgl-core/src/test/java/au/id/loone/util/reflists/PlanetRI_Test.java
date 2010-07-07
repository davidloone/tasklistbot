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

package au.id.loone.util.reflists;

import au.id.loone.util.tracing.TraceUtil;
import org.junit.Test;
import org.junit.Assert;

/**
 * Test class for {@link PlanetRI}.
 *
 * @author David G Loone
 */
public final class PlanetRI_Test
{

    /**
     * Log4j logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(PlanetRI_Test.class);

    /**
     */
    public PlanetRI_Test()
    {
        super();
    }

    /**
     */
    @Test
    public void test_Mercury_00()
            throws Exception
    {
        Assert.assertEquals("MERCURY", PlanetRI.MERCURY.getCode());
        Assert.assertEquals("Mercury", PlanetRI.MERCURY.getDescription());
        Assert.assertEquals(57.91e9, PlanetRI.MERCURY.getOrbit(), 0.0);
        Assert.assertEquals(4.88e6, PlanetRI.MERCURY.getDiameter(), 0.0);
        Assert.assertEquals(3.30e26, PlanetRI.MERCURY.getMass(), 0.0);
        Assert.assertEquals(0, PlanetRI.MERCURY.getMoons().length);
    }

    /**
     */
    @Test
    public void test_Venus_00()
            throws Exception
    {
        Assert.assertEquals("VENUS", PlanetRI.VENUS.getCode());
        Assert.assertEquals("Venus", PlanetRI.VENUS.getDescription());
        Assert.assertEquals(108.2e9, PlanetRI.VENUS.getOrbit(), 0.0);
        Assert.assertEquals(12.1036e6, PlanetRI.VENUS.getDiameter(), 0.0);
        Assert.assertEquals(4.869e27, PlanetRI.VENUS.getMass(), 0.0);
        Assert.assertEquals(0, PlanetRI.VENUS.getMoons().length);
    }

    /**
     */
    @Test
    public void test_Earth_00()
            throws Exception
    {
        Assert.assertEquals("EARTH", PlanetRI.EARTH.getCode());
        Assert.assertEquals("Earth", PlanetRI.EARTH.getDescription());
        Assert.assertEquals(149.6e9, PlanetRI.EARTH.getOrbit(), 0.0);
        Assert.assertEquals(12.7563e6, PlanetRI.EARTH.getDiameter(), 0.0);
        Assert.assertEquals(5.972e27, PlanetRI.EARTH.getMass(), 0.0);
//        Assert.assertEquals(1, PlanetRI.EARTH.getMoons().length);
//        Assert.assertEquals("MOON", PlanetRI.EARTH.getMoons()[0].getCode());
//        Assert.assertEquals("Moon", PlanetRI.EARTH.getMoons()[0].getDescription());
    }

    /**
     */
    @Test
    public void test_Mars_00()
            throws Exception
    {
        Assert.assertEquals("MARS", PlanetRI.MARS.getCode());
        Assert.assertEquals("Mars", PlanetRI.MARS.getDescription());
        Assert.assertEquals(227.94e9, PlanetRI.MARS.getOrbit(), 0.0);
        Assert.assertEquals(6.794e6, PlanetRI.MARS.getDiameter(), 0.0);
        Assert.assertEquals(6.4219e26, PlanetRI.MARS.getMass(), 0.0);
//        Assert.assertEquals(2, PlanetRI.MARS.getMoons().length);
//        Assert.assertEquals("PHOBOS", PlanetRI.MARS.getMoons()[0].getCode());
//        Assert.assertEquals("Phobos", PlanetRI.MARS.getMoons()[0].getDescription());
//        Assert.assertEquals("DEIMOS", PlanetRI.MARS.getMoons()[1].getCode());
//        Assert.assertEquals("Deimos", PlanetRI.MARS.getMoons()[1].getDescription());
//            new MoonRI[] {MoonRI.PHOBOS, MoonRI.DEIMOS});
    }

    /**
     */
    @Test
    public void test_Jupiter_00()
            throws Exception
    {
        Assert.assertEquals("JUPITER", PlanetRI.JUPITER.getCode());
        Assert.assertEquals("Jupiter", PlanetRI.JUPITER.getDescription());
        Assert.assertEquals(77833e9, PlanetRI.JUPITER.getOrbit(), 0.0);
        Assert.assertEquals(142.984e6, PlanetRI.JUPITER.getDiameter(), 0.0);
        Assert.assertEquals(1.900e30, PlanetRI.JUPITER.getMass(), 0.0);
//        Assert.assertEquals(16, PlanetRI.JUPITER.getMoons().length);
//        Assert.assertEquals("METIS", PlanetRI.JUPITER.getMoons()[0].getCode());
//        Assert.assertEquals("Metis", PlanetRI.JUPITER.getMoons()[0].getDescription());
//        Assert.assertEquals("ADRASTEA", PlanetRI.JUPITER.getMoons()[1].getCode());
//        Assert.assertEquals("Adrastea", PlanetRI.JUPITER.getMoons()[1].getDescription());
//        Assert.assertEquals("AMALTHEA", PlanetRI.JUPITER.getMoons()[2].getCode());
//        Assert.assertEquals("Amalthea", PlanetRI.JUPITER.getMoons()[2].getDescription());
//        Assert.assertEquals("THEBE", PlanetRI.JUPITER.getMoons()[3].getCode());
//        Assert.assertEquals("Thebe", PlanetRI.JUPITER.getMoons()[3].getDescription());
//        Assert.assertEquals("IO", PlanetRI.JUPITER.getMoons()[4].getCode());
//        Assert.assertEquals("Io", PlanetRI.JUPITER.getMoons()[4].getDescription());
//        Assert.assertEquals("EUROPA", PlanetRI.JUPITER.getMoons()[5].getCode());
//        Assert.assertEquals("Europa", PlanetRI.JUPITER.getMoons()[5].getDescription());
//        Assert.assertEquals("GANYMEDE", PlanetRI.JUPITER.getMoons()[6].getCode());
//        Assert.assertEquals("Ganymede", PlanetRI.JUPITER.getMoons()[6].getDescription());
//        Assert.assertEquals("CALLISTO", PlanetRI.JUPITER.getMoons()[7].getCode());
//        Assert.assertEquals("Callisto", PlanetRI.JUPITER.getMoons()[7].getDescription());
//        Assert.assertEquals("LEDA", PlanetRI.JUPITER.getMoons()[8].getCode());
//        Assert.assertEquals("Leda", PlanetRI.JUPITER.getMoons()[8].getDescription());
//        Assert.assertEquals("HIMALIA", PlanetRI.JUPITER.getMoons()[9].getCode());
//        Assert.assertEquals("Himalia", PlanetRI.JUPITER.getMoons()[9].getDescription());
//        Assert.assertEquals("LYSITHEA", PlanetRI.JUPITER.getMoons()[10].getCode());
//        Assert.assertEquals("Lysithea", PlanetRI.JUPITER.getMoons()[10].getDescription());
//        Assert.assertEquals("ELARA", PlanetRI.JUPITER.getMoons()[11].getCode());
//        Assert.assertEquals("Elara", PlanetRI.JUPITER.getMoons()[11].getDescription());
//        Assert.assertEquals("ANANKE", PlanetRI.JUPITER.getMoons()[12].getCode());
//        Assert.assertEquals("Ananke", PlanetRI.JUPITER.getMoons()[12].getDescription());
//        Assert.assertEquals("CARME", PlanetRI.JUPITER.getMoons()[13].getCode());
//        Assert.assertEquals("Carme", PlanetRI.JUPITER.getMoons()[13].getDescription());
//        Assert.assertEquals("PASIPHAE", PlanetRI.JUPITER.getMoons()[14].getCode());
//        Assert.assertEquals("Pasiphae", PlanetRI.JUPITER.getMoons()[14].getDescription());
//        Assert.assertEquals("SINOPE", PlanetRI.JUPITER.getMoons()[15].getCode());
//        Assert.assertEquals("Sinope", PlanetRI.JUPITER.getMoons()[15].getDescription());
//            new MoonRI[] {MoonRI.METIS, MoonRI.ADRASTEA, MoonRI.AMALTHEA, MoonRI.THEBE, MoonRI.IO, MoonRI.EUROPA,
//            MoonRI.GANYMEDE, MoonRI.CALLISTO, MoonRI.LEDA, MoonRI.HIMALIA, MoonRI.LYSITHEA, MoonRI.ELARA,
//            MoonRI.ANANKE, MoonRI.CARME, MoonRI.PASIPHAE, MoonRI.SINOPE});
    }

    /**
     */
    @Test
    public void test_Saturn_00()
            throws Exception
    {
        Assert.assertEquals("SATURN", PlanetRI.SATURN.getCode());
        Assert.assertEquals("Saturn", PlanetRI.SATURN.getDescription());
        Assert.assertEquals(1429.4e9, PlanetRI.SATURN.getOrbit(), 0.0);
        Assert.assertEquals(120.536e6, PlanetRI.SATURN.getDiameter(), 0.0);
        Assert.assertEquals(5.68e29, PlanetRI.SATURN.getMass(), 0.0);
//        Assert.assertEquals(18, PlanetRI.SATURN.getMoons().length);
//        Assert.assertEquals("PAN", PlanetRI.SATURN.getMoons()[0].getCode());
//        Assert.assertEquals("Pan", PlanetRI.SATURN.getMoons()[0].getDescription());
//        Assert.assertEquals("ATLAS", PlanetRI.SATURN.getMoons()[1].getCode());
//        Assert.assertEquals("Atlas", PlanetRI.SATURN.getMoons()[1].getDescription());
//        Assert.assertEquals("PROMETHEUS", PlanetRI.SATURN.getMoons()[2].getCode());
//        Assert.assertEquals("Prometheus", PlanetRI.SATURN.getMoons()[2].getDescription());
//        Assert.assertEquals("PANDORA", PlanetRI.SATURN.getMoons()[3].getCode());
//        Assert.assertEquals("Pandora", PlanetRI.SATURN.getMoons()[3].getDescription());
//        Assert.assertEquals("EPIMETHEUS", PlanetRI.SATURN.getMoons()[4].getCode());
//        Assert.assertEquals("Epimetheus", PlanetRI.SATURN.getMoons()[4].getDescription());
//        Assert.assertEquals("JANUS", PlanetRI.SATURN.getMoons()[5].getCode());
//        Assert.assertEquals("Janus", PlanetRI.SATURN.getMoons()[5].getDescription());
//        Assert.assertEquals("MIMAS", PlanetRI.SATURN.getMoons()[6].getCode());
//        Assert.assertEquals("Mimas", PlanetRI.SATURN.getMoons()[6].getDescription());
//        Assert.assertEquals("ENCELADUS", PlanetRI.SATURN.getMoons()[7].getCode());
//        Assert.assertEquals("Enceladus", PlanetRI.SATURN.getMoons()[7].getDescription());
//        Assert.assertEquals("TETHYS", PlanetRI.SATURN.getMoons()[8].getCode());
//        Assert.assertEquals("Tethys", PlanetRI.SATURN.getMoons()[8].getDescription());
//        Assert.assertEquals("TELESTO", PlanetRI.SATURN.getMoons()[9].getCode());
//        Assert.assertEquals("Telesto", PlanetRI.SATURN.getMoons()[9].getDescription());
//        Assert.assertEquals("CALYPSO", PlanetRI.SATURN.getMoons()[10].getCode());
//        Assert.assertEquals("Calypso", PlanetRI.SATURN.getMoons()[10].getDescription());
//        Assert.assertEquals("DIONE", PlanetRI.SATURN.getMoons()[11].getCode());
//        Assert.assertEquals("Dione", PlanetRI.SATURN.getMoons()[11].getDescription());
//        Assert.assertEquals("HELENE", PlanetRI.SATURN.getMoons()[12].getCode());
//        Assert.assertEquals("Helene", PlanetRI.SATURN.getMoons()[12].getDescription());
//        Assert.assertEquals("RHEA", PlanetRI.SATURN.getMoons()[13].getCode());
//        Assert.assertEquals("Rhea", PlanetRI.SATURN.getMoons()[13].getDescription());
//        Assert.assertEquals("TITAN", PlanetRI.SATURN.getMoons()[14].getCode());
//        Assert.assertEquals("Titan", PlanetRI.SATURN.getMoons()[14].getDescription());
//        Assert.assertEquals("HYPERION", PlanetRI.SATURN.getMoons()[15].getCode());
//        Assert.assertEquals("Hyperion", PlanetRI.SATURN.getMoons()[15].getDescription());
//        Assert.assertEquals("IAPETUS", PlanetRI.SATURN.getMoons()[16].getCode());
//        Assert.assertEquals("Iapetus", PlanetRI.SATURN.getMoons()[16].getDescription());
//        Assert.assertEquals("PHOEBE", PlanetRI.SATURN.getMoons()[17].getCode());
//        Assert.assertEquals("Phoebe", PlanetRI.SATURN.getMoons()[17].getDescription());
//            new MoonRI[] {MoonRI.PAN, MoonRI.ATLAS, MoonRI.PROMETHEUS, MoonRI.PANDORA, MoonRI.EPIMETHEUS,
//            MoonRI.JANUS, MoonRI.MIMAS, MoonRI.ENCELADUS, MoonRI.TETHYS, MoonRI.TELESTO, MoonRI.CALYPSO,
//            MoonRI.DIONE, MoonRI.HELENE, MoonRI.RHEA, MoonRI.TITAN, MoonRI.HYPERION, MoonRI.IAPETUS,
//            MoonRI.PHOEBE});
    }

    /**
     */
    @Test
    public void test_Uranus_00()
            throws Exception
    {
        Assert.assertEquals("URANUS", PlanetRI.URANUS.getCode());
        Assert.assertEquals("Uranus", PlanetRI.URANUS.getDescription());
        Assert.assertEquals(2870.99e9, PlanetRI.URANUS.getOrbit(), 0.0);
        Assert.assertEquals(51.118e6, PlanetRI.URANUS.getDiameter(), 0.0);
        Assert.assertEquals(8.683e28, PlanetRI.URANUS.getMass(), 0.0);
//        Assert.assertEquals(15, PlanetRI.URANUS.getMoons().length);
//        Assert.assertEquals("CORDELIA", PlanetRI.URANUS.getMoons()[0].getCode());
//        Assert.assertEquals("Cordelia", PlanetRI.URANUS.getMoons()[0].getDescription());
//        Assert.assertEquals("OPHELIA", PlanetRI.URANUS.getMoons()[1].getCode());
//        Assert.assertEquals("Ophelia", PlanetRI.URANUS.getMoons()[1].getDescription());
//        Assert.assertEquals("BIANCA", PlanetRI.URANUS.getMoons()[2].getCode());
//        Assert.assertEquals("Bianca", PlanetRI.URANUS.getMoons()[2].getDescription());
//        Assert.assertEquals("CRESSIDA", PlanetRI.URANUS.getMoons()[3].getCode());
//        Assert.assertEquals("Cressida", PlanetRI.URANUS.getMoons()[3].getDescription());
//        Assert.assertEquals("DESDEMONA", PlanetRI.URANUS.getMoons()[4].getCode());
//        Assert.assertEquals("Desdemona", PlanetRI.URANUS.getMoons()[4].getDescription());
//        Assert.assertEquals("JULIET", PlanetRI.URANUS.getMoons()[5].getCode());
//        Assert.assertEquals("Juliet", PlanetRI.URANUS.getMoons()[5].getDescription());
//        Assert.assertEquals("PORTIA", PlanetRI.URANUS.getMoons()[6].getCode());
//        Assert.assertEquals("Portia", PlanetRI.URANUS.getMoons()[6].getDescription());
//        Assert.assertEquals("ROSALIND", PlanetRI.URANUS.getMoons()[7].getCode());
//        Assert.assertEquals("Rosalind", PlanetRI.URANUS.getMoons()[7].getDescription());
//        Assert.assertEquals("BELINDA", PlanetRI.URANUS.getMoons()[8].getCode());
//        Assert.assertEquals("Belinda", PlanetRI.URANUS.getMoons()[8].getDescription());
//        Assert.assertEquals("PUCK", PlanetRI.URANUS.getMoons()[9].getCode());
//        Assert.assertEquals("Puck", PlanetRI.URANUS.getMoons()[9].getDescription());
//        Assert.assertEquals("MIRANDA", PlanetRI.URANUS.getMoons()[10].getCode());
//        Assert.assertEquals("Miranda", PlanetRI.URANUS.getMoons()[10].getDescription());
//        Assert.assertEquals("ARIEL", PlanetRI.URANUS.getMoons()[11].getCode());
//        Assert.assertEquals("Ariel", PlanetRI.URANUS.getMoons()[11].getDescription());
//        Assert.assertEquals("UMBRIEL", PlanetRI.URANUS.getMoons()[12].getCode());
//        Assert.assertEquals("Umbriel", PlanetRI.URANUS.getMoons()[12].getDescription());
//        Assert.assertEquals("TITANIA", PlanetRI.URANUS.getMoons()[13].getCode());
//        Assert.assertEquals("Titania", PlanetRI.URANUS.getMoons()[13].getDescription());
//        Assert.assertEquals("OBERON", PlanetRI.URANUS.getMoons()[14].getCode());
//        Assert.assertEquals("Oberon", PlanetRI.URANUS.getMoons()[14].getDescription());
//            new MoonRI[] {MoonRI.CORDELIA, MoonRI.OPHELIA, MoonRI.BIANCA, MoonRI.CRESSIDA, MoonRI.DESDEMONA,
//            MoonRI.JULIET, MoonRI.PORTIA, MoonRI.ROSALIND, MoonRI.BELINDA, MoonRI.PUCK, MoonRI.MIRANDA,
//            MoonRI.ARIEL, MoonRI.UMBRIEL, MoonRI.TITANIA, MoonRI.OBERON});
    }

    /**
     */
    @Test
    public void test_Neptune_00()
            throws Exception
    {
        Assert.assertEquals("NEPTUNE", PlanetRI.NEPTUNE.getCode());
        Assert.assertEquals("Neptune", PlanetRI.NEPTUNE.getDescription());
        Assert.assertEquals(4504e9, PlanetRI.NEPTUNE.getOrbit(), 0.0);
        Assert.assertEquals(49.532e6, PlanetRI.NEPTUNE.getDiameter(), 0.0);
        Assert.assertEquals(1.0247e29, PlanetRI.NEPTUNE.getMass(), 0.0);
//        Assert.assertEquals(8, PlanetRI.NEPTUNE.getMoons().length);
//        Assert.assertEquals("NAIAD", PlanetRI.NEPTUNE.getMoons()[0].getCode());
//        Assert.assertEquals("Naiad", PlanetRI.NEPTUNE.getMoons()[0].getDescription());
//        Assert.assertEquals("THALASSA", PlanetRI.NEPTUNE.getMoons()[1].getCode());
//        Assert.assertEquals("Thalassa", PlanetRI.NEPTUNE.getMoons()[1].getDescription());
//        Assert.assertEquals("DESPINA", PlanetRI.NEPTUNE.getMoons()[2].getCode());
//        Assert.assertEquals("Despina", PlanetRI.NEPTUNE.getMoons()[2].getDescription());
//        Assert.assertEquals("GALATEA", PlanetRI.NEPTUNE.getMoons()[3].getCode());
//        Assert.assertEquals("Galatea", PlanetRI.NEPTUNE.getMoons()[3].getDescription());
//        Assert.assertEquals("LARISSA", PlanetRI.NEPTUNE.getMoons()[4].getCode());
//        Assert.assertEquals("Larissa", PlanetRI.NEPTUNE.getMoons()[4].getDescription());
//        Assert.assertEquals("PROTEUS", PlanetRI.NEPTUNE.getMoons()[5].getCode());
//        Assert.assertEquals("Proteus", PlanetRI.NEPTUNE.getMoons()[5].getDescription());
//        Assert.assertEquals("TRITON", PlanetRI.NEPTUNE.getMoons()[6].getCode());
//        Assert.assertEquals("Triton", PlanetRI.NEPTUNE.getMoons()[6].getDescription());
//        Assert.assertEquals("NEREID", PlanetRI.NEPTUNE.getMoons()[7].getCode());
//        Assert.assertEquals("Nereid", PlanetRI.NEPTUNE.getMoons()[7].getDescription());
//            new MoonRI[] {MoonRI.NAIAD, MoonRI.THALASSA, MoonRI.DESPINA, MoonRI.GALATEA, MoonRI.LARISSA,
//            MoonRI.PROTEUS, MoonRI.TRITON, MoonRI.NEREID});
    }

    /**
     */
    @Test
    public void test_Pluto_00()
            throws Exception
    {
        Assert.assertEquals("PLUTO", PlanetRI.PLUTO.getCode());
        Assert.assertEquals("Pluto", PlanetRI.PLUTO.getDescription());
        Assert.assertEquals(5913.52e9, PlanetRI.PLUTO.getOrbit(), 0.0);
        Assert.assertEquals(2.274e6, PlanetRI.PLUTO.getDiameter(), 0.0);
        Assert.assertEquals(1.27e25, PlanetRI.PLUTO.getMass(), 0.0);
//        Assert.assertEquals(1, PlanetRI.PLUTO.getMoons().length);
//        Assert.assertEquals("CHARON", PlanetRI.PLUTO.getMoons()[0].getCode());
//        Assert.assertEquals("Charon", PlanetRI.PLUTO.getMoons()[0].getDescription());
//            new MoonRI[] {MoonRI.CHARON});
    }

}
