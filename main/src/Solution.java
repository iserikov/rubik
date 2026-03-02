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
