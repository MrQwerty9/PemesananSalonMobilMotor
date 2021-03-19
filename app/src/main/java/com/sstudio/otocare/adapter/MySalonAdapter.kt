package com.sstudio.otocare.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.sstudio.otocare.Model.Salon
import com.sstudio.otocare.R
import com.sstudio.otocare.common.Common
import com.sstudio.otocare.listener.IRecyclerItemSelectedListener
import kotlinx.android.synthetic.main.layout_salon.view.*

class MySalonAdapter(val context: Context, val salonList: List<Salon>): RecyclerView.Adapter<MySalonAdapter.viewHolder>() {

    var cardViewList = ArrayList<CardView>()
    val localBroadcastManager = LocalBroadcastManager.getInstance(context)

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
        if (!cardViewList.contains(holder.card_salon))
            cardViewList.add(holder.card_salon)

        holder.setiRecyclerItemSelectedListener(object : IRecyclerItemSelectedListener {
            override fun onItemSelectedListener(view: View, pos: Int) {
                //set background putin untuk semua card tdk dipilih
                for (cardView: CardView in cardViewList)
                    cardView.setCardBackgroundColor(context.resources.getColor(android.R.color.white))

                //set warna dipilih
                holder.card_salon.setCardBackgroundColor(context.resources.getColor(android.R.color.holo_orange_dark))

                //kirim broadcast untuk bookingActivity enable button next
                val intent = Intent(Common.KEY_ENABLE_BUTTON_NEXT)
                intent.putExtra(Common.KEY_SALON_STORE, salonList[pos])
                intent.putExtra(Common.KEY_STEP, 1)
                localBroadcastManager.sendBroadcast(intent)
            }

        })
    }

    class viewHolder(val view: View): RecyclerView.ViewHolder(view), View.OnClickListener {

        val tv_salon_name = view.tv_salon_name
        val tv_salon_address = view.tv_salon_address
        val card_salon = view.card_salon
        lateinit var iRecyclerItemSelectedListener: IRecyclerItemSelectedListener

        init {
            view.setOnClickListener(this)
        }

        fun setiRecyclerItemSelectedListener(iRecyclerItemSelectedListener: IRecyclerItemSelectedListener){
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener
        }

        override fun onClick(p0: View?) {
            iRecyclerItemSelectedListener.onItemSelectedListener(p0!!, adapterPosition)
        }
    }
}