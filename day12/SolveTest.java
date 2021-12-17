package day12;

import static common.Test.assertEquals;
import static day12.Solve1.*;
import static day12.Solve1TestRef.SMALL_EXAMPLE;
import static day12.Solve1TestRef.SMALL_PATHS;
import static java.util.stream.Collectors.joining;

public class SolveTest {

    public static void main(String[] args) {

        assertPaths(SMALL_PATHS, parseGraph(SMALL_EXAMPLE.lines()));

//        assertPaths(SMALL_PATHS, Solve2.parseGraph(SMALL_EXAMPLE.lines()));
        assertPaths(Solve2TestRef.SMALL_PATHS, Solve2.parseGraph(SMALL_EXAMPLE.lines()));
    }

    private static void assertPaths(String expected, Graph graph) {
        var actual = graph.findPaths().stream()
                .map(Path::toString)
                .sorted()
                .collect(joining("\n"));

        assertEquals(expected, actual);
    }

    private static void assertPaths(String expected, Solve2.Graph graph) {
        var actual = graph.countPaths();

        assertEquals(expected.lines().count(), actual);
    }
}
