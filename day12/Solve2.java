package day12;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.BaseStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static common.IO.getInput;
import static day12.Solve2.Cavern.Type.*;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Solve2 {

    public static long solve() {
        Graph caveSystem = parseGraph(getInput());
        return caveSystem.findPaths().size();
    }

    record Graph(Cavern start, Collection<Cavern> caverns) {
        List<Path> findPaths() {
            List<Path> paths = new ArrayList<>();
            for (Path p = newPath(start); p.advance(); p = newPath(p)) {
                paths.add(p);
            }
            return paths;
        }
    }

    record Path(Stack<Integer> indices, Stack<Cavern> caverns, AtomicInteger joker) {

        boolean advance() {

            if (tip().type == END) {
                if (!backtrack()) {
                    return false;
                }
            }

            for (;;) {

                if (!isValid()) {
                    if (!backtrack()) {
                        return false;
                    }
                }

                if (tip().type == END) {
                    return true;
                }

                caverns.push(tip().links.get(indices.push(0)));
            }
        }

        boolean backtrack() {

            for (;;) {

                // leave this, remembering state
                joker.compareAndSet(indices.size(), 0);
                int i = indices.pop() + 1;
                caverns.pop();

                // any uncharted cave left ?
                if (i < tip().links.size()) {

                    // ok - turn towards the next cave
                    caverns.push(tip().links.get(indices.push(i)));

                    if (isValid()) {
                        // yay - this looks promising
                        return true;
                    }
                }
                else if (indices.isEmpty()) {
                    // back to square one
                    return false;
                }
            }
        }

        boolean isValid() {
            return tip().type == UPPER
                    || joker.get() == indices.size()
                    || !caverns.subList(0, caverns.size() - 1).contains(tip())
                    || (tip().type == LOWER && joker.compareAndSet(0, indices.size()));
        }

        Cavern tip() {
            return caverns.peek();
        }

        public String toString() {
            return caverns.stream().map(c -> c.spec).collect(Collectors.joining(","));
        }
    }

    record Cavern(String spec, Type type, List<Cavern> links) {
        enum Type {
            START, END, UPPER, LOWER;
        }

        public boolean equals(Object o) {
            return o instanceof Cavern c
                    && c.spec.equals(spec);
        }

        public String toString() {
            return spec;
        }
    }


    // parsing & stuff

    static Graph parseGraph(Stream<String> input) {
        Map<String, Cavern> caverns = new HashMap<>();
        input.map(line -> {
                    int di = line.indexOf('-');
                    return Stream.of(line.substring(0, di), line.substring(di + 1));
                })
                .map(BaseStream::iterator)
                .forEach(pair -> {
                    var l = caverns.computeIfAbsent(pair.next(), Solve2::parseCavern);
                    var r = caverns.computeIfAbsent(pair.next(), Solve2::parseCavern);
                    if (!l.links.contains(r)) {
                        l.links.add(r);
                    }
                    if (!r.links.contains(l)) {
                        r.links.add(l);
                    }
                });
        Cavern start = caverns.values().stream().filter(c -> c.type == START).findFirst().orElseThrow();
        return new Graph(start, caverns.values());
    }

    static Cavern parseCavern(String spec) {
        return new Cavern(spec, getType(spec), new ArrayList<>());
    }

    static Cavern.Type getType(String spec) {
        if (spec.equals("start")) return START;
        if (spec.equals("end")) return END;
        if (Character.isUpperCase(spec.charAt(0))) return UPPER;
        return LOWER;
    }

    static Path newPath(Path prev) {
        var linkIndices = new Stack<Integer>();
        linkIndices.addAll(prev.indices);
        var caverns = new Stack<Cavern>();
        caverns.addAll(prev.caverns);
        return new Path(linkIndices, caverns, new AtomicInteger(prev.joker.get()));
    }

    static Path newPath(Cavern start) {
        var caverns = new Stack<Cavern>();
        caverns.push(start);
        return new Path(new Stack<>(), caverns, new AtomicInteger());
    }
}
