package com.sstudio.pemesanansalonmobilmotor.listener

interface IAllSalonLoadListener {
    fun onAllSalonLoadSuccess(areaNameList: List<String>)
    fun onAllSalonLoadFailed(message: String)
}