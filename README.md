# Fat free Map

The purpose of the project is to implement symbol table (dictionary, associative array, java.util.Map) data structure
with less memory footprint compared to standard implementations.

# Benchmarking

## Methodology

Currently, there are just two simple performance benchmarks are implemented. The first test tries to put as much elements as it can into the map
on each iteration. The second test runs get method on map which was filled with 1 mln elements before each iteration. In both cases
iteration lasts for 1 second and the benchmark captures how many elementary operation were performed during that period.
Obviously in the first case the **put** operation is measured, whether in second case it is the **get** method.
For further information you can check the code
of the performance benchmarks [benchmarking/src/main/java/ffmap/benchmarks/performance](benchmarking/src/main/java/ffmap/benchmarks/performance)

To measure memory footprint occupied by each implementation benchmark, the program fills each collection with the same elements and
takes memory snapshots ([benchmarking/src/main/java/ffmap/benchmarks/memory](benchmarking/src/main/java/ffmap/benchmarks/memory)).


## Running benchmarks

To compare FFMap performance to other popular implementations one can run benchmarks (you need cURL to be installed on your system):

    curl -L https://bintray.com/artifact/download/maksim07/maven/ff-map/benchmarking/0.7.1/benchmarking-0.7.1-jar-with-dependencies.tar.gz | tar -xz
    ./benchmarking.sh

If you want to test FFMap performance against particular JVM version you could use the following command instead of the last line above:

    export BENCHMARK_JAVA=<path to java executable>; ./benchmarking.sh

e.g.

    export BENCHMARK_JAVA=/Library/Java/JavaVirtualMachines/jdk1.8.0_25.jdk/Contents/Home/bin/java;./benchmarking.sh; ./benchmarking.sh

After script is completed you can find performance data in the following files:

  * **performance.log** - contains throughput tests of FFMap methods compared to corresponding methods of other implementations of java.util.Map
  * **memory.log** - contains information on how much memory take each map implementation

