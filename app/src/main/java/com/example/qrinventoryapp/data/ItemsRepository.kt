package com.example.qrinventoryapp.data

import kotlinx.coroutines.flow.Flow

interface ItemsRepository {

    fun getAllItemsStream(): Flow<List<Item>>

    fun getItemByQrStream(qr: String): Flow<Item?>

}