package com.sstudio.otocare.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sstudio.otocare.R
import kotlinx.android.synthetic.main.fragment_form_login.*

class FormLoginFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_form_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        btn_login.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v){
            btn_login -> {
                val arguments = Bundle()
                val phoneNumber = et_phone_number.text.toString()
                val countryCode = code_picker.selectedCountryCodeWithPlus
                arguments.putString(VerificationFragment.EXTRA_PHONE_NUMBER, "$countryCode$phoneNumber")
                val fragment = VerificationFragment()
                fragment.arguments = arguments
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.view_pager_login, fragment)
                    .commit()
            }
        }
    }

}