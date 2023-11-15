package com.carlos_alberto.firebasemvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.carlos_alberto.firebasemvvm.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreSaveViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = "users"

    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    fun addDocumentToFirestore(user: User, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        getCurrentUser()?.let { currentUser ->
            val uid = currentUser.uid

            db.collection(usersCollection).document(uid)
                .set(user)
                .addOnSuccessListener {
                    onSuccess.invoke()
                }
                .addOnFailureListener { e ->
                    onFailure.invoke("Error adding document: $e")
                }
        }
    }

    fun updateDocumentInFirestore(updates: Map<String, Any>, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        getCurrentUser()?.let { currentUser ->
            val uid = currentUser.uid
            val userRef = db.collection(usersCollection).document(uid)

            if (updates.isNotEmpty()) {
                userRef
                    .update(updates)
                    .addOnSuccessListener {
                        onSuccess.invoke()
                    }
                    .addOnFailureListener { e ->
                        onFailure.invoke("Error updating document: $e")
                    }
            } else {
                onFailure.invoke("No fields have been modified.")
            }
        }
    }

    fun deleteDocumentInFirestore(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        getCurrentUser()?.let { currentUser ->
            val uid = currentUser.uid
            db.collection(usersCollection).document(uid)
                .delete()
                .addOnSuccessListener {
                    onSuccess.invoke()
                }
                .addOnFailureListener { e ->
                    onFailure.invoke("Error deleting document: $e")
                }
        }
    }
}