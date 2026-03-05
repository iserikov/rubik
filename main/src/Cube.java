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
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Cube {

    static public void main (String [] args) {

        Arguments arguments = new Arguments (args);

        Command cmd = commands.get (arguments.getString ());
        if (cmd == null)
            usage ();

        cmd.execute (arguments);
    }

    private interface Command {
        void help ();
        void execute (Arguments arguments);
    }

    static private class CmdShow implements Command {
        CmdShow () {
            commands.put ("show", this);
        }
        @Override
        public void help () {
            System.err.println ("  show <path> [t]");
        }
        @Override
        public void execute (Arguments arguments) {
            ArrayList<Op> funcs = Util.translate (arguments.getString ());
            boolean trace = arguments.getFlag ('t');
            arguments.endOfArguments ();
            ArrayList<Reached> rs = Util.applyOps (new Reached (), funcs);
            boolean printed = true;
            if (!trace) {
                int i = rs.size () - 1;
                if (i >= 0) {
                    Reached reached = rs.get (i);
                    reached.print ();
                    printed = true;
                }
            } else {
                for (int i = rs.size () - 1; i >= 0; i --) {
                    Reached reached = rs.get (i);
                    if (!printed)
                        printed = true;
                    else
                        System.out.println ();
                    System.out.println ("------");
                    reached.print ();
                }
            }
            if (printed)
                System.out.println ();
            System.out.println ("Length: " + rs.size ());
        }
    }

    static private class CmdOptimize implements Command {
        CmdOptimize () {
            commands.put ("optimize", this);
        }
        @Override
        public void help () {
            System.err.println ("  optimize <path>");
        }
        @Override
        public void execute (Arguments arguments) {
            ArrayList<Op> funcs = Util.translate (arguments.getString ());
            if (funcs.size () == 0) {
                System.err.println ("Empty sequence");
                System.exit (1);
                return;
            }

            arguments.endOfArguments ();

            ArrayList<Reached> rs = Util.applyOps (new Reached (), funcs);
            Find find = new Find (rs.get (rs.size () - 1));
        }
    }

    static private class CmdPeriod implements Command {
        CmdPeriod () {
            commands.put ("period", this);
        }
        @Override
        public void help () {
            System.err.println ("  period <path>");
        }
        @Override
        public void execute (Arguments arguments) {
            ArrayList<Op> funcs = Util.translate (arguments.getString ());
            arguments.endOfArguments ();

            Reached initial = new Reached ();
            Reached reached = initial;
            int count = 0;
            do {
                for (Op func : funcs)
                    reached = func.apply (reached);
                count ++;
            } while (!reached.equals (initial));

            System.out.println ("count: " + count);
        }
    }

    static private class CmdReverse implements Command {
        CmdReverse () {
            commands.put ("reverse", this);
        }
        @Override
        public void help () {
            System.err.println ("  reverse <path>");
        }
        @Override
        public void execute (Arguments arguments) {
            ArrayList<Op> funcs = Util.translate (arguments.getString ());
            arguments.endOfArguments ();

            StringBuilder sb = new StringBuilder ();
            for (int i = funcs.size () - 1; i >= 0; i --)
                sb.append (funcs.get (i).inverse ().code ());

            System.out.println (sb);
        }
    }

    static void usage () {
        System.err.println ("Arguments: ");
        for (Command cmd : commands.values ())
            cmd.help ();
        System.exit (1);
    }

    static private class Arguments {
        Arguments (String [] arguments) {
            this.arguments = new LinkedList<> (Arrays.asList (arguments));
        }
        String getString () {
            String res = arguments.poll ();
            if (res == null)
                usage ();
            return res;
        }
        String getOptinalString (char code) {
            Iterator<String> it = arguments.iterator ();
            while (it.hasNext ()) {
                String str = it.next ();
                if (str.length () == 0)
                    usage ();
                if (str.charAt (0) == code) {
                    it.remove ();
                    return str.substring (1);
                }
            }
            return null;
        }
        String getString (char code) {
            String res = getOptinalString (code);
            if (res == null)
                usage ();
            return res;
        }
        boolean getFlag (char code) {
            String s = getOptinalString (code);
            if (s == null)
                return false;
            if (s.length () > 1)
                usage ();
            return true;
        }
        void endOfArguments () {
            if (!arguments.isEmpty ())
                usage ();
        }
        private final LinkedList<String> arguments;
    }

    static final private LinkedHashMap<String,Command> commands = new LinkedHashMap<> ();

    static {
        new CmdShow ();
        new CmdOptimize ();
        new CmdPeriod ();
        new CmdReverse ();
    }
}
