package com.mazzampr.githubsearch.di

import android.content.Context
import com.mazzampr.githubsearch.data.UsersRepository
import com.mazzampr.githubsearch.data.local.room.UsersDatabase
import com.mazzampr.githubsearch.data.remote.retrofit.RetrofitInstance
import com.mazzampr.githubsearch.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): UsersRepository {
        val apiService = RetrofitInstance.api
        val database =UsersDatabase.getInstance(context)
        val dao = database.usersDao()
        val appExecutors = AppExecutors()
        return UsersRepository.getInstance(apiService, dao, appExecutors)
    }
}