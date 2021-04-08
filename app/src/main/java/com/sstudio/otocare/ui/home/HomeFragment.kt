package com.sstudio.otocare.ui.home


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sstudio.core.data.Resource
import com.sstudio.core.domain.model.User
import com.sstudio.otocare.R
import com.sstudio.otocare.databinding.FragmentHomeBinding
import com.sstudio.otocare.services.PicassoImageLoadingServices
import com.sstudio.otocare.ui.booking.BookingActivity
import com.sstudio.otocare.ui.home.adapter.HomeSliderAdapter
import com.sstudio.otocare.ui.home.adapter.LookBookAdapter
import dmax.dialog.SpotsDialog
import org.koin.android.viewmodel.ext.android.viewModel
import ss.com.bannerslider.Slider


class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModel()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var dialog: AlertDialog
    private var currentUser = User()
    private var alreadyBooking = false
    private var activeBookingId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Slider.init(PicassoImageLoadingServices())

        setUserInformation()
        loadBanner()
        loadLookBook()
        loadBookingInformation()

        dialog = SpotsDialog.Builder().setContext(requireContext()).setCancelable(false).build()

        binding.cvBooking.setOnClickListener {
            if (!alreadyBooking) {
                val intent = Intent(activity, BookingActivity::class.java)
                intent.putExtra(BookingActivity.EXTRA_USER, currentUser)
                startActivity(intent)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.already_booking_your_account),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnCancel.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Batalkan Booking")
                .setMessage("Apakah anda yakin membatalkan booking?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(
                    android.R.string.yes
                ) { _, _ ->
                    viewModel.setCancelBooking(activeBookingId)
                        .observe(viewLifecycleOwner) { resource ->
                            when (resource) {
                                is Resource.Loading -> dialog.show()
                                is Resource.Success -> {
                                    dialog.dismiss()
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.booking_has_canceled),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                is Resource.Error -> {
                                    dialog.dismiss()
                                    Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                }
                .setNegativeButton(android.R.string.no, null)
                .show()
        }
    }

    private fun loadBookingInformation() {
        viewModel.getBookingInformation().observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> dialog.show()
                is Resource.Success -> {
                    dialog.dismiss()
                    if (resource.data == null) {
                        binding.layoutBookingInfo.visibility = View.GONE
                        alreadyBooking = false
                    } else {
                        alreadyBooking = true
                        activeBookingId = resource.data!!.id
                        binding.layoutBookingInfo.visibility = View.VISIBLE
                        binding.tvGarageBookingInfo.text = resource.data?.garage?.name
                        binding.tvAddressBookingInfo.text = resource.data?.garage?.address
                        binding.tvPackageBookingInfo.text = resource.data?.pkg?.name
                        binding.tvTimeBookingInfo.text =
                            "${resource.data?.timeSlot?.timeStart}-${resource.data?.timeSlot?.timeFinish} / ${resource.data?.date}"
                    }
                }
                is Resource.Error -> {
                    dialog.dismiss()
                    Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadLookBook() {
        viewModel.getHomeLookBook?.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> dialog.show()
                is Resource.Success -> {
                    dialog.dismiss()
                    resource.data?.let {
                        binding.rvLookbook.setHasFixedSize(true)
                        binding.rvLookbook.layoutManager = LinearLayoutManager(activity)
                        binding.rvLookbook.adapter = LookBookAdapter(requireActivity(), it)
                    }
                }
                is Resource.Error -> {
                    dialog.dismiss()
                    Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadBanner() {
        viewModel.getHomeBanner?.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> dialog.show()
                is Resource.Success -> {
                    dialog.dismiss()
                    resource.data?.let {
//                        Log.d("mytag", "$it")
                        binding.bannerSliderHome.setAdapter(HomeSliderAdapter(it))
                    }
                }
                is Resource.Error -> {
                    dialog.dismiss()
                    Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setUserInformation() {
        HomeActivity.currentUser.observe(viewLifecycleOwner) {
            binding.tvUserName.text = it.name
            currentUser = it
        }
    }
}
