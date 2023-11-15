package com.carlos_alberto.firebasemvvm.viewModel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carlos_alberto.firebasemvvm.helper.OpenActivity
import com.carlos_alberto.firebasemvvm.repository.RegistrationRepository
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class RegistrationViewModel : ViewModel() {

    private val showMessageError = MutableLiveData<String>()
    private val registrationRepository = RegistrationRepository()

    fun errorMessage(): LiveData<String> {
        return showMessageError
    }

    fun createUser(activity: Activity, email: String, password: String) {
        registrationRepository.doSignup(email, password)
        registrationRepository.returnRegistrationRepository.addOnSuccessListener {
            OpenActivity().signup(activity)
        }.addOnFailureListener { handleRegistrationError(it) }
    }

    private fun handleRegistrationError(exception: Exception) {
        showMessageError.value = when (exception) {
            is FirebaseAuthWeakPasswordException -> "Enter a password of at least 5 characters"
            is FirebaseAuthInvalidCredentialsException -> "Enter valid email"
            is FirebaseAuthUserCollisionException -> "This account has already been registered"
            is FirebaseNetworkException -> "No internet connection!"
            else -> "Error registering user"
        }
    }
}
