package ffmap.benchmarks;


import ffmap.benchmarks.memory.SimpleMemoryBenchmark;
import ffmap.benchmarks.performance.UUIDsGetScenarioBenchmark;

import java.util.Arrays;

public class Launcher {
    
    public static void main(String[] args) throws Exception {
        
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
            UUIDsGetScenarioBenchmark.main(bargs);
        }
        else {
            System.err.println("Unknown benchmark");
        }
    }
}