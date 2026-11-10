package com.projectraphael.animegreatsage.progress

/**
 * Progress record shapes. Persistence and editing UI are separate implementation tasks.
 * Do not write progress without the user's explicit action.
 */
enum class ProgressKind { ANIME_EPISODE, MOVIE, SPECIAL, GAME_MISSION, DLC }

data class UserProgress(
    val id: String,
    val franchiseId: String,
    val itemId: String,
    val itemTitle: String,
    val kind: ProgressKind,
    val completed: Boolean,
    val note: String? = null,
    val updatedAtEpochMillis: Long
)
