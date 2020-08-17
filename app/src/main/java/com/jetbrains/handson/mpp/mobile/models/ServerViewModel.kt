package com.jetbrains.handson.mpp.mobile.models

import androidx.lifecycle.ViewModel
import com.jetbrains.handson.mpp.mobile.db.Server

data class ServerViewModel(
    var title: String? = null,
    var url: String? = null,
    var token: String? = null
): ViewModel() {

    fun setModel(server: Server) : ServerViewModel {
        title = server.title.toString()
        url = server.url.toString()
        token = server.token.toString()

        return this
    }
}