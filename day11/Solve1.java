package day11;

import common.Transform;

import java.util.stream.LongStream;

import static common.IO.getInput;

public class Solve1 {

    public static long solve() {
        var levels2d = Transform.decimalDigitsToPaddedValuesRectangle(getInput(), 10, (byte) 0);
        var solver = new Solver(10, 10, levels2d);
        return LongStream.generate(solver::step)
                .limit(100)
                .sum();
    }

    record Solver(int width, int height, byte[] levels) {

        Solver(int width, int height, byte[][] startLevels2d) {
            this(width + 2, height + 2, Transform.flatten(startLevels2d));
        }

        long step() {
            resetPadding();
            resetFlashers();
            return flash(1, 11, 1, 11);
        }

        int flash(int xLow, int xHigh, int yLow, int yHigh) {
            int cnt = 0;
            for (int x = xLow; x < xHigh; x++) {
                for (int y = yLow; y < yHigh; y++) {
                    cnt += flash(x, y);
                }
            }
            return cnt;
        }

        int flash(int x, int y) {
            int i = x + width * y;
            if (levels[i] < 0) {
                return 0;
            }
            else if (levels[i] < 9) {
                levels[i]++;
                return 0;
            }
            else /* levels[i] == 9 */ {
                levels[i] = -1;
                return 1 + flash(x - 1, x + 2, y - 1, y + 2);
            }
        }

        void resetFlashers() {
            for (int i = 0, n = levels.length; i < n; i++) {
                if (levels[i] < 0) {
                    levels[i] = 0;
                }
            }
        }

        void resetPadding() {
            int maxX = width -1, maxY = height - 1;
            for (int x =    0; x < maxX; x++) levels[x]                = 0; // bottom
            for (int y =    0; y < maxY; y++) levels[maxX + y * width] = 0; // right
            for (int x = maxX; x >    0; x--) levels[x + maxY * width] = 0; // top
            for (int y = maxY; y >    0; y--) levels[y * width]        = 0; // left
        }
    }
}
