package day11;

import common.Run;
import common.Transform;

import java.util.stream.LongStream;

import static common.IO.getInput;

public class Solve2 {

    public static void main(String[] args) {
        Run.attempt(Solve2::solve);
    }

    static long solve() {
        var levels2d = Transform.decimalDigitsToPaddedValuesRectangle(getInput(), 10, (byte) 0);
        var solver = new Solve1.Solver(10, 10, levels2d);
        return 1 + LongStream.generate(solver::step)
                .takeWhile(n -> n < 100)
                .count();
    }
}
