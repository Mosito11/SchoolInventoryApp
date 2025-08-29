package com.example.qrinventoryapp.data

import kotlinx.coroutines.flow.Flow

interface IncorrectItemsRepository {

    suspend fun insert(incorrectItem: IncorrectItem)

    fun getAllIncorrectItemsStream(): Flow<List<IncorrectItem>>

    suspend fun deleteAllIncorrectItems()

    suspend fun existsByQr(qr: String): Boolean
}