package day13;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solve2 {

    public static String solve() {

        var points = new SolveTransparentOrigami().fold(Integer.MAX_VALUE).stream().toList();

        int w = points.stream().mapToInt(SolveTransparentOrigami.Point::x).max().orElseThrow() + 1;
        int h = points.stream().mapToInt(SolveTransparentOrigami.Point::y).max().orElseThrow() + 1;

        char[][] lines = new char[h][w];

        Arrays.stream(lines).forEach(line -> Arrays.fill(line, '.'));

        points.forEach(p -> lines[p.y()][p.x()] = '#');

        return IntStream.iterate(0, x -> x < w, x -> x + 5)
                .mapToObj(x -> Arrays.stream(Letter.values())
                        .filter(isLetterAt(lines, x))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("no match for\n" + getPatternAt(lines, x)))
                )
                .map(Enum::name)
                .collect(Collectors.joining());
    }

    private static Predicate<? super Letter> isLetterAt(char[][] lines, int x) {

        String patternAt = getPatternAt(lines, x);

        return ltr -> ltr.pattern.equals(patternAt);
    }

    private static String getPatternAt(char[][] lines, int x) {
        return Arrays.stream(lines)
                .map(l -> new String(l, x, 4))
                .collect(Collectors.joining("\n"));
    }
}
