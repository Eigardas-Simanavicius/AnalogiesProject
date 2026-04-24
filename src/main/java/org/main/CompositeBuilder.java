package org.main;

import org.main.Objects.Subject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CompositeBuilder {
    private HashMap<Double, ArrayList<String>> sourceAnalogiesMap = new HashMap<>();
    private HashMap<Double, ArrayList<String>> targetAnalogiesMap = new HashMap<>();
    private HashMap<Integer, ArrayList<Pair>> pairMap = null;
    private static final Logger logger = Logger.getLogger(CompositeBuilder.class.getName());
    private HashMap<String, String> forwardMap;
    private HashMap<String, String> backwardMap;

    //runs N times, starting from the next highest richness value every time
    public ArrayList<String> buildCompositeAnalogy(String source, String target){
        setup(source, target);
        List<Double> index = this.sourceAnalogiesMap.keySet().stream().sorted().toList(); //controls which source analogy is currently being looked at
        return(searchOnce(index, 0));
    }

    public ArrayList<ArrayList<String>> buildMultipleCompositeAnalogies(String source, String target, int N){
        setup(source, target);
        List<Double> index = this.sourceAnalogiesMap.keySet().stream().sorted().toList(); //controls which source analogy is currently being looked at

        if(N > index.size()){
            logger.log(Level.WARNING, "Requested more greedy runs than there are mappable analogies, defaulting to maximum possible value");
            N = index.size();
        }

        ArrayList<ArrayList<String>> listOfComposites = new ArrayList<>();

        for(int i = 0; i < N; i++){
            ArrayList<String> arr = searchOnce(index, i);
            if(arr != null) {
                listOfComposites.add(arr);
            }
        }

        return listOfComposites;
    }

    private ArrayList<String> searchOnce(List<Double> index, int start){
        ArrayList<String> compositeAnalogy = new ArrayList<>();
        ArrayList<Pair> mappingPairs = new ArrayList<>();
        boolean wasReset = false;

        //Iterates over richness maps
        for (int i = start; i < index.size(); i++) {
            //avoid adding duplicates
            if(wasReset && i == start){
                continue;
            }
            double currIndex = index.get(i);
            ArrayList<String> currentSources = this.sourceAnalogiesMap.get(currIndex);
            ArrayList<String> currentTargets = this.targetAnalogiesMap.get(currIndex);
            for (int j = 0; j < currentSources.size(); j++) {
                if (j >= currentTargets.size()) {
                    break;
                }
                //checks if two current analogies can be added to the composite, if yes then adds them (and adds all of their subject mappings to the subject map)
                if (coalesce(currentSources.get(j), currentTargets.get(j))) {
                    compositeAnalogy.add(currentSources.get(j));
                    compositeAnalogy.add(currentTargets.get(j));
                }
            }
            //considers all analogies "above" the one we started with
            //Since we're often starting with not the richest analogy, this goes back up the list and checks the richer ones as well.
            if(!wasReset && start != 0){ //avoid resetting if we start at 0
                i = -1;
                wasReset = true;
            }
        }

        if(notDuplicate()) {
            return compositeAnalogy;
        }else {
            return null;
        }
    }

    private void setup(String source, String target){
        ArrayList<String> sourceAnalogiesArr = AnalogyDataHolder.getAnalogiesFor(source);
        ArrayList<String> targetAnalogiesArr = AnalogyDataHolder.getAnalogiesFor(target);
        this.sourceAnalogiesMap = mapByRichness(sourceAnalogiesArr);
        this.targetAnalogiesMap = mapByRichness(targetAnalogiesArr);
        pairMap = new HashMap<>();
        sanitizeInputAnalogies();

        this.forwardMap = new HashMap<>(); //resets static hashmap of subjects
        this.backwardMap = new HashMap<>();
    }

    //makes sure there are no "unmappable" analogies in either set, by comparing structure richness
    private void sanitizeInputAnalogies(){
        for(Double key : this.sourceAnalogiesMap.keySet()){
            if(this.targetAnalogiesMap.get(key) == null){
                this.sourceAnalogiesMap.remove(key);
            }
        }
        for(Double key : this.targetAnalogiesMap.keySet()){
            if(this.sourceAnalogiesMap.get(key) == null){
                this.targetAnalogiesMap.remove(key);
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

    private boolean coalesce(String source, String target){
        //mapAnalogies also checks if they two inputs are mappable, extra sanitation not needed
        HashMap<Subject, Subject> mapping = MappingManager.mapAnalogies(AnalogyManager.ConvertToOOP(source), AnalogyManager.ConvertToOOP(target));
        if(mapping == null){
            return false;
        }

        for(Subject subject : mapping.keySet()){
            String firstSubject = subject.getName();
            String secondSubject = mapping.get(subject).getName();
            if(forwardMap.get(firstSubject) != null){
                //Checks that subject from analogy set 1 always maps to the same subject in analogy set 2. Also checks that subjects from set 2 always map to the same subject in set 1
                if(!(secondSubject.equals(this.forwardMap.get(firstSubject))) || !(firstSubject.equals(this.backwardMap.get(secondSubject)))){
                    return false;
                }
            }
        }

        for(Subject subject : mapping.keySet()){
            String subjectName = subject.getName();
            this.forwardMap.put(subjectName.intern(), mapping.get(subject).getName().intern());
            this.backwardMap.put(mapping.get(subject).getName().intern(), subjectName.intern());
        }
        return true;
    }

    private boolean notDuplicate(){
        return false;
    }
    // the two mapping can only be the same if both pairs have the same hash so to speak, we will use this to wittle down our options
    private int pairValue(ArrayList<Pair> mapping){
        int total = 0;
        for(Pair pair: mapping){
            total += pair.source;
        }
        return total;
    }


    // represent the pair the two analogies mapped together, the numbers representing their location in their respective analogies arralists
    private class Pair{
        int source;
        int target;

        public Pair(int s, int t){
            source = s;
            target = t;
        }
        public int getSource(){
            return source;
        }
        public int getTarget(){
            return target;
        }

    }

    private class sortPairs implements Comparator{

        @Override
        public int compare(Object t1, Object t2) {
            Pair a = (Pair) t1;
            Pair b = (Pair) t2;
            return Integer.compare(a.getSource(), b.getSource());
        }
    }
}
