import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.main.ConfigSetup;
import org.main.Objects.Config;

import static org.junit.Assert.*;

public class SetupConfigTest {

    Config testconfig;
    @Before
    public void init(){
        testconfig = ConfigSetup.applyConfig("testconfig.txt");
    }

    @Test
    public void checkRuleSetisNotNull(){
        assertNotNull(testconfig.getRuleSet());
    }

    @Test
    public void checkAnalogiesFilePath(){
        assertEquals("test.txt", testconfig.getAnalogiesFilePath());
    }

    @Test
    public void checkRewrite(){
        assertFalse(testconfig.isRewrite());
    }

    @Test
    public void checkTargets(){
        assertEquals(testconfig.getTargets().get(0),"Adonis");
        assertEquals(testconfig.getTargets().get(1),"AIDS");
    }

    @After
    public void reset(){
        ConfigSetup.resetConfig();
    }

}
