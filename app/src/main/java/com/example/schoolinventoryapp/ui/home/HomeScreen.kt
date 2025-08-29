package com.example.schoolinventoryapp.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.schoolinventoryapp.SchoolInventoryTopAppBar
import com.example.schoolinventoryapp.R
import com.example.schoolinventoryapp.data.RoomEntity
import com.example.schoolinventoryapp.data.UserEntity
import com.example.schoolinventoryapp.ui.AppViewModelProvider
import com.example.schoolinventoryapp.ui.navigation.NavigationHelper

object HomeDestination : NavigationHelper {
    override val route = "home"
    override val titleRes = R.string.homescreen_title
}

@Composable
fun HomeScreen(
    navigateToScan: (AppMode, Int?, Int?) -> Unit,
    navigateToQuit: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    var showUserDialog by remember { mutableStateOf(false) }
    var showRoomDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SchoolInventoryTopAppBar(title = stringResource(R.string.topbar_title) + " " + uiState.selectedMode.name)
        }
    ) {
        innerPadding ->
            HomeScreenContent(
                uiState = uiState,
                showUserDialog = showUserDialog,
                showRoomDialog = showRoomDialog,
                onShowUserDialogChange = { showUserDialog = it },
                onShowRoomDialogChange = { showRoomDialog = it },
                onModeSelected = viewModel::selectMode,
                onUserSelected = viewModel::selectUser,
                onRoomSelected = viewModel::selectRoom,
                navigateToScan = navigateToScan,
                navigateToQuit = navigateToQuit,
                modifier = modifier,
                contentPadding = innerPadding
        )
    }


}

@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    showUserDialog: Boolean,
    showRoomDialog: Boolean,
    onShowUserDialogChange: (Boolean) -> Unit,
    onShowRoomDialogChange: (Boolean) -> Unit,
    onModeSelected: (AppMode) -> Unit,
    onUserSelected: (Int) -> Unit,
    onRoomSelected: (Int) -> Unit,
    navigateToScan: (AppMode, Int?, Int?) -> Unit,
    navigateToQuit: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        val buttonWidth = 200.dp
        // Mode Dropdown
        ModeDropdown(
            selectedMode = uiState.selectedMode,
            onModeSelected = onModeSelected,
            modifier = Modifier.width(buttonWidth)
        )

        // User & Room selection
        if (uiState.selectedMode == AppMode.INVENTORY) {
            Button(
                onClick = { onShowUserDialogChange(true) },
                modifier = Modifier.width(buttonWidth)
            ) {
                Text(uiState.availableUsers.firstOrNull { it.id == uiState.selectedUserId }?.name
                    ?: stringResource(R.string.user_selection))
            }

            Button(
                onClick = { onShowRoomDialogChange(true) },
                modifier = Modifier.width(buttonWidth)
            ) {
                Text(uiState.availableRooms.firstOrNull { it.id == uiState.selectedRoomId }?.name
                    ?: stringResource(R.string.room_selection))
            }
        }

        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = navigateToQuit) {
                Text(stringResource(R.string.quit))
            }
            Button(
                onClick = { navigateToScan(uiState.selectedMode, uiState.selectedUserId, uiState.selectedRoomId) },
                enabled = uiState.isScanEnabled
            ) {
                Text(stringResource(R.string.scan))
            }
        }

        // User dialog
        if (showUserDialog) {
            EntitySelectDialog(
                title = stringResource(R.string.user_selection),
                items = uiState.availableUsers.map { it.id to it.name },
                onSelect = onUserSelected,
                onDismiss = { onShowUserDialogChange(false) }
            )
        }

        // Room dialog
        if (showRoomDialog) {
            EntitySelectDialog(
                title = stringResource(R.string.room_selection),
                items = uiState.availableRooms.map { it.id to it.name },
                onSelect = onRoomSelected,
                onDismiss = { onShowRoomDialogChange(false) }
            )
        }
    }
}


@Composable
private fun ModeDropdown(
    selectedMode: AppMode,
    onModeSelected: (AppMode) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(
            onClick = { expanded = true },
            modifier = modifier
        ) {
            Text(selectedMode.name)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            AppMode.entries.forEach { mode ->
                DropdownMenuItem(
                    text = { Text(mode.name) },
                    onClick = {
                        onModeSelected(mode)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun EntitySelectDialog(
    title: String,
    items: List<Pair<Int, String>>,
    onSelect: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredItems = items.filter { it.second.contains(searchQuery, ignoreCase = true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text(stringResource(R.string.search)) },
                    modifier = Modifier.fillMaxWidth()
                )
                LazyColumn {
                    items(filteredItems) { (id, name) ->
                        Text(
                            text = name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelect(id)
                                    onDismiss()
                                }
                                .padding(8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.search))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val fakeUiState = HomeUiState(
        selectedMode = AppMode.INVENTORY,
        selectedUserId = null,
        selectedRoomId = null,
        availableUsers = listOf(
            UserEntity(1, "Peter Parker"),
            UserEntity(2, "Tony Stark"),
            UserEntity(3, "Natasha Romanoff")
        ),
        availableRooms = listOf(
            RoomEntity(1, "Server Room"),
            RoomEntity(2, "Meeting Room"),
            RoomEntity(3, "Storage")
        )
    )
    Scaffold(
        topBar = {
            SchoolInventoryTopAppBar(title = stringResource(R.string.topbar_title) + " " + fakeUiState.selectedMode.name)
        }
    ) { innerPadding ->
        HomeScreenContent(
            uiState = fakeUiState,
            showUserDialog = false,
            showRoomDialog = false,
            onShowUserDialogChange = {},
            onShowRoomDialogChange = {},
            onModeSelected = {},
            onUserSelected = {},
            onRoomSelected = {},
            navigateToScan = { _, _, _ -> },
            navigateToQuit = {},
            contentPadding = innerPadding
        )
    }
}