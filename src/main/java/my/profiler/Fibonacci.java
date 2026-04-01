package my.profiler;

public class Fibonacci {
    public static long fibRecursive(long n) {
        if (n < 2) return 1;
        return fibRecursive(n - 2) + fibRecursive(n - 1);
    }
}