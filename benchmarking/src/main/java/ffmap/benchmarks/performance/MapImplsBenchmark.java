package ffmap.benchmarks.performance;

import gnu.trove.map.hash.TCustomHashMap;
import gnu.trove.strategy.HashingStrategy;
import org.openjdk.jmh.annotations.*;

import java.util.Map;

/**
 * @author Max Osipov
 */
@State(Scope.Thread)
public class MapImplsBenchmark {
    @Param({"ffmap.FFMap", "gnu.trove.map.hash.TCustomHashMap", "java.util.HashMap"})
    String mapClassName;

    Map<String, Integer> map;

    @Setup(Level.Iteration)
    public void initMap() throws Exception {
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
    }
}
