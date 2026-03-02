import java.util.LinkedList;
import org.junit.Test;

import static org.junit.Assert.fail;

public class ReachedAndTable {

    @Test
    public void relableLeft () {
        Reached r1 = new Reached ();
        Reached r2 = r1.down ();
        Reached r3 = r2.relable (Table.s_left + 1);
        Reached r4 = r3.backP ();
        if (!r4.equals (r1))
            fail ("Failed: relableLeft");
    }

    @Test
    public void relableLeftMirror () {
        Reached r1 = new Reached ();
        Reached r2 = r1.equator ().side ();
        Reached r3 = r2.relable (Table.A + Table.s_left + 1);
        Reached r4 = r3.equatorP ().side ();
        if (!r4.equals (r1))
            fail ("Failed");
    }

    @Test
    public void canonical1 () {
        Reached r1 = new Reached ();
        Canonical c1 = r1.left ().downP ().canonical ();
        Canonical c2 = r1.frontP ().down ().canonical ();
        if (!c1.reached.equals (c2.reached))
            fail ("Failed");
    }

    @Test
    public void canonical2 () {
        Reached r1 = new Reached ();
        Canonical c1 = r1.middle ().down ().canonical ();
        Canonical c2 = r1.equatorP ().back ().canonical ();
        if (!c1.reached.equals (c2.reached))
            fail ("Failed");
    }

    @Test
    public void movesAndRelables1 () {
        int trf = Table.trfTrf2Trf [Table.s_up + 1] [Table.s_left + 1];
        int m = Table.trfOp2Op [trf] [Table.o_left];
        if (m != Table.o_up)
            fail ("Failed");
    }

    @Test
    public void movesAndRelables2 () {
        int trf = Table.trfTrf2Trf [Table.s_front + 1] [Table.A];
        int m = Table.trfOp2Op [trf] [Table.o_up];
        if (m != Table.o_left_p)
            fail ("Failed: " + Op.ops [m].code ());
    }

    @Test
    public void movesAndRelables3 () {
        Reached r1 = new Reached ();
        Canonical c1 = r1.left ().canonical ();
        Canonical c2 = c1.reached.down ().canonical ();
        int trf = c2.transform;
        int m2 = Table.trfOp2Op [trf] [Table.o_down_p];
        trf = Table.trfTrf2Trf [c1.transform] [trf];
        int m1 = Table.trfOp2Op [trf] [Table.o_left_p];
        Reached r2 = Op.ops [m2].apply (c2.reached);
        Reached r3 = Op.ops [m1].apply (r2);
        if (!r3.equals (r1))
            fail ("Failed");
    }

    @Test
    public void canonicalCount () {
        Reached r1 = new Reached ();
        LinkedList<Reached> list = new LinkedList<>();
        
        for (Op op : Op.ops) {
            Canonical can = op.apply (r1).canonical ();
            boolean found = false;
            for (Reached r2 : list)
                if (can.reached.equals (r2)) {
                    found = true;
                    break;
                }
            if (!found)
                list.add (can.reached);
        }
        if (list.size () != 4)
            fail ("Failed");
    }
}
