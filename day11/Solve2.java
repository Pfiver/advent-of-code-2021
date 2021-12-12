package day11;

import common.Transform;

import java.util.stream.LongStream;

import static common.IO.getInput;

public class Solve2 {

    public static long solve() {
        var levels2d = Transform.decimalDigitsToPaddedValuesRectangle(getInput(), 10, (byte) 0);
        var solver = new Solve1.Solver(10, 10, levels2d);
        return 1 + LongStream.generate(solver::step)
                .takeWhile(n -> n < 100)
                .count();
    }
}
