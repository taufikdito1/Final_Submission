package com.example.storyapp.ui.activity.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.data.model.story.Story
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val repository: StoryRepository) : ViewModel() {

    val listStory: LiveData<List<Story>> = repository.listStory

    fun getAllStoryWithMaps(token: String) {
        repository.getStoryWithLocation("Bearer $token")
    }
}