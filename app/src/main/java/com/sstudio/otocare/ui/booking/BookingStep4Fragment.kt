package com.sstudio.otocare.ui.booking

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.sstudio.core.domain.model.Booking
import com.sstudio.otocare.common.Common
import com.sstudio.otocare.databinding.FragmentBookingStepFourBinding
import java.text.SimpleDateFormat
import java.util.*

class BookingStep4Fragment : Fragment() {

    var mInstance: BookingStep4Fragment? = null
    private val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    private lateinit var localBroadCastManager: LocalBroadcastManager
    private val booking = Booking()
    private var _binding: FragmentBookingStepFourBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingStepFourBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        localBroadCastManager = LocalBroadcastManager.getInstance(requireContext())
        localBroadCastManager.registerReceiver(
            confirmBookingReceiver(),
            IntentFilter(Common.KEY_CONFIRM_BOOKING)
        )

        binding.btnConfirm.setOnClickListener {
            Log.d("mytag", "klik2")

            //booking informasi
            Log.d("mytag", "klik")
            booking.pkg.id = Common.currentBengkel!!.id
            booking.pkg.name = Common.currentBengkel!!.name
            booking.customer.name = Common.currentUser!!.name
            booking.customer.phoneNumber = Common.currentUser!!.phoneNumber
            booking.garage.id = Common.currentGarage!!.id
            booking.garage.address = Common.currentGarage!!.address
            booking.garage.name = Common.currentGarage!!.name
            booking.slot = Common.currentTimeSlot.toLong()
            booking.time =
                StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                    .append(" " + simpleDateFormat.format(Common.currentDate.time)).toString()

//            val bookingDate = FirebaseFirestore.getInstance()
//                .collection("Cabang")
//                .document(Common.city)
//                .collection("Branch")
//                .document(Common.currentSalon!!.salonId)
//                .collection("Salon")
//                .document(Common.currentBengkel!!.bengkelId)
//                .collection(Common.simpleDateFormat.format(Common.currentDate.time))
//                .document(Common.currentTimeSlot.toString())

            //write data

//            bookingDate.set(booking)
//                .addOnSuccessListener {
//                    resetStaticData()
//                    activity?.finish() //close activity
//                    Toast.makeText(context, "Berhasil!", Toast.LENGTH_SHORT).show()
//                }.addOnFailureListener {
//                    Toast.makeText(context, "" + it.message, Toast.LENGTH_SHORT).show()
//                }
        }

    }

    fun getmInstance(): BookingStep4Fragment {
        if (mInstance == null)
            mInstance = BookingStep4Fragment()
        return mInstance!!
    }

    private fun resetStaticData() {
        Common.step = 0
        Common.currentTimeSlot = -1
        Common.currentGarage = null
        Common.currentBengkel = null
        Common.currentDate.add(Calendar.DATE, 0) //current date
    }

    fun confirmBookingReceiver() = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            setData()
        }
    }


    private fun setData() {
        binding.tvTime.text = StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
            .append(" " + simpleDateFormat.format(Common.currentDate.time))
        binding.tvBookingTechnician.text = Common.currentBengkel?.name
        binding.tvPhone.text = Common.currentGarage?.phone
        binding.tvGarageName.text = Common.currentGarage?.name
    }

}
