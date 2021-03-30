package com.sstudio.otocare.ui.booking

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.sstudio.core.data.Resource
import com.sstudio.core.domain.model.Booking
import com.sstudio.otocare.databinding.FragmentBookingStepFourBinding
import dmax.dialog.SpotsDialog
import org.koin.android.viewmodel.ext.android.viewModel

class BookingStep4Fragment : Fragment() {

    private val viewModel: BookingViewModel by viewModel()
    private lateinit var navController: NavController
    private lateinit var dialog: AlertDialog
    private lateinit var bookingBundle: Booking
    private var _binding: FragmentBookingStepFourBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingStepFourBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookingBundle = BookingStep4FragmentArgs.fromBundle(requireArguments()).booking
        dialog = SpotsDialog.Builder().setContext(context).setCancelable(false).build()
        navController = Navigation.findNavController(view)

        setToolbar()
        setData()

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnConfirm.setOnClickListener {
            viewModel.setBooking(bookingBundle).observe(viewLifecycleOwner) { resource ->
                when (resource) {
                    is Resource.Loading -> dialog.show()
                    is Resource.Success -> {
                        dialog.dismiss()
                        resource.data?.let {
                            val action = BookingStep4FragmentDirections.actionGotoFinish()
                            navController.navigate(action)
                        }
                    }
                    is Resource.Error -> {
                        dialog.dismiss()
                        Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun setToolbar() {
        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as BookingActivity?)?.setStep(3)
        binding.toolbar.title = "Konfirmasi"
    }


    private fun setData() {
        binding.tvTime.text = bookingBundle.timeSlot.timeSlot
        binding.tvBookingTechnician.text = bookingBundle.pkg.name
        binding.tvPhone.text = bookingBundle.customer.phoneNumber
        binding.tvGarageName.text = bookingBundle.garage.name
    }

}
