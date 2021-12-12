package day02;

import static common.IO.getInput;

public class Solve1 {

    public static long solve() {

        Position p = getInput()
                .map(Command::new)
                .reduce(new Position(), Position::advance, (l, r) -> {
                    throw new UnsupportedOperationException();
                });

        return p.x * p.depth;
    }

    static class Command {
        enum Direction {FORWARD, DOWN, UP}

        Direction direction;
        int value;

        Command(String line) {
            String[] cv = line.split(" ");
            direction = switch (cv[0]) {
                case "forward" -> Direction.FORWARD;
                case "down" -> Direction.DOWN;
                case "up" -> Direction.UP;
                default -> throw new IllegalStateException();
            };
            value = Integer.parseInt(cv[1]);
        }
    }

    static class Position {
        long x, depth;

        public Position advance(Command command) {
            switch (command.direction) {
                case FORWARD -> x += command.value;
                case DOWN -> depth += command.value;
                case UP -> depth -= command.value;
            }
            return this;
        }
    }
}
