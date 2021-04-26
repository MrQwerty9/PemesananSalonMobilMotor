package com.sstudio.otocare.ui.booking

import android.Manifest
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sstudio.otocare.databinding.ActivityBookingBinding
import com.sstudio.otocare.utils.PermissionManager
import dmax.dialog.SpotsDialog

class BookingActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_CART = "extra_cart"
    }

    lateinit var dialog: AlertDialog
    private lateinit var binding: ActivityBookingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = SpotsDialog.Builder().setContext(this).setCancelable(false).build()
        setupStepView()
        checkPermission()
    }

    fun setStep(step: Int) {
        binding.stepViewBooking.go(step, true)
    }

    fun hideStepView() {
        binding.stepViewBooking.visibility = View.GONE
    }

    private fun checkPermission() {
        PermissionManager.request(this, Manifest.permission.READ_CALENDAR, 101)
        PermissionManager.request(this, Manifest.permission.WRITE_CALENDAR, 101)
    }

    private fun setupStepView() {
        val stepList: ArrayList<String> = ArrayList()
        stepList.add("Paket")
        stepList.add("Lokasi")
        stepList.add("Waktu")
        stepList.add("Konfirmasi")
        binding.stepViewBooking.setSteps(stepList)
    }
}
