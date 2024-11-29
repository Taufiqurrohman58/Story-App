package com.barengsaya.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.barengsaya.storyapp.data.Repository
import com.barengsaya.storyapp.data.api.response.ListStoryItem
import com.barengsaya.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    val stories: LiveData<List<ListStoryItem>> = liveData {
        try {
            _isLoading.postValue(true)
            val response = repository.getStories()
            emit(response.listStory)
        } catch (e: Exception) {
            emit(emptyList())
        } finally {
            _isLoading.postValue(false)
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}
