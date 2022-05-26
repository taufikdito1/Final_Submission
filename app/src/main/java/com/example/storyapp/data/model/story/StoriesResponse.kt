package com.example.storyapp.data.model.story

data class StoriesResponse(
    val error: Boolean,
    val listStory: ArrayList<Story>,
    val message: String
)
