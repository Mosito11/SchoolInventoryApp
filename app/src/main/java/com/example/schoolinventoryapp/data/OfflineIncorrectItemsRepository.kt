package com.example.schoolinventoryapp.data

import kotlinx.coroutines.flow.Flow

class OfflineIncorrectItemsRepository(private val incorrectItemDao: IncorrectItemDao) : IncorrectItemsRepository {

    override suspend fun insert(incorrectItem: IncorrectItem) = incorrectItemDao.insert(incorrectItem)

    override fun getAllIncorrectItemsStream(): Flow<List<IncorrectItem>> = incorrectItemDao.getAllIncorrectItems()

    override suspend fun deleteAllIncorrectItems() = incorrectItemDao.deleteAll()

    override suspend fun existsByQr(qr: String): Boolean = incorrectItemDao.countByQr(qr) > 0
}