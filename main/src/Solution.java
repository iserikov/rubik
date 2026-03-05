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

import java.util.ArrayList;

class Solution {
    Solution (Storage storage, Point fromInitial, Point fromSolved) {
        this.storage = storage;
        getSolutionFromSolved (getSolutionFromInitial (fromInitial), fromSolved);
    }
    int getSolutionFromInitial (Point fromInitial) {
        int fp = 0;
        if (fromInitial.prev >= 0)
            fp = getSolutionFromInitial (storage.get (fromInitial.prev));
        if (fromInitial.op >= 0) {
            int mpi = Table.trfOp2Op [fp] [fromInitial.op];
            solution.add (0, Op.ops [mpi].inverse ());
        }
        return Table.trfTrf2Trf [Table.trf2InvTrf [fromInitial.trf]] [fp];
    }

    void getSolutionFromSolved (int fn, Point fromSolved) {
        fn = Table.trfTrf2Trf [fromSolved.trf] [fn];
        if (fromSolved.op >= 0) {
            int mn = Table.trfOp2Op [fn] [fromSolved.op];
            solution.add (0, Op.ops [mn]);
        }
        if (fromSolved.prev >= 0)
            getSolutionFromSolved (fn, storage.get (fromSolved.prev));
    }

    final ArrayList<Op> solution = new ArrayList<> ();
    final Storage storage;
}
