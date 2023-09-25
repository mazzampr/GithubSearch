package com.mazzampr.githubsearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mazzampr.githubsearch.data.remote.response.UserResponse
import com.mazzampr.githubsearch.data.remote.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel:ViewModel() {

    private val _userFollowing = MutableLiveData<List<UserResponse?>?>()
    val userFollowing: LiveData<List<UserResponse?>?> = _userFollowing

    private val _userFollowers = MutableLiveData<List<UserResponse?>?>()
    val userFollowers: LiveData<List<UserResponse?>?> = _userFollowers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "FollowViewModel"
    }

    fun getFollowing(user: String) {
        _isLoading.value = true
        RetrofitInstance.api.getFollowing(user).enqueue(object: Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userFollowing.value = response.body()
                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun getFollowers(user: String) {
        _isLoading.value = true
        RetrofitInstance.api.getFollowers(user).enqueue(object: Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userFollowers.value = response.body()
                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }
}