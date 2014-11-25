package ffmap.benchmarks.memory;

import com.carrotsearch.sizeof.RamUsageEstimator;
import ffmap.FFMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @author Max Osipov
 */
public class SimpleMemoryBenchmark {

    public static final int COLLECTION_SIZE = 1000000;

    public static void main(String[] args) {

        System.out.println("FFMapSize\tHashMapSize\tElements\tFFMapPerElement\tHashMapPerElement\tFATInBytes\tFATPerElement");

        Map<String, Integer> fmap = new FFMap<>();
        Map<String, Integer> map = new HashMap<>();

        Random random = new Random(33133312L);
        for (int i = 0; i < COLLECTION_SIZE; i ++) {
            String key = new UUID(random.nextLong(), random.nextLong()).toString();
            fmap.put(key, i);
            map.put(key, i);
            if (i % 10000 == 0 && i > 0) {
                printMapStats(fmap, map);
            }
        }

        printMapStats(fmap, map);
    }

    private static void printMapStats(Map<String, Integer> ffMap, Map<String, Integer> hashMap) {
        long fMapSize = RamUsageEstimator.sizeOf(ffMap);
        long mapSize = RamUsageEstimator.sizeOf(hashMap);
        int size = ffMap.size();

        System.out.println(fMapSize + "\t" + mapSize + "\t" +
                size + "\t" + (fMapSize / size) + "\t" + (mapSize / size) + "\t" + (mapSize - fMapSize) +
                "\t" + (mapSize - fMapSize) / size);

    }

}
