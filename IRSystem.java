package HRP;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.ArrayList;

/**
 *
 * @author ethan
 */
public class IRSystem {

    //The files from the directory
    //private File[] collection;
    private File inputFile;
    //The strings pulled from each file
    private String convertedFile;
    //Custom dictionary to store sentences in both modified and unmodified form
    private StringDict sentences;

    private ArrayList<String> stopWords;

    /**
     *
     * @param filePath
     */
    public IRSystem(String filePath) {
        inputFile = new File(filePath);
        convertedFile = convertFile();
        sentences = new StringDict();
        stopWords = parseStopWords();
    }

    /**
     * Turn the pdf files into strings
     *
     * @return
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
     *
     */
    public void start() {
        int i = 0;
        String sentence = "";
        try {
            //Split into sentences: TODO, figure out how to ignore abbreviations and stuff maybe
            StringTokenizer st = new StringTokenizer(convertedFile, ".");

            while (st.hasMoreTokens()) {
                sentence = st.nextToken();
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
        System.out.println("Building similarity matrix...");
        SimilarityMatrix simMat = new SimilarityMatrix(sentences, false);
        //System.out.println(simMat.toString());
        ArrayList<String> simPairs = simMat.similarSentences(0.8);
    }

    private String cleanSentence(String sentence) {
        String temp = sentence.toLowerCase();

        temp = temp.replaceAll("\n", "");
        temp = temp.replaceAll("\\p{Punct}", "");
        temp = temp.replaceAll("\\P{Print}", "");
        temp = temp.replaceAll("[0-9]" , "");

        //System.out.println(temp);
        return temp;
    }

    //This is bad pls find better before finishing
    private void removeStopWords() {
        for (int i = 0; i < sentences.length(); i++) {
            String string = sentences.get(i);
            StringTokenizer st = new StringTokenizer(string, " ");
            string = "";
            while (st.hasMoreTokens()) {
                String temp = st.nextToken();
                for (int j = 0; j < stopWords.size(); j++) {
                    if (temp.equalsIgnoreCase(stopWords.get(j))){
                        temp = "";
                    }
                }
                if (!temp.isBlank()) {
                    string += temp + " ";
                }
            }
            sentences.modify(i, string);
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

    private ArrayList parseStopWords() {
        ArrayList<String> temp = new ArrayList();

        try {
            Scanner sc = new Scanner(new File("C:/Users/ethan/Documents/NetBeansProjects/PDFBoxTake2/src/HRP/stopWords.txt"));
            while (sc.hasNext()) {
                temp.add(sc.next());
            }
        } catch (Exception e) {
            System.out.println("Caught error in parseStopWords: " + e.toString());
        }

        return temp;
    }

}
