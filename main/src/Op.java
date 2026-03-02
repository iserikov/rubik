abstract class Op {
    abstract Reached apply (Reached reached);
    abstract String code ();

    static final Op [] ops = new Op [Table.O];

    private Op (int op) {
        this.op = op;
        ops [op] = this;
    }

    Op inverse () {
        return ops [Table.op2InvOp [op]];
    }

    final int op;

    static final Op left = new Op (Table.o_left) {
            @Override
            Reached apply (Reached reached) {
                return reached.left ();
            }
            @Override
            String code () {
                return "l";
            }
        };
    static final Op leftP = new Op (Table.o_left_p) {
            @Override
            Reached apply (Reached reached) {
                return reached.leftP ();
            }
            @Override
            String code () {
                return "l-";
            }
        };
    static final Op left2 = new Op (Table.o_left_2) {
            @Override
            Reached apply (Reached reached) {
                return reached.left2 ();
            }
            @Override
            String code () {
                return "L";
            }
        };
    static final Op right = new Op (Table.o_right) {
            @Override
            Reached apply (Reached reached) {
                return reached.right ();
            }
            @Override
            String code () {
                return "r";
            }
        };
    static final Op rightP = new Op (Table.o_right_p) {
            @Override
            Reached apply (Reached reached) {
                return reached.rightP ();
            }
            @Override
            String code () {
                return "r-";
            }
        };
    static final Op right2 = new Op (Table.o_right_2) {
            @Override
            Reached apply (Reached reached) {
                return reached.right2 ();
            }
            @Override
            String code () {
                return "R";
            }
        };
    static final Op up = new Op (Table.o_up) {
            @Override
            Reached apply (Reached reached) {
                return reached.up ();
            }
            @Override
            String code () {
                return "u";
            }
        };
    static final Op upP = new Op (Table.o_up_p) {
            @Override
            Reached apply (Reached reached) {
                return reached.upP ();
            }
            @Override
            String code () {
                return "u-";
            }
        };
    static final Op up2 = new Op (Table.o_up_2) {
            @Override
            Reached apply (Reached reached) {
                return reached.up2 ();
            }
            @Override
            String code () {
                return "U";
            }
        };
    static final Op down = new Op (Table.o_down) {
            @Override
            Reached apply (Reached reached) {
                return reached.down ();
            }
            @Override
            String code () {
                return "d";
            }
        };
    static final Op downP = new Op (Table.o_down_p) {
            @Override
            Reached apply (Reached reached) {
                return reached.downP ();
            }
            @Override
            String code () {
                return "d-";
            }
        };
    static final Op down2 = new Op (Table.o_down_2) {
            @Override
            Reached apply (Reached reached) {
                return reached.down2 ();
            }
            @Override
            String code () {
                return "D";
            }
        };
    static final Op front = new Op (Table.o_front) {
            @Override
            Reached apply (Reached reached) {
                return reached.front ();
            }
            @Override
            String code () {
                return "f";
            }
        };
    static final Op frontP = new Op (Table.o_front_p) {
            @Override
            Reached apply (Reached reached) {
                return reached.frontP ();
            }
            @Override
            String code () {
                return "f-";
            }
        };
    static final Op front2 = new Op (Table.o_front_2) {
            @Override
            Reached apply (Reached reached) {
                return reached.front2 ();
            }
            @Override
            String code () {
                return "F";
            }
        };
    static final Op back = new Op (Table.o_back) {
            @Override
            Reached apply (Reached reached) {
                return reached.back ();
            }
            @Override
            String code () {
                return "b";
            }
        };
    static final Op backP = new Op (Table.o_back_p) {
            @Override
            Reached apply (Reached reached) {
                return reached.backP ();
            }
            @Override
            String code () {
                return "b-";
            }
        };
    static final Op back2 = new Op (Table.o_back_2) {
            @Override
            Reached apply (Reached reached) {
                return reached.back2 ();
            }
            @Override
            String code () {
                return "B";
            }
        };
    static final Op middle = new Op (Table.o_middle) {
            @Override
            Reached apply (Reached reached) {
                return reached.middle ();
            }
            @Override
            String code () {
                return "m";
            }
        };
    static final Op middleP = new Op (Table.o_middle_p) {
            @Override
            Reached apply (Reached reached) {
                return reached.middleP ();
            }
            @Override
            String code () {
                return "m-";
            }
        };
    static final Op middle2 = new Op (Table.o_middle_2) {
            @Override
            Reached apply (Reached reached) {
                return reached.middle2 ();
            }
            @Override
            String code () {
                return "M";
            }
        };
    static final Op equator = new Op (Table.o_equator) {
            @Override
            Reached apply (Reached reached) {
                return reached.equator ();
            }
            @Override
            String code () {
                return "e";
            }
        };
    static final Op equatorP = new Op (Table.o_equator_p) {
            @Override
            Reached apply (Reached reached) {
                return reached.equatorP ();
            }
            @Override
            String code () {
                return "e-";
            }
        };
    static final Op equator2 = new Op (Table.o_equator_2) {
            @Override
            Reached apply (Reached reached) {
                return reached.equator2 ();
            }
            @Override
            String code () {
                return "E";
            }
        };
    static final Op side = new Op (Table.o_side) {
            @Override
            Reached apply (Reached reached) {
                return reached.side ();
            }
            @Override
            String code () {
                return "s";
            }
        };
    static final Op sideP = new Op (Table.o_side_p) {
            @Override
            Reached apply (Reached reached) {
                return reached.sideP ();
            }
            @Override
            String code () {
                return "s-";
            }
        };
    static final Op side2 = new Op (Table.o_side_2) {
            @Override
            Reached apply (Reached reached) {
                return reached.side2 ();
            }
            @Override
            String code () {
                return "S";
            }
        };
}
