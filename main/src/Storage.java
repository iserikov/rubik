import java.util.Arrays;

class Storage {
	Storage () {
		for (int i = 0; i < N_BUCKETS; i ++)
			buckets [i] = new Bucket ();
	}
	long put (Point point) {
        int hash = point.hash ();
        int bIdx = (hash + (hash >>> N_SHIFT)) & N_MASK;
		int idx = buckets [bIdx].put (point);
		if (idx >= 0)
			return (long) bIdx << 32 | idx;
		else
			return - (((long) bIdx << 32 | (- idx - 1)) + 1);
	}
	Point get (long index) {
		// This is safe without a lock since the creation is done before the engine knows the location.
		return buckets [(int) (index >> 32)].get ((int) index);
	}
	static private class Bucket {
		synchronized int put (Point point) {
			if (data == null) {
				data = new long [STEP];
			} else {
				for (int i = 0; i < current; i += Point.longsPerPoint)
					if (data [i] == point.key1 && data [i + 1] == point.key2)
						return - (i + 1);
				assert current + STEP > 0; // No index overflow.
				if (current + Point.longsPerPoint > data.length)
					data = Arrays.copyOf (data, data.length + STEP);
			}
			int cur = current;
			current += Point.longsPerPoint;
			point.copy (data, cur);
			return cur;
		}
		Point get (int index) {
			// This is safe without a lock since the creation is done before the engine knows the location.
			return new Point (data, index);
		}
        int current () {
            return current;
        }
		private int current = 0;
		private long [] data = null;
	}

    long total () {
        long sum = 0;
        for (Bucket bucket : buckets)
            sum += bucket.current ();
        return sum;
    }

    private final Bucket []  buckets = new Bucket [N_BUCKETS];

    static final int N_SHIFT = 27;
    static final int N_BUCKETS = 1 << N_SHIFT;
    static final int N_MASK = N_BUCKETS - 1;
    static final int wordsPerPoint = 4;
    static final int STEP = 4 * Point.longsPerPoint;
}
