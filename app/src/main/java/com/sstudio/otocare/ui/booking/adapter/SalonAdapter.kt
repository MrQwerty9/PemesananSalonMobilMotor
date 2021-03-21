package com.sstudio.otocare.ui.booking.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.sstudio.core.domain.model.Salon
import com.sstudio.otocare.R
import com.sstudio.otocare.common.Common
import com.sstudio.otocare.databinding.ItemSalonBinding

class SalonAdapter(val context: Context, val salonList: List<Salon>) :
    RecyclerView.Adapter<SalonAdapter.ViewHolder>() {

    var cardViewList = ArrayList<CardView>()
    val localBroadcastManager = LocalBroadcastManager.getInstance(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = ItemSalonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return salonList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(salonList[position])
    }

    inner class ViewHolder(private val binding: ItemSalonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(salon: Salon) {
            binding.tvSalonName.text = salon.name
            binding.tvSalonAddress.text = salon.address
//            if (!cardViewList.contains(holder.card_salon))
//                cardViewList.add(holder.card_salon)

            binding.cardSalon.setOnClickListener {
                //set background putin untuk semua card tdk dipilih
                for (cardView: CardView in cardViewList)
                    cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))

                //set warna dipilih
                binding.cardSalon.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.dark_grey
                    )
                )

                //kirim broadcast untuk bookingActivity enable button next
                val intent = Intent(Common.KEY_ENABLE_BUTTON_NEXT)
                intent.putExtra(Common.KEY_SALON_STORE, salon)
                intent.putExtra(Common.KEY_STEP, 1)
                localBroadcastManager.sendBroadcast(intent)
            }
        }
    }
}