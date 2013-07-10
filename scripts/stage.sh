#!/bin/bash
set -eu
: ${1:? Usage: $0 DESCRIPTION}
DESCRIPTION="$1"
set -x

mvn clean deploy \
    --errors \
    -Psonatype-oss-release \
    -DaltDeploymentRepository="staging::default::file:staging"

mvn nexus-staging:deploy-staged-repository \
    --errors \
    -DrepositoryDirectory=staging \
    -DstagingDescription="$DESCRIPTION"
