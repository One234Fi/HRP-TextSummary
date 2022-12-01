package HRP;

import java.util.Arrays;
import java.util.TreeSet;
import java.util.StringTokenizer;
import java.util.ArrayList;

/**
 * TODO: rank sentences, add TF-IDF?
 *
 * matrix of X sentences by Y sentences. any value of [n, n] will be 1.0 for
 * values of [n, m], calculate with cosineSimilarity(sentenceN, sentenceM)
 *
 * COSINE SIMILARITY: use a treeset to get all the words out of each sentence in
 * order without duplicates then count the occurences of each word in the two
 * sentences calculate cosine similarity return
 */

/**
 * Object that implements cosine similarity methods
 * 
 * @author ethan
 */
public class SimilarityMatrix {

    //the stringdict of sentences to compare
    private final StringDict sentences;
    private double[][] simMatrix;

    /**
     * constructor
     *
     * @param sentenceList: the list of sentences to compare
     */
    public SimilarityMatrix(StringDict sentenceList) {
        sentences = sentenceList;
        simMatrix = new double[sentences.length()][sentences.length()];

        for (int i = 0; i < simMatrix.length; i++) {
            for (int j = 0; j < simMatrix.length; j++) {
                simMatrix[i][j] = cosineSimilarity(sentences.get(i), sentences.get(j));
                //System.out.println(simMatrix[i][j]);
            }
        }
    }

    /**
     * constructor
     *
     * @param sentenceList: the list of sentences to compare
     * @param generateMatrix: boolean that determines whether or not to actually generate the matrix
     */
    public SimilarityMatrix(StringDict sentenceList, boolean generateMatrix) {
        sentences = sentenceList;
        
        if (generateMatrix) {
            
            simMatrix = new double[sentences.length()][sentences.length()];

            for (int i = 0; i < simMatrix.length; i++) {
                for (int j = 0; j < simMatrix.length; j++) {
                    simMatrix[i][j] = cosineSimilarity(sentences.get(i), sentences.get(j));
                    //System.out.println(simMatrix[i][j]);
                }
            }
        }
    }
    
    /**
     * @param threshold: a double that determines where to cut off the strings selected
     * @return a list of sentences above the similarity threshold
     */
    public ArrayList<String> similarSentences(double threshold) {
        ArrayList<String> output = new ArrayList<String>();
        
        for (int i = 0; i < sentences.length(); i++) {
            for (int j = 0; j < sentences.length(); j++) {
                if(i != j) {
                    if (cosineSimilarity(sentences.get(i), sentences.get(j)) >= threshold && cosineSimilarity(sentences.get(i), sentences.get(j)) < 0.9999999999999998) {
                        output.add(sentences.getBase(i));
                        output.add(sentences.getBase(j));
                        //System.out.println("sentences[" + i + "]: " + sentences.getBase(i));
                        //System.out.println("sentences[" + j + "]: " + sentences.getBase(j));
                        //System.out.println("Cosine similarity: " + cosineSimilarity(sentences.get(i), sentences.get(j)));
                    }
                }
            }
        }
        return output;
    }
    
    /**
     * calculates cosine similarity of two strings
     *
     * @param a: the first string
     * @param b: the second string
     * @return a double representing the cosine similarity
     */
    public double cosineSimilarity(String a, String b) {
        TreeSet<String> tempSet = new TreeSet<>();
        StringTokenizer st = new StringTokenizer(a + " " + b, " ");

        while (st.hasMoreTokens()) {
            String temp = st.nextToken();
            //System.out.println(temp);
            tempSet.add(temp);
        }

        String[] sentence_1 = a.split(" ");
        String[] sentence_2 = b.split(" ");
        //System.out.println(Arrays.toString(sentence_1));
        //System.out.println(Arrays.toString(sentence_2));

        double[] vector_A = calculateVector(tempSet, sentence_1);
        double[] vector_B = calculateVector(tempSet, sentence_2);

        //System.out.println(Arrays.toString(vector_A));
        //System.out.println(Arrays.toString(vector_B));
        //Formula (A.B) / (||A||*||B||)
        double cosineSim = dotProduct(vector_A, vector_B) / (magnitude(vector_A) * magnitude(vector_B));

        return cosineSim;
    }

    /**
     * determine a vector based on the frequency of each word in a sentence
     * 
     * @param ts: a tree set containing a list of words to count
     * @param sentence: a string array of words
     * @return a normalized term frequency vector
     */
    public double[] calculateVector(TreeSet<String> ts, String[] sentence) {
        double[] vect = new double[ts.size()];

        //COPY the set so that it doesn't get deleted
        TreeSet<String> tempSet = new TreeSet(ts);
        int point = 0;
        while (!tempSet.isEmpty()) {
            double sum = 0.0;
            String temp = tempSet.pollFirst();
            for (String s : sentence) {
                if (temp.equalsIgnoreCase(s)) {
                    sum++;
                }
            }
            //normalize based on length
            sum /= sentence.length;
            vect[point] = sum;
            point++;
        }

        return vect;
    }

    /**
     * Takes the length of the smaller of the two vectors
     *
     * @param A: an vector of doubles
     * @param B: another vector of doubles
     * @return the dot product of two double vectors
     */
    public double dotProduct(double[] A, double[] B) {
        int minLength = Math.min(A.length, B.length);
        double DP = 0.0;

        for (int i = 0; i < minLength; i++) {
            DP += (A[i] * B[i]);
        }

        return DP;
    }

    /**
     * @param vector: a double vector
     * @return the magnitude of the vector as a double
     */
    public double magnitude(double[] vector) {
        double result = 0.0;

        for (double d : vector) {
            result += Math.pow(d, 2);
        }

        result = Math.sqrt(result);

        return result;
    }

    /**
     * @return the similarity matrix as a string (note: this takes several minutes with large files)
     */
    @Override
    public String toString() {
        String out = "";
        for (double[] d : simMatrix) {
            out += Arrays.toString(d);
            out += "\n";
        }
        return out;
    }
}
