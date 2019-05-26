package com.sstudio.pemesanansalonmobilmotor.listener

import com.sstudio.pemesanansalonmobilmotor.Model.Banner

interface IBannerLoadListener {
    fun onBannerLoadSuccess(banner: List<Banner>)
    fun onBannerLoadFailed(message: String)
}