package my.profiler.workload;

/**
 * A single kind of load the application can generate.
 *
 * One instance is shared by all worker threads, so implementations must be thread-safe.
 * Every workload leaves a different, easily recognizable footprint in a profiler:
 * CPU samples, allocation churn, monitor contention or growing retained heap.
 */
public interface Workload {
    /** Name used on the command line, e.g. {@code --workload=cpu}. */
    String name();

    /** One-line description shown in the help output. */
    String description();

    /** Value for {@code --n} when the user does not pass one; its meaning is workload-specific. */
    long defaultNumber();

    /**
     * Performs one unit of work.
     *
     * @param number the resolved {@code --n} value
     * @return a value derived from the work, so the JIT cannot optimize the call away
     */
    long runIteration(long number);

    /** Extra line printed after the run, e.g. retained heap. Empty when there is nothing to add. */
    default String summaryLine() {
        return "";
    }
}
