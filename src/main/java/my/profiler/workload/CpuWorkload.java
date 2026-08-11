package my.profiler.workload;

import my.profiler.Fibonacci;

/**
 * Pure CPU load: Fibonacci numbers, either the exponential recursive version
 * or the linear iterative one. Running both is the simplest way to see the
 * difference between a real hotspot and an already optimized method.
 */
public class CpuWorkload implements Workload {
    private final boolean iterative;

    public CpuWorkload(boolean iterative) {
        this.iterative = iterative;
    }

    @Override
    public String name() {
        return iterative ? "cpu-iterative" : "cpu";
    }

    @Override
    public String description() {
        return iterative
                ? "iterative Fibonacci, almost free; --n = the Fibonacci argument"
                : "recursive Fibonacci, one deep CPU hotspot; --n = the Fibonacci argument";
    }

    @Override
    public long defaultNumber() {
        return 39;
    }

    @Override
    public long runIteration(long number) {
        return iterative ? Fibonacci.fibIterative(number) : Fibonacci.fibRecursive(number);
    }
}
