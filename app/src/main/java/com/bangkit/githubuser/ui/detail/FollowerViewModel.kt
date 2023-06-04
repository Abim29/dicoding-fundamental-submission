package com.bangkit.githubuser.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.githubuser.api.ApiConfig
import com.bangkit.githubuser.model.Follow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class FollowerApiStatus { LOADING, ERROR, DONE }

class FollowerViewModel : ViewModel(){
    private val _userFollowers = MutableLiveData<List<Follow>>()
    val userFollowers : LiveData<List<Follow>> = _userFollowers

    private val _status = MutableLiveData<FollowerApiStatus>()
    val status : LiveData<FollowerApiStatus> = _status

    fun getUserFollowers(username: String) {
        _status.value = FollowerApiStatus.LOADING
        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<List<Follow>> {
            override fun onResponse(
                call: Call<List<Follow>>,
                response: Response<List<Follow>>
            ) {
                if (response.isSuccessful){
                    val res = response.body()!!
                    _userFollowers.value = res
                    _status.value = FollowerApiStatus.DONE
                } else {
                    _status.value = FollowerApiStatus.DONE
                    _userFollowers.value = listOf()
                }
            }

            override fun onFailure(call: Call<List<Follow>>, t: Throwable) {
                _status.value = FollowerApiStatus.ERROR
                _userFollowers.value = listOf()
            }
        })
    }
}