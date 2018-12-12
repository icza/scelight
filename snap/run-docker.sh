#!/usr/bin/env bash

docker run \
  --interactive \
  --tty \
  -e LOCAL_USER_ID=$(id -u $USER) \
  -e LOCAL_GROUP_ID=$(id -g $USER) \
  -v $PWD:/home/snapbuild/work \
  -w=/home/snapbuild/work \
  snapbuild bash
  --rm \

