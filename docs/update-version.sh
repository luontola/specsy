#!/bin/bash
set -eu
: ${1:? Usage: $0 RELEASE_VERSION}

RELEASE_VERSION="$1"
if [[ ! "$RELEASE_VERSION" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
    echo "Error: RELEASE_VERSION must be in X.Y.Z format, but was $RELEASE_VERSION"
    exit 1
fi

function assert-file-contains-substring() {
    local file="$1"
    local expected="$2"
    cat "$file" | grep --quiet --fixed-strings -e "$expected" || (echo "Error: file $file did not contain $expected"; exit 1)
}

function set-documentation-version() {
    local file="documentation.md"
    local version="$1"
    sed -i -r -e "s/^(\\s*&lt;version>).+(<\\/version>)\$/\1$version\2/" "$file"
    assert-file-contains-substring "$file" "&lt;version>$version</version>"
}

set -x

set-documentation-version "$RELEASE_VERSION"
git add -u
git commit -m "Update version number to $RELEASE_VERSION"
