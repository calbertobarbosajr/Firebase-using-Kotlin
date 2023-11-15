package com.carlos_alberto.firebasemvvm.viewModel

import android.app.Activity
import androidx.lifecycle.LiveData
import com.calberto.firebasewithkotlin.repository.AuthenticationRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carlos_alberto.firebasemvvm.helper.OpenActivity
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class AuthenticationViewModel : ViewModel() {

    private val showMessageError = MutableLiveData<String>()
    private val authenticationRepository = AuthenticationRepository()

    fun errorMessage(): LiveData<String> {
        return showMessageError
    }

    fun login(activity: Activity, email: String, password: String) {
        authenticationRepository.doLogin(email, password)
        authenticationRepository.returnAuthenticationRepository.addOnSuccessListener {
            OpenActivity().signup(activity)
        }.addOnFailureListener { handleLoginError(it) }
    }

    private fun handleLoginError(exception: Exception) {
        showMessageError.value = when (exception) {
            is FirebaseNetworkException -> "No internet connection!"
            is FirebaseAuthInvalidUserException -> "User not registered"
            is FirebaseAuthInvalidCredentialsException -> "Email or Password is incorrect"
            else -> "User login error"
        }
    }
}
