package chi.jyu.fi.big_data.hadoop.HyperLogLog;

import java.io.IOException;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

public class HyperLogLog {
	int log2m; // with b in [4...16]
	RegisterSet register;
	int register_index = 0;
	double alpha;
	Reader r;
	HashFunction hf = Hashing.murmur3_32();

	public HyperLogLog(int log2m, Reader r) {
		this.log2m = log2m;
		register = new RegisterSet(1 << log2m);
		int m = 1 << this.log2m;
		this.alpha = getAlpha(log2m, m);
		this.r = r;
		// https://github.com/addthis/stream-lib/blob/master/src/main/java/com/clearspring/analytics/stream/cardinality/HyperLogLog.java
	}

	public void readData() {
		byte[] b = null;
		try {
			while ((b = r.readLine()) != null) {
					offer(b);
				}
			} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected static double getAlpha(final int p, final int m) {
		switch (p) {
		case 4:
			return 0.673 * m * m;
		case 5:
			return 0.697 * m * m;
		case 6:
			return 0.709 * m * m;
		default:
			return (0.7213 / (1 + 1.079 / m)) * m * m;
		}
	}
    public boolean offerHashed(long hashedValue) {
        // j becomes the binary address determined by the first b log2m of x
        // j will be between 0 and 2^log2m
        final int j = (int) (hashedValue >>> (Long.SIZE - log2m));
        final int r = Long.numberOfLeadingZeros((hashedValue << this.log2m) | (1 << (this.log2m - 1)) + 1) + 1;
        return register.updateIfGreater(j, r);
    }
    public boolean offerHashed(int hashedValue) {
        // j becomes the binary address determined by the first b log2m of x
        // j will be between 0 and 2^log2m
        final int j = hashedValue >>> (Integer.SIZE - log2m);
        final int r = Integer.numberOfLeadingZeros((hashedValue << this.log2m) | (1 << (this.log2m - 1)) + 1) + 1;
        return register.updateIfGreater(j, r);
    }
    public boolean offer(byte[] b) {
    	Hasher hr = hf.newHasher();
    	hr.putBytes(b);
        int x = hr.hash().hashCode();
        return offerHashed(x);
    }
	public long cardinality() {
		double registerSum = 0;
		double zeroes = 0.;
		for (int j = 0; j < register.count; j++) {
			int val = register.get(j);
			registerSum += 1. / (1 << val);
			if (val == 0) {
				zeroes++;
			}
		}
		double estimate = alpha * (1 / registerSum);
		return Math.round(estimate);
	}

	public static void main(String[] args) {
		Reader r = null;
		try {
			r = new Reader("data1million");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HyperLogLog hll = new HyperLogLog(8, r);
		hll.readData();
		long c = hll.cardinality();
	}
}
