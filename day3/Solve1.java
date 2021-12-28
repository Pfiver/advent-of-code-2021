package day3;

import java.util.BitSet;

import static common.IO.getInput;

public class Solve1 {

    public static long solve() {

        return getInput()
                .reduce(new BitCounts(), BitCounts::add, (l, r) -> {
                    throw new UnsupportedOperationException();
                })
                .getGammaTimesEpsilon();
    }

    static class BitCounts {

        long n;
        long[] counts = new long[12];

        public BitCounts add(String s) {
            n++;
            for (int i = 0; i < counts.length; i++) {
                if (s.charAt(i) == '1') {
                    counts[i]++;
                }
            }
            return this;
        }

        long getGamma() {
            long gamma = 0;
            long half = n / 2;
            boolean even = (n & 1) == 0;
            for (int i = 0, countsLength = counts.length; i < countsLength; i++) {
                if (even && counts[i] == half) {
                    throw new UnsupportedOperationException("unspecified behavior: count == n / 2");
                }
                if (counts[i] > half) {
                    gamma |= 1L << (countsLength - 1 - i);
                }
            }

            return gamma;
        }

        long getGammaTimesEpsilon() {
            long gamma = getGamma();
//            System.out.printf("gamma: %12s -> %d%n", Long.toBinaryString(gamma), gamma);
            BitSet epsilon = BitSet.valueOf(new long[] { ~gamma });
            epsilon.clear(counts.length, Long.SIZE);
//            System.out.printf("epsilon: %12s -> %d%n", Long.toBinaryString(epsilon.toLongArray()[0]), epsilon.toLongArray()[0]);
            return gamma * epsilon.toLongArray()[0];
        }
    }
}
