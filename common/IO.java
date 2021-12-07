package common;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class IO {

    public static Path TOP_DIR = Path.of(".");

    public static Stream<String> getInput() {
        Class<?> cc = getCallerClass(2);
        return getInput(cc.getPackageName() + "/input.txt");
    }

    public static Stream<String> getInput(String path) {
        try {
            return Files.lines(TOP_DIR.resolve(path));
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void writeResult(String result) {
        Class<?> cc = getCallerClass(3);
        writeResult(parseInt(cc.getPackageName()), parseInt(cc.getSimpleName()), result);
    }

    static void writeResult(int day, int task, String result) {
        try {
            Files.writeString(TOP_DIR.resolve("day%02d/output%d.txt".formatted(day, task)), result);
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static Pattern DIGITS = Pattern.compile("[0-9]+");

    private static int parseInt(String name) {
        return Integer.parseInt(
                DIGITS.matcher(name).results()
                        .findFirst().orElseThrow().group());
    }

    private static Class<?> getCallerClass(int nFrames) {
        return StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                .walk(frames -> frames.skip(nFrames).findFirst()).get().getDeclaringClass();
    }
}
