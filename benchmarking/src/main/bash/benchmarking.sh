#!/bin/bash

if [ -z $BENCHMARK_JAVA ]; then
    if [ -z $JAVA_HOME ]; then
    BENCHMARK_JAVA=java
    else
    BENCHMARK_JAVA=$JAVA_HOME/bin/java
    fi
fi

echo "Benchmarking... (jdk $BENCHMARK_JAVA)"
sdir=`dirname $0`

echo "Performance test"
curl http://www.gutenberg.org/files/2600/2600.txt > $sdir/testfile.txt
$BENCHMARK_JAVA -Xmx1024m -jar $sdir/benchmarking.jar perfwc $sdir/testfile.txt > performance.log
rm $sdir/testfile.txt
$BENCHMARK_JAVA -Xmx1024m -jar $sdir/benchmarking.jar perfget >> performance.log
$BENCHMARK_JAVA -Xmx1024m -jar $sdir/benchmarking.jar perfput >> performance.log

echo "Memory test"
$BENCHMARK_JAVA -Xmx1024m -jar $sdir/benchmarking.jar memory > memory.log
