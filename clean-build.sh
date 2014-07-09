#!/bin/sh
set -eux
JAVA_HOME="$JAVA7_HOME" mvn clean verify
