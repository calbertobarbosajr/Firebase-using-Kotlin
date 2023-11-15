package com.carlos_alberto.firebasemvvm.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.carlos_alberto.firebasemvvm.databinding.ActivityFirestoreSaveBinding
import com.carlos_alberto.firebasemvvm.model.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import androidx.lifecycle.ViewModelProvider
import com.carlos_alberto.firebasemvvm.viewmodel.FirestoreSaveViewModel

class FirestoreSaveActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirestoreSaveBinding
    private lateinit var viewModel: FirestoreSaveViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirestoreSaveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(FirestoreSaveViewModel::class.java)
    }

    private fun showMessage(message: String) {
        binding.textViewMessageFirestore.text = message
    }

    private fun getUserFromUI(): User {
        return User(
            binding.editTextNameFirestore.text.toString(),
            binding.editTextCpfFirestore.text.toString(),
            binding.editTextEmailFirestore.text.toString()
        )
    }

    fun buttonSaveFirestore(view: View) {
        val user = getUserFromUI()
        viewModel.addDocumentToFirestore(user,
            onSuccess = { showMessage("Document added successfully!") },
            onFailure = { showMessage(it) }
        )
    }

    fun buttonUpdateFirestore(view: View) {
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

        viewModel.updateDocumentInFirestore(updates,
            onSuccess = { showMessage("Document updated successfully!") },
            onFailure = { showMessage(it) }
        )
    }

    fun buttonDeleteFirestore(view: View) {
        viewModel.deleteDocumentInFirestore(
            onSuccess = { showMessage("Document deleted successfully!") },
            onFailure = { showMessage(it) }
        )
    }
}
