package ir.adak.Vect.UI.Fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import ir.adak.Vect.App
import ir.adak.Vect.BuildConfig
import ir.adak.Vect.Data.Models.LoginRequestModel
import ir.adak.Vect.Data.Models.ReminderModel
import ir.adak.Vect.Data.Models.UserInfo
import ir.adak.Vect.Data.Models.adminAccountInfo
import ir.adak.Vect.Data.Retrofit.StandardServerResponse
import ir.adak.Vect.Listeners.OnLoginCompleted
import ir.adak.Vect.R
import ir.adak.Vect.SplashScreenActivity
import ir.adak.Vect.UI.Activities.ActivityLogin.ActivityLogin
import ir.adak.Vect.UI.Activities.BaseActivity
import ir.adak.Vect.UI.Dialogs.MyAlertDialog
import ir.adak.Vect.Utils.Constants
import ir.adak.Vect.Utils.enqueue
import org.json.JSONObject
import org.modelmapper.TypeToken
import retrofit2.Response


open class BaseFragment : Fragment() {
    lateinit var dialog: AlertDialog
    val TAG = "MyTagg"
    var token: String? = ""
    var securityKey: String? = ""
    var adminAccountInfo: adminAccountInfo?=null
    var Flag_Admin=false
    var S_1=""
    var S_2=""
    var token_copy: String? = ""
    var userInfo_Copy: UserInfo? = null
    var userInfo: UserInfo? = null
    var userInfo_2: UserInfo? = null
//    var projectTypes :ArrayList<projectTypes> ?= null
    var reminders: MutableList<ReminderModel>? = mutableListOf()

    companion object{
        var MyAct:Activity ? =null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("xcvdv",activity?.packageName)
        MyAct=activity
        initDialog()
        updateSharedReferences()

    }

    fun updateSharedReferences() {
        token = App.sharedPreferences?.getString(Constants.TOKEN, "")
        securityKey = App.sharedPreferences?.getString(Constants.SECURITY_KEY, "")
//        Flag_Admin = App.sharedPreferences?.getBoolean(Constants.Flag_Admin,false)!!
        Flag_Admin = App.sharedPreferences?.getBoolean(Constants.Flag_Admin, false)!!
        var temp = App.sharedPreferences?.getString(Constants.Admin_TWO, "")!!
        Log.i("vslmvsddvb",temp.toString())
        var vv=App.sharedPreferences?.getString(Constants.USER_INFO_Copy,"")
        var  data_2: UserInfo = Gson().fromJson(vv, ir.adak.Vect.Data.Models.UserInfo::class.java)
        userInfo_Copy =data_2
        BaseActivity.userInfo = Gson().fromJson(
            App.sharedPreferences?.getString(Constants.USER_INFO, ""),
            object : TypeToken<UserInfo>() {}.type
        )
        if (temp.equals("null"))
        {
            Log.i("dmvsmkvknsdvndjjndhttw","B")
            token=App.sharedPreferences?.getString(Constants.TOKEN_Copy,"")
            return
        }
        if (!temp.isNullOrEmpty())
        {
            Log.i("dmvsmkvknsdvndjjndhttw","A")
            var  data: adminAccountInfo = Gson().fromJson(temp, ir.adak.Vect.Data.Models.adminAccountInfo::class.java)
            Log.i("vslmvsddvb",data.toString())
            adminAccountInfo=data
            App.sharedPreferences?.edit()
                ?.putString(
                    Constants.Admin_TWO, Gson().toJson(adminAccountInfo)
                )?.apply()
            if (Flag_Admin)
            {
                Log.i("dmvsmkvknsdvndjjndhttw","C")
                Log.i("davmlsvml","A")
                if (!temp.isNullOrEmpty())
                {
                    var  data: adminAccountInfo = Gson().fromJson(temp, ir.adak.Vect.Data.Models.adminAccountInfo::class.java)
                    Log.i("vslmvsddvb",data.toString())
                    adminAccountInfo=data
                    App.sharedPreferences?.edit()
                        ?.putString(
                            Constants.Admin_TWO, Gson().toJson(adminAccountInfo)
                        )?.apply()
                }
                token="Bearer "+adminAccountInfo?.token
                BaseActivity.userInfo?.fullName=adminAccountInfo?.fullName
                BaseActivity.userInfo?.orgLevelTitle=adminAccountInfo?.orgLevelTitle
                BaseActivity.userInfo?.age=adminAccountInfo?.age
                BaseActivity.userInfo?.birthDayFa=adminAccountInfo?.birthDayFa
                BaseActivity.userInfo?.profileImage=adminAccountInfo?.profileImage
                App.sharedPreferences?.edit()
                    ?.putString(
                        Constants.USER_INFO,
                        Gson().toJson(BaseActivity.userInfo)
                    )?.apply()
                App.sharedPreferences?.edit()
                    ?.putString(
                        Constants.TOKEN,
                        token
                    )?.apply()
            }else{
                Log.i("dmvsmkvknsdvndjjndhttw","D")
                Log.i("davmlsvml","B")
                token=App.sharedPreferences?.getString(Constants.TOKEN_Copy,"")
//            userInfo= userInfo_Copy
                App.sharedPreferences?.edit()
                    ?.putString(
                        Constants.TOKEN,
                        token
                    )?.apply()
                var user=App.sharedPreferences?.getString(Constants.USER_INFO_Copy,"")
                if (!user.isNullOrEmpty())
                {
                    Log.i("dmvsmkvknsdvndjjndhttw","E")
                    var  user_2: UserInfo = Gson().fromJson(user,UserInfo::class.java)

                    userInfo?.fullName=user_2?.fullName
                   userInfo?.orgLevelTitle=user_2?.orgLevelTitle
                   userInfo?.age=user_2?.age
                   userInfo?.birthDayFa=user_2?.birthDayFa
                   userInfo?.profileImage=user_2?.profileImage
                    App.sharedPreferences?.edit()
                        ?.putString(
                            Constants.USER_INFO,
                            Gson().toJson(user_2)
                        )?.apply()
                }

            }
        }else{
            Log.i("dmvsmkvknsdvndjjndhttw","F")
            token=App.sharedPreferences?.getString(Constants.TOKEN_Copy,"")

        }
        Log.i("dsvsfdfyutuo", BaseActivity.userInfo.toString())
//        userInfo = Gson().fromJson(
//            App.sharedPreferences?.getString(Constants.USER_INFO, ""),
//            object : TypeToken<UserInfo>() {}.type
//        )
        reminders = Gson().fromJson(
            App.sharedPreferences?.getString(Constants.REMINDERS, ""),
            object : TypeToken<MutableList<ReminderModel>>() {}.type
        )
    }

