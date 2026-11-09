package com.morsen.ts3xclient

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.weight
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Dns
import androidx.compose.material.icons.rounded.Headphones
import androidx.compose.material.icons.rounded.HeadsetMic
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.MicOff
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PowerSettingsNew
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.VolumeOff
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val NexusBackground = Color(0xFF0D1110)
private val NexusSurface = Color(0xFF141A17)
private val NexusElevated = Color(0xFF1B2420)
private val NexusCard = Color(0xFF202A25)
private val NexusGreen = Color(0xFF00C878)
private val NexusMint = Color(0xFF29E69A)
private val NexusDeepGreen = Color(0xFF007A4B)
private val NexusText = Color(0xFFF2F5F3)
private val NexusSecondary = Color(0xFFA3ADA8)
private val NexusMuted = Color(0xFF737F78)
private val NexusDanger = Color(0xFFFF6C72)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(13, 17, 16)
        window.navigationBarColor = android.graphics.Color.rgb(13, 17, 16)
        val backend = VoiceSession.backend(applicationContext)
        val localStore = NexusLocalStore(applicationContext)
        setContent { NexusTheme { NexusApp(backend, localStore) } }
    }
}

@Composable
private fun NexusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = NexusGreen,
            onPrimary = Color(0xFF03150D),
            secondary = NexusMint,
            background = NexusBackground,
            surface = NexusSurface,
            onBackground = NexusText,
            onSurface = NexusText,
            error = NexusDanger
        ),
        content = content
    )
}

