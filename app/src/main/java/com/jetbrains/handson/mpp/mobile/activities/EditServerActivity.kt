package com.jetbrains.handson.mpp.mobile.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.jetbrains.handson.mpp.mobile.R
import com.jetbrains.handson.mpp.mobile.myMppApp
import com.jetbrains.handson.mpp.mobile.databinding.ActivityFormServerBinding
import com.jetbrains.handson.mpp.mobile.models.ServerFormViewModel
import com.jetbrains.handson.mpp.mobile.repositories.ServersRepository
import kotlinx.android.synthetic.main.activity_form_server.*
import kotlinx.coroutines.launch
import org.kodein.di.erased.instance

class EditServerActivity : AppCompatActivity() {

    private val repository: ServersRepository by myMppApp.kodein.instance()
    private var serverFormViewModel = ServerFormViewModel()
    private var serverId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityFormServerBinding = DataBindingUtil.setContentView(this, R.layout.activity_form_server)

        serverId = intent.getLongExtra("ID", 0)
        if (serverId > 0) {
            formTitle.text = getString(R.string.edit_server)
            lifecycleScope.launch {
                binding.progressBar.visibility = View.VISIBLE
                repository.get(serverId).let { server ->
                    serverFormViewModel.setForm(server)
                }
            }.also {
                binding.progressBar.visibility = View.GONE
            }
        }
        binding.item = serverFormViewModel

        saveBtn.setOnClickListener {
            if (serverFormViewModel.isFormValid()) {
                lifecycleScope.launch {
                    saveRecord(serverFormViewModel)
                }.also {
                    setResult(Activity.RESULT_OK, Intent())
                    finish()
                }
            } else {
//                Utils.snackMsg(editView, getString(R.string.error_empty_field))
            }
        }

        cancelBtn.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private fun saveRecord(viewModel: ServerFormViewModel) {
        progressBar.visibility = View.VISIBLE
        try {
            repository.save(serverId, viewModel.getModel(serverId))
        } catch (e: Exception) {
//            Utils.snackMsg(editView, e.message.toString())
        } finally {
            progressBar.visibility = View.GONE
        }
    }
}