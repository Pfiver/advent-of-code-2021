package day08;

import common.Run;

import java.util.Arrays;
import java.util.BitSet;
import java.util.stream.IntStream;

import static common.IO.getInput;
import static day08.Solve2.Display.SevenSeg.segmentsIndices;
import static java.lang.Integer.bitCount;

public class Solve2 {

    public static void main(String[] args) {
        Run.attempt(Solve2::solve);
    }

    static long solve() {

        return getInput()
                .map(Record::new)
                .mapToInt(r -> r.output.display()).sum();
    }

    static class Record {

        Display output;
        byte[] patterns;

        Record(String line) {
            String[] patternsAndOutput = line.split(" \\| ");
            var patternInputs = patternsAndOutput[0].split(" ");

            patterns = new byte[patternInputs.length];
            IntStream.range(0, patterns.length)
                    .forEach(i -> patternInputs[i].chars()
                            .forEach(c -> patterns[i] |= 1 << (c - 'a')));

            output = new Display(patternsAndOutput[1]);

//            System.out.println("r: " + Arrays.toString(patterns)
//                    + " | " + Arrays.stream(output.digits).map(d -> d.segments).toList());

            output.wiring = new Wiring().learn(this.patterns);
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

        public int display() {
            return 1000 * digits[0].display()
                    + 100 * digits[1].display()
                    + 10 * digits[2].display()
                    + digits[3].display();
        }


        class SevenSeg {

            String input;
            byte segments; // a bitset - the first seven bits represent the on-state of segments 'a' to 'g'

            SevenSeg(String onSegments) {
                input = onSegments;
                onSegments.chars().forEach(c -> segments |= 1 << (c - 'a'));
            }

            static int[] segmentsIndices(byte pattern) {
                return IntStream.range(0, 7).filter(i -> (pattern & (1 << i)) > 0).toArray();
            }

            final byte[] representation = new byte[]{
                    Byte.parseByte("1110111", 2), // 0
                    Byte.parseByte("0100100", 2), // 1
                    Byte.parseByte("1011101", 2), // 2
                    Byte.parseByte("1101101", 2), // 3
                    Byte.parseByte("0101110", 2), // 4
                    Byte.parseByte("1101011", 2), // 5
                    Byte.parseByte("1111011", 2), // 6
                    Byte.parseByte("0100101", 2), // 7
                    Byte.parseByte("1111111", 2), // 8
                    Byte.parseByte("1101111", 2), // 9
            };

            int display() {
                var translation = wiring.translate(segments);
                for (int i = 0; i < representation.length; i++) {
                    if (translation == representation[i]) {
                        return i;
                    }
                }
                throw new IllegalStateException("?");
            }

            void show() {
                System.out.println("on      : " + input);
                System.out.printf("wires   : '%7s'%n", Integer.toBinaryString(segments));
                System.out.printf("segments: '%7s'%n", Integer.toBinaryString(wiring.translate(segments)));
            }
        }
    }

    static class Wiring {

        // mapping[<wire>] = <segment>;
        int[] mapping = new int[] {
                -1, -1, -1, -1, -1, -1, -1
        };
        int a=0, b=1, c=2, d=3, e=4, f=5, g=6;

        Wiring learn(byte[] patterns) {

            byte pattern1 = 0;
            byte pattern4 = 0;
            byte pattern_d_or_e = 0;
            byte[] six_bit_patterns = new byte[3];
            byte[] five_bit_patterns = new byte[3];

            /*      0: 2 bits -> find .c_or_f.
             *      1: 6 bits -> find "c" + "f"
             *      2: 3 bits -> find "a"
             *      3: 4 bits -> find .b_or_d.
             *      4: 5 bits -> find "b" + "d"
             *      5: 6 bits -> find "e" + "g"
             *      6: done
             */
            int state = 0;
            int substate = 0; // used in 1

            while (state < 6) {
                int prevState = state;
                for (byte pattern : patterns) {

                    /*     2: // 1
                     *     3: // 7
                     *     4: // 4
                     *     5: // 2, 3, 5
                     *     6: // 6, 9, 0
                     *     7: // 8
                     */
                    int bitCount = bitCount(pattern);

                    if (state == 0 && bitCount == 2) {
                        pattern1 = pattern;
                        state = 1;
                    }
                    else if (state == 1 && bitCount == 6) {
                        six_bit_patterns[substate++] = pattern;
                        if (substate == 3) {
                            byte pattern_c = -1;
                            for (int i = 0; i<3; i++) {
                                if ((six_bit_patterns[i] & pattern1) != pattern1) {
                                    pattern_c = (byte) ~six_bit_patterns[i];
                                    mapping[segmentsIndices(pattern_c)[0]] = c;
                                }
                                pattern_d_or_e |= ~six_bit_patterns[i];

                            }
                            mapping[segmentsIndices((byte)(pattern1 & ~pattern_c))[0]] = f;
//                            System.out.println("learned 1: " + Arrays.toString(mapping));
                            state = 2;
                            substate = 0;
                        }
                    }
                    else if (state == 2 && bitCount == 3) {
                        mapping[segmentsIndices((byte) (pattern & ~pattern1))[0]] = a;
//                        System.out.println("learned 2: " + Arrays.toString(mapping));
                        state = 3;
                    }
                    else if (state == 3 && bitCount == 4) {
                        pattern4 = pattern;
                        state = 4;
                    }
                    else if (state == 4 && bitCount == 5) {
                        five_bit_patterns[substate++] = pattern;
                        if (substate == 3) {
                            byte pattern_d = (byte)(pattern4
                                            & five_bit_patterns[0]
                                            & five_bit_patterns[1]
                                            & five_bit_patterns[2]);
                            mapping[segmentsIndices(pattern_d)[0]] = d;
                            mapping[segmentsIndices((byte)(pattern4 & ~pattern1 & ~pattern_d))[0]] = b;
                            state = 5;
                            substate = 0;
//                            System.out.println("learned 4: " + Arrays.toString(mapping));
                        }
                    }
                    else if (state == 5 && bitCount == 6) {
                        byte pattern_e = (byte) (pattern_d_or_e & ~pattern4);
                        mapping[segmentsIndices(pattern_e)[0]] = e;
                        for (int i = 0; i < 7; i++) {
                            if (mapping[i] == -1) {
                                mapping[i] = g;
                                break;
                            }
                        }
//                        System.out.println("learned 5: " + Arrays.toString(mapping));
                        state = 6;
                    }
                }
                if (state == prevState) {
                    throw new IllegalStateException("no progress in state " + state);
                }
            }

            return this;
        }

        byte translate(byte wires) {
            var in = BitSet.valueOf(new byte[]{wires});
            var out = new BitSet(7);
            in.stream().map(i -> mapping[i]).forEach(out::set);
            return out.toByteArray()[0];
        }
    }
}
