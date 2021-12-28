package day3;

import java.util.BitSet;
import java.util.List;

import static common.IO.getInput;

public class Solve2 {

    public static final int NBITS = 12;

    public static long solve() {

        List<BitSet> numbers = getInput()
                .map(s -> {
                    BitSet bs = new BitSet(NBITS);
                    for (int i = 0; i < NBITS; i++) {
                        if (s.charAt(i) == '1') {
                            bs.set(NBITS-1-i);
                        }
                    }
                    return bs;
                })
                .toList();

        long oxy = reduce(numbers, 0, true, true);
//        System.out.println("oxy: " + oxy);

        long co2 = reduce(numbers, 0, false, false);
//        System.out.println("co2: " + co2);

        return oxy * co2;
    }

    private static long reduce(List<BitSet> numbers, int lookingAt, boolean majority, boolean onTie) {

        long total = numbers.size();
        long half = total / 2;
        boolean even = (total & 1) == 0;
        long nSet = numbers.stream().filter(bs -> bs.get(NBITS-1-lookingAt)).count();
        boolean selector;
        if (nSet > half) {
            // majority is 1
            selector = majority;
        }
        else if (even && nSet == half) {
            // tie
            selector = onTie;
        }
        else {
            // majority is 0
            selector = !majority;
        }

        List<BitSet> subset = numbers.stream().filter(bs -> bs.get(NBITS-1-lookingAt) == selector).toList();

        if (subset.size() == 1) {
            return subset.get(0).toLongArray()[0];
        }
        return reduce(subset,lookingAt + 1, majority, onTie);
    }
}
