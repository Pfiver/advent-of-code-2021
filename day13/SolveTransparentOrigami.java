package day13;

import java.util.Collection;

import static common.Common.throwingBinOp;
import static common.IO.getInput;
import static java.util.stream.Collectors.toSet;

public class SolveTransparentOrigami {

    record Point(int x, int y) {}
    interface Fold { Point apply(Point p);}

    /**
     * ... fold the paper up (for horizontal y=... lines) or left (for vertical x=... lines) ...
     */
    public Collection<Point> fold(int limit) {

        return getInput()
                .dropWhile(l1 -> !l1.isEmpty())
                .skip(1)
                .limit(limit)
                .<Fold>map(l1 -> {
                    int i1 = l1.indexOf('=');
                    int d = Integer.parseInt(l1.substring(i1 + 1));
                    return l1.charAt(i1 - 1) == 'x'
                            ? p -> p.x <= d ? p : new Point(2 * d - p.x, p.y)
                            : p -> p.y <= d ? p : new Point(p.x, 2 * d - p.y);
                })
                .reduce(getInput()
                                .takeWhile(l -> !l.isEmpty())
                                .map(l -> {
                                    int i = l.indexOf(',');
                                    return new Point(
                                            Integer.parseInt(l.substring(0, i)),
                                            Integer.parseInt(l.substring(i + 1)));
                                })
                                .collect(toSet()),
                        (pts, fld) -> pts.stream().map(fld::apply).collect(toSet()), throwingBinOp()
                );
    }

    public long solve2() {

        record Point(int x, int y) {}
        interface Fold { Point apply(Point p);}

        return getInput()
                .dropWhile(l1 -> !l1.isEmpty())
                .skip(1)
                .limit(1)
                .<Fold>map(l1 -> {
                    int i1 = l1.indexOf('=');
                    int d = Integer.parseInt(l1.substring(i1 + 1));
                    return l1.charAt(i1 - 1) == 'x'
                            ? p -> p.x < d ? p : new Point(2 * d - p.x, p.y)
                            : p -> p.y < d ? p : new Point(p.x, 2 * d - p.y);
                })
                .reduce(getInput()
                                .takeWhile(l -> !l.isEmpty())
                                .map(l -> {
                                    int i = l.indexOf(',');
                                    return new Point(
                                            Integer.parseInt(l.substring(0, i)),
                                            Integer.parseInt(l.substring(i + 1)));
                                })
                                .collect(toSet()),
                        (pts, fld) -> pts.stream().map(fld::apply).collect(toSet()), throwingBinOp()
                ).size();
    }
}
