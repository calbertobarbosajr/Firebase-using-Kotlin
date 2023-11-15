package com.carlos_alberto.firebasemvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carlos_alberto.firebasemvvm.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class RealtimeReadViewModel : ViewModel() {

    private val usersReference = FirebaseDatabase.getInstance().getReference("users")
    private val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    private val _userLiveData = MutableLiveData<User>()
    val userLiveData: LiveData<User> get() = _userLiveData
    private val _errorMessageLiveData = MutableLiveData<String>()
    val errorMessageLiveData: LiveData<String> get() = _errorMessageLiveData

    private val valueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val user: User? = snapshot.getValue(User::class.java)
            _userLiveData.value = user
        }

        override fun onCancelled(error: DatabaseError) {
            _errorMessageLiveData.value = "Error reading data"
        }
    }

    fun attachDatabaseListener() {
        currentUser?.let {
            val userReference = usersReference.child(it.uid)
            userReference.addValueEventListener(valueEventListener)
        }
    }

    fun detachDatabaseListener() {
        currentUser?.let {
            val userReference = usersReference.child(it.uid)
            userReference.removeEventListener(valueEventListener)
        }
    }
}