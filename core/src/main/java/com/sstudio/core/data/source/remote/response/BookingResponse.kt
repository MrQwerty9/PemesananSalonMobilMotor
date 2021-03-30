package com.sstudio.core.data.source.remote.response

data class BookingResponse(
    var customerId: String = "",
    var date: String = "",
    var garageId: String = "",
    var packageId: String = "",
    var timeSlotId: String = ""
)