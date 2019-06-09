package com.sstudio.pemesanansalonmobilmotor.common

import com.sstudio.pemesanansalonmobilmotor.Model.Salon
import com.sstudio.pemesanansalonmobilmotor.Model.User
import java.util.*


object Common {
    var IS_LOGIN: String = "IsLogin"
    var currentUser: User? = null
    var KEY_ENABLE_BUTTON_NEXT: String = "ENABLE_BUTTON_NEXT"
    var KEY_SALON_STORE = "SALON_SAVE"
    var currentSalon: Salon? = null
    var step: Int = 0
    var city: String = ""
    val KEY_BENGKEL_LOAD_DONE: String = "BENGKEL_LOAD_DONE"
}