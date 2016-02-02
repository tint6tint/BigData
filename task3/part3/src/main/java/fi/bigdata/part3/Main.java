package fi.bigdata.part3;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import fi.bigdata.part3.estimators.CardinalityEstimator;
import fi.bigdata.part3.estimators.StatisticsCardinalityEstimator;

/**
 * Created by kjanowsk on 13.12.2014.
 */
public class Main {

    private static final int ELEMENTS_TO_PRODUCE = 100000;

    public static void main(String[] args) throws IOException {

        Set<String> actuallyUniqueValues = readInputSet();

        // algorithm 1
        for (int k = 10; k < ELEMENTS_TO_PRODUCE / 10; k *= 10) {
            CardinalityEstimator estimator = new StatisticsCardinalityEstimator(getDataInputStream(), k);
            int estimate = estimator.estimateUniqueValues();
            System.out.println("k = " + k + ": estimated value=" + estimate + "; actual value="
                + actuallyUniqueValues.size() + "; error = " + Math.abs(actuallyUniqueValues.size() - estimate));
        }
    }

    private static DataInputStream getDataInputStream() throws IOException{
        Process proc = Runtime.getRuntime().exec("java -jar ../generator.jar " + ELEMENTS_TO_PRODUCE);
        DataInputStream is = new DataInputStream(new BufferedInputStream(proc.getInputStream()));
        return is;
    }

    /**
     * Used only for testing purposes
     * 
     * @return
     * @throws IOException
     */
    private static Set<String> readInputSet() throws IOException {
        Set<String> set = new HashSet<String>(ELEMENTS_TO_PRODUCE);
        DataInputStream is = getDataInputStream();
        String line;
        while ((line = is.readLine()) != null) {
            set.add(line);
        }
        is.close();

        return set;
    }
}
