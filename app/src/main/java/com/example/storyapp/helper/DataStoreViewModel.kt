package com.example.storyapp.helper

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.model.login.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataStoreViewModel @Inject constructor(private val preference: UserPreference): ViewModel() {

    fun setSession(user: UserSession) {
        viewModelScope.launch {
            preference.setUser(user)
        }
    }

    fun getSession(): LiveData<UserSession> {
        return preference.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            preference.logout()
        }
    }

}