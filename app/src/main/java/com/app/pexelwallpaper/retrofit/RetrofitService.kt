package com.app.pexelwallpaper.retrofit

import com.app.pexelwallpaper.response.PexelsResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface RetrofitService {
    @GET("search")
    fun searchPhotos(
        @Query("query") query: String,
        @Query("per_page") perPage: Int=10,
        @Query("page") page: Int = 1
    ): Call<PexelsResponse>

    @GET("curated")
    fun trendingWallpapers(@Query("per_page") perPage: Int=42
    ): Call<PexelsResponse>

    companion object {
        private var retrofitService: RetrofitService? = null
        private const val API_KEY = "mVTyjU0cZSEk1OZOBLDxb9x2j5qOlRNOprMCLkIbV76PyNg8tlHqxCKt"
        fun getInstance(): RetrofitService {
            if (retrofitService == null) {
                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }

                val headerInterceptor = Interceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", API_KEY)
                        .build()
                    chain.proceed(request)
                }

                val client = OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(headerInterceptor)
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.pexels.com/v1/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }
}
