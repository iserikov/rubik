import java.util.Arrays;

class DynamicArray {
	synchronized void add (long value) {
		if (data == null) {
			data = new long [STEP];
		} else {
			assert current + STEP > 0; // No index overflow.
			if (current + 1 > data.length)
				data = Arrays.copyOf (data, data.length + STEP);
		}
		data [current] = value;
		current ++;
	}

	int count () {
		return current;
	}

	long [] values () {
		return data;
	}

	void reset () {
		current = 0;
	}

	private int current = 0;
	private long [] data = null;

	private final int STEP = 512;
}
