package day15;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import static common.IO.getInput;

public class Solve0 {


    public static long solve() {
        return -1
//                + solve1()
//                + solve2()
        ;
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
        boolean run = true;
        while (run) {
            run = false;
//            for (int y = 0; y < h; y++)
//            for (int x = 0; x < w; x++) {
//                Node n = nodes[y][x];

            for (Optional<Node> next = Optional.of(nodes[0][0]);
                 next.isPresent();
                 next = Arrays.stream(nodes).flatMap(Arrays::stream)
                         .filter(Objects::nonNull)
                         .filter(n -> !n.visited)
                         .min(Comparator.comparing(n -> n.lowestRisk))) {
                Node n = next.get();

                if (++round % 100 == 0) {
                    System.out.print("\033[2K\rround " + round);
                }

                if (n.left != null && n.left.lowestRisk > n.lowestRisk + n.left.risk) {
                    n.left.lowestRisk = n.lowestRisk + n.left.risk;
                    n.left.comingFrom = n;
                    run = true;
                }

                if (n.right != null && n.right.lowestRisk > n.lowestRisk + n.right.risk) {
                    n.right.lowestRisk = n.lowestRisk + n.right.risk;
                    n.right.comingFrom = n;
                    run = true;
                }

                if (n.top != null && n.top.lowestRisk > n.lowestRisk + n.top.risk) {
                    n.top.lowestRisk = n.lowestRisk + n.top.risk;
                    n.top.comingFrom = n;
                    run = true;
                }

                if (n.bottom != null && n.bottom.lowestRisk > n.lowestRisk + n.bottom.risk) {
                    n.bottom.lowestRisk = n.lowestRisk + n.bottom.risk;
                    n.bottom.comingFrom = n;
                    run = true;
                }

                n.visited = true;
            }
        }
        System.out.println();

        return nodes[h-1][w-1];
    }

    static class Node {
        boolean visited;
        int risk, lowestRisk = Short.MAX_VALUE;
        Node left, right, top, bottom, comingFrom;
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
