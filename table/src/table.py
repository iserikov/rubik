import sys

# Cube sides.
number2side = ( 'left', 'right', 'up', 'down', 'front', 'back' )

# Cube rotations.
sides = {
    'left'    :  ( 'up',   'front', 'down',   'back'  ),
    'right'   :  ( 'up',   'back',  'down',   'front' ),
    'up'      :  ( 'left', 'back',  'right',  'front' ),
    'down'    :  ( 'left', 'front', 'right',  'back'  ),
    'front'   :  ( 'left', 'up',    'right',  'down'  ),
    'back'    :  ( 'left', 'down',   'right', 'up'    )
}

# Facet rotations: name, rotating facet and a "script" that implements it.
ops = [
    ('left',      'left',  [ 'left' ]),
    ('left_p',    'left',  [ 'left', 'left', 'left' ]),
    ('left_2',    'left',  [ 'left', 'left' ]),
    ('right',     'right', [ 'right' ]),
    ('right_p',   'right', [ 'right', 'right', 'right' ]),
    ('right_2',   'right', [ 'right', 'right' ]),
    ('up',        'up',    [ 'up' ]),
    ('up_p',      'up',    [ 'up', 'up', 'up' ]),
    ('up_2',      'up',    [ 'up', 'up' ]),
    ('down',      'down',  [ 'down' ]),
    ('down_p',    'down',  [ 'down', 'down', 'down' ]),
    ('down_2',    'down',  [ 'down', 'down' ]),
    ('front',     'front', [ 'front' ]),
    ('front_p',   'front', [ 'front', 'front', 'front' ]),
    ('front_2',   'front', [ 'front', 'front' ]),
    ('back',      'back',  [ 'back' ]),
    ('back_p',    'back',  [ 'back', 'back', 'back' ]),
    ('back_2',    'back',  [ 'back', 'back' ]),
    ('middle',    None,    [ 'left' ]),
    ('middle_p',  None,    [ 'left', 'left', 'left' ]),
    ('middle_2',  None,    [ 'left', 'left' ]),
    ('equator',   None,    [ 'up' ]),
    ('equator_p', None,    [ 'up', 'up', 'up' ]),
    ('equator_2', None,    [ 'up', 'up' ]),
    ('side',      None,    [ 'front' ]),
    ('side_p',    None,    [ 'front', 'front', 'front' ]),
    ('side_2',    None,    [ 'front', 'front' ])
]

#-------------------------------------------------------------------------------

def error (msg):
    print (msg)
    sys.exit (1)

if len (sys.argv) != 2:
    error ('Usage: %s destination' % sys.argv [0])

destination = sys.argv [1]

print ('Generating tables')

# M is the number of sides.
M = len (number2side)

for s, acs in sides.items ():
    for cs in acs:
        if not (s in sides [cs]):
            error ('Connection conflict: %s %s', (s, cs))

side2number = dict ()

# Construct x -> n relaion based on n -> x.
def make_numbers (q):
    nums = dict ()
    for i in range (0, len (q)):
        nums [q [i]] = i
    return nums

side2number = make_numbers (number2side)

# Make a canonical cubie name based on a list of facets.
def order (*args):
    return tuple (sorted (args, key = lambda s: side2number [s]))

number2cube = []
cube2number = dict ()

# Register a cube represnted as a list of facets.
def add_cube (*args):
    co = order (*args)
    if cube2number.get (co, None) == None:
        cube2number [co] = len (cube2number)
        number2cube.append (co)

# Add the middle cubie.
add_cube ()

# Add sidle cubies.
for s in number2side:
    acs = sides [s]
    for cs in acs:
        add_cube (s, cs)

# Add corener cubies.
for s in number2side:
    acs = sides [s]
    for cs in acs:
        for ts in sides [cs]:
            if ts in acs:
                add_cube (s, cs, ts)

# B is the total number of cubies.
B = len (number2cube)

transform2number = dict ()
number2transform = []

# Make a canonical transformation label (as a tuple) based on a side -> side relation.
def trkey (tr):
    return tuple (sorted (tr.items (), key = lambda i: side2number [i [0]]))

# Add a trsnformation reperesented as a side -> side relation.
def add_transform (tr):
    k = trkey (tr)
    if transform2number.get (k, None) == None:
        transform2number [k] = len (number2transform)
        number2transform.append (tr)

# Create an identity transformation.
def identity ():
    res = dict ()
    for s in number2side:
        res [s] = s
    return res

# Add the identity transformation.
add_transform (identity ())

# Add transformations corresponding to the cube rotations.
for s in number2side:
    acs = sides [s]
    tr = dict ()
    for i in range (0, len (acs) - 1):
        tr [acs [i]] = acs [i + 1]
    tr [acs [len (acs) - 1]] = acs [0]
    for cs in number2side:
        if tr.get (cs, None) == None:
            tr [cs] = cs
    add_transform (tr)

# Combine N transformations together.
def combine (*args):
    res = identity ()
    for tr2 in args:
        tr1 = res.copy ()
        for f, t in tr1.items ():
            res [f] = tr2 [t]
    return res

# Add all transformations that are combinations of the first transfomrations.
i = 1
while i < len (number2transform):
    tr1 = number2transform [i]
    for j in range (1, M + 1):
        tr2 = number2transform [j]
        tr = combine (tr1, tr2)
        add_transform (tr)
    i = i + 1

# A is the number of all tranformation achived via rotations plus identity tranformation.
# They correspond to cubies' rotations. Slightly abusuing the term we call them arrangements.
A = len (number2transform)

# Add all mirrored transformations.

trm = identity ()
trm ['left'] = 'right'
trm ['right'] = 'left'
add_transform (trm)

for i in range (1, A):
    tr1 = number2transform [i]
    tr = combine (trm, tr1)
    add_transform (tr)

