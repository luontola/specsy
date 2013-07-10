#!/bin/bash
set -eu
: ${1:? Usage: $0 RELEASE_VERSION}

RELEASE_VERSION="$1"
if [[ ! "$RELEASE_VERSION" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
    echo "Error: RELEASE_VERSION must be in X.Y.Z format, but was $RELEASE_VERSION"
    exit 1
fi

function bump_version()
{
    local prefix=`echo $1 | sed -n -r 's/([0-9]+\.[0-9]+\.)[0-9]+/\1/p'`
    local suffix=`echo $1 | sed -n -r 's/[0-9]+\.[0-9]+\.([0-9]+)/\1/p'`
    ((suffix++))
    echo "$prefix$suffix-SNAPSHOT"
}
NEXT_VERSION=`bump_version $RELEASE_VERSION`
set -x

mvn versions:set \
    -DgenerateBackupPoms=false \
    -DnewVersion="$RELEASE_VERSION" \
    --file parent/pom.xml
git add -u
git commit -m "Release $RELEASE_VERSION"

mvn clean deploy \
    --errors \
    -Psonatype-oss-release \
    -DaltDeploymentRepository="staging::default::file:staging"

mvn nexus-staging:deploy-staged-repository \
    --errors \
    -DrepositoryDirectory=staging \
    -DstagingDescription="Specsy $RELEASE_VERSION"

# TODO: release OSSRH and push to GitHub automatically
#mvn nexus-staging:release \
#    --errors \
#    -DaltStagingDirectory=staging \
#    -DstagingDescription="Specsy $RELEASE_VERSION"

git tag -s -m "Specsy $RELEASE_VERSION" "v$RELEASE_VERSION"

mvn versions:set \
    -DgenerateBackupPoms=false \
    -DnewVersion="$NEXT_VERSION" \
    --file parent/pom.xml
git add -u
git commit -m "Prepare for next development iteration"

set +x
echo ""
echo "Done. Next steps:"
echo "    open https://oss.sonatype.org/"
echo "    git push"
echo "    git push --tags"
