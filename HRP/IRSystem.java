package HRP;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.ArrayList;

/**
 * A class that handles most of the file stuff and text cleaning for this program,
 * kinda the "main" class that calls all the other functions if you think of the other classes as "methods"
 * 
 * @author ethan
 */
public class IRSystem {

    //the pdf file to be summarized
    private final File inputFile;
    //The strings pulled from the file
    private final String convertedFile;
    //Custom dictionary to store sentences in both modified and unmodified form
    private final StringDict sentences;
    //list of stop words to use 
    private final ArrayList<String> stopWords;
    //similarity threshold for sentence comparision
    private final double threshold;

    /**
     * Constructor
     *
     * @param filePath: string path to a file
     */
    public IRSystem(String filePath) {
        inputFile = new File(filePath);
        convertedFile = convertFile();
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
     * starts the program and calls all the methods needed to make this thing work
     */
    public void start() {
        int i = 0;
        String sentence = "";
        try {
            //Split into sentences
            StringTokenizer st = new StringTokenizer(convertedFile, ".");

            //remove whitespace, then add sentence to the stringDict, then also add the cleaned sentence
            while (st.hasMoreTokens()) {
                sentence = st.nextToken();
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
        
        
        System.out.println("Removing stop words...");
        removeStopWords();
        System.out.println("Generating summary...\n\n\n");
        
        SimilarityMatrix simMat = new SimilarityMatrix(sentences, false);
        ArrayList<String> simPairs = simMat.similarSentences(threshold);
        
        
        ArrayList<String> output = new ArrayList<>();
        for (String s : simPairs) {
            if (!output.contains(s)) {
                output.add(s);
            }
        }
        
        for (String s : output) {
            System.out.println(s);
        }
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
     * @return a string with punctuation, numbers, and invalid characters removed
     */
    private String cleanSentence(String sentence) {
        String temp = sentence.toLowerCase();
        temp = temp.replaceAll("\\p{Punct}|\\P{Print}|[0-9]", "");
        return temp;
    }

    /**
     * Iterates through each sentences and removes the stop words from them
     * 
     * This is extremely slow because it has to iterate through 
     * the list of stop words for every sentence
     * 
     * So the time complexity is something along the lines of O(n^3) plus however long clearUselessData() takes
     */
    private void removeStopWords() {
        for (int i = 0; i < sentences.length(); i++) {
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
        sentences.clearUselessData();
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
    private ArrayList parseStopWords() {
        ArrayList<String> temp = new ArrayList();

        try {
            Scanner sc = new Scanner(new File("src/HRP/stopWords.txt"));
            while (sc.hasNext()) {
                temp.add(sc.next());
            }
        } catch (Exception e) {
            System.out.println("Caught error in parseStopWords: " + e.toString());
        }

        return temp;
    }

}
