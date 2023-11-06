package ir.adak.Vect

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import ir.adak.Vect.Data.Models.LoginRequestModel
import ir.adak.Vect.Data.Models.UserInfo
import ir.adak.Vect.UI.Activities.ActivityLogin.ActivityLogin
import ir.adak.Vect.UI.Activities.BaseActivity
import ir.adak.Vect.UI.Dialogs.MyAlertDialog
import ir.adak.Vect.Utils.Constants
import ir.adak.Vect.Utils.enqueue
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        var S=App.sharedPreferences?.getString(Constants.SECURITY_KEY,"B")
        Log.i("svnsnvs",S)
        if (S.equals("B")||S.equals(" ")||S.isNullOrEmpty())
        {
            Log.i("Asdczz","a")
            startActivity(Intent(this,ActivityLogin::class.java))
            finish()
        }else{
            Log.i("Asdczz","B")
            var req2 = App.getRetofitApi()?.login(
                LoginRequestModel(
                    S,
                    1, BuildConfig.VERSION_CODE, ""
                ))
            req2?.enqueue {
                onResponse={
                    Log.i("advadvava",it.code().toString())
                    if (isResponseValid(it)==2)
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
//                        userInfo_Copy= userInfo
//                        token=token_copy





                        App.sharedPreferences?.edit()?.putString(Constants.TOKEN_Copy,myResponse?.data?.token)?.apply()
                        if (myResponse?.data?.adminAccountInfo!=null)
                        {
                            Log.i("ddsacaca", myResponse?.data?.adminAccountInfo.toString())
                            App.sharedPreferences?.edit()
                                ?.putString(
                                    Constants.Admin_TWO,
                                    Gson().toJson(myResponse.data?.adminAccountInfo)
                                )?.apply()
                        }
                        Log.i("ddsacaca", "B")
                        App.sharedPreferences?.edit()
                            ?.putString(
                                Constants.USER_INFO_Copy,
                                Gson().toJson(userInfo)
                            )?.apply()
                        Log.i("vbvbvbvbs", myResponse?.data?.projectTypes?.size.toString())
                        BaseActivity.projectTypes=myResponse?.data?.projectTypes
                        App.sharedPreferences?.edit()
                            ?.putString(
                                Constants.USER_INFO,
                                Gson().toJson(userInfo)
                            )?.apply()
                        var Item=it.body()?.data?.adminAccountInfo
                        App.sharedPreferences?.edit()
                            ?.putString(
                                Constants.Admin_TWO, Gson().toJson(Item)
                            )?.apply()
                        token ="Bearer ${myResponse?.data?.token}"
                        App.sharedPreferences?.edit()
                            ?.putString(
                                Constants.TOKEN, token
                            )?.apply()

                        App.sharedPreferences?.edit()
                            ?.putString(
                                Constants.TOKEN_Copy, token
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
                                this@SplashScreenActivity,
                                MainActivity_New::class.java
                            )
                        )
                        overridePendingTransition(
                            R.anim.slide_left_in,
                            R.anim.slide_left_out
                        )
                        finish()
                    }else{
                        showAlertDialog(
                            R.drawable.ic_close_black_24dp,
                            "خطا",
                            "احراز هویت شما با مشکل مواجه شده است؛ لطفا دوباره لاگین کنید.",
                            object : MyAlertDialog.OnButtonClickedListener {
                                override fun OnButtonClicked() {
                                    startActivity(
                                        Intent(
                                            this@SplashScreenActivity,
                                            ActivityLogin::class.java
                                        )
                                    )
                                    overridePendingTransition(
                                        R.anim.slide_left_in,
                                        R.anim.slide_left_out
                                    )
                                    finishAffinity()
                                }
                            }

                        )
                    }
                }
                onFailure={
                    Snackbar.make(constraint_parent,"عدم ارتباط با سرور",Snackbar.LENGTH_INDEFINITE).show()
                }
            }
        }
    }
}