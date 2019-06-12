package com.sstudio.pemesanansalonmobilmotor.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sstudio.pemesanansalonmobilmotor.Model.TimeSlot
import com.sstudio.pemesanansalonmobilmotor.R
import com.sstudio.pemesanansalonmobilmotor.common.Common
import kotlinx.android.synthetic.main.layout_time_slot.view.*

class MyTimeSlotAdapter(var context: Context): RecyclerView.Adapter<MyTimeSlotAdapter.ViewHolder>() {

    private var timeSlotList = ArrayList<TimeSlot>()

    constructor(context: Context, timeSlotList: ArrayList<TimeSlot>) : this(context){
        this.timeSlotList = timeSlotList
        this.context = context
        Log.d("step3", "ada janji $timeSlotList")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.layout_time_slot, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return Common.TIME_SLOT_TOTAL
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("slotAdapter","onBind ${timeSlotList.size}")
        holder.tv_time_slot.text = StringBuilder(Common.convertTimeSlotToString(position))
        if (timeSlotList.size == 0){
            holder.tv_time_slot_describ.text = "Tersedia"
            holder.tv_time_slot_describ.setTextColor(context.resources.getColor(android.R.color.black))
            holder.tv_time_slot.setTextColor(context.resources.getColor(android.R.color.black))
            holder.card_time_slot.setCardBackgroundColor(context.resources.getColor(android.R.color.white))
        }
        else { //jika ada posisi full
            for (slotValue: TimeSlot in timeSlotList){
                //Loop all time slot from service
                val slot = Integer.parseInt(slotValue.slot.toString())
                if (slot == position){
                    holder.tv_time_slot_describ.text = "Full"
                    holder.tv_time_slot_describ.setTextColor(context.resources.getColor(android.R.color.white))
                    holder.tv_time_slot.setTextColor(context.resources.getColor(android.R.color.white))
                    holder.card_time_slot.setCardBackgroundColor(context.resources.getColor(android.R.color.darker_gray))
                }
            }

        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_time_slot = itemView.tv_time_slot
        val tv_time_slot_describ = itemView.tv_time_slot_describ
        val card_time_slot = itemView.card_time_slot
    }

}