# NEXUS TS architecture — final1

## Runtime layout

```text
MainActivity (NEXUS Compose UI)
  ├─ local profile + saved-server store (NexusLocalStore)
  ├─ server / channel / member / chat screens
  └─ VoiceSession.backend()
       └─ TS3X session bridge (Ts3jBackend)
            ├─ protocol module
            ├─ ts3j transport module
            ├─ TS3 audio engine + Concentus/Opus
            └─ process-owned VoiceSessionService
```

The UI and transport have one boundary: `Ts3jBackend`. The UI reads local event-driven snapshots (`channels()`, `clients()`, `activeVoiceClientIds()`) and invokes explicit operations for connect, join, chat and audio. Composable rows do not create protocol connections or send refresh commands.

## Why the selected combination is intentionally asymmetric

| Project | Role in final1 | Code copied into this app? |
|---|---|---:|
| TS3X Client | Actual TS3/TS6 protocol, audio, identity and Android session services | Yes, pinned `protocol` and `ts3j` modules plus required non-UI session/service classes |
| PocketTS | Reference for a focused TeamSpeak mobile feature set and TS6 interaction patterns | No |
| WebSpeak3 | Reference for the channel/member model, server interaction patterns and Persian-capable responsive UX | No |
| Stoat Android | Reference for Discord-like mobile navigation and channel hierarchy | No |
| DiscordJetpackCompose | Historical UI layout reference only | No |

Only one TeamSpeak transport is used. Merging TS3X, PocketTS and WebSpeak3 transport implementations would create duplicate identity, connection and audio lifecycles. The UI references inform product decisions; they are not additional network backends.

## Local identity model

- The NEXUS display profile stores the chosen nickname and language locally.
- The TeamSpeak cryptographic identity is separate from the display name. TS3X creates and persists it on first connection and reuses it on later connections.
- Saved server passwords are encrypted with AES-GCM using a device-bound Android Keystore key.
- Server profiles and chat history do not require an NEXUS cloud account.

## Network model

NEXUS TS connects directly to the user-entered TeamSpeak host and port. A private IPv4/IPv6 address on the same reachable LAN is preferred for a national-network outage. DNS/SRV/TSDNS resolution is performed against the active Android network where needed. There is no NEXUS sign-in endpoint, external server directory or relay service in the normal connection path.

This is not a route bypass: if the operator blocks the route, the server IP, UDP voice port or required TCP transport, the client cannot restore connectivity by itself. The server and its configured ports must remain reachable from the phone's current network.

## UI system

- Graphite/near-black surfaces with emerald and mint accents.
- Translucent gradient panels, restrained borders and rounded controls for an iOS-inspired glass treatment.
- Animated screen transitions, content-size changes, skeleton loading and tactile Push-to-Talk press feedback.
- Static ambient background rather than a continuous full-screen animation, to reduce idle rendering cost on mid-range devices.
- Persian RTL and English LTR layouts.

The app intentionally uses lightweight translucent gradients rather than a permanent real-time blur. This keeps the glass effect while avoiding an always-on GPU cost on devices such as the Galaxy A13.

## Data and performance boundaries

- Channel/member state is read from the backend's in-memory event cache.
- Channel/member snapshots are checked at a slower interval than the short-lived voice-activity signal.
- Long server and chat lists are composed lazily.
- Per-channel member groups and parent-channel maps are memoized.
- Chat history is local, indexed by server and conversation, and bounded to 500 records per conversation.
- Connection, join, chat-send and SQLite reads run away from the UI thread.
- No image CDN, Redis cache, remote autocomplete or HTTP pagination layer is introduced where the TeamSpeak protocol does not require one.
