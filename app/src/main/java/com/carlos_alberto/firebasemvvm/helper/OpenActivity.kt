package com.carlos_alberto.firebasemvvm.helper

import android.app.Activity
import android.content.Intent
import com.carlos_alberto.firebasemvvm.activity.MainActivity

class OpenActivity {

    fun signup( activity: Activity ) {
        val intent = Intent(activity, MainActivity::class.java)
        activity.startActivity(intent)
    }

    fun login( activity: Activity ) {
        val intent = Intent(activity, MainActivity::class.java)
        activity.startActivity(intent)
    }
}