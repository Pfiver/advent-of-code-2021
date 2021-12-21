// blatantly copied from a person called Kleber who posted this on reddit -- labelled "scuffed Dijkstra" -- damn fast

// https://www.reddit.com/r/adventofcode/comments/rgqzt5/comment/hotsy26/
// https://github.com/KleberPF/advent-of-code-2021/blob/main/15.c

// to run: $ llvm-gcc -o solve solve.c && ./solve < input.txt

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

#define SIZE 100

int (*risks)[5*SIZE];
int (*distances)[5*SIZE];

bool minDistance(int i, int j, bool part2)
{
    int len = part2 ? 5*SIZE : SIZE;
    bool result = false;
    if (i - 1 >= 0) {
        int newValue = distances[i][j] + risks[i - 1][j];
        if (newValue < distances[i - 1][j]) {
            distances[i - 1][j] = newValue;
            result = true;
        }
    }
    if (i + 1 < len) {
        int newValue = distances[i][j] + risks[i + 1][j];
        if (newValue < distances[i + 1][j]) {
            distances[i + 1][j] = newValue;
            result = true;
        }
    }
    if (j - 1 >= 0) {
        int newValue = distances[i][j] + risks[i][j - 1];
        if (newValue < distances[i][j - 1]) {
            distances[i][j - 1] = newValue;
            result = true;
        }
    }
    if (j + 1 < len) {
        int newValue = distances[i][j] + risks[i][j + 1];
        if (newValue < distances[i][j + 1]) {
            distances[i][j + 1] = newValue;
            result = true;
        }
    }

    return result;
}

int findMinDistance(bool part2)
{
    for (int i = 0; i < 5*SIZE; ++i) {
        for (int j = 0; j < 5*SIZE; ++j) {
            distances[i][j] = INT32_MAX;
        }
    }
    distances[0][0] = 0;

    bool stop = false;
    int len = part2 ? 5*SIZE : SIZE;
    while (!stop) {
        stop = true;
        for (int i = 0; i < len; ++i) {
            for (int j = 0; j < len; ++j) {
                if (minDistance(i, j, part2)) {
                    stop = false;
                }
            }
        }
    }

    return distances[len - 1][len - 1];
}

int main()
{
    risks = malloc(sizeof(int[5*SIZE][5*SIZE]));
    distances = malloc(sizeof(int[5*SIZE][5*SIZE]));

    for (int i = 0; i < SIZE; ++i) {
        for (int j = 0; j < SIZE; ++j) {
            int c = fgetc(stdin);
            risks[i][j] = c - '0';
        }
        fgetc(stdin);
    }

    printf("Part 1: %d\n", findMinDistance(false));

    // populate 5x5
    int j = 0;
    for (int i = 0; i < 5; ++i) {
        if (i != 0) {
            for (int k = 0; k < SIZE; ++k) {
                memcpy(&risks[SIZE * i + k][0], &risks[SIZE * (i - 1) + k][SIZE],
                       SIZE * 4 * sizeof(int));
            }
            j = 3;
        }
        for (; j < 4; ++j) {
            for (int k = 0; k < SIZE; ++k) {
                for (int l = 0; l < SIZE; ++l) {
                    int value = risks[SIZE * i + k][SIZE * j + l];
                    if (value + 1 > 9) {
                        risks[SIZE * i + k][SIZE * j + l + SIZE] = 1;
                    } else {
                        risks[SIZE * i + k][SIZE * j + l + SIZE] = value + 1;
                    }
                }
            }
        }
    }

    printf("Part 2: %d\n", findMinDistance(true));

    free(risks);
    free(distances);
    return 0;
}
