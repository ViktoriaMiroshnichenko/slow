package my.profiler.workload;

/**
 * Monitor contention: all threads fight over one lock, and the work inside the
 * critical section is much larger than the work outside it. With --threads=1
 * this is plain CPU load; the interesting picture starts at --threads=2 and up,
 * where a profiler shows threads blocked instead of running.
 */
public class LockWorkload implements Workload {
    private final Object lock = new Object();
    private long guarded;

    @Override
    public String name() {
        return "lock";
    }

    @Override
    public String description() {
        return "all threads contend on one monitor; --n = spins inside the critical section";
    }

    @Override
    public long defaultNumber() {
        return 5000;
    }

    @Override
    public long runIteration(long number) {
        // A little work outside the lock, so the threads do not arrive in lockstep.
        long local = 0;
        for (long i = 0; i < number / 10; i++) {
            local += i * 31;
        }

        synchronized (lock) {
            for (long i = 0; i < number; i++) {
                guarded += i ^ local;
            }
            return guarded;
        }
    }
}
