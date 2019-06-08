package com.sstudio.pemesanansalonmobilmotor

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.facebook.accountkit.Account
import com.facebook.accountkit.AccountKit
import com.facebook.accountkit.AccountKitCallback
import com.facebook.accountkit.AccountKitError
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sstudio.pemesanansalonmobilmotor.common.Common
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.sstudio.pemesanansalonmobilmotor.Fragments.HomeFragment
import com.sstudio.pemesanansalonmobilmotor.Fragments.ShoppingFragment
import com.sstudio.pemesanansalonmobilmotor.Model.User
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_update_information.view.*

class HomeActivity : AppCompatActivity() {

    lateinit var userRef: CollectionReference
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var dialog: AlertDialog
    val homeFragment = HomeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        userRef = FirebaseFirestore.getInstance().collection("User")
        dialog = SpotsDialog.Builder().setContext(this).setCancelable(false).build()

        if (intent != null){
            val isLogin: Boolean = intent.getBooleanExtra(Common.IS_LOGIN, false)
            if (isLogin){
                dialog.show()
                //cek jika terdapat user
                AccountKit.getCurrentAccount(object: AccountKitCallback<Account> {
                    override fun onSuccess(account: Account?) {
                        if (account != null){
                            val currentUser: DocumentReference= userRef.document(account.phoneNumber.toString())
                            currentUser.get()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful){
                                        val userSnapShot: DocumentSnapshot = task.result!!
                                        if (!userSnapShot.exists()){
                                            showUpdateDialog(account.phoneNumber.toString())
                                        }
                                        else{
                                            Common.currentUser = userSnapShot.toObject(User::class.java)
//                                            bn_home.menu.findItem(R.id.action_home).isChecked = true
//                                            bn_home.selectedItemId = R.id.action_home

                                        }
                                        if (dialog.isShowing)
                                            dialog.dismiss()

                                    }
                                }
                        }
                    }

                    override fun onError(accountKitError: AccountKitError?) {
                        Toast.makeText(this@HomeActivity, "isLogin"+ accountKitError!!.errorType.message.toString(), Toast.LENGTH_SHORT).show()
                    }


                })

            }
        }
        //view
        
        bn_home.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener {
            var fragment: Fragment? = null

            override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.action_home)
                    fragment = HomeFragment()
                else if (menuItem.itemId == R.id.action_shopping)
                    fragment = ShoppingFragment()
                return loadFragment(fragment)
            }
        })

    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null){
            supportFragmentManager.beginTransaction().replace(R.id.fl_home, fragment).commit()
            return true
        }
        return false
    }


    private fun showUpdateDialog(nomorHp: String) {

        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setTitle("Satu langkah lagi!")
        bottomSheetDialog.setCanceledOnTouchOutside(false)
        bottomSheetDialog.setCancelable(false)
        val sheetView = layoutInflater.inflate(R.layout.layout_update_information, null)

        sheetView.btn_update.setOnClickListener {
            if (!dialog.isShowing)
                dialog.show()
            val user = User(sheetView.et_nama.text.toString(), sheetView.et_alamat.text.toString(), nomorHp)
            userRef.document(nomorHp)
                .set(user)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                    if (dialog.isShowing)
                        dialog.dismiss()

                    Common.currentUser = user
                    bn_home.selectedItemId = R.id.action_home

                    Toast.makeText(this, "Terima kasih", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {e ->
                    bottomSheetDialog.dismiss()
                    if (dialog.isShowing)
                        dialog.dismiss()
                    Toast.makeText(this, "updateDialog"+ e.message, Toast.LENGTH_SHORT).show()
                }

        }
        bottomSheetDialog.setContentView(sheetView)
        bottomSheetDialog.show()
    }
}




















