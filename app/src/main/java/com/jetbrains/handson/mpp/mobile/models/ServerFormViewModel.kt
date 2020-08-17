package com.jetbrains.handson.mpp.mobile.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.jetbrains.handson.mpp.mobile.db.Server


class ServerFormViewModel : BaseObservable() {
    private var serverModel = ServerViewModel()

    fun setForm(server: Server) {
        serverModel.setModel(server)
    }

    @Bindable
    fun getTitle() : String {
        return serverModel.title ?: ""
    }

    fun setTitle(value: String) {
        serverModel.title = value
        notifyChange()
    }

    @Bindable
    fun getUrl() : String {
        return serverModel.url ?: ""
    }

    fun setUrl(value: String) {
        serverModel.url = value
        notifyChange()
    }

    @Bindable
    fun getToken() : String {
        return serverModel.token ?: ""
    }

    fun setToken(value: String) {
        serverModel.token = value
        notifyChange()
    }

    fun isFormValid() : Boolean {
        return !serverModel.title.isNullOrEmpty() and !serverModel.url.isNullOrEmpty() and !serverModel.token.isNullOrEmpty()
    }

    fun getModel(id: Long?) : Server {
        return Server(id!!, serverModel.title, serverModel.url, serverModel.token)
    }
}