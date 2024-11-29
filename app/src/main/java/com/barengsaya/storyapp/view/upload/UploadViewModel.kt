package com.barengsaya.storyapp.view.upload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barengsaya.storyapp.data.Repository
import kotlinx.coroutines.launch
import java.io.File

class UploadViewModel(private val repository: Repository) : ViewModel() {

    fun uploadStory(file: File, description: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                repository.uploadStory(file, description)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Upload gagal")
            }
        }
    }
}
