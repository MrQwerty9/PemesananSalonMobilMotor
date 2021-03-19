package com.sstudio.otocare.listener

import com.sstudio.otocare.Model.TimeSlot

interface ITimeSlotLoadListener {
    fun onTimeSlotLoadSuccess(timeSlotList: ArrayList<TimeSlot>)
    fun onTimeSlotLoadFailed(message: String)
    fun onTimeSlotLoadEmpty()
}