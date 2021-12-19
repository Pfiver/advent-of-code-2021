package day14;

import java.util.Comparator;
import java.util.stream.IntStream;

import static common.IO.getInput;
import static java.util.Map.Entry.comparingByValue;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Solve1 {

    /**
     * ... Apply 10 steps of pair insertion ...
     * What do you get if you take the quantity of the most common element
     * and subtract the quantity of the least common element?
     */
    public static long solve() {

        StringBuffer p = new StringBuffer(getInput().findFirst().orElseThrow());

        var rules = getInput().skip(2)
                .map(line -> new Object() {
                    final String pair = line.substring(0, 2);
                    final Character ins = line.charAt(6);
                })
                .toList();

        for (int n = 0; n < 10; n++) {

            rules.stream()
                    .flatMap(pi -> IntStream.iterate(p.length(), i -> p.lastIndexOf(pi.pair, i - 1))
                            .skip(1)
                            .takeWhile(i -> i >= 0)
                            .mapToObj(i -> new Object() {
                                final int of = i + 1;
                                final char ch = pi.ins;
                            })
                    )
                    .sorted(Comparator.comparing(i -> -i.of))
                    .forEach(i -> p.insert(i.of, i.ch));
        }

        var frequencies = p.chars().boxed()
                .collect(groupingBy(identity(), counting()))
                .entrySet()
                .stream()
                .sorted(comparingByValue())
                .toList();

        return frequencies.get(frequencies.size() - 1).getValue() - frequencies.get(0).getValue();
    }

//    static Stream<String> getInput() {
//        return TEST_INPUT.lines();
//    }

    static String TEST_INPUT = """
            NNCB
                        
            CH -> B
            HH -> N
            CB -> H
            NH -> C
            HB -> C
            HC -> B
            HN -> C
            NN -> C
            BH -> H
            NC -> B
            NB -> B
            BN -> B
            BB -> N
            BC -> B
            CC -> N
            CN -> C
            """
            .trim();

    static String TEST_PARTIAL_SOLUTIONS = """
            Template:     NNCB
            After step 1: NCNBCHB
            After step 2: NBCCNBBBCBHCB
            After step 3: NBBBCNCCNBBNBNBBCHBHHBCHB
            After step 4: NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB
            """
            .trim();

    static int TEST_RESULT = 1588;
}
