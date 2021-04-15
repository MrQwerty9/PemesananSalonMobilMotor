package com.sstudio.otocare.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.sstudio.otocare.MainActivity
import com.sstudio.otocare.R
import com.sstudio.otocare.databinding.FragmentFormLoginBinding

class FormLoginFragment : Fragment(), View.OnClickListener {

    private var phoneNumber = ""
    private var _binding: FragmentFormLoginBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val SAVED_INSTANCE_PHONE_NUMBER = "saved_phone_number"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFormLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkCurrentUser()

        if (savedInstanceState != null) {
            phoneNumber = savedInstanceState.getString(SAVED_INSTANCE_PHONE_NUMBER) ?: ""
            binding.etPhoneNumber.setText(phoneNumber)
        }

        binding.btnLogin.setOnClickListener(this)
        binding.btnSkipLogin.setOnClickListener {
            showHomeActivity()
        }

    }

    private fun checkCurrentUser() {
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            // Name, email address
            val email = user.phoneNumber
            Log.d("mytag", " $email")
            showHomeActivity()
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnLogin -> {
                val arguments = Bundle()
                val phoneNumber = binding.etPhoneNumber.text.toString()
                val countryCode = binding.codePicker.selectedCountryCodeWithPlus
                arguments.putString(
                    VerificationFragment.EXTRA_PHONE_NUMBER,
                    "$countryCode$phoneNumber"
                )
                val fragment = VerificationFragment()
                fragment.arguments = arguments
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.view_pager_login, fragment)
                    .commit()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_INSTANCE_PHONE_NUMBER, phoneNumber)
    }

    private fun showHomeActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}