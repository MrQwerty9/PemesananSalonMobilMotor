package com.sstudio.core.data.source.remote.response

data class BookingResponse(
    var customer: UserResponse = UserResponse(),
    var time: String = "",
    var garage: GarageResponse = GarageResponse(),
    var salon: SalonResponse = SalonResponse(),
    var slot: Long = 0
)