@Composable
private fun NexusApp(backend: Ts3jBackend, store: NexusLocalStore) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var language by rememberSaveable { mutableStateOf(store.language()) }
    var displayName by rememberSaveable { mutableStateOf(store.displayName()) }
    var servers by remember { mutableStateOf(store.servers()) }
    val restoredEndpoint = remember { backend.activeServerEndpoint() }
    var selectedServer by remember {
        mutableStateOf(
            restoredEndpoint?.let { (host, port) ->
                servers.firstOrNull { it.host.equals(host, ignoreCase = true) && it.port == port }
                    ?: NexusServerProfile(
                        title = backend.serverName()?.takeIf { it.isNotBlank() } ?: host,
                        host = host,
                        port = port
                    )
            }
        )
    }
    var screen by rememberSaveable {
        mutableStateOf(when {
            backend.connected -> "server"
            displayName.isBlank() -> "welcome"
            else -> "servers"
        })
    }
    var channels by remember { mutableStateOf<List<BackendChannel>>(emptyList()) }
    var clients by remember { mutableStateOf<List<BackendClient>>(emptyList()) }
    var activeVoiceIds by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var connected by remember { mutableStateOf(backend.connected) }
    var connectionStatus by remember { mutableStateOf(backend.status) }
    var isBusy by remember { mutableStateOf(false) }
    var playbackMuted by remember { mutableStateOf(false) }
    var selectedChannelId by remember { mutableStateOf<Long?>(backend.currentChannelId) }
    var chatKey by remember { mutableStateOf("") }
    var chatTitle by remember { mutableStateOf("") }
    var chatRecipient by remember { mutableStateOf<Int?>(null) }
    var chatMessages by remember { mutableStateOf<List<BackendChatMessage>>(emptyList()) }
    var chatRevision by remember { mutableIntStateOf(0) }
    var draft by rememberSaveable { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var showServerDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var pendingServer by remember { mutableStateOf<NexusServerProfile?>(null) }
    var channelPassword by rememberSaveable { mutableStateOf("") }
    var pendingChannel by remember { mutableStateOf<BackendChannel?>(null) }
    var deleteCandidate by remember { mutableStateOf<NexusServerProfile?>(null) }

    var micPermissionGranted by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
    }
    val micPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        micPermissionGranted = granted
        if (!granted) errorMessage = tr(language, "برای مکالمه باید دسترسی میکروفون را فعال کنی.", "Microphone permission is required for voice chat.")
    }
    val notificationsPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { }

    val currentChatKey by rememberUpdatedState(chatKey)

    DisposableEffect(backend, scope) {
        backend.setChatListener { changedKey ->
            if (changedKey == currentChatKey) scope.launch { chatRevision++ }
        }
        onDispose { backend.setChatListener(null) }
    }

    LaunchedEffect(backend) {
        var nextSnapshotAt = 0L
        while (true) {
            connected = backend.connected
            connectionStatus = backend.status
            if (backend.connected) {
                // Voice packets are short-lived; sample that tiny in-memory signal
                // more frequently than the heavier channel/member snapshots.
                val latestVoiceIds = backend.activeVoiceClientIds()
                if (latestVoiceIds != activeVoiceIds) activeVoiceIds = latestVoiceIds

                val now = android.os.SystemClock.elapsedRealtime()
                if (now >= nextSnapshotAt) {
                    val latestChannels = backend.channels().sortedWith(
                        compareBy<BackendChannel> { it.parentId }.thenBy { it.order }.thenBy { it.id }
                    )
                    val latestClients = backend.clients()
                    if (latestChannels != channels) channels = latestChannels
                    if (latestClients != clients) clients = latestClients
                    selectedChannelId = backend.currentChannelId ?: selectedChannelId
                    nextSnapshotAt = now + 700L
                }
            } else {
                if (channels.isNotEmpty()) channels = emptyList()
                if (clients.isNotEmpty()) clients = emptyList()
                if (activeVoiceIds.isNotEmpty()) activeVoiceIds = emptySet()
                nextSnapshotAt = 0L
            }
            delay(100)
        }
    }

    LaunchedEffect(chatKey, chatRevision, connected, screen) {
        if (screen == "chat" && chatKey.isNotBlank() && connected) {
            val targetKey = chatKey
            val latestMessages = withContext(Dispatchers.IO) {
                backend.setOpenChat(targetKey)
                backend.chatMessages(targetKey)
            }
            if (chatKey == targetKey) chatMessages = latestMessages
        }
    }

    fun connectTo(profile: NexusServerProfile) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            pendingServer = profile
            micPermission.launch(Manifest.permission.RECORD_AUDIO)
            return
        }
        if (android.os.Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            notificationsPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        scope.launch {
            isBusy = true
            errorMessage = ""
            val result = withContext(Dispatchers.IO) {
                backend.connect(profile.host, profile.port, displayName.ifBlank { "NEXUS User" }, profile.password)
            }
            isBusy = false
            result.onSuccess {
                selectedServer = profile
                screen = "server"
                selectedChannelId = backend.currentChannelId
                runCatching { VoiceSessionService.start(context) }
            }.onFailure {
                errorMessage = it.cause?.message ?: it.message ?: tr(language, "اتصال ناموفق بود.", "Connection failed.")
            }
        }
    }

    LaunchedEffect(pendingServer, micPermissionGranted) {
        val profile = pendingServer ?: return@LaunchedEffect
        if (micPermissionGranted) {
            pendingServer = null
            connectTo(profile)
        }
    }

    fun disconnect() {
        if (isBusy) return
        scope.launch {
            isBusy = true
            val result = withContext(Dispatchers.IO) { backend.disconnect() }
            isBusy = false
            runCatching { VoiceSessionService.stop(context) }
            screen = "servers"
            selectedServer = null
            selectedChannelId = null
            result.onFailure { errorMessage = it.message ?: tr(language, "قطع اتصال کامل نشد.", "The connection did not close cleanly.") }
        }
    }

    fun openChat(key: String, title: String, recipient: Int? = null) {
        chatKey = key
        chatTitle = title
        chatRecipient = recipient
        draft = ""
        chatMessages = emptyList()
        screen = "chat"
    }

    fun join(channel: BackendChannel, password: String? = null) {
        scope.launch {
            isBusy = true
            val result = withContext(Dispatchers.IO) { backend.joinChannel(channel.id, password) }
            isBusy = false
            result.onSuccess {
                selectedChannelId = channel.id
                backend.setOpenChat(null)
                screen = "server"
            }.onFailure { errorMessage = it.message ?: tr(language, "ورود به کانال ممکن نشد.", "Could not join channel.") }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(NexusBackground)) {
        NexusBackdrop()
        CompositionLocalProvider(LocalLayoutDirection provides if (language == "fa") LayoutDirection.Rtl else LayoutDirection.Ltr) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = 18.dp)
            ) {
                NexusTopBar(
                    language = language,
                    connected = connected,
                    connectionStatus = connectionStatus,
                    canGoBack = screen == "chat" || screen == "server",
                    onBack = { if (screen == "chat") backend.setOpenChat(null); screen = if (screen == "chat") "server" else "servers" },
                    onSettings = { if (screen == "chat") backend.setOpenChat(null); screen = "profile" }
                )
                Spacer(Modifier.height(14.dp))
                AnimatedVisibility(
                    visible = errorMessage.isNotBlank(),
                    enter = fadeIn() + slideInVertically { -it / 3 },
                    exit = fadeOut() + slideOutVertically { -it / 3 }
                ) {
                    GlassPanel(modifier = Modifier.fillMaxWidth(), radius = 16.dp) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(errorMessage, color = NexusDanger, fontSize = 12.sp, modifier = Modifier.weight(1f))
                            IconButton(onClick = { errorMessage = "" }) { Icon(Icons.Rounded.Close, null, tint = NexusSecondary) }
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                }

                Box(Modifier.weight(1f)) {
                    AnimatedContent(
                        targetState = screen,
                        transitionSpec = {
                            (fadeIn(tween(220)) + slideInHorizontally(tween(220)) { it / 20 }) togetherWith
                                (fadeOut(tween(160)) + slideOutHorizontally(tween(160)) { -it / 28 })
                        },
                        label = "nexus-screen-transition"
                    ) { targetScreen ->
                    when (targetScreen) {
                        "welcome" -> WelcomePage(
                            language = language,
                            displayName = displayName,
                            onNameChange = { displayName = it },
                            onContinue = {
                                if (displayName.trim().isNotEmpty()) {
                                    store.saveDisplayName(displayName)
                                    screen = "servers"
                                }
                            }
                        )
                        "servers" -> ServersPage(
                            language = language,
                            servers = servers,
                            connected = connected,
                            selectedServer = selectedServer,
                            isBusy = isBusy,
                            onAdd = { showServerDialog = true },
                            onConnect = ::connectTo,
                            onDelete = { deleteCandidate = it },
                            onOpenConnected = { screen = "server" }
                        )
                        "server" -> ServerPage(
                            language = language,
                            server = selectedServer,
                            channels = channels,
                            clients = clients,
                            selectedChannelId = selectedChannelId,
                            activeVoiceIds = activeVoiceIds,
                            isBusy = isBusy,
                            muted = backend.isInputMuted(),
                            playbackMuted = playbackMuted,
                            onJoin = { channel ->
                                if (channel.hasPassword) {
                                    pendingChannel = channel
                                    channelPassword = ""
                                    showPasswordDialog = true
                                } else join(channel)
                            },
                            onChat = { channel -> openChat(backend.channelChatKey(channel.id), channel.name) },
                            onMember = { client -> openChat(backend.privateChatKey(client), client.nickname, client.id) },
                            onMute = { backend.toggleInputMuted().onFailure { errorMessage = it.message.orEmpty() } },
                            onPlaybackMute = { playbackMuted = !playbackMuted; backend.setPlaybackMuted(playbackMuted) },
                            onDisconnect = ::disconnect,
                            onPttStart = { backend.startPushToTalk().onFailure { errorMessage = it.message.orEmpty() } },
                            onPttStop = { backend.stopPushToTalk() }
                        )
                        "chat" -> ChatPage(
                            language = language,
                            title = chatTitle,
                            messages = chatMessages,
                            draft = draft,
                            onDraft = { draft = it },
                            onSend = {
                                val message = draft.trim()
                                if (message.isNotEmpty()) {
                                    val destinationKey = chatKey
                                    val recipientId = chatRecipient
                                    draft = ""
                                    scope.launch {
                                        val result = withContext(Dispatchers.IO) {
                                            backend.sendChatMessage(destinationKey, message, recipientId)
                                        }
                                        result.onFailure {
                                            if (chatKey == destinationKey) draft = message
                                            errorMessage = it.message.orEmpty()
                                        }
                                    }
                                }
                            }
                        )
                        "profile" -> ProfilePage(
                            language = language,
                            displayName = displayName,
                            connected = connected,
                            identity = backend.identityInfo(),
                            onName = { value ->
                                val normalized = value.trim().take(32)
                                displayName = normalized
                                store.saveDisplayName(normalized)
                                if (connected && normalized.isNotBlank()) {
                                    scope.launch {
                                        val result = withContext(Dispatchers.IO) { backend.setClientNickname(normalized) }
                                        result.onFailure { errorMessage = it.message.orEmpty() }
                                    }
                                }
                            },
                            onLanguage = { language = it; store.saveLanguage(it) },
                            onBack = { screen = if (connected) "server" else "servers" }
                        )
                        else -> Unit
                    }
                    }
                }

                if (screen != "welcome" && screen != "chat") {
                    Spacer(Modifier.height(12.dp))
                    NexusDock(
                        language = language,
                        selected = screen,
                        connected = connected,
                        onServers = { screen = if (connected) "server" else "servers" },
                        onProfile = { screen = "profile" },
                        onAdd = { showServerDialog = true }
                    )
                }
            }
        }
    }

    if (showServerDialog) {
        ServerEditorDialog(language, onDismiss = { showServerDialog = false }, onSave = { title, host, port, password ->
            runCatching {
                val item = NexusServerProfile(title = title, host = host, port = port, password = password)
                store.saveServer(item)
                servers = store.servers()
                showServerDialog = false
            }.onFailure { errorMessage = it.message.orEmpty() }
        })
    }

    if (showPasswordDialog) {
        PasswordDialog(language, channelPassword, onValue = { channelPassword = it }, onDismiss = { showPasswordDialog = false }, onJoin = {
            showPasswordDialog = false
            val target = pendingChannel
            pendingChannel = null
            if (target != null) join(target, channelPassword)
        })
    }

    deleteCandidate?.let { candidate ->
        AlertDialog(
            onDismissRequest = { deleteCandidate = null },
            title = { Text(tr(language, "حذف سرور؟", "Remove server?")) },
            text = { Text(tr(language, "این سرور فقط از فهرست محلی حذف می‌شود.", "This only removes the server from your local list.")) },
            confirmButton = { TextButton(onClick = { store.removeServer(candidate.id); servers = store.servers(); deleteCandidate = null }) { Text(tr(language, "حذف", "Remove"), color = NexusDanger) } },
            dismissButton = { TextButton(onClick = { deleteCandidate = null }) { Text(tr(language, "انصراف", "Cancel")) } },
            containerColor = NexusElevated,
            titleContentColor = NexusText,
            textContentColor = NexusSecondary
        )
    }
}

