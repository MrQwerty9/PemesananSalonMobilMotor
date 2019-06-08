package com.sstudio.pemesanansalonmobilmotor

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.sstudio.pemesanansalonmobilmotor.adapter.ViePagerAdapter
import kotlinx.android.synthetic.main.activity_booking.*

class BookingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        setupStepView()
        setColorButton()

        viewpager_bookig.adapter = ViePagerAdapter(supportFragmentManager)
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
                btn_prevstep.isEnabled = position != 0

                setColorButton()
            }
        })
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
}
