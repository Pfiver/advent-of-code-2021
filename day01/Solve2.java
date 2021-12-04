package day01;

import static common.IO.getInput;
import static common.Run.attempt;

class Solve2 {

    public static void main(String[] args) {

        attempt(2,
                Solve2::method1
        );
    }

    static long method1() {
        return Window.apply(
                        Window.apply(getInput()
                                        .map(Long::parseLong),
                                3)
                                .map(w -> w.stream().mapToLong(Long::longValue).sum()),
                        2)
                .filter(w -> w.get(0) < w.get(1))
                .count();
    }
}
