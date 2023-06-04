package com.bangkit.githubuser.database

import android.app.Application

class UserApplication : Application() {
    val database: UserRoomDatabase by lazy { UserRoomDatabase.getDatabase(this) }
}