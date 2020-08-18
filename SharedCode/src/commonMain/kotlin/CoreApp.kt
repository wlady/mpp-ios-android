package com.jetbrains.handson.mpp.mobile

import com.jetbrains.handson.mpp.mobile.base.getSqlDriver
import com.jetbrains.handson.mpp.mobile.repositories.ServersRepository
import com.squareup.sqldelight.db.SqlDriver
import io.ktor.client.HttpClient
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.singleton

class CoreApp(sqlDriver: SqlDriver) {
    var kodein = Kodein {
        bind() from singleton { ServersRepository(sqlDriver) }
        bind() from singleton { HttpClient() }
    }
}

var isInitialized = false
    private set
lateinit var app: CoreApp
    private set

fun initApplication(sqlDriver: SqlDriver? = null) {
    if (!isInitialized) {
        app = CoreApp(sqlDriver ?: getSqlDriver("servers.db"))
        isInitialized = true
    }
}
