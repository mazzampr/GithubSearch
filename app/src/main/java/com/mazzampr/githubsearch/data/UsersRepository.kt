package com.mazzampr.githubsearch.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.mazzampr.githubsearch.data.local.entity.UsersEntity
import com.mazzampr.githubsearch.data.local.room.UsersDao
import com.mazzampr.githubsearch.data.remote.retrofit.GithubAPI
import com.mazzampr.githubsearch.utils.AppExecutors

class UsersRepository private constructor(
    private val apiService: GithubAPI,
    private val usersDao: UsersDao,
    private val appExecutors: AppExecutors
) {

    private val result = MediatorLiveData<Result<List<UsersEntity>>>()
    private val resultMessage = MediatorLiveData<Result<String>>()
    fun getAllFavUser(): LiveData<Result<List<UsersEntity>>> {
        result.value = Result.Loading
        val localData = usersDao.getLikedUsers()
        result.addSource(localData) {
            result.value = Result.Success(it)
        }
        return result
    }

    fun getUserFav(username: String): LiveData<List<UsersEntity>> {
        return usersDao.getUserDetail(username)
    }

    fun insertFavUser(user: UsersEntity): LiveData<Result<String>> {
        resultMessage.value = Result.Loading
        appExecutors.diskIO.execute{
            usersDao.upsert(user)
        }
        resultMessage.value = Result.Success("${user.username} berhasil ditambahkan ke Favorite")
        return resultMessage
    }

    fun deleteUserFav(user: UsersEntity): LiveData<Result<String>> {
        resultMessage.value = Result.Loading
        appExecutors.diskIO.execute{
            usersDao.delete(user)
        }
        resultMessage.value = Result.Success("${user.username} telah dihapus dari Favorite")
        return resultMessage
    }

    companion object {
        @Volatile
        private var instance: UsersRepository? = null
        fun getInstance(
            apiService: GithubAPI,
            usersDao: UsersDao,
            appExecutors: AppExecutors
        ): UsersRepository =
            instance ?: synchronized(this) {
                instance ?: UsersRepository(apiService, usersDao, appExecutors)
            }.also { instance = it }
    }
}