package org.main;

import org.main.Interfaces.Predicate;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IllegalArgumentException {
        Clause c1 = new Clause("serve", "priest");
        Clause c2 = new Clause("some", "congregation");
        Clause c3 = new Clause();
        c3.setName("that");
        Clause c4 = new Clause();
        c4.setName("perform");
        Clause c5 = new Clause();
        c5.setName("for");
        Clause c6 = new Clause("some", "god");
        Clause c7 = new Clause("some", "worship");
        c1.addEmbedded(c2);
        c2.addEmbedded(c3);
        c3.addEmbedded(c4);
        c4.addEmbedded(c5);
        c4.addEmbedded(c7);
        c5.addEmbedded(c6);




        String input = ")(()()())())()))))())";
        System.out.println(AnalogyManager.ConvertToOOP(input));
    }
}
