# by "mockle2" -- https://www.reddit.com/r/adventofcode/comments/rehj2r/2021_day_12_solutions/ho8zlrj/

from collections import defaultdict
from time import perf_counter
c = perf_counter()

paths = defaultdict(list)
for a, b in [line.split('-') for line in open('input.txt').read().splitlines()]:
    paths[a].append(b)
    paths[b].append(a)

def dfs(cave, visited, one_off):
    if cave == 'end':
        return 1
    if cave.islower():
        visited.add(cave)
    total = sum([dfs(i, visited, one_off) for i in paths[cave] if not i in visited])
    if one_off == ' ':
        total += sum([dfs(i, visited, i) for i in paths[cave] if i in visited and i != 'start'])
    if cave == one_off:
        pass
        # print("c: %s / %s" % (cave, one_off))
    else:
        visited.discard(cave)
    # visited.discard(cave)
    return total


print('Part 1:', dfs('start', set(), ''))
print('Part 2:', dfs('start', set(), ' '))

print("Elapsed time: %s" % (perf_counter() - c))
