package com.sstudio.otocare.common

import com.sstudio.core.domain.model.Package
import java.text.SimpleDateFormat
import java.util.*


object Common {
    var IS_LOGIN: String = "IsLogin"
    var currentUser: com.sstudio.core.domain.model.User? = null
    var KEY_ENABLE_BUTTON_NEXT: String = "ENABLE_BUTTON_NEXT"
    var KEY_SALON_STORE = "SALON_SAVE"
    var currentGarage: com.sstudio.core.domain.model.Garage? = null
    var step: Int = 0
    var city: String = ""
    val KEY_BENGKEL_LOAD_DONE: String = "BENGKEL_LOAD_DONE"
    var currentBengkel: Package? = null
    val KEY_DISPLAY_TIME_SLOT: String = "DISPLAY_TIMA_SLOT"
    val KEY_STEP:String = "STEP"
    val KEY_BENGKEL_SELECTED: String = "BENGKEL_SELECTED"
    val TIME_SLOT_TOTAL: Int = 5
    val DISABLE_TAG = "DISABLE"
    val KEY_TIME_SLOT = "TIME_SLOT"
    var currentTimeSlot = -1
    val KEY_CONFIRM_BOOKING = "CONFIRM_BOOKING"
    var currentDate = Calendar.getInstance()
    var simpleDateFormat = SimpleDateFormat("dd_MM_yyyy")

    fun convertTimeSlotToString(slot: Int): String{
        when(slot){
            0 -> return "7:00-9:00"
            1 -> return "9:00-11:00"
            2 -> return "11:00-13:00"
            3 -> return "13:00-15:00"
            4 -> return "16:00-17:00"
            else -> return "Tutup"
        }
    }
}