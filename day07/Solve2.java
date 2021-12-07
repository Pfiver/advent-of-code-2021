package day07;

import common.Run;

import java.util.Arrays;
import java.util.stream.LongStream;

import static common.IO.getInput;

public class Solve2 {

    public static void main(String[] args) {
        Run.attempt(Solve2::solve);
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

        long[] fuelCost = LongStream.range(0, fuel.length)
                .map(distance -> LongStream.range(0, distance + 1).sum()).toArray();

        for (int pos = minPos; pos <= maxPos; pos++) {
            int posIndex = pos - minPos;
            while (pos > subs[zeroSubIndex]) {
                zeroSubIndex++;
            }
            for (int i = 0; i < zeroSubIndex; i++) {
                fuel[posIndex] += fuelCost[pos - subs[i]];
            }
            for (int i = zeroSubIndex; i < subCount; i++) {
                fuel[posIndex] += fuelCost[subs[i] - pos];
            }
        }

        return Arrays.stream(fuel).min().orElseThrow();
    }
}
