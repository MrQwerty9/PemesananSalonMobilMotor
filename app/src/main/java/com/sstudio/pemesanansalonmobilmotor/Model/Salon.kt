package com.sstudio.pemesanansalonmobilmotor.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Salon(val nama: String, val alamat: String, var salonId: String): Parcelable{
    constructor() : this("","", "")
}