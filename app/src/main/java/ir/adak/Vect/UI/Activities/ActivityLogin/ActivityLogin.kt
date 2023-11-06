package ir.adak.Vect.UI.Activities.ActivityLogin

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import ir.adak.Vect.App
import ir.adak.Vect.BuildConfig
import ir.adak.Vect.Data.Models.ConfirmSmsRequestModel
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.BaseActivity
import ir.adak.Vect.Utils.Constants
import ir.adak.Vect.Utils.enqueue
import kotlinx.android.synthetic.main.activity_login.*
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.os.Build.VERSION_CODES.M
import android.os.Build.VERSION.SDK_INT
import android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
import android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
import android.os.Build.VERSION_CODES.LOLLIPOP
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import android.net.Uri
import android.provider.Settings
import android.widget.Toast

import ir.adak.Vect.UI.Activities.PasswordActivty


class ActivityLogin : BaseActivity() {
    val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 125


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (SDK_INT >= LOLLIPOP) {
            val window = window
            window.addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.WHITE
        }
        val decor = window.decorView
        if (SDK_INT >= M) {
            decor.systemUiVisibility = SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setContentView(R.layout.activity_login)
        App.sharedPreferences?.edit()?.putBoolean(Constants.Flag_Admin,false)?.apply()
        App.sharedPreferences?.edit()?.putString(Constants.Admin_TWO,"")?.apply()
        var s=App.sharedPreferences?.getString("Number","A")
        var s2=App.sharedPreferences?.getString("Melicode2","A")
        var s3=App.sharedPreferences?.getString("Perseneli2","A")
        if (s.equals("A")||s2.equals("A")||s3.equals("A"))
        {

        }else{
            edt_melli_code.setText(s2)
            edt_phone_number.setText(s)
            edt_personel_number.setText(s3)
        }
        btn_login.setOnClickListener {
            if(edt_melli_code.text.isEmpty()){
                Toast.makeText(this , "لطفا کد ملی را وارد نمایید" , Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(edt_personel_number.text.isEmpty()){
                Toast.makeText(this , "لطفا کد پرسنلی را وارد نمایید" , Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(edt_phone_number.text.isEmpty()){
                Toast.makeText(this , "لطفا شماره تلفن را وارد نمایید" , Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            Dexter.withActivity(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WAKE_LOCK
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report?.areAllPermissionsGranted()!!) {
                        checkPermission()
                    } else {
                        if (report.isAnyPermissionPermanentlyDenied) {

                        }
                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    permissionToken: PermissionToken?
                ) {

                }
            }).check()
        }
//        loginWith1()
//        loginWith4()
//        loginWith7()
//        loginWith10()
//        loginWith11()
    }


    fun loginWith1() {
        edt_melli_code.setText("1")
        edt_phone_number.setText("1")
        edt_personel_number.setText("1")
        btn_login.performClick()
    }
    fun loginWith4() {
        edt_melli_code.setText("4")
        edt_phone_number.setText("4")
        edt_personel_number.setText("4")
        btn_login.performClick()
    }

    fun loginWith7() {
        edt_melli_code.setText("7")
        edt_phone_number.setText("7")
        edt_personel_number.setText("7")
        btn_login.performClick()
    }

    fun loginWith10() {
        edt_melli_code.setText("10")
        edt_phone_number.setText("10")
        edt_personel_number.setText("10")
        btn_login.performClick()
    }

    fun loginWith11() {
        edt_melli_code.setText("11")
        edt_phone_number.setText("11")
        edt_personel_number.setText("11")
        btn_login.performClick()
    }

    fun checkPermission()

    {
        if (SDK_INT >= M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")
                )
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
            } else {
                login()
            }
        } else {
            login()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (SDK_INT >= M) {
                if (!Settings.canDrawOverlays(this)) {
                    // You don't have permission
                    checkPermission()
                } else {
                    // Do as per your logic
                    login()
                }
            } else {
                login()
            }

        }
    }



    fun login() {
        val f = File(APP_FOLDER)
        if (!f.exists()) {
            f.mkdirs()
        }
        if (isNetConnected()) {
            val req = App.getRetofitApi()?.confirmSms(
                ConfirmSmsRequestModel(
                    edt_melli_code.text.toString(),
                    edt_personel_number.text.toString(),
                    edt_phone_number.text.toString(),
                    1,
                    "",
                    "",
                    BuildConfig.VERSION_CODE
                )
            )
            showpDialog(this@ActivityLogin)
            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {
                        val myResponse = it.body()
                        if (myResponse?.data?.status == 1) {
                            val req = App.getRetofitApi()?.confirmSms(
                                ConfirmSmsRequestModel(
                                    edt_melli_code.text.toString(),
                                    edt_personel_number.text.toString(),
                                    edt_phone_number.text.toString(),
                                    1,
                                    "",
                                    "",
                                    BuildConfig.VERSION_CODE
                                )
                            )
                            Melicode= edt_melli_code.text.toString();
                            Perseneli=edt_personel_number.text.toString()
                            Number=edt_phone_number.text.toString()
                             var i=Intent(this@ActivityLogin,PasswordActivty::class.java)
                            i.putExtra("Melicode",Melicode)
                            i.putExtra("Perseneli",Perseneli)
                            i.putExtra("Number",Number)
//                            startActivity(Intent(this@ActivityLogin,PasswordActivty::class.java))
                            startActivity(i)
                            finish()
//                            req?.enqueue {
//                                onResponse = { response ->
//                                    if (isResponseValid(response) == 2) {
//                                        val myResponse = response.body()
//                                        if (myResponse?.data?.status == 2) {
//                                            App.sharedPreferences?.edit()
//                                                ?.putString(
//                                                    Constants.SECURITY_KEY,
//                                                    myResponse.data?.securityKey
//                                                )?.apply()
//
//
//                                            val req = App.getRetofitApi()?.login(
//                                                LoginRequestModel(
//                                                    myResponse.data?.securityKey,
//                                                    1, BuildConfig.VERSION_CODE, ""
//                                                )
//                                            )
//
//
//
//                                            req?.enqueue {
//                                                onResponse = { loginResonse ->
//                                                    if (isResponseValid(loginResonse) == 2) {
//                                                        val myResponse = loginResonse.body()
//                                                        userInfo = UserInfo(
//                                                            myResponse?.data?.age,
//                                                            myResponse?.data?.gender,
//                                                            myResponse?.data?.fullName,
//                                                            myResponse?.data?.birthDayFa,
//                                                            myResponse?.data?.profileImage,
//                                                            myResponse?.data?.orgLevelId,
//                                                            myResponse?.data?.orgLevelTitle
//                                                        )
//                                                        App.sharedPreferences?.edit()
//                                                            ?.putString(
//                                                                Constants.USER_INFO,
//                                                                Gson().toJson(userInfo)
//                                                            )?.apply()
//                                                        token =
//                                                            "Bearer ${myResponse?.data?.token}"
//                                                        App.sharedPreferences?.edit()
//                                                            ?.putString(
//                                                                Constants.TOKEN, token
//                                                            )?.apply()
//                                                        securityKey = myResponse?.data?.securityKey
//                                                        App.sharedPreferences?.edit()
//                                                            ?.putString(
//                                                                Constants.SECURITY_KEY, securityKey
//                                                            )?.apply()
//                                                        App.sharedPreferences?.edit()
//                                                            ?.putInt(
//                                                                Constants.ORG_LEVEL_ID,
//                                                                userInfo?.orgLevelId ?: 0
//                                                            )?.apply()
//                                                        startActivity(
//                                                            Intent(
//                                                                this@ActivityLogin,
//                                                                NewMainActivity::class.java
//                                                            )
//                                                        )
//                                                        overridePendingTransition(
//                                                            R.anim.slide_left_in,
//                                                            R.anim.slide_left_out
//                                                        )
//                                                        finish()
//                                                    }
//
//                                                    hidepDialog()
//                                                }
//                                                onFailure = {
//                                                    hidepDialog()
//                                                }
//                                            }
//                                        } else {
//                                            Toast.makeText(
//                                                this@ActivityLogin,
//                                                "هنگام ارسال کد اعتبارسنجی خطایی رخ داده است",
//                                                Toast.LENGTH_LONG
//                                            ).show()
//                                            hidepDialog()
//                                        }
//                                    } else {
//                                        hidepDialog()
//                                    }
//                                }
//
//                                onFailure = {
//                                    hidepDialog()
//                                }
//                            }
                        } else {
                            Toast.makeText(
                                this@ActivityLogin,
                                "کاربری با این مشخصات یافت نشد",
                                Toast.LENGTH_LONG
                            ).show()
                            hidepDialog()
                        }
                    } else {
                        hidepDialog()
                    }
                }
                onFailure = {
                    hidepDialog()
                }
            }
        }
    }
}