@Composable
private fun NexusBackdrop() {
    // Static ambient light: the interface animates on interaction, not continuously
    // across the whole display, keeping idle GPU work low on mid-range phones.
    Box(Modifier.fillMaxSize().background(
        Brush.verticalGradient(listOf(Color(0xFF101713), NexusBackground, Color(0xFF090D0B)))
    ))
    Canvas(Modifier.fillMaxSize()) {
        val upper = Offset(size.width * 0.78f, size.height * 0.18f)
        val lower = Offset(size.width * 0.10f, size.height * 0.88f)
        drawCircle(
            Brush.radialGradient(listOf(NexusGreen.copy(alpha = 0.24f), Color.Transparent), center = upper, radius = size.width * 0.78f),
            radius = size.width * 0.78f,
            center = upper
        )
        drawCircle(
            Brush.radialGradient(listOf(Color(0xFF14734E).copy(alpha = 0.12f), Color.Transparent), center = lower, radius = size.width * 0.70f),
            radius = size.width * 0.70f,
            center = lower
        )
    }
}

@Composable
private fun NexusMark(modifier: Modifier = Modifier) {
    Box(
        modifier.size(42.dp).shadow(12.dp, RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .background(Brush.linearGradient(listOf(NexusMint, NexusGreen, NexusDeepGreen)))
            .border(1.dp, Color.White.copy(alpha = 0.28f), RoundedCornerShape(15.dp)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.size(22.dp)) {
            val stroke = 3.1.dp.toPx()
            drawLine(Color(0xFF062015), Offset(size.width * 0.18f, size.height * 0.82f), Offset(size.width * 0.18f, size.height * 0.18f), stroke, StrokeCap.Round)
            drawLine(Color(0xFF062015), Offset(size.width * 0.18f, size.height * 0.18f), Offset(size.width * 0.82f, size.height * 0.82f), stroke, StrokeCap.Round)
            drawLine(Color(0xFF062015), Offset(size.width * 0.82f, size.height * 0.82f), Offset(size.width * 0.82f, size.height * 0.18f), stroke, StrokeCap.Round)
        }
    }
}

@Composable
private fun NexusTopBar(language: String, connected: Boolean, connectionStatus: String, canGoBack: Boolean, onBack: () -> Unit, onSettings: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        if (canGoBack) {
            IconButton(onClick = onBack, modifier = Modifier.size(42.dp)) { Icon(Icons.Rounded.ArrowBack, null, tint = NexusText) }
            Spacer(Modifier.width(5.dp))
        } else NexusMark()
        Column(Modifier.weight(1f).padding(horizontal = 10.dp)) {
            Text("NEXUS TS", color = NexusText, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.5.sp)
            Text(tr(language, "ارتباط صوتی، بدون وابستگی ابری", "Voice communities. Locally connected."), color = NexusMuted, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Surface(color = if (connected) NexusGreen.copy(alpha = 0.12f) else Color.White.copy(alpha = 0.045f), shape = CircleShape, border = androidx.compose.foundation.BorderStroke(1.dp, if (connected) NexusGreen.copy(alpha = 0.35f) else Color.White.copy(alpha = 0.08f))) {
            Row(Modifier.padding(horizontal = 10.dp, vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(6.dp).clip(CircleShape).background(if (connected) NexusMint else NexusMuted))
                Spacer(Modifier.width(6.dp))
                Text(if (connected) tr(language, "متصل", "Connected") else tr(language, "آفلاین", "Offline"), color = if (connected) NexusMint else NexusSecondary, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        IconButton(onClick = onSettings, modifier = Modifier.padding(start = 5.dp).size(40.dp)) { Icon(Icons.Rounded.Settings, null, tint = NexusSecondary) }
    }
}

@Composable
private fun GlassPanel(modifier: Modifier = Modifier, radius: androidx.compose.ui.unit.Dp = 22.dp, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = modifier.animateContentSize(tween(180)).clip(RoundedCornerShape(radius))
            .background(Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.075f), Color.White.copy(alpha = 0.028f))))
            .border(1.dp, Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.14f), Color.White.copy(alpha = 0.045f))), RoundedCornerShape(radius))
            .padding(15.dp),
        content = content
    )
}

