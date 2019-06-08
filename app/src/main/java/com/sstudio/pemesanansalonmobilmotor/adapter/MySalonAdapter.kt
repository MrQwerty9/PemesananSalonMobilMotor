package com.sstudio.pemesanansalonmobilmotor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sstudio.pemesanansalonmobilmotor.Model.Salon
import com.sstudio.pemesanansalonmobilmotor.R
import kotlinx.android.synthetic.main.layout_salon.view.*

class MySalonAdapter(val context: Context, val salonList: List<Salon>): RecyclerView.Adapter<MySalonAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.layout_salon, parent, false)
        return viewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return salonList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.tv_salon_name.text = salonList[position].nama
        holder.tv_salon_address.text = salonList[position].alamat
    }

    class viewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val tv_salon_name = view.tv_salon_name
        val tv_salon_address = view.tv_salon_address
    }
}