import org.junit.After;
import org.junit.Test;
import org.main.*;
import org.main.Objects.Clause;
import org.main.Objects.CoalescentMapping;
import org.main.Objects.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MapAnalogiesTest {

    @Test
    public void basicTest(){
        Clause c1 = (Clause) AnalogyManager.ConvertToOOP("(p1 subject1(p2 subject4(p3 subject5)))");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(p1 subject2(p2 subject3(p3 s6)))");

        HashMap<Subject, Subject> tempMap = MappingManager.mapAnalogies(c1,c2);

        assertEquals(3, tempMap.size());
    }

    @Test
    public void simpleMapping(){
        Clause c1 = (Clause) AnalogyManager.ConvertToOOP("(p1 subject1(p2 subject4(p3 subject5)))");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(p1 subject2(p2 subject3(p3 s6)))");

        HashMap<Subject, Subject> tempMap = MappingManager.mapAnalogies(c1,c2);
        Subject key = (Subject)c1.getChildren().getFirst();

        assertEquals("subject2", tempMap.get(key).getName());
    }

    @Test
    public void nestedMapping(){
        Clause c1 = (Clause) AnalogyManager.ConvertToOOP("(p1 subject1(p2 subject4(p3 subject5)))");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(p1 subject2(p2 subject3(p3 s6)))");

        HashMap<Subject, Subject> tempMap = MappingManager.mapAnalogies(c1,c2);
        Clause c3 = (Clause)c1.getChildren().get(1);
        Subject key = (Subject) c3.getChildren().getFirst();

        assertEquals("subject3", tempMap.get(key).getName());
    }

    @Test
    public void asteriskMapping(){
        Clause c1 = (Clause) AnalogyManager.ConvertToOOP("(p1 *subject1(p2 subject4(p3 subject5)))");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(p1 *subject2(p2 subject3(p3 s6)))");

        HashMap<Subject, Subject> tempMap = MappingManager.mapAnalogies(c1,c2);
        Subject key = (Subject)c1.getChildren().getFirst();

        assertEquals("*subject2", tempMap.get(key).getName());
    }

    @Test
    public void asteriskMappingNested(){
        Clause c1 = (Clause) AnalogyManager.ConvertToOOP("(p1 subject1(p2 *subject4(p3 subject5)))");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(p1 subject2(p2 *subject3(p3 s6)))");

        HashMap<Subject, Subject> tempMap = MappingManager.mapAnalogies(c1,c2);
        Clause c3 = (Clause)c1.getChildren().get(1);
        Subject key = (Subject) c3.getChildren().getFirst();

        assertEquals("*subject3", tempMap.get(key).getName());
    }

    @Test
    public void multipleSubjectMapping(){
        Clause c1 = (Clause) AnalogyManager.ConvertToOOP("(p1 subject1 subject7(p2 subject4))");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(p1 subject2 subject8(p2 subject3))");

        HashMap<Subject, Subject> tempMap = MappingManager.mapAnalogies(c1,c2);
        Subject key = (Subject) c1.getChildren().get(1);

        assertEquals("subject8", tempMap.get(key).getName());
    }

    @Test
    public void multipleSubjectMappingAsterisk(){
        ConfigSetup.resetConfig();
        Clause c1 = (Clause) AnalogyManager.ConvertToOOP("(p1 subject1 *subject7(p2 subject4))");
        Clause c2 = (Clause)AnalogyManager.ConvertToOOP("(p1 subject2 *subject8(p2 subject3))");

        HashMap<Subject, Subject> tempMap = MappingManager.mapAnalogies(c1,c2);
        Subject key = (Subject) c1.getChildren().get(1);

        assertEquals("*subject8", tempMap.get(key).getName());
    }

    @Test
    public void rankBestSingleTarget(){
        AnalogyDataHolder.addAnalogyToHash("(be *Bill sigma)");
        AnalogyDataHolder.addAnalogyToHash("(be *Bob delta)");
        AnalogyDataHolder.addAnalogyToHash("(Sigma male (exercise.0 *Bob muscle))");
        AnalogyDataHolder.addAnalogyToHash("(Sigma male (exercise.0 *Bill muscle))");

        ArrayList<ArrayList<String>> arr = MappingManager.rankBestAnalogiesSingleTarget("Bill","Bob");
        assertEquals("[[(Sigma male (exercise.0 *Bill muscle)), (Sigma male (exercise.0 *Bob muscle)), (be *Bill sigma), (be *Bob delta)]]",arr.toString());

    }

    @Test
    public void rankBestCoalescentMapping(){
        AnalogyDataHolder.addAnalogyToHash("(be *Bill sigma)");
        AnalogyDataHolder.addAnalogyToHash("(be *Bob delta)");
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *Bob self) (display *Bob self))");
        AnalogyDataHolder.addAnalogyToHash("(Sigma male (exercise.0 *Bob muscle))");
        AnalogyDataHolder.addAnalogyToHash("(Sigma male (exercise.0 *Bill muscle))");
        MappingManager.createNewCoalescentMapping("Bob","Bill");

        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *Bob self) (display *Bob self))");
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *Adonis body) (display *Adonis body))");
        MappingManager.createNewCoalescentMapping("Bob","Adonis");

        ArrayList<CoalescentMapping> mappings =  MappingManager.rankBestCoalescentMapping("Bob");
        List<Double> richness = ( mappings.stream().map(CoalescentMapping::getRichness)).toList();
        assertEquals("[2.4464571609137615, 4.277161658802512]",richness.toString());

    }

    @After
    public void reset(){
        ConfigSetup.resetConfig();
    }
}
