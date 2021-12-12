package day02;

import day02.Solve1.Command;

import static common.IO.getInput;

public class Solve2 {

    public static long solve() {

        Position p = getInput()
                .map(Command::new)
                .reduce(new Position(), Position::advance, (l, r) -> {
                    throw new UnsupportedOperationException();
                });

        return p.x * p.depth;
    }

    static class Position {
        long x, depth, aim;
        public Position advance(Command command) {
            switch (command.direction) {
                case FORWARD -> { x += command.value; depth += aim * command.value; }
                case DOWN -> aim += command.value;
                case UP -> aim -= command.value;
            }
            return this;
        }
    }
}
