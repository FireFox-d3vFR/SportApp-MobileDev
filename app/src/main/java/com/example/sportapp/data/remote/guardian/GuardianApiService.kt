package com.example.sportapp.data.remote.guardian

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Docs: https://open-platform.theguardian.com/documentation/
 * On utilise l'endpoint /search avec quelques champs utiles.
 */
interface GuardianApiService {

    @GET("search")
    suspend fun search(
        @Query("section") section: String? = null,              // ex: "football"
        @Query("page") page: Int? = null,
        @Query("page-size") pageSize: Int? = 20,
        // utile: webTitle, webPublicationDate, webUrl, sectionName + fields supplémentaires
        @Query("show-fields") showFields: String = "thumbnail,trailText,headline,byline",
        // tri des plus récents aux plus anciens
        @Query("order-by") orderBy: String = "newest",
        // ta clé d’API
        @Query("api-key") apiKey: String
    ): GuardianSearchResponse
}
