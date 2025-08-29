package com.example.schoolinventoryapp.data

import kotlinx.coroutines.flow.Flow

class OfflineRoomEntitiesRepository(private val roomEntityDao: RoomEntityDao) : RoomEntitiesRepository {

    override fun getAllRoomEntitiesStream(): Flow<List<RoomEntity>> = roomEntityDao.getAllRoomEntities()

    override fun getRoomEntityByIdStream(id: Int): Flow<RoomEntity> = roomEntityDao.getRoomEntityById(id)
}