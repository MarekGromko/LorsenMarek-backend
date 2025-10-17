#!/bin/sh
git branch      "$CHECK_GIT_BRANCH";
git checkout    "$CHECK_GIT_BRANCH";
git pull origin "$CHECK_GIT_BRANCH";
if [ -n "$1" ]; then
  $1;
fi;
if [ -n "$KEEP_ALIVE" ]; then
  tail -f /dev/null
fi;