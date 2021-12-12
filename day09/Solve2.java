package day09;

import common.Transform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static common.IO.getInput;

public class Solve2 {

    public static long solve() {

        byte[][] heights = Transform.decimalDigitsToPaddedValuesRectangle(getInput(), 100, Byte.MAX_VALUE);

        return new Scene(heights).getLowPoints()
//                .parallel() // buuuuh --- no gain :-( / running time of ~50ms probably too low to produce an obvious effect ...
                .map(lp -> lp.getBasin(heights))
                .mapToInt(Basin::size)
                .map(i -> ~i).sorted().map(i -> ~i)
                .limit(3).reduce(1, (s1, s2) -> s1 * s2);
    }

    record Scene(byte[][] heights) {
        boolean isLow(int x, int y) {
            return heights[y][x] < heights[y][x + 1]
                    && heights[y][x] < heights[y][x - 1]
                    && heights[y][x] < heights[y + 1][x]
                    && heights[y][x] < heights[y - 1][x];
        }

        Stream<LowPoint> getLowPoints() {
            return IntStream.range(1, 101)
                    .mapToObj(x -> IntStream.range(1, 101)
                            .filter(y -> isLow(x, y))
                            .mapToObj(y -> new LowPoint(x, y)))
                    .flatMap(Function.identity());
        }
    }

    record LowPoint(int x, int y) {
        Basin getBasin(byte[][] heights) {
            Collection<Long> points = new ArrayList<>();
            addThisAndAllBelowNineGreaterOrEqualNeighbours(heights, -1, x, y, points);
            return new Basin(points.stream().mapToLong(Long::longValue).sorted().toArray());
        }

        void addThisAndAllBelowNineGreaterOrEqualNeighbours(byte[][] heights, int origin, int x, int y, Collection<Long> out) {
            var self = x + y * 102L;
            if (!out.contains(self)) {
                out.add(self);
                var height = heights[y][x];
                findGreaterOrEqualBelowNinNeighbours(heights, 0, height, x + 1, y, out);
                findGreaterOrEqualBelowNinNeighbours(heights, 1, height, x, y + 1, out);
                findGreaterOrEqualBelowNinNeighbours(heights, 2, height, x - 1, y, out);
                findGreaterOrEqualBelowNinNeighbours(heights, 3, height, x, y - 1, out);
            }
        }

        void findGreaterOrEqualBelowNinNeighbours(byte[][] heights, int origin, int originHeight, int x, int y, Collection<Long> out) {
            var height = heights[y][x];
            if (height < 9 && height >= originHeight) {
                addThisAndAllBelowNineGreaterOrEqualNeighbours(heights, origin, x, y, out);
            }
        }
    }

    record Basin(long[] points) {
        public boolean equals(Object obj) {
            return obj instanceof Basin b && Arrays.equals(points, b.points);
        }

        int size() {
            return points.length;
        }
    }
}
