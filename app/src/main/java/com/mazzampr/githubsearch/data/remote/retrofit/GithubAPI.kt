package com.mazzampr.githubsearch.data.remote.retrofit

import com.mazzampr.githubsearch.data.remote.response.SearchResponse
import com.mazzampr.githubsearch.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubAPI {

    @GET("search/users")
    fun getUserList(@Query("q") query: String) : Call<SearchResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<UserResponse>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<UserResponse>>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<UserResponse>>
}