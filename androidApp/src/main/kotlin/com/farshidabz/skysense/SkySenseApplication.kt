package com.farshidabz.skysense

import android.app.Application
import com.farshidabz.skysense.di.sharedModulesForAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SkySenseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SkySenseApplication)
            modules(sharedModulesForAndroid())
        }
    }
}
