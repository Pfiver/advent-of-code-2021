package day12;

import java.util.*;
import java.util.stream.BaseStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static common.IO.getInput;
import static day12.Solve1.Cavern.Type.*;

public class Solve1 {

    public static long solve() {
        Graph caveSystem = parseGraph(getInput());
        return caveSystem.findPaths().size();
    }

    record Graph(Cavern start, Collection<Cavern> caverns) {
        List<Path> findPaths() {
            List<Path> paths = new ArrayList<>();
            Path p = newPath(start);
            while (p.extend(paths) || p.backtrack()) {
                if (p.tip().type == END) {
                    paths.add(p);
                    p = newPath(p);
                }
            }
            return paths;
        }
    }

    record Path(Stack<Integer> linkIndices, Stack<Cavern> caverns) {
        Cavern tip() {
            return caverns.peek();
        }

        boolean extend(Collection<Path> existing) {
            if (tip().type == END) {
                return false;
            }
            caverns.push(caverns.peek().links.get(linkIndices.push(0)));
            do {
                if (isValid() && isNew(existing)) {
                    return true;
                }
            } while (nextLink());
            linkIndices.pop();
            caverns.pop();
            return false;
        }

        boolean backtrack() {
            for (; linkIndices.size() > 0; linkIndices.pop(), caverns.pop()) {
                while (nextLink()) {
                    if (isValid()) {
                        return true;
                    }
                }
            }
            return false;
        }

        boolean nextLink() {
            Cavern previousCavern = caverns.get(caverns.size() - 2);
            if (linkIndices.peek() + 1 < previousCavern.links.size()) {
                var i = linkIndices.pop() + 1;
                caverns.pop();
                linkIndices.push(i);
                caverns.push(previousCavern.links.get(i));
                return true;
            }
            return false;
        }

        boolean isValid() {
            return tip().type == UPPER || !caverns.subList(0, caverns.size()-1).contains(tip());
        }

        public boolean isNew(Collection<Path> paths) {
            return true;
//            return paths.stream().map(Path::caverns)
//                    .flatMap(Collection::stream)
//                    .filter(c -> c.type != UPPER)
//                    .filter(tip()::equals)
//                    .findAny()
//                    .isEmpty();
        }

        public String toString() {
            return caverns.stream().map(c -> c.spec).collect(Collectors.joining(","));
        }
    }

    record Cavern(String spec, Type type, List<Cavern> links) {
        enum Type {
            START, END, UPPER, LOWER;
        }
        public String toString() {
            return spec;
        }
        public boolean equals(Object o) {
            return o instanceof Cavern c && c.spec.equals(spec);
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
                    var l = caverns.computeIfAbsent(pair.next(), Solve1::parseCavern);
                    var r = caverns.computeIfAbsent(pair.next(), Solve1::parseCavern);
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

    static Path newPath(Path perev) {
        var linkIndices = new Stack<Integer>();
        linkIndices.addAll(perev.linkIndices);
        var caverns = new Stack<Cavern>();
        caverns.addAll(perev.caverns);
        return new Path(linkIndices, caverns);
    }

    static Path newPath(Cavern start) {
        var caverns = new Stack<Cavern>();
        caverns.push(start);
        return new Path(new Stack<>(), caverns);
    }
}
