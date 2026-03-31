package org.main;

import org.main.Interfaces.AnalogicalObject;
import org.main.Interfaces.Predicate;

import java.util.Iterator;

public class MappingManager {
    public static Boolean canMap(Predicate head1, Predicate head2){
        Iterator<AnalogicalObject> struct1 = ((Clause) head1).getPreOrderIterator();
        Iterator<AnalogicalObject> struct2 = ((Clause) head1).getPreOrderIterator();
        while (struct1.hasNext()){
            // this does not look create right now, will fix later
            if(struct1 instanceof Clause){
                System.out.println("here");
                if(((Clause) struct1).getName().equals(((Clause) struct2).getName())){
                    struct1.next();
                    struct2.next();
                }
            }else if(struct1 instanceof Subject && struct2 instanceof Subject){
                if(((Subject) struct1).isHasAsterisk() && ((Subject) struct2).isHasAsterisk()){
                    struct1.next();
                    struct2.next();
                }
            }else{

                System.out.println(struct1.next().getClass());
                System.out.println(struct1.next() instanceof AnalogicalObject);
                System.out.println(struct2.next().getClass());
                return false;}
        }
        return true;
    }

    public static void preOrderTraversalTogether(Predicate curr1, Predicate curr2){

    }

}
