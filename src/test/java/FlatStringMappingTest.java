import org.junit.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.ParameterizedTest;
import java.util.stream.Stream;
import org.main.MappingManager;
import java.util.*;

import static org.junit.Assert.*;

public class FlatStringMappingTest {

    static Stream<Arguments> mapData(){
        return Stream.of(
                Arguments.of("(work_in scientist (some lab (that (conduct experiment))))","(work_in scientist1 (some lab1 (that (conduct experiment1))))", Map.of("scientist","scientist1","lab","lab1","experiment","experiment1")),
                Arguments.of("(work_in scientist (some lab (that (conduct lab))))","(work_in 0 (some 1 (that (conduct 1))))",Map.of("scientist","0","lab","1")),
                Arguments.of("(if (can (cause.0 *AIDS (some death (when crushing (crush something))))) (can (succeed_at *AIDS (crush something))))","(if (can (cause.0 * (some 0 (when 1 (crush 2))))) (can (succeed_at * (crush 2))))",Map.of("*AIDS","*","death","0","crushing","1","something","2"))
        );
    }

    static Stream<Arguments> unmappableStrings(){
        return Stream.of(
                //mismatched predicates
                Arguments.of("(work_in scientist (some lab (that (conducts experiments))))", "(work_out scientist (some lab (that (conducts experiments))))"),
                //mismatched parentheses
                Arguments.of("((work_in scientist (some lab (that (conducts experiments))))", "(work_in scientist (some lab (that (conducts experiments))))"),
                //mismatched structure
                Arguments.of("(work_in scientist (some lab (that (conducts experiments)(and studies))))", "(work_in scientist (some lab (that (conducts experiments))))"),
                //mismatched structure 2
                Arguments.of("(work_in scientist (some (well_known lab (that (conducts experiments)))))", "(work_in scientist (some lab (that (conducts experiments))))"),
                //mismatched asterisks 1
                Arguments.of("(work_in *scientist (some lab (that (conduct experiment))))","(work_in scientist1 (some lab1 (that (conduct experiment1))))"),
                //mismatched asterisks 2
                Arguments.of("(work_in scientist (some lab (that (conduct experiment))))","(work_in scientist1 (some lab1 (that (conduct *experiment1))))")
        );
    }

    @ParameterizedTest
    @MethodSource("mapData")
    public void BasicTest1(String inputSource, String inputTarget, Map<String, String> expected) throws IllegalAccessException {
        assertEquals(expected,MappingManager.flatStringMapping(inputSource,inputTarget));
    }

    @ParameterizedTest
    @MethodSource("unmappableStrings")
    public void UnmappableClauses(String inputSource, String inputTarget) throws IllegalAccessException{
        assertNull(null, MappingManager.flatStringMapping(inputSource, inputTarget));
    }

    @Test
    public void exceptionTest(){
        try{
            MappingManager.flatStringMapping("(work_in scientist(some lab))", null);
        }catch (IllegalArgumentException e){
            assertTrue(true);
        }
    }
}
