package day1;

import static common.IO.getInput;

public class SonarSweep {
    public static void main(String... args) {

        long count = 0;
        String prev = "" + Long.MAX_VALUE;
        for (String line : getInput().toList()) {
            if (Long.parseLong(line) > Long.parseLong(prev)) {
                count++;
            }
            prev = line;
        }
        System.out.println(count);

        System.out.println(Window.apply(
                        Window.apply(getInput()
                                                .map(Long::parseLong),
                                        3)
                                .map(w -> w.stream().mapToLong(Long::longValue).sum()),
                        2)
                .filter(w -> w.get(0) < w.get(1))
                .count());
    }
}
