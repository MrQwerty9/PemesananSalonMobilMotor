package com.sstudio.otocare.ui.booking


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.sstudio.core.data.Resource
import com.sstudio.core.domain.model.Booking
import com.sstudio.core.domain.model.Cart
import com.sstudio.core.domain.model.User
import com.sstudio.otocare.R
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
    private var cart: ArrayList<Cart>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingStepOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        dialog = SpotsDialog.Builder().setContext(activity).setCancelable(false).build()
        currentUser = requireActivity().intent.getParcelableExtra(BookingActivity.EXTRA_USER)
        cart = requireActivity().intent.getParcelableArrayListExtra(BookingActivity.EXTRA_CART)
        navController = Navigation.findNavController(view)
        initView()
        loadAllPackage()
    }

    private fun setToolbar() {
        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as BookingActivity?)?.setStep(0)
        (activity as AppCompatActivity?)?.supportActionBar?.title =
            getString(R.string.choose_package)
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
                        if (viewModel.savedStatePackage >= 0) {
                            packageAdapter.setSelectedPosition(viewModel.savedStatePackage)
                            setEnableNextBtn()
                        }
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

        if (packageAdapter.itemSelected != null) {
            setEnableNextBtn()
        }
        binding.btnNextStep.setOnClickListener {
            if (currentUser != null && packageAdapter.itemSelected != null) {
                val action =
                    BookingStep1FragmentDirections.actionGotoStepTwo(
                        Booking(
                            cart = cart ?: listOf(),
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

        packageAdapter.selectedPackagePosition = {
            setEnableNextBtn()
            viewModel.savedStatePackage = it
        }
    }

    private fun setEnableNextBtn() {
        binding.btnNextStep.isEnabled = true
        binding.btnNextStep.background =
            ContextCompat.getDrawable(requireActivity(), R.drawable.button_primary_radius)
    }

}
