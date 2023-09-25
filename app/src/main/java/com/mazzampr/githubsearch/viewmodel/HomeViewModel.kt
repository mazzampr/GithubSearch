package com.mazzampr.githubsearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mazzampr.githubsearch.data.remote.response.SearchResponse
import com.mazzampr.githubsearch.data.remote.response.UserResponse
import com.mazzampr.githubsearch.data.remote.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel: ViewModel() {

    private val _userList = MutableLiveData<List<UserResponse?>?>()
    val userList: LiveData<List<UserResponse?>?> = _userList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "HomeViewModel"
    }

    fun getUserList(query: String) {
        _isLoading.value = true
        RetrofitInstance.api.getUserList(query).enqueue(object: Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userList.value = response.body()!!.items

                }
                else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}