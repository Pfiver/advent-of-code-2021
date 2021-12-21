package day15;

import static common.IO.getInput;

public class Solve2 {

    /**
     * ... add up the risk levels of each position you enter ...
     * <p>
     * Your goal is to find a path with the lowest total risk.
     */
    public static long solve() {

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

        return Solve1.solve(risks).lowestRisk;
    }

    private static int wrap(int r) {
        return (r - 1) % 9 + 1;
    }
}
