package com.sstudio.otocare.ui.booking.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sstudio.core.domain.model.TimeSlot
import com.sstudio.otocare.R
import com.sstudio.otocare.databinding.ItemTimeSlotBinding

class TimeSlotAdapter : RecyclerView.Adapter<TimeSlotAdapter.ViewHolder>() {

    var itemSelected: TimeSlot? = null
    var selectedTimeSlotPosition: ((Int) -> Unit)? = null

    private var timeSlotList = ArrayList<TimeSlot>()
    private lateinit var context: Context
    private var selectedPosition = -1

    fun setTimeSlot(list: List<TimeSlot>) {
        timeSlotList = list as ArrayList<TimeSlot>
        notifyDataSetChanged()
    }

    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        this.context = parent.context
        val itemView =
            ItemTimeSlotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return timeSlotList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(timeSlotList[position], position)
    }

    inner class ViewHolder(private val binding: ItemTimeSlotBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(timeSlot: TimeSlot, position: Int) {
            binding.tvTimeSlot.text = "${timeSlot.timeStart}-${timeSlot.timeFinish}"
            if (timeSlot.available) {
                if (selectedPosition == position) {
                    itemSelected = timeSlotList[selectedPosition]
                    setItemSelected(binding)
                } else { //unselect
                    setItemUnselected(binding)
                }

            } else { //full
                setItemFull(binding)
            }

            binding.cardTimeSlot.setOnClickListener {
                if (timeSlot.available) {
                    if (selectedPosition >= 0)
                        notifyItemChanged(selectedPosition)
                    selectedPosition = position
                    notifyItemChanged(selectedPosition)
                    selectedTimeSlotPosition?.invoke(selectedPosition)
                }
            }
        }

        private fun setItemSelected(binding: ItemTimeSlotBinding) {
            binding.tvTimeSlotStatus.text = context.getString(R.string.available)
            binding.cardTimeSlot.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.colorPrimary)
            )
            binding.tvTimeSlot.setTextColor(ContextCompat.getColor(context, R.color.white))
            binding.tvTimeSlotStatus.setTextColor(ContextCompat.getColor(context, R.color.white))
        }

        private fun setItemUnselected(binding: ItemTimeSlotBinding) {
            binding.tvTimeSlotStatus.text = context.getString(R.string.available)
            binding.tvTimeSlotStatus.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.dark_grey
                )
            )
            binding.tvTimeSlot.setTextColor(ContextCompat.getColor(context, R.color.dark_grey))
            binding.cardTimeSlot.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
        }

        private fun setItemFull(binding: ItemTimeSlotBinding) {
            binding.tvTimeSlotStatus.text = context.getString(R.string.full)
            binding.tvTimeSlotStatus.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
            binding.tvTimeSlot.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
            binding.cardTimeSlot.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.light_grey
                )
            )
        }
    }

}