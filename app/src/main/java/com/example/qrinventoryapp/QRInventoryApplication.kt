package com.example.qrinventoryapp

import android.app.Application
import com.example.qrinventoryapp.data.AppContainer
import com.example.qrinventoryapp.data.AppDataContainer

class QRInventoryApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }

}