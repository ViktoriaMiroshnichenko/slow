package my.profiler;

import my.profiler.workload.Workload;
import my.profiler.workload.Workloads;

import java.util.Locale;

public class ConsoleReporter {
    public static void printHeader(LoadConfig config, Workload workload) {
        StringBuilder sb = new StringBuilder();
        sb.append("Starting '").append(workload.name()).append("' load:")
          .append(" --n=").append(config.getNumber())
          .append(" --rounds=").append(config.getRounds())
          .append(" --repeats=").append(config.getRepeatsPerRound())
          .append(" --threads=").append(config.getThreads())
          .append(" --sleep=").append(config.getSleepMillis());
        System.out.println(sb);
        if (config.getThreads() == 1 && "lock".equals(workload.name())) {
            System.out.println("WARNING: the lock workload only contends with --threads=2 or more.");
        }
    }

    public static void printRound(int round, int totalRounds, long result, long durationNanos) {
        System.out.println("Round " + round + "/" + totalRounds
                + ", result = " + result
                + ", took " + millis(durationNanos));
    }

    public static void printSummary(LoadConfig config, Workload workload, RoundStats stats, long wallNanos, long result) {
        System.out.println();
        System.out.println("--- Summary ---");
        System.out.println(line("Workload", workload.name() + " (" + workload.description() + ")"));
        System.out.println(line("Threads", String.valueOf(config.getThreads())));
        System.out.println(line("Iterations", config.getTotalIterations()
                + " (" + config.getRounds() + " rounds x " + config.getThreads()
                + " threads x " + config.getRepeatsPerRound() + " repeats)"));
        System.out.println(line("Wall time", seconds(wallNanos)));
        System.out.println(line("Busy time", seconds(stats.getBusyNanos()) + ", sleep excluded"));
        if (!stats.isEmpty()) {
            System.out.println(line("Round time", "min " + millis(stats.getMinNanos())
                    + ", avg " + millis(stats.getAverageNanos())
                    + ", p95 " + millis(stats.getPercentileNanos(95))
                    + ", max " + millis(stats.getMaxNanos())));
            System.out.println(line("Throughput", throughput(config.getTotalIterations(), stats.getBusyNanos())));
        }
        String extra = workload.summaryLine();
        if (!extra.isEmpty()) {
            System.out.println(extra);
        }
        System.out.println(line("Last result", String.valueOf(result)));
    }

    public static void printUsage() {
        System.out.println("Usage: java my.profiler.Slow [options]");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  --workload=NAME  what kind of load to generate (default: " + Workloads.DEFAULT_NAME + ")");
        System.out.println("  --n=N            workload-specific size knob (default: per workload, see below)");
        System.out.println("  --rounds=N       how many rounds to run (default: 120)");
        System.out.println("  --repeats=N      iterations per thread per round (default: 3)");
        System.out.println("  --threads=N      worker threads (default: 1)");
        System.out.println("  --sleep=MS       pause between rounds (default: 100)");
        System.out.println("  --help, -h       print this text");
        System.out.println();
        System.out.println("Workloads:");
        for (Workload workload : Workloads.all()) {
            System.out.println(String.format(Locale.ROOT, "  %-14s %s [--n default %d]",
                    workload.name(), workload.description(), workload.defaultNumber()));
        }
    }

    public static void printError(String message) {
        System.err.printf("Error: %s%n", message);
        System.err.println("Run with --help to see the available options.");
    }

    private static String line(String label, String value) {
        return String.format(Locale.ROOT, "%-16s %s", label + ":", value);
    }

    private static String millis(long nanos) {
        return String.format(Locale.ROOT, "%.1f ms", nanos / 1_000_000.0);
    }

    private static String seconds(long nanos) {
        return String.format(Locale.ROOT, "%.2f s", nanos / 1_000_000_000.0);
    }

    private static String throughput(long iterations, long busyNanos) {
        if (busyNanos <= 0) {
            return "n/a";
        }
        return String.format(Locale.ROOT, "%.1f iterations/sec", iterations * 1_000_000_000.0 / busyNanos);
    }
}
