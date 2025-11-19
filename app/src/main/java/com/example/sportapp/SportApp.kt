package com.example.sportapp

import android.app.Application
import android.app.LocaleManager
import com.example.sportapp.data.settings.AppPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SportApp : Application() {
//    override fun onCreate() {
//        super.onCreate()
//        val repo = AppPreferences(this)
//        CoroutineScope(Dispatchers.Default).launch {
//            val state = repo.prefsFlow.first()
//            LocaleManager.apply(state.languageTag)
//        }
//    }
}