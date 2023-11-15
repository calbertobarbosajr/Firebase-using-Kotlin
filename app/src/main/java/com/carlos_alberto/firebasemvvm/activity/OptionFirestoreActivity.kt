package com.carlos_alberto.firebasemvvm.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.carlos_alberto.firebasemvvm.databinding.ActivityOptionFirestoreBinding

class OptionFirestoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOptionFirestoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOptionFirestoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun buttonFirestoreRead(view: View) {
        val intent = Intent(this, FirestoreReadActivity::class.java)
        startActivity(intent)
    }

    fun buttonFirestoreSave(view: View) {
        val intent = Intent(this, FirestoreSaveActivity::class.java)
        startActivity(intent)
    }
}