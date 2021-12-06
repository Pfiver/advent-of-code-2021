package common;

import java.util.function.BinaryOperator;

public class Common {

    public static <T> BinaryOperator<T> throwingBinOp() {
        return (l, r) -> {
            throw new UnsupportedOperationException();
        };
    }
}
