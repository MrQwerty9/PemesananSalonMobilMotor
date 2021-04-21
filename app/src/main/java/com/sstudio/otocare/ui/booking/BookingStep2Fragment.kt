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
import androidx.recyclerview.widget.GridLayoutManager
import com.sstudio.core.data.Resource
import com.sstudio.core.domain.model.Booking
import com.sstudio.core.domain.model.User
import com.sstudio.otocare.R
import com.sstudio.otocare.common.SpaceItemDecoration
import com.sstudio.otocare.databinding.FragmentBookingStepTwoBinding
import com.sstudio.otocare.ui.booking.adapter.GarageAdapter
import dmax.dialog.SpotsDialog
import org.koin.android.viewmodel.ext.android.viewModel

class BookingStep2Fragment : Fragment() {

    lateinit var dialog: AlertDialog
    private val viewModel: BookingViewModel by viewModel()
    private val garageAdapter = GarageAdapter()
    private var currentUser: User? = null
    private lateinit var bookingBundle: Booking
    private lateinit var navController: NavController
    private var _binding: FragmentBookingStepTwoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingStepTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        bookingBundle = BookingStep2FragmentArgs.fromBundle(requireArguments()).booking
        dialog = SpotsDialog.Builder().setContext(activity).setCancelable(false).build()
        currentUser = requireActivity().intent.getParcelableExtra(BookingActivity.EXTRA_USER)
        navController = Navigation.findNavController(view)
        initView()
        loadAllCityOfGarage()
        loadBranchOfCity()
    }

    private fun setToolbar() {
        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as BookingActivity?)?.setStep(1)
        (activity as AppCompatActivity?)?.supportActionBar?.title =
            getString(R.string.choose_location)
    }

    private fun initView() {
        binding.rvGarage.setHasFixedSize(true)
        binding.rvGarage.layoutManager = GridLayoutManager(activity, 2)
        binding.rvGarage.addItemDecoration(SpaceItemDecoration(4))

        binding.btnNextStep.setOnClickListener {
            if (currentUser != null && garageAdapter.itemSelected != null) {
                val action =
                    BookingStep2FragmentDirections.actionGotoStepThree(
                        Booking(
                            customer = currentUser!!,
                            garage = garageAdapter.itemSelected!!,
                            pkg = bookingBundle.pkg
                        )
                    )
                navController.navigate(action)
            }
        }

        garageAdapter.selectedGaragePosition = {
            setEnableNextBtn()
            viewModel.savedStateItemGarage = it
        }
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun loadAllCityOfGarage() {
        viewModel.getAllCityOfGarage?.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> dialog.show()
                is Resource.Success -> {
                    dialog.dismiss()
                    resource.data?.let {
                        var listCityName = it.map { city ->
                            city.name
                        }
                        listCityName = listOf("Silahkan Pilih") + listCityName
                        binding.spinBookingStepOne.setItems(listCityName)

                        binding.spinBookingStepOne.selectedIndex = viewModel.savedStateSpinnerCity
                        binding.spinBookingStepOne.setOnItemSelectedListener { view, position, id, item ->
                            viewModel.savedStateItemGarage = -1 //reset
                            viewModel.savedStateSpinnerCity = position
                            if (position > 0)
//                                loadBranchOfCity(it[position - 1].id)
                                viewModel.city.value = it[position - 1].id
                            else
                                binding.rvGarage.visibility = View.GONE
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

    private fun loadBranchOfCity() {
        dialog.show()
        viewModel.getBranchOfCity.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> dialog.show()
                is Resource.Success -> {
                    dialog.dismiss()
                    resource.data?.let {
                        binding.rvGarage.visibility = View.VISIBLE

                        garageAdapter.setGarageList(it)
                        binding.rvGarage.adapter = garageAdapter
                        if (viewModel.savedStateItemGarage >= 0) {
                            garageAdapter.setSelectedPosition(viewModel.savedStateItemGarage)
                            setEnableNextBtn()
                        }
                        dialog.dismiss()
                    }
                }
                is Resource.Error -> {
                    dialog.dismiss()
                    Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setEnableNextBtn() {
        binding.btnNextStep.isEnabled = true
        binding.btnNextStep.background =
            ContextCompat.getDrawable(requireActivity(), R.drawable.button_primary_radius)
    }
}
