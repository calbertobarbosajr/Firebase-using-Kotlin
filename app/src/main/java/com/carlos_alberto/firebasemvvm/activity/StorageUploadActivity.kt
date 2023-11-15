package com.carlos_alberto.firebasemvvm.activity

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.carlos_alberto.firebasemvvm.R
import com.carlos_alberto.firebasemvvm.databinding.ActivityStorageUploadBinding
import com.carlos_alberto.firebasemvvm.helper.Permissions
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.concurrent.Executors
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth

class StorageUploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStorageUploadBinding
    private val imageUris = mutableListOf<Uri>()
    private var uri_imagem: Uri? = null
    private val storage: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }
    private lateinit var showImage1: ImageView
    private lateinit var showImage2: ImageView
    private lateinit var showImage3: ImageView
    private var selectedImageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStorageUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showImage1 = findViewById(R.id.showImage1)
        showImage2 = findViewById(R.id.showImage2)
        showImage3 = findViewById(R.id.showImage3)

        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val permissionsHelper = Permissions()
        permissionsHelper.validatePermissions(
            permissions,
            this,
            Permissions.REQUEST_PERMISSIONS_CODE
        )
    }

    fun buttonUpload(view: View) {
        uploadImages()
    }

    fun showImage1(view: View) {
        selectedImageView = showImage1
        showCameraGalleryDialog()
    }

    fun showImage2(view: View) {
        selectedImageView = showImage2
        showCameraGalleryDialog()
    }

    fun showImage3(view: View) {
        selectedImageView = showImage3
        showCameraGalleryDialog()
    }

    private fun showCameraGalleryDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Camera or Gallery?")
            .setTitle("Choose!")
            .setCancelable(false)
            .setPositiveButton("Camera") { dialog, which ->
                itemCamera()
            }
            .setNegativeButton("Gallery") { dialog, which ->
                obterImagemGaleria()
            }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun uploadImages() {
        val user = Firebase.auth.currentUser
        user?.let {
            val uid: String = user.uid

            imageUris.forEachIndexed { index, uri ->
                val reference = storage.reference.child("images").child(uid)
                val nome_imagem = reference.child("Name_$index.jpg")

                Executors.newSingleThreadExecutor().execute {
                    try {
                        val bytes = ByteArrayOutputStream()
                        val inputStream = contentResolver.openInputStream(uri)
                        val buffer = ByteArray(1024)
                        var bytesRead: Int
                        while (inputStream!!.read(buffer).also { bytesRead = it } != -1) {
                            bytes.write(buffer, 0, bytesRead)
                        }

                        bytes.close()
                        inputStream.close()

                        val byteArray = bytes.toByteArray()

                        val uploadTask = nome_imagem.putBytes(byteArray)
                        uploadTask.continueWithTask { task ->
                            if (!task.isSuccessful) {
                                task.exception?.let { throw it }
                            }
                            nome_imagem.downloadUrl
                        }.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val uri = task.result
                                val url_imagem = uri.toString()
                                Toast.makeText(
                                    baseContext,
                                    "Upload Success",
                                    Toast.LENGTH_LONG
                                ).show()
                                finish()
                            } else {
                                Toast.makeText(
                                    baseContext,
                                    "Error when uploading",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }



    private fun itemCamera() {
        if (ContextCompat.checkSelfPermission(
                baseContext,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            obterImagemCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_CAMERA_CODE
            )
        }
    }

    private fun obterImagemGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        getContent.launch(intent)
    }

    private fun obterImagemCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val diretorio =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val nomeImagem = "$diretorio/Name_${System.currentTimeMillis()}.jpg"
        val file = File(nomeImagem)
        val autorizacao = "com.carlos_alberto.firebasemvvm"
        val uriImagem = FileProvider.getUriForFile(baseContext, autorizacao, file)
        uri_imagem = uriImagem
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri_imagem)
        getCameraImage.launch(intent)
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    uri_imagem = data.data
                    loadImageIntoImageView(selectedImageView)
                } else {
                    Toast.makeText(
                        baseContext,
                        "Failed to select image",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    private val getCameraImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                loadImageIntoImageView(selectedImageView)
            } else {
                Toast.makeText(baseContext, "Failed to take photo", Toast.LENGTH_LONG).show()
            }
        }

    private fun loadImageIntoImageView(imageView: ImageView?) {
        uri_imagem?.let {
            imageView?.let { imageView ->
                Glide.with(baseContext)
                    .asBitmap()
                    .load(it)
                    .listener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Toast.makeText(
                                baseContext,
                                "Error loading image",
                                Toast.LENGTH_LONG
                            ).show()
                            return false
                        }

                        override fun onResourceReady(
                            resource: Bitmap?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }
                    })
                    .into(imageView)
                imageUris.add(it)
            }
        }
    }

    companion object {
        private const val PERMISSION_CAMERA_CODE = 1001
    }



    //===============================================================================================
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (arePermissionsDenied(grantResults)) {
            showPermissionDeniedAlert()
        }
    }

    private fun arePermissionsDenied(grantResults: IntArray): Boolean {
        return grantResults.any { it == PackageManager.PERMISSION_DENIED }
    }

    private fun showPermissionDeniedAlert() {
        AlertDialog.Builder(this)
            .setTitle("Permissions Denied")
            .setMessage("To use the app, you need to accept the required permissions.")
            .setCancelable(false)
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, _ -> finish() })
            .create()
            .show()
    }




}