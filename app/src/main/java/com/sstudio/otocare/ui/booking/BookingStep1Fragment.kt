package com.sstudio.otocare.ui.booking


import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.sstudio.core.data.Resource
import com.sstudio.core.domain.model.Booking
import com.sstudio.core.domain.model.User
import com.sstudio.otocare.databinding.FragmentBookingStepOneBinding
import com.sstudio.otocare.ui.booking.adapter.PackageAdapter
import dmax.dialog.SpotsDialog
import org.koin.android.viewmodel.ext.android.viewModel

/*
Order bundle
 */
class BookingStep1Fragment : Fragment() {

    lateinit var dialog: AlertDialog
    private val viewModel: BookingViewModel by viewModel()
    private var packageAdapter = PackageAdapter()
    private var _binding: FragmentBookingStepOneBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController


    //    private var selectedSpinnerCity = 0
//    private var selectedGaragePosition = -1
    private var currentUser: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingStepOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as BookingActivity?)?.setStep(0)
        binding.toolbar.title = "Pilih Paket"
        dialog = SpotsDialog.Builder().setContext(activity).setCancelable(false).build()
        currentUser = requireActivity().intent.getParcelableExtra(BookingActivity.EXTRA_USER)
        navController = Navigation.findNavController(view)
        initView()
        loadAllPackage()

        binding.btnNextStep.isEnabled = true
        binding.btnNextStep.setOnClickListener {
            Log.d("mytag", "user $currentUser")
            if (currentUser != null && packageAdapter.itemSelected != null) {
                val action =
                    BookingStep1FragmentDirections.actionGotoStepTwo(
                        Booking(
                            customer = currentUser!!,
                            pkg = packageAdapter.itemSelected!!
                        )
                    )
                navController.navigate(action)
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun loadAllPackage() {
        viewModel.getPackage?.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> dialog.show()
                is Resource.Success -> {
                    dialog.dismiss()
                    resource.data?.let {
                        packageAdapter.setPackage(it)
                        binding.rvPackage.adapter = packageAdapter
                    }
                }
                is Resource.Error -> {
                    dialog.dismiss()
                    Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initView() {
        binding.rvPackage.setHasFixedSize(true)
        binding.rvPackage.layoutManager = LinearLayoutManager(requireContext())
    }

}
