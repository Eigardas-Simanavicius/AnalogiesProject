package org.main.Interfaces;
import java.util.ArrayList;

public interface Predicate extends AnalogicalObject {
    void addEmbedded(AnalogicalObject analogicalObject);
    void addAllEmbedded(ArrayList<AnalogicalObject> analogicalObjects);
    ArrayList<AnalogicalObject> getChildren();
    ArrayList<Predicate> getAllChildren();
}

