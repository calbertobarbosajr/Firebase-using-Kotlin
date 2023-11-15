package com.carlos_alberto.firebasemvvm.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.carlos_alberto.firebasemvvm.databinding.ActivityOptionRealtimeDatabaseBinding

class OptionRealtimeDatabaseActivity : AppCompatActivity() {

        private lateinit var binding: ActivityOptionRealtimeDatabaseBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            binding = ActivityOptionRealtimeDatabaseBinding.inflate(layoutInflater)
            setContentView(binding.root)
        }

        fun buttonRealTimeRead(view: View) {
            val intent = Intent(this, RealtimeReadActivity::class.java)
            startActivity(intent)
        }

        fun buttonRealTimeSave(view: View) {
            val intent = Intent(this, RealtimeSaveActivity::class.java)
            startActivity(intent)
        }
}