package org.fstruct.fmap;

import com.carrotsearch.sizeof.RamUsageEstimator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.*;

public class FMapTest {

    public static final int COUNT_OF_ITEMS = 2500000;

    @org.junit.Test
    public void testPut() throws Exception {
        Map<String, Integer> map1 = new FMap<>(700001);
        Map<String, Integer> map2 = new HashMap<>();

        Random random = new Random();
        for (int i = 0; i < COUNT_OF_ITEMS; i ++) {
            String key = new UUID(random.nextLong(), random.nextLong()).toString();
            map1.put(key, i);
            map2.put(key, i);
            assertEquals(map1.size(), map2.size());
            assertEquals("For index " + key, map1.get(key), map2.get(key));
            assertTrue(map1.containsKey(key));
        }

        long size1 = RamUsageEstimator.sizeOf(map1);
        long size2 = RamUsageEstimator.sizeOf(map2);
        System.out.println("Sizes are " + size1 + " vs " + size2 + " and the ratio is " + ((double) size1 / size2) +
                " or " + (size2 - size1) / COUNT_OF_ITEMS + " bytes per element; or " + (size2 - size1) / (1024 * 1024) + " mb");
    }
}