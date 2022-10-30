/*
 Similarity matrix that implements cosine similarity
 */
package HRP;

/**
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
    
    private double cosineSimilarity(String a, String b) {
        
        return 0.0;
    }
    
}
