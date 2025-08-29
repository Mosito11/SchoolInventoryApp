package com.example.schoolinventoryapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolinventoryapp.data.RoomEntitiesRepository
import com.example.schoolinventoryapp.data.RoomEntity
import com.example.schoolinventoryapp.data.UserEntitiesRepository
import com.example.schoolinventoryapp.data.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppMode {
    CONTROL,
    INVENTORY
}

class HomeViewModel(
    private val userEntitiesRepository: UserEntitiesRepository,
    private val roomEntitiesRepository: RoomEntitiesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userEntitiesRepository.getAllUserEntitiesStream().collect { userList ->
                _uiState.update {
                    it.copy(availableUsers = userList)
                }
            }
        }

        viewModelScope.launch {
            roomEntitiesRepository.getAllRoomEntitiesStream().collect { roomList ->
                _uiState.update {
                    it.copy(availableRooms = roomList)
                }
            }
        }
    }

    fun selectMode(mode: AppMode) {
        _uiState.update { it.copy(selectedMode = mode) }
    }

    fun selectUser(userId: Int) {
        _uiState.update { it.copy(selectedUserId = userId) }
    }

    fun selectRoom(roomId: Int) {
        _uiState.update { it.copy(selectedRoomId = roomId) }
    }
}

data class HomeUiState(
    val selectedMode: AppMode = AppMode.CONTROL,
    val selectedUserId: Int? = null,
    val selectedRoomId: Int? = null,
    val availableUsers: List<UserEntity> = listOf(),
    val availableRooms: List<RoomEntity> = listOf()
) {
    val isScanEnabled: Boolean
        get() = when (selectedMode) {
            AppMode.CONTROL -> true
            AppMode.INVENTORY -> selectedUserId != null && selectedRoomId != null
        }
}
