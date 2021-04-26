package com.sstudio.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Booking(
    var id: String = "",
    var customer: User = User(),
    var timeSlot: TimeSlot = TimeSlot(),
    var pkg: Package = Package(),
    var garage: Garage = Garage(),
    var date: String = "",
    var eventId: Int = 0,
    var cart: List<Cart> = listOf()
) : Parcelable