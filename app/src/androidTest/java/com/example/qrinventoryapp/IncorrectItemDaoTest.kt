package com.example.qrinventoryapp

import com.example.qrinventoryapp.data.IncorrectItem
import com.example.qrinventoryapp.data.IncorrectItemDao
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class IncorrectItemDaoTest : BaseDaoTest( ){

    private lateinit var incorrectItemDao: IncorrectItemDao

    override fun afterCreatingDatabaseFun() {
        incorrectItemDao = inventoryDatabase.incorrectItemDao()
    }

    private var incorrectItem1 = IncorrectItem(1, "ABC123", "11.5.1980", 1, 2, 1, 1)
    private var incorrectItem2 = IncorrectItem(2, "DEF123", "11.6.1981", 1, 1,  2,  1)

    private suspend fun addOneIncorrectItemToDb() {
        incorrectItemDao.insert(incorrectItem1)
    }

    private suspend fun addTwoIncorrectItemsToDb() {
        incorrectItemDao.insert(incorrectItem1)
        incorrectItemDao.insert(incorrectItem2)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsIncorrectItemIntoDB() = runBlocking {
        addOneIncorrectItemToDb()
        val allIncorrectItems = incorrectItemDao.getAllIncorrectItems().first()
        assertEquals(allIncorrectItems[0], incorrectItem1)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsTwoIncorrectItemsIntoDB() = runBlocking {
        addTwoIncorrectItemsToDb()
        val allIncorrectItems = incorrectItemDao.getAllIncorrectItems().first()
        assertEquals(allIncorrectItems[0], incorrectItem1)
        assertEquals(allIncorrectItems[1], incorrectItem2)
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteItems_deletesAllIncorrectItemsFromDB() = runBlocking {
        addTwoIncorrectItemsToDb()
        incorrectItemDao.deleteAll()

        val allIncorrectItems = incorrectItemDao.getAllIncorrectItems().first()
        assertTrue(allIncorrectItems.isEmpty())
    }

    @Test
    fun daoGetAll_onEmptyDb_returnsEmptyList() = runBlocking {
        val allIncorrectItems = incorrectItemDao.getAllIncorrectItems().first()
        assertTrue(allIncorrectItems.isEmpty())
    }
}

