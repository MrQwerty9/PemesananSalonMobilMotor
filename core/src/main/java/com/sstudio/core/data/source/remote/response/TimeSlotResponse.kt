package com.sstudio.core.data.source.remote.response

data class TimeSlotResponse(
    var slot: Long
) {
    constructor() : this(0)
}