package com.projectraphael.animegreatsage.ai

/**
 * Provider boundary for Raphael. Implementations must never transmit conversation
 * content unless the user has explicitly enabled and confirmed the online provider.
 */
data class AiRequest(
    val conversationId: String,
    val message: String
)

data class AiResponse(
    val text: String,
    val providerName: String,
    val isOffline: Boolean
)

interface AiProvider {
    val displayName: String
    suspend fun isAvailable(): Boolean
    suspend fun respond(request: AiRequest): AiResponse
}

enum class AiProviderChoice {
    LOCAL_ONLY,
    ONLINE_WITH_EXPLICIT_CONSENT,
    DISABLED
}

/**
 * Routing policy only. No fake AI response is returned when no engine is installed.
 */
class AiProviderRouter(
    private val localProvider: AiProvider?,
    private val onlineProvider: AiProvider?,
    private val onlineConsentGranted: suspend () -> Boolean
) {
    suspend fun selectedProvider(choice: AiProviderChoice): AiProvider? {
        return when (choice) {
            AiProviderChoice.LOCAL_ONLY ->
                localProvider?.takeIf { it.isAvailable() }

            AiProviderChoice.ONLINE_WITH_EXPLICIT_CONSENT -> {
                if (!onlineConsentGranted()) return null
                localProvider?.takeIf { it.isAvailable() }
                    ?: onlineProvider?.takeIf { it.isAvailable() }
            }

            AiProviderChoice.DISABLED -> null
        }
    }
}
