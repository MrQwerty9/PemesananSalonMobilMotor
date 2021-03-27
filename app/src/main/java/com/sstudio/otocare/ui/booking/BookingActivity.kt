package com.sstudio.otocare.ui.booking

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sstudio.otocare.databinding.ActivityBookingBinding
import dmax.dialog.SpotsDialog

class BookingActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    lateinit var dialog: AlertDialog
    private lateinit var binding: ActivityBookingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = SpotsDialog.Builder().setContext(this).setCancelable(false).build()
        setupStepView()


//        binding.viewpagerBooking.adapter = ViePagerAdapter(supportFragmentManager)
//        binding.viewpagerBooking.offscreenPageLimit = 4
//        binding.viewpagerBooking.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//            override fun onPageScrollStateChanged(state: Int) {
//
//            }
//
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//
//            }
//
//            override fun onPageSelected(position: Int) {
////                if (position == 0)
////                    btn_prevstep.isEnabled = false
////                else
////                    btn_prevstep.isEnabled = true
//                binding.stepViewBooking.go(position, true)
//                binding.btnNextStep.isEnabled = false
//                //show step
//                binding.btnPrevStep.isEnabled = position != 0
//
//                setColorButton()
//            }
//        })

//        binding.btnNextStep.setOnClickListener {
//            if (Common.step < 3 || Common.step == 0) {
//                Common.step++
//                if (Common.step == 1) { //setelah pilih salon
//                    if (Common.currentGarage != null)
//                        loadBarberBySalon(Common.currentGarage!!.id)
//                } else if (Common.step == 2) { //pick slot waktu
//                    if (Common.currentBengkel != null)
//                        loadTimeSlotBengkel(Common.currentBengkel!!.id)
//                } else if (Common.step == 3) { //confirm
//                    if (Common.currentTimeSlot != -1)
//                        confirmBooking()
//                }
//                binding.viewpagerBooking.currentItem = Common.step
//            }
//        }
    }

    fun setStep(step: Int) {
        binding.stepViewBooking.go(step, true)
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
