package com.mazzampr.githubsearch.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mazzampr.githubsearch.data.local.entity.UsersEntity

@Dao
interface UsersDao {
    @Query("SELECT * FROM users_fav")
    fun getLikedUsers(): LiveData<List<UsersEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(user: UsersEntity)

    @Delete
    fun delete(user: UsersEntity)

    @Query("SELECT * from users_fav WHERE username = :username")
    fun getUserDetail(username: String): LiveData<List<UsersEntity>>

}