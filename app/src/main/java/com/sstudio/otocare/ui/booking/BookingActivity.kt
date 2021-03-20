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
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.sstudio.core.domain.model.Bengkel
import com.sstudio.otocare.R
import com.sstudio.otocare.adapter.ViePagerAdapter
import com.sstudio.otocare.common.Common
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_booking.*

class BookingActivity : AppCompatActivity() {

    lateinit var localBroadcastManager: LocalBroadcastManager
    lateinit var dialog: AlertDialog
    lateinit var barberRef: CollectionReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.registerReceiver(buttonNextReceiver, IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT))
        dialog = SpotsDialog.Builder().setContext(this).setCancelable(false).build()
        setupStepView()
        setColorButton()

        viewpager_bookig.adapter = ViePagerAdapter(supportFragmentManager)
        viewpager_bookig.offscreenPageLimit = 4
        viewpager_bookig.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
//                if (position == 0)
//                    btn_prevstep.isEnabled = false
//                else
//                    btn_prevstep.isEnabled = true
                stepview_booking.go(position, true)
                btn_nextstep.isEnabled = false
                //show step
                btn_prevstep.isEnabled = position != 0

                setColorButton()
            }
        })

        btn_prevstep.setOnClickListener {
            if (Common.step == 3 || Common.step > 0){
                Common.step--
                viewpager_bookig.currentItem = Common.step
                if(Common.step < 3){
                    btn_nextstep.isEnabled = true
                    setColorButton()
                }
            }
        }

        btn_nextstep.setOnClickListener {
            if (Common.step < 3 || Common.step == 0){
                Common.step++
                if (Common.step == 1){ //setelah pilih salon
                    if (Common.currentSalon != null)
                        loadBarberBySalon(Common.currentSalon!!.salonId)
                }
                else if (Common.step ==  2){ //pick slot waktu
                    if (Common.currentBengkel != null)
                        loadTimeSlotBengkel(Common.currentBengkel!!.bengkelId)
                }
                else if (Common.step ==  3){ //confirm
                    if (Common.currentTimeSlot != -1)
                        confirmBooking()
                }
                viewpager_bookig.currentItem = Common.step
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
        barberRef = FirebaseFirestore.getInstance()
            .collection("Cabang")
            .document(Common.city)
            .collection("Branch")
            .document(salonId)
            .collection("Salon")
        barberRef.get()
            .addOnCompleteListener {
                val bengkels = ArrayList<com.sstudio.core.domain.model.Bengkel>()
                for (bengkelsnapShot: QueryDocumentSnapshot in it.result!!) {
                    val bengkel =
                        bengkelsnapShot.toObject(com.sstudio.core.domain.model.Bengkel::class.java)
                    bengkel.password = ""
                    bengkel.bengkelId = bengkelsnapShot.id

                    bengkels.add(bengkel)
                }
                val intent = Intent(Common.KEY_BENGKEL_LOAD_DONE)
                intent.putParcelableArrayListExtra(Common.KEY_BENGKEL_LOAD_DONE, bengkels)
                localBroadcastManager.sendBroadcast(intent)
                dialog.dismiss()
            }
            .addOnFailureListener {

            }
    }

    @SuppressLint("ResourceAsColor")
    private fun setColorButton() {
        if (btn_nextstep.isEnabled)
            btn_nextstep.setBackgroundColor(R.color.button1)
        else
            btn_nextstep.setBackgroundColor(R.color.abu)
        if (btn_prevstep.isEnabled)
            btn_prevstep.setBackgroundColor(R.color.button1)
        else
            btn_prevstep.setBackgroundColor(R.color.abu)
    }

    private fun setupStepView() {
        val stepList: ArrayList<String> = ArrayList()
        stepList.add("Salon")
        stepList.add("Care")
        stepList.add("Time")
        stepList.add("Confirm")
        stepview_booking.setSteps(stepList)
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
            btn_nextstep.isEnabled = true
        }
    }
}
