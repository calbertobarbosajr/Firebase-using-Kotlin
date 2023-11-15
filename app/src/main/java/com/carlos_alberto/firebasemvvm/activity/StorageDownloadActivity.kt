package com.carlos_alberto.firebasemvvm.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.carlos_alberto.firebasemvvm.databinding.ActivityStorageDownloadBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carlos_alberto.firebasemvvm.adapter.ImageAdapter
import com.carlos_alberto.firebasemvvm.model.ImageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class StorageDownloadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStorageDownloadBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    private val imageList = mutableListOf<ImageModel>()
    private val storage = Firebase.storage
    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStorageDownloadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        imageAdapter = ImageAdapter(this, imageList, storage)
        recyclerView.adapter = imageAdapter

        currentUser?.let { user ->
            val uid: String = user.uid
            val reference = storage.reference.child("images").child(uid)

            reference.listAll()
                .addOnSuccessListener { result ->
                    result.items.forEach { imageRef ->
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            val imageUrl = uri.toString()
                            imageList.add(ImageModel(imageUrl))
                            imageAdapter.notifyDataSetChanged()
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Download failed: ${exception.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}