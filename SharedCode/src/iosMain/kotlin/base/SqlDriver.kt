package com.jetbrains.handson.mpp.mobile.base

import com.jetbrains.handson.mpp.databes.Servers
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual fun getSqlDriver(databaseName: String): SqlDriver = NativeSqliteDriver(Servers.Schema, databaseName)
