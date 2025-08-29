package com.example.schoolinventoryapp.data

import kotlinx.coroutines.flow.Flow

interface UserEntitiesRepository {

    fun getAllUserEntitiesStream(): Flow<List<UserEntity>>

    fun getUserEntityByIdStream(id: Int): Flow<UserEntity>
}