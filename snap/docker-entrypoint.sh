#!/bin/bash

# Add local user
# Either use the LOCAL_USER_ID if passed in at runtime or fallback

USER_ID=${LOCAL_USER_ID:-9001}
GROUP_ID=${LOCAL_GROUP_ID:-$USER_ID}


echo "Starting with UID : $USER_ID, GID: $GROUP_ID"
groupadd -g ${GROUP_ID} snapbuild
useradd --shell /bin/bash -u ${USER_ID} -g snapbuild -G sudo -o -c "" -m snapbuild
export HOME=/home/snapbuild
chown -R snapbuild /home/snapbuild/work
chown snapbuild:snapbuild /home/snapbuild

exec gosu snapbuild "$@"
