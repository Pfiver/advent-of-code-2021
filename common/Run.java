package common;

import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.lang.invoke.MethodType;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.function.Consumer;

public class Run {

    public static void main(String[] args) throws Exception {
        run(
                new Solution(day1.SonarSweep.class, day1.SonarSweep::main)
                , new Solution(day2.Dive.class, day2.Dive::main)
                , new Solution(day3.BinaryDiagnostic.class, day3.BinaryDiagnostic::main)
                , new Solution(day4.GiantSquid.class, day4.GiantSquid::main)
                , new Solution(day5.HydrothermalVenture.class, day5.HydrothermalVenture::main)
                , new Solution(day6.Lanternfish.class, day6.Lanternfish::main)
                , new Solution(day7.TheTreacheryofWhales.class, day7.TheTreacheryofWhales::main)
                , new Solution(day8.SevenSegmentSearch.class, day8.SevenSegmentSearch::main)
                , new Solution(day9.SmokeBasin.class, day9.SmokeBasin::main)
                , new Solution(day10.SyntaxScoring.class, day10.SyntaxScoring::main)
                , new Solution(day11.DumboOctopus.class, day11.DumboOctopus::main)
                , new Solution(day12.PassagePathing.class, day12.PassagePathing::main)
                , new Solution(day13.TransparentOrigami.class, day13.TransparentOrigami::main)
                , new Solution(day14.ExtendedPolymerization.class, day14.ExtendedPolymerization::main)
                , new Solution(day15.Chiton.class, day15.Chiton::main)
                , new Solution(day16.PacketDecoder.class, day16.PacketDecoder::main)
                , new Solution(day17.TrickShot.class, day17.TrickShot::main)
                , new Solution(day18.Snailfish.class, day18.Snailfish::main)
                , new Solution(day19.BeaconScanner.class, day19.BeaconScanner::main)
                , new Solution(day20.TrenchMap.class, day20.TrenchMap::main)
                , new Solution(day21.DiracDice.class, day21.DiracDice::main)
                , new Solution(day22.ReactorReboot.class, day22.ReactorReboot::main)
                , new Solution(day23.Amphipod.class, day23.Amphipod::main)
                , new Solution(day24.ArithmeticLogicUnit.class, day24.ArithmeticLogicUnit::main)
                , new Solution(day25.SeaCucumber.class, day25.SeaCucumber::main)
        );
    }

    public record Solution(Class<?> clazz, Runnable main) {}
    public static void run(Solution... solutions) throws Exception {

        var readme = new RandomAccessFile(IO.TOP_DIR.resolve("README.md").toFile(), "rw");
        readme.seek(162);
        Runtime.getRuntime().addShutdownHook(new Thread((Common.CheckedRunnable) () -> {
            readme.setLength(readme.getFilePointer());
            readme.close();
        }));

        var sysOut = new PrintStream(new IO.TeeOutputStream(System.out, readme));
        var oc = new Consumer<String>() {
            int part;
            long start;
            String heading;
            public void accept(String result) {
                var heading = this.heading + "#" + part++;
                long runtime = System.currentTimeMillis() - start;
                sysOut.printf("    %-30s: %-16s (computed in %3d milliseconds)%n", heading, result, runtime);
            }
        };
        IO.captureSystemOut(oc);

        sysOut.println();
        long start = System.currentTimeMillis();
        for (Solution solution : solutions) {
            oc.part = 1;
            oc.start = System.currentTimeMillis();
            oc.heading = solution.clazz.getName();
            solution.main.run();
        }
        sysOut.println();
        sysOut.printf("    Total running time: %d milliseconds%n", System.currentTimeMillis() - start);
    }
}
