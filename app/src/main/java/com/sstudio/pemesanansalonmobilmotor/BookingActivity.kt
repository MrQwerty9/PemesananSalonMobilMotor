package com.sstudio.pemesanansalonmobilmotor

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.ViewPager
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.sstudio.pemesanansalonmobilmotor.Model.Bengkel
import com.sstudio.pemesanansalonmobilmotor.adapter.ViePagerAdapter
import com.sstudio.pemesanansalonmobilmotor.common.Common
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
        dialog = SpotsDialog.Builder().setContext(this).build()
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
                btn_prevstep.isEnabled = position != 0

                setColorButton()
            }
        })

        btn_prevstep.setOnClickListener {
            if (Common.step == 3 || Common.step > 0){
                Common.step--
                viewpager_bookig.currentItem = Common.step
            }
        }

        btn_nextstep.setOnClickListener {
            if (Common.step < 3 || Common.step == 0){
                Common.step++
                if (Common.step == 1){
                    if (Common.currentSalon != null)
                        loadBarberBySalon(Common.currentSalon!!.salonId)
                }
                viewpager_bookig.currentItem = Common.step
            }
        }
    }

    private fun loadBarberBySalon(salonId: String) {
        dialog.show()
        //Cabang/Semolowaru/Branch/03Fw4uig9UPvUSyjhdrR/Salon/9x9RvTB44HtJ85KztRPP
        barberRef = FirebaseFirestore.getInstance()
            .collection("Cabang")
            .document(Common.city)
            .collection("Branch")
            .document(salonId)
            .collection("Salon")
        barberRef.get()
            .addOnCompleteListener {
                val bengkels = ArrayList<Bengkel>()
                for (bengkelsnapShot: QueryDocumentSnapshot in it.result!!){
                    val bengkel = bengkelsnapShot.toObject(Bengkel::class.java)
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
            Common.currentSalon = intent.getParcelableExtra(Common.KEY_SALON_STORE)
            btn_nextstep.isEnabled = true
        }
    }
}
