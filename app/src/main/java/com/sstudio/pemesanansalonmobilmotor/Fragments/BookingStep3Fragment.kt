package com.sstudio.pemesanansalonmobilmotor.Fragments


import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sstudio.pemesanansalonmobilmotor.Model.TimeSlot
import com.sstudio.pemesanansalonmobilmotor.R
import com.sstudio.pemesanansalonmobilmotor.adapter.MyTimeSlotAdapter
import com.sstudio.pemesanansalonmobilmotor.common.Common
import com.sstudio.pemesanansalonmobilmotor.common.SpaceItemDecoration
import com.sstudio.pemesanansalonmobilmotor.listener.ITimeSlotLoadListener
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_booking_step_three.*
import kotlinx.android.synthetic.main.fragment_booking_step_three.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 *
 */
class BookingStep3Fragment : Fragment(), ITimeSlotLoadListener {

    var mInstance: BookingStep3Fragment? = null
    private lateinit var bengkelDoo: DocumentReference
    private lateinit var iTimeSlotLoadListener: ITimeSlotLoadListener
    private lateinit var dialog: AlertDialog
    private lateinit var localBroadcastManager: LocalBroadcastManager
    private lateinit var simpleDateFormat: SimpleDateFormat
    private lateinit var mRv_time_slot: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        iTimeSlotLoadListener = this
        localBroadcastManager = LocalBroadcastManager.getInstance(this!!.context!!)
        localBroadcastManager.registerReceiver(displayTimeSlot, IntentFilter(Common.KEY_DISPLAY_TIME_SLOT))

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
            loadAvailableTimeSlotOfBengkel(Common.currentBengkel!!.bengkelId, simpleDateFormat.format(date.time))
        }
    }

    private fun loadAvailableTimeSlotOfBengkel(bengkelId: String, bookDate: String?) {
        dialog.show()

        // /Cabang/Semolowaru/Branch/03Fw4uig9UPvUSyjhdrR/Salon/fu9kIFHAcDggmEUmItcu
        bengkelDoo = FirebaseFirestore.getInstance()
            .collection("Cabang")
            .document(Common.city)
            .collection("Branch")
            .document(Common.currentSalon!!.salonId)
            .collection("Salon")
            .document(Common.currentBengkel!!.bengkelId)

        //Get informasi this bengkel
        bengkelDoo.get().addOnCompleteListener {
            if (it.isSuccessful){
                val documentSnapshot = it.result
                if (documentSnapshot!!.exists()){
                    //get informasi booking
                    //jika tidak created, return empty
                    val date = FirebaseFirestore.getInstance()
                        .collection("Cabang")
                        .document(Common.city)
                        .collection("Branch")
                        .document(Common.currentSalon!!.salonId)
                        .collection("Salon")
                        .document(Common.currentBengkel!!.bengkelId)
                        .collection(bookDate!!)

                    date.get().addOnCompleteListener {
                        if (it.isSuccessful){
                            val querySnapshot = it.result!!
                            if (querySnapshot.isEmpty){ //jika tidak ada janji
                                iTimeSlotLoadListener.onTimeSlotLoadEmpty()
                            }
                            else{//jika ada janji
                                Log.d("step3", "ada janji")
                                val timeSlot = ArrayList<TimeSlot>()
                                for (document in it.result!!)
                                    timeSlot.add(document.toObject(TimeSlot::class.java))
                                iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlot)
                            }
                        }
                    }.addOnFailureListener {
                        iTimeSlotLoadListener.onTimeSlotLoadFailed(it.message!!)
                    }
                }
            }
        }
    }

    fun getmInstance(): BookingStep3Fragment{
        if (mInstance == null)
            mInstance = BookingStep3Fragment()
        return mInstance!!
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val itemView = inflater.inflate(R.layout.fragment_booking_step_three, container,false)
        initView(itemView)
        return itemView
    }

    private fun initView(itemView: View){
        mRv_time_slot = itemView.rv_time_slot_step3
        mRv_time_slot.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(activity!!, 3)
        mRv_time_slot.layoutManager = gridLayoutManager
        mRv_time_slot.addItemDecoration(SpaceItemDecoration(8))

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
                if (Common.currentDate.timeInMillis != date!!.timeInMillis){
                    Common.currentDate = date!! //tidak load lagi jika milih hari baru yang sama dg hari yg telah dipilih
                    loadAvailableTimeSlotOfBengkel(Common.currentBengkel!!.bengkelId, simpleDateFormat.format(date.time))

                }
            }

        }
    }

    override fun onTimeSlotLoadSuccess(timeSlotList: ArrayList<TimeSlot>) {
        val adapter = MyTimeSlotAdapter(context!!, timeSlotList)
        mRv_time_slot.adapter = adapter

        dialog.dismiss()
    }

    override fun onTimeSlotLoadFailed(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        dialog.dismiss()
    }

    override fun onTimeSlotLoadEmpty() {
        val adapter = MyTimeSlotAdapter(context!!)
        mRv_time_slot.adapter = adapter

        dialog.dismiss()
    }
}
