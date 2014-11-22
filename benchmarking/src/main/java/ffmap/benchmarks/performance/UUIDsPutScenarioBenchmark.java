package ffmap.benchmarks.performance;

import ffmap.FFMap;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;


/**
 * @author Max Osipov
 */
public class UUIDsPutScenarioBenchmark {

    @State(Scope.Thread)
    public static class KeysState {

        List<String> keys;
        int index = 0;
        Map<String, Integer> map;

        public KeysState() {

            map = createMap();
            index = 0;
            keys = new ArrayList<>();

            Random random = new Random(12241);
            for (int i = 0; i < 1000000; i ++) {
                String key = new UUID(random.nextLong(), random.nextLong()).toString();
                keys.add(key);
            }
        }
    }

    @State(Scope.Thread)
    public static class MapState {

        List<String> keys;
        int index = 0;
        Map<String, Integer> map;

        public MapState() {

            map = createMap();
            index = 0;
            keys = new ArrayList<>();

            Random random = new Random(12241);
            for (int i = 0; i < 1000000; i ++) {
                String key = new UUID(random.nextLong(), random.nextLong()).toString();
                keys.add(key);
                map.put(key, i);
            }
        }
    }

    @Benchmark
    public void benchmarkPut(KeysState state) {
        state.map.put(state.keys.get(state.index % state.keys.size()), state.index);
        state.index ++;
    }

    @Benchmark
    public int benchmarkGet(MapState state) {
        return state.map.get(state.keys.get((state.index ++) % state.keys.size()));
    }

    private static Map<String, Integer> createMap() {
        return new FFMap<>();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(UUIDsPutScenarioBenchmark.class.getSimpleName())
                .warmupIterations(10)
                .measurementIterations(20)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
