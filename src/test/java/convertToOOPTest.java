import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.main.AnalogyManager;
import org.main.Clause;
import org.main.Interfaces.Predicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertEquals;

public class convertToOOPTest {

    String input = "(serve priest (some congregation (that (perform (for (some god)) (some worship)))))";
    Clause Head = (Clause) AnalogyManager.ConvertToOOP(input);
    ArrayList<String> names = new ArrayList<>();
    @Test
    public void BasicTest() {
        assertEquals("serve ", Head.getName());
        assertEquals("some ",Head.getChildren().getFirst().getName());
    }

    @Test
    public void AssertChildren(){
        names.add("serve ");
        names.add("some ");
        names.add("that ");
        names.add("perform ");
        names.add("for ");
        names.add("some ");
        names.add("some ");

        ArrayList<Predicate> children = Head.getAllChildren();
        for (int i = 0; i < children.size(); i++) {
            assertEquals(children.get(i).getName(),names.get(i));
        }
    }

    @Test
    public void ParentTest() {
        assertEquals(Head, Head.getChildren().getFirst().getParent());
    }
}
