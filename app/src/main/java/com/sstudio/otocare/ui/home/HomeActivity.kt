package com.sstudio.otocare.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sstudio.otocare.Model.User
import com.sstudio.otocare.R
import com.sstudio.otocare.common.Common
import com.sstudio.otocare.ui.login.LoginActivity
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_update_information.view.*


class HomeActivity : AppCompatActivity() {

    lateinit var userRef: CollectionReference
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var dialog: AlertDialog
    private var isLogin = false

    companion object {
        var currentUser: User? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        userRef = FirebaseFirestore.getInstance().collection("User")
        dialog = SpotsDialog.Builder().setContext(this).setCancelable(false).build()

        dialog.show()
        checkCurrentUser()

        val navController = supportFragmentManager
            .findFragmentById(R.id.fragment_nav_host) as NavHostFragment
        NavigationUI.setupWithNavController(
            bottom_nav,
            navController.navController
        )
    }

    private fun checkCurrentUser() {
        val user = FirebaseAuth.getInstance().currentUser
        val userPhone = user?.phoneNumber
        if (user != null) {
            isLogin = true
            val currentUserRef = userRef.document(userPhone.toString())
            currentUserRef.get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val userSnapShot = it.result
                        if (userSnapShot?.exists() == false) {
                            userPhone?.let { it1 -> showUpdateDialog(it1) }
                        } else {
                            userSnapShot?.let { documentSnapShot ->
                                currentUser = User(
                                    name = documentSnapShot[User().name].toString(),
                                    address = documentSnapShot[User().address].toString(),
                                    phoneNumber = documentSnapShot[User().phoneNumber].toString()
                                )
                            }
                        }
                    }
                }
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        dialog.dismiss()
    }

    private fun showUpdateDialog(phoneNumber: String) {

        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setTitle("Satu langkah lagi!")
        bottomSheetDialog.setCanceledOnTouchOutside(false)
        bottomSheetDialog.setCancelable(false)
        val sheetView = layoutInflater.inflate(R.layout.layout_update_information, null)

        sheetView.btn_update.setOnClickListener {
            if (!dialog.isShowing)
                dialog.show()
            val user = User(
                sheetView.et_nama.text.toString(),
                sheetView.et_alamat.text.toString(),
                phoneNumber
            )
            userRef.document(phoneNumber)
                .set(user)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                    if (dialog.isShowing)
                        dialog.dismiss()

                    Common.currentUser = user
                }.addOnFailureListener { e ->
                    bottomSheetDialog.dismiss()
                    if (dialog.isShowing)
                        dialog.dismiss()
                    Toast.makeText(this, "updateDialog" + e.message, Toast.LENGTH_SHORT).show()
                }

        }
        bottomSheetDialog.setContentView(sheetView)
        bottomSheetDialog.show()
    }
}




















