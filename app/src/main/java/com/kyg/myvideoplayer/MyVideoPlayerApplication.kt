package com.kyg.myvideoplayer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyVideoPlayerApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}