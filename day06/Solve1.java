package day06;

import java.util.Arrays;

import static common.IO.getInput;

public class Solve1 {

    public static long solve() {
        return solve(80);
    }

    static long solve(int days) {

        var input = getInput().findFirst().orElseThrow().split(",");
        var exponentiallyIncreasingFishTimers = Arrays.stream(input).mapToInt(Integer::parseInt).toArray();
        var fishTimerFrequencies = new long[9];
        for (int ft : exponentiallyIncreasingFishTimers) {
            fishTimerFrequencies[ft]++;
        }

        for (int day = 0; day < days; day++) {
            long timeouts = fishTimerFrequencies[0];
            System.arraycopy(fishTimerFrequencies, 1, fishTimerFrequencies, 0, 8);
            fishTimerFrequencies[6] += timeouts;
            fishTimerFrequencies[8] = timeouts;
        }

        return Arrays.stream(fishTimerFrequencies).sum();
    }
}
