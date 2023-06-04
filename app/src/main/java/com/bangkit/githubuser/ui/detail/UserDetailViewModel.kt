package com.bangkit.githubuser.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.githubuser.api.ApiConfig
import com.bangkit.githubuser.model.UserDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class UserDetailApiStatus { LOADING, ERROR, DONE }

class UserDetailViewModel : ViewModel() {
    private val _user = MutableLiveData<UserDetail>()
    val user : LiveData<UserDetail> = _user

    private val _status = MutableLiveData<UserDetailApiStatus>()
    val status : LiveData<UserDetailApiStatus> = _status

    fun getUserDetails(username: String) {
        _status.value = UserDetailApiStatus.LOADING
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<UserDetail> {
            override fun onResponse(
                call: Call<UserDetail>,
                response: Response<UserDetail>
            ) {
                _status.value = UserDetailApiStatus.DONE
                if (response.isSuccessful){
                    _user.value = response.body()
                } else {
                    _user.value = UserDetail()
                }
            }

            override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                _status.value = UserDetailApiStatus.ERROR
                _user.value = UserDetail()
            }
        })
    }
}