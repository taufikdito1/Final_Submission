package com.example.storyapp.ui.activity.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.model.login.RegisterResponse
import com.example.storyapp.data.networking.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun registerUser(name: String, email: String, password: String) {
        _isLoading.value = true
        ApiConfig().getApiService().registerUser(name, email, password)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        Log.d(TAG, response.body()?.message.toString())
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.d(TAG, t.message.toString())
                }
            })
    }

    companion object {
        const val TAG = "tag"
    }
}