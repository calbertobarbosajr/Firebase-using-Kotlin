package com.carlos_alberto.firebasemvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carlos_alberto.firebasemvvm.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreReadViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = "users"

    val userLiveData: MutableLiveData<User?> = MutableLiveData()
    val errorMessageLiveData: MutableLiveData<String?> = MutableLiveData()

    fun readDocument() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val uid = user.uid

            db.collection(usersCollection).document(uid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    handleDocumentSnapshot(documentSnapshot)
                }
                .addOnFailureListener { exception ->
                    handleFailure(exception)
                }
        }
    }

    private fun handleDocumentSnapshot(documentSnapshot: DocumentSnapshot) {
        if (documentSnapshot.exists()) {
            val user = documentSnapshot.toObject(User::class.java)
            userLiveData.postValue(user)
        } else {
            errorMessageLiveData.postValue("Document not found")
        }
    }

    private fun handleFailure(exception: Exception) {
        errorMessageLiveData.postValue("Error reading document: ${exception.message}")
    }
}