package org.main;

import org.main.Objects.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CompositeBuilder {
    private static HashMap<Double, ArrayList<String>> sourceAnalogiesMap = new HashMap<>();
    private static HashMap<Double, ArrayList<String>> targetAnalogiesMap = new HashMap<>();
    private static final Logger logger = Logger.getLogger(CompositeBuilder.class.getName());
    private static HashMap<Subject, Subject> mainMap;

    //runs N times, starting from the next highest richness value every time
    public static ArrayList<String> buildGreedyCompositeAnalogy(String source, String target, int N){
        ArrayList<String> sourceAnalogiesArr = AnalogyDataHolder.getAnalogiesFor(source);
        ArrayList<String> targetAnalogiesArr = AnalogyDataHolder.getAnalogiesFor(target);
        sourceAnalogiesMap = mapByRichness(sourceAnalogiesArr);
        targetAnalogiesMap = mapByRichness(targetAnalogiesArr);
        sanitizeInputAnalogies();

        ArrayList<String> compositeAnalogy = new ArrayList<>();
        mainMap = new HashMap<>(); //resets static hashmap of subjects

        List<Double> index = sourceAnalogiesMap.keySet().stream().sorted().toList();

        if(N > index.size()){
            logger.log(Level.WARNING, "Requested more greedy runs than there are mappable analogies, defaulting to maximum possible value");
            N = index.size();
        }

        //Iterates over richness maps
        for(int i = 0; i < index.size(); i++){
            double currIndex = index.get(i);
            ArrayList<String> currentSources = sourceAnalogiesMap.get(currIndex);
            ArrayList<String> currentTargets = targetAnalogiesMap.get(currIndex);
            for(int j = 0; j < currentSources.size(); j++){
                if(i >= currentTargets.size()){
                    break;
                }
                //checks if two current analogies can be added to the composite, if yes then adds them (and adds all of their subject mappings to the subject map)
                if(coalesce(currentSources.get(i), currentTargets.get(i))){
                    compositeAnalogy.add(currentSources.get(i));
                    compositeAnalogy.add(currentTargets.get(i));
                }
            }
        }


        return compositeAnalogy;
    }

    //makes sure there are no "unmappable" analogies in either set, by comparing structure richness
    private static void sanitizeInputAnalogies(){
        for(Double key : sourceAnalogiesMap.keySet()){
            if(targetAnalogiesMap.get(key) == null){
                sourceAnalogiesMap.remove(key);
            }
        }
        for(Double key : targetAnalogiesMap.keySet()){
            if(sourceAnalogiesMap.get(key) == null){
                targetAnalogiesMap.remove(key);
            }
        }

    }

    private static HashMap<Double, ArrayList<String>> mapByRichness(ArrayList<String> inputAnalogies){
        HashMap<Double, ArrayList<String>> richnessMap = new HashMap<>();
        for(String analogy : inputAnalogies){
            double richness = AnalogyManager.getPredicateRichness(AnalogyManager.ConvertToOOP(analogy));
            ArrayList<String> arr;

            if(richnessMap.get(richness) == null){
                arr = new ArrayList<>();
            }
            else{
                arr = richnessMap.get(richness);
            }
            arr.add(analogy);
            richnessMap.put(richness,arr);
        }
        return richnessMap;
    }

    private static boolean coalesce(String source, String target){
        //mapAnalogies also checks if they two inputs are mappable, extra sanitation not needed
        HashMap<Subject, Subject> mapping = MappingManager.mapAnalogies(AnalogyManager.ConvertToOOP(source), AnalogyManager.ConvertToOOP(target));
        if(mapping == null){
            return false;
        }
        for(Subject subject : mapping.keySet()){
            if(mainMap.get(subject) != null){
                if(!(mapping.get(subject).equals(mainMap.get(subject)))){
                    return false;
                }
            }
        }

        for(Subject subject : mapping.keySet()){
            mainMap.put(subject, mapping.get(subject));
        }
        return true;
    }


}
