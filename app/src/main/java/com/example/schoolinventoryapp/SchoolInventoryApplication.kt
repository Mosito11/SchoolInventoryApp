package com.example.schoolinventoryapp

import android.app.Application
import com.example.schoolinventoryapp.data.AppContainer
import com.example.schoolinventoryapp.data.AppDataContainer

class SchoolInventoryApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }

}