package day22;

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Long.MAX_VALUE;
import static java.lang.Long.MIN_VALUE;

record Cuboid(Command cmd, Range x, Range y, Range z) {

    public static Stream<Cuboid> reduce(Stream<Cuboid> result, Cuboid c) {
        return Stream.concat(result.flatMap(r -> r.remove(c)), Stream.of(c));
    }

    public Stream<Cuboid> remove(Cuboid c) {

        if (       c.x.hi < x.lo || c.x.lo > x.hi
                || c.y.hi < y.lo || c.y.lo > y.hi
                || c.z.hi < z.lo || c.z.lo > z.hi) {
            return Stream.of(this);
        }

        return Stream.of(
                          new Cuboid(null, new Range(MIN_VALUE, c.x.lo-1), new Range(MIN_VALUE, MAX_VALUE), new Range(MIN_VALUE, MAX_VALUE))
                        , new Cuboid(null, new Range(c.x.hi+1, MAX_VALUE), new Range(MIN_VALUE, MAX_VALUE), new Range(MIN_VALUE, MAX_VALUE))
                        , new Cuboid(null, new Range(c.x.lo, c.x.hi), new Range(MIN_VALUE, c.y.lo-1), new Range(MIN_VALUE, MAX_VALUE))
                        , new Cuboid(null, new Range(c.x.lo, c.x.hi), new Range(c.y.hi+1, MAX_VALUE), new Range(MIN_VALUE, MAX_VALUE))
                        , new Cuboid(null, new Range(c.x.lo, c.x.hi), new Range(c.y.lo, c.y.hi), new Range(MIN_VALUE, c.z.lo-1))
                        , new Cuboid(null, new Range(c.x.lo, c.x.hi), new Range(c.y.lo, c.y.hi), new Range(c.z.hi+1, MAX_VALUE))
                )
                .map(this::clip)
                .flatMap(Optional::stream)
                .filter(Cuboid::hasVolume)
                ;
    }

    Optional<Cuboid> clip(Cuboid clip) {

        if (       clip.x.hi < x.lo || clip.x.lo > x.hi
                || clip.y.lo > y.hi || clip.y.hi < y.lo
                || clip.z.lo > z.hi || clip.z.hi < z.lo) {
            return Optional.empty();
        }

        // noinspection ManualMinMaxCalculation
        return Optional.of(new Cuboid(cmd,
                new Range(clip.x.lo >= x.lo ? clip.x.lo : x.lo, clip.x.hi <= x.hi ? clip.x.hi : x.hi),
                new Range(clip.y.lo >= y.lo ? clip.y.lo : y.lo, clip.y.hi <= y.hi ? clip.y.hi : y.hi),
                new Range(clip.z.lo >= z.lo ? clip.z.lo : z.lo, clip.z.hi <= z.hi ? clip.z.hi : z.hi)));
    }

    long volume() {
        return (x.hi - x.lo + 1) * (y.hi - y.lo + 1) * (z.hi - z.lo + 1);
    }

    boolean hasVolume() {
        return volume() > 0;
    }

    boolean isOn() {
        return cmd == Cuboid.Command.on;
    }

    enum Command { on, off }
    record Range(long lo, long hi) {}

    static Cuboid parse(String line) {

        var cm = commandPattern.matcher(line);
        if (!cm.find()) {
            throw new IllegalStateException();
        }
        var cmd = Command.valueOf(cm.group(1));

        var rm = rangePattern.matcher(line);
        if (!rm.find()) {
            throw new IllegalStateException();
        }
        var x = new Range(Integer.parseInt(rm.group(1)), Integer.parseInt(rm.group(2)));

        if (!rm.find()) {
            throw new IllegalStateException();
        }
        var y = new Range(Integer.parseInt(rm.group(1)), Integer.parseInt(rm.group(2)));

        if (!rm.find()) {
            throw new IllegalStateException();
        }
        var z = new Range(Integer.parseInt(rm.group(1)), Integer.parseInt(rm.group(2)));

        return new Cuboid(cmd, x, y, z);
    }

    static Pattern commandPattern = Pattern.compile("(on|off|clip) ");
    static Pattern rangePattern = Pattern.compile("[xyz]=(-?\\d+)..(-?\\d+)");

    public static void main(String[] args) {
        var matcher = commandPattern.matcher("on x=-15..36,y=-36..8,z=-12..33");
        System.out.println(matcher.find());
        System.out.println(matcher.group(1));
        matcher = rangePattern.matcher("on x=-15..36,y=-36..8,z=-12..33");
        System.out.println(matcher.find());
        System.out.println(matcher.groupCount());
        System.out.println(matcher.group(1));
        System.out.println(matcher.group(2));
    }
}
