#!/usr/bin/env sh
set -eu
ROOT="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
if [ ! -f "$ROOT/upstream/ts3x-client/gradlew" ]; then
  echo "Missing pinned TeamSpeak source. Run: ./bootstrap.sh" >&2
  exit 1
fi
exec "$ROOT/upstream/ts3x-client/gradlew" "$@"
