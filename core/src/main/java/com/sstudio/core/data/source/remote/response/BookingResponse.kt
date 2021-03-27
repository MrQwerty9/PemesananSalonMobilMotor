package com.sstudio.core.data.source.remote.response

data class BookingResponse(
    var customer: UserResponse = UserResponse(),
    var time: String = "",
    var team: PackageResponse = PackageResponse(),
    var garage: GarageResponse = GarageResponse(),
    var slot: Long = 0
)