#!/bin/bash

echo "Benchmarking..."
sdir=`dirname $0`

echo "Performance test"
java -Xmx1024m -jar $sdir/benchmarking.jar performance > performance.log

echo "Memory test"
java -Xmx1024m -jar $sdir/benchmarking.jar memory > memory.log
