# NEXUS TS — final1

NEXUS TS is a local-first Android TeamSpeak client. It combines a native Compose interface inspired by Discord's channel model with a restrained iOS-style dark glass design, while using one real TeamSpeak transport rather than trying to make a Discord backend speak the TeamSpeak protocol.

## What final1 contains

- Native Android UI written in Kotlin and Jetpack Compose.
- Persian (RTL) and English (LTR) interface, with local language selection.
- Local display profile; its nickname is sent to TeamSpeak when connecting.
- Saved server profiles with AES-GCM encrypted passwords using Android Keystore.
- Direct connections to reachable TeamSpeak 3 and TeamSpeak 6 servers, including private/internal IP addresses.
- Live channel/member view, channel and private chat, local chat history, microphone mute, playback mute and hold-to-talk.
- Process-owned foreground voice session, so audio can continue while the app is backgrounded.
- No NEXUS cloud account, Firebase, Supabase, analytics, external server directory or mandatory external API.

## Final architecture decision

| Layer | Choice | Why |
|---|---|---|
| Android UI | Custom Jetpack Compose NEXUS interface | Native interactions, RTL/LTR and small-screen performance |
| TS3/TS6 protocol and session | Pinned TS3X Client source (`protocol` + `ts3j`) | A single tested transport implementation for the first milestone |
| Audio | TS3X audio bridge + Concentus/Opus | Avoids mixing incompatible audio/protocol engines |
| UI / feature references | PocketTS, WebSpeak3 and Stoat | Used as research references, not copied as separate networking stacks |
| Local persistence | App-private preferences + SQLite chat history | No cloud dependency; only necessary local data is retained |
| Secret storage | Android Keystore + AES-GCM for saved server passwords | Password ciphertext is protected by a device-bound key |

The upstream TS3X source is pinned to commit `62373967a3fa1c15fdd6dc1e90766d5846e1e81c`. Its `protocol` and `ts3j` modules are loaded as Gradle subprojects from `upstream/ts3x-client`. Gradle also copies the required TS3X session, audio, notification and resolver support classes into a generated source directory, while explicitly excluding the original TS3X `MainActivity`. NEXUS TS supplies its own `MainActivity`, local profile store and resources. The upstream checkout is left untouched.

PocketTS is an excellent reference for TS6-specific UX and features, and WebSpeak3 is useful for understanding browser gateway architecture. Their transports are not merged into this APK: combining multiple unrelated TeamSpeak protocol engines would create duplicate connection/audio implementations and increase risk.

## Get the pinned source and build

This source archive does not bundle the large upstream protocol repository. The bootstrap script fetches the exact pinned commit and verifies it before Gradle is run.

```bash
chmod +x bootstrap.sh gradlew
./bootstrap.sh
./gradlew :protocol:test :ts3j:test :app:lintDebug :app:assembleDebug --stacktrace
```

Debug APK:

```text
app/build/outputs/apk/debug/app-debug.apk
```

On Windows, run `bash bootstrap.sh` from Git Bash or WSL, then use `gradlew.bat` from the project root.

Requirements: JDK 17, Android SDK Platform 35, Android Build Tools 35.0.0, Git and network access to GitHub/Maven repositories for the initial source and dependency download.

## Network behavior during an international-internet outage

The app itself does not need a remote NEXUS service to launch, create a profile, show saved servers or start a direct connection. Save the actual LAN/internal IP address of the TeamSpeak server when possible; this avoids relying on external DNS. TeamSpeak traffic still needs a working route to that server and its configured ports. The app cannot bypass an operator route restriction or a blocked UDP port. Server-hosted banners and other external artwork may not load while their hosts are unreachable, but core voice/channel traffic does not depend on those images.

## Performance and implementation notes from the supplied video

The recommendations that apply to this Android project are reflected in the implementation: channel/member lists use Compose `LazyColumn`; member grouping and channel-parent maps are memoized instead of rebuilt for every row; the UI polls in-memory backend snapshots rather than issuing new TeamSpeak commands every render; chat history is bounded and indexed by server/conversation; the channel history screen only loads a bounded local window; and no unnecessary network search, remote profile lookup or third-party analytics package is added. UI state updates from background send operations are marshalled back to the main coroutine.

The video is general optimization guidance, not a TeamSpeak protocol specification. Its web-specific advice (browser bundle splitting, HTTP API pagination, image CDN handling) is not presented as implemented where it does not apply to this native client.

## Publish to `younesmi98/NEXUS-TS`

The repository already has a `final1` branch with existing setup files. Upload this project at the repository root while preserving those files; do not force-push or replace the branch history. From Termux, clone the existing branch, copy this project's files into the clone, then commit and push:

```bash
git clone --branch final1 https://github.com/younesmi98/NEXUS-TS.git NEXUS-TS-upload
cp -Rn . NEXUS-TS-upload/
cd NEXUS-TS-upload
git add -A
git commit -m "Add NEXUS TS final1"
git push origin final1
```

The included GitHub Actions workflow runs protocol tests, app lint and a debug APK build on that branch. It publishes the APK as the `NEXUS-TS-final1-debug` workflow artifact.

## Important first-version boundaries

- One active TeamSpeak connection at a time.
- TeamSpeak nickname is a display name, while the cryptographic TeamSpeak identity is a separate persistent identity created on first connection.
- TeamSpeak's normal private messages are delivered to online users; this is not Discord-style global messaging or a cloud friends system.
- PocketTS-only capabilities not exposed by the selected TS3X transport are not claimed as implemented.
- No APK has been built or runtime-tested by this archive-generation environment. Run the checks above (or the included GitHub Actions workflow) before treating a build as validated.

## Licenses and attribution

The original NEXUS TS application code is provided under Apache-2.0. The bootstrap script retrieves the TS3X Client source at the exact commit listed in `UPSTREAM.lock`; its license, NOTICE, ts3j license and third-party notices remain in the fetched upstream tree. See `NOTICE` and the upstream repository for complete notices. TeamSpeak is a trademark of TeamSpeak Systems GmbH. NEXUS TS is unofficial and is not affiliated with or endorsed by TeamSpeak.
