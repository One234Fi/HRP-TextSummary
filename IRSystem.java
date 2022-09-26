
package HRP;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.ArrayList;

public class IRSystem {
    //The files from the directory
    private File[] collection;
    //The strings pulled from each file
    private String[] collectionStrings;
    //2D arraylist that stores sentences for each file
    private ArrayList sentences;
    //Store the path for the directory
    private File directory;
    
    //Constructor initializes lists
    public IRSystem() {
        collection = getFiles();
        collectionStrings = convertFiles();
        sentences = new ArrayList<ArrayList<String>>();
    }
    
    //get all the files from a directory
    public File[] getFiles() {
        File[] files = null;
        try {
            System.out.println("");
            System.out.println("Enter directory...");
            Scanner sc = new Scanner(System.in);
            File dir = new File(sc.nextLine());
            files = dir.listFiles();
            directory = dir;
            System.out.println("");
        }
        catch (Exception e) {
            System.out.println("Caught error in getFiles: " + e.toString());
        }
        return files;
    }
    
    //turn the pdf files into strings
    public String[] convertFiles() {
        String[] strings = new String[collection.length];
        
        try {
            for (int i = 0; i < collection.length; i++) {
                strings[i] = PDFReader.getText(collection[i].getAbsolutePath());
            }
        }
        catch (IOException e) {
            System.out.println("Caught error in convertFiles: " + e.toString());
        }
        
        return strings;
    }
    
    //TODO: store processed pdfs as text files for quicker computation/access later
    public void storeFilesAsText() {
        for (File f : collection) {
            
        }
    }
    
    public void start() {
        try {
            for (String s : collectionStrings) {
                //Split into sentences: TODO, figure out how to ignore abbreviations and stuff maybe
                StringTokenizer st = new StringTokenizer (s, ".");
                ArrayList temp = new ArrayList<String>();
                while (st.hasMoreTokens()) {
                    String sentence = st.nextToken();
                    temp.add(sentence);
                }
                sentences.add(temp);
            }
        }
        catch (Exception e) {
            System.out.println("Error in start: " + e.toString());
        }
    }
    
    //prints all the collected text
    public void printText() {
        for (String s : collectionStrings) {
            System.out.println(s);
            System.out.println("");
            System.out.println("===================================");
            System.out.println("");
        }
    }
    
    //Tokenize and clean text
    String process(String w) {
        String s = w.toLowerCase();
        s = s.replaceAll("\\P{Print}", "");
        s = s.replaceAll("\\p{Punct}", "");
        //s = s.replaceAll("\\d", "");
        
        //Remove links and number strings/math expressions
        if (s.contains("http") || s.matches(".*\\d+.*")) {
            return "";
        }
        
        //TODO: Lemmaticise and stuff
        
        if (s.equalsIgnoreCase("am") || s.equalsIgnoreCase("are") || s.equalsIgnoreCase("is")) {
            return "be";
        }
        
        return s;
    }
    
    //I don't think I'll need this but I felt like making it anyway
    String soundex (String s) {
        //Retain first letter
        String result = s.substring(0, 1);
        
        //Change    aeiouhwy to 0
        //          bfpv to 1
        //          cgjkqsxz to 2
        //          dt to 3
        //          l to 4
        //          mn to 5
        //          r to 6
        for (int i = 1; i < s.length(); i++) {
            switch (s.charAt(i)) {
                case 'a', 'e', 'i', 'o', 'u', 'h', 'w', 'y' -> result = result + "0";
                case 'b', 'f', 'p', 'v' -> result = result + "1";
                case 'c', 'g', 'j', 'k', 'q', 's', 'x', 'z' -> result = result + "2";
                case 'd', 't' -> result = result + "3";
                case 'l' -> result = result + "4";
                case 'm', 'n' -> result = result + "5";
                case 'r' -> result = result + "6";
            }
        }
        
        //remove one from each pair of consecutive identical digits
        for (int i = 0; i < result.length()-1; i++) {
            if (result.charAt(i) == result.charAt(i+1)) {
                
            }
        }
        
        //remove all zeros from result
        
        //if less than four, pad with zeros
        
        //return the first four characters
        
        return s;
    }
    
}
