package com.example.qrinventoryapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface IncorrectItemDao {

    @Insert
    suspend fun insert(incorrectItem: IncorrectItem)

    @Query("SELECT * FROM items_not_correct")
    fun getAllIncorrectItems(): Flow<List<IncorrectItem>>

    @Query("DELETE FROM items_not_correct")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM items_not_correct WHERE qr = :qr")
    suspend fun countByQr(qr: String): Int
}

