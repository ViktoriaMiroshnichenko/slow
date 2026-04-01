package my.profiler;

public class LoadConfig {
    private final long number;
    private final int rounds;
    private final int repeatsPerRound;
    private final int sleepMillis;

    public LoadConfig(long number, int rounds, int repeatsPerRound, int sleepMillis) {
        this.number = number;
        this.rounds = rounds;
        this.repeatsPerRound = repeatsPerRound;
        this.sleepMillis = sleepMillis;
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
}