package com.example.storyapp.data.model.story

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Story(
    val createdAt: String,
    val description: String,
    @PrimaryKey
    val id: String,
    val name: String,
    val photoUrl: String,
    val lat: Double,
    val lon: Double
)