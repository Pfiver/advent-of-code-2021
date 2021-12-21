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

    static File TOP_DIR = new File(URI.create(Run.class.getProtectionDomain().getCodeSource().getLocation().toString()));

    public static void main(String[] args) {

        if (args.length == 0) {
            IO.appendSystemOutToReadme();
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
//                .limit(2)
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

//        System.out.printf("%s***", clazz.getName());
        System.out.printf("%s", clazz.getName());

        long start = System.currentTimeMillis();

        List<?> results = Stream.of(suppliers)
                .limit(1)
//                .peek(s -> System.out.printf("\u0008\u0008\u0008%n    %s: ***", getMethod(s)))
                .map(Supplier::get)
//                .peek(x -> System.out.printf("\u0008\u0008\u0008%s ***", x))
                .distinct()
                .toList();


        if (results.size() != 1) {
            throw new IllegalStateException("two or more different methods produce different results in " + clazz);
        }

        IO.writeResult(clazz, results.get(0));

//        System.out.printf("\u0008\u0008\u0008%n    (computed in %3d milliseconds)%n%n", System.currentTimeMillis() - start);
//        System.out.printf("\u0008\u0008\u0008: %-16s (computed in %3d milliseconds)%n", results.get(0), System.currentTimeMillis() - start);
        System.out.printf(": %-16s (computed in %3d milliseconds)%n", results.get(0), System.currentTimeMillis() - start);
    }
}
