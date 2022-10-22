/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package HRP;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
 
import java.io.File;
import java.io.IOException;

//DELETE BEFORE DISTRIBUTING
//Directory:   C:/Users/ethan/OneDrive/Documents/Honors Research Project/ResearchPapers

public class PDFReader {

    public static void main(String[] args) throws IOException {
        // provide the path to pdf file
        //String fileName = "C:/Users/ethan/OneDrive/Documents/Honors Research Project/ResearchPapers/3486674.pdf"; 
        
        //String text = getText(fileName);
        //System.out.println(text);
        
        IRSystem IR = new IRSystem("C:/Users/ethan/OneDrive/Documents/Honors Research Project/ResearchPapers/3486674.pdf");
        IR.start();
        IR.printText();
        
        
    }
    
    //Open a directory, strip the text, put text in new files
    
    
    
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
