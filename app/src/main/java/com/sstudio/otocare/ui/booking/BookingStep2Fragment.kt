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
import androidx.recyclerview.widget.RecyclerView
import com.sstudio.core.domain.model.Bengkel
import com.sstudio.otocare.R
import com.sstudio.otocare.adapter.GarageAdapter
import com.sstudio.otocare.common.Common
import com.sstudio.otocare.common.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_booking_step_two.view.*

/**
 * A simple [Fragment] subclass.
 *
 */
class BookingStep2Fragment : Fragment() {

    var mInstance: BookingStep2Fragment? = null
    private lateinit var localBroadcastManager: LocalBroadcastManager
    private lateinit var rv_booking: RecyclerView

    private fun bengkelDoneReceiver() = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val bengkelArrayList: ArrayList<com.sstudio.core.domain.model.Bengkel>? =
                intent?.getParcelableArrayListExtra(Common.KEY_BENGKEL_LOAD_DONE)
            val adapter = GarageAdapter(context!!, bengkelArrayList!!)
            rv_booking.adapter = adapter
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        localBroadcastManager = LocalBroadcastManager.getInstance(requireContext())
        localBroadcastManager.registerReceiver(bengkelDoneReceiver(), IntentFilter(Common.KEY_BENGKEL_LOAD_DONE))
    }

    override fun onDestroy() {
        localBroadcastManager.unregisterReceiver(bengkelDoneReceiver())
        super.onDestroy()
    }

    fun getmInstance(): BookingStep2Fragment {
        if (mInstance == null)
            mInstance = BookingStep2Fragment()
        return mInstance!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val itemView = inflater.inflate(R.layout.fragment_booking_step_two, container,false)
        rv_booking = itemView.rv_booking_step_two

        initView()
        return itemView

    }

    private fun initView() {
        rv_booking.setHasFixedSize(true)
        rv_booking.layoutManager = GridLayoutManager(activity, 2)
        rv_booking.addItemDecoration(SpaceItemDecoration(4))
    }


}
