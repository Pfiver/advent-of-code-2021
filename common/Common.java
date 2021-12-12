package common;

import java.util.function.BinaryOperator;
import java.util.function.LongSupplier;

public class Common {

    public interface CheckedRunnable {
        void tryRun() throws Exception;
        static void tryRun(CheckedRunnable r) {
            try {r.tryRun();}
            catch (Exception e) {throw new RuntimeException(e);}
        }
    }

    interface CheckedSupplier<T> {
        T tryGet() throws Exception;
        static <T> T tryGet(CheckedSupplier<T> c) {
            try {return c.tryGet();}
            catch (Exception e) {throw new RuntimeException(e);}
        }
        static <T> void tryCatch(CheckedSupplier<T> s, CheckedConsumer<T> pass, CheckedRunnable fail) {
            try { pass.tryAccept(s.tryGet()); }
            catch (Exception e) { CheckedRunnable.tryRun(fail); }
        }
    }

    interface CheckedLongSupplier extends LongSupplier {
        long tryGetAsLong() throws Exception;
        default long getAsLong() {
            try {return tryGetAsLong();}
            catch (Exception e) {throw new RuntimeException(e);}
        }
    }

    interface CheckedConsumer<T> {
        void tryAccept(T v) throws Exception;
    }

    public static <T> BinaryOperator<T> throwingBinOp() {
        return (l, r) -> {
            throw new UnsupportedOperationException();
        };
    }

    static Class<?> getCallerClass(int nFrames) {
        return StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                .walk(frames -> frames.skip(nFrames).findFirst()).get().getDeclaringClass();
    }
}
