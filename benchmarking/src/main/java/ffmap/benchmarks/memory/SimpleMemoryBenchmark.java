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
                long fMapSize = RamUsageEstimator.sizeOf(fmap);
                long mapSize = RamUsageEstimator.sizeOf(map);

                System.out.println(fMapSize + "\t" + mapSize + "\t" +
                                   i + "\t" + (fMapSize / i) + "\t" + (mapSize / i) + "\t" + (mapSize - fMapSize) +
                                   "\t" + (mapSize - fMapSize) / i);
            }
        }

        long fMapSize = RamUsageEstimator.sizeOf(fmap);
        long mapSize = RamUsageEstimator.sizeOf(map);

        System.out.println("FFMap: " + fMapSize + " or " + Math.round((double)fMapSize / COLLECTION_SIZE) + " per key");
        System.out.println("  Map: " + mapSize + " or " + Math.round((double)mapSize / COLLECTION_SIZE) + " per key");
        System.out.println("FMap/map percentage is " + Math.round(((double)fMapSize / mapSize) * 100) + " %");
        System.out.println("Per element difference is " + Math.round(((double)mapSize - fMapSize) / COLLECTION_SIZE) + " byte");
        System.out.println("Overall saving is " + Math.round((mapSize - fMapSize) / 1024d) + " Kb");
    }

}
