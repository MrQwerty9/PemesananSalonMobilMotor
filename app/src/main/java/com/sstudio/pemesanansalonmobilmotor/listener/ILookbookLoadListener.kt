package com.sstudio.pemesanansalonmobilmotor.listener

import com.sstudio.pemesanansalonmobilmotor.Model.Banner

interface ILookbookLoadListener {
    fun onLookbookLoadSuccess(banner: List<Banner>)
    fun onLookbookLoadFailed(message: String)
}