/*-
 * Copyright (c) 2026 Igor Serikov
 *
 * Permission to use, copy, modify, distribute, and sell this software and its
 * documentation for any purpose is hereby granted without fee, provided that
 * the above copyright notice appear in all copies and that both that
 * copyright notice and this permission notice appear in supporting
 * documentation. The copyright holders make no representations about the
 * suitability of this software for any purpose.  It is provided "as is"
 * without express or implied warranty.
 *
 * THE COPYRIGHT HOLDERS DISCLAIM ALL WARRANTIES WITH REGARD TO THIS SOFTWARE,
 * INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS, IN NO
 * EVENT SHALL THE COPYRIGHT HOLDERS BE LIABLE FOR ANY SPECIAL, INDIRECT OR
 * CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE,
 * DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER
 * TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF THIS SOFTWARE.
 */

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
