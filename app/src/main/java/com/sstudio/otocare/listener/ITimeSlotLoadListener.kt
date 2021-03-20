package com.sstudio.otocare.listener

interface ITimeSlotLoadListener {
    fun onTimeSlotLoadSuccess(timeSlotList: ArrayList<com.sstudio.core.domain.model.TimeSlot>)
    fun onTimeSlotLoadFailed(message: String)
    fun onTimeSlotLoadEmpty()
}