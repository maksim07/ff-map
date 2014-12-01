package ffmap.benchmarks.performance;

import ffmap.FFMap;
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
public class UUIDsGetScenarioBenchmark {

    @Param({"gnu.trove.map.hash.TCustomHashMap", "java.util.HashMap", "ffmap.FFMap"})
    String mapClassName;

    List<String> keys;
    int index = 0;
    Map<String, Integer> map;

    @Setup(Level.Iteration)
    public void setup() throws Exception {
        map = (Map<String, Integer>) Class.forName(mapClassName).newInstance();
        index = 0;
        keys = new ArrayList<>();

        Random random = new Random(12241);
        for (int i = 0; i < 1000000; i ++) {
            String key = new UUID(random.nextLong(), random.nextLong()).toString();
            keys.add(key);
        }
    }

    @State(Scope.Thread)
    public static class GetState {

        List<String> keys;
        int index = 0;
        Map<String, Integer> map;

        @Setup(Level.Iteration)
        public void setup() {

            map = create();
            index = 0;
            keys = new ArrayList<>();

            Random random = new Random(12241);
            for (int i = 0; i < 1000000; i ++) {
                String key = new UUID(random.nextLong(), random.nextLong()).toString();
                keys.add(key);
                map.put(key, i);
            }
        }

        public Map<String, Integer> create() {
            return new FFMap<>();
        }
    }

    @State(Scope.Thread)
    public static class GetStateHashMap extends GetState {
        @Override
        public Map<String, Integer> create() {
            return new HashMap<>();
        }
    }

    @Benchmark
    public void benchmarkPut() {
        map.put(keys.get(index % keys.size()), index);
        index ++;
    }

    @Benchmark
    public int benchmarkGet(GetState state) {
        return state.map.get(state.keys.get((state.index ++) % state.keys.size()));
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
