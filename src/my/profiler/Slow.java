package my.profiler;

public class Slow {
    public static void main(String[] args) {
        for (int i = 1; i < 50; i++) {
            long start = System.currentTimeMillis();
            System.out.println("Fibonacci recursive " + i + " = " + fibRecursive(i) + " took " + (System.currentTimeMillis() - start) + "ms");
            start = System.currentTimeMillis();
            System.out.println("Fibonacci fast      " + i + " = " + fibFast(i) + " took " + (System.currentTimeMillis() - start) + "ms");
        }
    }


    static long fibRecursive(long i) {
        if (i < 2) return 1;
        long l = fibRecursive(i - 2) + fibRecursive(i - 1);
        return l;
    }

    static long fibFast(long i) {
        if (i < 2) return i;
        int a = 0, b = 1, c = 0;
        while (i-- > 0) {
            c = a + b;
            a = b;
            b = c;
        }
        return c;
    }
}