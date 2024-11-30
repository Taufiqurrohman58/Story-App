package com.barengsaya.storyapp.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barengsaya.storyapp.data.Repository
import com.barengsaya.storyapp.data.api.response.ListStoryItem
import kotlinx.coroutines.launch

class MapsViewModel(
    private val storyRepository: Repository
) : ViewModel() {

    private val _storyLocation = MutableLiveData<List<ListStoryItem>?>()
    val storyLocation: LiveData<List<ListStoryItem>?> get() = _storyLocation

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    init {
        getStoryWithLocation()
    }

    private fun getStoryWithLocation() {
        viewModelScope.launch {
            try {
                val response = storyRepository.getStoriesWithLocation()
                if (response.listStory.isNotEmpty()) {
                    _storyLocation.value = response.listStory
                    _error.value = null
                } else {
                    _storyLocation.value = null
                    _error.value = "No stories found"
                }
            } catch (e: Exception) {
                _storyLocation.value = null
                _error.value = e.message
            }
        }
    }
}
