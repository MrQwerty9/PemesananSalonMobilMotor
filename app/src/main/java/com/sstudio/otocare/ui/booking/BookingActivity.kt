package com.sstudio.otocare.ui.booking

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.ViewPager
import com.sstudio.otocare.R
import com.sstudio.otocare.common.Common
import com.sstudio.otocare.databinding.ActivityBookingBinding
import com.sstudio.otocare.ui.booking.adapter.ViePagerAdapter
import dmax.dialog.SpotsDialog
import org.koin.android.viewmodel.ext.android.viewModel

class BookingActivity : AppCompatActivity() {

    lateinit var localBroadcastManager: LocalBroadcastManager
    lateinit var dialog: AlertDialog

    //    lateinit var barberRef: CollectionReference
    private lateinit var binding: ActivityBookingBinding
    private val viewModel: BookingViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)
        binding = ActivityBookingBinding.inflate(layoutInflater)

        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.registerReceiver(
            buttonNextReceiver,
            IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT)
        )
        dialog = SpotsDialog.Builder().setContext(this).setCancelable(false).build()
        setupStepView()
        setColorButton()

        binding.viewpagerBooking.adapter = ViePagerAdapter(supportFragmentManager)
        binding.viewpagerBooking.offscreenPageLimit = 4
        binding.viewpagerBooking.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
//                if (position == 0)
//                    btn_prevstep.isEnabled = false
//                else
//                    btn_prevstep.isEnabled = true
                binding.stepViewBooking.go(position, true)
                binding.btnNextStep.isEnabled = false
                //show step
                binding.btnPrevStep.isEnabled = position != 0

                setColorButton()
            }
        })

        binding.btnPrevStep.setOnClickListener {
            if (Common.step == 3 || Common.step > 0) {
                Common.step--
                binding.viewpagerBooking.currentItem = Common.step
                if (Common.step < 3) {
                    binding.btnNextStep.isEnabled = true
                    setColorButton()
                }
            }
        }

        binding.btnNextStep.setOnClickListener {
            if (Common.step < 3 || Common.step == 0) {
                Common.step++
                if (Common.step == 1) { //setelah pilih salon
                    if (Common.currentSalon != null)
                        loadBarberBySalon(Common.currentSalon!!.id)
                } else if (Common.step == 2) { //pick slot waktu
                    if (Common.currentBengkel != null)
                        loadTimeSlotBengkel(Common.currentBengkel!!.id)
                } else if (Common.step == 3) { //confirm
                    if (Common.currentTimeSlot != -1)
                        confirmBooking()
                }
                binding.viewpagerBooking.currentItem = Common.step
            }
        }
    }

    private fun confirmBooking() {
        //kitim broadcast ke fragment step 4
        val intent = Intent(Common.KEY_CONFIRM_BOOKING)
        localBroadcastManager.sendBroadcast(intent)
    }

    private fun loadTimeSlotBengkel(bengkelId: String) {
        //kirim local broadcast ke fragment step 3
        val intent = Intent(Common.KEY_DISPLAY_TIME_SLOT)
        localBroadcastManager.sendBroadcast(intent)
    }

    private fun loadBarberBySalon(salonId: String) {
        dialog.show()

        //pilih semua bengkel di salon
        //Cabang/Semolowaru/Branch/03Fw4uig9UPvUSyjhdrR/Salon/9x9RvTB44HtJ85KztRPP
//        barberRef = FirebaseFirestore.getInstance()
//            .collection("Cabang")
//            .document(Common.city)
//            .collection("Branch")
//            .document(salonId)
//            .collection("Salon")
//        barberRef.get()
//            .addOnCompleteListener {
//                val bengkels = ArrayList<com.sstudio.core.domain.model.Bengkel>()
//                for (bengkelsnapShot: QueryDocumentSnapshot in it.result!!) {
//                    val bengkel =
//                        bengkelsnapShot.toObject(com.sstudio.core.domain.model.Bengkel::class.java)
//                    bengkel.password = ""
//                    bengkel.bengkelId = bengkelsnapShot.id
//
//                    bengkels.add(bengkel)
//                }
//                val intent = Intent(Common.KEY_BENGKEL_LOAD_DONE)
//                intent.putParcelableArrayListExtra(Common.KEY_BENGKEL_LOAD_DONE, bengkels)
//                localBroadcastManager.sendBroadcast(intent)
//                dialog.dismiss()
//            }
//            .addOnFailureListener {
//
//            }
    }

    @SuppressLint("ResourceAsColor")
    private fun setColorButton() {
        if (binding.btnNextStep.isEnabled)
            binding.btnNextStep.setBackgroundColor(R.color.button1)
        else
            binding.btnNextStep.setBackgroundColor(R.color.abu)
        if (binding.btnPrevStep.isEnabled)
            binding.btnPrevStep.setBackgroundColor(R.color.button1)
        else
            binding.btnPrevStep.setBackgroundColor(R.color.abu)
    }

    private fun setupStepView() {
        val stepList: ArrayList<String> = ArrayList()
        stepList.add("Salon")
        stepList.add("Care")
        stepList.add("Time")
        stepList.add("Confirm")
        binding.stepViewBooking.setSteps(stepList)
    }



    private val buttonNextReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            val step = intent.getIntExtra(Common.KEY_STEP, 0)
            if (step == 1)
                Common.currentSalon = intent.getParcelableExtra(Common.KEY_SALON_STORE)
            else if(step == 2)
                Common.currentBengkel = intent.getParcelableExtra(Common.KEY_BENGKEL_SELECTED)
            else if(step == 3)
                Common.currentTimeSlot = intent.getIntExtra(Common.KEY_TIME_SLOT, -1)
            binding.btnNextStep.isEnabled = true
        }
    }
}
