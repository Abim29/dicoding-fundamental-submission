package com.bangkit.githubuser.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.githubuser.api.ApiConfig
import com.bangkit.githubuser.model.GithubResponse
import com.bangkit.githubuser.model.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class UserApiStatus { LOADING, ERROR, DONE }

class UserViewModel : ViewModel() {
    private val _users = MutableLiveData<List<Users>>()
    val users : LiveData<List<Users>> = _users

    private val _status = MutableLiveData<UserApiStatus>()
    val status : LiveData<UserApiStatus> = _status

    init {
        findUsers("a")
    }

    fun findUsers(username: String) {
        _status.value = UserApiStatus.LOADING
        val client = ApiConfig.getApiService().getUsers(username)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _users.value = responseBody.items
                        _status.value = UserApiStatus.DONE
                    }
                } else {
                    _status.value = UserApiStatus.DONE
                    _users.value = listOf()
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _users.value = listOf()
                _status.value = UserApiStatus.ERROR
            }
        })
    }
}