package com.sstudio.pemesanansalonmobilmotor.Fragments



import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jaredrummler.materialspinner.MaterialSpinner
import com.sstudio.pemesanansalonmobilmotor.Model.Salon
import com.sstudio.pemesanansalonmobilmotor.R
import com.sstudio.pemesanansalonmobilmotor.adapter.MySalonAdapter
import com.sstudio.pemesanansalonmobilmotor.common.SpaceItemDecoration
import com.sstudio.pemesanansalonmobilmotor.listener.IAllSalonLoadListener
import com.sstudio.pemesanansalonmobilmotor.listener.IBranchLoadListener
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_booking_step_one.*
import kotlinx.android.synthetic.main.fragment_booking_step_one.view.*

/**
 * A simple [Fragment] subclass.
 *
 */
class BookingStep1Fragment : Fragment(), IAllSalonLoadListener, IBranchLoadListener {

    lateinit var allSalonRef: CollectionReference
    lateinit var branchRef: CollectionReference
    lateinit var iAllSalonLoadListener : IAllSalonLoadListener
    lateinit var iBranchLoadListener: IBranchLoadListener
    lateinit var dialog: AlertDialog
    lateinit var rv_booking: RecyclerView
    var mInstance: BookingStep1Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        allSalonRef = FirebaseFirestore.getInstance().collection("Cabang")
        iAllSalonLoadListener = this
        iBranchLoadListener = this
        dialog = SpotsDialog.Builder().setContext(activity).build()
        
    }

    fun get_nstance(): BookingStep1Fragment{
        if (mInstance == null)
            mInstance = BookingStep1Fragment()
        return mInstance!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val thisView = inflater.inflate(R.layout.fragment_booking_step_one, container,false)
        rv_booking = thisView.rv_booking_step_one
        initView()
        loadAllSalon()

        return thisView

    }

    private fun initView() {
        rv_booking.setHasFixedSize(true)
        rv_booking.layoutManager = GridLayoutManager(activity, 2)
        rv_booking.addItemDecoration(SpaceItemDecoration(4))
    }

    private fun loadAllSalon() {
        allSalonRef.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = ArrayList<String>()
                    list.add("Silahkan pilih kota")
                    for (queryDocumentSnapshot in task.result!!) {
                        list.add(queryDocumentSnapshot.id)
                    }
                    iAllSalonLoadListener.onAllSalonLoadSuccess(list)
                }
            }.addOnFailureListener { e -> iAllSalonLoadListener.onAllSalonLoadFailed(e.message!!) }
    }

    override fun onAllSalonLoadSuccess(areaNameList: List<String>) {
        spin_booking_step_one.setItems(areaNameList)
        spin_booking_step_one.setOnItemSelectedListener { view, position, id, item ->
            if (position > 0)
                loadBranchOfCity(item.toString())
            else
                rv_booking.visibility = View.GONE
        }
    }

    private fun loadBranchOfCity(cityName: String) {
        dialog.show()
        branchRef = FirebaseFirestore.getInstance()
            .collection("Cabang")
            .document(cityName)
            .collection("Branch")

        branchRef.get().addOnCompleteListener { task ->
            val list = ArrayList<Salon>()
            if (task.isSuccessful){
                for (queryDocumentSnapshot in task.result!!)
                    list.add(queryDocumentSnapshot.toObject(Salon::class.java))
                iBranchLoadListener.onBranchLoadSuccess(list)
            }
        }.addOnFailureListener {
            iBranchLoadListener.onBranchLoadFailed(it.message!!)
        }
    }

    override fun onAllSalonLoadFailed(message: String) {
Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
    override fun onBranchLoadSuccess(salonList: List<Salon>) {
        val adapter = MySalonAdapter(this!!.activity!!, salonList)
        rv_booking.adapter = adapter
        rv_booking.visibility = View.VISIBLE

        dialog.dismiss()
    }

    override fun onBranchLoadFailed(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        dialog.dismiss()
    }

}
