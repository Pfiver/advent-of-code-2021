package day15;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static common.IO.getInput;

public class Solve0 {

    public static void main(String[] args) {
        System.out.println(solve1());
        System.out.println(solve2());
    }

    /**
     * ... add up the risk levels of each position you enter ...
     * <p>
     * Your goal is to find a path with the lowest total risk.
     */
    public static long solve1() {

        var risks = getInput()
                .map(line -> line.chars()
                        .map(Character::getNumericValue)
                        .toArray())
                .toArray(int[][]::new);

        return solve(risks).lowestRisk;
    }

    public static long solve2() {

        var baseRisks = getInput()
                .map(line -> line.chars()
                        .map(Character::getNumericValue)
                        .toArray())
                .toArray(int[][]::new);

        var risks = new int[baseRisks.length*5][baseRisks[0].length*5];
        int baseLen = baseRisks.length;
        int baseHeight = baseRisks[0].length;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int x = 0; x < baseLen; x++) {
                    for (int y = 0; y < baseHeight; y++) {

                        int r = baseRisks[y][x] + i + j;
                        risks[j*baseHeight+y][i*baseLen+x] = wrap(r);
                    }
                }
            }
        }

        return solve(risks).lowestRisk;
    }

    private static int wrap(int r) {
        return (r - 1) % 9 + 1;
    }

    static Node solve(int[][] risks) {

        // Dijkstra

        int h = risks.length;
        int w = risks[0].length;
        var nodes = new Node[h][w];

        IntStream.iterate(0, y -> y < h, y -> y + 1)
                .forEach(y -> IntStream.iterate(0, x -> x < w, x -> x + 1)
                        .forEach(x -> {
                            nodes[y][x] = new Node();
                            nodes[y][x].risk = risks[y][x];
                        }));

        IntStream.iterate(0, y -> y < h, y -> y + 1)
                .forEach(y -> IntStream.iterate(0, x -> x < w, x -> x + 1)
                        .forEach(x -> {
                            if (y > 0) nodes[y][x].top = nodes[y - 1][x];
                            if (x < w-1) nodes[y][x].right = nodes[y][x + 1];
                            if (y < h-1) nodes[y][x].bottom = nodes[y + 1][x];
                            if (x > 0) nodes[y][x].left = nodes[y][x - 1];
                        }));

        nodes[0][0].lowestRisk = 0;

        int round = 0;
        int minRounds = w * h;
        long ref = System.nanoTime();
        boolean run = true;
        while (run) {
            run = false;

//            NodeIterator ni = iterateNaive(nodes);
//            NodeIterator ni = iterateSkewed(nodes);
            NodeIterator ni = iteratePrioQ(nodes);
            while (ni.hasNext()){
                Node n = ni.next();

                if (++round % 100 == 0) {
                    long ref2 = System.nanoTime();
                    long avg = (ref2 - ref) / round / 1000;
                    System.out.printf("\033[2K\rround %6d / min. %6d / avg. %6d µs p. rnd.", round, minRounds, avg);
                }

                if (n.left != null && n.left.lowestRisk > n.lowestRisk + n.left.risk) {
                    n.left.lowestRisk = n.lowestRisk + n.left.risk;
                    n.left.comingFrom = n;
                    ni.update(n.left);
                    run = true;
                }

                if (n.right != null && n.right.lowestRisk > n.lowestRisk + n.right.risk) {
                    n.right.lowestRisk = n.lowestRisk + n.right.risk;
                    n.right.comingFrom = n;
                    ni.update(n.right);
                    run = true;
                }

                if (n.top != null && n.top.lowestRisk > n.lowestRisk + n.top.risk) {
                    n.top.lowestRisk = n.lowestRisk + n.top.risk;
                    n.top.comingFrom = n;
                    ni.update(n.top);
                    run = true;
                }

                if (n.bottom != null && n.bottom.lowestRisk > n.lowestRisk + n.bottom.risk) {
                    n.bottom.lowestRisk = n.lowestRisk + n.bottom.risk;
                    n.bottom.comingFrom = n;
                    ni.update(n.bottom);
                    run = true;
                }

                n.visited = true;
            }
            if (ni.hasPriorityQueueSemantics()) {
                break;
            }
        }
        System.out.println();

        return nodes[h-1][w-1];
    }

    static NodeIterator iterateNaive(Node[][] nodes) {
        return new DelegatingNodeIterator(Stream.generate(() -> Arrays.stream(nodes).flatMap(Arrays::stream)
                        .filter(Objects::nonNull)
                        .filter(n -> !n.visited)
                        .min(Comparator.comparing(n -> n.lowestRisk)))
                .takeWhile(Optional::isPresent)
                .flatMap(Optional::stream)
                .iterator(), true);
    }
    interface NodeIterator extends Iterator<Node> {
        void update(Node it);
        boolean hasPriorityQueueSemantics();
    }
    static class DelegatingNodeIterator implements NodeIterator {
        private final Iterator<Node> delegate;
        private final boolean hasPriorityQueueSemantics;
        DelegatingNodeIterator(Iterator<Node> delegate, boolean hasPriorityQueueSemantics) {
            this.delegate = delegate;
            this.hasPriorityQueueSemantics = hasPriorityQueueSemantics;
        }
        public boolean hasNext() { return delegate.hasNext(); }
        public Node next() { return delegate.next(); }
        public void update(Node it) {}
        public boolean hasPriorityQueueSemantics() { return hasPriorityQueueSemantics; }
    }
    static NodeIterator iteratePrioQ(Node[][] nodes) {
        System.out.println("visitted: " + Arrays.stream(nodes).flatMap(Arrays::stream).filter(Node::visited).count());
        var q = new PriorityQueue<>(Comparator.comparing((Node n) -> n.lowestRisk));
        q.addAll(Arrays.stream(nodes).flatMap(Arrays::stream).toList());
        return new NodeIterator() {
            int nDuplicates;
            public boolean hasNext() {
                return q.size() > nDuplicates;
            }
            public Node next() {
                Node next;
                do {
                    next = q.poll();
                    if (next.visited()) {
                        System.out.println("duped--");
//                        nDuplicates--;
//                        continue;
                    }
                } while(false);
//                if (next.visited()) System.out.println("hä 2??");
                return next;
            }
            public void update(Node it) {
                if (it.visited()) {
                    System.out.print("\nhä 1?");
//                    return;
                }
                q.siftUp(it);
//                q.remove(it);
//                q.add(it);
//                nDuplicates++;
            }
            public boolean hasPriorityQueueSemantics() {
                return true;
            }
        };
    }
    static NodeIterator iterateSkewed(Node[][] nodes) {
        int h = nodes.length;
        int w = nodes[0].length;
        return new DelegatingNodeIterator(IntStream.iterate(0, i -> i < w, i -> i + 1).boxed()
                .flatMap(i -> IntStream.iterate(0, j -> j < h, j -> j + 1).boxed()
                        .map(j -> nodes[i][j]))
                .iterator(), false);
    }
    interface Visitable {
        boolean visited();
    }
    static class Node implements Visitable {
        boolean visited;
        int risk, lowestRisk = Short.MAX_VALUE;
        Node left, right, top, bottom, comingFrom;
        public boolean visited() {
            return visited;
        }
    }

    static String TEST_INPUT = """
            1163751742
            1381373672
            2136511328
            3694931569
            7463417111
            1319128137
            1359912421
            3125421639
            1293138521
            2311944581
            """
            .trim();

    static int TEST_RESULT = 40;
}
