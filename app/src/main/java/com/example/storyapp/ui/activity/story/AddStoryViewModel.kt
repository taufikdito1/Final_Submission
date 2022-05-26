package com.example.storyapp.ui.activity.story

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(private val repository: StoryRepository): ViewModel() {

    fun addStory(token: String, imageMultipart: File, description: String) {
        repository.addStory(token, imageMultipart, description)
    }
}