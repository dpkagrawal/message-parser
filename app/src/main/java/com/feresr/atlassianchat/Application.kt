package com.feresr.atlassianchat

import android.app.Application
import android.os.StrictMode
import com.feresr.atlassianchat.DI.AppModule
import com.feresr.atlassianchat.DI.ApplicationComponent
import com.feresr.atlassianchat.DI.DaggerApplicationComponent

/**
 * Created by feresr on 26/6/17.
 */
class Application : Application() {

    lateinit var component: ApplicationComponent
        private set

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectAll()
                    .penaltyLog()
                    .build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build())
        }
        super.onCreate()
        component = DaggerApplicationComponent.builder()
                .appModule(AppModule()).build()
    }
}