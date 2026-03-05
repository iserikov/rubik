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

@SuppressWarnings("overrides")
class Reached {

    Reached () {
        cubes = new byte [Table.B];
    }

    Reached (byte [] cubes) {
        this.cubes = cubes;
    }

    Reached left () {
        return new Reached (turn (Table.s_left, Table.s_left));
    }

    Reached leftP () {
        return new Reached (turn (Table.s_right, Table.s_left));
    }

    Reached left2 () {
        return new Reached (turn2 (Table.s_left));
    }

    Reached right () {
        return new Reached (turn (Table.s_right, Table.s_right));
    }

    Reached rightP () {
        return new Reached (turn (Table.s_left, Table.s_right));
    }

    Reached right2 () {
        return new Reached (turn2 (Table.s_right));
    }

    Reached up () {
        return new Reached (turn (Table.s_up, Table.s_up));
    }

    Reached upP () {
        return new Reached (turn (Table.s_down, Table.s_up));
    }

    Reached up2 () {
        return new Reached (turn2 (Table.s_up));
    }

    Reached down () {
        return new Reached (turn (Table.s_down, Table.s_down));
    }

    Reached downP () {
        return new Reached (turn (Table.s_up, Table.s_down));
    }

    Reached down2 () {
        return new Reached (turn2 (Table.s_down));
    }

    Reached front () {
        return new Reached (turn (Table.s_front, Table.s_front));
    }

    Reached frontP () {
        return new Reached (turn (Table.s_back, Table.s_front));
    }

    Reached front2 () {
        return new Reached (turn2 (Table.s_front));
    }

    Reached back () {
        return new Reached (turn (Table.s_back, Table.s_back));
    }

    Reached backP () {
        return new Reached (turn (Table.s_front, Table.s_back));
    }

    Reached back2 () {
        return new Reached (turn2 (Table.s_back));
    }

    Reached middle () {
        return new Reached (mturn (Table.s_left, Table.s_right));
    }

    Reached middleP () {
        return new Reached (mturn (Table.s_right, Table.s_left));
    }

    Reached middle2 () {
        return new Reached (mturn2 (Table.s_left, Table.s_right));
    }

    Reached equator () {
        return new Reached (mturn (Table.s_up, Table.s_down));
    }

    Reached equatorP () {
        return new Reached (mturn (Table.s_down, Table.s_up));
    }

    Reached equator2 () {
        return new Reached (mturn2 (Table.s_up, Table.s_down));
    }

    Reached side () {
        return new Reached (mturn (Table.s_front, Table.s_back));
    }

    Reached sideP () {
        return new Reached (mturn (Table.s_back, Table.s_front));
    }

    Reached side2 () {
        return new Reached (mturn2 (Table.s_front, Table.s_back));
    }

    private byte [] turn (int arrow1, int arrow2) {
        byte [] newCubes = new byte [Table.B];
        for (int i = 0; i < Table.B; i ++) {
            int arr_i = cubes [i];
            if ((1 << Table.trfSide2OrigSide [arr_i] [arrow2] & Table.initials [i]) == 0)
                newCubes [i] = cubes [i];
            else
                newCubes [i] = (byte) Table.trfTrf2Trf [arr_i] [arrow1 + 1];
        }
        return newCubes;
    }

    private byte [] turn2 (int arrow) {
        byte [] newCubes = new byte [Table.B];
        for (int i = 0; i < Table.B; i ++) {
            int arr_i = cubes [i];
            if ((1 << Table.trfSide2OrigSide [arr_i] [arrow] & Table.initials [i]) == 0)
                newCubes [i] = cubes [i];
            else {
                int im = Table.trfTrf2Trf [arr_i] [arrow + 1];
                newCubes [i] = (byte) Table.trfTrf2Trf [im] [arrow + 1];
            }
        }
        return newCubes;
    }

