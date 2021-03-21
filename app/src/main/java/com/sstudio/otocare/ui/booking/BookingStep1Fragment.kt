package com.sstudio.otocare.ui.booking



import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.sstudio.otocare.common.Common
import com.sstudio.otocare.common.SpaceItemDecoration
import com.sstudio.otocare.databinding.FragmentBookingStepOneBinding
import com.sstudio.otocare.listener.IAllSalonLoadListener
import com.sstudio.otocare.listener.IBranchLoadListener
import com.sstudio.otocare.ui.booking.adapter.SalonAdapter
import dmax.dialog.SpotsDialog

class BookingStep1Fragment : Fragment(), IAllSalonLoadListener, IBranchLoadListener {

    //    lateinit var allSalonRef: CollectionReference
//    lateinit var branchRef: CollectionReference
    lateinit var iAllSalonLoadListener: IAllSalonLoadListener
    lateinit var iBranchLoadListener: IBranchLoadListener
    lateinit var dialog: AlertDialog
    var mInstance: BookingStep1Fragment? = null
    private var _binding: FragmentBookingStepOneBinding? = null
    private val binding get() = _binding!!

    fun get_nstance(): BookingStep1Fragment {
        if (mInstance == null)
            mInstance = BookingStep1Fragment()
        return mInstance!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingStepOneBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //        allSalonRef = FirebaseFirestore.getInstance().collection("Cabang")
        iAllSalonLoadListener = this
        iBranchLoadListener = this
        dialog = SpotsDialog.Builder().setContext(activity).setCancelable(false).build()

        initView()
        loadAllSalon()
    }

    private fun initView() {
        binding.rvBookingStepOne.setHasFixedSize(true)
        binding.rvBookingStepOne.layoutManager = GridLayoutManager(activity, 2)
        binding.rvBookingStepOne.addItemDecoration(SpaceItemDecoration(4))
    }

    private fun loadAllSalon() {
//        allSalonRef.get()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val list = ArrayList<String>()
//                    list.add("Silahkan pilih kota")
//                    for (queryDocumentSnapshot in task.result!!) {
//                        list.add(queryDocumentSnapshot.id)
//                    }
//                    iAllSalonLoadListener.onAllSalonLoadSuccess(list)
//                }
//            }.addOnFailureListener { e -> iAllSalonLoadListener.onAllSalonLoadFailed(e.message!!) }
    }

    override fun onAllSalonLoadSuccess(areaNameList: List<String>) {
        binding.spinBookingStepOne.setItems(areaNameList)
        binding.spinBookingStepOne.setOnItemSelectedListener { view, position, id, item ->
            if (position > 0)
                loadBranchOfCity(item.toString())
            else
                binding.rvBookingStepOne.visibility = View.GONE
        }
    }

    private fun loadBranchOfCity(cityName: String) {
        dialog.show()
        Common.city = cityName
//        branchRef = FirebaseFirestore.getInstance()
//            .collection("Cabang")
//            .document(cityName)
//            .collection("Branch")
//
//        branchRef.get().addOnCompleteListener { task ->
//            val list = ArrayList<com.sstudio.core.domain.model.Salon>()
//            if (task.isSuccessful){
//                for (queryDocumentSnapshot in task.result!!) {
//                    val salon =
//                        queryDocumentSnapshot.toObject(com.sstudio.core.domain.model.Salon::class.java)
//                    salon.salonId = queryDocumentSnapshot.id
//                    list.add(salon)
//                }
//
//                iBranchLoadListener.onBranchLoadSuccess(list)
//            }
//        }.addOnFailureListener {
//            iBranchLoadListener.onBranchLoadFailed(it.message!!)
//        }
    }

    override fun onAllSalonLoadFailed(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBranchLoadSuccess(salonList: List<com.sstudio.core.domain.model.Salon>) {
        val adapter = SalonAdapter(requireActivity(), salonList)
        binding.rvBookingStepOne.adapter = adapter
        binding.rvBookingStepOne.visibility = View.VISIBLE

        dialog.dismiss()
    }

    override fun onBranchLoadFailed(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        dialog.dismiss()
    }

}
