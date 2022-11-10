/*
 Similarity matrix that implements cosine similarity
 */
package HRP;

import java.util.Arrays;
import java.util.TreeSet;
import java.util.StringTokenizer;

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
 *
 * @author ethan
 */
public class SimilarityMatrix {

    private String[] sentences;
    private double[][] simMatrix;

    public SimilarityMatrix(String[] sentenceList) {
        sentences = sentenceList;
        simMatrix = new double[sentences.length][sentences.length];

        for (int i = 0; i < simMatrix.length; i++) {
            for (int j = 0; j < simMatrix.length; j++) {
                simMatrix[i][j] = cosineSimilarity(sentences[i], sentences[j]);
                //System.out.println(simMatrix[i][j]);
            }
        }
    }

    //Calculate cosine similarity. There's rounding errors or something?
    private double cosineSimilarity(String a, String b) {
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

    //determine a vector based on the frequency of each word in a sentence
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

    //Takes the length of the smaller of the two vectors
    public double dotProduct(double[] A, double[] B) {
        int minLength = Math.min(A.length, B.length);
        double DP = 0.0;

        for (int i = 0; i < minLength; i++) {
            DP += (A[i] * B[i]);
        }

        return DP;
    }

    //calculate the magnitude of a vector
    public double magnitude(double[] vector) {
        double result = 0.0;

        for (double d : vector) {
            result += Math.pow(d, 2);
        }

        result = Math.sqrt(result);

        return result;
    }

    //return a string of the matrix
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
