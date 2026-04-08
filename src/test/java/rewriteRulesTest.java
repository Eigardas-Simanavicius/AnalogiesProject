import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.ValueSources;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.main.AnalogyManager;
import org.main.Interfaces.Predicate;
import org.main.rewriteRule;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class rewriteRulesTest {

    public void BasicTest() throws IllegalAccessException {
        String[] inputs = {"(exercise *athlete muscle) ","(flex *athlete muscle)","(dislike *rival colleague)","(lose_control_over *captain mutineer)","(flunk *student exam)","(step_down_from *president presidency)"};
        String[] outputs = {"(by exercising( perform *athlete exercise(of muscle)))","(by flexing(display *athlete muscle(with effort)))","(by disliking(not(respect *rival colleague(as friend))))",
                "(by rejecting_control(not(respect mutineer *captain(as leader))))","(by flunking(reject teacher *student(for exam)))","(by stepping_down(reject *president presidency(as way_of_life)))"};
        rewriteRule[] rules = {new rewriteRule("exercise"," perform_of:exercise*&exercising"),new rewriteRule("flex","display_with:effort&flexing"),new rewriteRule("dislike","!respect_as:friend&disliking"),
                new rewriteRule("lose_control_over","<!respect_as:leader&rejecting_control"),new rewriteRule("step_down_from","reject_as:way_of_life&stepping_down")};
       for(int i = 0; i < inputs.length;i++) {
           Predicate p = AnalogyManager.ConvertToOOP(inputs[i]);
           assertEquals(outputs[i], rules[i].rewrite(p).toString());
       }
    }


        @Test
        public void hasPredicateChildrenTest() {
            Predicate p = AnalogyManager.ConvertToOOP("(exercise athelete muscle (big mac))");
            assertThrows(IllegalArgumentException.class, () -> {
                new rewriteRule("exercise", " perform_of:exercise*&exercising").rewrite(p);
            });
        }

        @Test
        public void nonMatchingRuleException() {
            Predicate p = AnalogyManager.ConvertToOOP("(exercise athelete muscle)");
            assertThrows(IllegalArgumentException.class, () -> {
                new rewriteRule("explode", " perform_of:exercise*&exercising").rewrite(p);
            });
        }

    @Test
    public void badRuleStructure() {
        Predicate p = AnalogyManager.ConvertToOOP("(exercise athelete muscle)");
        assertThrows(IllegalArgumentException.class, () -> {
            new rewriteRule("exercise", "test").rewrite(p);
        });
        assertThrows(InvalidParameterException.class, () -> {
            new rewriteRule("exercise", "test_ _ _  _ & &::::").rewrite(p);
        });
    }
}
