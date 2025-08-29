package com.example.qrinventoryapp

import com.example.qrinventoryapp.data.RoomEntityDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RoomEntityDaoTest : BaseDaoTest() {

    private lateinit var roomEntityDao: RoomEntityDao

    override fun afterCreatingDatabaseFun() {
        roomEntityDao = inventoryDatabase.roomEntityDao()
    }

    @Test
    fun daoGetAll_returnsRoomEntitiesCount() = runBlocking {
        val allRoomEntities = roomEntityDao.getAllRoomEntities().first()
        assertTrue(allRoomEntities.isNotEmpty())
        assertEquals(38, allRoomEntities.count())
    }

    @Test
    fun daoGetOneById_returnsCorrectRoom() = runBlocking {
        val testRoomEntityId = 37
        val testRoomEntity = roomEntityDao.getRoomEntityById(testRoomEntityId).first()
        assertNotNull(testRoomEntity)
        assertEquals(testRoomEntityId, testRoomEntity.id)
        assertEquals("kabinet fyziky", testRoomEntity.name)
    }
}