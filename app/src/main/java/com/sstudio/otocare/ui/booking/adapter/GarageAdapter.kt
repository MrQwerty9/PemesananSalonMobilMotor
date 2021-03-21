package com.sstudio.otocare.ui.booking.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.sstudio.core.domain.model.Garage
import com.sstudio.otocare.databinding.ItemGarageBinding

class GarageAdapter(val context: Context, private val garageList: List<Garage>) :
    RecyclerView.Adapter<GarageAdapter.ViewHolder>() {

    private val cardViewList: ArrayList<CardView> = ArrayList()
    private val localBroadcastManager = LocalBroadcastManager.getInstance(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = ItemGarageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return garageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(garageList[position])
    }

    inner class ViewHolder(private val binding: ItemGarageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(garage: Garage) {
            binding.tvGarageName.text = garage.name
            binding.ratingGarage.rating = garage.rating.toFloat()
//            if (!cardViewList.contains(holder.card_bengkel))
//                cardViewList.add(holder.card_bengkel)

            binding.cardGarage.setOnClickListener {

                //set background semua item tdk dipilih
//                for (cardView: CardView in cardViewList) {
//                    cardView.setCardBackgroundColor(
//                        context.resources
//                            .getColor(android.R.color.white)
//                    )
//                }

                //set background yg dipilih
//                holder.card_bengkel.setCardBackgroundColor(
//                    context.resources
//                        .getColor(android.R.color.holo_orange_dark)
//                )

                //kirim local broadcast untuk enable button next
//                val intent = Intent(Common.KEY_ENABLE_BUTTON_NEXT)
//                intent.putExtra(Common.KEY_BENGKEL_SELECTED, bengkelList[pos])
//                intent.putExtra(Common.KEY_STEP, 2)
//                localBroadcastManager.sendBroadcast(intent)
            }
        }
    }
}