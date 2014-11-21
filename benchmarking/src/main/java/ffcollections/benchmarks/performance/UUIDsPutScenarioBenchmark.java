package ffcollections.benchmarks.performance;

import org.openjdk.jmh.annotations.Benchmark;


/**
 * @author Max Osipov
 */
public class UUIDsPutScenarioBenchmark {

    @Benchmark
    public void testFFMap() {

//        for (int i = 0; i < 5; i ++) {
//            Map<String, Integer> map = args[0].equals("map") ? new HashMap<String, Integer>() : new FFMap<String, Integer>();
//            testMap(map);
//            System.out.println("Warm up loop " + i);
//        }
//
//        long time = 0;
//        for (int i = 0; i < 10; i ++) {
//            long t = System.currentTimeMillis();
//            Map<String, Integer> map = args[0].equals("map") ? new HashMap<String, Integer>() : new FFMap<String, Integer>();
//            testMap(map);
//            time += (System.currentTimeMillis() - t);
//        }

//        System.out.println("Average time is " + (time / 10));
    }

//    private static void testMap(Map<String, Integer> map) {
//        Random random = new Random(1235);
//        for (int i = 0; i < 1000000; i ++) {
//            String key = new UUID(random.nextLong(), random.nextLong()).toString();
//            map.put(key, i);
//        }
//    }
}
