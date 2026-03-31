package my.profiler;

public class Slow {
    private static volatile long sink;

    public static void main(String[] args) throws InterruptedException {
        for (int round = 0; round < 50; round++) {
            for (int i = 0; i < 5; i++) {
                sink = fibRecursive(39);
            }
            Thread.sleep(50);
        }

        System.out.println("Done: " + sink);
    }

    static long fibRecursive(long n) {
        if (n < 2) return 1;
        return fibRecursive(n - 2) + fibRecursive(n - 1);
    }
}