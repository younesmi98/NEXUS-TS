#!/usr/bin/env bash
set -Eeuo pipefail

ROOT="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
UPSTREAM_DIR="$ROOT/upstream/ts3x-client"
EXPECTED_COMMIT="62373967a3fa1c15fdd6dc1e90766d5846e1e81c"
UPSTREAM_URL="https://github.com/ravenmsc/ts3x-client.git"

if [[ ! -d "$UPSTREAM_DIR/.git" ]]; then
  mkdir -p "$ROOT/upstream"
  if [[ -e "$UPSTREAM_DIR" ]]; then
    rmdir "$UPSTREAM_DIR" 2>/dev/null || {
      echo "Refusing to overwrite non-empty $UPSTREAM_DIR" >&2
      exit 2
    }
  fi
  echo "Fetching pinned TeamSpeak protocol source..."
  git clone --filter=blob:none "$UPSTREAM_URL" "$UPSTREAM_DIR"
fi

cd "$UPSTREAM_DIR"
git fetch --quiet origin "$EXPECTED_COMMIT"
git checkout --quiet --detach "$EXPECTED_COMMIT"
ACTUAL="$(git rev-parse HEAD)"
if [[ "$ACTUAL" != "$EXPECTED_COMMIT" ]]; then
  echo "Upstream source mismatch: expected $EXPECTED_COMMIT, got $ACTUAL" >&2
  exit 3
fi

for module in protocol ts3j; do
  [[ -f "$UPSTREAM_DIR/$module/build.gradle.kts" ]] || {
    echo "Missing upstream module: $module" >&2
    exit 4
  }
done

echo "Pinned TeamSpeak source ready: $ACTUAL"
