package com.example.qrinventoryapp.data

import kotlinx.coroutines.flow.Flow

class OfflineUserEntitiesRepository(private val userEntityDao: UserEntityDao) : UserEntitiesRepository {

    override fun getAllUserEntitiesStream(): Flow<List<UserEntity>> = userEntityDao.getAllUserEntities()

    override fun getUserEntityByIdStream(id: Int): Flow<UserEntity> = userEntityDao.getUserEntityById(id)
}