@Composable
private fun AccentButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true, icon: androidx.compose.ui.graphics.vector.ImageVector? = null) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = NexusGreen, contentColor = Color(0xFF06180F), disabledContainerColor = NexusGreen.copy(alpha = 0.25f), disabledContentColor = Color.White.copy(alpha = 0.5f))
    ) {
        if (icon != null) { Icon(icon, null, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)) }
        Text(text, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}

@Composable
private fun WelcomePage(language: String, displayName: String, onNameChange: (String) -> Unit, onContinue: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Center) {
        NexusMark(Modifier.size(58.dp))
        Spacer(Modifier.height(26.dp))
        Text(tr(language, "به NEXUS TS خوش آمدی", "Welcome to NEXUS TS"), color = NexusText, fontWeight = FontWeight.ExtraBold, fontSize = 27.sp)
        Spacer(Modifier.height(10.dp))
        Text(tr(language, "یک هویت محلی بساز و مستقیم به سرورهای TeamSpeak متصل شو. حساب ابری یا سرویس خارجی لازم نیست.", "Create a local profile and connect directly to TeamSpeak servers. No cloud account or external service is required."), color = NexusSecondary, fontSize = 14.sp, lineHeight = 22.sp)
        Spacer(Modifier.height(28.dp))
        GlassPanel(Modifier.fillMaxWidth()) {
            Text(tr(language, "نام نمایشی", "Display name"), color = NexusSecondary, fontSize = 12.sp)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = displayName, onValueChange = onNameChange, modifier = Modifier.fillMaxWidth(), singleLine = true, placeholder = { Text("Younes") })
            Spacer(Modifier.height(14.dp))
            AccentButton(tr(language, "ساخت پروفایل", "Create profile"), onContinue, Modifier.fillMaxWidth(), enabled = displayName.trim().isNotEmpty())
        }
        Spacer(Modifier.height(18.dp))
        Text(tr(language, "پروفایل و فهرست سرورها روی همین دستگاه ذخیره می‌شوند.", "Your profile and server list stay on this device."), color = NexusMuted, fontSize = 11.sp)
    }
}

