package my.profiler;

public class Fibonacci {
    public static long fibRecursive(long n) {
        if (n < 2) return 1;
        return fibRecursive(n - 2) + fibRecursive(n - 1);
    }

    public static long fibIterative(long n) {
        long previous = 1;
        long current = 1;
        for (long i = 2; i <= n; i++) {
            long next = previous + current;
            previous = current;
            current = next;
        }
        return current;
    }
}