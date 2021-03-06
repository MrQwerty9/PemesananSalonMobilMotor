package com.sstudio.otocare.ui.booking.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sstudio.core.domain.model.Garage
import com.sstudio.otocare.R
import com.sstudio.otocare.databinding.ItemGarageBinding

class GarageAdapter : RecyclerView.Adapter<GarageAdapter.ViewHolder>() {

    var itemSelected: Garage? = null
    var selectedGaragePosition: ((Int) -> Unit)? = null

    private lateinit var context: Context
    private var garageList: List<Garage> = ArrayList()
    private var selectedPosition = -1
//    val localBroadcastManager = LocalBroadcastManager.getInstance(context)

    fun setGarageList(list: List<Garage>) {
        this.garageList = list
        notifyDataSetChanged()
    }

    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        this.context = parent.context
        val itemView = ItemGarageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return garageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(garageList[position], position)
    }

    inner class ViewHolder(private val binding: ItemGarageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(garage: Garage, position: Int) {

            binding.tvGarageName.text = garage.name
            binding.tvGarageAddress.text = garage.address

            if (selectedPosition == position) {
//                binding.cardSalon.isSelected = true
                itemSelected = garageList[selectedPosition]
                binding.cardGarage.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorPrimary
                    )
                )
                binding.tvGarageAddress.setTextColor(ContextCompat.getColor(context, R.color.white))
                binding.tvGarageName.setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
//                binding.cardSalon.isSelected = false
                binding.cardGarage.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
                binding.tvGarageAddress.setTextColor(ContextCompat.getColor(context, R.color.grey))
                binding.tvGarageName.setTextColor(ContextCompat.getColor(context, R.color.grey))
            }


            binding.cardGarage.setOnClickListener {

                if (selectedPosition >= 0)
                    notifyItemChanged(selectedPosition)
                selectedPosition = position
                notifyItemChanged(selectedPosition)
                selectedGaragePosition?.invoke(selectedPosition)
            }
        }
    }
}