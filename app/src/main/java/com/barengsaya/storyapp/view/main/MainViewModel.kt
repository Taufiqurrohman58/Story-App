package com.barengsaya.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.barengsaya.storyapp.data.Repository
import com.barengsaya.storyapp.data.api.response.ListStoryItem
import com.barengsaya.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch
import androidx.paging.PagingData
import androidx.paging.cachedIn

class MainViewModel(private val repository: Repository) : ViewModel() {

    fun getStoriesPaging(token: String): LiveData<PagingData<ListStoryItem>> {
        return repository.getStoriesPaging(token).cachedIn(viewModelScope)
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

