package com.example.schoolinventoryapp.data

import kotlinx.coroutines.flow.Flow

interface RoomEntitiesRepository {

    fun getAllRoomEntitiesStream(): Flow<List<RoomEntity>>

    fun getRoomEntityByIdStream(id: Int): Flow<RoomEntity>
}