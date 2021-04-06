package com.sstudio.otocare.ui.login

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.sstudio.otocare.R
import com.sstudio.otocare.databinding.FragmentVerificationBinding
import com.sstudio.otocare.ui.home.HomeActivity
import java.util.concurrent.TimeUnit


class VerificationFragment : Fragment(), View.OnClickListener {

    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var phoneNumber: String? = null
    private var mVerificationId: String? = null
    private var mResendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var progressDialog: ProgressDialog? = null
    private var isTimerActive = false
    private var mCounterDown: CountDownTimer? = null
    private var timeLeft: Long = -1
    private var _binding: FragmentVerificationBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val EXTRA_PHONE_NUMBER = "extra_phone_number"
        const val SAVED_INSTANCE_PHONE_NUMBER = "saved_phone_number"
        const val SAVED_INSTANCE_TIME_LEFT = "saved_time_left"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(savedInstanceState==null) {
            initView()
            startVerfiy()
        }
        else {
            phoneNumber = savedInstanceState.getString(SAVED_INSTANCE_PHONE_NUMBER)
            initView()
            startPhoneNumberVerification(phoneNumber.toString())


            timeLeft = savedInstanceState.getLong(SAVED_INSTANCE_TIME_LEFT)
            showTimer(timeLeft)
        }
    }

    override fun onClick(v: View?) {
        when (v){
            binding.btnVerification -> {
                // try to enter the code by yourself to handle the case
                // if user enter another sim card used in another phone ...
                val code = binding.etSentCode.text
                Log.e("code is --- ", code.toString())
                if (code != null && code.isNotEmpty() && mVerificationId != null && mVerificationId!!.isNotEmpty()) {

                    showProgressDialog(requireContext(), "please wait...", false)

                    val credential = PhoneAuthProvider.getCredential(
                        mVerificationId!!,
                        code.toString()
                    )
                    signInWithPhoneAuthCredential(credential)
                }
            }

            binding.btnResend -> {
                if (mResendToken != null && !isTimerActive) {
                    resendVerificationCode(phoneNumber.toString(), mResendToken)
                    showTimer(60000)
                    showProgressDialog(
                        requireContext(),
                        getString(R.string.sending_code_verif),
                        false
                    )
                } else {
                    toast(getString(R.string.please_wait_resend_code))
                }

            }

            binding.tvWrongPhone -> {
                showLoginActivity()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(SAVED_INSTANCE_TIME_LEFT, timeLeft)
        outState.putString(SAVED_INSTANCE_PHONE_NUMBER, phoneNumber)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mCounterDown != null) {
            mCounterDown!!.cancel()
        }
    }

    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            requireActivity(), // Activity (for callback binding)
            callbacks, // OnVerificationStateChangedCallbacks
            token
        ) // ForceResendingToken from callbacks
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        val mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    if (progressDialog != null) {
                        dismissProgressDialog(progressDialog)
                    }

                    val user = task.result?.user
                    Log.e("Sign in with phone auth", "Success $user")
                    showHomeActivity()
                } else {

                    if (progressDialog != null) {
                        dismissProgressDialog(progressDialog)
                    }
                    toast(getString(R.string.wrong_code_verif))
                }
            }
    }

    private fun showProgressDialog(mActivity: Context, message: String, isCancelable: Boolean): ProgressDialog {
        progressDialog = ProgressDialog(mActivity)
        progressDialog?.show()
        progressDialog?.setCancelable(isCancelable)
        progressDialog?.setCanceledOnTouchOutside(false)
        progressDialog?.setMessage(message)
        return progressDialog as ProgressDialog
    }

    private fun dismissProgressDialog(progressDialog: ProgressDialog?) {
        if (progressDialog != null && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    private fun showTimer(milliesInFuture: Long){
        isTimerActive = true
        mCounterDown = object : CountDownTimer(milliesInFuture, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                binding.tvCounterDown.visibility = View.VISIBLE
                binding.tvCounterDown.text = "sisa waktu: " + millisUntilFinished / 1000

                //here you can have your logic to set text to edittext
            }

            override fun onFinish() {
                binding.tvCounterDown.visibility = View.GONE
                isTimerActive = false
            }

        }.start()
    }

    private fun startVerfiy(){
        Log.e("User Phone Number ===  ", "$phoneNumber")

        if (phoneNumber != null && phoneNumber!!.isNotEmpty()) {
            startPhoneNumberVerification(phoneNumber!!)
            showTimer(60000)
            showProgressDialog(requireContext(), "Sending a verification code", false)
        } else {
            toast("Please enter a valid number to continue!")
        }
    }

    private fun initView() {
        // init vars from bundle
        val arguments = arguments
        phoneNumber = arguments?.getString(EXTRA_PHONE_NUMBER)
        binding.tvVerify.text = "Verfiy $phoneNumber"
        binding.tvPhoneNumber.text = phoneNumber

        // init click listener
        binding.btnVerification.setOnClickListener(this)
        binding.btnResend.setOnClickListener(this)
        binding.tvWrongPhone.setOnClickListener(this)

        // init fire base verfiyPhone number callback
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onCodeAutoRetrievalTimeOut(verificationId: String) {
                if (progressDialog != null) {
                    dismissProgressDialog(progressDialog)
                }
//                notifyUserAndRetry("Time Out :( failed.Retry again!")
            }

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.e("onVerificationCompleted", "onVerificationCompleted:$credential")
                if (progressDialog != null) {
                    dismissProgressDialog(progressDialog)
                }

                val smsMessageSent: String = credential.smsCode.toString()
                Log.e("the message is ----- ", smsMessageSent)

                binding.etSentCode.setText(smsMessageSent)

                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.e("+++2", "onVerificationFailed", e)

                if (progressDialog != null) {
                    dismissProgressDialog(progressDialog)
                }

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Log.e("Exception:", "FirebaseAuthInvalidCredentialsException", e)
                    Log.e("=========:", "FirebaseAuthInvalidCredentialsException " + e.message)


                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Log.e("Exception:", "FirebaseTooManyRequestsException", e)
                }

                // Show a message and update the UI
                notifyUserAndRetry("Your Phone Number might be wrong or connection error.Retry again!")

            }

            override fun onCodeSent(
                verificationId: String?,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                //for low level version which doesn't do auto verification save the verification code and the token


                dismissProgressDialog(progressDialog)
                binding.tvCounterDown.visibility = View.GONE
                // Save verification ID and resending token so we can use them later
                Log.e("onCodeSent===", "onCodeSent:$verificationId")

                mVerificationId = verificationId
                mResendToken = token

            }
        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String){
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun notifyUserAndRetry(message: String) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setPositiveButton(
            "Ok"
        ) { _, _ -> showLoginActivity() }

        alertDialogBuilder.setNegativeButton(
            "Cancel"
        ) { _, _ -> showLoginActivity() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showLoginActivity() {
//        startActivity(
//            Intent(requireContext(), LoginActivity::class.java)
//                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//        )

        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.view_pager_login, FormLoginFragment())
            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            .commit()
    }

    private fun showHomeActivity() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}