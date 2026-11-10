package com.projectraphael.animegreatsage.knowledge

/**
 * Structured records for the future, source-backed knowledge repository.
 * These are schemas, not a pre-populated or exhaustive content database.
 */
enum class KnowledgeDomain {
    ANIME, MANGA, MANHWA, MANHUA, LIGHT_NOVEL, GAME,
    STUDY, WORLD_CULTURE, COOKING, ENERGY_TRADITIONS,
    SCIENCE, GENERAL_KNOWLEDGE
}

enum class TimelineOrder {
    ORIGINAL_RELEASE, IN_UNIVERSE_CHRONOLOGY, FIRST_TIME_RECOMMENDED
}

enum class CanonStatus {
    CANON, NON_CANON, MIXED, UNKNOWN
}

data class SourceReference(
    val title: String,
    val url: String,
    val publisher: String? = null,
    val accessedAtIso: String? = null
)

data class KnowledgeEntry(
    val id: String,
    val title: String,
    val domain: KnowledgeDomain,
    val summary: String,
    val canonStatus: CanonStatus = CanonStatus.UNKNOWN,
    val sources: List<SourceReference> = emptyList(),
    val lastVerifiedAtIso: String? = null,
    val uncertaintyNote: String? = null
)

data class TimelineItem(
    val id: String,
    val franchiseId: String,
    val title: String,
    val sequenceNumber: Int,
    val orderType: TimelineOrder,
    val medium: String,
    val canonStatus: CanonStatus = CanonStatus.UNKNOWN,
    val explanation: String? = null,
    val spoilerWarning: Boolean = false,
    val sources: List<SourceReference> = emptyList()
)

data class GameJourneyItem(
    val id: String,
    val franchiseId: String,
    val title: String,
    val releaseSequence: Int? = null,
    val storySequence: Int? = null,
    val contentType: String,
    val requiredForMainStory: Boolean = false,
    val dlcOrExpansion: Boolean = false,
    val sources: List<SourceReference> = emptyList()
)
