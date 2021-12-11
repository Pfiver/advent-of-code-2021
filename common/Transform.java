package common;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

public class Transform {

    public static byte[][] decimalDigitsToPaddedValuesRectangle(Stream<String> input, int width, byte padVal) {
        Function<String, byte[]> lineMapper = line -> {
            var i = 1;
            var vals = new byte[width + 2];
            for (char c : line.toCharArray()) {
                vals[i++] = (byte) Character.getNumericValue(c);
            }
            vals[0] = vals[width + 1] = padVal;
            return vals;
        };
        byte[] padLine = new byte[width + 2];
        Arrays.fill(padLine, padVal);
        return Stream.concat(Stream.concat(Stream.of(padLine), input.map(lineMapper)), Stream.of(padLine)).toArray(byte[][]::new);
    }

    public static byte[] flatten(byte[][] src) {
        int w = src[0].length;
        int h = src.length;
        var ret = new byte[w * h];
        for (int y = 0; y < h; y++) {
            System.arraycopy(src[y], 0, ret, w * y, w);
        }
        return ret;
    }
}
