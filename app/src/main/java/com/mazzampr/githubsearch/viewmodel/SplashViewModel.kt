package com.mazzampr.githubsearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.mazzampr.githubsearch.utils.SettingPreferences

class SplashViewModel(private val pref: SettingPreferences): ViewModel() {
    fun getThemeSetting() : LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }
}