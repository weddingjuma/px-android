#!/bin/bash

DEPLOY_COMMAND="[ci deploy]"

# get current directory name
BASEDIR=`dirname $0`
# assumes that next gradle commands will execute in basedir/..
# put current dir in the stack and move current working dir to basedir/..
# stack -> ./scripts ---- /.../.../px-android/
command pushd "$BASEDIR/.." > /dev/null

echo "Evaluating if it was deploy command $DEPLOY_COMMAND"

# Get last 2 commit messages (1 is the merge, second last real commit) in a variable
LAST_GIT_COMMIT=$(git log -2 --pretty=%B)

echo "last git commit has $LAST_GIT_COMMIT"


if [[ "$LAST_GIT_COMMIT" == *"$DEPLOY_COMMAND"* ]]
then
	#git tag -a
	#gradlew publishAar -q
	# Load properties
	. gradle.properties
	TMP_BRANCH="deploy_branch_tag_$version_to_deploy"
	## Tag and push
	git checkout -b $TMP_BRANCH && git push origin $TMP_BRANCH && git tag -a $version_to_deploy -m "travis deployed version $version_to_deploy" && git push origin $TMP_BRANCH --follow-tags && ./gradlew -Pproduction publishAar -q
fi

if [[ "$LAST_GIT_COMMIT" !=  *"$DEPLOY_COMMAND"* ]]
then
	./gradlew publishAar -q
fi
