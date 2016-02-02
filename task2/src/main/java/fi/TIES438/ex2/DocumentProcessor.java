package fi.TIES438.ex2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by kjanowsk on 19.11.2014.
 */
public final class DocumentProcessor {

    private static final int SHINGLE_LENGTH = 5;

    static Set<String> buildShingles(String terms) {
        Set<String> shingles = new HashSet<String>();

        if (terms.length() < SHINGLE_LENGTH) {
            shingles.add(terms);
        } else {
            for (int i = 0; i < terms.length() - SHINGLE_LENGTH; i++) {
                shingles.add(terms.substring(i, i + SHINGLE_LENGTH));
            }
        }

        return shingles;
    }

    /**
     * calculates intersection of two sets
     * 
     * @param d1
     *            first set to be calculated intersection of
     * @param d2
     *            second set to be calculated intersection of
     * @return intersection of set d1 and d2
     */
    private static Set<String> calculateIntersection(Set<String> d1, Set<String> d2) {
        Set<String> intersection = new HashSet<String>();
        for (String str : d1) {
            if (d2.contains(str)) {
                intersection.add(str);
            }
        }
        return intersection;
    }

    /**
     * Calculates the Jaccard distance between two sets - required in Part 2 of the exercise
     *
     * @param d1
     *            first set to be calculated the Jaccard distance of
     * @param d2
     *            second set to be calculated the Jaccard distance of
     * @return jaccard distance between sets d1 and d2
     */
    public static double calculateJaccardDistance(Set<String> d1, Set<String> d2) {
        Set<String> union = calculateUnion(d1, d2);
        Set<String> intersection = calculateIntersection(d1, d2);
        return 1 - ((double) intersection.size() / (double) union.size());
    }

    /**
     * Calculates Pr[minhash(s1) = minhash (s2)]
     *
     * @param s1
     * @param s2
     * @return
     */
    public static double calculateMinhashProbability(Set<String> s1, Set<String> s2, int n) {
        int equalSignature = 0;

        ArrayList<String> shingles = new ArrayList<String>(calculateUnion(s1, s2));
        Random r = new Random();

        for (int i = 0; i < n; i++) {
            Collections.shuffle(shingles, r);
            Integer[] signature = calculateSignature(shingles, s1, s2);
            if (signature[0] == signature[1]) {
                equalSignature++;
            }
        }

        return 1 - (equalSignature / (double) n);
    }

    /**
     * Calculates the minhashing signature
     * 
     * @param allShingles
     * @param setForSignature1
     * @param setForSignature2
     * @return a vector representing a signature
     */
    static Integer[] calculateSignature(List<String> allShingles, Set<String> setForSignature1,
        Set<String> setForSignature2) {
        Integer[] result = new Integer[2];
        int index = 1;
        for (String shingle : allShingles) {
            if (result[0] == null && setForSignature1.contains(shingle)) {
                result[0] = index;
            }
            if (result[1] == null && setForSignature2.contains(shingle)) {
                result[1] = index;
            }
            if (result[0] != null && result[1] != null) {
                break;
            }
            index++;
        }
        return result;
    }

    /**
     * Calculates union of sets
     *
     * @param sets
     *            sets to be joined into union
     * @return union of all sets provided
     */
    private static Set<String> calculateUnion(Set<String>... sets) {
        Set<String> union = new HashSet<String>();
        for (Set<String> set : sets) {
            union.addAll(set);
        }
        return union;
    }

    /**
     * Performs the actions described in part 1
     *
     * @param destinationAllSets
     *            destination list for list of sets of 2-grams (each set represents a separate document)
     * @throws Exception
     * @return All terms
     */
    public static List<String> executePart1(List<Set<String>> destinationAllSets) throws Exception {
        List<Set<String>> allSets = getAllSets();
        Set<String> allTerms = mergeSets(allSets);
        destinationAllSets.addAll(allSets);
        return new ArrayList<String>(allTerms);
    }

    static List<Set<String>> getAllSets() throws IOException {
        List<Set<String>> allSets = new LinkedList<Set<String>>();
        BufferedReader in = new BufferedReader(new InputStreamReader(
            DocumentProcessor.class.getResourceAsStream("/docs")));
        String line;
        while ((line = in.readLine()) != null) {
            allSets.add(buildShingles(line));
        }
        return allSets;
    }

    private static Set<String> mergeSets(List<Set<String>> allSets) {
        Set<String> allTerms = new HashSet<String>();
        for (Set<String> set : allSets) {
            allTerms.addAll(set);
        }
        return allTerms;
    }

    private DocumentProcessor() {

    }
}
