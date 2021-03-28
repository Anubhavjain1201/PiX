package com.example.pix.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pix.databinding.ListItemImageBinding

class GalleryAdapter(private val imagesList: MutableList<Image>): RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>(){

    class GalleryViewHolder(private var binding: ListItemImageBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(image: Image){
            binding.image = image
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        return GalleryViewHolder(ListItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {

        val image = imagesList[position]
        holder.bind(image)

    }
}