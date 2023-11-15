package com.carlos_alberto.firebasemvvm.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.carlos_alberto.firebasemvvm.databinding.ActivityFirestoreSaveBinding
import com.carlos_alberto.firebasemvvm.model.User
import com.carlos_alberto.firebasemvvm.viewmodel.RealtimeViewModel

class RealtimeSaveActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirestoreSaveBinding
    private lateinit var viewModel: RealtimeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirestoreSaveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(RealtimeViewModel::class.java)

        // Observa as atualizações na mensagem e atualiza a interface do usuário
        viewModel.messageLiveData.observe(this, Observer {
            showMessage(it)
        })
    }

    private fun showMessage(message: String) {
        binding.textViewMessageFirestore.text = message
    }

    private fun getUserFromInputFields(): User {
        with(binding) {
            val name = editTextNameFirestore.text.toString()
            val cpf = editTextCpfFirestore.text.toString()
            val email = editTextEmailFirestore.text.toString()
            return User(name, cpf, email)
        }
    }

    private fun getFieldUpdates(): Map<String, Any> {
        val updates = mutableMapOf<String, Any>()
        with(binding) {
            editTextNameFirestore.text.toString().takeIf { it.isNotEmpty() }?.let {
                updates["name"] = it
            }
            editTextCpfFirestore.text.toString().takeIf { it.isNotEmpty() }?.let {
                updates["cpf"] = it
            }
            editTextEmailFirestore.text.toString().takeIf { it.isNotEmpty() }?.let {
                updates["email"] = it
            }
        }
        return updates
    }

    fun buttonSaveFirestore(view: View) {
        val user = getUserFromInputFields()
        viewModel.addDocumentToRealtimeDatabase(user)
    }

    fun buttonUpdateFirestore(view: View) {
        val updates = getFieldUpdates()
        viewModel.updateDocumentInRealtimeDatabase(updates)
    }

    fun buttonDeleteFirestore(view: View) {
        viewModel.deleteDocumentInRealtimeDatabase()
    }
}