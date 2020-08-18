package com.jetbrains.handson.mpp.mobile.base

import com.squareup.sqldelight.db.SqlDriver

expect fun getSqlDriver(databaseName: String): SqlDriver

