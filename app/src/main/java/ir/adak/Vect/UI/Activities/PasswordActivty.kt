package ir.adak.Vect.UI.Activities

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import ir.adak.Vect.App
import ir.adak.Vect.BuildConfig
import ir.adak.Vect.Data.Models.ConfirmSmsRequestModel
import ir.adak.Vect.Data.Models.LoginRequestModel
import ir.adak.Vect.Data.Models.UserInfo
import ir.adak.Vect.MainActivity_New
import ir.adak.Vect.R
import ir.adak.Vect.Utils.Constants
import ir.adak.Vect.Utils.enqueue
import kotlinx.android.synthetic.main.activity_password.*
import java.util.concurrent.TimeUnit

class PasswordActivty : BaseActivity() {
    var Melicode2:String ? =""
    var Perseneli2:String ? =""
    var Number2:String ? =""
    var Message=""
    var timer:CountDownTimer ?= null
    var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action.equals("SmsJBase"))
            {
                Log.i("sdmkvskvs","ASD")
                Log.i("sdvnsnkvksdvnk",intent.getStringExtra("Data1"))
                 editTextTextPersonName.setText(intent.getStringExtra("Data1"))
                 editTextTextPersonName2.setText(intent.getStringExtra("Data2"))
                 editTextTextPersonName3.setText(intent.getStringExtra("Data3"))
                 editTextTextPersonName4.setText(intent.getStringExtra("Data4"))
                Message=intent.getStringExtra("Data5")
                Run()
            }else{
                Log.i("sdmkvskvs","FV")
                Log.i("dsss", intent.action)
            }

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)
        registerReceiver(broadcastReceiver, IntentFilter("SmsJBase"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.parseColor("#3358f5")
        }

        val decor = window.decorView

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        Melicode2=intent.getStringExtra("Melicode")
        Perseneli2=intent.getStringExtra("Perseneli")
        Number2=intent.getStringExtra("Number")
        textView11.setText("کد پیامک شده را به شماره $Number2  ارسال کنید ")
        TimeStart()
        textView13.setOnClickListener {
            if (isNetConnected())
            {
                confirmSMS_2()
            }else{
                Snackbar.make(
                    Conteaner,
                    "مشکلی در اتصال به اینترنت به وجود آمده",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

        }
        button.setOnClickListener {
            if (editTextTextPersonName.text.trim().toString().isNotBlank()
                &&editTextTextPersonName2.text.trim().toString().isNotBlank()
                &&editTextTextPersonName3.text.trim().toString().isNotBlank()
                &&editTextTextPersonName4.text.trim().toString().isNotBlank())
            {
                Run()
            }else{
                Snackbar.make(Conteaner, "کد عبور را به صورت کامل وارد کنید", Snackbar.LENGTH_SHORT).show()

            }
        }
        TextWTACHERS()
        TextWTACHERS_Ontoch()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun TextWTACHERS_Ontoch() {
        editTextTextPersonName.setOnTouchListener(View.OnTouchListener { v, event ->
            editTextTextPersonName.isCursorVisible = false
//            editTextTextPersonName.setFocusableInTouchMode(true)
            false
        })
        editTextTextPersonName2.setOnTouchListener(View.OnTouchListener { v, event ->
            editTextTextPersonName2.isCursorVisible = false
//            editTextTextPersonName.setFocusableInTouchMode(true)
            false
        })
        editTextTextPersonName3.setOnTouchListener(View.OnTouchListener { v, event ->
            editTextTextPersonName3.isCursorVisible = false
//            editTextTextPersonName.setFocusableInTouchMode(true)
            false
        })
        editTextTextPersonName4.setOnTouchListener(View.OnTouchListener { v, event ->
            editTextTextPersonName4.isCursorVisible = false
//            editTextTextPersonName.setFocusableInTouchMode(true)
            false
        })

    }
    private fun TextWTACHERS() {
        editTextTextPersonName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                if (editTextTextPersonName.text.trim())
                if (p0.toString().trim().isEmpty()) {
                    editTextTextPersonName.setBackgroundResource(R.drawable.ic_box1)
                } else {
                    editTextTextPersonName.setBackgroundResource(R.drawable.ic_box_2)
                    editTextTextPersonName2.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        editTextTextPersonName2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isEmpty()) {
                    editTextTextPersonName2.setBackgroundResource(R.drawable.ic_box1)
                    editTextTextPersonName.requestFocus()
                } else {
                    editTextTextPersonName2.setBackgroundResource(R.drawable.ic_box_2)
                    editTextTextPersonName3.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        editTextTextPersonName3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (editTextTextPersonName3.text.trim().isEmpty()) {
                    editTextTextPersonName3.setBackgroundResource(R.drawable.ic_box1)
                    editTextTextPersonName2.requestFocus()
                } else {
                    editTextTextPersonName3.setBackgroundResource(R.drawable.ic_box_2)
                    editTextTextPersonName4.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        editTextTextPersonName4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isEmpty()) {
                    editTextTextPersonName4.setBackgroundResource(R.drawable.ic_box1)
                    editTextTextPersonName3.requestFocus()
                } else {
                    editTextTextPersonName4.setBackgroundResource(R.drawable.ic_box_2)

                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }
    private fun confirmSMS_2() {
        showpDialog(this)
        val req = App.getRetofitApi()?.confirmSms(
            ConfirmSmsRequestModel(
                Melicode2!!,
                Perseneli2!!,
                Number2!!,
                1,
                "",
                "",
                BuildConfig.VERSION_CODE
            )
        )
        req?.enqueue {
            onResponse={
                hidepDialog()
                textView13.setEnabled(false)
                textView13.setTextColor(Color.parseColor("#878787"))
                TimeStart()
            }
            onFailure={
                hidepDialog()
            }
        }
    }
    private fun TimeStart() {

        timer=object :CountDownTimer(120000, 1000)
        {
            @SuppressLint("SetTextI18n")
            override fun onTick(p0: Long) {
                textView12.setText(
                    "" + String.format(
                        "%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes(p0),
                        TimeUnit.MILLISECONDS.toSeconds(p0) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(p0))
                    )
                )
            }
            override fun onFinish() {
                textView13.setTextColor(Color.parseColor("#3358f5"))
                textView13.setEnabled(true)
            }

        }
        timer?.start()
    }

    fun  Run()
    {
        Message=editTextTextPersonName.text.toString().trim()+
                editTextTextPersonName2.text.toString().trim()+
                editTextTextPersonName3.text.toString().trim()+
                editTextTextPersonName4.text.toString().trim();
        if (isNetConnected())
        {
            showpDialog(this)
//            val req = App.getRetofitApi()?.confirmSms(
//                Melicode?.let {
//                    Perseneli?.let { it1 ->
//                        Number?.let { it2 ->
//                            ConfirmSmsRequestModel(
//                                it,
//                                it1,
//                                it2,
//                                1,
//                                "",
//                                txt_pin_entry.text.toString(),
//                                BuildConfig.VERSION_CODE
//                            )
//                        }
//                    }
//                }
//            )
            val req = App.getRetofitApi()?.confirmSms(
                ConfirmSmsRequestModel(
                    Melicode2!!,
                    Perseneli2!!,
                    Number2!!,
                    1,
                    "",
                    Message,
                    BuildConfig.VERSION_CODE
                )

            )
            req?.enqueue {
                onResponse={
                    hidepDialog()
                    if(isResponseValid(it)==2)
                    {
                        val res=it.body()
                        if (res?.data?.status == 2)
                        {
                            App.sharedPreferences?.edit()
                                                ?.putString(
                                                    Constants.SECURITY_KEY,
                                                    res.data?.securityKey
                                                )?.apply()
                            var req2 = App.getRetofitApi()?.login(
                                LoginRequestModel(
                                    res.data?.securityKey,
                                    1, BuildConfig.VERSION_CODE, ""
                                )
                            )
                            req2?.enqueue {
                                onResponse={
                                    if (isResponseValid(it) == 2)
                                    {
                                        val myResponse = it.body()
                                                        userInfo = UserInfo(
                                                            myResponse?.data?.age,
                                                            myResponse?.data?.gender,
                                                            myResponse?.data?.fullName,
                                                            myResponse?.data?.birthDayFa,
                                                            myResponse?.data?.profileImage,
                                                            myResponse?.data?.orgLevelId,
                                                            myResponse?.data?.orgLevelTitle
                                                        )
                                        projectTypes=myResponse?.data?.projectTypes
                                        App.sharedPreferences?.edit()?.putString(Constants.TOKEN_Copy,myResponse?.data?.token)?.apply()
                                         if (myResponse?.data?.adminAccountInfo!=null)
                                         {
                                             App.sharedPreferences?.edit()
                                                 ?.putString(
                                                     Constants.Admin_TWO,
                                                     Gson().toJson(myResponse.data?.adminAccountInfo)
                                                 )?.apply()
                                         }
                                        App.sharedPreferences?.edit()
                                            ?.putString(
                                                Constants.USER_INFO_Copy,
                                                Gson().toJson(userInfo)
                                            )?.apply()
                                        App.sharedPreferences?.edit()?.putString("Number", Number2)?.apply()
                                        App.sharedPreferences?.edit()?.putString(
                                            "Melicode2",
                                            Melicode2
                                        )?.apply()
                                        App.sharedPreferences?.edit()?.putString(
                                            "Perseneli2",
                                            Perseneli2
                                        )?.apply()
                                        App.sharedPreferences?.edit()
                                                            ?.putString(
                                                                Constants.USER_INFO,
                                                                Gson().toJson(userInfo)
                                                            )?.apply()
                                                        token =
                                                            "Bearer ${myResponse?.data?.token}"
                                                        App.sharedPreferences?.edit()
                                                            ?.putString(
                                                                Constants.TOKEN, token
                                                            )?.apply()
                                                        securityKey = myResponse?.data?.securityKey
                                                        App.sharedPreferences?.edit()
                                                            ?.putString(
                                                                Constants.SECURITY_KEY, securityKey
                                                            )?.apply()
                                                        App.sharedPreferences?.edit()
                                                            ?.putInt(
                                                                Constants.ORG_LEVEL_ID,
                                                                userInfo?.orgLevelId ?: 0
                                                            )?.apply()
                                        startActivity(
                                            Intent(
                                                this@PasswordActivty,
                                                MainActivity_New::class.java
                                            )
                                        )
                                                        overridePendingTransition(
                                                            R.anim.slide_left_in,
                                                            R.anim.slide_left_out
                                                        )
                                                        finish()

                                        hidepDialog()
                                    }else{
                                        hidepDialog()
                                    }

                                }
                                onFailure={
                                    hidepDialog()
                                }
                            }

                        }else{
                            Toast.makeText(
                                this@PasswordActivty,
                                "هنگام ارسال کد اعتبارسنجی خطایی رخ داده است",
                                Toast.LENGTH_LONG
                            ).show()
                            hidepDialog()
                        }
                    }else{
                        hidepDialog()
                    }

                }
                onFailure={
                    hidepDialog()
                }
            }

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
        }else{
            Snackbar.make(Conteaner, "بروز خطا در برقراری با سرور", Snackbar.LENGTH_SHORT).show()
        }
    }
}