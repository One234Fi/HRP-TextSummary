/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package HRP;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Scanner;

public class PDFReader {

    public static void main(String[] args) throws IOException {
        // provide the path to pdf file
        //String fileName = "C:/Users/ethan/OneDrive/Documents/Honors Research Project/ResearchPapers/3486674.pdf"; 
        
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter a pdf file path...");
        String path = sc.nextLine();
        while (!isValidPath(path)) {
            System.out.println("Couldn't find that file, please enter a valid path...");
            path = sc.nextLine();
        }
        
        System.out.println("Please enter a similarity threshold between 0.0 and 1.0, (default value is 0.6)...");
        double thresh = sc.nextDouble();
        while(thresh < 0.0 || thresh > 1.0) {
            System.out.println("Similarity threshold must be between 0.0 and 1.0. Please input a threshold...");
            thresh = sc.nextDouble();
        }
        
        IRSystem IR = new IRSystem(path, thresh);
        IR.start();
        //IR.printText();
    }
    
    //checks if a filepath exists
    static boolean isValidPath(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException ex) {
            return false;
        }
        return true;
    }
    
    //retrieves the text from a pdf file
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
