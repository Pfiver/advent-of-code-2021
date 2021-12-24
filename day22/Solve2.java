package day22;


import java.util.stream.Stream;

import static common.Common.throwingBinOp;
import static common.IO.getInput;

public class Solve2 {

    public static long solve() {
        return getInput()
                .map(Cuboid::parse)
                .reduce(Stream.of(), Cuboid::reduce, throwingBinOp())
                .filter(Cuboid::isOn)
                .mapToLong(Cuboid::volume)
                .sum();
    }
}
