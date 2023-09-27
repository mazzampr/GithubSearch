package com.mazzampr.githubsearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mazzampr.githubsearch.data.UsersRepository
import com.mazzampr.githubsearch.data.local.entity.UsersEntity
import com.mazzampr.githubsearch.data.remote.response.UserResponse
import com.mazzampr.githubsearch.data.remote.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val usersRepository: UsersRepository): ViewModel() {

    private val _user = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "DetailViewModel"
    }

    fun getDetailUser(username: String) {
        _isLoading.value = true
        RetrofitInstance.api.getDetailUser(username).enqueue(object: Callback<UserResponse> {

            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful) {
                    _user.value = responseBody!!

                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getUserFav(username: String) = usersRepository.getUserFav(username)

    fun saveFavUser(user: UsersEntity) = usersRepository.insertFavUser(user)

    fun deleteUser(user: UsersEntity) = usersRepository.deleteUserFav(user)
}