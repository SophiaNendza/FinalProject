#!/bin/bash -ex

./compile.sh
mvn -q -e -Dprism.order=sw -Dexec.cleanupDaemonthreads=false exec:java
