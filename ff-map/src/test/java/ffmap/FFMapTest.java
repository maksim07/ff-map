package ffmap;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class FFMapTest {

    public static final int COUNT_OF_ITEMS = 250000;

    @Test
    public void testNullKey() throws Exception {
        Map<String, Integer> map1 = new FFMap<>();
        map1.put("key", 1);
        map1.put(null, 2);
        map1.put("key", 3);
        map1.put(null, 4);

        assertEquals(map1.size(), 2);
        assertTrue(map1.containsKey("key"));
        assertTrue(map1.containsKey(null));
        assertEquals(map1.get("key"), new Integer(3));
        assertEquals(map1.get(null), new Integer(4));
    }

    @Test
    public void testPut() throws Exception {
        Map<String, Integer> map1 = new FFMap<>(1024 * 64);
        Map<String, Integer> map2 = new HashMap<>();

        Random random = new Random(121);
        for (int i = 0; i < COUNT_OF_ITEMS; i ++) {
            String key = new UUID(random.nextLong(), random.nextLong()).toString();

            map1.put(key, i);
            map2.put(key, i);
            assertEquals(map1.size(), map2.size());
            assertEquals("For index " + key, map1.get(key), map2.get(key));
            assertTrue(map1.containsKey(key));
        }
    }

    @Test
    public void testKeySet() {
        Map<Integer, Integer> map = new FFMap<>(37);

        for (int i = 0; i < 100; i ++)
            map.put(i, i + 1);

        Set<Integer> keySet = map.keySet();
        for (int i = 0; i < 100; i ++) {
            assertTrue(keySet.contains(i));
        }

        int count = 0;
        for (Integer key: keySet) {
            count ++;
        }

        assertEquals(count, map.size());
        assertEquals(keySet.size(), map.size());
    }

    @Test
    public void testValueSet() {
        Map<Integer, Integer> map = new FFMap<>(37);

        for (int i = 0; i < 100; i ++)
            map.put(i, i % 2);

        Collection<Integer> valueSet = map.values();
        int zeros = 0;
        int ones = 0;
        for (Integer i : valueSet) {
            if (i == 0) zeros ++;
            else if (i == 1) ones ++;
            else fail("Wrong value");
        }

        assertEquals(zeros, 50);
        assertEquals(ones, 50);
    }

    @Test
    public void testClear() {
        Map<Integer, Integer> map = new FFMap<>();

        for (int i = 0; i < COUNT_OF_ITEMS; i ++)
            map.put(i, i + 1);

        assertEquals(map.size(), COUNT_OF_ITEMS);
        map.clear();
        assertEquals(map.size(), 0);

        for (Integer i : map.keySet())
            fail();

        for (Integer i : map.values())
            fail();
    }

    @Test
    public void testRemove() {
        Map<Integer, Integer> map = new FFMap<>();

        for (int i = 0; i < COUNT_OF_ITEMS; i ++)
            map.put(i, i + 1);

        int itemsToRemove = 2000;
        int offset = COUNT_OF_ITEMS / 2;
        for (int i = 0; i < itemsToRemove; i ++)
            assertEquals(map.remove(i + offset), new Integer(i + offset + 1));

        assertEquals(map.size(), COUNT_OF_ITEMS - itemsToRemove);
        int offset2 = 100;
        for (int i = 0; i < itemsToRemove; i ++)
            map.put(COUNT_OF_ITEMS + i - offset2, 1);

        assertEquals(map.size(), COUNT_OF_ITEMS - offset2);

        map.remove(-1);
        assertEquals(map.size(), COUNT_OF_ITEMS - offset2);
    }

    @Test
    public void testRemoveSmallMap() {
        Map<Integer, Integer> map = new FFMap<>();

        map.put(1, 2);
        assertEquals(map.size(), 1);
        assertEquals(map.get(1), new Integer(2));

        map.remove(1);
        assertEquals(map.size(), 0);
        assertNull(map.get(1));

        map.put(1, 2);
        assertEquals(map.size(), 1);
        assertEquals(map.get(1), new Integer(2));
    }
}