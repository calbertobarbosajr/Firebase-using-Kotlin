package com.carlos_alberto.firebasemvvm.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.Executors

class StorageUploadViewModel(application: Application) : AndroidViewModel(application) {

    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val imageUris = MutableLiveData<List<Uri>>()
    private val uploadStatus = MutableLiveData<UploadStatus>()

    enum class UploadStatus {
        SUCCESS, ERROR
    }

    fun getImageUris(): LiveData<List<Uri>> {
        return imageUris
    }

    fun getUploadStatus(): LiveData<UploadStatus> {
        return uploadStatus
    }

    fun uploadImages(imageUris: List<Uri>) {
        val user = auth.currentUser
        user?.let {
            val uid: String = user.uid

            Executors.newSingleThreadExecutor().execute {
                try {
                    imageUris.forEachIndexed { index, uri ->
                        val reference = storage.reference.child("images").child(uid)
                        val nome_imagem = reference.child("Name_$index.jpg")

                        val bytes = getBytesFromUri(uri)
                        val uploadTask = nome_imagem.putBytes(bytes)

                        uploadTask.continueWithTask { task ->
                            if (!task.isSuccessful) {
                                task.exception?.let { throw it }
                            }
                            nome_imagem.downloadUrl
                        }.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Adapte conforme necessário
                                uploadStatus.postValue(UploadStatus.SUCCESS)
                            } else {
                                // Adapte conforme necessário
                                uploadStatus.postValue(UploadStatus.ERROR)
                            }
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    // Adapte conforme necessário
                    uploadStatus.postValue(UploadStatus.ERROR)
                }
            }
        }
    }

    // Converte Uri em ByteArray
    @Throws(IOException::class)
    private fun getBytesFromUri(uri: Uri): ByteArray {
        val inputStream: InputStream? = getApplication<Application>().contentResolver.openInputStream(uri)
        inputStream.use {
            return it?.readBytes() ?: ByteArray(0)
        }
    }
}