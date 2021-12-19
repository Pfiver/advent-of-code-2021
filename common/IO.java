package common;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class IO {

    public static Path TOP_DIR = Path.of(".");

    public static Stream<String> getInput() {
        Class<?> cc = Common.getCallerClass(2);
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

    public static void writeResult(Class<?> clazz, Object result) {
        writeResult(parseInt(clazz.getPackageName()), parseInt(clazz.getSimpleName()), result);
    }

    static void writeResult(int day, int task, Object result) {
        try {
            Files.writeString(TOP_DIR.resolve("day%02d/output%d.txt".formatted(day, task)), Objects.toString(result));
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static Pattern DIGITS = Pattern.compile("[0-9]+");

    private static int parseInt(String name) {
        return DIGITS.matcher(name).results().findFirst()
                .map(MatchResult::group)
                .map(Integer::parseInt)
                .orElse(0);
    }
}
