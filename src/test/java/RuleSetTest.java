import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.main.RuleSet;
import org.main.rewriteRule;

import java.io.FileNotFoundException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RuleSetTest {


    @Test
    public void simpleTest() throws FileNotFoundException{
        RuleSet rules = new RuleSet("rewrite rules.txt");
        assertTrue(true);
    }
}
