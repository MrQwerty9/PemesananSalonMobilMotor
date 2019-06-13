package com.sstudio.pemesanansalonmobilmotor.Model

data class BookingInformasi(var customerNama: String, var customerPhone: String, var waktu: String, var bengkelId: String,
                            var bengkelNama: String, var salonId: String, var salonNama: String, var salonAlamat: String,
                            var slot: Long) {

    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        0
    )
}