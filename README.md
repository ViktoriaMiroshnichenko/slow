# Slow Project

A Java performance comparison tool for Fibonacci number computation using recursive and iterative methods.

## Description

This project demonstrates the performance difference between a naive recursive Fibonacci algorithm and an optimized iterative one. It calculates Fibonacci numbers for indices 1 through 50 and prints the results along with the time taken by each method.

The recursive approach has exponential time complexity O(2^n), making it noticeably slow for larger values — hence the project name **Slow**.

## Project Structure

```
slow/
├── src/
│   └── my/profiler/
│       └── Slow.java       # Main application
├── README.md
└── Slow.iml
```

## Methods

- **`fibRecursive(long i)`** — Computes the Fibonacci number recursively. Simple but extremely slow for large indices.
- **`fibFast(long i)`** — Computes the Fibonacci number using a fast iterative approach with O(n) time complexity.

## How to Run

Compile and run using the command line:

```bash
javac -d out src/my/profiler/Slow.java
java -cp out my.profiler.Slow
```

Or open the project in IntelliJ IDEA and run the `Slow` class directly.

## Example Output

```
Fibonacci recursive 1 = 1 took 0ms
Fibonacci fast      1 = 1 took 0ms
Fibonacci recursive 2 = 2 took 0ms
Fibonacci fast      2 = 1 took 0ms
...
Fibonacci recursive 40 = 165580141 took 1023ms
Fibonacci fast      40 = 102334155 took 0ms
```
