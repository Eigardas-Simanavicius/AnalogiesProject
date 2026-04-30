package org.main;

import org.main.Objects.CoalescentMapping;
import org.main.Objects.Config;

import java.io.IOException;
import java.util.logging.*;


public class Main {
    public static void main(String[] args){
        //logger initialization
        Logger rootLogger = Logger.getLogger("");
        try{
            //getting rid of logging errors to console in order to isolate them to the log file
            for(Handler handler : rootLogger.getHandlers()){
                rootLogger.removeHandler(handler);
            }

            FileHandler fileHandler = new FileHandler("logs/errorLog.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            rootLogger.addHandler(fileHandler);
            rootLogger.setLevel(Level.WARNING);
        }catch(IOException e){
            System.out.println("failed config " + e.getMessage());
        }

        //Config Init
        // no given config
        Config config;
        if(args.length == 0){
            config = ConfigSetup.findConfig();
        }else{
            config = ConfigSetup.applyConfig(args[0]);
        }
            ConfigSetup.resetConfig();
            config.setRewrite(true);
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *barbarian self) (display *barbarian self))");
        AnalogyDataHolder.addAnalogyToHash("(if (train.0 *Adonis body) (display *Adonis body))");
        CoalescentMapping mapping = MappingManager.createNewCoalesentMapping("Adonis","barbarian");
        System.out.println(mapping.getAnalogies());

    }
}
