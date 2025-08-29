package com.example.qrinventoryapp

import com.example.qrinventoryapp.data.UserEntityDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UserEntityDaoTest : BaseDaoTest( ){

    private lateinit var userEntityDao: UserEntityDao

    override fun afterCreatingDatabaseFun() {
        userEntityDao = inventoryDatabase.userEntityDao()
    }

    @Test
    fun daoGetAll_returnsUsersCount() = runBlocking {
        val allUserEntities = userEntityDao.getAllUserEntities().first()
        assertTrue(allUserEntities.isNotEmpty())
        assertEquals(66, allUserEntities.count())
    }

    @Test
    fun daoGetOneById_returnsCorrectUser() = runBlocking {
        val testUserEntityId = 55
        val testUserEntity = userEntityDao.getUserEntityById(testUserEntityId).first()
        assertNotNull(testUserEntity)
        assertEquals(testUserEntityId, testUserEntity.id)
        assertEquals("Mainx Oskar", testUserEntity.name)
    }
}

