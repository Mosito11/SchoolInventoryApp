package com.example.schoolinventoryapp.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserEntityDao {

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserEntityById(id: Int): Flow<UserEntity>

    @Query("SELECT * FROM users ORDER BY name ASC")
    fun getAllUserEntities(): Flow<List<UserEntity>>
}