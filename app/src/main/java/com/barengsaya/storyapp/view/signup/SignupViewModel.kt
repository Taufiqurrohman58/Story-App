package com.barengsaya.storyapp.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barengsaya.storyapp.data.Repository
import com.barengsaya.storyapp.data.api.response.RegisterResponse
import com.barengsaya.storyapp.view.util.Event
import kotlinx.coroutines.launch

class SignupViewModel(private val repository: Repository) : ViewModel() {

    private val _registerResult = MutableLiveData<RegisterResponse?>()
    val registerResult: LiveData<RegisterResponse?> = _registerResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.register(name, email, password)
                _registerResult.value = response
            } catch (e: Exception) {
                _errorMessage.value = Event(e.message ?: "Terjadi kesalahan")
            } finally {
                _isLoading.value = false
            }
        }
    }
}

