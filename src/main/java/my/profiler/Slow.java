package my.profiler;

import my.profiler.workload.Workload;
import my.profiler.workload.Workloads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class Slow {
    private static volatile long sink;

    public static void main(String[] args) throws InterruptedException {
        if (LoadConfig.isHelpRequested(args)) {
            ConsoleReporter.printUsage();
            return;
        }

        LoadConfig config;
        Workload workload;
        try {
            config = LoadConfig.fromArgs(args);
            workload = Workloads.create(config.getWorkloadName());
        } catch (IllegalArgumentException e) {
            ConsoleReporter.printError(e.getMessage());
            System.exit(1);
            return;
        }
        config = config.withNumberDefault(workload.defaultNumber());

        ConsoleReporter.printHeader(config, workload);

        RoundStats stats = new RoundStats();
        // Use a thread pool only when threads > 1; single-threaded mode runs inline.
        ExecutorService pool = config.getThreads() > 1 ? newPool(config.getThreads()) : null;
        long startTime = System.nanoTime();
        try {
            for (int round = 1; round <= config.getRounds(); round++) {
                long roundStart = System.nanoTime();
                if (pool == null) {
                    runIterations(workload, config);
                } else {
                    runIterationsInParallel(pool, workload, config);
                }
                long roundNanos = System.nanoTime() - roundStart;

                stats.record(roundNanos);
                ConsoleReporter.printRound(round, config.getRounds(), sink, roundNanos);
                Thread.sleep(config.getSleepMillis());
            }
        } finally {
            if (pool != null) {
                pool.shutdownNow();
            }
        }
        long totalNanos = System.nanoTime() - startTime;

        ConsoleReporter.printSummary(config, workload, stats, totalNanos, sink);
    }

    private static void runIterations(Workload workload, LoadConfig config) {
        for (int i = 0; i < config.getRepeatsPerRound(); i++) {
            sink = workload.runIteration(config.getNumber());
        }
    }

    private static void runIterationsInParallel(ExecutorService pool, Workload workload, LoadConfig config)
            throws InterruptedException {
        List<Callable<Void>> tasks = new ArrayList<>();
        for (int thread = 0; thread < config.getThreads(); thread++) {
            tasks.add(() -> {
                runIterations(workload, config);
                return null;
            });
        }

        for (Future<Void> future : pool.invokeAll(tasks)) {
            try {
                future.get();
            } catch (ExecutionException e) {
                throw new IllegalStateException("Workload '" + workload.name() + "' failed", e.getCause());
            }
        }
    }

    private static ExecutorService newPool(int threads) {
        AtomicInteger counter = new AtomicInteger();
        ThreadFactory factory = runnable -> {
            // Named threads make the profiler's thread list readable.
            Thread thread = new Thread(runnable, "slow-worker-" + counter.incrementAndGet());
            thread.setDaemon(true);
            return thread;
        };
        return Executors.newFixedThreadPool(threads, factory);
    }
}
