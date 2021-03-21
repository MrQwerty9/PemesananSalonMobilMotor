package com.sstudio.otocare.ui.booking.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.sstudio.core.domain.model.TimeSlot
import com.sstudio.otocare.R
import com.sstudio.otocare.common.Common
import com.sstudio.otocare.databinding.ItemTimeSlotBinding

class TimeSlotAdapter : RecyclerView.Adapter<TimeSlotAdapter.ViewHolder>() {

    private var timeSlotList = ArrayList<TimeSlot>()
    private lateinit var localBroadcastManager: LocalBroadcastManager
    private var cardViewList = ArrayList<CardView>()
    private lateinit var context: Context

    fun setTimeSlot(list: ArrayList<TimeSlot>) {
        timeSlotList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        this.context = parent.context
        localBroadcastManager = LocalBroadcastManager.getInstance(parent.context)
        val itemView =
            ItemTimeSlotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return Common.TIME_SLOT_TOTAL
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("slotAdapter", "onBind ${timeSlotList.size}")
        holder.bind(timeSlotList[position], position)
    }

    inner class ViewHolder(private val binding: ItemTimeSlotBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(timeSlot: TimeSlot, position: Int) {
            binding.tvTimeSlot.text = StringBuilder(Common.convertTimeSlotToString(position))
            if (timeSlotList.size == 0) {
                binding.tvTimeSlotDescrib.text = "Tersedia"
                binding.tvTimeSlotDescrib.setTextColor(
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
            } else { //jika ada posisi full
                for (slotValue: TimeSlot in timeSlotList) {
                    //Loop all time slot from service
                    val slot = Integer.parseInt(slotValue.slot.toString())
                    if (slot == position) {
                        //set semua item ke full
                        //set all remain card background without change full time slot

                        binding.cardTimeSlot.tag = Common.DISABLE_TAG
                        binding.tvTimeSlotDescrib.text = "Full"
                        binding.tvTimeSlotDescrib.setTextColor(
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
                                R.color.dark_grey
                            )
                        )
                    }
                }
            }

            //Add semua card ke list
            //tidak add card yg ada di cardviewList
            if (!cardViewList.contains(binding.cardTimeSlot))
                cardViewList.add(binding.cardTimeSlot)

            //cek jika time slot tersedia
            binding.cardTimeSlot.setOnClickListener {
                //loop semua card di cardList
                for (cardView in cardViewList) {
                    if (cardView.tag == null)
                        cardView.setCardBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.white
                            )
                        )
                }
                //card terpilih akan ganti warna
                binding.cardTimeSlot.setCardBackgroundColor(
                    ContextCompat.getColor(context, R.color.colorPrimary)
                )

                //kemudian kirim broadcast to enable button next
                val intent = Intent(Common.KEY_ENABLE_BUTTON_NEXT)
                intent.putExtra(Common.KEY_TIME_SLOT, position) //index time slot yg dipilih
                intent.putExtra(Common.KEY_STEP, 3) //ke step 3
                localBroadcastManager.sendBroadcast(intent)
            }
        }
    }

}