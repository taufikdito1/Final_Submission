package com.example.storyapp.ui.activity.main

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.data.model.story.Story
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainVIewModel @Inject constructor(repository: StoryRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val story: LiveData<PagingData<Story>> = repository.getStory().cachedIn(viewModelScope)
}