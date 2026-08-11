package my.profiler;

import my.profiler.workload.Workloads;

public class LoadConfig {
    /** Value of {@link #getNumber()} while the workload default has not been applied yet. */
    public static final long UNSET_NUMBER = -1;

    private static final int DEFAULT_ROUNDS = 120;
    private static final int DEFAULT_REPEATS_PER_ROUND = 3;
    private static final int DEFAULT_SLEEP_MILLIS = 100;
    private static final int DEFAULT_THREADS = 1;

    private final String workloadName;
    private final long number;
    private final int rounds;
    private final int repeatsPerRound;
    private final int sleepMillis;
    private final int threads;

    public LoadConfig(String workloadName, long number, int rounds, int repeatsPerRound, int sleepMillis, int threads) {
        this.workloadName = workloadName;
        this.number = number;
        this.rounds = rounds;
        this.repeatsPerRound = repeatsPerRound;
        this.sleepMillis = sleepMillis;
        this.threads = threads;
    }

    /**
     * Parses the command line. Every option is optional; the defaults reproduce the
     * original behaviour of this project: recursive Fibonacci of 39, single threaded.
     *
     * @throws IllegalArgumentException if an option is unknown or its value is not usable
     */
    public static LoadConfig fromArgs(String[] args) {
        String workloadName = Workloads.DEFAULT_NAME;
        long number = UNSET_NUMBER;
        int rounds = DEFAULT_ROUNDS;
        int repeatsPerRound = DEFAULT_REPEATS_PER_ROUND;
        int sleepMillis = DEFAULT_SLEEP_MILLIS;
        int threads = DEFAULT_THREADS;

        for (String arg : args) {
            // Kept from the original command line, where "iterative" was the only argument.
            if ("iterative".equalsIgnoreCase(arg)) {
                workloadName = "cpu-iterative";
                continue;
            }
            if ("recursive".equalsIgnoreCase(arg)) {
                workloadName = "cpu";
                continue;
            }

            int separator = arg.indexOf('=');
            if (!arg.startsWith("--") || separator < 0) {
                throw new IllegalArgumentException("Unexpected argument '" + arg + "', options look like --name=value");
            }

            String name = arg.substring(2, separator);
            String value = arg.substring(separator + 1);
            switch (name) {
                case "workload":
                    workloadName = value;
                    break;
                case "n":
                    number = parseBounded(name, value, 0);
                    break;
                case "rounds":
                    rounds = (int) parseBounded(name, value, 1);
                    break;
                case "repeats":
                    repeatsPerRound = (int) parseBounded(name, value, 1);
                    break;
                case "sleep":
                    sleepMillis = (int) parseBounded(name, value, 0);
                    break;
                case "threads":
                    threads = (int) parseBounded(name, value, 1);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown option '--" + name + "'");
            }
        }

        return new LoadConfig(workloadName, number, rounds, repeatsPerRound, sleepMillis, threads);
    }

    public static boolean isHelpRequested(String[] args) {
        for (String arg : args) {
            if ("--help".equals(arg) || "-h".equals(arg)) {
                return true;
            }
        }
        return false;
    }

    private static long parseBounded(String name, String value, long minimum) {
        long parsed;
        try {
            parsed = Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Option --" + name + " expects a number but got '" + value + "'");
        }
        if (parsed < minimum) {
            throw new IllegalArgumentException("Option --" + name + " must be at least " + minimum + " but was " + parsed);
        }
        if (parsed > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Option --" + name + " is too large: " + parsed);
        }
        return parsed;
    }

    /** Same configuration, but with {@code --n} filled in from the workload when the user left it out. */
    public LoadConfig withNumberDefault(long fallback) {
        if (number != UNSET_NUMBER) {
            return this;
        }
        return new LoadConfig(workloadName, fallback, rounds, repeatsPerRound, sleepMillis, threads);
    }

    public String getWorkloadName() {
        return workloadName;
    }

    public long getNumber() {
        return number;
    }

    public int getRounds() {
        return rounds;
    }

    public int getRepeatsPerRound() {
        return repeatsPerRound;
    }

    public int getSleepMillis() {
        return sleepMillis;
    }

    public int getThreads() {
        return threads;
    }

    /** Iterations across the whole run: every thread does {@code repeatsPerRound} of them each round. */
    public long getTotalIterations() {
        return (long) rounds * threads * repeatsPerRound;
    }
}
