# Rubik's cube optimal solver.

The program finds the **shortest path** to achieve a given configuration from the solved configuration.

## Notation

|Syntax|Meaning|
|-----|--|
| l   | Left facet |
| r   | Right facet |
| u   | Up - top facet |
| d   | Down - bottom facet |
| f   | Front facet |
| b   | Back facet |
| e   | (equator) The plain between down and top plains. We look at it from the top to define the direction|
| m   | (middle) The plain between left and right plains. We look at it from the left to define the direction|
| s   | (standing) The plain between back and front plains. We look at it from the front to define the direction|
|_x+_ | Turn _x_ clockwise|
|_x+_ | Turn _x_ counter|clockwise|
|_X_  | Turn _x_ 180 degrees|

The sequence of moves we call _path_. Spaces are ignored.

## Commands and arguments

| Command | Operation |
|---------|-----------|
| show _path_ \[t\] | Apply the given list of operations to the solved configuration and display \
                          the result with or without showing the intermediate configuration (tracing).|
| optimize _path_    | Find the shortest path to the configuration (optimize the path). The program \
                       will report solutions having N and N+1 moves as it discovers them and then stop. \
                       Note that this is different from the solvers that allow different orientation of the \
					   cube at the solved state (for example, up -> down).
| period _path_  | Find the period of the given sequence of moves. | 
| reverse _path_ | Apply the sequence in the reverse order. We use the inverse of every operation. |

## Algorithm

### Key points

- We use a brute force breadth first bidirectional search from both the initial and the solved configuration until
  find an a configuration (or a set of them) reachable from both ends. We do it in rounds. The first round corresponds
  to depth 1 from both ends, the second corresponds to depth 2 and so on.

- Every configuration includes 21 cubes. Cube orientation defines its position. The central cube never changes its
  position (middles are the holes through which we can see its facets).

- Every configuration has 47 siblings which can be achieved by turning/mirroring the cube and relabeling the facets.
  Every relabeling (isomorphism) of the solved configuration is the solved configuration itself.
  
- Whenever we reach a configuration we apply relabeling to find its Canonical representative. We use it to maintain
  the table of known configurations.

### An example

Assume the search starting from C~solved~ and C~initial~ and found C~middle~ in the second round. So, we have:

 _C~solved~ - move~1~ - relabeling~1~ - C~middle~ - relabeling~2~ - move~2~ - relabeling~3~ - C~initial~_
 
We traverse the chain starting from C~initial~ changing the moves according to relabeling. Finally, we arrive at
C~solved~ which some final relabeling which we can discard since all relabeling of the solved configuration gives
us the solved configuration.
