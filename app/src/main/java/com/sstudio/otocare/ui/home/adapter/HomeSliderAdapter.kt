package com.sstudio.otocare.ui.home.adapter

import ss.com.bannerslider.adapters.SliderAdapter
import ss.com.bannerslider.viewholder.ImageSlideViewHolder

class HomeSliderAdapter(val bannerList: List<com.sstudio.core.domain.model.Banner>) :
    SliderAdapter() {

    override fun getItemCount(): Int {
        return bannerList.size
    }

    override fun onBindImageSlide(position: Int, imageSlideViewHolder: ImageSlideViewHolder?) {
        imageSlideViewHolder!!.bindImageSlide(bannerList.get(position).image)
    }
}