package common;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.LongSupplier;
import java.util.stream.Stream;

public class Run {

    static File TOP_DIR = new File(URI.create(Run.class.getProtectionDomain().getCodeSource().getLocation().toString()));

    public static void main(String[] args) throws Exception {
        Arrays.stream(Objects.requireNonNull(TOP_DIR.list()))
                .filter(n -> n.matches("day.."))
                .sorted()
                .map(n -> new File(TOP_DIR, n))
                .flatMap(d -> Arrays.stream(Objects.requireNonNull(d.list()))
                        .map(n -> new File(d, n)))
                .map(File::getPath)
                .map(p -> p.substring(TOP_DIR.getPath().length() + 1).replaceAll("\\.class$", ""))
                .filter(p -> p.matches(".*/Solve[^$]*"))
                .sorted()
                .map(p -> p.replaceAll("/", "."))
                .forEach(Run::loadAndRunMain);
    }

    private static void loadAndRunMain(String className) {
        try {
            Class.forName(className).getDeclaredMethod("main", String[].class).invoke(null, (Object) new String[0]);
        }
        catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static void attempt(LongSupplier... longSuppliers) {

        System.out.printf("%14s ***", Common.getCallerClass(2).getName());

        long start = System.currentTimeMillis();

        List<Long> results = Stream.of(longSuppliers)
//                .peek(s -> System.out.printf("%s: ", s)) // TODO: https://twitter.com/Pfiver/status/1370123672434921474
                .map(LongSupplier::getAsLong)
//                .peek(System.out::println)
                .distinct()
                .toList();

        if (results.size() != 1) {
            throw new IllegalStateException("fail");
        }

        IO.writeResult(results.get(0));
        System.out.printf("\u0008\u0008\u0008: %16d "
                + " (computed in %3d milliseconds)%n", results.get(0), System.currentTimeMillis() - start);
    }
}
