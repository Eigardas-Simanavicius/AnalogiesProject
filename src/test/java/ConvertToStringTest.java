import org.junit.Test;
import org.main.AnalogyManager;
import org.main.Clause;

import static org.junit.Assert.assertEquals;

public class ConvertToStringTest {

    @Test
    public void SimpleConversion(){
        Clause c1 = new Clause("c1","c1");
        Clause c2 = new Clause("c2","c2");
        c1.addEmbedded(c2);

        String out = AnalogyManager.ConvertToString(c1, false);

        assertEquals("(c1 c1(c2 c2))", out);
    }

    @Test
    public void NullSubject(){
        Clause c1 = new Clause("c1","c1");
        Clause c2 = new Clause();
        c2.setName("c2");
        c1.addEmbedded(c2);

        String out = AnalogyManager.ConvertToString(c1, false);

        assertEquals("(c1 c1(c2))", out);
    }

    @Test
    public void NullEverything(){
        Clause c1 = new Clause("c1","c1");
        Clause c2 = new Clause();
        c1.addEmbedded(c2);

        String out = AnalogyManager.ConvertToString(c1, false);

        assertEquals("(c1 c1())", out);
    }

    @Test
    public void Indentation(){
        Clause c1 = new Clause("c1","c1");
        Clause c2 = new Clause("c2", "c2");
        c1.addEmbedded(c2);

        String out = AnalogyManager.ConvertToString(c1, true);

        assertEquals("(c1 c1\n\t(c2 c2))", out);
    }

    @Test
    public void MultipleChildren(){
        Clause c1 = new Clause("c1","c1");
        Clause c2 = new Clause("c2","c2");
        Clause c3 = new Clause("c3","c3");
        Clause c4 = new Clause("c4","c4");
        Clause c5 = new Clause("c5","c5");

        c1.addEmbedded(c2);
        c2.addEmbedded(c3);
        c2.addEmbedded(c4);
        c3.addEmbedded(c5);

        String out = AnalogyManager.ConvertToString(c1, false);

        assertEquals("(c1 c1(c2 c2(c3 c3(c5 c5))(c4 c4)))", out);
    }

    @Test
    public void MultipleChildrenIndented(){
        Clause c1 = new Clause("c1","c1");
        Clause c2 = new Clause("c2","c2");
        Clause c3 = new Clause("c3","c3");
        Clause c4 = new Clause("c4","c4");
        Clause c5 = new Clause("c5","c5");

        c1.addEmbedded(c2);
        c2.addEmbedded(c3);
        c2.addEmbedded(c4);
        c3.addEmbedded(c5);

        String out = AnalogyManager.ConvertToString(c1, true);

        assertEquals("(c1 c1\n\t(c2 c2\n\t\t(c3 c3\n\t\t\t(c5 c5))\n\t\t\t(c4 c4)))", out);
    }

    @Test
    public void NoSubjectIndentation(){
        Clause c1 = new Clause("c1","c1");
        Clause c2 = new Clause("c2");
        Clause c3 = new Clause("c3");
        Clause c4 = new Clause("c4","c4");
        Clause c5 = new Clause("c5","c5");

        c1.addEmbedded(c2);
        c2.addEmbedded(c3);
        c2.addEmbedded(c4);
        c3.addEmbedded(c5);

        String out = AnalogyManager.ConvertToString(c1, true);

        assertEquals("(c1 c1\n\t(c2(c3(c5 c5))\n\t\t\t(c4 c4)))", out);
    }
}
