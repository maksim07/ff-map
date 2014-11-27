#!/bin/bash

echo "Benchmarking..."
sdir=`dirname $0`

echo "Performance test"
java -jar $sdir/benchmarking.jar performance > performance.log

echo "Memory test"
java -jar $sdir/benchmarking.jar memory > memory.log
