package com.mazzampr.githubsearch.data.remote.retrofit

import com.mazzampr.githubsearch.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val API_KEY = BuildConfig.API_KEY
    private val authInterceptor = Interceptor { chain ->
        val req = chain.request()
        val requestHeaders = req.newBuilder()
            .addHeader("Authorization", "token $API_KEY")
            .build()
        chain.proceed(requestHeaders)
    }
    private val client = OkHttpClient.Builder().addInterceptor(authInterceptor)

    val api:GithubAPI by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()
            .create(GithubAPI::class.java)
    }
}