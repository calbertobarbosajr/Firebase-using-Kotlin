package com.carlos_alberto.firebasemvvm.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.carlos_alberto.firebasemvvm.databinding.ActivityFirestoreReadBinding
import com.carlos_alberto.firebasemvvm.model.User
import com.carlos_alberto.firebasemvvm.viewmodel.FirestoreReadViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class FirestoreReadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirestoreReadBinding
    private lateinit var viewModel: FirestoreReadViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirestoreReadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(FirestoreReadViewModel::class.java)

        viewModel.userLiveData.observe(this, Observer { user ->
            user?.let { displayUserInfo(it) }
        })

        viewModel.errorMessageLiveData.observe(this, Observer { errorMessage ->
            errorMessage?.let { showToast(it) }
        })

        viewModel.readDocument()
    }

    private fun displayUserInfo(user: User) {
        with(binding) {
            textViewNameFirestore.text = "Nome: ${user.name}"
            textViewCpfFirestore.text = "CPF: ${user.cpf}"
            textViewEmailFirestore.text = "E-mail: ${user.email}"
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}