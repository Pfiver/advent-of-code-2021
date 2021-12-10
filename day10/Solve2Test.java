package day10;

import static day10.Solve2.solve;

public class Solve2Test {

    static String input = """
            [({(<(())[]>[[{[]{<()<>>
            [(()[<>])]({[<{<<[]>>(
            {([(<{}[<>[]}>{[]{[(<()>
            (((({<>}<{<{<>}{[]{[]{}
            [[<[([]))<([[{}[[()]]]
            [{[{({}]{}}([{[{{{}}([]
            {<[[]]>}<{[{[{[]{()[[[]
            [<(<(<(<{}))><([]([]()
            <{([([[(<>()){}]>(<<{{
            <{([{{}}[<[[[<>{}]]]>[]]
            """;

    public static void main(String[] args) {

        var actual = solve(input.lines());

        if (actual != 288957) {
            throw new AssertionError("expected " + 288957 + " but actual is " + actual);
        }
    }
}
