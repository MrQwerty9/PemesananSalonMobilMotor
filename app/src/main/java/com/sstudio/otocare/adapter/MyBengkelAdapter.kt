package com.sstudio.otocare.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.sstudio.otocare.Model.Bengkel
import com.sstudio.otocare.R
import com.sstudio.otocare.common.Common
import com.sstudio.otocare.listener.IRecyclerItemSelectedListener
import kotlinx.android.synthetic.main.layout_bengkel.view.*

class MyBengkelAdapter(val context: Context, val bengkelList: List<Bengkel>):
    RecyclerView.Adapter<MyBengkelAdapter.ViewHolder>() {

    private val cardViewList: ArrayList<CardView> = ArrayList()
    private val localBroadcastManager = LocalBroadcastManager.getInstance(context)

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
        if(!cardViewList.contains(holder.card_bengkel))
            cardViewList.add(holder.card_bengkel)

        holder.iRecyclerItemSelectedListener = object : IRecyclerItemSelectedListener {
            override fun onItemSelectedListener(view: View, pos: Int) {

                //set background semua item tdk dipilih
                for (cardView: CardView in cardViewList){
                    cardView.setCardBackgroundColor(context.resources
                        .getColor(android.R.color.white))
                }

                //set background yg dipilih
                holder.card_bengkel.setCardBackgroundColor(
                    context.resources
                        .getColor(android.R.color.holo_orange_dark)
                )

                //kirim local broadcast untuk enable button next
                val intent = Intent(Common.KEY_ENABLE_BUTTON_NEXT)
                intent.putExtra(Common.KEY_BENGKEL_SELECTED, bengkelList[pos])
                intent.putExtra(Common.KEY_STEP, 2)
                localBroadcastManager.sendBroadcast(intent)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val tv_bengkel_akun = itemView.tv_bengkel_nama
        val rating_bengkel = itemView.rating_bengkel
        val card_bengkel = itemView.card_bengkel
        lateinit var iRecyclerItemSelectedListener: IRecyclerItemSelectedListener

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            iRecyclerItemSelectedListener.onItemSelectedListener(p0!!, adapterPosition)
        }
    }
}