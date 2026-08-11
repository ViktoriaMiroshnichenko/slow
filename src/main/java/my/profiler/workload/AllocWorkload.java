package my.profiler.workload;

/**
 * Allocation churn: every iteration creates short-lived arrays and drops them
 * immediately, so the young generation fills up and the GC runs constantly.
 * Nothing is retained, so the heap stays flat while GC activity stays high.
 */
public class AllocWorkload implements Workload {
    private static final int CHUNK_BYTES = 1024;

    @Override
    public String name() {
        return "alloc";
    }

    @Override
    public String description() {
        return "short-lived byte[] churn, keeps the GC busy; --n = KB allocated per iteration";
    }

    @Override
    public long defaultNumber() {
        return 2048;
    }

    @Override
    public long runIteration(long number) {
        long sum = 0;
        for (long i = 0; i < number; i++) {
            byte[] chunk = new byte[CHUNK_BYTES];
            chunk[0] = (byte) i;
            chunk[chunk.length - 1] = (byte) (i >> 8);
            sum += chunk[0] + chunk[chunk.length - 1];
        }
        return sum;
    }
}
