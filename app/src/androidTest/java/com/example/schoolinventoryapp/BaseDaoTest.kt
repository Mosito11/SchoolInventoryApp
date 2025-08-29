package com.example.schoolinventoryapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.schoolinventoryapp.data.InventoryDatabase
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
abstract class BaseDaoTest {

    protected lateinit var inventoryDatabase: InventoryDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()

        val testDbFile = context.getDatabasePath("test_inventory.db")
        if (testDbFile.exists()) testDbFile.delete()

        context.assets.open("database/inventory.db").use { inputStream ->
            testDbFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        inventoryDatabase = Room.databaseBuilder(
            context,
            InventoryDatabase::class.java,
            "test_inventory.db"
        )
            .allowMainThreadQueries()
            .build()

        //inventoryDatabase = Room.inMemoryDatabaseBuilder(context, InventoryDatabase::class.java)
        //    .allowMainThreadQueries()
        //    .build()
        afterCreatingDatabaseFun()
    }

    protected abstract fun afterCreatingDatabaseFun()

    @After
    @Throws(IOException::class)
    fun closeDb() {
        inventoryDatabase.close()
    }
}