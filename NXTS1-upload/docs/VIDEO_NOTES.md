# Engineering notes from the supplied 31-point performance video

I reviewed the full 121-second vertical video. It is a general web/app performance checklist (its final card names items such as skeleton screens, WebP/AVIF, CDN, background queues, Redis caching, database indexes and code splitting). It is not a TeamSpeak protocol tutorial. The following are the points that genuinely transfer to this native Android client.

## Applied in final1

- **Avoid blank loading states:** the server screen now shows lightweight animated channel skeletons while the first channel/member snapshot is arriving.
- **Avoid duplicate work:** one process-owned TS3X backend/session is reused. Compose does not issue TeamSpeak commands from individual channel/member rows; it reads the backend's in-memory event snapshots.
- **Separate update frequencies:** the short-lived incoming-voice activity signal is sampled at 100 ms, while the larger channel/member snapshots are sampled at 700 ms. Both are local cache reads, not network requests.
- **Virtualize long UI lists:** server profiles, channels and messages use `LazyColumn`. Member groups and channel-parent lookups are memoized instead of rebuilding a full search for each channel row.
- **Keep database work bounded:** channel/private chat history is retained per conversation with a 500-message limit and an index on `(server_key, conversation_key, id)`. The UI reads it from an IO dispatcher.
- **Keep work off the UI thread:** connection, channel joins, message sends and local chat reads run away from the main thread; Compose state changes are returned on the main coroutine.
- **Avoid unused dependencies and remote calls:** the NEXUS UI does not ship image/CDN clients, analytics, cloud auth or a remote server directory. Its runtime connection is direct to the selected TeamSpeak server.
- **Low-end-device restraint:** translucent gradient/border surfaces and small transitions are used instead of a continuous real-time blur. The Galaxy A13 remains a required physical-device test target.

## Deliberately not copied from the web checklist

- Debounced autocomplete is useful only if a search box sends remote requests. final1 has no cloud search or autocomplete endpoint.
- HTTP API pagination is not the same as TeamSpeak's live channel events. The current server channel snapshot is the server's current event-driven view; the app virtualizes rendering but does not invent REST pagination.
- WebP/AVIF/CDN guidance applies when the app fetches artwork. final1 currently avoids external profile/banner artwork rather than making the voice/channel experience depend on third-party hosts.
- Redis, HTTP connection pools and JavaScript code splitting do not belong in a local-first native client with no NEXUS backend. The long-lived TeamSpeak session is reused while connected.

## Physical validation still required

Run the checklist in `DEVICE_TEST_PLAN.md` on the Galaxy A13 and at least one reachable LAN TeamSpeak server. Source-level optimization is not proof of smooth runtime behavior on a specific device or carrier.
