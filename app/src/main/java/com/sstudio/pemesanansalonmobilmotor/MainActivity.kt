package com.sstudio.pemesanansalonmobilmotor

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.Signature
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.facebook.accountkit.AccountKit
import com.facebook.accountkit.AccountKitLoginResult
import com.facebook.accountkit.ui.AccountKitActivity
import com.facebook.accountkit.ui.AccountKitConfiguration
import com.facebook.accountkit.ui.LoginType
import com.sstudio.pemesanansalonmobilmotor.common.Common
import kotlinx.android.synthetic.main.activity_main.*
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {

    private val APP_REQUEST_CODE: Int = 9991

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val accessToken = AccountKit.getCurrentAccessToken()
        if (accessToken != null){
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra(Common.IS_LOGIN, true)
            startActivity(intent)
            finish()
        }else{
            setContentView(R.layout.activity_main)
            btn_login.setOnClickListener {
                val intent = Intent(this, AccountKitActivity::class.java)
                val configurationBuilder =
                    AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE, AccountKitActivity.ResponseType.TOKEN)
                intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                    configurationBuilder.build())
                startActivityForResult(intent, APP_REQUEST_CODE)
            }
            tv_lewati.setOnClickListener {
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra(Common.IS_LOGIN, false)
                startActivity(intent)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == APP_REQUEST_CODE){
            val loginResult: AccountKitLoginResult = data!!.getParcelableExtra(AccountKitLoginResult.RESULT_KEY)
            if (loginResult.error != null)
                Toast.makeText(this, ""+ loginResult.error!!.errorType.message, Toast.LENGTH_SHORT).show()
            else if (loginResult.wasCancelled())
                Toast.makeText(this, "Login cancellet", Toast.LENGTH_SHORT).show()
            else{

                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra(Common.IS_LOGIN, true)
                startActivity(intent)
                finish()
            }


        }
    }
    fun key(){
        try {
            var packageInfo = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature: Signature in packageInfo.signatures){
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KEYHASH", Base64.encodeToString(md.digest(),Base64.DEFAULT))
            }
        } catch (e : PackageManager.NameNotFoundException){

        }
    }
}
