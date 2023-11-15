package com.carlos_alberto.firebasemvvm.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carlos_alberto.firebasemvvm.R
import com.carlos_alberto.firebasemvvm.model.ImageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class ImageAdapter(
    private val context: Context,
    private val images: MutableList<ImageModel>,
    private val storage: FirebaseStorage
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    lateinit var recyclerView: RecyclerView

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    private fun showDialog(position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("Choose an action:")
            .setTitle("Image Options")
            .setCancelable(true)
            .setPositiveButton("Share Image") { dialog, which ->
                val imageView = (recyclerView.findViewHolderForAdapterPosition(position) as ImageViewHolder).imageView
                shareImage(imageView)
            }
            .setNegativeButton("Remove Image") { dialog, which ->
                removeImage(position)
            }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun shareImage(imageView: ImageView) {
        val drawable = imageView.drawable as BitmapDrawable
        val bitmap = drawable.bitmap
        val uri = getImageUri(context, bitmap)

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/jpeg"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        context.startActivity(Intent.createChooser(intent, "Share Image"))
    }

    private fun removeImage(position: Int) {
        val currentImage = images[position]
        val storageReference = storage.getReferenceFromUrl(currentImage.imageUrl)

        storageReference.delete()
            .addOnSuccessListener {
                // Imagem excluída com sucesso do Firebase Storage
                images.removeAt(position)
                notifyItemRemoved(position)
                Toast.makeText(context, "Image removed successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                // Falha ao excluir a imagem
                Toast.makeText(context, "Failed to remove image: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        recyclerView = parent as RecyclerView
        return ImageViewHolder(itemView).apply {
            itemView.setOnClickListener {
                showDialog(adapterPosition)
            }
        }
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentImage = images[position]
        Glide.with(context)
            .load(currentImage.imageUrl)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    private fun getImageUri(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Shared Image", null)
        return Uri.parse(path)
    }

    private fun getNameFromUrl(url: String): String {
        return url.substringAfterLast("/", url)
    }
}
