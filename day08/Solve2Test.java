package day08;

import java.util.Arrays;
import java.util.BitSet;
import java.util.stream.IntStream;

import static common.IO.getInput;

public class Solve2Test {

    /*

    // segment numbers

     aaaa
    b    c
    b    c
     dddd
    e    f
    e    f
     gggg

    // wiring

     dddd
    e    a
    e    a
     ffff
    g    b
    g    b
     cccc

    // then

    cdfeb: 5
    fcadb: 3
    cdfeb: 5
    cdbaf: 3

     */

    public static void main(String[] args) {

//        System.out.println("sensible 'numbers':" + 10);
//        System.out.println("possible 'numbers':" + Math.pow(2, 7));
//        System.out.println("possible configurations: " + factorial(7));

        test1();
//        System.out.println("test 1 passed\n\n");

        test2();
//        System.out.println("test 2 passed\n\n");

        test3();
//        System.out.println("test 3 passed\n\n");
    }

    private static long factorial(int num) {
        return IntStream.range(1, num+1).reduce((s, n) -> s * n).orElseThrow();
    }

    private static void test1() {
        var display = new Solve2.Display("cdfeb fcadb cdfeb cdbaf");

        Solve2.Wiring w = new Solve2.Wiring();
        w.mapping = new int[] {
                //// 0, 1, 2, 3, 4, 5, 6
                /**/ 2, 5, 6, 0, 1, 3, 4
        };

        display.wiring = w;

//        Arrays.stream(display.digits).forEach(Solve2.Display.SevenSeg::show);
//        Arrays.stream(display.digits).map(Solve2.Display.SevenSeg::display).forEach(v -> System.out.println("v: " + v));

        if (display.display() != 5353) {
            throw new AssertionError("fail");
        }
    }

    private static void test2() {
        var actual = getInput("day08/test-input.txt")
                .map(Solve2.Record::new)
//                .peek(r -> r.output.digits[0].show())
//                .peek(r -> r.output.digits[1].show())
//                .peek(r -> r.output.digits[2].show())
//                .peek(r -> r.output.digits[3].show())
//                .peek(r -> System.out.println(" => " + r.output.display()))
                .mapToInt(r -> r.output.display()).sum();

        if (actual != 61229) {
            throw new AssertionError("fail");
        }
    }

    private static void test3() {
        var r = new Solve2.Record("fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg");
//        r.output.digits[0].show();
//        r.output.digits[1].show();
//        r.output.digits[2].show();
//        r.output.digits[3].show();
        var actual = r.output.display();

        if (actual != 1197) {
            throw new AssertionError("fail");
        }
    }
}
