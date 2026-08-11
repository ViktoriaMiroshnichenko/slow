package my.profiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Collects how long each round of work took, excluding the sleep between rounds.
 * These numbers are a naive baseline to hold next to whatever the profiler reports.
 */
public class RoundStats {
    private final List<Long> durationsNanos = new ArrayList<>();
    private long busyNanos;

    public void record(long durationNanos) {
        durationsNanos.add(durationNanos);
        busyNanos += durationNanos;
    }

    public boolean isEmpty() {
        return durationsNanos.isEmpty();
    }

    public long getBusyNanos() {
        return busyNanos;
    }

    public long getMinNanos() {
        return Collections.min(durationsNanos);
    }

    public long getMaxNanos() {
        return Collections.max(durationsNanos);
    }

    public long getAverageNanos() {
        return busyNanos / durationsNanos.size();
    }

    /** Nearest-rank percentile, so p95 is a round that actually happened. */
    public long getPercentileNanos(double percentile) {
        List<Long> sorted = new ArrayList<>(durationsNanos);
        Collections.sort(sorted);
        int rank = (int) Math.ceil(percentile / 100.0 * sorted.size());
        return sorted.get(Math.min(Math.max(rank, 1), sorted.size()) - 1);
    }
}
