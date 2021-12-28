package day2;

import static common.Common.throwingBinOp;
import static common.IO.getInput;

public class Dive {
    public static void main(String... args) {

        Position1 p1 = getInput()
                .map(Command::new)
                .reduce(new Position1(), Position1::advance, throwingBinOp());

        System.out.println(p1.x * p1.depth);

        Position2 p2 = getInput()
                .map(Command::new)
                .reduce(new Position2(), Position2::advance, throwingBinOp());

        System.out.println(p2.x * p2.depth);
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

    static class Position1 {
        long x, depth;
        public Position1 advance(Command command) {
            switch (command.direction) {
                case FORWARD -> x += command.value;
                case DOWN -> depth += command.value;
                case UP -> depth -= command.value;
            }
            return this;
        }
    }

    static class Position2 {
        long x, depth, aim;
        public Position2 advance(Command command) {
            switch (command.direction) {
                case FORWARD -> { x += command.value; depth += aim * command.value; }
                case DOWN -> aim += command.value;
                case UP -> aim -= command.value;
            }
            return this;
        }
    }
}
