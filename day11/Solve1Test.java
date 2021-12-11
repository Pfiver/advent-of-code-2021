package day11;

import common.Transform;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static common.Transform.flatten;
import static day11.Solve1TestRef.FLASHES_AFTER_100;

public class Solve1Test {

    static String input = """
            5483143223
            2745854711
            5264556173
            6141336146
            6357385478
            4167524645
            2176841721
            6882881134
            4846848554
            5283751526
            """;

    public static void main(String[] args) {

        var levels2d = Transform.decimalDigitsToPaddedValuesRectangle(input.lines(), 10, (byte) 0);
        Solve1.Solver solver = new Solve1.Solver(10, 10, levels2d);

        long cnt = 0;

        assertLevels(Solve1TestRef.BEFORE_ANY_STEPS, solver);

        cnt += solver.step();
        assertLevels(Solve1TestRef.AFTER_STEP_1, solver);

        cnt += solver.step();
        assertLevels(Solve1TestRef.AFTER_STEP_2, solver);

        cnt += LongStream.generate(solver::step)
                .limit(98)
                .sum();

        assertEquals(FLASHES_AFTER_100, cnt);
    }

    private static void assertLevels(String expected, Solve1.Solver solver) {
        assertEquals(toString(flatten(Transform.decimalDigitsToPaddedValuesRectangle(expected.lines(), 10, (byte) 0))), toString(solver.levels()));
    }

    private static void assertEquals(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("expected\n" + expected + "\n but actual is\n" + actual);
        }
    }

    static void print(Solve1.Solver solver) {
        System.out.println("levels.length: " + solver.levels().length);
        System.out.println(toString(solver.levels()));
    }

    static String toString(byte[] levels) {
        var repr = IntStream.range(0, levels.length)
                .mapToObj(i -> Byte.toString(levels[i]))
                .collect(Collectors.joining());
        return IntStream.range(0, 12)
                .mapToObj(i -> repr.substring(i * 12, i * 12 + 12))
                .collect(Collectors.joining("\n"));
    }
}