@Composable
private fun ServersPage(
    language: String,
    servers: List<NexusServerProfile>,
    connected: Boolean,
    selectedServer: NexusServerProfile?,
    isBusy: Boolean,
    onAdd: () -> Unit,
    onConnect: (NexusServerProfile) -> Unit,
    onDelete: (NexusServerProfile) -> Unit,
    onOpenConnected: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(tr(language, "سرورهای تو", "Your servers"), color = NexusText, fontWeight = FontWeight.Bold, fontSize = 23.sp)
                Text(tr(language, "اتصال مستقیم به شبکه‌های TeamSpeak", "Direct connections to TeamSpeak communities"), color = NexusSecondary, fontSize = 11.sp)
            }
            AccentButton(tr(language, "افزودن", "Add"), onAdd, icon = Icons.Rounded.Add)
        }
        Spacer(Modifier.height(18.dp))
        if (connected && selectedServer != null) {
            GlassPanel(Modifier.fillMaxWidth(), radius = 18.dp) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(42.dp).clip(RoundedCornerShape(14.dp)).background(NexusGreen.copy(alpha = 0.13f)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Rounded.HeadsetMic, null, tint = NexusMint)
                    }
                    Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                        Text(selectedServer.title, color = NexusText, fontWeight = FontWeight.Bold)
                        Text(tr(language, "جلسه فعال", "Active session"), color = NexusMint, fontSize = 11.sp)
                    }
                    TextButton(onClick = onOpenConnected) { Text(tr(language, "باز کردن", "Open"), color = NexusMint) }
                }
            }
            Spacer(Modifier.height(14.dp))
        }
        if (servers.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Rounded.Dns, null, tint = NexusMuted, modifier = Modifier.size(46.dp))
                    Spacer(Modifier.height(12.dp))
                    Text(tr(language, "هنوز سروری اضافه نکردی", "No servers added yet"), color = NexusText, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(5.dp))
                    Text(tr(language, "آدرس سرور داخلی یا عمومی خودت را وارد کن.", "Add an internal or public server address."), color = NexusSecondary, fontSize = 12.sp)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(11.dp), contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 16.dp)) {
                items(servers, key = { it.id }) { server ->
                    GlassPanel(Modifier.fillMaxWidth(), radius = 18.dp) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(42.dp).clip(RoundedCornerShape(14.dp)).background(Color.White.copy(alpha = 0.055f)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Rounded.Dns, null, tint = NexusMint)
                            }
                            Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                                Text(server.title, color = NexusText, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text("${server.host}:${server.port}", color = NexusSecondary, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                            IconButton(onClick = { onDelete(server) }) { Icon(Icons.Rounded.MoreHoriz, null, tint = NexusMuted) }
                        }
                        Spacer(Modifier.height(12.dp))
                        AccentButton(
                            if (connected && selectedServer?.id == server.id) tr(language, "بازگشت به سرور", "Return to server") else tr(language, "اتصال", "Connect"),
                            { if (connected && selectedServer?.id == server.id) onOpenConnected() else onConnect(server) },
                            Modifier.fillMaxWidth(), enabled = !isBusy, icon = Icons.Rounded.HeadsetMic
                        )
                    }
                }
            }
        }
    }
}

private data class ServerListRow(
    val key: String,
    val channel: BackendChannel? = null,
    val member: BackendClient? = null,
    val depth: Int = 0,
    val memberCount: Int = 0
)

