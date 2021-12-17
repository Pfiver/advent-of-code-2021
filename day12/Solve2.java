package day12;

import java.util.*;
import java.util.stream.BaseStream;
import java.util.stream.Stream;

import static common.IO.getInput;
import static day12.Solve2.Cavern.Type.*;

public class Solve2 {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        System.out.println(solve());
        System.out.println(System.currentTimeMillis() - start);
    }

    public static long solve() {
        Graph caveSystem = parseGraph(getInput());
        return caveSystem.countPaths();
    }

    record Graph(Cavern start, Collection<Cavern> caverns) {
        long countPaths() {
            int cnt = 0;
            var path = new Path(start);
            while (path.advance()) {
                cnt++;
            }
            return cnt;
        }
    }

    static class Path {

        Cavern tip;
        int len = 0;
        int joker = 0;
        int[] indices = new int[1024];
        Cavern[] visited = new Cavern[1024];

        public Path(Cavern start) {
            tip = start;
        }

        boolean advance() {

            if (tip.type == END) {
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

                if (tip.type == END) {
                    return true;
                }

                push(0);
            }
        }

        boolean backtrack() {

            for (;;) {

                // leave this, remembering state
                if (joker == len) {
                    joker = 0;
                }
                int i = pop() + 1;

                // any uncharted cave left ?
                if (i < tip.links.size()) {

                    // ok - turn towards the next cave
                    push(i);

                    if (isValid()) {
                        // yay - this looks promising
                        return true;
                    }
                }
                else if (len == 0) {
                    // back to square one
                    return false;
                }
            }
        }

        void push(int i) {
            indices[len] = i;
            visited[len] = tip;
            tip = tip.links.get(i);
            len++;
        }

        private int pop() {
            len--;
            tip = visited[len];
            return indices[len];
        }

        boolean isValid() {
            return tip.type == UPPER
                    || !visited()
                    || joker == len
                    || (tip.type == LOWER && pullJoker());
        }

        boolean pullJoker() {
            if (joker == 0) {
                joker = len;
                return true;
            }
            return false;
        }

        boolean visited() {
            for (int i = 0; i < len; i++) {
                if (tip.equals(visited[i])) {
                    return true;
                }
            }
            return false;
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
}
