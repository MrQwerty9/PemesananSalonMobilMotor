package com.sstudio.otocare.ui.booking

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
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
import com.sstudio.otocare.utils.PermissionManager
import dmax.dialog.SpotsDialog
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

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
            var eventId = 0
            if (PermissionManager.check(requireActivity(), Manifest.permission.READ_CALENDAR) &&
                PermissionManager.check(requireActivity(), Manifest.permission.WRITE_CALENDAR)
            ) {
                eventId = setEventCalendar()
            }
            setBooking(eventId)
        }

    }

    private fun setBooking(eventId: Int) {
        bookingBundle.eventId = eventId
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

    private fun setEventCalendar(): Int {

        val calID: Long = 99
        val startMillis: Long = Calendar.getInstance().run {
            set(2021, 3, 2, 3, 0)
            timeInMillis
        }
        val endMillis: Long = Calendar.getInstance().run {
            set(2021, 3, 2, 4, 45)
            timeInMillis
        }

        Log.d("mytag", "$startMillis $endMillis")

        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, startMillis)
            put(CalendarContract.Events.DTEND, endMillis)
            put(CalendarContract.Events.TITLE, "Jazzercise")
            put(CalendarContract.Events.DESCRIPTION, "Group workout")
            put(CalendarContract.Events.CALENDAR_ID, getCalendarId())
            put(CalendarContract.Events.ALL_DAY, 0)
            put(CalendarContract.Events.HAS_ALARM, 1)
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        }
        val uri: Uri? =
            requireActivity().contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)

        // get the event ID that is the last element in the Uri
        return uri?.lastPathSegment?.toInt() ?: 0
    }

    private fun getCalendarId(): String {
        var gmailIdCalendar = ""
        var projection = arrayOf("_id", "calendar_displayName")
        var calendars = CalendarContract.Events.CONTENT_URI
        var contentResolver = requireContext().contentResolver
        var managedCursor = contentResolver.query(calendars, projection, null, null, null)
        if (managedCursor?.moveToFirst() == true) {
            var calName = ""
            val nameCol = managedCursor.getColumnIndex(projection[1])
            val idCol = managedCursor.getColumnIndex(projection[0])
            do {
                calName = managedCursor.getString(nameCol)
                if (calName.contains("@gmail.com")) {
                    gmailIdCalendar = managedCursor.getString(idCol)
                    break
                }
            } while (managedCursor.moveToNext())
            managedCursor.close()
        }
        return gmailIdCalendar
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
