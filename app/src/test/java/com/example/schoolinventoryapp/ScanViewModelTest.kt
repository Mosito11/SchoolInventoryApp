package com.example.schoolinventoryapp

import androidx.lifecycle.SavedStateHandle
import com.example.schoolinventoryapp.data.IncorrectItem
import com.example.schoolinventoryapp.data.Item
import com.example.schoolinventoryapp.data.RoomEntity
import com.example.schoolinventoryapp.data.UserEntity
import com.example.schoolinventoryapp.fakes.FakeIncorrectItemsRepository
import com.example.schoolinventoryapp.fakes.FakeItemsRepository
import com.example.schoolinventoryapp.fakes.FakeRoomEntitiesRepository
import com.example.schoolinventoryapp.fakes.FakeUserEntitiesRepository
import com.example.schoolinventoryapp.ui.home.AppMode
import com.example.schoolinventoryapp.ui.scan.ScanScreenDestination
import com.example.schoolinventoryapp.ui.scan.ScanViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ScanViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeItemsRepository: FakeItemsRepository
    private lateinit var fakeUserEntitiesRepository: FakeUserEntitiesRepository
    private lateinit var fakeRoomEntitiesRepository: FakeRoomEntitiesRepository
    private lateinit var fakeIncorrectItemsRepository: FakeIncorrectItemsRepository

    @Before
    fun setupTest() {
        Dispatchers.setMain(dispatcher)
        fakeItemsRepository = FakeItemsRepository()
        fakeUserEntitiesRepository = FakeUserEntitiesRepository()
        fakeRoomEntitiesRepository = FakeRoomEntitiesRepository()
        fakeIncorrectItemsRepository = FakeIncorrectItemsRepository()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun closeTest() {
        Dispatchers.resetMain()
    }

    private fun createFakeViewModel(fakeSavedStateArgs: Map<String, Any?>): ScanViewModel {
        val fakeSavedStateHandle = SavedStateHandle(fakeSavedStateArgs)
        return ScanViewModel(
            fakeSavedStateHandle,
            fakeItemsRepository,
            fakeUserEntitiesRepository,
            fakeRoomEntitiesRepository,
            fakeIncorrectItemsRepository
        )
    }

    @Test
    fun processScannedCode_appModeControl_qrCodeNotFound() = runTest {
        val fakeViewModel = createFakeViewModel(
            mapOf(ScanScreenDestination.modeArg to AppMode.CONTROL.name)
        )

        fakeItemsRepository.emitItem(null)
        fakeViewModel.processScannedCode("SOME_QR_CODE")

        val fakeState = fakeViewModel.uiState.value
        assertEquals(R.string.qr_not_found, fakeState.errorTextId)
        assertNull(fakeState.qr)
        assertNull(fakeState.userNameFromDB)
        assertNull(fakeState.roomNameFromDB)
    }

    @Test
    fun processScannedCode_appModeControl_qrCodeFound() = runTest {

        val testUserId = 1
        val testRoomId = 10
        val testItem = Item(
            id = 1,
            qr = "QR123",
            invCis = "",
            nazInv = null,
            vyrCis = null,
            zarazeno = "",
            vyroba = null,
            nazSku = "",
            platnost = 1,
            userId = testUserId,
            roomId = testRoomId
        )

        fakeItemsRepository.emitItem(testItem)
        fakeUserEntitiesRepository.emitUser(UserEntity(id = testUserId, name = "Fake User"))
        fakeRoomEntitiesRepository.emitRoom(RoomEntity(id = testRoomId, name = "Fake Room"))

        val fakeViewModel = createFakeViewModel(
            mapOf(ScanScreenDestination.modeArg to AppMode.CONTROL.name)
        )

        fakeViewModel.processScannedCode("QR123")

        val fakeState = fakeViewModel.uiState.value
        assertEquals(null, fakeState.errorTextId)
        assertEquals("QR123", fakeState.qr)
        assertEquals("Fake User", fakeState.userNameFromDB)
        assertEquals("Fake Room", fakeState.roomNameFromDB)
    }

    @Test
    fun processScannedCode_appModeInventory_userAndRoomMatch() = runTest {

        val testUserId = 1
        val testRoomId = 10
        val testItem = Item(
            id = 1,
            qr = "QR123",
            invCis = "",
            nazInv = null,
            vyrCis = null,
            zarazeno = "",
            vyroba = null,
            nazSku = "",
            platnost = 1,
            userId = testUserId,
            roomId = testRoomId
        )
        fakeItemsRepository.emitItem(testItem)
        fakeUserEntitiesRepository.emitUser(UserEntity(id = testUserId, name = "Fake User"))
        fakeRoomEntitiesRepository.emitRoom(RoomEntity(id = testRoomId, name = "Fake Room"))

        val fakeViewModel = createFakeViewModel(
            mapOf(
                ScanScreenDestination.modeArg to AppMode.INVENTORY.name,
                ScanScreenDestination.userIdArg to testUserId,
                ScanScreenDestination.roomIdArg to testRoomId
            )
        )

        fakeViewModel.processScannedCode("QR123")

        val fakeState = fakeViewModel.uiState.value
        assertEquals("QR123", fakeState.qr)
        assertEquals("Fake User", fakeState.userNameFromDB)
        assertEquals("Fake Room", fakeState.roomNameFromDB)
        assertTrue(fakeState.userMatch == true)
        assertTrue(fakeState.roomMatch == true)
    }

    @Test
    fun processScannedCode_appModeInventory_userMatchAndRoomDoesNotMatch() = runTest {

        val testUserId = 1
        val testRoomId = 10
        val testItem = Item(
            id = 1,
            qr = "QR123",
            invCis = "",
            nazInv = null,
            vyrCis = null,
            zarazeno = "",
            vyroba = null,
            nazSku = "",
            platnost = 1,
            userId = testUserId,
            roomId = 4
        )
        fakeItemsRepository.emitItem(testItem)
        fakeUserEntitiesRepository.emitUser(UserEntity(id = testUserId, name = "Fake User"))
        fakeRoomEntitiesRepository.emitRoom(RoomEntity(id = testRoomId, name = "Fake Room"))

        val fakeViewModel = createFakeViewModel(
            mapOf(
                ScanScreenDestination.modeArg to AppMode.INVENTORY.name,
                ScanScreenDestination.userIdArg to testUserId,
                ScanScreenDestination.roomIdArg to testRoomId
            )
        )

        fakeViewModel.processScannedCode("QR123")

        val fakeState = fakeViewModel.uiState.value
        assertEquals("QR123", fakeState.qr)
        assertEquals("Fake User", fakeState.userNameFromDB)
        assertNotEquals("Another Fake Room", fakeState.roomNameFromDB)
        assertTrue(fakeState.userMatch == true)
        assertTrue(fakeState.roomMatch == false)
    }

    @Test
    fun insertIncorrectItemAndDeleteAll_saveAndDeleteCorrectly() = runTest {
        val testIncorrectItem = IncorrectItem(
            id = 1,
            qr = "QR123",
            userFromDatabase = 7,
            userSelected = 1,
            roomFromDatabase = 10,
            roomSelected = 10,
        )

        fakeIncorrectItemsRepository.insert(testIncorrectItem)

        val testIncorrectItems = fakeIncorrectItemsRepository.getAllIncorrectItemsStream().first()
        assertNotNull(testIncorrectItems)
        assertEquals(1, testIncorrectItems.size)

        val testInserted = testIncorrectItems[0]
        assertEquals("QR123", testInserted.qr)
        assertEquals(1, testInserted.userSelected)
        assertEquals(7, testInserted.userFromDatabase)
        assertEquals(10, testInserted.roomSelected)
        assertEquals(10, testInserted.roomFromDatabase)
        assertEquals(false, testInserted.userFromDatabase == testInserted.userSelected)
        assertEquals(true, testInserted.roomFromDatabase == testInserted.roomSelected)

        fakeIncorrectItemsRepository.deleteAllIncorrectItems()
        val testDeletedIncorrectItems =
            fakeIncorrectItemsRepository.getAllIncorrectItemsStream().first()
        assertTrue(testDeletedIncorrectItems.isEmpty())

    }

    @Test
    fun clearScan_resetUiState() = runTest {
        val testUserId = 10
        val testRoomId = 10
        val testItem = Item(
            id = 1,
            qr = "QR123",
            invCis = "",
            nazInv = null,
            vyrCis = null,
            zarazeno = "",
            vyroba = null,
            nazSku = "",
            platnost = 1,
            userId = testUserId,
            roomId = 4
        )

        val fakeViewModel = createFakeViewModel(
            mapOf(
                ScanScreenDestination.modeArg to AppMode.INVENTORY.name,
                ScanScreenDestination.userIdArg to testUserId,
                ScanScreenDestination.roomIdArg to testRoomId
            )
        )
        fakeItemsRepository.emitItem(testItem)
        fakeUserEntitiesRepository.emitUser(UserEntity(id = testUserId, name = "Fake User"))
        fakeRoomEntitiesRepository.emitRoom(RoomEntity(id = testRoomId, name = "Fake Room"))

        fakeViewModel.processScannedCode("QR123")
        assertNotNull(fakeViewModel.uiState.value.qr)

        fakeViewModel.clearScan()
        val stateAfterClearScan = fakeViewModel.uiState.value

        assertNull(stateAfterClearScan.qr)
        assertNull(stateAfterClearScan.userNameFromDB)
        assertNull(stateAfterClearScan.roomNameFromDB)
        assertNull(stateAfterClearScan.userMatch)
        assertNull(stateAfterClearScan.roomMatch)
    }

    @Test
    fun existsByQr_returnTrueWhenExist() = runBlocking {
        val testIncorrectItem = IncorrectItem(
            id = 1,
            qr = "QR123",
            userFromDatabase = 7,
            userSelected = 1,
            roomFromDatabase = 10,
            roomSelected = 10,
        )

        fakeIncorrectItemsRepository.insert(testIncorrectItem)

        val exists = fakeIncorrectItemsRepository.existsByQr("QR123")
        assertTrue(exists)
    }

    @Test
    fun existsByQr_returnFalseWhenDoesNotExist() = runBlocking {
        val exists = fakeIncorrectItemsRepository.existsByQr("QR999")
        assertFalse(exists)
    }
}