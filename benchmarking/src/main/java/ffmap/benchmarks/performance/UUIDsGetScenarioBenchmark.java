package ffmap.benchmarks.performance;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;


/**
 * @author Max Osipov
 */
@State(Scope.Thread)
public class UUIDsGetScenarioBenchmark extends MapImplsBenchmark {

    List<String> keys;
    int index = 0;

    @Setup(Level.Iteration)
    public void setup() throws Exception {
        keys = new ArrayList<>();

        Random random = new Random(12241);
        for (int i = 0; i < 1000000; i ++) {
            String key = new UUID(random.nextLong(), random.nextLong()).toString();
            keys.add(key);
            map.put(key, i);
        }
    }

    @Setup(Level.Iteration)
    public void setupIteration() {
        index = 0;
    }

    @Benchmark
    public int benchmarkGet() {
        return map.get(keys.get((index ++) % keys.size()));
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(UUIDsGetScenarioBenchmark.class.getSimpleName())
                .warmupIterations(20)
                .measurementIterations(20)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