# F is the total number of possible tranformations. It includes mirrors.
F = len (number2transform)

number2inv_transform = []

# Create inverse transformations.
for tr in number2transform:
    itr = dict ()
    for f, t in tr.items ():
        itr [t] = f
    number2inv_transform.append (itr)

transform_transform2transform = []

# Create transformations that are combinations of the given 2 as index * index -> index relation.
for tr1 in number2transform:
    row = []
    for tr2 in number2transform:
        tr = combine (tr1, tr2)
        row.append (transform2number [trkey (tr)])
    transform_transform2transform.append (row)

# Create a relabeling relation: transform * arrangement -> arrangement.
transform_arr2arr = []

for i in range (0, F):
    trd = number2transform [i]
    tri = number2inv_transform [i]
    row = []
    for j in range (0, A):
        tr2 = number2transform [j]
        tr = combine (tri, tr2, trd)
        row.append (transform2number [trkey (tr)])
    transform_arr2arr.append (row)

# Create a mask for exposed facets for every cube in the initial state (arrangement).
number2initial = []

for c in number2cube:
    n = 0
    for s in c:
        n = n | 1 << side2number [s]
    number2initial.append (n)

# Create a cubie postion to cublie position relation for every relabeling.
transform_pos2pos = []

for tr in number2transform:
    row = []
    for c in number2cube:
        row.append (cube2number [order (*[tr [s] for s in c])])
    transform_pos2pos.append (row)

number2op = [o [0] for o in ops]
op2number = make_numbers (number2op)

O = len (number2op)

op_table = dict ()

# Create a a transformation resulting from a given facet "operation".
def op_transform (q):
    return combine (*[number2transform [side2number [s] + 1] for s in q])

for m, c, q  in ops:
    tr = op_transform (q)
    k = (c, trkey (tr))
    op_table [k] = m

# Create the invers operation table.    
number2inv_op = []

for m, c, q  in ops:
    tr = op_transform (q)
    tri = number2inv_transform [transform2number [trkey (tr)]]
    k = (c, trkey (tri))
    number2inv_op.append (op_table [k])

# Create a transformation * operation -> operation relation telling what the operations becomes after a relabeling.
transform_op2op = []

for i in range (0, len (number2transform)):
    tr = number2transform [i]
    tri = number2inv_transform [i]
    row = []
    for m, c, q  in ops:
        otr = op_transform (q)
        if c == None:
            nc = None
        else:
            nc = tr [c]
        ntr = combine (tri, otr, tr)
        k = (nc, trkey (ntr))
        row.append (op_table [k])
    transform_op2op.append (row)

with open (destination, 'w') as fout:
    fout.write ('class Table {\n')

    fout.write ('\n')

    for i in range (0, len (number2side)):
        fout.write ('    static final int s_{:5} = {};\n'.format (number2side [i], i))

    fout.write ('\n')

    for i in range (0, len (number2op)):
        fout.write ('    static final int o_{:5} = {};\n'.format (number2op [i], i))

    fout.write ('\n')

    fout.write ('    static final int A = {};\n'.format (A))
    fout.write ('    static final int F = {};\n'.format (F))
    fout.write ('    static final int B = {};\n'.format (B))
    fout.write ('    static final int O = {};\n'.format (O))

    def write_data (start, data, end):
        fout.write ('\n')
        d = '    ' + start + ' ' + ', '.join (data) + ' ' + end
        if (len (d) < 120):
            fout.write (d)
            fout.write ('\n')
        else:
            fout.write ('    ' + start)
            l = 0
            first = True
            for d in data:
                if first:
                    fout.write ('\n')
                    fout.write ('        ')
                    l = 8
                    first = False
                else:
                    if l + len (d) > 120:
                        fout.write (',\n')
                        fout.write ('        ')
                        l = 8
                    else:
                        fout.write (', ')
                        l = l + 2
                fout.write (d)
                l = l + len (d)
            fout.write ('\n')
            fout.write ('    ' + end)
            fout.write ('\n')

    write_data ('static final String [] sides = {', ['"{}"'.format (x) for x in number2side], '};')

    data = [
        '{{ {} }}'.format (', '.join ([str (side2number [x [y]]) for y in number2side]))
        for x in number2transform
    ]

    write_data ('static final int [] [] trfSide2Side = {', data, '};')

    data = [
        '{{ {} }}'.format (', '.join ([str (side2number [x [y]]) for y in number2side]))
        for x in number2inv_transform
    ]

    write_data ('static final int [] [] trfSide2OrigSide = {', data, '};')

    data = [str (transform2number [trkey (x)]) for x in number2inv_transform]

    write_data ('static final int [] trf2InvTrf = {', data, '};')

    data = ['{{ {} }}'.format (', '.join ([str (y) for y in x])) for x in transform_transform2transform]

    write_data ('static final int [] [] trfTrf2Trf = {', data, '};')

    data = ['{{ {} }}'.format (', '.join ([str (y) for y in x])) for x in transform_arr2arr]

    write_data ('static final int [] [] trfArr2Arr = {', data, '};')

    write_data ('static final int [] initials = {', [str (x) for x in number2initial], '};')

    data = [ '{{ {} }}'.format (', '.join ([str (y) for y in x])) for x in transform_pos2pos ]

    write_data ('static final int [] [] trfPos2Pos = {', data, '};')

    data = [str (op2number [x]) for x in number2inv_op]

    write_data ('static final int [] op2InvOp = {', data, '};')

    data = ['{{ {} }}'.format (', '.join ([str (op2number [y]) for y in x])) for x in transform_op2op]

    write_data ('static final int [] [] trfOp2Op = {', data, '};')

    fout.write ('}\n')
    fout.close ()
