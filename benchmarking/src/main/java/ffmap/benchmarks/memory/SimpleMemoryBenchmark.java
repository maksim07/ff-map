package ffmap.benchmarks.memory;

import com.carrotsearch.sizeof.RamUsageEstimator;
import ffmap.FFMap;
import gnu.trove.map.hash.TCustomHashMap;
import gnu.trove.strategy.HashingStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @author Max Osipov
 */
public class SimpleMemoryBenchmark {

    public static final int COLLECTION_SIZE = 2000000;

    public static void main(String[] args) {

        System.out.println("FFMapSize\tHashMapSize\tElements\tFFMapPerElement\tHashMapPerElement\tFATInBytes" +
                "\tFATPerElement\tTroveMapSize\tTroveFatReduceInBytes\tTroveFatReducePerElement");

        Map<String, Integer> fmap = new FFMap<>();
        Map<String, Integer> map = new HashMap<>();
        Map<String, Integer> tmap = new TCustomHashMap<>(new HashingStrategy<String>() {
            @Override
            public int computeHashCode(String object) {
                return object.hashCode();
            }

            @Override
            public boolean equals(String o1, String o2) {
                return o1.equals(o2);
            }
        });


        Random random = new Random(33133312L);
        for (int i = 0; i < COLLECTION_SIZE; i ++) {
            String key = new UUID(random.nextLong(), random.nextLong()).toString();
            fmap.put(key, i);
            map.put(key, i);
            tmap.put(key, i);
            if (i % 100000 == 0 && i > 0) {
                printMapStats(fmap, map, tmap);
            }
        }

        printMapStats(fmap, map, tmap);
    }

    private static void printMapStats(Map<String, Integer> ffMap, Map<String, Integer> hashMap, Map<String, Integer> troveMap) {
        long fMapSize = RamUsageEstimator.sizeOf(ffMap);
        long mapSize = RamUsageEstimator.sizeOf(hashMap);
        long tmapSize = RamUsageEstimator.sizeOf(troveMap);

        int size = ffMap.size();

        System.out.println(fMapSize + "\t" + mapSize + "\t" +
                size + "\t" + (fMapSize / size) + "\t" + (mapSize / size) + "\t" + (mapSize - fMapSize) +
                "\t" + (mapSize - fMapSize) / size + "\t" +
                tmapSize + "\t" + (mapSize - tmapSize) + "\t" + (mapSize - tmapSize) / size);

    }

}
