package common;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class IO {

    public static Stream<String> getInput() {
        return getInput(getCallerPath(2));
    }

    public static Stream<String> getInput(String path) {
        try {
            return Files.lines(Path.of(path + "/input.txt"));
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static void writeResult(int taskNumber, String result) {
        try {
            Files.writeString(Path.of(getCallerPath(3) + "/output" + taskNumber + ".txt"), result);
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static String getCallerPath(int frame) {
        return StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                .walk(frames -> frames.skip(frame)
                        .findFirst()).get().getDeclaringClass().getPackageName().replace(".", "/");
    }
}
