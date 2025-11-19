package com.example.sportapp.data.remote

import com.example.sportapp.data.remote.guardian.GuardianSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // GET /search?section=football&show-fields=thumbnail,trailText&order-by=newest&api-key=...
    @GET("search")
    suspend fun getGuardianFootball(
        @Query("section") section: String = "football",
        @Query("show-fields") fields: String = "thumbnail,trailText",
        @Query("order-by") orderBy: String = "newest",
        @Query("page") page: Int? = null,
        @Query("page-size") pageSize: Int? = 20,
        @Query("api-key") apiKey: String
    ): GuardianSearchResponse
}