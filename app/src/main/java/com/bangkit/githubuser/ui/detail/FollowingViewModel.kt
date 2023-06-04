package com.bangkit.githubuser.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.githubuser.api.ApiConfig
import com.bangkit.githubuser.model.Follow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class FollowingApiStatus { LOADING, ERROR, DONE }

class FollowingViewModel : ViewModel(){
    private val _userFollowing = MutableLiveData<List<Follow>>()
    val userFollowing : LiveData<List<Follow>> = _userFollowing

    private val _status = MutableLiveData<FollowingApiStatus>()
    val status : LiveData<FollowingApiStatus> = _status

    fun getUserFollowing(username: String) {
        _status.value = FollowingApiStatus.LOADING
        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<Follow>> {
            override fun onResponse(
                call: Call<List<Follow>>,
                response: Response<List<Follow>>
            ) {
                if (response.isSuccessful){
                    val res = response.body()!!
                    _userFollowing.value = res
                    _status.value = FollowingApiStatus.DONE
                } else {
                    _status.value = FollowingApiStatus.DONE
                    _userFollowing.value = listOf()
                }
            }

            override fun onFailure(call: Call<List<Follow>>, t: Throwable) {
                _status.value = FollowingApiStatus.ERROR
                _userFollowing.value = listOf()
            }
        })
    }
}