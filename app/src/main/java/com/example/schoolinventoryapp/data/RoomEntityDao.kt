package com.example.schoolinventoryapp.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomEntityDao {

    @Query("SELECT * FROM rooms WHERE id = :id")
    fun getRoomEntityById(id: Int): Flow<RoomEntity>

    @Query("SELECT * FROM rooms ORDER BY name ASC")
    fun getAllRoomEntities(): Flow<List<RoomEntity>>
}