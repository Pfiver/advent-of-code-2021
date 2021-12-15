package day13;

public class SolveTransparentOrigami {

    /**
     * ... fold the paper up (for horizontal y=... lines) or left (for vertical x=... lines) ...
     */
    public static void main(String[] args) {


    }

    static String TEST_INPUT = """
            6,10
            0,14
            9,10
            0,3
            10,4
            4,11
            6,0
            6,12
            4,1
            0,13
            10,12
            3,4
            3,0
            8,4
            1,10
            2,14
            8,10
            9,0
                        
            fold along y=7
            fold along x=5
            """
            .trim();

    static int TEST_RESULT = 17;

    static String TEST_RESULT_REPR = """
            #####
            #...#
            #...#
            #...#
            #####
            .....
            .....
            """
            .trim();
}
