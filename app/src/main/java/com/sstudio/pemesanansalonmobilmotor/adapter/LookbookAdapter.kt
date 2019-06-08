package com.sstudio.pemesanansalonmobilmotor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.sstudio.pemesanansalonmobilmotor.Model.Banner
import com.sstudio.pemesanansalonmobilmotor.R
import kotlinx.android.synthetic.main.layout_lookbook.view.*

class LookbookAdapter(val context: Context, val lookbook: List<Banner>): RecyclerView.Adapter<LookbookAdapter.viewHolder>() {

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.iv_lookbook

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.layout_lookbook,parent,false)
        return viewHolder(itemView)
    }

    override fun getItemCount(): Int = lookbook.size

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        Picasso.get().load(lookbook.get(position).image).into(holder.imageView)
    }


}