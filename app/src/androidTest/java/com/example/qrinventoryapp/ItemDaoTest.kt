package com.example.qrinventoryapp

import com.example.qrinventoryapp.data.ItemDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ItemDaoTest : BaseDaoTest() {

    private lateinit var itemDao: ItemDao

    override fun afterCreatingDatabaseFun() {
        itemDao = inventoryDatabase.itemDao()
    }

    @Test
    fun daoGetAll_returnsItemsCount() = runBlocking {
        val allItems = itemDao.getAllItems().first()
        assertTrue(allItems.isNotEmpty())
        assertEquals(2, allItems.count())
    }

    @Test
    fun daoGetOneByQr_returnsCorrectItem() = runBlocking {
        val testItemQr = "8114672"
        val testItem = itemDao.getItemByQr(testItemQr).first()
        assertNotNull(testItem)
        assertEquals(testItemQr, testItem?.qr)
        assertEquals("PC Dell OptiPlex GX 620 SFF", testItem?.nazInv)
    }
}
