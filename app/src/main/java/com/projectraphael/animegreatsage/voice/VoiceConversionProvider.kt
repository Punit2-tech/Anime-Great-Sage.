package com.projectraphael.animegreatsage.voice

/**
 * Contract for a future real-time voice-conversion engine.
 * This module does not yet capture microphone audio, clone voices, or alter call audio.
 */
data class VoiceProfile(
    val id: String,
    val displayName: String,
    val description: String,
    val exactReplicaAuthorized: Boolean = false
)

interface VoiceConversionProvider {
    val engineName: String
    fun isAvailable(): Boolean
    fun availableProfiles(): List<VoiceProfile>
    suspend fun start(profileId: String): Result<Unit>
    suspend fun stop()
}

enum class VoiceUseContext {
    IN_APP_VOICE_CHAT, SUPPORTED_VOIP_INTEGRATION, SYSTEM_PHONE_CALL
}
