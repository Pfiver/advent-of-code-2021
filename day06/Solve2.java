package day06;

import common.Run;

import static common.IO.getInput;

public class Solve2 {

    public static void main(String[] args) {
        Run.attempt(Solve2::solve);
    }

    static long solve() {

        return Solve1.solve(256);
    }
}
