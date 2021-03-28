package com.example.pix

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pix.gallery.GalleryAdapter
import com.example.pix.gallery.Image

@BindingAdapter("img")
fun bindImage(imageView: ImageView, imgUri: Uri?){

    imgUri?.let {

        Glide.with(imageView.context)
            .load(imgUri)
            .into(imageView)
    }
}