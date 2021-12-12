package common;

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.LongSupplier;
import java.util.stream.Stream;

import static common.Common.CheckedSupplier.tryCatch;
import static common.Common.CheckedSupplier.tryGet;

public class Run {

    static File TOP_DIR = new File(URI.create(Run.class.getProtectionDomain().getCodeSource().getLocation().toString()));

    public static void main(String[] args) {

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
                .filter(p -> p.matches(".*/Solve\\d*"))
                .sorted()
//                .limit(2)
                .map(p -> p.replace("/", "."))
                .filter(c -> args.length == 0 || c.equals(args[0]))
                .map(className -> tryGet(() -> Class.forName(className)))
                .forEach(clazz -> tryCatch(
                        () -> clazz.getDeclaredMethod("solve"),
                        solve -> run(clazz, (Common.CheckedLongSupplier) () -> (long) solve.invoke(null)),
                        () -> clazz.getDeclaredMethod("main", String[].class).invoke(null, (Object) new String[0])));
        System.out.printf("%nTotal running time: %d milliseconds%n", System.currentTimeMillis() - start);
    }

    public static void run(LongSupplier... longSuppliers) {
        run(Common.getCallerClass(2), longSuppliers);
    }

    private static void run(Class<?> clazz, LongSupplier... longSuppliers) {

        System.out.printf("%s***", clazz.getName());

        long start = System.currentTimeMillis();

        List<Long> results = Stream.of(longSuppliers)
                .limit(1)
//                .peek(s -> System.out.printf("\u0008\u0008\u0008%n    %s: ***", getMethod(s)))
                .map(LongSupplier::getAsLong)
//                .peek(x -> System.out.printf("\u0008\u0008\u0008%s ***", x))
                .distinct()
                .toList();


        if (results.size() != 1) {
            throw new IllegalStateException("two or more different methods produce different results in " + clazz);
        }

        IO.writeResult(clazz, results.get(0));

//        System.out.printf("\u0008\u0008\u0008%n    (computed in %3d milliseconds)%n%n", System.currentTimeMillis() - start);
        System.out.printf("\u0008\u0008\u0008: %16d (computed in %3d milliseconds)%n", results.get(0), System.currentTimeMillis() - start);
    }
}
