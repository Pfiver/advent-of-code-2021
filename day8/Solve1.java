package day8;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.IntStream;

import static common.IO.getInput;
import static java.util.Arrays.asList;

public class Solve1 {

    public static long solve() {

        Collection<Integer> segmentCountsOfUniqueSegmentCountDigits = asList(
                2 // 1
                , 4 // 4
                , 3 // 7
                , 7 // 8
        );

        return getInput()
                .map(Record::new)
                .mapToLong(r -> Arrays.stream(r.output.digits)
                        .map(SevenSeg::onCount)
                        .filter(segmentCountsOfUniqueSegmentCountDigits::contains)
                        .count()
                )
                .sum();
    }

    static class Record {

        Display output;

        Record(String line) {
            String[] patternsAndOutput = line.split(" \\| ");
            output = new Display(patternsAndOutput[1]);
        }
    }

    static class Display {

        SevenSeg[] digits;
        Wiring wiring;

        Display(String sevenSegs) {
            digits = Arrays.stream(sevenSegs.split(" "))
                    .map(SevenSeg::new)
                    .toArray(SevenSeg[]::new);
        }
    }

    static class SevenSeg {

        int segments;
        static int[] MASKS = IntStream.range(0, 7).map(idx -> 1 << idx).toArray();

        SevenSeg(String onSegments) {
            onSegments.chars().forEach(c -> segments |= 1 << (c - 'a'));
        }

        int onCount() {
            return (int) IntStream.range(0, 7).filter(idx -> (segments & MASKS[idx]) > 0).count();
        }
    }

    static class Wiring {

    }
}