    fun isNetConnected(): Boolean {
        val cn = MyAct?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nf: NetworkInfo?
        nf = cn.activeNetworkInfo
        if (nf != null && nf.isConnected) {
            return true
        } else {
            Toast.makeText(activity, "اینترت شما وصل نیست", Toast.LENGTH_LONG).show()
            return false
        }
    }


    fun showpDialog() {
        try {
            val builder = AlertDialog.Builder(activity!!)
            val layoutInflater = layoutInflater
            builder.setView(layoutInflater.inflate(R.layout.loading, null))
            dialog = builder.create()
            dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.show()

        } catch (e: Exception) {
            //nothing
            Log.i("showpDialog", e.toString() + "")
        }

    }

    fun initDialog() {
        try {
            val builder = AlertDialog.Builder(activity!!)
            val layoutInflater = layoutInflater
            builder.setView(layoutInflater.inflate(R.layout.loading, null))
            dialog = builder.create()
            dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)

        } catch (e: Exception) {
            //nothing
            Log.i("showpDialog", e.toString() + "")
        }
    }

    fun hidepDialog() {
        try {
            dialog.dismiss()

        } catch (e: Exception) {
            //nothing
            Log.i("HidePDialog", e.toString() + "")
        }


    }


    fun dp2px(px: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, px.toFloat(), resources
                .displayMetrics
        ).toInt()
    }

    fun <T> isResponseValid(response: Response<StandardServerResponse<T>>): Int {
        try {
            if (!response.isSuccessful) {
                if (response.code() == 500) {
                    val errorBody = response.errorBody()?.string()
                    Log.i("qwert2", errorBody)
                    val jsonObject = JSONObject(errorBody?.trim() ?: "")
                    val statusCode = jsonObject.getInt("StatusCode")
                    Log.i("qwert2", statusCode.toString())
                    if (statusCode == 2) {
                        val error = jsonObject.getString("Message")
                        Log.i("qwert", error)
                        showAlertDialog(
                            R.drawable.ic_close_black_24dp,
                            "خطا",
                            error,
                            object : MyAlertDialog.OnButtonClickedListener {
                                override fun OnButtonClicked() {

                                }
                            }
                        )
                        return 0

                    }

                }
                if (response.code() == 401) {
                    Log.i("advjnsjvnsvnjzxvnzlxv","Here")

//                    showAlertDialog(
//                        R.drawable.ic_close_black_24dp,
//                        "خطا",
//                        "احراز هویت شما با مشکل مواجه شده است؛ لطفا دوباره لاگین کنید.",
//                        object : MyAlertDialog.OnButtonClickedListener {
//                            override fun OnButtonClicked() {
//                                startActivity(Intent(activity, ActivityLogin::class.java))
//                                activity?.overridePendingTransition(
//                                    R.anim.slide_left_in,
//                                    R.anim.slide_left_out
//                                )
//                                activity?.finishAffinity()
//                            }
//                        }
//
//                    )


//                    return 0
                    return 1
                }

//                showAlertDialog(
//                    R.drawable.ic_close_black_24dp,
//                    "خطا",
//                    "خطایی رخ داده است؛ لطفا بعدا تلاش نمایید.",
//                    object : MyAlertDialog.OnButtonClickedListener {
//                        override fun OnButtonClicked() {
//
//                        }
//                    }
//
//                )

//                return 0
                return 0

            }

        } catch (e: java.lang.Exception) {
            Log.i(TAG, e.message ?: "")
//            showAlertDialog(
//                R.drawable.ic_close_black_24dp,
//                "خطا",
//                "خطایی رخ داده است؛ لطفا بعدا تلاش نمایید.",
//                object : MyAlertDialog.OnButtonClickedListener {
//                    override fun OnButtonClicked() {
//
//                    }
//                }
//
//            )

//            return 0
//            return 1
        }

        val myResponse = response.body() ?:
//        return 0
        return 0

        if (!myResponse.isSuccess) {
            if (myResponse.message != "Not Fount")
//                showAlertDialog(
//                    R.drawable.ic_close_black_24dp,
//                    "خطا",
//                    myResponse.message ?: "خطایی رخ داده است لطفا بعدا تلاش نمایید.",
//                    object : MyAlertDialog.OnButtonClickedListener {
//                        override fun OnButtonClicked() {
//
//                        }
//                    }
//
//                )

//            return 0
            return 0
        }
        if (myResponse.data == null) {

//            return 0
            return 0
        }

        return 2
    }


    fun convertTime(input: Int): String {
        return if (input >= 10) {
            input.toString()
        } else {
            "0$input"
        }
    }


    fun showAlertDialog(
        icon: Int,
        title: String,
        text: String,
        onButtonClickedListener: MyAlertDialog.OnButtonClickedListener
    ) {
        val alertDialog =
            MyAlertDialog(icon, title, text, onButtonClickedListener)
        alertDialog.show(childFragmentManager, "")
    }


    fun numToMonth(num: Int): String {
        when (num) {
            1 -> return "فروردین"
            2 -> return "اردیبهشت"

            3 -> return "خرداد"

            4 -> return "تیر"

            5 -> return "مرداد"

            6 -> return "شهریور"

            7 -> return "مهر"

            8 -> return "آبان"

            9 -> return "آذر"

            10 -> return "دی"

            11 -> return "بهمن"

            12 -> return "اسفند"
        }
        return ""
    }




    fun silentLogin(onLoginCompleted: OnLoginCompleted) {
        Log.i("ererw", App.sharedPreferences?.getString(Constants.SECURITY_KEY, "null"))
        val result = App.getRetofitApi()
            ?.login(
                LoginRequestModel(
//                    securityKey,
                    App.sharedPreferences?.getString(Constants.SECURITY_KEY, "null"),
                    1,
                    BuildConfig.VERSION_CODE,
                    "imei"
                )
            )?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {
                        val result = it.body()
                        userInfo = UserInfo(
                            result?.data?.age,
                            result?.data?.gender,
                            result?.data?.fullName,
                            result?.data?.birthDayFa,
                            result?.data?.profileImage,
                            result?.data?.orgLevelId,
                            result?.data?.orgLevelTitle
                        )
//                        userInfo_Copy=userInfo  Copy
//                        userInfo_Copy=userInfo
//                        App.sharedPreferences?.edit()?.putString(Constants.TOKEN_Copy,it.body()?.data?.token)?.apply()
                        App.sharedPreferences?.edit()?.putString(Constants.TOKEN_Copy,"Bearer ${result?.data?.token}")?.apply()
                        if (it.body()?.data?.adminAccountInfo!=null)
                        {
                            App.sharedPreferences?.edit()
                                ?.putString(
                                    Constants.Admin_TWO,
                                    Gson().toJson(it.body()?.data?.adminAccountInfo)
                                )?.apply()
                        }else{
                            App.sharedPreferences?.edit()
                                ?.putString(
                                    Constants.Admin_TWO,
                                   ""
                                )?.apply()
                            App.sharedPreferences?.edit()
                                ?.putBoolean(
                                    Constants.Flag_Admin,
                                    false)?.apply()
                            adminAccountInfo= ir.adak.Vect.Data.Models.adminAccountInfo()
                        }
                        App.sharedPreferences?.edit()
                            ?.putString(
                                Constants.USER_INFO_Copy,
                                Gson().toJson(BaseActivity.userInfo)
                            )?.apply()
                        Log.i("vbvbvbvbs", result?.data?.projectTypes?.size.toString())
                        if (App.sharedPreferences?.getBoolean(Constants.Flag_Admin,false)!!)
                        {
                            token=adminAccountInfo?.token
                        }
                        App.sharedPreferences?.edit()
                            ?.putString(
                                Constants.USER_INFO,
                                Gson().toJson(userInfo)
                            )?.apply()
                        var Item=it.body()?.data?.adminAccountInfo
                        App.sharedPreferences?.edit()
                            ?.putString(
                                Constants.Admin_TWO,Gson().toJson(Item)
                            )?.apply()
                        BaseActivity.projectTypes=result?.data?.projectTypes
                        token = "Bearer ${result?.data?.token}"
                        App.sharedPreferences?.edit()
                            ?.putString(
                                Constants.TOKEN, "Bearer ${result?.data?.token}"
                            )?.apply()
                        securityKey = result?.data?.securityKey
                        Log.i("dvknsaaaaaaaA", securityKey)
                        App.sharedPreferences?.edit()
                            ?.putString(
                                Constants.SECURITY_KEY, result?.data?.securityKey
                            )?.apply()
                        App.sharedPreferences?.edit()
                            ?.putInt(
                                Constants.ORG_LEVEL_ID,
                                userInfo?.orgLevelId ?: 0
                            )?.apply()
                        updateSharedReferences()
                        onLoginCompleted.OnSuccess()
                    }
                    else {
                        showAlertDialog(
                            R.drawable.ic_close_black_24dp,
                            "خطا",
                            "احراز هویت شما با مشکل مواجه شده است؛ لطفا دوباره لاگین کنید.",
                            object : MyAlertDialog.OnButtonClickedListener {
                                override fun OnButtonClicked() {
//                                    Log.i("sdvmksmmvksvakd",adminAccountInfo?.token)
                                    var temp=App.sharedPreferences?.getString(Constants.Admin_TWO,"")

                                 if (!temp.isNullOrEmpty()||!temp.equals("null"))
                                 {
                                     App.sharedPreferences?.edit()?.putString(Constants.Admin_TWO,"")?.apply()
                                     startActivity(
                                         Intent(
                                             activity,
                                             SplashScreenActivity::class.java
                                         )
                                     )
                                 }else {

                                     startActivity(
                                         Intent(
                                             activity,
                                             ActivityLogin::class.java
                                         )
                                     )
                                 }

//                                    adminAccountInfo=data

                                    activity?.overridePendingTransition(
                                        R.anim.slide_left_in,
                                        R.anim.slide_left_out
                                    )
                                    activity?.finishAffinity()
                                }
                            }

                        )
                    }
                }
                onFailure = {
//                    Toast.makeText(context,"NIma MORADIeerrer",Toast.LENGTH_LONG).show()
                    showAlertDialog(
                        R.drawable.ic_close_black_24dp,
                        "خطا",
                        "احراز هویت شما با مشکل مواجه شده است؛ لطفا دوباره لاگین کنید.",
                        object : MyAlertDialog.OnButtonClickedListener {
                            override fun OnButtonClicked() {
                                startActivity(Intent(activity, ActivityLogin::class.java))
                                activity?.overridePendingTransition(
                                    R.anim.slide_left_in,
                                    R.anim.slide_left_out
                                )
                                activity?.finishAffinity()
                            }
                        }

                    )
                }
            }
    }

}