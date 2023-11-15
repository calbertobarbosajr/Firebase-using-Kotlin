package com.carlos_alberto.firebasemvvm.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.carlos_alberto.firebasemvvm.databinding.ActivityRealtimeReadBinding
import com.carlos_alberto.firebasemvvm.model.User
import com.carlos_alberto.firebasemvvm.viewmodel.RealtimeReadViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class RealtimeReadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRealtimeReadBinding
    private lateinit var viewModel: RealtimeReadViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRealtimeReadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(RealtimeReadViewModel::class.java)

        // Observa as atualizações no usuário e na mensagem de erro
        viewModel.userLiveData.observe(this, Observer { user ->
            updateUI(user)
        })

        viewModel.errorMessageLiveData.observe(this, Observer { errorMessage ->
            showToast(errorMessage)
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun updateUI(user: User) {
        with(binding) {
            textViewNameRealtime.text = "Nome: ${user.name}"
            textViewCpfRealtime.text = "CPF: ${user.cpf}"
            textViewEmailRealtime.text = "E-mail: ${user.email}"
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.attachDatabaseListener()
    }

    override fun onStop() {
        super.onStop()
        viewModel.detachDatabaseListener()
    }
}