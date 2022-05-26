package com.example.storyapp.data.model.login

data class UserSession(
    val name: String,
    val token: String,
    val userId: String,
    val isLogin: Boolean
)
