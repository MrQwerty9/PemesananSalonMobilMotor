package com.sstudio.otocare.listener

import com.sstudio.otocare.Model.Banner

interface IBannerLoadListener {
    fun onBannerLoadSuccess(banner: List<Banner>)
    fun onBannerLoadFailed(message: String)
}