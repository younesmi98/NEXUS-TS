package com.morsen.ts3xclient

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import org.json.JSONArray
import org.json.JSONObject
import java.security.KeyStore
import java.util.UUID
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

data class NexusServerProfile(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val host: String,
    val port: Int = 9987,
    val password: String = ""
)

/** Local-first profile and server storage. Server passwords are AES-GCM encrypted with Android Keystore. */
class NexusLocalStore(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences("nexus_ts_local", Context.MODE_PRIVATE)
    private val cipher = KeystoreCipher()

    fun displayName(): String = prefs.getString("display_name", "")?.trim().orEmpty()
    fun saveDisplayName(value: String) {
        prefs.edit().putString("display_name", value.trim().take(32)).apply()
    }
    fun language(): String = prefs.getString("language", "fa")?.takeIf { it == "fa" || it == "en" } ?: "fa"
    fun saveLanguage(value: String) { prefs.edit().putString("language", if (value == "en") "en" else "fa").apply() }

    @Synchronized
    fun servers(): List<NexusServerProfile> = runCatching {
        val raw = prefs.getString("servers", "[]") ?: "[]"
        val array = JSONArray(raw)
        buildList {
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val host = obj.optString("host").trim()
                val port = obj.optInt("port", 9987)
                if (host.isBlank() || port !in 1..65535) continue
                val encrypted = obj.optString("password")
                val password = if (encrypted.isBlank()) "" else cipher.decrypt(encrypted).orEmpty()
                add(NexusServerProfile(
                    id = obj.optString("id").ifBlank { UUID.randomUUID().toString() },
                    title = obj.optString("title").ifBlank { host },
                    host = host,
                    port = port,
                    password = password
                ))
            }
        }
    }.getOrDefault(emptyList())

    @Synchronized
    fun saveServer(server: NexusServerProfile) {
        val all = servers().toMutableList()
        val index = all.indexOfFirst { it.id == server.id }
        if (index >= 0) all[index] = server else all.add(server)
        persistServers(all)
    }

    @Synchronized
    fun removeServer(id: String) { persistServers(servers().filterNot { it.id == id }) }

    private fun persistServers(items: List<NexusServerProfile>) {
        val json = JSONArray()
        items.forEach { item ->
            json.put(JSONObject().apply {
                put("id", item.id)
                put("title", item.title.take(64))
                put("host", item.host.trim())
                put("port", item.port)
                put("password", if (item.password.isBlank()) "" else cipher.encrypt(item.password))
            })
        }
        check(prefs.edit().putString("servers", json.toString()).commit()) { "Could not save server profiles" }
    }
}

private class KeystoreCipher {
    private val alias = "nexus.ts.server-passwords.v1"

    private fun key(): SecretKey {
        val store = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        (store.getKey(alias, null) as? SecretKey)?.let { return it }
        val generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        generator.init(KeyGenParameterSpec.Builder(
            alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setRandomizedEncryptionRequired(true)
            .build())
        return generator.generateKey()
    }

    fun encrypt(value: String): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key())
        val encrypted = cipher.doFinal(value.toByteArray(Charsets.UTF_8))
        val payload = cipher.iv + encrypted
        return Base64.encodeToString(payload, Base64.NO_WRAP)
    }

    fun decrypt(value: String): String? = runCatching {
        val payload = Base64.decode(value, Base64.NO_WRAP)
        require(payload.size > 12) { "Invalid encrypted password" }
        val iv = payload.copyOfRange(0, 12)
        val body = payload.copyOfRange(12, payload.size)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, key(), GCMParameterSpec(128, iv))
        String(cipher.doFinal(body), Charsets.UTF_8)
    }.getOrNull()
}
