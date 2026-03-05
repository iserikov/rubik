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

class Point {
    Point (Reached reached, int kind, int trf) {
        this (reached, kind, trf, -1, -1);
    }
    Point (Reached reached, int kind, int trf, int op, long prev) {
        long v1 = 0;
        long v2 = 0;
        int ofs = 0;
        for (int i = 0; i < 12; i ++, ofs += 5) {
            assert reached.cubes [i] >= 0 && reached.cubes [i] < Table.A;
            v1 |= ((long) reached.cubes [i]) << ofs;
        }
        ofs = 0;
        for (int i = 12; i < Table.B; i ++, ofs += 5) {
            assert reached.cubes [i] >= 0 && reached.cubes [i] < Table.A;
            v2 |= ((long) reached.cubes [i]) << ofs;
        }
        this.key1 = v1;
        this.key2 = v2;
        this.kind = kind;
        this.trf = trf;
        this.op = op;
        this.prev = prev;
    }
	Point (long [] bucket, int ofs) {
		this.key1 = bucket [ofs + 0];
		this.key2 = bucket [ofs + 1];
		this.kind = (int) (bucket [ofs + 2] >> 32);
		this.trf = (short) (bucket [ofs + 2] >> 16);
		this.op = (short) bucket [ofs + 2];
		this.prev = bucket [ofs + 3];
	}
	int hash () {
		return (int) (key1 >>> 32) + (int) key1 + (int) (key2 >>> 32) + (int) key2;
	}
	void copy (long [] bucket, int ofs) {
		bucket [ofs + 0] = key1;
		bucket [ofs + 1] = key2;
		bucket [ofs + 2] = (long) kind << 32 | (long) (trf & 0xffff) << 16 | (op & 0xffff);
		bucket [ofs + 3] = prev;
	}
    Reached reached () {
        byte [] vals = new byte [Table.B];
        int ofs = 0;
        for (int i = 0; i < 12; i ++, ofs += 5)
            vals [i]  = (byte) (key1 >> ofs & 31);
        ofs = 0;
        for (int i = 12; i < Table.B; i ++, ofs += 5)
            vals [i]  = (byte) (key2 >> ofs & 31);
        return new Reached (vals);
    }
	final long key1;
	final long key2;
    final int kind;
    final int trf;
    final int op;
    final long prev;

	static final int longsPerPoint = 4;
}
