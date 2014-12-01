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
public class UUIDsPutScenarioBenchmark {

    @State(Scope.Thread)
    public static class PutState {
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
            }
        }

        public Map<String, Integer> create() {
            return new FFMap<>();
        }
    }

    @State(Scope.Thread)
    public static class PutStateHashMap extends PutState {
        @Override
        public Map<String, Integer> create() {
            return new HashMap<>();
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
    public void benchmarkPut(PutState state) {
        state.map.put(state.keys.get(state.index % state.keys.size()), state.index);
        state.index ++;
    }

    @Benchmark
    public int benchmarkGet(GetState state) {
        return state.map.get(state.keys.get((state.index ++) % state.keys.size()));
    }

    @Benchmark
    public void benchmarkPutHM(PutStateHashMap state) {
        state.map.put(state.keys.get(state.index % state.keys.size()), state.index);
        state.index ++;
    }

    @Benchmark
    public int benchmarkGetHM(GetStateHashMap state) {
        return state.map.get(state.keys.get((state.index ++) % state.keys.size()));
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
