package com.bangkit.githubuser.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bangkit.githubuser.model.FavoriteUser

@Database(entities = [FavoriteUser::class], version = 1, exportSchema = false)
abstract class UserRoomDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao

    companion object {
        @Volatile
        private var INSTANCE : UserRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context) : UserRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserRoomDatabase::class.java,
                    "github_user_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}