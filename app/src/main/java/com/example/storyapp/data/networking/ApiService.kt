package com.example.storyapp.data.networking

import com.example.storyapp.data.model.login.LoginResponse
import com.example.storyapp.data.model.login.RegisterResponse
import com.example.storyapp.data.model.story.AddResponse
import com.example.storyapp.data.model.story.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    suspend fun getListStory(
        @Header("Authorization") auth: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoriesResponse

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") auth: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<AddResponse>

    @GET("stories")
    fun getListStoryWithLocation(
        @Header("Authorization") auth: String,
        @Query("location") location: Int
    ): Call<StoriesResponse>
}