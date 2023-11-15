package com.carlos_alberto.firebasemvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carlos_alberto.firebasemvvm.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RealtimeViewModel : ViewModel() {

    private val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")
    private val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    private val _messageLiveData = MutableLiveData<String>()
    val messageLiveData: LiveData<String> get() = _messageLiveData

    fun addDocumentToRealtimeDatabase(user: User) {
        currentUser?.let {
            val uid: String = it.uid

            usersRef.child(uid)
                .setValue(user)
                .addOnSuccessListener {
                    _messageLiveData.value = "Document added successfully!"
                }
                .addOnFailureListener { e ->
                    _messageLiveData.value = "Error adding document: $e"
                }
        }
    }

    fun updateDocumentInRealtimeDatabase(updates: Map<String, Any>) {
        currentUser?.let {
            val uid: String = it.uid
            val userRef = usersRef.child(uid)

            if (updates.isNotEmpty()) {
                userRef.updateChildren(updates)
                    .addOnSuccessListener {
                        _messageLiveData.value = "Document updated successfully!"
                    }
                    .addOnFailureListener { e ->
                        _messageLiveData.value = "Error updating document: $e"
                    }
            } else {
                _messageLiveData.value = "No fields have been modified."
            }
        }
    }

    fun deleteDocumentInRealtimeDatabase() {
        currentUser?.let {
            val uid: String = it.uid

            usersRef.child(uid)
                .removeValue()
                .addOnSuccessListener {
                    _messageLiveData.value = "Document deleted successfully!"
                }
                .addOnFailureListener { e ->
                    _messageLiveData.value = "Error deleting document: $e"
                }
        }
    }
}