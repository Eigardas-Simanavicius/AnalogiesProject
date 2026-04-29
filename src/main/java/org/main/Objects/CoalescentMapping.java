package org.main.Objects;

import org.main.CompositeBuilder;
import org.main.MappingManager;

import java.util.ArrayList;
import java.util.HashMap;

public class CoalescentMapping {

    private String source;
    private String target;
    private final double richness;
    private ArrayList<String> analogies;
    private HashMap<String,String> mapping;

    public CoalescentMapping(String source,String target){
        this.source = source;
        this.target = target;

        analogies = new CompositeBuilder().buildCompositeAnalogy(source,target);
        richness = MappingManager.getMappingRichness(analogies);
    }

    public String getSource(){return source;}
    public String getTarget(){return target;}
    public ArrayList<String> getAnalogies(){return analogies;}
    public double getRichness() {
        return richness;
    }
}
