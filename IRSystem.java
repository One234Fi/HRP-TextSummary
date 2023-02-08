package nlp;

import static java.awt.SystemColor.text;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

/**
 * A class that handles most of the file stuff and text cleaning for this
 * program, kinda the "main" class that calls all the other functions if you
 * think of the other classes as "methods"
 *
 * @author ethan
 */
public class IRSystem {

    //the pdf file to be summarized
    private final File inputFile;
    //The strings pulled from the file
    private String convertedFile;
    //Custom dictionary to store sentences in both modified and unmodified form
    private final StringDict sentences;
    //list of stop words to use 
    private final Set<String> stopWords;
    //similarity threshold for sentence comparision
    private final double threshold;
    //the list of keywords extracted from the text via TF-IDF
    //private final ArrayList<String> keywords;

    /**
     * Constructor
     *
     * @param filePath: string path to a file
     */
    public IRSystem(String text) {
        inputFile = null;
        convertedFile = text;
        sentences = new StringDict();
        stopWords = parseStopWords();
        threshold = 0.6;
    }

    /**
     * Constructor
     *
     * @param filePath: string path to a file
     * @param threshold: double representing the similarity threshold
     */
    public IRSystem(String filePath, double threshold) {
        inputFile = new File(filePath);
        convertedFile = convertFile();
        sentences = new StringDict();
        stopWords = parseStopWords();
        this.threshold = threshold;
    }

    /**
     * Turn the pdf files into strings
     *
     * @return a string representation of the contents of the input pdf file
     */
    public String convertFile() {
        String convertedText = "";
        try {
            convertedText = PDFReader.getText(inputFile.getAbsolutePath());

        } catch (IOException e) {
            System.out.println("Caught error in convertFiles: " + e.toString());
        }

        return convertedText;
    }

    /**
     * starts the program and calls all the methods needed to make this thing
     * work
     */
    public void start() {
        //System.out.println(convertedFile);
        convertedFile = convertedFile.substring(0, convertedFile.lastIndexOf("REFERENCE"));

        int i = 0;
        String sentence = "";
        try {
            //Split into sentences
            StringTokenizer st = new StringTokenizer(convertedFile, ".");

            //remove whitespace, then add sentence to the stringDict, then also add the cleaned sentence
            while (st.hasMoreTokens()) {
                sentence = st.nextToken();
                //System.out.println(sentence);
                sentence = removeWhitespace(sentence);
                String cleanedSentence = cleanSentence(sentence);
                if (!cleanedSentence.isBlank()) {
                    sentences.put(i, sentence);
                    sentences.modify(i, cleanedSentence);

                    i++;
                }
            }
        } catch (Exception e) {
            System.out.println("Error in start: " + e.toString());
            System.out.println("Error at: " + i + " " + sentence);
        }

        //System.out.println("Removing stop words...");
        List<String> contentWords = removeStopWords();
        System.out.println("Generating summary...\n\n\n");

        SimilarityMatrix simMat = new SimilarityMatrix(sentences, false);
        ArrayList<String> simPairs = simMat.similarSentences(0.6);

        ArrayList<String> output = new ArrayList<>();
        for (String s : simPairs) {
            if (!output.contains(s)) {
                output.add(s);
            }
        }
        /*
        for (String s : output) {
            System.out.println(s);
        }
        */

        
        String[] keywords = getKeywords();
        //System.out.println(Arrays.toString(keywords));
    }

    /**
     * @param sentence: String
     * @return a string with the white space and newline characters removed
     */
    private String removeWhitespace(String sentence) {
        String temp = sentence.replaceAll("\n|[^ -~]", "").trim();
        return temp;
    }

    /**
     * @param sentence: String
     * @return a string with punctuation, numbers, and invalid characters
     * removed
     */
    private String cleanSentence(String sentence) {
        String temp = sentence.toLowerCase();
        temp = temp.replaceAll("\\p{Punct}|\\P{Print}|[0-9]", "");
        return temp;
    }

    /**
     * Iterates through each sentences and removes the stop words from them
     *
     * This is extremely slow because it has to iterate through the list of stop
     * words for every sentence
     *
     * So the time complexity is something along the lines of O(n^3) plus
     * however long clearUselessData() takes
     */
    private List removeStopWords() {
        List<String> wordList = new ArrayList<>(Arrays.asList(convertedFile.replaceAll("[\\s;]+", " ").trim().split(" ")));
        wordList.removeAll(stopWords);
        return wordList;

        /*for (int i = 0; i < sentences.length(); i++) {
            String string = sentences.get(i);
            StringTokenizer st = new StringTokenizer(string, " ");
            StringBuilder output = new StringBuilder();
            while (st.hasMoreTokens()) {
                String temp = st.nextToken();
                for (int j = 0; j < stopWords.size(); j++) {
                    if (temp.equalsIgnoreCase(stopWords.get(j))){
                        temp = "";
                    }
                }
                if (!temp.isBlank()) {
                    output.append(temp).append(" ");
                }
            }
            sentences.modify(i, output.toString());
        }
        sentences.clearUselessData();*/
    }

