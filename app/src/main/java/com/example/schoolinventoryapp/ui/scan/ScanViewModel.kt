package com.example.schoolinventoryapp.ui.scan

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolinventoryapp.R
import com.example.schoolinventoryapp.data.IncorrectItem
import com.example.schoolinventoryapp.data.IncorrectItemsRepository
import com.example.schoolinventoryapp.data.ItemsRepository
import com.example.schoolinventoryapp.data.RoomEntitiesRepository
import com.example.schoolinventoryapp.data.UserEntitiesRepository
import com.example.schoolinventoryapp.ui.home.AppMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class ScanViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository,
    private val userEntitiesRepository: UserEntitiesRepository,
    private val roomEntitiesRepository: RoomEntitiesRepository,
    private val incorrectItemsRepository: IncorrectItemsRepository
) : ViewModel() {

    //tieto val mozu byt privatne?
    val selectedMode = AppMode.valueOf(
        savedStateHandle[ScanScreenDestination.modeArg] ?: AppMode.CONTROL.name
    )
    /*
    val selectedUserId: Int? = savedStateHandle[ScanScreenDestination.userIdArg]
    val selectedRoomId: Int? = savedStateHandle[ScanScreenDestination.roomIdArg]
*/
    val selectedUserId: Int? = savedStateHandle.get<Int>(ScanScreenDestination.userIdArg) ?.takeIf { it != -1 }  // ak je -1, Int = null nemoze byt v NavHoste
    val selectedRoomId: Int? = savedStateHandle.get<Int>(ScanScreenDestination.roomIdArg) ?.takeIf { it != -1 }

    private val _uiState = MutableStateFlow(ScanUiState())
    val uiState: StateFlow<ScanUiState> = _uiState.asStateFlow()

    fun processScannedCode(qrCode: String) {
        if (_uiState.value.qr == qrCode) return //pre pripad duplicitneho skenovania?

        viewModelScope.launch {
            val item = itemsRepository.getItemByQrStream(qrCode).firstOrNull()
            if (item != null) {
                val userFound = userEntitiesRepository.getUserEntityByIdStream(item.userId).firstOrNull()
                val roomFound = roomEntitiesRepository.getRoomEntityByIdStream(item.roomId).firstOrNull()
                val userMatchFound = if (selectedMode == AppMode.INVENTORY) item.userId == selectedUserId else null
                val roomMatchFound = if (selectedMode == AppMode.INVENTORY) item.roomId == selectedRoomId else null

                _uiState.value = ScanUiState(
                    qr = item.qr,
                    userNameFromDB = userFound?.name,
                    roomNameFromDB = roomFound?.name,
                    errorTextId = null,
                    userMatch = userMatchFound,
                    roomMatch = roomMatchFound
                )
                /*
                if (userMatchFound == false || roomMatchFound == false) {
                    insertIncorrectItemToDB(item)
                }*/
            } else {
                _uiState.value = ScanUiState(errorTextId = R.string.qr_not_found)
            }
        }
    }
/*
    private suspend fun insertIncorrectItemToDB(item: Item) {
        val selectedUserIdForDB = requireNotNull(selectedUserId)
        val selectedRoomIdForDB = requireNotNull(selectedRoomId)

        incorrectItemsRepository.insert(
                IncorrectItem(
                    qr = item.qr,
                    userFromDatabase = item.userId,
                    userSelected = selectedUserIdForDB,
                    roomFromDatabase = item.roomId,
                    roomSelected = selectedRoomIdForDB
                )
            )
    }
*/
    fun saveIncorrectItem() {
    val currentUiState = _uiState.value
    val qr = currentUiState.qr ?: return
    val selectedUserIdForDB = requireNotNull(selectedUserId)
    val selectedRoomIdForDB = requireNotNull(selectedRoomId)

    viewModelScope.launch() {

        val item = itemsRepository.getItemByQrStream(qr).firstOrNull() ?: return@launch

        val exists = incorrectItemsRepository.existsByQr(qr)
        if (exists) {
            _uiState.value =
                currentUiState.copy(snackBarMessageId = R.string.incorrect_item_already_exists)
            return@launch
        }

        Log.d("ScanViewModel", "Inserting incorrect item: ${item.qr}")
        incorrectItemsRepository.insert(
            IncorrectItem(
                qr = qr,
                userFromDatabase = item.userId,
                userSelected = selectedUserIdForDB,
                roomFromDatabase = item.roomId,
                roomSelected = selectedRoomIdForDB
            )
        )

        clearScan()
    }
}

    fun clearScan(errorTextId: Int? = null) {
        _uiState.value = ScanUiState(errorTextId = errorTextId)
    }

    fun clearSnackBarMessage() {
        _uiState.value = _uiState.value.copy(snackBarMessageId = null)
    }

}

data class ScanUiState(
    val qr: String? = null,
    val userNameFromDB: String? = null,
    val roomNameFromDB: String? = null,
    val errorTextId: Int? = null,
    val userMatch: Boolean? = null,
    val roomMatch: Boolean? = null,
    val snackBarMessageId: Int? = null
)
