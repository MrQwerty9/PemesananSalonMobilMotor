package com.sstudio.core.data.source.remote.response

data class BookingResponse(
    var id: String = "",
    var userPhone: String = "",
    var date: String = "",
    var garageId: String = "",
    val garageName: String = "",
    val garageAddress: String = "",
    var garagePhone: String = "",
    var packageId: String = "",
    var packageName: String = "",
    var packageInclude: List<String> = listOf(),
    var packagePrice: Double = 0.0,
    var timeId: String = "",
    var timeStart: String = "",
    var timeFinish: String = ""
)