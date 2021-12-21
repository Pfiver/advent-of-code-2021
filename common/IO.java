package common;

import common.Common.CheckedRunnable;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static common.Common.CheckedSupplier.tryCatch;
import static common.Common.CheckedSupplier.tryGet;

public class IO {

    public static Path TOP_DIR = Path.of(".");

    public static Stream<String> getInput() {
        Class<?> cc = Common.getCallerClass(2);
        return getInput(cc.getPackageName() + "/input.txt");
    }

    public static Stream<String> getInput(String path) {
        return tryCatch(() -> Files.lines(TOP_DIR.resolve(path)), Stream.empty());
    }

    public static void writeResult(Class<?> clazz, Object result) {
        writeResult(parseInt(clazz.getPackageName()), parseInt(clazz.getSimpleName()), result);
    }

    static void writeResult(int day, int task, Object result) {
        var outFile = "day%02d/output%d.txt".formatted(day, task);
        tryGet(() -> Files.writeString(TOP_DIR.resolve(outFile), Objects.toString(result)));
    }

    static void appendSystemOutToReadme() {

        System.setOut(new PrintStream(new TeeOutputStream(System.out, tryGet(() -> {
            var rac = new RandomAccessFile(TOP_DIR.resolve("README.md").toFile(), "rw");
            rac.seek(162);
            Runtime.getRuntime().addShutdownHook(new Thread((CheckedRunnable) () -> {
                rac.setLength(rac.getFilePointer());
                rac.close();
            }));
            return rac;
        })), true));
    }

    static Pattern DIGITS = Pattern.compile("[0-9]+");

    private static int parseInt(String name) {
        return DIGITS.matcher(name).results().findFirst()
                .map(MatchResult::group)
                .map(Integer::parseInt)
                .orElse(0);
    }

    static class TeeOutputStream extends OutputStream {
        boolean nl = true;
        final OutputStream sysout;
        final RandomAccessFile readme;
        TeeOutputStream(OutputStream sysout, RandomAccessFile readme) {
            this.sysout = sysout;
            this.readme = readme;
        }
        public void write(int b) throws IOException {
            sysout.write(b);
            if (nl) { nl = false; readme.write("    ".getBytes()); }
            if (b == '\n') { nl = true; }
            readme.write(b);
        }
    }
}
