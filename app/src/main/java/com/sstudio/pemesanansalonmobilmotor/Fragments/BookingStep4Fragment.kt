package com.sstudio.pemesanansalonmobilmotor.Fragments

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
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.firestore.FirebaseFirestore
import com.sstudio.pemesanansalonmobilmotor.Model.BookingInformasi
import com.sstudio.pemesanansalonmobilmotor.R
import com.sstudio.pemesanansalonmobilmotor.common.Common
import kotlinx.android.synthetic.main.fragment_booking_step_four.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 *
 */
class BookingStep4Fragment : Fragment(), View.OnClickListener {
    override fun onClick(p0: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var mInstance: BookingStep4Fragment? = null
    private val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    private lateinit var localBroadCastManager: LocalBroadcastManager
    private lateinit var mTv_phone: TextView
    private lateinit var mTv_lokasi: TextView
    private lateinit var mTv_waktu: TextView
    private lateinit var mTv_nama_salon: TextView
    private lateinit var mTv_nama_teknisi: TextView
    private lateinit var btn_confirm: Button
    private val bookingInformasi = BookingInformasi()

    fun getmInstance(): BookingStep4Fragment {
        if (mInstance == null)
            mInstance = BookingStep4Fragment()
        return mInstance!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val itemView = inflater.inflate(R.layout.fragment_booking_step_four, container, false)
        mTv_lokasi = itemView.tv_booking4_lokasi
        mTv_phone = itemView.tv_booking4_telp
        mTv_waktu = itemView.tv_booking4_waktu
        mTv_nama_salon = itemView.tv_nama_salon
        mTv_nama_teknisi = itemView.tv_booking_teknisi
        btn_confirm = itemView.btn_booking4_konfirm

        itemView.setOnClickListener(this)
        Log.d("mytag", "itemview")

        return itemView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        localBroadCastManager = LocalBroadcastManager.getInstance(context!!)
        localBroadCastManager.registerReceiver(confirmBookingReceiver(), IntentFilter(Common.KEY_CONFIRM_BOOKING))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_confirm.setOnClickListener {
            Log.d("mytag", "klik2")

            //booking informasi
            Log.d("mytag", "klik")
            bookingInformasi.bengkelId = Common.currentBengkel!!.bengkelId
            bookingInformasi.bengkelNama = Common.currentBengkel!!.nama
            bookingInformasi.customerNama = Common.currentUser!!.nama
            bookingInformasi.customerPhone = Common.currentUser!!.nomorHp
            bookingInformasi.salonId = Common.currentSalon!!.salonId
            bookingInformasi.salonAlamat = Common.currentSalon!!.alamat
            bookingInformasi.salonNama = Common.currentSalon!!.nama
            bookingInformasi.slot = Common.currentTimeSlot.toLong()
            bookingInformasi.waktu = StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                .append(" " + simpleDateFormat.format(Common.currentDate.time)).toString()

            val bookingDate = FirebaseFirestore.getInstance()
                .collection("Cabang")
                .document(Common.city)
                .collection("Branch")
                .document(Common.currentSalon!!.salonId)
                .collection("Salon")
                .document(Common.currentBengkel!!.bengkelId)
                .collection(Common.simpleDateFormat.format(Common.currentDate.time))
                .document(Common.currentTimeSlot.toString())

            //write data
            Log.d("mytag step4", bookingInformasi.customerNama)
            bookingDate.set(bookingInformasi)
                .addOnSuccessListener {
                    resetStaticData()
                    activity?.finish() //close activity
                    Toast.makeText(context, "Berhasil!", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(context, "" + it.message, Toast.LENGTH_SHORT).show()
                }
        }

    }

    private fun resetStaticData() {
        Common.step = 0
        Common.currentTimeSlot = -1
        Common.currentSalon = null
        Common.currentBengkel = null
        Common.currentDate.add(Calendar.DATE, 0) //current date
    }

    fun confirmBookingReceiver() = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            setData()
        }
    }


    private fun setData() {
        mTv_waktu.text = StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
            .append(" " + simpleDateFormat.format(Common.currentDate.time))
        mTv_nama_teknisi.text = Common.currentBengkel?.nama
        mTv_phone.text = Common.currentSalon?.phone
        mTv_nama_salon.text = Common.currentSalon?.nama


    }

}
