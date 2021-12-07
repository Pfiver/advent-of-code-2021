package day07;

import common.Run;

import java.util.Arrays;

import static common.IO.getInput;

public class Solve1 {

    public static void main(String[] args) {
        Run.attempt(Solve1::solve);
    }

    static long solve() {

        String[] input = getInput()
                .findFirst().orElseThrow().split(",");
        int[] subs = Arrays.stream(input).mapToInt(Integer::parseInt).toArray();

        Arrays.sort(subs);

        int minPos = Arrays.stream(subs).min().orElseThrow();
        int maxPos = Arrays.stream(subs).max().orElseThrow();

        long[] fuel = new long[maxPos - minPos + 1];

        int zeroSubIndex = 0;
        long subCount = subs.length;

        // TODO: optimize by derivating fuel[n] from fuel[n-1] ...
//        for (int sub : subs) {
//            fuel[0] += sub - minPos;
//        }

        for (int pos = minPos; pos <= maxPos; pos++) {
            int posIndex = pos - minPos;
            while (pos > subs[zeroSubIndex]) {
                zeroSubIndex++;
            }
            for (int i = 0; i < zeroSubIndex; i++) {
                fuel[posIndex] += pos - subs[i];
            }
            for (int i = zeroSubIndex; i < subCount; i++) {
                fuel[posIndex] += subs[i] - pos;
            }
        }

        return Arrays.stream(fuel).min().orElseThrow();
    }
}
