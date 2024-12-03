package com.barengsaya.storyapp.di

import android.content.Context
import com.barengsaya.storyapp.data.Repository
import com.barengsaya.storyapp.data.api.retrofit.ApiConfig
import com.barengsaya.storyapp.data.pref.UserPreference
import com.barengsaya.storyapp.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(pref, apiService)
    }
}
