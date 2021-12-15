package day14;

public class SolveExtendedPolymerization {

    /**
     * ... Apply 10 steps of pair insertion ...
     * What do you get if you take the quantity of the most common element
     * and subtract the quantity of the least common element?
     */
    public static void main(String[] args) {

    }

    static String TEST_INPUT = """
            NNCB
                        
            CH -> B
            HH -> N
            CB -> H
            NH -> C
            HB -> C
            HC -> B
            HN -> C
            NN -> C
            BH -> H
            NC -> B
            NB -> B
            BN -> B
            BB -> N
            BC -> B
            CC -> N
            CN -> C
            """
            .trim();

    static String TEST_PARTIAL_SOLUTIONS = """
            Template:     NNCB
            After step 1: NCNBCHB
            After step 2: NBCCNBBBCBHCB
            After step 3: NBBBCNCCNBBNBNBBCHBHHBCHB
            After step 4: NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB
            """
            .trim();

    static int TEST_RESULT = 1588;
}
