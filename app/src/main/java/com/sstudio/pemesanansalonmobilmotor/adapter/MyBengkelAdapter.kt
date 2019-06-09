package com.sstudio.pemesanansalonmobilmotor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sstudio.pemesanansalonmobilmotor.Model.Bengkel
import com.sstudio.pemesanansalonmobilmotor.R
import kotlinx.android.synthetic.main.layout_bengkel.view.*

class MyBengkelAdapter(val context: Context, val bengkelList: List<Bengkel>):
    RecyclerView.Adapter<MyBengkelAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.layout_bengkel, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return bengkelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_bengkel_akun.text = bengkelList[position].nama
        holder.rating_bengkel.rating = bengkelList[position].rating.toFloat()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_bengkel_akun = itemView.tv_bengkel_nama
        val rating_bengkel = itemView.rating_bengkel
    }
}