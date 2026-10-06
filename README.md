# Slow

A small Java project that generates load on purpose, so you have something to point a profiler at.

## Purpose
Every workload here has a different, recognizable footprint — a CPU hotspot, allocation churn,
monitor contention, a growing heap. Because you know in advance what the program is doing, you can
learn to read what the profiler shows you.

## Main class
`my.profiler.Slow`

## Running

```
java my.profiler.Slow                                  # recursive Fibonacci of 39, single threaded
java my.profiler.Slow --workload=alloc                 # GC pressure instead of CPU load
java my.profiler.Slow --workload=lock --threads=8      # eight threads fighting over one monitor
java my.profiler.Slow --help                           # all options and workloads
```

### Options

| Option | Meaning | Default |
| --- | --- | --- |
| `--workload=NAME` | what kind of load to generate | `cpu` |
| `--n=N` | size knob, meaning depends on the workload | per workload |
| `--rounds=N` | how many rounds to run | `120` |
| `--repeats=N` | iterations per thread per round | `3` |
| `--threads=N` | worker threads | `1` |
| `--sleep=MS` | pause between rounds, keeps the machine usable | `100` |

The defaults reproduce the original behaviour of this project. With `--threads=1` the work runs on
the main thread, so nothing from a thread pool shows up in the profile; above that, the workers are
named `slow-worker-N`.

### Workloads

| Name | What it does | `--n` means | Default |
| --- | --- | --- | --- |
| `cpu` | recursive Fibonacci — one deep, obvious hotspot | the Fibonacci argument | `39` |
| `cpu-iterative` | the same result computed in linear time | the Fibonacci argument | `39` |
| `alloc` | allocates short-lived arrays and drops them, keeping the GC busy | KB allocated per iteration | `2048` |
| `lock` | all threads contend on one monitor | spins inside the critical section | `5000` |
| `leak` | keeps everything it allocates, so retained heap only grows | KB retained per iteration | `256` |

Things worth trying:

- `cpu` vs `cpu-iterative` — the same output, one of them a hotspot and the other invisible.
- `cpu --threads=4` vs `lock --threads=4` — CPU load scales with threads, contention does not.
- `alloc` vs `leak` — both allocate hard, but only one of them makes the heap grow.
- `leak` under a small heap (`java -Xmx64m ...`) — it will reach `OutOfMemoryError`, which is the point.

## Output

Each round prints its result and duration; at the end the program prints a summary with wall time,
busy time excluding sleeps, min/avg/p95/max round time and throughput. Those are naive numbers to
hold next to whatever the profiler reports.

## Remote change
This line was added on the remote.
