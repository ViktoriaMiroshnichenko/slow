package my.profiler.workload;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A deliberate leak: every iteration keeps its allocation forever, so retained
 * heap grows linearly for the whole run and never comes back down after a GC.
 * That saw-less, always-climbing memory chart is exactly what a real leak looks like.
 *
 * <p>With the default settings the run retains well under 100 MB. Push --rounds,
 * --repeats or --n high enough and the JVM will eventually die with OutOfMemoryError,
 * which is the point: run it under a small -Xmx to get there quickly.
 */
public class LeakWorkload implements Workload {
    private static final int CHUNK_BYTES = 1024;

    private final List<byte[]> retained = new ArrayList<>();
    private long retainedBytes;

    @Override
    public String name() {
        return "leak";
    }

    @Override
    public String description() {
        return "never releases what it allocates; --n = KB retained per iteration";
    }

    @Override
    public long defaultNumber() {
        return 256;
    }

    @Override
    public long runIteration(long number) {
        for (long i = 0; i < number; i++) {
            byte[] chunk = new byte[CHUNK_BYTES];
            chunk[0] = (byte) i;
            synchronized (retained) {
                retained.add(chunk);
                retainedBytes += CHUNK_BYTES;
            }
        }
        synchronized (retained) {
            return retainedBytes;
        }
    }

    @Override
    public String summaryLine() {
        synchronized (retained) {
            return String.format(Locale.ROOT, "%-16s %.1f MB in %d objects",
                    "Retained:", retainedBytes / (1024.0 * 1024.0), retained.size());
        }
    }
}
