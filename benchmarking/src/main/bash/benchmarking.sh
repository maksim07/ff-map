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
$BENCHMARK_JAVA -Xmx1024m -jar $sdir/benchmarking.jar perfget > performance.log
$BENCHMARK_JAVA -Xmx1024m -jar $sdir/benchmarking.jar perfput >> performance.log

echo "Memory test"
$BENCHMARK_JAVA -Xmx1024m -jar $sdir/benchmarking.jar memory > memory.log
