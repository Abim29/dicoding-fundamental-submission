package com.bangkit.githubuser.api

import com.bangkit.githubuser.model.Follow
import com.bangkit.githubuser.model.GithubResponse
import com.bangkit.githubuser.model.UserDetail
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getUsers(
        @Query("q") username : String
    ) : Call<GithubResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ) : Call<UserDetail>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ) : Call<List<Follow>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String
    ) : Call<List<Follow>>
}