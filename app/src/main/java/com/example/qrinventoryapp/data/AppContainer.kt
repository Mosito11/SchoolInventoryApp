package com.example.qrinventoryapp.data

import android.content.Context

interface AppContainer {
    val itemsRepository: ItemsRepository
    val incorrectItemsRepository: IncorrectItemsRepository
    val roomEntitiesRepository: RoomEntitiesRepository
    val userEntitiesRepository: UserEntitiesRepository

}

class AppDataContainer(private val context: Context) : AppContainer {

    override val itemsRepository: ItemsRepository by lazy {
        OfflineItemsRepository(InventoryDatabase.getDatabase(context).itemDao())
    }

    override val incorrectItemsRepository: IncorrectItemsRepository by lazy {
        OfflineIncorrectItemsRepository(InventoryDatabase.getDatabase(context).incorrectItemDao())
    }

    override val roomEntitiesRepository: RoomEntitiesRepository by lazy {
        OfflineRoomEntitiesRepository(InventoryDatabase.getDatabase(context).roomEntityDao())
    }

    override val userEntitiesRepository: UserEntitiesRepository by lazy {
        OfflineUserEntitiesRepository(InventoryDatabase.getDatabase(context).userEntityDao())
    }

}