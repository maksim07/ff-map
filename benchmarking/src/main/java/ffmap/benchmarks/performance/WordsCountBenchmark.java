package ffmap.benchmarks.performance;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Max Osipov
 */
@State(Scope.Thread)
public class WordsCountBenchmark extends MapImplsBenchmark {

    @Param({""})
    String texFilePath;
    List<String> words;
    int index;

    @Setup
    public void setup() throws Exception {
        words = new ArrayList<>();

        String regexp = "[\\s,\\.-:;\\+]";

        System.out.println();
        System.out.println("Loading text from " + texFilePath);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(texFilePath)))) {

            String line;
            while ((line = reader.readLine()) != null) {
                Collections.addAll(words, line.split(regexp));
            }
        }
        System.out.println("Text contains " + words.size() + " words");
    }

    @Setup(Level.Iteration)
    public void setupIteration() {
        index = 0;
    }

    @Benchmark
    public void benchmarkWordsCount() {
        String word = words.get(index % words.size());
        Integer count = map.get(word);
        map.put(word, (count == null ? 0 : count) + 1);
        index ++;
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(WordsCountBenchmark.class.getSimpleName())
                .param("texFilePath", args[0])
                .warmupIterations(20)
                .measurementIterations(20)
                .forks(1)
                .build();

        new Runner(opt).run();
    }

}
