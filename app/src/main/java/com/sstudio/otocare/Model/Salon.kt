package com.sstudio.otocare.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Salon(val nama: String, val alamat: String, var salonId: String, var phone: String, var waktu: String): Parcelable{
    constructor() : this("","", "", "", "")
}