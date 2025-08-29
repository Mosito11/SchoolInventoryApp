package com.example.qrinventoryapp.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("SELECT * FROM items WHERE qr = :qr")
    fun getItemByQr(qr: String): Flow<Item?>

    @Query("SELECT * FROM items")
    fun getAllItems(): Flow<List<Item>>
}