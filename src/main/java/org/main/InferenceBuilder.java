package org.main;

import org.main.Objects.CoalescentMapping;

import java.util.ArrayList;
import java.util.HashMap;

public class InferenceBuilder {

    public InferenceBuilder(){}
    public static ArrayList<String> getInferredAnalogies(CoalescentMapping mapping){
        ArrayList<String> sourceTopicAnalogies = AnalogyDataHolder.getAnalogiesFor(mapping.getSource());

        sourceTopicAnalogies = new ArrayList<>(sourceTopicAnalogies.stream().filter(source -> !mapping.getCoalescedMapping().containsKey(source)).toList());

        return new ArrayList<>(sourceTopicAnalogies.stream().filter(source -> canBeInferred(mapping.getInferredMapping(),source)).toList());
    }

    private static boolean canBeInferred(HashMap<String,String> inferredMapping, String analogy){
        return inferredMapping.keySet().containsAll(AnalogyManager.getUniqueSubjects(analogy));
    }

    public static HashMap<String,String> getInferredMappings(CoalescentMapping mapping){
        HashMap<String,String> inferredMappedAnalogies = new HashMap<>();

        for(String mappableSource: getInferredAnalogies(mapping)){
            inferredMappedAnalogies.put(mappableSource,getInferredTarget(mapping.getInferredMapping(),mappableSource));
        }

        return inferredMappedAnalogies;
    }

    private static String getInferredTarget(HashMap<String,String> inferredMapping, String analogy){
        String[] separatedAnalogy = analogy.split("(?<=[ (])|(?=[ )])");
        StringBuilder inferredString = new StringBuilder();

        for(String token: separatedAnalogy){
            inferredString.append(inferredMapping.getOrDefault(token, token));
        }

        return inferredString.toString();
    }
}