    private byte [] mturn (int arrow1, int arrow2) {
        byte [] newCubes = new byte [Table.B];
        for (int i = 0; i < Table.B; i ++) {
            int arr_i = cubes [i];
            if (
              (
                (
                  1 << Table.trfSide2OrigSide [arr_i] [arrow1] |
                  1 << Table.trfSide2OrigSide [arr_i] [arrow2]
                ) & Table.initials [i]
              ) != 0
            )
                newCubes [i] = cubes [i];
            else
                newCubes [i] = (byte) Table.trfTrf2Trf [arr_i] [arrow1 + 1];
        }
        return newCubes;
    }

    private  byte [] mturn2 (int arrow1, int arrow2) {
        byte [] newCubes = new byte [Table.B];
        for (int i = 0; i < Table.B; i ++) {
            int arr_i = cubes [i];
            if (
              (
                (
                  1 << Table.trfSide2OrigSide [arr_i] [arrow1] |
                  1 << Table.trfSide2OrigSide [arr_i] [arrow2]
                ) & Table.initials [i]
              ) != 0
            )
                newCubes [i] = cubes [i];
            else {
                int im = Table.trfTrf2Trf [arr_i] [arrow1 + 1];
                newCubes [i] = (byte) Table.trfTrf2Trf [im] [arrow1 + 1];
            }
        }
        return newCubes;
    }

    void print () {
        boolean [] seen = new boolean [6];
        for (int i = 0; i < 6; i ++) {
            if (Table.trfSide2Side [cubes [0]] [i] == i || seen [i])
                continue;
            int j = i;
            System.out.println ();
            do {
                seen [j] = true;
                System.out.println (Table.sides [j] + " -> " + Table.sides [Table.trfSide2Side [cubes [0]] [j]]);
                j = Table.trfSide2Side [cubes [0]] [j];
            } while (!seen [j]);
        }
        seen = new boolean [Table.B];
        for (int i = 1; i < Table.B; i ++) {
            if (cubes [i] == 0 || seen [i])
                continue;
            int j = i;
            System.out.println ();
            do {
                seen [j] = true;
                int pos = Table.initials [j];
                String s1 = "";
                String s2 = "";
                for (int k = 0; k < 6; k ++)
                    if ((pos & 1 << k) != 0) {
                        s1 += "," + Table.sides [k];
                        s2 += "," + Table.sides [Table.trfSide2Side [cubes [j]] [k]];
                    }
                System.out.println (s1.substring (1) + " -> " + s2.substring (1));
                j = Table.trfPos2Pos [cubes [j]] [j];
            } while (!seen [j]);
        }
    }

    Reached relable (int n) {
        byte [] newCubes = new byte [Table.B];
        doRelable (n, newCubes);
        return new Reached (newCubes);
    }

    Canonical canonical () {
        byte [] [] buffers = new byte [2] [Table.B];
        System.arraycopy (cubes, 0, buffers [0], 0, Table.B);
        int gb = 0;
        int wb = 1;
        int t = 0;
        for (int i = 1; i < Table.F; i ++) {
            doRelable (i, buffers [wb]);
            if (Arrays.compareUnsigned (buffers [wb], buffers [gb]) < 0) {
                int tmp = gb;
                gb = wb;
                wb = tmp;
                t = i;
            }
        }
        return new Canonical (new Reached (buffers [gb]), t);
    }

    private void doRelable (int n, byte [] newCubes) {
        for (int i = 0; i < Table.B; i ++) {
            int cube = Table.trfPos2Pos [n] [i];
            int narr = Table.trfArr2Arr [n] [cubes [i]];
            newCubes [cube] = (byte) narr;
        }
    }

    void dump () {
        StringBuilder sb = new StringBuilder ();
        for (int i = 0; i < Table.B; i ++) {
            if (sb.length () > 0)
                sb.append (' ');
            sb.append (cubes [i]);
        }
        System.out.println (sb);
    }

    @Override
    public boolean equals (Object other) {
        if (!(other instanceof Reached))
            return false;
        Reached or = (Reached) other;
        return Arrays.equals (cubes, or.cubes);
    }

    final byte [] cubes;
}
