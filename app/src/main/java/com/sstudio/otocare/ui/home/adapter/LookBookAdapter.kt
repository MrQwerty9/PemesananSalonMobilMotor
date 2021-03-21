package com.sstudio.otocare.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.sstudio.core.domain.model.Banner
import com.sstudio.otocare.databinding.ItemLookbookBinding

class LookBookAdapter(
    val context: Context, val lookbook: List<Banner>
) : RecyclerView.Adapter<LookBookAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = ItemLookbookBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = lookbook.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(lookbook.get(position).image).into(holder.imageView)
    }

    inner class ViewHolder(binding: ItemLookbookBinding) : RecyclerView.ViewHolder(binding.root) {
        var imageView: ImageView = binding.ivLookbook

    }
}