package common;

import java.util.function.BinaryOperator;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

public class Common {

    public interface CheckedRunnable extends Runnable {
        void tryRun() throws Exception;
        default void run() {
            tryRun(this);
        }
        static void tryRun(CheckedRunnable r) {
            try {r.tryRun();}
            catch (Exception e) {throw new RuntimeException(e);}
        }
    }

    public interface CheckedSupplier<T> extends Supplier<T> {
        T tryGet() throws Exception;
        default T get() {
            return tryGet(this);
        }
        static <T> T tryGet(CheckedSupplier<T> c) {
            try {return c.tryGet();}
            catch (Exception e) {throw new RuntimeException(e);}
        }
        static <T> T tryCatch(CheckedSupplier<T> s, T fail) {
            try { return s.tryGet(); }
            catch (Exception e) { return fail; }
        }
        static <T> void tryCatch(CheckedSupplier<T> s, CheckedConsumer<T> pass, CheckedRunnable fail) {
            T v;
            try { v = s.tryGet(); }
            catch (Exception e) { CheckedRunnable.tryRun(fail); return; }
            pass.tryAccept(v);
        }
    }

    public interface CheckedConsumer<T> {
        void accept(T v) throws Exception;
        default void tryAccept(T v) {
            try { accept(v); }
            catch (Exception e) { throw new RuntimeException(e); }
        }
    }

    public static <T> BinaryOperator<T> throwingBinOp() {
        return (l, r) -> {throw new UnsupportedOperationException();};
    }

    static Class<?> getCallerClass(int nFrames) {
        return StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                .walk(frames -> frames.skip(nFrames).findFirst()).get().getDeclaringClass();
    }
}
