package com.barengsaya.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.barengsaya.storyapp.data.Repository
import com.barengsaya.storyapp.data.api.response.ListStoryItem
import com.barengsaya.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> get() = _stories

    init {
        fetchStories()
    }
    private fun fetchStories() {
        viewModelScope.launch {
            try {
                _isLoading.postValue(true)
                val response = repository.getStories()
                _stories.postValue(response.listStory)
            } catch (e: Exception) {
                _stories.postValue(emptyList())
            } finally {
                _isLoading.postValue(false)
            }
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
