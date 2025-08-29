package com.example.schoolinventoryapp.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.schoolinventoryapp.SchoolInventoryApplication
import com.example.schoolinventoryapp.ui.home.HomeViewModel
import com.example.schoolinventoryapp.ui.scan.ScanViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            HomeViewModel(
                qrInventoryApplication().container.userEntitiesRepository,
                qrInventoryApplication().container.roomEntitiesRepository
            )
        }

        initializer {
            ScanViewModel(
                this.createSavedStateHandle(),
                qrInventoryApplication().container.itemsRepository,
                qrInventoryApplication().container.userEntitiesRepository,
                qrInventoryApplication().container.roomEntitiesRepository,
                qrInventoryApplication().container.incorrectItemsRepository
            )
        }
    }
}

fun CreationExtras.qrInventoryApplication(): SchoolInventoryApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as SchoolInventoryApplication)