package my.profiler;

public class ConsoleReporter {
    public static void printRound(int round, int totalRounds, long result) {
        System.out.println("Round " + round + "/" + totalRounds + ", result = " + result);
    }

    public static void printDone(long result) {
        System.out.println("Done: " + result);
    }
}