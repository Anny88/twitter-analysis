/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javatweet;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class NLP {
    static StanfordCoreNLP pipeline;

    public static void init() {
            pipeline = new StanfordCoreNLP("MyPropFile.properties");
    }

    public static int findSentiment(String tweet) {

        double mainError = 0;
        String mainProb = "";
        int mainSentiment = 0;
        if (tweet != null && tweet.length() > 0) {
                tweet = preprocess(tweet);
                //System.out.println(tweet);
                int longest = 0;
                Annotation annotation = pipeline.process(tweet);
                for (CoreMap sentence : annotation
                                .get(CoreAnnotations.SentencesAnnotation.class)) {
                        Tree tree = sentence
                                        .get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                        int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                        //double prob = RNNCoreAnnotations.getPredictedClassProb(tree);
                        //double error = RNNCoreAnnotations.getPredictionError(tree);
                        //String p = String.format("%.3f", RNNCoreAnnotations.getPredictedClassProb(tree));
                        String partText = sentence.toString();
                        if (partText.length() > longest) {
                                mainSentiment = sentiment;
                                //mainProb = p;
                                //mainError = error;
                                longest = partText.length();
                        }

                }
        }
        
        return mainSentiment;
        
    }
    //preprocessing = clean text
    public static String preprocess (String s){
       // System.out.println(s);
        //String str = s;
        String str = s.replaceAll(" ?#|(?<=#\\w{0,100})_", " ").trim();
        //str.replace("#"," ");
        //str.replace("  "," ");        
        //System.out.println(str);
        List<String> words = new ArrayList<>(Arrays.asList(str.split(" ")));
        List<String> stopWords = Arrays.asList("in", "is", "at", "on", "the","RT", "a", "an");
        for (int i=0; i< words.size(); i++){
            String w = words.get(i);
            //System.out.println(w+" ");
            if ((stopWords.contains(w)) || (w.matches("(http|@).*")) ){
                words.remove(w);                
            }
        }        
        String strg = String.join(" ", words);
        //System.out.println(strg);
        
        strg.replaceAll("[^a-zA-Z0-9]+","");
        return strg;
    }
}


