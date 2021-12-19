package day14;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static common.IO.getInput;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.*;

public class Solve2 {

    /**
     * ... Apply 10 steps of pair insertion ...
     * What do you get if you take the quantity of the most common element
     * and subtract the quantity of the least common element?
     */
    public static long solve() {

        StringBuffer p = new StringBuffer(getInput().findFirst().orElseThrow());
        int tail = p.charAt(p.length() - 1);

        var rules = getInput().skip(2)
                .map(line -> new Rule(line.substring(0, 2), line.charAt(6)))
                .toList();

        assertAssumptions(p, rules);

        // init rule application graph
        var ruleApplications = rules.stream()
                .map(r -> new RuleApplication(
                        r,
                        rules.stream().filter(r_ -> r_.pair.equals(r.pair.charAt(0) + "" + r.ins)).findFirst().orElseThrow(),
                        rules.stream().filter(r_ -> r_.pair.equals(r.ins + "" + r.pair.charAt(1))).findFirst().orElseThrow()))
                .toList();

        // init rule counts
        IntStream.range(0, p.length() - 1)
                .mapToObj(i -> p.substring(i, i + 2))
                .forEach(pair -> rules.stream().filter(r -> r.pair.equals(pair)).findFirst().orElseThrow().count++);

        // apply rules
        for (int i = 0; i < 40; i++) {
            // update rule counts
            var ruleApplicationCounts = ruleApplications.stream().map(appl -> new Object() {
                RuleApplication a = appl;
                long cnt = a.in.count;
            }).toList();
            rules.forEach(r -> r.count = 0);
            ruleApplicationCounts.forEach(rac -> {
                rac.a.out1.count += rac.cnt;
                rac.a.out2.count += rac.cnt;
            });
        }

        // init character frequencies
        var frequencies = rules.stream()
                .flatMapToInt(r -> r.pair.chars())
                .boxed()
                .distinct()
                .collect(Collectors.toMap(Function.identity(), v -> (long) 0));

        // update character frequencies
        rules.forEach(r -> frequencies.compute((int) r.pair.charAt(0), (k, v) -> v + r.count));

        frequencies.put(tail, frequencies.get(tail) + 1);

        var sortedFrequencies = frequencies.entrySet().stream()
                .collect(groupingBy(Map.Entry::getKey, mapping(Map.Entry::getValue, Collectors.summingLong(Long::longValue))))
                .entrySet()
                .stream()
                .sorted(comparingByValue())
                .toList();

        return sortedFrequencies.get(sortedFrequencies.size() - 1).getValue() - sortedFrequencies.get(0).getValue();
    }

    static class Rule {
        String pair;
        Character ins;
        long count;

        Rule(String pair, Character ins) {
            this.pair = pair;
            this.ins = ins;
        }
    }

    record RuleApplication(Rule in, Rule out1, Rule out2) {
    }

    static void assertAssumptions(StringBuffer p, List<Rule> rules) {

        IntStream.range(0, p.length() - 1)
                .mapToObj(i -> p.substring(i, i + 2))
                .forEach(pair -> {
                    if (rules.stream().filter(r -> r.pair.equals(pair)).count() != 1) {
                        throw new UnsupportedOperationException("pair not covered by rule: '%s'".formatted(pair));
                    }
                });

        rules.forEach(rule -> {
            var out = rule.pair.charAt(0) + "" + rule.ins + "" + rule.pair.charAt(1);
            if (rules.stream().filter(r -> r.pair.equals(rule.pair.charAt(0) + "" + rule.ins)).count() != 1
                    || rules.stream().filter(r -> r.pair.equals(rule.ins + "" + rule.pair.charAt(1))).count() != 1) {
                throw new UnsupportedOperationException("rule output not fully covered by rules: '%s'".formatted(out));
            }
        });
    }
}
