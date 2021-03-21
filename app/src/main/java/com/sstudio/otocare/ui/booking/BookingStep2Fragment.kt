package com.sstudio.otocare.ui.booking

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import com.sstudio.core.domain.model.Garage
import com.sstudio.otocare.R
import com.sstudio.otocare.common.Common
import com.sstudio.otocare.common.SpaceItemDecoration
import com.sstudio.otocare.databinding.FragmentBookingStepTwoBinding
import com.sstudio.otocare.ui.booking.adapter.GarageAdapter

class BookingStep2Fragment : Fragment() {

    var mInstance: BookingStep2Fragment? = null
    private lateinit var localBroadcastManager: LocalBroadcastManager
    private var _binding: FragmentBookingStepTwoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val itemView = inflater.inflate(R.layout.fragment_booking_step_two, container, false)

        initView()
        return itemView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        localBroadcastManager = LocalBroadcastManager.getInstance(requireContext())
        localBroadcastManager.registerReceiver(
            garageDoneReceiver(),
            IntentFilter(Common.KEY_BENGKEL_LOAD_DONE)
        )
    }

    private fun garageDoneReceiver() = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val garageList: ArrayList<Garage>? =
                intent?.getParcelableArrayListExtra(Common.KEY_BENGKEL_LOAD_DONE)
            val adapter = GarageAdapter(context!!, garageList!!)
            binding.rvBookingStepTwo.adapter = adapter
        }

    }

    override fun onDestroy() {
        localBroadcastManager.unregisterReceiver(garageDoneReceiver())
        super.onDestroy()
    }

    fun getmInstance(): BookingStep2Fragment {
        if (mInstance == null)
            mInstance = BookingStep2Fragment()
        return mInstance!!
    }

    private fun initView() {
        binding.rvBookingStepTwo.setHasFixedSize(true)
        binding.rvBookingStepTwo.layoutManager = GridLayoutManager(activity, 2)
        binding.rvBookingStepTwo.addItemDecoration(SpaceItemDecoration(4))
    }


}
