package com.example.sportapp.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiClient {
    private const val BASE_URL = "https://content.guardianapis.com/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Intercepteur pour tracer les erreurs réseau
    private val errorTracer = Interceptor { chain ->
        try {
            chain.proceed(chain.request())
        } catch (t: Throwable) {
            // Log lisible dans Logcat si une exception réseau survient
            android.util.Log.e("ApiClient", "Network error", t)
            throw t
        }
    }

    private val okHttp = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(errorTracer)
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val api: retrofit2.Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttp)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
}