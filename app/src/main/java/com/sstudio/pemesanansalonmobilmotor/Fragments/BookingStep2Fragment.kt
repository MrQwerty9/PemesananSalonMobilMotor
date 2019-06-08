package com.sstudio.pemesanansalonmobilmotor.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sstudio.pemesanansalonmobilmotor.R


/**
 * A simple [Fragment] subclass.
 *
 */
class BookingStep2Fragment : Fragment() {

    var mInstance: BookingStep2Fragment? = null
    fun getmInstance(): BookingStep2Fragment{
        if (mInstance == null)
            mInstance = BookingStep2Fragment()
        return mInstance!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_booking_step_two, container,false)

    }


}