@Composable
private fun ServerPage(
    language: String,
    server: NexusServerProfile?,
    channels: List<BackendChannel>,
    clients: List<BackendClient>,
    selectedChannelId: Long?,
    activeVoiceIds: Set<Int>,
    isBusy: Boolean,
    muted: Boolean,
    playbackMuted: Boolean,
    onJoin: (BackendChannel) -> Unit,
    onChat: (BackendChannel) -> Unit,
    onMember: (BackendClient) -> Unit,
    onMute: () -> Unit,
    onPlaybackMute: () -> Unit,
    onDisconnect: () -> Unit,
    onPttStart: () -> Unit,
    onPttStop: () -> Unit
) {
    val membersByChannel = remember(clients) { clients.groupBy { it.channelId } }
    val parentByChannel = remember(channels) { channels.associate { it.id to it.parentId } }
    val rows = remember(channels, clients, membersByChannel, parentByChannel) {
        buildList {
            channels.forEach { channel ->
                val members = membersByChannel[channel.id].orEmpty()
                val depth = channelDepth(channel, parentByChannel)
                add(ServerListRow("channel:${channel.id}", channel = channel, depth = depth, memberCount = members.size))
                members.forEach { member ->
                    add(ServerListRow("member:${channel.id}:${member.id}", member = member, depth = depth + 1))
                }
            }
        }
    }
    var pttPressed by remember { mutableStateOf(false) }
    val latestPttStart by rememberUpdatedState(onPttStart)
    val latestPttStop by rememberUpdatedState(onPttStop)
    val pttScale by animateFloatAsState(
        targetValue = if (pttPressed) 0.975f else 1f,
        animationSpec = tween(110),
        label = "ptt-press-scale"
    )
    Column(Modifier.fillMaxSize()) {
        GlassPanel(Modifier.fillMaxWidth(), radius = 20.dp) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(45.dp).clip(RoundedCornerShape(15.dp)).background(Brush.linearGradient(listOf(NexusGreen.copy(alpha = 0.28f), NexusDeepGreen.copy(alpha = 0.22f)))), contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.HeadsetMic, null, tint = NexusMint, modifier = Modifier.size(23.dp))
                }
                Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                    Text(server?.title ?: tr(language, "سرور TeamSpeak", "TeamSpeak server"), color = NexusText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(server?.let { "${it.host}:${it.port}" } ?: "", color = NexusSecondary, fontSize = 10.sp)
                }
                Text("${clients.size}", color = NexusMint, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text(tr(language, " عضو", " online"), color = NexusSecondary, fontSize = 10.sp)
            }
        }
        Spacer(Modifier.height(13.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(tr(language, "کانال‌ها و اعضا", "Channels & members"), color = NexusText, fontWeight = FontWeight.Bold, fontSize = 17.sp, modifier = Modifier.weight(1f))
            Text(if (isBusy) tr(language, "در حال انجام…", "Working…") else tr(language, "زنده", "LIVE"), color = NexusMint, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(8.dp))
        if (channels.isEmpty()) {
            ChannelLoadingSkeleton(language = language, modifier = Modifier.weight(1f))
        } else {
            // Channels and people share one lazy list; large rooms never compose every member at once.
            LazyColumn(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 8.dp)
            ) {
                items(rows, key = { it.key }) { row ->
                    val channel = row.channel
                    if (channel != null) {
                        GlassPanel(Modifier.fillMaxWidth().padding(start = (row.depth * 12).dp), radius = 16.dp) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(Modifier.size(33.dp).clip(RoundedCornerShape(11.dp)).background(if (selectedChannelId == channel.id) NexusGreen.copy(alpha = 0.17f) else Color.White.copy(alpha = 0.045f)), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Rounded.Headphones, null, tint = if (selectedChannelId == channel.id) NexusMint else NexusSecondary, modifier = Modifier.size(17.dp))
                                }
                                Column(Modifier.weight(1f).padding(horizontal = 10.dp)) {
                                    Text(channel.name, color = NexusText, fontWeight = if (selectedChannelId == channel.id) FontWeight.Bold else FontWeight.Medium, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    Text("${row.memberCount} ${tr(language, "نفر", "members")}${if (channel.hasPassword) "  ·  🔒" else ""}", color = NexusMuted, fontSize = 10.sp)
                                }
                                IconButton(
                                    onClick = { onChat(channel) },
                                    enabled = selectedChannelId == channel.id,
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    Icon(
                                        Icons.Rounded.ChatBubbleOutline,
                                        null,
                                        tint = if (selectedChannelId == channel.id) NexusSecondary else NexusMuted,
                                        modifier = Modifier.size(17.dp)
                                    )
                                }
                                TextButton(onClick = { onJoin(channel) }, enabled = !isBusy) {
                                    Text(if (selectedChannelId == channel.id) tr(language, "داخل", "Joined") else tr(language, "ورود", "Join"), color = if (selectedChannelId == channel.id) NexusMint else NexusSecondary, fontSize = 11.sp)
                                }
                            }
                        }
                    } else {
                        val member = row.member ?: return@items
                        Row(
                            Modifier.fillMaxWidth()
                                .padding(start = (row.depth * 12 + 36).dp, end = 8.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.025f))
                                .clickable { onMember(member) }
                                .padding(horizontal = 11.dp, vertical = 9.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(Modifier.size(7.dp).clip(CircleShape).background(if (activeVoiceIds.contains(member.id)) NexusMint else NexusMuted))
                            Spacer(Modifier.width(9.dp))
                            Text(member.nickname, color = if (activeVoiceIds.contains(member.id)) NexusMint else NexusSecondary, fontSize = 11.sp, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                            if (member.isAway) Text("AFK", color = NexusMuted, fontSize = 9.sp)
                            Spacer(Modifier.width(6.dp))
                            Icon(Icons.Rounded.ChatBubbleOutline, null, tint = NexusMuted, modifier = Modifier.size(13.dp))
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        GlassPanel(Modifier.fillMaxWidth(), radius = 19.dp) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                IconButton(onClick = onMute) { Icon(if (muted) Icons.Rounded.MicOff else Icons.Rounded.Mic, null, tint = if (muted) NexusDanger else NexusMint) }
                Box(
                    Modifier.weight(1f).padding(horizontal = 8.dp).height(48.dp)
                        .graphicsLayer { scaleX = pttScale; scaleY = pttScale }
                        .clip(RoundedCornerShape(16.dp))
                        .background(Brush.horizontalGradient(listOf(NexusDeepGreen, NexusGreen, NexusMint)))
                        .pointerInput(Unit) {
                            detectTapGestures(onPress = {
                                pttPressed = true
                                latestPttStart()
                                try { tryAwaitRelease() } finally {
                                    pttPressed = false
                                    latestPttStop()
                                }
                            })
                        }
                        .semantics { contentDescription = tr(language, "برای صحبت نگه دار", "Hold to talk") },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Mic, null, tint = Color(0xFF052015), modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(tr(language, "برای صحبت نگه دار", "HOLD TO TALK"), color = Color(0xFF052015), fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, letterSpacing = 0.6.sp)
                    }
                }
                IconButton(onClick = onPlaybackMute) { Icon(if (playbackMuted) Icons.Rounded.VolumeOff else Icons.Rounded.VolumeUp, null, tint = if (playbackMuted) NexusDanger else NexusMint) }
            }
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(tr(language, "جلسه صوتی فعال", "Voice session active"), color = NexusSecondary, fontSize = 10.sp, modifier = Modifier.weight(1f))
                TextButton(onClick = onDisconnect) { Icon(Icons.Rounded.PowerSettingsNew, null, tint = NexusDanger, modifier = Modifier.size(15.dp)); Spacer(Modifier.width(5.dp)); Text(tr(language, "قطع اتصال", "Disconnect"), color = NexusDanger, fontSize = 11.sp) }
            }
        }
    }
}

private fun channelDepth(channel: BackendChannel, parentByChannel: Map<Long, Long>): Int {
    var parent = channel.parentId
    var depth = 0
    val visited = mutableSetOf<Long>()
    while (parent > 0 && depth < 4 && visited.add(parent)) {
        depth++
        parent = parentByChannel[parent] ?: 0L
    }
    return depth
}

