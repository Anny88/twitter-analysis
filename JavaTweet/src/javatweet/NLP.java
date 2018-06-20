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



        int mainSentiment = 0;
        if (tweet != null && tweet.length() > 0) {
                tweet = preprocess(tweet);
                int longest = 0;
                Annotation annotation = pipeline.process(tweet);
                for (CoreMap sentence : annotation
                                .get(CoreAnnotations.SentencesAnnotation.class)) {
                        Tree tree = sentence
                                        .get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                        int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                        String partText = sentence.toString();
                        if (partText.length() > longest) {
                                mainSentiment = sentiment;
                                longest = partText.length();
                        }

                }
        }
        return mainSentiment;
    }
    //preprocessing
    public static String preprocess (String s){
        System.out.println(s);
        String str = s;
                //.replace("#","");
        System.out.println(s);
        List<String> words = new ArrayList<>(Arrays.asList(str.split(" ")));
        List<String> stopWords = Arrays.asList("in", "is", "at", "on", "the","RT");
        for (int i=0; i< words.size(); i++){
            String w = words.get(i);
            System.out.print(w+" ");
            if ((stopWords.contains(w)) || (w.matches("(http|@).*")) ){
                words.remove(w);                
            }
        }
        
        str = String.join(" ", words);
        str.replaceAll("[^a-zA-Z0-9]+","");
        return str;
    }
}


