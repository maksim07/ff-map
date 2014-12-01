#!/bin/bash

echo "Benchmarking..."
sdir=`dirname $0`

echo "Performance test"
java -Xmx1024m -jar $sdir/benchmarking.jar perfget > performance.log
java -Xmx1024m -jar $sdir/benchmarking.jar perfput >> performance.log

echo "Memory test"
java -Xmx1024m -jar $sdir/benchmarking.jar memory > memory.log
