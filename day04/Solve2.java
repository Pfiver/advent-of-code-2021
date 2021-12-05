package day04;

import common.Run;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static common.IO.getInput;

public class Solve2 extends Solve1 {

    public static void main(String[] args) {
        Run.attempt(Solve2::solve);
    }

    static long solve() {

        Iterator<String> lines = getInput().iterator();
        int[] numbers = getNumbers(lines.next());
        lines.next();
        List<Board> boards = getBoards(lines);
        long[] scores = new long[boards.size()];
        Arrays.fill(scores, -1);
        long loScore = -1;
        for (int number : numbers) {
            for (int i = 0, boardsSize = boards.size(); i < boardsSize; i++) {
                if (scores[i] < 0) {
                    Board board = boards.get(i);
                    long score = board.mark(number);
                    if (scores[i] != score) {
                        scores[i] = score;
                        loScore = score;
                    }
                }
            }
        }
        return loScore;
    }
}
