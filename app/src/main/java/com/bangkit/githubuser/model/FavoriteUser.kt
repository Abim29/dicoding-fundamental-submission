package com.bangkit.githubuser.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "fav_user")
@Parcelize
data class FavoriteUser(
    @PrimaryKey(autoGenerate = false)

    @ColumnInfo("username")
    var username: String = "",

    @ColumnInfo("avatarUrl")
    var avatarUrl: String? = null
) : Parcelable
