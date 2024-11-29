package com.barengsaya.storyapp.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barengsaya.storyapp.data.Repository
import com.barengsaya.storyapp.data.api.response.LoginResponse
import com.barengsaya.storyapp.data.pref.UserModel
import com.barengsaya.storyapp.view.util.Event
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel() {

    private val _loginResult = MutableLiveData<Event<LoginResponse>>()
    val loginResult: LiveData<Event<LoginResponse>> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.login(email, password)
                _loginResult.value = Event(response)

                if (response.error == false && response.loginResult != null) {
                    val user = UserModel(
                        email = email,
                        token = response.loginResult.token ?: "",
                        isLogin = true
                    )
                    saveSession(user)
                }
            } catch (e: Exception) {
                _errorMessage.value = Event(e.message ?: "Terjadi kesalahan")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}
