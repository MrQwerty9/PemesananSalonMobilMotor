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
import com.sstudio.otocare.databinding.FragmentHomeBinding
import com.sstudio.otocare.services.PicassoImageLoadingServices
import com.sstudio.otocare.ui.booking.BookingActivity
import com.sstudio.otocare.ui.home.adapter.HomeSliderAdapter
import com.sstudio.otocare.ui.home.adapter.LookbookAdapter
import dmax.dialog.SpotsDialog
import org.koin.android.viewmodel.ext.android.viewModel
import ss.com.bannerslider.Slider

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModel()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var dialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        dialog = SpotsDialog.Builder().setContext(requireContext()).setCancelable(false).build()

        binding.cvBooking.setOnClickListener {
            startActivity(Intent(activity, BookingActivity::class.java))
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
                        binding.rvLookbook.adapter = LookbookAdapter(requireActivity(), it)
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
        viewModel.getHomeLookBook?.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> dialog.show()
                is Resource.Success -> {
                    dialog.dismiss()
                    resource.data?.let {
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
        binding.tvUserName.text = HomeActivity.currentUser?.name
    }
}
