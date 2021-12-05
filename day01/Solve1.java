package day01;

import java.util.function.LongBinaryOperator;
import java.util.function.LongPredicate;

import static common.IO.getInput;
import static common.Run.attempt;
import static java.lang.Boolean.TRUE;

public class Solve1 {

    public static void main(String[] args) throws Exception {

        attempt(
                Solve1::method1,
                Solve1::method2,
                Solve1::method3,
                Solve1::method1,
                Solve1::method5
        );
    }

    static long method1() {
        long count = 0;
        String prev = "" + Long.MAX_VALUE;
        for (String line : getInput().toList()) {
            if (Long.parseLong(line) > Long.parseLong(prev)) {
                count++;
            }
            prev = line;
        }
        return count;
    }

    static long method2() {

        return getInput()
                .mapToLong(Long::parseLong)
                .filter(new LongPredicate() {
                    long prev = Long.MAX_VALUE;

                    public boolean test(long value) {
                        boolean take = value > prev;
                        prev = value;
                        return take;
                    }
                })
                .count();
    }

    static long method3() {

        var counter = new LongBinaryOperator() {
            long count = 0;

            public long applyAsLong(long l, long r) {
                if (l < r) count++;
                return r;
            }
        };

        getInput()
                .mapToLong(Long::parseLong)
                .reduce(counter);

        return counter.count;
    }

    static long method4() {
        return Window.apply(getInput()
                                .map(Long::parseLong),
                        2
                )
                .map(w -> w.get(0) < w.get(1))
                .filter(TRUE::equals)
                .count();
    }

    static long method5() {
        class State {
            long n;
            long prev;

            State advance(String next) {
                long nextVal = Long.parseLong(next);
                if (prev < nextVal) {
                    n++;
                }
                prev = nextVal;
                return this;
            }
        }
        class InitialState extends State {
            State advance(String next) {
                long nextVal = Long.parseLong(next);
                State state = new State();
                state.prev = nextVal;
                return state;
            }
        }
        return getInput()
                .reduce(new InitialState(), State::advance, (l, r) -> {
                    throw new UnsupportedOperationException();
                }).n;
    }
}
