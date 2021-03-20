package com.sstudio.core.domain.model

data class Booking(
    var customer: User = User(),
    var time: String = "",
    var garage: Garage = Garage(),
    var salon: Salon = Salon(),
    var slot: Long = 0
)