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
import com.sstudio.otocare.R
import com.sstudio.otocare.common.SpaceItemDecoration
import com.sstudio.otocare.databinding.FragmentBookingStepThreeBinding
import com.sstudio.otocare.ui.booking.adapter.TimeSlotAdapter
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import dmax.dialog.SpotsDialog
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class BookingStep3Fragment : Fragment() {

    private lateinit var dialog: AlertDialog
    private val viewModel: BookingViewModel by viewModel()
    private lateinit var navController: NavController
    private var _binding: FragmentBookingStepThreeBinding? = null
    private val binding get() = _binding!!
    private lateinit var bookingBundle: Booking
    private val timeSlotAdapter = TimeSlotAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingStepThreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        bookingBundle = BookingStep3FragmentArgs.fromBundle(requireArguments()).booking
        setToolbar()
        initView()
        dialog = SpotsDialog.Builder().setContext(context).setCancelable(false).build()

        viewModel.garageSelected = bookingBundle.garage
        viewModel.getTimeSlot.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> dialog.show()
                is Resource.Success -> {
                    dialog.dismiss()
                    resource.data?.let {
                        binding.rvTimeSlotStep3.visibility = View.VISIBLE

                        timeSlotAdapter.setTimeSlot(it)
                        binding.rvTimeSlotStep3.adapter = timeSlotAdapter
                        if (viewModel.savedStateItemTimeSlot >= 0) {
                            timeSlotAdapter.setSelectedPosition(viewModel.savedStateItemTimeSlot)
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

        timeSlotAdapter.selectedTimeSlotPosition = {
            setEnableNextBtn()
            viewModel.savedStateItemTimeSlot = it
        }

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setToolbar() {
        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as BookingActivity?)?.setStep(2)
        binding.toolbar.title = "Pilih Waktu"
    }

    private fun loadAvailableTimeSlot(bengkelId: String, bookDate: String?) {
        dialog.show()
        val date = Calendar.getInstance()
        date.add(Calendar.DATE, 0)
    }

    private fun initView() {
        binding.rvTimeSlotStep3.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(requireActivity(), 3)
        binding.rvTimeSlotStep3.layoutManager = gridLayoutManager
        binding.rvTimeSlotStep3.addItemDecoration(SpaceItemDecoration(8))

        val startDate = Calendar.getInstance(TimeZone.getDefault())
        startDate.add(Calendar.DATE, 1)
        val endDate = Calendar.getInstance(TimeZone.getDefault())
        endDate.add(Calendar.DATE, 7)

        val horizontalCalendar = HorizontalCalendar.Builder(requireActivity(), R.id.calendar_step3)
            .range(startDate, endDate)
            .datesNumberOnScreen(3)
            .mode(HorizontalCalendar.Mode.DAYS)
            .defaultSelectedDate(startDate)
            .build()

        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar?, position: Int) {
                viewModel.savedStateItemTimeSlot = -1
                viewModel.setDateSelected(date?.time)
            }
        }

        binding.btnNextStep.setOnClickListener {
            if (timeSlotAdapter.itemSelected != null) {
                val action =
                    BookingStep3FragmentDirections.actionGotoStepFour(
                        Booking(
                            customer = bookingBundle.customer,
                            garage = bookingBundle.garage,
                            pkg = bookingBundle.pkg,
                            timeSlot = timeSlotAdapter.itemSelected!!,
                            date = viewModel.dateSelected.value ?: ""
                        )
                    )
                navController.navigate(action)
            }
        }
    }

    private fun setEnableNextBtn() {
        binding.btnNextStep.isEnabled = true
        binding.btnNextStep.background =
            ContextCompat.getDrawable(requireActivity(), R.drawable.button_primary_radius)
    }
}
