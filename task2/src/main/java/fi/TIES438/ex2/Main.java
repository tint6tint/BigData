package fi.TIES438.ex2;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by kjanowsk on 19.11.2014.
 */
public class Main {

    private static final double probabilityPlusMinus = 0.15;

    private static final double getError(double distance, double probability) {
        return Math.abs(distance - probability);
    }

    public static void main(String[] args) throws Exception {

        // PART 1
        List<Set<String>> allSets = new LinkedList<Set<String>>();
        List<String> allTerms = DocumentProcessor.executePart1(allSets);
        System.out.println("Found " + allSets.size() + " documents");

        // PART 2 - in DocumentProcessor.calculateJaccardDistance()
        // in any case, you can check out DocumentProcessorTest.testCalculateJaccardDistance() to be make sure it works
        // correctly

        // PART 3,4 - calculating jaccard distance and minhash probability to make sure they're similar

        // change the limit to some other value in order to compare more documents
        for (int i = 0; i < 100; i++) {
            System.out.println("Comparison " + (i + 1) + ": ");
            double jaccardDistance = DocumentProcessor.calculateJaccardDistance(allSets.get(i), allSets.get(i + 1));

            double minhashProbabilityN5 = DocumentProcessor.calculateMinhashProbability(allSets.get(i),
                allSets.get(i + 1), 5);

            double minhashProbabilityN10 = DocumentProcessor.calculateMinhashProbability(allSets.get(i),
                allSets.get(i + 1), 10);

            double minhashProbabilityN20 = DocumentProcessor.calculateMinhashProbability(allSets.get(i),
                allSets.get(i + 1), 20);

            double minhashProbabilityN40 = DocumentProcessor.calculateMinhashProbability(allSets.get(i),
                allSets.get(i + 1), 40);

            double minhashProbabilityN80 = DocumentProcessor.calculateMinhashProbability(allSets.get(i),
                allSets.get(i + 1), 80);

            System.out.println("Jaccard distance: " + jaccardDistance);
            System.out.println(getError(jaccardDistance, minhashProbabilityN5) + "\t"
                + getError(jaccardDistance, minhashProbabilityN10) + "\t"
                + getError(jaccardDistance, minhashProbabilityN20) + "\t"
                + getError(jaccardDistance, minhashProbabilityN40) + "\t"
                + getError(jaccardDistance, minhashProbabilityN80));
        }

    }
}
