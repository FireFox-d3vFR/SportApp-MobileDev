package com.example.sportapp.data.remote.guardian

import com.example.sportapp.data.model.ArticleDto

// ----- Réponse brute Guardian -----

data class GuardianSearchResponse(
    val response: GuardianResponseBody
)

data class GuardianResponseBody(
    val status: String,
    val userTier: String?,
    val total: Int?,
    val startIndex: Int?,
    val pageSize: Int?,
    val currentPage: Int?,
    val pages: Int?,
    val results: List<GuardianResult>
)

data class GuardianResult(
    val id: String,
    val type: String?,
    val sectionId: String?,
    val sectionName: String?,           // ex: "Football"
    val webPublicationDate: String?,    // ISO8601
    val webTitle: String,               // titre visible
    val webUrl: String,                 // URL publique
    val apiUrl: String?,
    val fields: GuardianFields?         // champs additionnels via show-fields
)

data class GuardianFields(
    val thumbnail: String?,             // petite image
    val trailText: String?,             // chapeau (souvent en HTML)
    val headline: String?,              // titre alternatif
    val byline: String?                 // auteur(s)
)

// ----- Mapper -> ArticleDto -----

/**
 * Le trailText peut contenir un peu de HTML. On le désactive ici très basiquement,
 * libre à toi de faire mieux dans la UI (HtmlCompat) si tu préfères.
 */
private fun String.stripBasicHtml(): String =
    this.replace(Regex("<[^>]*>"), "").replace("&nbsp;", " ").trim()

fun GuardianResult.toArticleDto(): ArticleDto {
    val title = fields?.headline?.takeIf { !it.isNullOrBlank() } ?: webTitle
    val summary = when {
        !fields?.trailText.isNullOrBlank() -> fields!!.trailText!!.stripBasicHtml()
        else -> webTitle
    }

    return ArticleDto(
        id = id,
        title = title,
        category = sectionName ?: (sectionId ?: "Sports"),
        summary = summary,
        imageUrl = fields?.thumbnail,
        publishedAt = webPublicationDate ?: "",
        source = "The Guardian",
        url = webUrl
    )
}
