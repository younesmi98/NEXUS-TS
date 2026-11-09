# NEXUS TS final1 — device and network test plan

Run these checks on a physical phone before distributing a release build.

## Primary phone

- Samsung Galaxy A13, Android 14 / One UI 6.x or later available on the device.
- Portrait layout, system font scaling 1.0x and 1.2x.
- Confirm Persian RTL and English LTR; verify channel names, IP addresses, ports and mixed Latin/Persian text.
- Confirm the app stays responsive while scrolling a large channel tree and a busy chat.

## Connection scenarios

- Reachable private IPv4 server address on the same LAN/hotspot.
- Reachable private IPv6 address, if the server network offers it.
- Hostname via the active carrier/Wi-Fi DNS.
- Correct and incorrect server passwords; password-protected channel.
- Disconnect/reconnect; switch from Wi-Fi to mobile data while the session is interrupted.
- Verify voice continues with the screen off and that the persistent notification can disconnect the session.
- Test push-to-talk, microphone mute, playback mute, incoming/outgoing channel messages and private messages.

## Weak network and low-resource checks

- Test with latency and packet loss introduced by a router/network emulator where available.
- Test on constrained mobile data (3G/low-bandwidth profile); note that TeamSpeak voice quality depends on server codec/bitrate and network loss.
- Test server reachability while the public internet is unavailable but the local route remains available.
- Confirm that an unreachable server is reported as unavailable rather than silently treated as connected.
- Watch battery, CPU and memory during a 30-minute voice session on the A13.

## Acceptance criteria

- No crash or ANR during the above checks.
- No repeated connection attempts caused by ordinary Compose recomposition.
- Saved server passwords remain encrypted at rest; the app does not require an external NEXUS/Firebase/Supabase service.
- A local network outage is reported accurately. The client must not claim it can bypass blocked routes or ports.
