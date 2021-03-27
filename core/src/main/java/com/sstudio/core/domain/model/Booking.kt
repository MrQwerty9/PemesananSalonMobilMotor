package com.sstudio.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Booking(
    var customer: User = User(),
    var time: String = "",
    var pkg: Package = Package(),
    var garage: Garage = Garage(),
    var slot: Long = 0
) : Parcelable