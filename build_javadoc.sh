#!/bin/bash

test TRAVIS_PULL_REQUEST = true && exit 0

JAVA_FILES=$(find ./src/ -type f -iname *.java -print0 | tr '\0' ' ')
CLASSES=$(find $HOME/.gradle/caches/ -type f -iname *.jar -print0 | tr '\0' ';')

git clone https://${GH_TOKEN}@github.com/bennet0496/technicserver.git -b gh-pages ../technicserver-gh-pages

set -x
mkdir -p ../technicserver-gh-pages/docs/javadoc/${TRAVIS_BRANCH}
javadoc -locale en_US -private -splitindex -use -author -version ${JAVA_FILES} \
    -sourcepath "$PWD/src/main/java;$PWD/src/main/resources;$PWD/src/test/java;$PWD/src/test/resources" \
	-classpath ${CLASSES} -d ../technicserver-gh-pages/docs/javadoc/${TRAVIS_BRANCH}
	
pushd .
cd ../technicserver-gh-pages
pwd
git status
git add docs/javadoc
git status
git commit -am "Travis $TRAVIS_BUILD_NUMBER: Javadoc"
git status
git push -q origin gh-pages

popd
