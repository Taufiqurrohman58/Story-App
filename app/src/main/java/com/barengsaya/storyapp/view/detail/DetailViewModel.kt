package com.barengsaya.storyapp.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barengsaya.storyapp.data.Repository
import com.barengsaya.storyapp.data.api.response.Story
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: Repository) : ViewModel() {

    private val _story = MutableLiveData<Story?>()
    val story: LiveData<Story?> get() = _story

    fun getDetailStory(id: String) {
        viewModelScope.launch {
            try {
                val response = repository.getDetailStory(id)
                _story.value = response.story
            } catch (e: Exception) {
                _story.value = null
            }
        }
    }
}
