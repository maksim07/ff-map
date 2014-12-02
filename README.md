# Fat free Map

The purpose of the project is to implement symbol table (dictionary, associative array, java.util.Map) data structure
with less memory footprint compared to standard implementations.

# Benchmarking

To compare FFMap performance to other popular implementations one can run benchmarks (you need cURL to be installed on your system):

    curl -L https://bintray.com/artifact/download/maksim07/maven/ff-map/benchmarking/0.7/benchmarking-0.7-jar-with-dependencies.tar.gz | tar -xz
    ./benchmarking.sh

If you want to test FFMap performance against particular JVM version you could use the following command instead of the last line above:

    export BENCHMARK_JAVA=<path to java executable>; ./benchmarking.sh

e.g.

    export BENCHMARK_JAVA=/Library/Java/JavaVirtualMachines/jdk1.8.0_25.jdk/Contents/Home/bin/java;./benchmarking.sh; ./benchmarking.sh

After script is completed you can find performance data in the following files:

  * **performance.log** - contains throughput tests of FFMap methods compared to corresponding methods of other implementations of java.util.Map
  * **memory.log** - contains information on how much memory take each map implementation

