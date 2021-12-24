package common;

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static common.Common.CheckedSupplier.tryCatch;
import static common.Common.CheckedSupplier.tryGet;

public class Run {

    public static boolean SINGLE_RUN = false;
    static File TOP_DIR = new File(URI.create(Run.class.getProtectionDomain().getCodeSource().getLocation().toString()));

    public static void main(String[] args) {

        if (args.length == 0) {
            IO.appendSystemOutToReadme();
        }
        else {
            SINGLE_RUN = true;
        }

        System.out.println();
        long start = System.currentTimeMillis();
        Arrays.stream(Objects.requireNonNull(TOP_DIR.list()))
                .filter(n -> n.matches("day.."))
                .sorted()
                .map(n -> new File(TOP_DIR, n))
                .flatMap(d -> Arrays.stream(Objects.requireNonNull(d.list()))
                        .map(n -> new File(d, n)))
                .map(File::getPath)
                .map(p -> p.substring(TOP_DIR.getPath().length() + 1).replaceAll("\\.class$", ""))
                .sorted()
                .map(p -> p.replace("/", "."))
                .filter(c -> args.length == 0 ? c.matches(".*\\.Solve\\d*") : c.equals(args[0]))
                .map(className -> tryGet(() -> Class.forName(className)))
                .forEach(clazz -> tryCatch(
                        () -> clazz.getDeclaredMethod("solve"),
                        solve -> run(clazz, (Common.CheckedSupplier<?>) () -> solve.invoke(null)),
                        () -> clazz.getDeclaredMethod("main", String[].class).invoke(null, (Object) new String[0])));
        System.out.printf("%nTotal running time: %d milliseconds%n", System.currentTimeMillis() - start);
    }

    public static void run(Supplier<?>... suppliers) {
        run(Common.getCallerClass(2), suppliers);
    }

    private static void run(Class<?> clazz, Supplier<?>... suppliers) {

        if (!SINGLE_RUN) {
            System.out.printf("%s", clazz.getName());
        }

        long start = System.currentTimeMillis();

        List<?> results = Stream.of(suppliers)
                .limit(1)
                .map(Supplier::get)
                .distinct()
                .toList();


        if (results.size() != 1) {
            throw new IllegalStateException("two or more different methods produce different results in " + clazz);
        }

        IO.writeResult(clazz, results.get(0));

        if (SINGLE_RUN) {
            System.out.println("\nResult: " + results.get(0));
        }
        else {
            System.out.printf(": %-16s (computed in %3d milliseconds)%n", results.get(0), System.currentTimeMillis() - start);
        }
    }
}
