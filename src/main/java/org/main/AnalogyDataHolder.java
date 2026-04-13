package org.main;

import org.main.Objects.RewriteRule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

// hold all the analogies, is incharge of parsing and what not
public class AnalogyDataHolder {

    ConcurrentHashMap<String,String> Analogies = new ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger(RewriteRule.class.getName());

    public static void addAnalogiesFromFile(String filename) throws IOException {
       int linesNumber = getLineNumber(filename);
       System.out.println(linesNumber);
       int sets = linesNumber/4;
       System.out.println(sets + " " + sets * 4);
    }

    private static int getLineNumber(String filename){
        int counter = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            while (br.readLine() != null) {
                counter++;
            }
        } catch (IOException e) {
            logger.warning("File not found, no rules will be written to");
        }
        return counter;
    }


    static class sortAnalogies extends Thread{
        String filename;
        int startLine;
        int endline;
        sortAnalogies(String filename,int startLine,int endline){

        }


    }



}
