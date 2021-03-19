package com.sstudio.otocare.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sstudio.otocare.Fragments.BookingStep1Fragment
import com.sstudio.otocare.Fragments.BookingStep2Fragment
import com.sstudio.otocare.Fragments.BookingStep3Fragment
import com.sstudio.otocare.Fragments.BookingStep4Fragment

class ViePagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        val bookingStep1Fragment = BookingStep1Fragment()
        val bookingStep2Fragment = BookingStep2Fragment()
        val bookingStep3Fragment = BookingStep3Fragment()
        val bookingStep4Fragment = BookingStep4Fragment()
        when (position){
            0 -> return bookingStep1Fragment.get_nstance()
            1 -> return bookingStep2Fragment.getmInstance()
            2 -> return bookingStep3Fragment.getmInstance()
            3 -> return bookingStep4Fragment.getmInstance()

        }
        return bookingStep1Fragment.get_nstance()
    }

    override fun getCount(): Int {
        return 4
    }

}
