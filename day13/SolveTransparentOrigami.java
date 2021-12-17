package day13;

import static java.util.stream.Collectors.toSet;

public class SolveTransparentOrigami {

    /**
     * ... fold the paper up (for horizontal y=... lines) or left (for vertical x=... lines) ...
     */
    public static void main(String[] args) {

        record Point(int x, int y) {}
        interface Fold { Point apply(Point p);}

        var points = TEST_INPUT.lines()
                .dropWhile(l1 -> !l1.isEmpty())
                .skip(1)
                .limit(1)
                .<Fold>map(l1 -> {
                    int i1 = l1.indexOf('=');
                    int d = Integer.parseInt(l1.substring(i1 + 1));
                    return l1.charAt(i1 - 1) == 'x'
                            ? p -> p.y < d ? p : new Point(p.x, 2 * d - p.y)
                            : p -> p.x < d ? p : new Point(2 * d - p.x, p.y);
                })
                .reduce(TEST_INPUT.lines()
                                .takeWhile(l -> !l.isEmpty())
                                .map(l -> {
                                    int i = l.indexOf(',');
                                    return new Point(
                                            Integer.parseInt(l.substring(0, i)),
                                            Integer.parseInt(l.substring(i + 1)));
                                })
                                .collect(toSet()),
                        (pts, fld) -> pts.stream().map(fld::apply).collect(toSet()),
                        (l, r) -> {
                            throw new UnsupportedOperationException();
                        }
                );

        System.out.println(points.size());
    }

    static String TEST_INPUT = """
            6,10
            0,14
            9,10
            0,3
            10,4
            4,11
            6,0
            6,12
            4,1
            0,13
            10,12
            3,4
            3,0
            8,4
            1,10
            2,14
            8,10
            9,0
                        
            fold along y=7
            fold along x=5
            """
            .trim();

    static int TEST_RESULT = 17;

    static String TEST_RESULT_REPR = """
            #####
            #...#
            #...#
            #...#
            #####
            .....
            .....
            """
            .trim();
}
