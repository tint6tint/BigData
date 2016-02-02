package fi.bigdata.part3.estimators;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

/**
 * Created by kjanowsk on 13.12.2014.
 */
public class StatisticsCardinalityEstimator implements CardinalityEstimator {

    private static int hash(String str) {
        HashFunction hf = Hashing.murmur3_32();
        Hasher hr = hf.newHasher();
        hr.putBytes(str.getBytes());
        return Math.abs(hf.hashString(str, StandardCharsets.UTF_8).asInt());
    }

    private DataInputStream dataInputStream;

    private int k = 1;

    public StatisticsCardinalityEstimator(DataInputStream is, int k) {
        this.dataInputStream = is;
        this.k = k;
    }

    /**
     * algorithm from http://research.neustar.biz/2012/07/09/sketch-of-the-day-k-minimum-values/
     *
     * Cardinality(KMV):
     * 
     * return: (k-1)/max(KMV)
     * 
     * @return
     */
    @Override
    public int estimateUniqueValues() throws IOException {
        int maxKmv = findKMinimum(); // still not divided

        // (k-1)/max(KMV) = (k-1)/(x/2^number of bits) = ((k-1) * (2^number of bits)) / x
        double result = ((k - 1l) * Math.pow(2, 31)) / maxKmv;

        return (int) result;
    }

    /**
     * algorithm from http://research.neustar.biz/2012/07/09/sketch-of-the-day-k-minimum-values/
     *
     * Initialize KMV with first k values
     * 
     * for all h(n):
     * 
     * if h(n) < max(KMV):
     * 
     * insert h(n) into KMV set
     * 
     * remove largest value from KMV
     * 
     * @return max(kmv) - still has to be devided by 2^32 at this point
     */
    int findKMinimum() throws IOException {
        Set<Integer> kmv = getInitialKMV(); // Initialize KMV with first k values

        String str;
        while ((str = dataInputStream.readLine()) != null) {
            int hash = hash(str);
            int maxKmv = Collections.max(kmv);
            if (hash < maxKmv && !kmv.contains(hash)) { // if h(n) < max(KMV):
                kmv.add(hash);
                kmv.remove(maxKmv);
            }
        }
        return Collections.max(kmv);
    }

    /**
     * @return returns initial KMV
     */
    private Set<Integer> getInitialKMV() throws IOException {
        int i = 0;
        Set<Integer> kmv = new HashSet<Integer>();
        String line;
        while ((line = dataInputStream.readLine()) != null) {
            kmv.add(hash(line));
            if (++i >= k) {
                break;
            }
        }
        return kmv;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }
}
