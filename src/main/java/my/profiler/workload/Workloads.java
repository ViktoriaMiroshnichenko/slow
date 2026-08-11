package my.profiler.workload;

import java.util.ArrayList;
import java.util.List;

/** Everything the {@code --workload} option accepts. */
public final class Workloads {
    public static final String DEFAULT_NAME = "cpu";

    private Workloads() {
    }

    /** A fresh instance of every known workload, in the order they are listed in the help. */
    public static List<Workload> all() {
        List<Workload> workloads = new ArrayList<>();
        workloads.add(new CpuWorkload(false));
        workloads.add(new CpuWorkload(true));
        workloads.add(new AllocWorkload());
        workloads.add(new LockWorkload());
        workloads.add(new LeakWorkload());
        return workloads;
    }

    /** @throws IllegalArgumentException if no workload has that name */
    public static Workload create(String name) {
        for (Workload workload : all()) {
            if (workload.name().equalsIgnoreCase(name)) {
                return workload;
            }
        }
        throw new IllegalArgumentException("Unknown workload '" + name + "', expected one of " + names());
    }

    public static String names() {
        StringBuilder names = new StringBuilder();
        for (Workload workload : all()) {
            if (names.length() > 0) {
                names.append(", ");
            }
            names.append(workload.name());
        }
        return names.toString();
    }
}
