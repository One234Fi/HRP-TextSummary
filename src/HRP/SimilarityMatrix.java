/*
 Similarity matrix that implements cosine similarity
 */
package HRP;

import java.util.TreeSet;
import java.util.StringTokenizer;

/**
 * TODO: rank sentences, add TF-IDF?
 * 
 * matrix of X sentences by Y sentences. any value of [n, n] will be 1.0
 * for values of [n, m], calculate with cosineSimilarity(sentenceN, sentenceM)
 * 
 * COSINE SIMILARITY:
 * use a treeset to get all the words out of each sentence in order without duplicates
 * then count the occurences of each word in the two sentences
 * calculate cosine similarity
 * return
 */

/**
 *
 * @author ethan
 */
public class SimilarityMatrix {
    private String[] sentences;
    private double [][] simMatrix;
    
    public SimilarityMatrix(String[] sentenceList) {
        sentences = sentenceList;
        simMatrix = new double[sentences.length][sentences.length];
        
        for (int i = 0; i < simMatrix.length; i++) {
            for (int j = 0; j < simMatrix.length; j++) {
                simMatrix[i][j] = cosineSimilarity(sentences[i], sentences[j]);
            }
        }
    }
    
    //this probably could be broken up into a couple separate methods for readability
    //ALSO wow this is some garbage time scaling
    private double cosineSimilarity(String a, String b) {
        TreeSet<String> tempSet = new TreeSet<String>();
        StringTokenizer st = new StringTokenizer(a + " " + b, " ");
        
        while (st.hasMoreTokens()) {
            //String temp = st.nextToken();
            //System.out.println(temp);
            tempSet.add(st.nextToken());
        }
        
        
        String [] sentence_1 = a.split(" ");
        String [] sentence_2 = b.split(" ");
        int[] vector_A = new int[tempSet.size()];
        int[] vector_B = new int[tempSet.size()];
        
        int i = 0;
        String temp;
        while (!tempSet.isEmpty()) {
            temp = tempSet.pollFirst();
            
            int c1 = 0;
            for (String s : sentence_1) {
                if (temp.equalsIgnoreCase(s)) {
                    c1++;
                }
            }
            
            int c2 = 0;
            for (String s : sentence_2) {
                if (temp.equalsIgnoreCase(s)) {
                    c2++;
                }
            }
            
            vector_A[i] = c1;
            vector_B[i] = c2;
        }
        
        //Formula (A.B) / (||A||*||B||)
        double cosineSim = dotProduct(vector_A, vector_B) / (magnitude(vector_A) * magnitude(vector_B));
        
        return cosineSim;
    }
    
    //A and B must be the same length for this
    private double dotProduct(int[] A, int[] B) {
        double DP = 0.0;
        
        for (int i = 0; i < A.length; i++) {
            DP += (A[i] * B[i]);
        }
        
        return DP;
    }
    
    private double magnitude (int[] vector) {
        double result = 0.0;
        
        for (int i : vector) {
            result += Math.pow(i, 2);
        }
        
        result = Math.sqrt(result);
        
        return result;
    }
    
}
