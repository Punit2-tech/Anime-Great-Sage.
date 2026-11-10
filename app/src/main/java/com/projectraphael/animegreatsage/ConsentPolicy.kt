package com.projectraphael.animegreatsage.privacy

/**
 * Explicit consent flags. Defaults are off. These values must be persisted only
 * after a clear user action in a consent screen.
 */
data class ConsentPolicy(
    val onlineAiEnabled: Boolean = false,
    val microphoneEnabled: Boolean = false,
    val voiceConversionEnabled: Boolean = false,
    val accountConnectionEnabled: Boolean = false,
    val analyticsEnabled: Boolean = false
)