    //RAKE keyword extraction **UNFINISHED**
    /**
     * count how many times each content word appears across the candidate
     * phrases
     *
     * @return
     */
    public String[] getKeywords() {
        //String text = convertedFile.toLowerCase();

        //System.out.println("finding keywords");
        String[] all_words = convertedFile.split("\\W+");
        ArrayList<String> content = new ArrayList<>();
        for (String s : all_words) {
            String t = removeWhitespace(cleanSentence(s));
            if (!content.contains(t)) {
                content.add(t);
            }
        }

        //System.out.println("removing stopwords from content");
        //remove stop words from content word list
        content.removeAll(stopWords);

        //TODO candidate expressions
        //take text, split on stop words and 
        List<String> candidates = getCandidates(all_words);

        //System.out.println("computing RAKE matrix");
        int[][] matrix = new int[content.size()][content.size()];

        //TODO: this is reaaaaally slow, find a way to do it better/faster (takes like two minutes with the current sampleset)
        //3 loops kinda sucks but idk how to do this with better time complexity yet and I don't feel like spending a week researching it cause I'm not getting paid for this
        
        //need: count, co-occurence count
        //problem: lots of unused space
        //idea: compress 2d array into 1d array that counts co-occurences
        //make new array of same size to keep track of counts
        
        for (int i = 0; i < content.size(); i++) {
            //System.out.println("loop " + i);
            for (int j = 0; j < content.size(); j++) {
                for (String phrase : candidates) {
                    if (phrase.contains(content.get(i)) && phrase.contains(content.get(j))) {
                        //the mat is always symmetrical afaik so I could probably find a way to shortcut the calc but imma worry about that later
                        matrix[i][j]+=1;
                    }
                }
            }
        }
        
        //System.out.println("determining rankings");
        //for each word, calculate its score as the number of its co-ocuurences divided by its occurences 
        //aka (row sum divided by entry)
        double [] ranks = new double[content.size()];
        for (int i = 0; i < ranks.length; i++) {
            double sum = 0.0;
            // I can probably replace matrix[i].len with matrix.len but I don't need more edge cases to deal with rn
            for (int j = 0; j < matrix[i].length; j++) {
                sum += matrix[i][j];
            }
            
            if (matrix[0][i] != 0) {
                ranks[i] = sum / matrix[0][i];
            } 
            
        }
        
        //
        double[] scores = new double[candidates.size()];
        for (int i = 0; i < scores.length; i++) {
            String [] words = candidates.get(i).split(" ");
            double sum = 0.0;
            for (String s : words) {
                //ranks.get(content.indexOf(s)
                if (content.indexOf(s) != -1) {
                    sum += ranks[content.indexOf(s)];
                }
                
            }
            scores[i] = sum;
        }
        
        
        //testing that this works because I don't trust my spaghetti code
        for (int i = 0; i < candidates.size(); i++) {
            System.out.println(candidates.get(i) + " " + scores[i]);
        }
        
        
        //System.out.println("selecting best keywords");
        //now take the top 1/3 ranking words
        int num = content.size() / 5;
        String[] output = new String[num];
        //Double[] ranksCopy = (Double[]) DoubleStream.of(ranks).boxed().collect(Collectors.toCollection(ArrayList::new)).toArray();
        
        int t;
        for (int i = 0; i < num; i++) {
            //Collections.max(DoubleStream.of(ranks).boxed().collect(Collectors.toCollection(ArrayList::new)));
            
            //set output i to the string value of the max of ranks, then set the max of ranks to 0, repeat until output is full
            t = getMaxIndex(ranks);
            output[i] =  content.get(getMaxIndex(ranks));
            ranks[t] = 0.0;
        }
        

        return null;
    }

    //take text and split on stop words
    List<String> getCandidates(String[] words) {
        //the list of candidates
        List<String> candidates = new ArrayList<>();

        //the string that will become a candidate
        StringBuilder candidate = new StringBuilder();

        //split the list of words off of the list of stop words, I think this is O(n)ish so not the worst
        for (String s : words) {

            //if the word isn't a stop word, add it to the current candidate
            if (!stopWords.contains(s)) {
                candidate.append(s).append(" ");
            } //if the word is a stop word, add the current candidate to the list, then clear the candidate
            else {
                if (!candidate.toString().trim().isBlank()) {
                    candidates.add(candidate.toString().trim());
                    //System.out.println(candidate.toString() + ".");
                    candidate.setLength(0);
                }
            }
        }

        return candidates;
    }

    /**
     * Prints all the collected text
     */
    public void printText() {
        for (int i = 0; i < sentences.length(); i++) {
            System.out.println(sentences.get(i));
            System.out.println("");
            System.out.println("===================================");
            System.out.println("");
        }
    }

    /**
     * gets all of the stop words from the text file
     *
     * @return a list of stop words from stopWords.txt
     */
    private Set parseStopWords() {
        Set<String> stopWords = new TreeSet<>();

        try {
            stopWords.addAll(Files.readAllLines(Paths.get("src/nlp/stopWords.txt")));

        } catch (Exception e) {
            System.out.println("Caught error in parseStopWords: " + e.toString());
        }

        return stopWords;
    }
    
    //return the index of the max of an array
    public int getMaxIndex(double[] arr) {
        double max = arr[0];
        int index = 0;
        
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
                index = i;
            }
        }
        
        return index;
    }

}
