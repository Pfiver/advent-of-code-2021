package day09;

import common.Run;
import common.Transform;

import static common.IO.getInput;

public class Solve1 {

    public static void main(String[] args) {
        Run.attempt(Solve1::solve);
    }

    static long solve() {

        byte[][] heights = Transform.decimalDigitsToPaddedValuesRectangle(getInput(), 100, Byte.MAX_VALUE);

        var scene = new Scene(heights);

        var sum = 0;
        for (int x = 1; x < 101; x++) for (int y = 1; y < 101; y++) {
            if (scene.isLow(x, y)) {
                sum += heights[y][x] + 1;
            }
        }
        return sum;
    }

    record Scene(byte[][] heights) {
        boolean isLow(int x, int y) {

            return heights[y][x] < heights[y][x+1]
                    && heights[y][x] < heights[y][x-1]
                    && heights[y][x] < heights[y+1][x]
                    && heights[y][x] < heights[y-1][x];
        }
    }
}
