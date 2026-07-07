package my.profiler;

public class Slow {
    private static volatile long sink;

    public static void main(String[] args) throws InterruptedException {
        LoadConfig config = new LoadConfig(39, 120, 3, 100);

        System.out.println("Starting Fibonacci load test for number: " + config.getNumber());
        long startTime = System.currentTimeMillis();

        boolean isRecursive = args.length == 0 || !"iterative".equalsIgnoreCase(args[0]);

        for (int round = 1; round <= config.getRounds(); round++) {
            for (int i = 0; i < config.getRepeatsPerRound(); i++) {
                if (isRecursive) {
                    sink = Fibonacci.fibRecursive(config.getNumber());
                } else {
                    sink = Fibonacci.fibIterative(config.getNumber());
                }
            }

            ConsoleReporter.printRound(round, config.getRounds(), sink);
            Thread.sleep(config.getSleepMillis());
        }
        long totalTime = System.currentTimeMillis() - startTime;
        ConsoleReporter.printDone(sink);
    }
}