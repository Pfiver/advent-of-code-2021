package day09;

import common.Run;

import java.util.Arrays;
import java.util.stream.Stream;

import static common.IO.getInput;

public class Solve1 {

    public static void main(String[] args) {
        Run.attempt(Solve1::solve);
    }

    static long solve() {

        byte[][] heights = getHeights();

        var scene = new Scene(heights);

        var sum = 0;
        for (int x = 1; x < 101; x++) for (int y = 1; y < 101; y++) {
            if (scene.isLow(x, y)) {
                sum += heights[y][x] + 1;
            }
        }
        return sum;
    }

    static byte[][] getHeights() {
        byte[] highs = new byte[102];
        Arrays.fill(highs, Byte.MAX_VALUE);
        var heights = Stream.concat(Stream.concat(Stream.of(highs), getInput()
                .map(line -> {
                    var i = 1;
                    var h = new byte[102];
                    h[0] = h[101] = Byte.MAX_VALUE;
                    for (char c : line.toCharArray()) {
                        h[i++] = (byte) Character.getNumericValue(c);
                    }
                    return h;
                })), Stream.of(highs))
                .toArray(byte[][]::new);
        return heights;
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