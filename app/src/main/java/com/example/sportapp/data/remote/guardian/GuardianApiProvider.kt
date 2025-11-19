package com.example.sportapp.data.remote.guardian

import com.example.sportapp.data.remote.ApiClient

object GuardianApiProvider {
    val service: GuardianApiService by lazy {
        ApiClient.api.create(GuardianApiService::class.java)
    }
}