import org.junit.Before;
import org.junit.Test;
import org.main.AnalogyDataHolder;
import org.main.CompositeBuilder;
import org.main.ConfigSetup;
import org.main.Objects.Config;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CompositeBuilderTest {
    Config testconfig;
    private static final HashMap<String, ArrayList<String>> analogies = new HashMap<>();
    @Before
    public void init(){
        testconfig = ConfigSetup.applyConfig("testconfig.txt");
        ArrayList<String> t = new ArrayList<String>(testconfig.getTargets());
        t.add("barbarian");
        testconfig.setTargets(t);
        AnalogyDataHolder.addAnalogiesFromFile(testconfig);

    }


    @Test
    public void simpleTest(){
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *barbarian self) (display *barbarian self))");
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *Adonis body) (display *Adonis body))");
        CompositeBuilder comp = new CompositeBuilder();
        ArrayList<String> composite = comp.buildCompositeAnalogy("Adonis", "barbarian");
        assertEquals(4, composite.size());
        assertTrue(composite.contains("(if (train.0 *Adonis body) (display *Adonis body))"));
    }

    @Test
    public void unmappableStructures(){
        AnalogyDataHolder.addAnalogyToHash("(explode (completely *building))");
        AnalogyDataHolder.addAnalogyToHash("(detonate *location (completely *location))");
        CompositeBuilder comp = new CompositeBuilder();
        assertTrue(comp.buildCompositeAnalogy("building", "location").isEmpty());
    }

    @Test
    public void subjectMismatch(){
        AnalogyDataHolder.addAnalogyToHash("(modify (with ink (completely *narrative)))");
        AnalogyDataHolder.addAnalogyToHash("(modify (with *story (completely *story)))");
        CompositeBuilder comp = new CompositeBuilder();
        assertTrue(comp.buildCompositeAnalogy("story", "narrative").isEmpty());
    }

    @Test
    public void partialSubjectMismatch(){
        AnalogyDataHolder.addAnalogyToHash("(detonate *place (completely *place))");
        AnalogyDataHolder.addAnalogyToHash("(detonate *locale (completely *locale))");
        AnalogyDataHolder.addAnalogyToHash("(if person (can (find *place)) (can (rest person (in *place))))");
        AnalogyDataHolder.addAnalogyToHash("(if person (can (find *locale)) (can (rest person (in *locale))))");
        AnalogyDataHolder.addAnalogyToHash("(can person (get_lost finding *place))");
        AnalogyDataHolder.addAnalogyToHash("(can creature (get_lost finding *locale))");
        CompositeBuilder comp = new CompositeBuilder();
        ArrayList<String> composite = comp.buildCompositeAnalogy("place", "locale");

        assertEquals(4, composite.size());
        assertTrue(!composite.contains("(can creature (get_lost finding *location))"));
    }


}
