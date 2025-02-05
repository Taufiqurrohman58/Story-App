package com.barengsaya.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.barengsaya.storyapp.data.api.response.DetailResponse
import com.barengsaya.storyapp.data.api.retrofit.ApiService
import com.barengsaya.storyapp.data.api.response.ErrorResponse
import com.barengsaya.storyapp.data.api.response.ListStoryItem
import com.barengsaya.storyapp.data.api.response.LoginResponse
import com.barengsaya.storyapp.data.api.response.RegisterResponse
import com.barengsaya.storyapp.data.api.response.StoryResponse
import com.barengsaya.storyapp.data.pref.UserModel
import com.barengsaya.storyapp.data.pref.UserPreference
import com.barengsaya.storyapp.view.upload.reduceFileImage
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class Repository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return try {
            apiService.register(name, email, password)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            throw Exception(errorResponse?.message ?: "Unknown error")
        }
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    fun getStoriesPaging(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { StoryPagingSource(apiService, token) }
        ).liveData
    }


    suspend fun getDetailStory(id: String): DetailResponse {
        return apiService.getDetailId("Bearer ${userPreference.getSession().first().token}", id)
    }
    suspend fun getStoriesWithLocation(): StoryResponse {
        val token = "Bearer ${userPreference.getSession().first().token}"
        return apiService.getStoriesWithLocation(token)
    }

    suspend fun uploadStory(file: File, description: String, latitude: Double?, longitude: Double?) {
        val token = userPreference.getSession().first().token
        val reducedFile = file.reduceFileImage()
        val requestFile = reducedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("photo", reducedFile.name, requestFile)
        val desc = description.toRequestBody("text/plain".toMediaType())

        val latitudePart = latitude?.toString()?.toRequestBody("text/plain".toMediaType())
        val longitudePart = longitude?.toString()?.toRequestBody("text/plain".toMediaType())

        apiService.uploadStory("Bearer $token", body, desc, latitudePart, longitudePart)
    }



    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(userPreference, apiService)
            }.also { instance = it }
    }
}