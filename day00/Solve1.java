package day00;

import common.Run;

import static common.IO.getInput;

public class Solve1 {

    public static void main(String[] args) {
        Run.attempt(Solve1::solve);
    }

    static long solve() {

        return getInput().count();
    }
}
