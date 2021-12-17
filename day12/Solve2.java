package day12;

import java.util.*;
import java.util.stream.BaseStream;
import java.util.stream.Stream;

import static common.IO.getInput;
import static day12.Solve2.Cavern.Type.*;

public class Solve2 {

    public static long solve() {
        Graph caveSystem = parseGraph(getInput());
        return caveSystem.countPaths();
    }

    record Graph(Cavern start, Collection<Cavern> caverns) {
        long countPaths() {
            return new PathFinder(start).countPaths();
        }
    }

    static class PathFinder {

        Cavern tip;
        int len = 0;
        int joker = 0;
        int[] indices = new int[1024];
        Cavern[] visited = new Cavern[1024];

        public PathFinder(Cavern start) {
            tip = start;
        }

        long countPaths() {

            int i = 0;
            long cnt = 0;

            do {
                do {
                    if (tip.type == END) {
                        cnt += 1;
                        break;
                    }
                    else {
                        push(i);
                        i = 0;
                    }
                }
                while (tip.type == UPPER || fresh() || joker());

                do {
                    if (len == 0) {
                        return cnt;
                    }
                    i = pop() + 1;
                }
                while (i == tip.links.size());

            } while (true);
        }

        void push(int i) {
            indices[len] = i;
            visited[len] = tip;
            tip = tip.links.get(i);
            len++;
        }

        private int pop() {
            if (joker == len) {
                joker = 0;
            }
            len--;
            tip = visited[len];
            return indices[len];
        }

        boolean fresh() {
            for (int i = 0; i < len; i++) {
                if (tip.equals(visited[i])) {
                    return false;
                }
            }
            return true;
        }

        boolean joker() {
            if (joker == 0) {
                joker = len;
                return true;
            }
            return joker == len;
        }

        public String toString() {
            String repr = "";
            for (int i = 0; i < len; i++) {
                repr += visited[i].spec + ",";
            }
            repr += tip.spec;
            return repr;
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
                    if (r.type != START && !l.links.contains(r)) {
                        l.links.add(r);
                    }
                    if (l.type != START && !r.links.contains(l)) {
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
}
