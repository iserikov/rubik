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
import java.util.LinkedList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

class Find {
    Find (Reached initialConfiguration) {
        this.initial = initialConfiguration.canonical ();

        for (int i = 0; i < queues [0].length; i ++) {
            queues [0] [i] = new DynamicArray ();
            queues [1] [i] = new DynamicArray ();
        }

        Reached solved = new Reached ();

        if (initial.reached.equals (solved)) {
            System.out.println ("Initial states is the same as solved");
            return;
        }

        for (int i = 0; i < threads; i ++)
            workers [i] = new Worker (i);

        putToQueue (new Point (initial.reached, kindFromInitial, initial.transform));
        putToQueue (new Point (solved, kindFromSolved, 0));

        round = 1;

        for (int i = 0; i < threads; i ++)
            workers [i].start ();

        for (int i = 0; i < threads; i ++)
            for (;;) {
                try {
                    workers [i].join ();
                    break;
                } catch (InterruptedException ign) {
                }
            }
        System.out.println(storage.total () + " states");
    }

    private void putToQueue (Point point) {
        putToQueue (storage.put (point), point.hash ());
    }

    private void putToQueue (long index, int hash) {
        if (found)
            return;
        queues [(round + 1) % 2][(hash + (hash >>> Q_SHIFT)) & Q_MASK].add (index);
    }

    class Worker extends Thread {
        Worker (int workerIndex) {
            this.workerIndex = workerIndex;
        }
        @SuppressWarnings("serial")
        @Override
        public void run () {
            while (!stop) {
                int per_worker = (queues [0].length + workers.length - 1) / workers.length;
                int q = workerIndex * per_worker;
                int e = Integer.min (q + per_worker, queues [0].length);
                for (; q < e; q ++) {
                    long [] values = queues [round % 2][q].values ();
                    int count = queues [round % 2][q].count ();
                    for (int i = 0; i < count; i ++) {
                        Point point = storage.get (values [i]);
                        if (point.kind == kindFromInitial)
                            exectuteFromInitial (values [i], point);
                        else
                            executeFromSolved (values [i], point);
                    }
                    queues [round % 2][q].reset ();
                }
                try {
                    barrier.await ();
                } catch (InterruptedException ex) {
                    System.out.println ("Unexpected interrupt");
                    System.exit (1);
                } catch (BrokenBarrierException ex) {
                    System.out.println ("Broken barrier");
                    System.exit (1);
                }
            }
        }

        void exectuteFromInitial (long prev, Point point) {
            Reached reached = point.reached ();

            for (Op op : Op.ops) {
                Canonical can = op.apply (reached).canonical ();
                Point nextPoint = new Point (can.reached, kindFromInitial, can.transform, op.op, prev);
                long nextIndex = storage.put (nextPoint);
                if (nextIndex < 0) {
                    nextIndex = - nextIndex - 1;
                    Point old = storage.get (nextIndex);
                    LongPair pair = new LongPair (prev, nextIndex);
                    if (old.kind == kindFromSolved && unique.putIfAbsent (pair, pair) == null)
                        reportSolution (nextPoint, old);
                } else {
                    newPoints.incrementAndGet ();
                    putToQueue (nextIndex, nextPoint.hash ());
                }
            }
        }

        void executeFromSolved (long prev, Point point) {
            Reached reached = point.reached ();
            for (Op op : Op.ops) {
                Canonical can = op.apply (reached).canonical ();

                Point nextPoint = new Point (can.reached, kindFromSolved, can.transform, op.op, prev);
                long nextIndex = storage.put (nextPoint);
                if (nextIndex < 0) {
                    nextIndex = - nextIndex - 1;
                    Point old = storage.get (nextIndex);
                    LongPair pair = new LongPair (nextIndex, prev);
                    if (old.kind == kindFromInitial && unique.putIfAbsent (pair, pair) == null)
                        reportSolution (old, nextPoint);
                } else {
                    newPoints.incrementAndGet ();
                    putToQueue (nextIndex, nextPoint.hash ());
                }
            }
        }
        final int workerIndex;
    }

    private void reportSolution (Point fromInitial, Point fromSolved) {
        Solution sol = new Solution (storage, fromInitial, fromSolved);
        ArrayList<Op> ops = sol.solution;
        synchronized (System.out) {
            System.out.println (" --- " + Util.codes (ops) + " " + ops.size () + " ---");
        }
        found = true;
    }

    private void endOfRound () {
        if (found) {
            stop = true;
            return;
        }
        int ns = newPoints.get ();
        System.out.println (round + " " + ns);
        if (ns == 0) {
            stop = true;
            return;
        }
        newPoints.set (0);
        round ++;
    }

    static private record LongPair (long one, long two) {}

    private final Canonical initial;
    private final Worker [] workers = new Worker [threads];
    private final CyclicBarrier barrier = new CyclicBarrier (threads, this::endOfRound);
    private final Storage storage = new Storage ();

    private volatile int round = 0;
    private volatile boolean stop = false;
    private volatile boolean found = false;

    private final ConcurrentHashMap<LongPair,LongPair> unique = new ConcurrentHashMap<>();
    private final AtomicInteger newPoints = new AtomicInteger ();
    private final DynamicArray [] [] queues = new DynamicArray [2] [Q_SIZE];

    private static final int threads = Runtime.getRuntime ().availableProcessors ();
    private static final int kindFromInitial = 0;
    private static final int kindFromSolved = 1;
    private static final int Q_SHIFT  = 16;
    private static final int Q_SIZE = 1 << Q_SHIFT;
    private static final int Q_MASK   = Q_SIZE - 1;
}
