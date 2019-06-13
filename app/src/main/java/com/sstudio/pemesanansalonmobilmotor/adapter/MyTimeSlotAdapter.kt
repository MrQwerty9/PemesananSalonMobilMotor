package com.sstudio.pemesanansalonmobilmotor.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.sstudio.pemesanansalonmobilmotor.Model.TimeSlot
import com.sstudio.pemesanansalonmobilmotor.R
import com.sstudio.pemesanansalonmobilmotor.common.Common
import com.sstudio.pemesanansalonmobilmotor.listener.IRecyclerItemSelectedListener
import kotlinx.android.synthetic.main.layout_time_slot.view.*

class MyTimeSlotAdapter(var context: Context): RecyclerView.Adapter<MyTimeSlotAdapter.ViewHolder>() {

    private var timeSlotList = ArrayList<TimeSlot>()
    private var localBroadcastManager = LocalBroadcastManager.getInstance(context)
    private var cardViewList = ArrayList<CardView>()

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
                    //set semua item ke full
                    //set all remain card background without change full time slot

                    holder.card_time_slot.tag = Common.DISABLE_TAG
                    holder.tv_time_slot_describ.text = "Full"
                    holder.tv_time_slot_describ.setTextColor(context.resources.getColor(android.R.color.white))
                    holder.tv_time_slot.setTextColor(context.resources.getColor(android.R.color.white))
                    holder.card_time_slot.setCardBackgroundColor(context.resources.getColor(android.R.color.darker_gray))
                }
            }
        }

        //Add semua card ke list
        //tidak add card yg ada di cardviewList
        if (!cardViewList.contains(holder.card_time_slot))
            cardViewList.add(holder.card_time_slot)

        //cek jika time slot tersedia
        holder.iRecyclerViewSelectedListener = object : IRecyclerItemSelectedListener{
            override fun onItemSelectedListener(view: View, pos: Int) {
                //loop semua card di cardList
                for (cardView in cardViewList){
                    if (cardView.tag == null)
                        cardView.setCardBackgroundColor(context.resources
                            .getColor(android.R.color.white))
                }
                //card terpilih akan ganti warna
                holder.card_time_slot.setCardBackgroundColor(context.resources
                    .getColor(android.R.color.holo_orange_dark))

                //kemudian kirim broadcast to enable button next
                val intent = Intent(Common.KEY_ENABLE_BUTTON_NEXT)
                intent.putExtra(Common.KEY_TIME_SLOT, pos) //index time slot yg dipilih
                intent.putExtra(Common.KEY_STEP, 3) //ke step 3
                localBroadcastManager.sendBroadcast(intent)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val tv_time_slot = itemView.tv_time_slot
        val tv_time_slot_describ = itemView.tv_time_slot_describ
        val card_time_slot = itemView.card_time_slot
        lateinit var iRecyclerViewSelectedListener: IRecyclerItemSelectedListener

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            iRecyclerViewSelectedListener.onItemSelectedListener(view!!, adapterPosition)
        }
    }

}