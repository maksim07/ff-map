package ffmap.benchmarks.performance;

import gnu.trove.map.hash.TCustomHashMap;
import gnu.trove.strategy.HashingStrategy;
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
public class UUIDsPutScenarioBenchmark {

    @Param({"gnu.trove.map.hash.TCustomHashMap", "java.util.HashMap", "ffmap.FFMap"})
    String mapClassName;

    List<String> keys;
    int index = 0;
    Map<String, Integer> map;

    @Setup(Level.Iteration)
    public void setup() throws Exception {
        map = (Map<String, Integer>) Class.forName(mapClassName).newInstance();
        if (map instanceof TCustomHashMap)
            map = new TCustomHashMap<>(new HashingStrategy<String>() {
                @Override
                public int computeHashCode(String object) {
                    return object.hashCode();
                }

                @Override
                public boolean equals(String o1, String o2) {
                    return o1.equals(o2);
                }
            });

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
