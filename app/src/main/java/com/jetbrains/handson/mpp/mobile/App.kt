package com.jetbrains.handson.mpp.mobile

import android.app.Application
import com.jetbrains.handson.mpp.databes.Servers
import com.jetbrains.handson.mpp.mobile.base.initApplication
import com.squareup.sqldelight.android.AndroidSqliteDriver

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initApplication(
            AndroidSqliteDriver(
                Servers.Schema,
                applicationContext,
                "servers.db"
            )
        )
    }
}
