package org.main;

import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

// exists to read config , and do stuff with it
public class ConfigSetup {
    private static final Logger logger = Logger.getLogger(ConfigSetup.class.getName());
    public static void applyConfig(String filepath){
        try{
            File configFile = new File(filepath);
            setupConfig(configFile);
        }catch (Exception e){
            findConfig();
        }
    }

    public static void findConfig(){
        try {
            File configFile = new File("config.txt");
            if(configFile.createNewFile()) {
                createDefaultConfig(configFile);
            }else { setupConfig(configFile);}
        }catch (Exception e){

        }

    }

    private static void createDefaultConfig(File configFile) throws IOException {

        configFile.createNewFile();
        logger.log(Level.WARNING, "No config found or provided, will create default file, empty by default,nothing will be loaded");
    }

    private static void setupConfig(File config) {
        System.out.println("here");

        try (Scanner myReader = new Scanner(config)) {
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                String[] currLine = line.replace(" ","").split("=");
                if(currLine[0].equals("rules")){
                    //new RuleSet(line.split("=")[1]);
                    // TODO fix when ruleset works
                }else if(currLine[1].equals("analogies")){
                    //TODO analogyDataHolder will do stuff here , for next week tho
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
