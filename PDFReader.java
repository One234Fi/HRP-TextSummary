/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package nlp;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Contains the main method and the pdf text stripper
 *
 * @author ethan
 */
public class PDFReader {

    /**
     * Handles user IO and starts the IRSystem
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String filePath = "C:/Users/ethan/OneDrive/Documents/Honors Research Project/ResearchPapers/3486674.pdf";
        
        Scanner sc = new Scanner(System.in);
        IRSystem IR;
        boolean text = false;
        if (text) {
            String s = sc.nextLine();
             IR = new IRSystem(s);
        }
        else {
        
            
        String path;
        System.out.println("Please enter a pdf file path...");
        //path = sc.nextLine();
        
        path = filePath;
        
        while (!isValidPath(path)) {
            System.out.println("Couldn't find that file, please enter a valid path...");
            path = sc.nextLine();
        }
        
        System.out.println("Please enter a similarity threshold between 0.0 and 1.0, (default value is 0.7)...");
        //double thresh = sc.nextDouble();
        double thresh = 0.7;
        while(thresh < 0.0 || thresh > 1.0) {
            System.out.println("Similarity threshold must be between 0.0 and 1.0. Please input a threshold...");
            thresh = sc.nextDouble();
        }
        
        IR = new IRSystem(path, thresh);
        
        }
        
        IR.start();
        //IR.printText();
    }
    
    /**
     * Checks if a file at a file path exists
     * 
     * @param path a string representing a file path
     * @return a boolean representing whether or not the file path is valid
     */
    static boolean isValidPath(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException ex) {
            return false;
        }
        return true;
    }
    
    /**
     * Retrieves the text from a pdf file
     *
     * @param fileName the file path to a pdf file
     * @return the text from the pdf file as a string
     * @throws IOException
     */
    public static String getText(String fileName) throws IOException {
        PDDocument doc = null;
        
        try {
            doc = PDDocument.load(new File(fileName));
            PDFTextStripper stripper = new PDFTextStripper();
            String pdfText = stripper.getText(doc);
            return pdfText;
        }
        finally {
            if (doc != null) {
                doc.close();
            }
        }
    }
    
    
}
