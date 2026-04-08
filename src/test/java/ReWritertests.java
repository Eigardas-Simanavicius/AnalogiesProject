import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.main.AnalogyManager;
import org.main.Clause;
import org.main.Interfaces.Predicate;
import org.main.ReWriter;
import org.main.rewriteRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class ReWritertests  {


    @Test
    public void retainChildrenTest(){
        rewriteRule rule1 = new rewriteRule("exercise","preform_of:exercise*&exercising");
        rewriteRule rule2 = new rewriteRule("explode","destroy_of:explode*&exploding");
        ArrayList<rewriteRule> rules = new ArrayList<>();
        rules.add(rule1);
        rules.add(rule2);
        Clause testClause2 = (Clause)AnalogyManager.ConvertToOOP("(Sigma male (Whopper jr) (exercise.0 athelete muscle (big mac)) (explode Gregs legs))");
        ArrayList<Predicate> ans = ReWriter.reWriteAnalogyAllPermuatations(rules,testClause2);
        assertEquals("(Sigma male(Whopper jr)(by exercising(preform athelete exercise(of muscle))(big mac))(by exploding(destroy Gregs explode(of legs))))", ((Clause) (ans.getFirst())).toString());
    }

    @Test
    public void allPermutationsTest(){
        rewriteRule rule1 = new rewriteRule("exercise","preform_of:exercise*&exercising");
        rewriteRule rule2 = new rewriteRule("explode","destroy_of:explode*&exploding");
        rewriteRule rule3 = new rewriteRule("explode","boom_of:explode*:booming");
        ArrayList<rewriteRule> rules = new ArrayList<>();
        rules.add(rule1);
        rules.add(rule2);
        rules.add(rule3);
        Clause testClause2 = (Clause)AnalogyManager.ConvertToOOP("(Sigma male (Whopper jr) (exercise.0 athelete muscle (big mac)) (explode Gregs legs))");
        Clause testClause3 = (Clause)AnalogyManager.ConvertToOOP("(Sigma male (exercise.0 athelete muscle) (explode Gregs legs) (explode Gregs legs) (explode Gregs legs) (explode Gregs legs))");
        ArrayList<Predicate> ans = ReWriter.reWriteAnalogyAllPermuatations(rules,testClause2);
        ArrayList<Predicate> ans2 = ReWriter.reWriteAnalogyAllPermuatations(rules,testClause3);
        assertEquals(2, ans.size());
        assertEquals(16, ans2.size());

    }
}
