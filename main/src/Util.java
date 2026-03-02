import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

class Util {
    static ArrayList<Op> translate (String op) {
        ArrayList<Op> res = new ArrayList<> ();
        int i = 0;
        while (i < op.length ())  {
            Op fun1 = null;
            Op fun2 = null;
            char c = op.charAt (i);
            i ++;
            switch (c) {
              case 'l':
                fun1 = Op.left;
                fun2 = Op.leftP;
                break;
              case 'L':
                fun1 = Op.left2;
                fun2 = Op.left2;
                break;
              case 'r':
                fun1 = Op.right;
                fun2 = Op.rightP;
                break;
              case 'R':
                fun1 = Op.right2;
                fun2 = Op.right2;
                break;
              case 'u':
                fun1 = Op.up;
                fun2 = Op.upP;
                break;
              case 'U':
                fun1 = Op.up2;
                fun2 = Op.up2;
                break;
              case 'd':
                fun1 = Op.down;
                fun2 = Op.downP;
                break;
              case 'D':
                fun1 = Op.down2;
                fun2 = Op.down2;
                break;
              case 'f':
                fun1 = Op.front;
                fun2 = Op.frontP;
                break;
              case 'F':
                fun1 = Op.front2;
                fun2 = Op.front2;
                break;
              case 'b':
                fun1 = Op.back;
                fun2 = Op.backP;
                break;
              case 'B':
                fun1 = Op.back2;
                fun2 = Op.back2;
                break;
              case 'm':
                fun1 = Op.middle;
                fun2 = Op.middleP;
                break;
              case 'M':
                fun1 = Op.middle2;
                fun2 = Op.middle2;
                break;
              case 'e':
                fun1 = Op.equator;
                fun2 = Op.equatorP;
                break;
              case 'E':
                fun1 = Op.equator2;
                fun2 = Op.equator2;
                break;
              case 's':
                fun1 = Op.side;
                fun2 = Op.sideP;
                break;
              case 'S':
                fun1 = Op.side2;
                fun2 = Op.side2;
                break;
              case ' ':
                continue;
              default:
                System.err.println ("Bad code: "  + c);
                System.exit (1);
            }
            if (i < op.length () && op.charAt (i) == '-') {
                res.add (fun2);
                i ++;
            } else {
                res.add (fun1);
            }
        }
        return res;
    }

    static ArrayList<Reached> applyOps (Reached reached, Iterable<Op> ops) {
        ArrayList<Reached> res = new ArrayList<> ();
        for (Op func : ops) {
            reached = func.apply (reached);
            res.add (reached);
        }
        return res;
    }

    static String codes (Iterable<Op> ops) {
        StringBuilder sb = new StringBuilder ();
        for (Op func : ops)
            sb.append (func.code ());
        return sb.toString ();
    }
}
