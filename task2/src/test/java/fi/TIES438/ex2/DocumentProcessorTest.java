package fi.TIES438.ex2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class DocumentProcessorTest {

    private boolean containsJaccardDistanceGreaterThanZero(List<Set<String>> allSets) {
        for (int i = 0; i < allSets.size() - 1; i++) {
            if (DocumentProcessor.calculateJaccardDistance(allSets.get(i), allSets.get(i + 1)) != 1) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testCalculateJaccardDistance() throws Exception {
        Set<String> d1 = new HashSet<String>();
        Set<String> d2 = new HashSet<String>();

        d1.add("a");
        d1.add("b");
        d1.add("c");
        d2.add("a");
        d2.add("d");
        d2.add("e");

        assertEquals(0.8, DocumentProcessor.calculateJaccardDistance(d1, d2), 0);
    }

    @Test
    public void testCalculateJaccardDistance_basedOnProvidedFile() throws Exception {
        List<Set<String>> allSets = new LinkedList<Set<String>>();
        DocumentProcessor.executePart1(allSets);

        assertTrue(containsJaccardDistanceGreaterThanZero(allSets));
    }

    @Test
    public void testCalculateMinhashProbability() {
        Set<String> d1 = new HashSet<String>();
        Set<String> d2 = new HashSet<String>();

        d1.add("a");
        d1.add("b");
        d1.add("c");
        d2.add("a");
        d2.add("d");
        d2.add("e");

        int n = 1000;

        double result = DocumentProcessor.calculateMinhashProbability(d1, d2, n);
        System.out.println("Pr = " + result);

        assertEquals(DocumentProcessor.calculateJaccardDistance(d1, d2), result, 0.1);
    }

    @Test
    public void testCalculateSignature() {
        Set<String> d1 = new HashSet<String>();
        Set<String> d2 = new HashSet<String>();

        d1.add("a");
        d1.add("b");
        d1.add("c");
        d2.add("b");
        d2.add("c");
        d2.add("e");

        ArrayList<String> permutation1 = new ArrayList<String>();
        permutation1.add("a");
        permutation1.add("b");
        permutation1.add("c");
        permutation1.add("e");

        ArrayList<String> permutation2 = new ArrayList<String>();
        permutation2.add("e");
        permutation2.add("c");
        permutation2.add("b");
        permutation2.add("a");

        assertEquals(new Integer[] { 1, 2 }, DocumentProcessor.calculateSignature(permutation1, d1, d2));
        assertEquals(new Integer[] { 2, 1 }, DocumentProcessor.calculateSignature(permutation2, d1, d2));
    }

    @Test
    public void testGetAllSets() throws Exception {
        List<Set<String>> sets = DocumentProcessor.getAllSets();
        assertEquals(15410, sets.size());
        assertTrue(sets.get(0).contains("blue "));
    }
}