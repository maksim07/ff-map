package ffcollections.benchmarks;

import java.util.Arrays;
import ffcollections.benchmarks.memory.*;
import ffcollections.benchmarks.performance.*;


public class Launcher {
    
    public static void main(String[] args) {
        
        if (args.length < 1) {
            System.err.println("USAGE: java Launcher <becnhmark>");
            System.err.println("    banchmarks: memory, performance");
            System.exit(1);
            return;
        }
        
        String[] bargs = Arrays.copyOfRange(args, 1, args.length);
        String benchmark = args[0];
        
        if (benchmark.equals("memory")) {
                SimpleMemoryBenchmark.main(bargs);
        }
        else if (benchmark.equals("performance")) {
            UUIDsPutScenarioBenchmark.main(bargs);
        }
    }
}