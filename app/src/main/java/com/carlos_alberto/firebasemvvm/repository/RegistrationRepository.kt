package com.carlos_alberto.firebasemvvm.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class RegistrationRepository {

    lateinit var returnRegistrationRepository: Task<AuthResult>

    fun doSignup(email: String, password: String) {
        returnRegistrationRepository = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
    }
}
