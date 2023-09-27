package com.mazzampr.githubsearch.viewmodel

import androidx.lifecycle.ViewModel
import com.mazzampr.githubsearch.data.UsersRepository

class FavoriteViewModel(private val usersRepository: UsersRepository): ViewModel() {
    fun getAllFavUsers() = usersRepository.getAllFavUser()
}