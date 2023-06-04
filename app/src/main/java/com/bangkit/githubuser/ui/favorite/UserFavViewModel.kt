package com.bangkit.githubuser.ui.favorite

import androidx.lifecycle.*
import com.bangkit.githubuser.database.UserDao
import com.bangkit.githubuser.model.FavoriteUser
import kotlinx.coroutines.launch

class UserFavViewModel(private val userDao: UserDao) : ViewModel() {
    val allUsersFav = userDao.getAllUsers().asLiveData()
    fun insert(user: FavoriteUser){
        viewModelScope.launch {
            userDao.insert(user)
        }
    }

    fun delete(user: FavoriteUser){
        viewModelScope.launch {
            userDao.delete(user)
        }
    }
}

class UserFavViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserFavViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserFavViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}