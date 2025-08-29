package com.example.qrinventoryapp.data

import kotlinx.coroutines.flow.Flow

class OfflineItemsRepository(private val itemDao: ItemDao) : ItemsRepository {

    override fun getAllItemsStream(): Flow<List<Item>> = itemDao.getAllItems()

    override fun getItemByQrStream(qr: String): Flow<Item?> = itemDao.getItemByQr(qr)
}