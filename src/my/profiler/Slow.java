package my.profiler;

public class Slow {
    private static volatile long sink;

    public static void main(String[] args) throws InterruptedException {
        LoadConfig config = new LoadConfig(39, 50, 5, 50);

        for (int round = 1; round <= config.getRounds(); round++) {
            for (int i = 0; i < config.getRepeatsPerRound(); i++) {
                sink = Fibonacci.fibRecursive(config.getNumber());
            }

            ConsoleReporter.printRound(round, config.getRounds(), sink);
            Thread.sleep(config.getSleepMillis());
        }

        ConsoleReporter.printDone(sink);
    }
}