@Composable
private fun ChannelLoadingSkeleton(language: String, modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "channel-skeleton")
    val pulse by transition.animateFloat(
        initialValue = 0.055f,
        targetValue = 0.13f,
        animationSpec = infiniteRepeatable(tween(950, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "skeleton-pulse"
    )
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(9.dp)) {
        repeat(4) { index ->
            GlassPanel(Modifier.fillMaxWidth(), radius = 15.dp) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(34.dp).clip(RoundedCornerShape(11.dp)).background(NexusMint.copy(alpha = pulse)))
                    Spacer(Modifier.width(11.dp))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                        Box(Modifier.fillMaxWidth(if (index % 2 == 0) 0.56f else 0.42f).height(10.dp).clip(CircleShape).background(NexusText.copy(alpha = pulse)))
                        Box(Modifier.fillMaxWidth(0.28f).height(7.dp).clip(CircleShape).background(NexusSecondary.copy(alpha = pulse * 0.85f)))
                    }
                    Box(Modifier.width(42.dp).height(26.dp).clip(RoundedCornerShape(9.dp)).background(NexusGreen.copy(alpha = pulse)))
                }
            }
        }
        Text(tr(language, "در حال همگام‌سازی کانال‌ها و اعضا…", "Syncing channels and members…"), color = NexusMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
private fun ChatPage(language: String, title: String, messages: List<BackendChatMessage>, draft: String, onDraft: (String) -> Unit, onSend: () -> Unit) {
    val listState = rememberLazyListState()
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.lastIndex)
    }
    Column(Modifier.fillMaxSize().imePadding()) {
        GlassPanel(Modifier.fillMaxWidth(), radius = 17.dp) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.ChatBubbleOutline, null, tint = NexusMint)
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(title, color = NexusText, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(tr(language, "گفت‌وگوی محلی سرور", "Server conversation · local history"), color = NexusMuted, fontSize = 10.sp)
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        if (messages.isEmpty()) {
            Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(tr(language, "هنوز پیامی وجود ندارد. اولین پیام را بفرست.", "No messages yet. Start the conversation."), color = NexusMuted, fontSize = 12.sp)
            }
        } else {
            LazyColumn(state = listState, modifier = Modifier.weight(1f).fillMaxWidth(), reverseLayout = false, verticalArrangement = Arrangement.spacedBy(8.dp), contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp)) {
                items(messages, key = { it.id }) { message ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = if (message.outgoing) Arrangement.End else Arrangement.Start) {
                        Column(Modifier.fillMaxWidth(0.86f).clip(RoundedCornerShape(16.dp)).background(if (message.outgoing) NexusGreen.copy(alpha = 0.13f) else Color.White.copy(alpha = 0.055f)).border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp)).padding(12.dp)) {
                            Text(message.sender, color = if (message.outgoing) NexusMint else NexusSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Text(message.text, color = NexusText, fontSize = 13.sp, lineHeight = 19.sp)
                            Spacer(Modifier.height(4.dp))
                            Text(SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(message.receivedAt)), color = NexusMuted, fontSize = 9.sp)
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        GlassPanel(Modifier.fillMaxWidth(), radius = 17.dp) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BasicTextField(
                    value = draft,
                    onValueChange = onDraft,
                    modifier = Modifier.weight(1f).padding(horizontal = 5.dp, vertical = 10.dp),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = NexusText, fontSize = 13.sp),
                    cursorBrush = Brush.verticalGradient(listOf(NexusMint, NexusGreen)),
                    decorationBox = { inner ->
                        Box {
                            if (draft.isEmpty()) Text(tr(language, "پیامت را بنویس…", "Write a message…"), color = NexusMuted, fontSize = 13.sp)
                            inner()
                        }
                    }
                )
                IconButton(onClick = onSend, enabled = draft.trim().isNotEmpty()) {
                    Icon(Icons.Rounded.Send, null, tint = if (draft.trim().isNotEmpty()) NexusMint else NexusMuted)
                }
            }
        }
    }
}

@Composable
private fun ProfilePage(language: String, displayName: String, connected: Boolean, identity: IdentityInfo, onName: (String) -> Unit, onLanguage: (String) -> Unit, onBack: () -> Unit) {
    var nicknameDraft by rememberSaveable(displayName) { mutableStateOf(displayName) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(tr(language, "پروفایل و تنظیمات", "Profile & settings"), color = NexusText, fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            TextButton(onClick = onBack) { Text(tr(language, "بازگشت", "Done"), color = NexusMint) }
        }
        Spacer(Modifier.height(12.dp))
        GlassPanel(Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(56.dp).clip(CircleShape).background(Brush.linearGradient(listOf(NexusMint, NexusDeepGreen))), contentAlignment = Alignment.Center) {
                    Text(displayName.firstOrNull()?.uppercase() ?: "N", color = Color(0xFF06180F), fontSize = 23.sp, fontWeight = FontWeight.ExtraBold)
                }
                Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                    Text(displayName.ifBlank { "NEXUS User" }, color = NexusText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(tr(language, "پروفایل ذخیره‌شده روی دستگاه", "Device-local profile"), color = NexusSecondary, fontSize = 10.sp)
                }
                Icon(Icons.Rounded.CheckCircle, null, tint = NexusMint)
            }
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = nicknameDraft, onValueChange = { nicknameDraft = it.take(32) }, modifier = Modifier.fillMaxWidth(), singleLine = true, label = { Text(tr(language, "نام نمایشی", "Display name")) })
            Spacer(Modifier.height(10.dp))
            AccentButton(
                tr(language, "ذخیره نام", "Save display name"),
                onClick = { onName(nicknameDraft) },
                modifier = Modifier.fillMaxWidth(),
                enabled = nicknameDraft.trim().isNotBlank() && nicknameDraft.trim() != displayName.trim()
            )
        }
        Spacer(Modifier.height(12.dp))
        GlassPanel(Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Language, null, tint = NexusMint)
                Spacer(Modifier.width(10.dp))
                Text(tr(language, "زبان برنامه", "App language"), color = NexusText, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                TextButton(onClick = { onLanguage(if (language == "fa") "en" else "fa") }) { Text(if (language == "fa") "فارسی / FA" else "English / EN", color = NexusMint) }
            }
            Divider(color = Color.White.copy(alpha = 0.06f))
            Spacer(Modifier.height(12.dp))
            Text(tr(language, "هویت TeamSpeak", "TeamSpeak identity"), color = NexusText, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(5.dp))
            Text(if (identity.saved) tr(language, "هویت محلی آماده است", "Local identity is ready") else tr(language, "هنوز هویتی ساخته نشده؛ هنگام اولین اتصال به‌صورت خودکار ساخته می‌شود.", "No identity yet; one is created automatically on first connection."), color = NexusSecondary, fontSize = 11.sp, lineHeight = 18.sp)
            if (identity.clientUid.isNotBlank()) {
                Spacer(Modifier.height(6.dp))
                Text("UID: ${identity.clientUid.take(20)}…", color = NexusMuted, fontSize = 9.sp)
            }
        }
        Spacer(Modifier.height(12.dp))
        GlassPanel(Modifier.fillMaxWidth()) {
            Text(tr(language, "حریم خصوصی و شبکه", "Privacy & network"), color = NexusText, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text(tr(language, "NEXUS TS برای ورود، پروفایل یا فهرست سرورها به سرویس ابری وابسته نیست. ارتباط شبکه فقط برای اتصال مستقیم به سرور TeamSpeak انتخابی تو و دریافت محتوای همان سرور استفاده می‌شود.", "NEXUS TS does not rely on a cloud service for sign-in, profiles or server discovery. Network access is used for direct connections to the TeamSpeak servers you choose and content served by those servers."), color = NexusSecondary, fontSize = 11.sp, lineHeight = 18.sp)
            Spacer(Modifier.height(7.dp))
            Text(tr(language, "رمز سرورها با AES-GCM و کلید Android Keystore نگهداری می‌شود. هویت TeamSpeak در فضای خصوصی برنامه ذخیره می‌شود.", "Server passwords are protected with AES-GCM and an Android Keystore key. TeamSpeak identity data stays in the app's private storage."), color = NexusMuted, fontSize = 10.sp, lineHeight = 16.sp)
            Text(if (connected) tr(language, "وضعیت فعلی: متصل", "Current state: connected") else tr(language, "وضعیت فعلی: قطع", "Current state: disconnected"), color = NexusMint, fontSize = 10.sp)
        }
    }
}

