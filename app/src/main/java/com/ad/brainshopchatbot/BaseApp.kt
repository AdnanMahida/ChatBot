package com.ad.brainshopchatbot

import android.app.Application
import android.content.Context

class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        lateinit var appContext: Context
    }
}