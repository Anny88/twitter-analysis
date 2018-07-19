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
import java.util.Properties;
import java.util.StringTokenizer;



public class NLP {
    static StanfordCoreNLP pipeline;

    public static void init() {
         pipeline = new StanfordCoreNLP("MyPropFile.properties");
    }

    public static int findSentiment(String tweet) {

    
        //double mainError = 0;
        //String mainProb = "";
        //String tweet = "";
        int mainSentiment = 0;
        if (tweet != null && tweet.length() > 0) {
                tweet = preprocess(tweet);
                //System.out.println(tweet);
                int longest = 0;
                Annotation annotation = pipeline.process(tweet);
                //Annotation annotation = new Annotation("So many great deals.");
                //pipeline.annotate(annotation);
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
        //System.out.println(s);
        String str = "";
        
        //str.replace("#"," ");
        //str.replace("  "," ");        
        //System.out.println(str);
       // List<String> words = new ArrayList<>(Arrays.asList(str.split(" ")));
        StringTokenizer st = new StringTokenizer(s);
        List<String> stopWords = Arrays.asList("RT", "URL", "the", "The","A", "a", "An", "an", "Is", "is", "to", "it", "of");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
           //System.out.println(token);
            
           if (!((stopWords.contains(token)) || (token.matches("(http|@|#).*"))) ){
                str = str + token + " ";
            }
        }
        //System.out.println(str);
        /*for (int i=0; i< words.size(); i++){
            String w = words.get(i);
            System.out.println(w+" ");
            if ((stopWords.contains(w)) || (w.matches("(http|@|#).*")) ){
                words.remove(w);                
            }
        }  */      
        //String strg = String.join(" ", words);
        //System.out.println(strg);
        //str = str.replaceAll(" ?#|(?<=#\\w{0,100})_", " ").trim();
        str = str.replaceAll("[^,.a-zA-Z ]+","");
        return str;
    }
}


