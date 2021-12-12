package day01;

import static common.IO.getInput;

public class Solve2 {

    public static long solve() {

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
