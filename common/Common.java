package common;

import java.util.function.BinaryOperator;

public class Common {

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
