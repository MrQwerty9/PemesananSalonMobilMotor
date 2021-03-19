package com.sstudio.otocare.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sstudio.otocare.R
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.view_pager_login, FormLoginFragment())
            .commit()
    }




}
