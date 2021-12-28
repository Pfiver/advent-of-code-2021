package day5;

import common.Common;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static common.IO.getInput;

public class Solve1 {

    public static long solve() {
        var cover = getInput().reduce(new Cover(), Cover::record, Common.throwingBinOp());
        return cover.counts.values().stream().filter(n -> n > 1).count();
    }

    static class Cover {

        Map<Point, Integer> counts = new HashMap<>();

        Cover record(String line) {
            int arrow = line.indexOf(" -> ");
            Point head = newPoint(line.substring(0, arrow));
            Point tail = newPoint(line.substring(arrow + 4));
            head.walk(tail).forEach(p -> {
                var c = counts.get(p);
                int cn = c == null ? 0 : c;
                counts.put(p, cn + 1);
            });
            return this;
        }

        Point newPoint(String xy) {
            return new Point(xy);
        }
    }

    static class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Point(String xy) {
            int comma = xy.indexOf(',');
            x = Integer.parseInt(xy.substring(0, comma));
            y = Integer.parseInt(xy.substring(comma + 1));
        }

        Stream<Point> walk(Point dest) {
            if (x != dest.x && y != dest.y) {
                return Stream.empty();
            }
            var b = Stream.<Point>builder();
            b.add(this);
            if (!equals(dest)) {
                if (x == dest.x) {
                    int sign = Integer.signum(dest.y - y);
                    for (int i = y + sign; i != dest.y; i += sign) {
                        b.add(new Point(x, i));
                    }
                }
                if (y == dest.y) {
                    int sign = Integer.signum(dest.x - x);
                    for (int i = x + sign; i != dest.x; i += sign) {
                        b.add(new Point(i, y));
                    }
                }
                b.add(dest);
            }
            return b.build();
        }

        public int hashCode() {
            return 31 * x + y;
        }

        public boolean equals(Object o) {
            return o instanceof Point c && c.x == x && c.y == y;
        }
    }
}
