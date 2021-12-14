from functools import lru_cache
from collections import defaultdict

edges = defaultdict(list)
for line in open("input.txt").readlines():
    src, dst = map(lambda s: s.strip(), line.split("-"))
    edges[src].append(dst)
    edges[dst].append(src)

@lru_cache(maxsize=None)
def dfs(current, visited, twice=True):
    num_paths = 0
    for dst in edges[current]:
        if dst == "end":
            num_paths += 1
        elif not dst.islower():
            num_paths += dfs(dst, tuple(set(visited) | {dst}), twice)
        elif dst != "start":
            if dst not in visited:
                num_paths += dfs(dst, tuple(set(visited) | {dst}), twice)
            elif not twice:
                num_paths += dfs(dst, tuple(set(visited) | {dst}), True)
    return num_paths


print("Part 1:", dfs("start", tuple(set())))
print("Part 2:", dfs("start", tuple(set()), False))