@Composable
private fun NexusDock(language: String, selected: String, connected: Boolean, onServers: () -> Unit, onProfile: () -> Unit, onAdd: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(bottom = 7.dp).clip(RoundedCornerShape(22.dp))
            .background(Brush.verticalGradient(listOf(Color(0xFF202A25).copy(alpha = 0.92f), Color(0xFF111815).copy(alpha = 0.96f))))
            .border(1.dp, Color.White.copy(alpha = 0.09f), RoundedCornerShape(22.dp)).padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        DockItem(Icons.Rounded.Dns, tr(language, "سرورها", "Servers"), selected == "servers" || selected == "server", onServers)
        Box(Modifier.size(46.dp).clip(RoundedCornerShape(16.dp)).background(Brush.linearGradient(listOf(NexusGreen, NexusDeepGreen))).clickable(onClick = onAdd), contentAlignment = Alignment.Center) {
            Icon(Icons.Rounded.Add, null, tint = Color(0xFF04190F), modifier = Modifier.size(23.dp))
        }
        DockItem(Icons.Rounded.AccountCircle, tr(language, "پروفایل", "Profile"), selected == "profile", onProfile)
    }
}

@Composable
private fun DockItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, active: Boolean, onClick: () -> Unit) {
    Column(Modifier.clip(RoundedCornerShape(14.dp)).clickable(onClick = onClick).padding(horizontal = 13.dp, vertical = 3.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, tint = if (active) NexusMint else NexusMuted, modifier = Modifier.size(20.dp))
        Spacer(Modifier.height(2.dp))
        Text(label, color = if (active) NexusMint else NexusMuted, fontSize = 9.sp, fontWeight = if (active) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
private fun ServerEditorDialog(language: String, onDismiss: () -> Unit, onSave: (String, String, Int, String) -> Unit) {
    var title by rememberSaveable { mutableStateOf("") }
    var host by rememberSaveable { mutableStateOf("") }
    var port by rememberSaveable { mutableStateOf("9987") }
    var password by rememberSaveable { mutableStateOf("") }
    val valid = host.trim().isNotEmpty() && (port.toIntOrNull()?.let { it in 1..65535 } == true)
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(tr(language, "افزودن سرور", "Add a server"), color = NexusText) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(title, { title = it }, label = { Text(tr(language, "نام سرور", "Server name")) }, singleLine = true)
                OutlinedTextField(host, { host = it }, label = { Text(tr(language, "آدرس یا IP", "Hostname or IP")) }, singleLine = true)
                OutlinedTextField(port, { port = it.filter(Char::isDigit).take(5) }, label = { Text(tr(language, "پورت صدا", "Voice port")) }, singleLine = true)
                OutlinedTextField(
                    password,
                    { password = it },
                    label = { Text(tr(language, "رمز سرور (اختیاری)", "Server password (optional)")) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )
                Text(tr(language, "آدرس محلی مثل 192.168.1.20 یا دامنه سرور را وارد کن.", "Enter a local IP such as 192.168.1.20 or a server hostname."), color = NexusMuted, fontSize = 10.sp)
            }
        },
        confirmButton = { TextButton(onClick = { if (valid) onSave(title.trim().ifBlank { host.trim() }, host.trim(), port.toInt(), password) }, enabled = valid) { Text(tr(language, "ذخیره", "Save"), color = NexusMint) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(tr(language, "انصراف", "Cancel"), color = NexusSecondary) } },
        containerColor = NexusElevated,
        titleContentColor = NexusText,
        textContentColor = NexusSecondary
    )
}

@Composable
private fun PasswordDialog(language: String, value: String, onValue: (String) -> Unit, onDismiss: () -> Unit, onJoin: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(tr(language, "رمز کانال", "Channel password"), color = NexusText) },
        text = {
            OutlinedTextField(
                value,
                onValue,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text(tr(language, "رمز ورود", "Password")) },
                visualTransformation = PasswordVisualTransformation()
            )
        },
        confirmButton = { TextButton(onClick = onJoin) { Text(tr(language, "ورود", "Join"), color = NexusMint) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(tr(language, "انصراف", "Cancel"), color = NexusSecondary) } },
        containerColor = NexusElevated,
        titleContentColor = NexusText
    )
}

private fun tr(language: String, fa: String, en: String): String = if (language == "fa") fa else en
