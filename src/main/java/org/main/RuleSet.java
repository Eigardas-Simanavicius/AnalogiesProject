package org.main;

import java.security.InvalidParameterException;
import java.util.*;

public class RuleSet {

    private final String ruleFileDir = "rewrite rules.txt";

    private static TreeMap<String, List<String>> rules;
    private TreeMap<String,List<rewriteRule>> parsedRules;

    public RuleSet(){
        if(rules.isEmpty()){
            loadRulesFromFile();
        }
    }

    private void loadRulesFromFile(){
        Scanner scanner = new Scanner(ruleFileDir);

        for(String line : scanner.delimiter().split("\n")){
            ArrayList<String> delimitedLine = new ArrayList<>(List.of(line.split(" ")));

            rules.put(
                    delimitedLine.removeFirst(),
                    delimitedLine.stream().map(x -> x.replace(",","").trim()).toList()
            );
        }
    }

    public ArrayList<String> getRulesForAsString(String ruleSubject){
        return (ArrayList<String>) rules.get(ruleSubject);
    }

    public ArrayList<rewriteRule> getRulesFor(String ruleSubject){
        List<rewriteRule> r;
        if((r = parsedRules.get(ruleSubject)) != null ){
            return (ArrayList<rewriteRule>) r;
        }else{
            return (ArrayList<rewriteRule>) parseRules(ruleSubject, rules.get(ruleSubject));
        }
    }

    private List<rewriteRule> parseRules(String ruleSubject,List<String> stringRules){
        if(stringRules == null) return Collections.emptyList();

        ArrayList<rewriteRule> rewriteRules = new ArrayList<>(stringRules.size());

        for(String stringRule : stringRules){
            try {
                rewriteRules.add(new rewriteRule(ruleSubject, stringRule));
            }catch(InvalidParameterException e){
                e.printStackTrace();
                System.out.println(e.getMessage() + " : " + stringRule);
            }// Handles poorly formatted rules strings
        }

        parsedRules.put(ruleSubject,rewriteRules);
        return rewriteRules;
    }
}
