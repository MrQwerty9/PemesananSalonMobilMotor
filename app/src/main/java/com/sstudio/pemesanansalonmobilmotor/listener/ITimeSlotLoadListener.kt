package com.sstudio.pemesanansalonmobilmotor.listener

import com.sstudio.pemesanansalonmobilmotor.Model.TimeSlot

interface ITimeSlotLoadListener {
    fun onTimeSlotLoadSuccess(timeSlotList: ArrayList<TimeSlot>)
    fun onTimeSlotLoadFailed(message: String)
    fun onTimeSlotLoadEmpty()
}