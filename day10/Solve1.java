package day10;

import common.IO;
import common.Run;

import java.util.Arrays;
import java.util.Optional;
import java.util.Stack;

public class Solve1 {

    public static void main(String[] args) {
        Run.attempt(Solve1::solve);
    }

    enum Chunk {
        PAREN("()", 3), BRACKET("[]", 57), BRACE("{}", 1197), ANGLE("<>", 25137);
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
        return IO.getInput().mapToInt(line -> {
            Stack<Chunk> stack = new Stack<>();
            return line.chars().map(c -> {
                if (Chunk.open((char) c).map(stack::push).isEmpty()) {
                    var close = Chunk.close((char) c).orElseThrow();
                    if (stack.peek() == close) {
                        stack.pop();
                        return 0;
                    }
                    return close.score;
                }
                return 0;
            })
            .filter(i -> i > 0)
            .findFirst()
            .orElse(0);
        })
        .sum();
    }
}
