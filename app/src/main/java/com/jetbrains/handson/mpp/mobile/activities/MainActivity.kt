package com.jetbrains.handson.mpp.mobile.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.jetbrains.handson.mpp.mobile.R
import com.jetbrains.handson.mpp.mobile.adapters.ServersAdapter
import com.jetbrains.handson.mpp.mobile.base.myApp
import com.jetbrains.handson.mpp.mobile.db.Server
import com.jetbrains.handson.mpp.mobile.models.ServerViewModel
import org.kodein.di.erased.instance
import com.jetbrains.handson.mpp.mobile.repositories.ServersRepository
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        const val EDITED = 101
        const val DETAILS = 102
        const val CONNECTION_ERROR = 2
    }

    private val repository : ServersRepository by myApp.kodein.instance()
    lateinit var adapter: ServersAdapter
    lateinit var serversList: List<Server>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            val intent = Intent(applicationContext, EditServerActivity::class.java)
            startActivityForResult(intent, EDITED)
        }

        val viewModel = ViewModelProvider.Factory(ServerViewModel::class.java)
        lifecycleScope.launch {
            progressBar.visibility = View.VISIBLE
            serversList = repository.getAll()
        }.also {
            adapter = ServersAdapter(
                serversList,
                object : ServersAdapter.ClickCallback {
                    override fun onItemClicked(item: Server) {
//                        val intent = Intent(applicationContext, DetailsActivity::class.java)
//                        intent.putExtra("ID", item.ID)
//                        startActivityForResult(intent, DETAILS)
                    }
                },
                object : ServersAdapter.EditClickCallback {
                    override fun onEditItemClicked(item: Server) {
                        val intent = Intent(applicationContext, EditServerActivity::class.java)
                        intent.putExtra("ID", item.ID)
                        startActivityForResult(intent, EDITED)
                    }
                },
                object :
                    ServersAdapter.DeleteClickCallback {
                    override fun onDeleteItemClicked(item: Server) {
                        AlertDialog.Builder(this@MainActivity)
                            .setTitle(getString(R.string.confirm_delete, item.title))
                            .setCancelable(true)
                            .setPositiveButton(android.R.string.yes) { _, _ ->
                                try {
                                    repository.delete(item.ID).also {
                                        adapter.items = repository.getAll()
                                        adapter.notifyDataSetChanged()
//                                        Utils.snackMsg(mainView, getString(R.string.deleted))
                                    }
                                } catch (e: Exception) {
//                                    Utils.snackMsg(mainView, e.message.toString())
                                }
                            }
                            .setNegativeButton(android.R.string.no) { _, _ ->
                                // nothing to do
                            }
                            .show()
                    }
                }
            )
            servers.adapter = adapter
            progressBar.visibility = View.GONE
        }

        swipeContainer.setOnRefreshListener {
            adapter.items = repository.getAll()
            adapter.notifyDataSetChanged()
            if (swipeContainer.isRefreshing) {
                swipeContainer.isRefreshing = false
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDITED && resultCode == Activity.RESULT_OK) {
            adapter.items = repository.getAll()
            adapter.notifyDataSetChanged()
        } else if (requestCode == DETAILS && resultCode == CONNECTION_ERROR) {
            val message = data!!.getStringExtra("message")
//            Utils.snackMsg(mainView, message!!)
            println(message)
        }
    }
}
