package com.example.sportapp.data.repo

import com.example.sportapp.data.model.ArticleDto
import com.example.sportapp.data.remote.guardian.GuardianApiProvider
import com.example.sportapp.data.remote.guardian.toArticleDto

class ArticleRepository {

    // Pour le moment en dur (tu pourras passer par BuildConfig plus tard)
    private val apiKey: String = "17a3dd53-4583-4e91-a086-450013a7dba5"

//    private val apiKey = BuildConfig.GUARDIAN_API_KEY

    /**
     * category -> section Guardian (par défaut "football").
     * Tu peux aussi passer null pour un flux multi-sections (sport général).
     */
    suspend fun fetchArticles(category: String? = null): List<ArticleDto> {
        val section = category ?: "football"
        val res = GuardianApiProvider.service.search(
            section = section,
            page = null,
            pageSize = 50,
            showFields = "thumbnail,trailText,headline,byline",
            orderBy = "newest",
            apiKey = apiKey
        )
        return res.response.results.map { it.toArticleDto() }
    }
}
