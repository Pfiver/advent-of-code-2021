package day15;

import static common.IO.getInput;

public class Solve1 {

    /**
     * ... add up the risk levels of each position you enter ...
     * <p>
     * Your goal is to find a path with the lowest total risk.
     */
    public static long solve() {

        var risks = getInput()
                .map(line -> line.chars()
                        .map(Character::getNumericValue)
                        .toArray())
                .toArray(int[][]::new);

        return solve(risks).lowestRisk;
    }

    static Node solve(int[][] risks) {

        // "skewed" Dijkstra

        int h = risks.length;
        int w = risks[0].length;
        var nodes = new Node[w * h];

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                nodes[y * w + x] = new Node();
                nodes[y * w + x].risk = risks[y][x];

                if (y > 0) {
                    nodes[y * w + x].top = nodes[(y - 1) * w + x];
                }
                if (x > 0) {
                    nodes[y * w + x].left = nodes[y * w + (x - 1)];
                }
            }
        }

        for (int y = 0; y < h - 1; y++) {
            for (int x = 0; x < w - 1; x++) {
                nodes[y * w + x].bottom = nodes[(y + 1) * w + x];
                nodes[y * w + x].right = nodes[y * w + (x + 1)];
            }
        }
        for (int y = 0; y < h - 1; y++) {
            nodes[y * w + (h - 1)].bottom = nodes[(y + 1) * w + (h - 1)];
        }
        for (int x = 0; x < w - 1; x++) {
            nodes[(h - 1) * w + x].bottom = nodes[(h - 1) * w + (x + 1)];
        }

        nodes[0].lowestRisk = 0;

//        System.out.println();
        int loop = 0;
        int round = 0;
        boolean run;
        do {
            run = false;
            loop++;
            for (Node n : nodes) {
//                if (++round % 100 == 0) {
//                    System.out.printf("\033[2K\rloop %3d / round %9d", loop, round);
//                }
//                if (++round % 100 == 0) {
//                    System.out.print("\033[2K\rround " + round);
//                }

                if (n.top != null && n.top.lowestRisk > n.lowestRisk + n.top.risk) {
                    n.top.lowestRisk = n.lowestRisk + n.top.risk;
                    n.top.comingFrom = n;
                    run = true;
                }

                if (n.left != null && n.left.lowestRisk > n.lowestRisk + n.left.risk) {
                    n.left.lowestRisk = n.lowestRisk + n.left.risk;
                    n.left.comingFrom = n;
                    run = true;
                }

                if (n.bottom != null && n.bottom.lowestRisk > n.lowestRisk + n.bottom.risk) {
                    n.bottom.lowestRisk = n.lowestRisk + n.bottom.risk;
                    n.bottom.comingFrom = n;
                    run = true;
                }

                if (n.right != null && n.right.lowestRisk > n.lowestRisk + n.right.risk) {
                    n.right.lowestRisk = n.lowestRisk + n.right.risk;
                    n.right.comingFrom = n;
                    run = true;
                }
            }
        } while (run);
//        System.out.println();

        return nodes[w * h - 1];
    }

    static class Node {
        int risk, lowestRisk = Integer.MAX_VALUE / 2;
        Node top, left, bottom, right, comingFrom;
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
