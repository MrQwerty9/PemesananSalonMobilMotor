package com.sstudio.otocare.ui.home.adapter

import com.sstudio.core.domain.model.Banner
import ss.com.bannerslider.adapters.SliderAdapter
import ss.com.bannerslider.viewholder.ImageSlideViewHolder

class HomeSliderAdapter(private val bannerList: List<Banner>) :
    SliderAdapter() {

    override fun getItemCount(): Int {
        return bannerList.size
    }

    override fun onBindImageSlide(position: Int, imageSlideViewHolder: ImageSlideViewHolder?) {
        imageSlideViewHolder!!.bindImageSlide(bannerList[position].image)
    }
}