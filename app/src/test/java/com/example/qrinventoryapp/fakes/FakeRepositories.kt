package com.example.qrinventoryapp.fakes

import com.example.qrinventoryapp.data.IncorrectItem
import com.example.qrinventoryapp.data.IncorrectItemsRepository
import com.example.qrinventoryapp.data.Item
import com.example.qrinventoryapp.data.ItemsRepository
import com.example.qrinventoryapp.data.RoomEntitiesRepository
import com.example.qrinventoryapp.data.RoomEntity
import com.example.qrinventoryapp.data.UserEntitiesRepository
import com.example.qrinventoryapp.data.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeUserEntitiesRepository : UserEntitiesRepository {
    private val _flowAll = MutableSharedFlow<List<UserEntity>>(replay = 1)
    override fun getAllUserEntitiesStream(): Flow<List<UserEntity>> = _flowAll

    suspend fun emitUserList(users: List<UserEntity>) {
        _flowAll.emit(users)
    }

    private val _flowById = MutableSharedFlow<UserEntity>(replay = 1)
    override fun getUserEntityByIdStream(id: Int): Flow<UserEntity> = _flowById

    suspend fun emitUser(user: UserEntity) {
        _flowById.emit(user)
    }
}

class FakeRoomEntitiesRepository : RoomEntitiesRepository {
    private val _flowAll = MutableSharedFlow<List<RoomEntity>>(replay = 1)
    override fun getAllRoomEntitiesStream(): Flow<List<RoomEntity>> = _flowAll

    suspend fun emitRoomList(rooms: List<RoomEntity>) {
        _flowAll.emit(rooms)
    }

    private val _flowById = MutableSharedFlow<RoomEntity>(replay = 1)
    override fun getRoomEntityByIdStream(id: Int): Flow<RoomEntity> = _flowById

    suspend fun emitRoom(room: RoomEntity) {
        _flowById.emit(room)
    }
}

class FakeItemsRepository : ItemsRepository {
    private val _flowAll = MutableSharedFlow<List<Item>>(replay = 1)
    override fun getAllItemsStream(): Flow<List<Item>> = _flowAll

    suspend fun emitItemList(items: List<Item>) {
        _flowAll.emit(items)
    }

    private val _flowByQr = MutableSharedFlow<Item?>(replay = 1)
    override fun getItemByQrStream(qr: String): Flow<Item?> = _flowByQr

    suspend fun emitItem(item: Item?) {
        _flowByQr.emit(item)
    }
}

class FakeIncorrectItemsRepository : IncorrectItemsRepository {
    private val _incorrectItems = mutableListOf<IncorrectItem>()

    private val _flowAll = MutableSharedFlow<List<IncorrectItem>>(replay = 1)
    override fun getAllIncorrectItemsStream(): Flow<List<IncorrectItem>> = _flowAll

    suspend fun emitIncorrectItemList(incorrectItems: List<IncorrectItem>) {
        _flowAll.emit(incorrectItems)
    }

    override suspend fun insert(incorrectItem: IncorrectItem) {
        _incorrectItems.add(incorrectItem)
        _flowAll.emit(_incorrectItems.toList())
    }

    override suspend fun deleteAllIncorrectItems() {
        _incorrectItems.clear()
        _flowAll.emit(_incorrectItems.toList())
    }

    override suspend fun existsByQr(qr: String): Boolean {
        return _incorrectItems.count { it.qr == qr } > 0
    }
}