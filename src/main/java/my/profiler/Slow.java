package my.profiler;

public class Slow {
    private static volatile long sink;

     static void main(String[] args) throws InterruptedException {
        LoadConfig config = new LoadConfig(39, 10, 3, 100);

        ConsoleReporter.printDone(config.getNumber());
        long startTime = System.currentTimeMillis();

        boolean isRecursive = "recursive".equals(config.getNumber());

        for (int round = 1; round <= config.getRounds(); round++) {
            for (int i = 0; i < config.getRepeatsPerRound(); i++) {
                if (isRecursive) {
                    sink = Fibonacci.fibRecursive(config.getNumber());
                } else {
                    sink = Fibonacci.fibRecursive(config.getNumber());
                }
            }

            ConsoleReporter.printRound(round, config.getRounds(), sink);
            Thread.sleep(config.getSleepMillis());
        }
        long totalTime = System.currentTimeMillis() - startTime;
        ConsoleReporter.printDone(sink);
    }
}