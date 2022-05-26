package com.example.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.example.storyapp.data.database.StoryDatabase
import com.example.storyapp.data.model.story.AddResponse
import com.example.storyapp.data.model.story.StoriesResponse
import com.example.storyapp.data.model.story.Story
import com.example.storyapp.data.networking.ApiService
import com.example.storyapp.helper.UserPreference
import com.example.storyapp.helper.wrapEspressoIdlingResource
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,
    private val preference: UserPreference
) {
    private val _toastMessage = MutableLiveData<String>()

    private val _listStory = MutableLiveData<List<Story>>()
    val listStory: LiveData<List<Story>> = _listStory

    @OptIn(ExperimentalPagingApi::class)
    fun getStory(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, preference),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    fun getStoryWithLocation(token: String) {
        wrapEspressoIdlingResource {
            apiService.getListStoryWithLocation(token, 1)
                .enqueue(object : Callback<StoriesResponse> {
                    override fun onResponse(
                        call: Call<StoriesResponse>,
                        response: Response<StoriesResponse>
                    ) {
                        if (response.isSuccessful) {
                            _listStory.postValue(response.body()?.listStory)
                        }
                    }

                    override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                        Log.d(TAG, t.message.toString())
                    }

                })
        }

    }

    fun addStory(token: String, photo: File, description: String) {
        wrapEspressoIdlingResource {
            val descriptionText = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = photo.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                photo.name,
                requestImageFile
            )
            val service = apiService.uploadImage(token, imageMultipart, descriptionText)
            service.enqueue(object : Callback<AddResponse> {
                override fun onResponse(call: Call<AddResponse>, response: Response<AddResponse>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            _toastMessage.value = responseBody.message
                        } else {
                            _toastMessage.value = response.message()
                        }
                    }
                }

                override fun onFailure(call: Call<AddResponse>, t: Throwable) {
                    _toastMessage.value = "Failed to instance retrofit"
                }

            })
        }

    }

    companion object {
        const val TAG = "tag"
    }

}