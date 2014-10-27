package org.fstruct.fmap;

import com.carrotsearch.sizeof.RamUsageEstimator;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class FMapTest {

    @org.junit.Test
    public void testPut() throws Exception {
        Map<Integer, Integer> map1 = new FMap<Integer, Integer>(2500000);
        Map<Integer, Integer> map2 = new HashMap<Integer, Integer>();

        for (int i = 0; i < 2500000; i ++) {
            map1.put(i , i);
            map2.put(i, i);
            assertEquals(map1.size(), map2.size());
        }

        long size1 = RamUsageEstimator.sizeOf(map1);
        long size2 = RamUsageEstimator.sizeOf(map2);
        System.out.println("Sizes are " + size1 + " vs " + size2 + " and the ratio is " + ((double) size1 / size2) +
                " or " + (size2 - size1) / 2500000 + " per element; or " + (size2 - size1) / (1024 * 1024) + " mb");

        for (int i = 0; i < 2500000; i ++) {
            assertEquals(map1.get(i / 2), map2.get(i / 2));
        }
    }
}