package com.example.sportapp.data.model

import com.squareup.moshi.Json
data class ArticleDto(
    val id: String,
    val title: String,
    val category: String,
    val summary: String,
    @Json(name= "image") val imageUrl: String?,
    val publishedAt: String,
    val source: String,
    val url: String
)