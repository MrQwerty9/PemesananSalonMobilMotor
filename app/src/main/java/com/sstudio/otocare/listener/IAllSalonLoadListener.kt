package com.sstudio.otocare.listener

interface IAllSalonLoadListener {
    fun onAllSalonLoadSuccess(areaNameList: List<String>)
    fun onAllSalonLoadFailed(message: String)
}