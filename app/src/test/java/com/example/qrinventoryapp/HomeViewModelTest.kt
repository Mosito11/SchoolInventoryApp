package com.example.qrinventoryapp

import com.example.qrinventoryapp.data.RoomEntity
import com.example.qrinventoryapp.data.UserEntity
import com.example.qrinventoryapp.fakes.FakeRoomEntitiesRepository
import com.example.qrinventoryapp.fakes.FakeUserEntitiesRepository
import com.example.qrinventoryapp.ui.home.AppMode
import com.example.qrinventoryapp.ui.home.HomeViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeUserEntitiesRepository: FakeUserEntitiesRepository
    private lateinit var fakeRoomEntitiesRepository: FakeRoomEntitiesRepository
    private lateinit var fakeViewModel: HomeViewModel

    @Before
    fun setupTest() {
        Dispatchers.setMain(dispatcher)
        fakeUserEntitiesRepository = FakeUserEntitiesRepository()
        fakeRoomEntitiesRepository = FakeRoomEntitiesRepository()
        fakeViewModel = HomeViewModel(fakeUserEntitiesRepository, fakeRoomEntitiesRepository)
    }

    @After
    fun closeTest() {
        Dispatchers.resetMain()
    }

    @Test
    fun selectMode_changeSelectedMode() {
        fakeViewModel.selectMode(AppMode.INVENTORY)
        assertEquals(AppMode.INVENTORY, fakeViewModel.uiState.value.selectedMode)
    }

    @Test
    fun selectUser_changeSelectedUserId() {
        fakeViewModel.selectUser(5)
        assertEquals(5, fakeViewModel.uiState.value.selectedUserId)
    }

    @Test
    fun selectRoom_changeSelectedRoomId() {
        fakeViewModel.selectRoom(10)
        assertEquals(10, fakeViewModel.uiState.value.selectedRoomId)
    }

    @Test
    fun isScanEnabled_whenModeSetToCONTROL() {
        fakeViewModel.selectMode(AppMode.CONTROL)
        assertTrue(fakeViewModel.uiState.value.isScanEnabled)
    }

    @Test
    fun isScanEnabled_whenModeSetToINVENTORY_andWhenUserAndRoomSelected() {
        fakeViewModel.selectMode(AppMode.INVENTORY)
        assertEquals(false, fakeViewModel.uiState.value.isScanEnabled)

        fakeViewModel.selectUser(1)
        assertEquals(false, fakeViewModel.uiState.value.isScanEnabled)

        fakeViewModel.selectRoom(2)
        assertEquals(true, fakeViewModel.uiState.value.isScanEnabled)
    }

    @Test
    fun emittingAllUsersUpdatesAvailableUsersInUiState() = runTest {
        val testUsers = listOf(UserEntity(id = 1, name = "Fake User"))
        fakeUserEntitiesRepository.emitUserList(testUsers)
        assertEquals(testUsers, fakeViewModel.uiState.value.availableUsers)
    }

    @Test
    fun emittingAllRoomsUpdatesAvailableRoomsInUiState() = runTest {
        val testRooms = listOf(RoomEntity(id = 1, name = "Fake Room"))
        fakeRoomEntitiesRepository.emitRoomList(testRooms)
        assertEquals(testRooms, fakeViewModel.uiState.value.availableRooms)
    }

    @Test
    fun emittingUserByIdUpdatesSelectedUserInUiState() = runTest {
        val testUser = UserEntity(id = 1, name = "Fake User")
        fakeUserEntitiesRepository.emitUser(testUser)
        fakeViewModel.selectUser(testUser.id)
        assertEquals(testUser.id, fakeViewModel.uiState.value.selectedUserId)
    }

    @Test
    fun emittingRoomByIdUpdatesSelectedRoomInUiState() = runTest {
        val testRoom = RoomEntity(id = 1, name = "Fake Room")
        fakeRoomEntitiesRepository.emitRoom(testRoom)
        fakeViewModel.selectRoom(testRoom.id)
        assertEquals(testRoom.id, fakeViewModel.uiState.value.selectedRoomId)
    }
}