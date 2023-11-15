package com.carlos_alberto.firebasemvvm.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.carlos_alberto.firebasemvvm.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun buttonStorage(view: View) {
        val intent = Intent(this, OptionStorageActivity::class.java)
        startActivity(intent)
    }

    fun buttonDatabase(view: View) {
        val intent = Intent(this, OptionRealtimeDatabaseActivity::class.java)
        startActivity(intent)
    }

    fun buttonFirestore(view: View) {
        val intent = Intent(this, OptionFirestoreActivity::class.java)
        startActivity(intent)
    }

    fun buttonExit(view: View) {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, AuthenticationActivity::class.java)
        startActivity(intent)
        finish()
    }
}