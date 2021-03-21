package com.sstudio.otocare.ui.booking


import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import com.sstudio.otocare.R
import com.sstudio.otocare.common.Common
import com.sstudio.otocare.common.SpaceItemDecoration
import com.sstudio.otocare.databinding.FragmentBookingStepThreeBinding
import com.sstudio.otocare.listener.ITimeSlotLoadListener
import com.sstudio.otocare.ui.booking.adapter.TimeSlotAdapter
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import dmax.dialog.SpotsDialog
import java.text.SimpleDateFormat
import java.util.*

class BookingStep3Fragment : Fragment(), ITimeSlotLoadListener {

    var mInstance: BookingStep3Fragment? = null

    //    private lateinit var bengkelDoo: DocumentReference
    private lateinit var iTimeSlotLoadListener: ITimeSlotLoadListener
    private lateinit var dialog: AlertDialog
    private lateinit var localBroadcastManager: LocalBroadcastManager
    private lateinit var simpleDateFormat: SimpleDateFormat
    private var _binding: FragmentBookingStepThreeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        iTimeSlotLoadListener = this
        localBroadcastManager = LocalBroadcastManager.getInstance(requireContext())
        localBroadcastManager.registerReceiver(
            displayTimeSlot,
            IntentFilter(Common.KEY_DISPLAY_TIME_SLOT)
        )

        simpleDateFormat = SimpleDateFormat("dd_MM_yyyy")

        dialog = SpotsDialog.Builder().setContext(context).setCancelable(false).build()
    }

    override fun onDestroy() {
        super.onDestroy()
        localBroadcastManager.unregisterReceiver(displayTimeSlot)
    }

    private val displayTimeSlot = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            val date = Calendar.getInstance()
            date.add(Calendar.DATE, 0) //add current date
            loadAvailableTimeSlotOfBengkel(
                Common.currentBengkel!!.id,
                simpleDateFormat.format(date.time)
            )
        }
    }

    private fun loadAvailableTimeSlotOfBengkel(bengkelId: String, bookDate: String?) {
        dialog.show()

        // /Cabang/Semolowaru/Branch/03Fw4uig9UPvUSyjhdrR/Salon/fu9kIFHAcDggmEUmItcu
//        bengkelDoo = FirebaseFirestore.getInstance()
//            .collection("Cabang")
//            .document(Common.city)
//            .collection("Branch")
//            .document(Common.currentSalon!!.id)
//            .collection("Salon")
//            .document(Common.currentBengkel!!.id)

        //Get informasi this bengkel
//        bengkelDoo.get().addOnCompleteListener {
//            if (it.isSuccessful){
//                val documentSnapshot = it.result
//                if (documentSnapshot!!.exists()){
//                    //get informasi booking
//                    //jika tidak created, return empty
//                    val date = FirebaseFirestore.getInstance()
//                        .collection("Cabang")
//                        .document(Common.city)
//                        .collection("Branch")
//                        .document(Common.currentSalon!!.salonId)
//                        .collection("Salon")
//                        .document(Common.currentBengkel!!.bengkelId)
//                        .collection(bookDate!!)
//
//                    date.get().addOnCompleteListener {
//                        if (it.isSuccessful){
//                            val querySnapshot = it.result!!
//                            if (querySnapshot.isEmpty){ //jika tidak ada janji
//                                iTimeSlotLoadListener.onTimeSlotLoadEmpty()
//                            }
//                            else{//jika ada janji
//                                Log.d("step3", "ada janji")
//                                val timeSlot = ArrayList<com.sstudio.core.domain.model.TimeSlot>()
//                                for (document in it.result!!)
//                                    timeSlot.add(document.toObject(com.sstudio.core.domain.model.TimeSlot::class.java))
//                                iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlot)
//                            }
//                        }
//                    }.addOnFailureListener {
//                        iTimeSlotLoadListener.onTimeSlotLoadFailed(it.message!!)
//                    }
//                }
//            }
//        }
    }

    fun getmInstance(): BookingStep3Fragment {
        if (mInstance == null)
            mInstance = BookingStep3Fragment()
        return mInstance!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val itemView = inflater.inflate(R.layout.fragment_booking_step_three, container, false)
        initView(itemView)
        return itemView
    }

    private fun initView(itemView: View) {
        binding.rvTimeSlotStep3.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(requireActivity(), 3)
        binding.rvTimeSlotStep3.layoutManager = gridLayoutManager
        binding.rvTimeSlotStep3.addItemDecoration(SpaceItemDecoration(8))

        val startDate = Calendar.getInstance()
        startDate.add(Calendar.DATE, 0)
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.DATE, 2)

        val horizontalCalendar = HorizontalCalendar.Builder(itemView, R.id.calendar_step3)
            .range(startDate, endDate)
            .datesNumberOnScreen(1)
            .mode(HorizontalCalendar.Mode.DAYS)
            .defaultSelectedDate(startDate)
            .build()

        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar?, position: Int) {
                if (Common.currentDate.timeInMillis != date!!.timeInMillis) {
                    Common.currentDate =
                        date //tidak load lagi jika milih hari baru yang sama dg hari yg telah dipilih
                    loadAvailableTimeSlotOfBengkel(
                        Common.currentBengkel!!.id,
                        simpleDateFormat.format(date.time)
                    )

                }
            }

        }
    }

    override fun onTimeSlotLoadSuccess(timeSlotList: ArrayList<com.sstudio.core.domain.model.TimeSlot>) {
        val adapter = TimeSlotAdapter()
        binding.rvTimeSlotStep3.adapter = adapter

        dialog.dismiss()
    }

    override fun onTimeSlotLoadFailed(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        dialog.dismiss()
    }

    override fun onTimeSlotLoadEmpty() {
        val adapter = TimeSlotAdapter()
        binding.rvTimeSlotStep3.adapter = adapter

        dialog.dismiss()
    }
}
