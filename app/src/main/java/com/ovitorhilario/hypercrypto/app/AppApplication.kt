package com.ovitorhilario.hypercrypto.app

import android.app.Application
import com.ovitorhilario.hypercrypto.di.module.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@AppApplication)
            modules(appModule)
        }
    }
}