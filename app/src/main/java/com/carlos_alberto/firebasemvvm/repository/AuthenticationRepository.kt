package com.calberto.firebasewithkotlin.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class AuthenticationRepository {

    lateinit var returnAuthenticationRepository: Task<AuthResult>

    fun doLogin(email: String, password: String) {
        returnAuthenticationRepository = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
    }
}
