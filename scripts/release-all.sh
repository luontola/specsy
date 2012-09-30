#!/bin/bash
set -eu
: ${1:? Usage: $0 VERSION}

VERSION="$1"
if [[ ! "$VERSION" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
    echo "Error: VERSION must be in X.Y.Z format, but was $VERSION"
    exit 1
fi

set -x

mvn versions:set \
    -DgenerateBackupPoms=false \
    -DnewVersion="$VERSION" \
    --file parent/pom.xml

git add -u
git commit -m "Release $VERSION"

mvn clean deploy \
    -Psonatype-oss-release

mvn nexus:staging-close \
    -Dnexus.description="Specsy $VERSION"

git tag -s -m "Release $VERSION" "v$VERSION"
