package common;

import java.util.Objects;

public class Test {
    public static void assertEquals(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("expected\n" + expected + "\n but actual is\n" + actual);
        }
    }
}
