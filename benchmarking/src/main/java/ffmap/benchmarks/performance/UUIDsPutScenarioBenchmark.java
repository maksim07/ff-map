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
public class UUIDsPutScenarioBenchmark extends MapImplsBenchmark {

    List<String> keys;
    int index = 0;

    @Setup(Level.Iteration)
    public void setup() throws Exception {
        index = 0;
        keys = new ArrayList<>();

        Random random = new Random(12241);
        for (int i = 0; i < 1000000; i ++) {
            String key = new UUID(random.nextLong(), random.nextLong()).toString();
            keys.add(key);
        }
    }

    @Benchmark
    public void benchmarkPut() {
        map.put(keys.get(index % keys.size()), index);
        index ++;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(UUIDsPutScenarioBenchmark.class.getSimpleName())
                .warmupIterations(20)
                .measurementIterations(20)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
