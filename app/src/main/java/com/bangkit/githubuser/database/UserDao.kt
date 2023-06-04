package com.bangkit.githubuser.database

import androidx.room.*
import com.bangkit.githubuser.model.FavoriteUser
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: FavoriteUser)

    @Delete
    suspend fun delete(user: FavoriteUser)

    @Query("SELECT * FROM fav_user")
    fun getAllUsers() : Flow<List<FavoriteUser>>
}