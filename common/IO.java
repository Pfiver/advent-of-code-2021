package common;

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static common.Common.CheckedSupplier.tryCatch;

public class IO {

    public static Path TOP_DIR = Path.of(".");

    public static Stream<String> getInput() {
        Class<?> cc = Common.getCallerClass(2);
        return getInput(cc.getPackageName() + "/input.txt");
    }

    public static Stream<String> getInput(String path) {
        return tryCatch(() -> Files.lines(TOP_DIR.resolve(path)), Stream.empty());
    }

    static void captureSystemOut(Consumer<String> consumer) {
        System.setOut(new PrintStream(new OutputStream() {
            StringBuilder buf = new StringBuilder();
            public void write(int b) {
                if (b == '\n') {
                    consumer.accept(buf.toString());
                    buf.setLength(0);
                    return;
                }
                buf.append((char) b);
            }
        }));
    }

    static class TeeOutputStream extends OutputStream {
        final OutputStream sysout;
        final DataOutput readme;
        TeeOutputStream(OutputStream sysout, DataOutput readme) {
            this.sysout = sysout;
            this.readme = readme;
        }
        public void write(int b) throws IOException {
            sysout.write(b);
            readme.write(b);
        }
    }
}
