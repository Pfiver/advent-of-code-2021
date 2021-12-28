package day4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static common.IO.getInput;

public class Solve1 {

    public static long solve() {

        Iterator<String> lines = getInput().iterator();
        int[] numbers = getNumbers(lines.next());
        lines.next();
        List<Board> boards = getBoards(lines);
        for (int number : numbers) {
            for (Board board : boards) {
                long score = board.mark(number);
                if (score < 0) {
                    continue;
                }
                return score;
            }
        }
        return -1;
    }

    static int[] getNumbers(String next) {
        return Arrays.stream(next.split(",")).mapToInt(Integer::parseInt).toArray();
    }

    static List<Board> getBoards(Iterator<String> lines) {
        var boards = new ArrayList<Board>();
        while (lines.hasNext()) {
            boards.add(getBoard(lines));
        }
        return boards;
    }

    static Board getBoard(Iterator<String> lines) {
        Board board = new Board();
        while (lines.hasNext()) {
            String line = lines.next();
            if (line.isBlank()) {
                break;
            }
            board.addLine(line);
        }
        return board;
    }

    static class Board {
        int rows = 0;
        int[] numbers = new int[25];
        boolean[] seen = new boolean[25];

        void addLine(String line) {
            System.arraycopy(
                    Arrays.stream(line.trim().split(" +")).mapToInt(Integer::parseInt).toArray(),
                    0,
                    numbers,
                    rows++ * 5,
                    5
            );
        }

        long mark(int number) {
            // mark
            for (int i = 0; i < 25; i++) {
                if (numbers[i] == number) {
                    seen[i] = true;
                    break;
                }
            }
            // check
            boolean won = false;
            for (int i = 0; i < 25; i+=5) {
                boolean allSeen = true;
                for (int j = i; j < i+5; j++) {
                    allSeen &= seen[j];
                }
                won = allSeen;
                if (won) {
                    break;
                }
            }
            if (!won) {
                for (int i = 0; i < 5; i++) {
                    boolean allSeen = true;
                    for (int j = i; j < 25; j+=5) {
                        allSeen &= seen[j];
                    }
                    won = allSeen;
                    if (won) {
                        break;
                    }
                }
            }
            if (!won) {
                return -1;
            }
            long score = 0;
            for (int i = 0; i < 25; i++) {
                if (!seen[i]) {
                    score += numbers[i];
                }
            }
            score *= number;
            return score;
        }
    }
}
