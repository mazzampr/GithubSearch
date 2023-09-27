package com.mazzampr.githubsearch.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_fav")
class UsersEntity (
    @field:ColumnInfo(name = "username")
    @field:PrimaryKey
    val username: String,

    @field:ColumnInfo(name = "id")
    val idUser: String,

    @field:ColumnInfo(name = "avatar_url")
    val avatarUrl: String,
)