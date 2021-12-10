package day10;

import common.IO;
import common.Run;

import java.util.*;
import java.util.stream.Stream;

public class Solve2 {

    public static void main(String[] args) {
        Run.attempt(Solve2::solve);
    }

    enum Chunk {
        PAREN("()", 1), BRACKET("[]", 2), BRACE("{}", 3), ANGLE("<>", 4);
        Chunk(String s, int score) {open = s.charAt(0);close = s.charAt(1);this.score = score;}
        final char open, close;
        final int score;
        static Optional<Chunk> open(char open) {
            return Arrays.stream(values()).filter(c -> c.open == open).findFirst();
        }
        static Optional<Chunk> close(char close) {
            return Arrays.stream(values()).filter(c -> c.close == close).findFirst();
        }
    }

    static long solve() {
        return solve(IO.getInput());
    }

    static long solve(Stream<String> input) {
        long[] scores = input.map(line -> {
            Stack<Chunk> stack = new Stack<>();
            for (char c : line.toCharArray()) {
                if (Chunk.open(c).map(stack::push).isEmpty()) {
                    if (Chunk.close(c).orElseThrow() != stack.peek()) {
                        return null;
                    }
                    stack.pop();
                }
            }
            Collections.reverse(stack);
            return stack;
        })
                .filter(Objects::nonNull)
                .mapToLong(stack -> stack.stream().mapToLong(c -> c.score).reduce(0, (total, cur) -> total * 5 + cur))
                .sorted()
                .toArray();

//        System.out.println("scores: " + Arrays.toString(scores));

        return scores[scores.length / 2];
    }
}
