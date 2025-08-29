package com.example.schoolinventoryapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Item::class, IncorrectItem::class, RoomEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class InventoryDatabase : RoomDatabase() {
    abstract fun itemDao() : ItemDao
    abstract fun incorrectItemDao() : IncorrectItemDao
    abstract fun userEntityDao() : UserEntityDao
    abstract fun roomEntityDao() : RoomEntityDao

    companion object {

        @Volatile
        private var INSTANCE: InventoryDatabase? = null

        fun getDatabase(context: Context): InventoryDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, InventoryDatabase::class.java, "inventory_database")
                    .createFromAsset("database/inventory.db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }

    }
}