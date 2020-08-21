package com.jetbrains.handson.mpp.mobile

import com.jetbrains.handson.mpp.mobile.base.getSqlDriver
import com.jetbrains.handson.mpp.mobile.repositories.ServersRepository
import com.squareup.sqldelight.db.SqlDriver
import io.ktor.client.HttpClient
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

class CoreApp(sqlDriver: SqlDriver) {
    var kodein = DI {
        bind() from singleton { ServersRepository(sqlDriver) }
        bind() from singleton { HttpClient() }
    }
}

var myAppIsInitialized = false
    private set
lateinit var myMppApp: CoreApp
    private set

fun initApplication(sqlDriver: SqlDriver? = null) {
    if (!myAppIsInitialized) {
        myMppApp = CoreApp(sqlDriver ?: getSqlDriver("servers.db"))
        myAppIsInitialized = true
    }
}
