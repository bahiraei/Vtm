package ir.adak.Vect.UI.Activities.UsersActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import ir.adak.Vect.Adapters.UsersAdapter
import ir.adak.Vect.App
import ir.adak.Vect.Data.Models.partnersForSecretary
import ir.adak.Vect.Listeners.OnLoginCompleted
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.ActivityNewUser.ActivityNewUser
import ir.adak.Vect.UI.Activities.BaseActivity
import ir.adak.Vect.UI.Dialogs.MyAlertDialog
import ir.adak.Vect.Utils.Constants
import ir.adak.Vect.Utils.WrapContentLinearLayoutManager
import ir.adak.Vect.Utils.enqueue
import kotlinx.android.synthetic.main.activity_users.*
import kotlinx.android.synthetic.main.activity_users.fab_add_user
import kotlinx.android.synthetic.main.activity_users.rv_users
import kotlinx.android.synthetic.main.activity_users_2.*
import kotlinx.android.synthetic.main.fragment_task_filter_main.*
import okhttp3.MediaType
import okhttp3.RequestBody

class UsersActivity : BaseActivity() {
    var usersAdapter : UsersAdapter ?=null
    var Org:String=""
    var Flag=false
    var list: ArrayList<partnersForSecretary> ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_users)
        setContentView(R.layout.activity_users_2)
        usersAdapter=UsersAdapter(this)
        rv_users.apply {
            layoutManager = WrapContentLinearLayoutManager(this@UsersActivity)
            adapter = usersAdapter
        }
        Flag= App.sharedPreferences?.getBoolean(Constants.Flag_Admin,false)!!
        rv_users.isNestedScrollingEnabled=false
        getMyPersonel()
        spin.setOnItemSelectedListener { view, position, id, item ->
            if (list!=null)
            {
                Org= list?.get(position)?.orgLevelId.toString()
            }
        }
        fab_add_user.setOnClickListener {
            startActivityForResult(Intent(this , ActivityNewUser::class.java) , Constants.ADD_NEW_USER_REQUEST_CODE)
        }
        button2.setOnClickListener {
            Del()
        }

        button3.setOnClickListener {
            if(!Org.isNullOrEmpty())
            {
                Select()
            }

        }
    }

    fun Select()
    {
        if(isNetConnected()){
            if(!dialog.isShowing)
                showpDialog(this)
            var body=RequestBody.create(MediaType.parse("text/plain"),Org)
            val req = App.getRetofitApi()?.SelectAdmin(token,body)
            req?.enqueue {
                onResponse={
                    if(isResponseValid(it) == 2){
                        val myRrsponse = it.body()
                        if (myRrsponse?.isSuccess!!)
                        {
//                            App.sharedPreferences?.edit()?.putString(Constants.Admin_TWO,"")?.apply()
//                            App.sharedPreferences?.edit()?.putString(Constants.TOKEN,"")?.apply()
//                            App.sharedPreferences?.edit()?.putString(Constants.SECURITY_KEY,"")?.apply()
                            finish()
                        }
                        hidepDialog()
                    }else if(isResponseValid(it) == 1){
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                getMyPersonel()
                            }
                        })
                    }
                }
                onFailure={
                    Log.i(TAG , it?.message?:"")
                    showAlertDialog(
                        R.drawable.ic_close_black_24dp,
                        "خطا",
                        "خطایی رخ داده است؛ لطفا بعدا تلاش نمایید.",
                        object : MyAlertDialog.OnButtonClickedListener {
                            override fun OnButtonClicked() {

                            }
                        }

                    )
                    hidepDialog()
                }
            }
        }
    }
    fun  Del()
    {
        if(isNetConnected()){
            if(!dialog.isShowing)
                showpDialog(this)
            val req = App.getRetofitApi()?.DelAdmin(token)
            req?.enqueue {
                onResponse={
                    if(isResponseValid(it) == 2){
                        val myRrsponse = it.body()
                        if (myRrsponse?.isSuccess!!)
                        {
//                            App.sharedPreferences?.edit()?.putString(Constants.Admin_TWO,"")?.apply()
//                            App.sharedPreferences?.edit()?.putString(Constants.TOKEN,"")?.apply()
//                            App.sharedPreferences?.edit()?.putString(Constants.SECURITY_KEY,"")?.apply()
                            finish()
                        }
                        hidepDialog()
                    }else if(isResponseValid(it) == 1){
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                getMyPersonel()
                            }
                        })
                    }
                }
                onFailure={
                    Log.i(TAG , it?.message?:"")
                    showAlertDialog(
                        R.drawable.ic_close_black_24dp,
                        "خطا",
                        "خطایی رخ داده است؛ لطفا بعدا تلاش نمایید.",
                        object : MyAlertDialog.OnButtonClickedListener {
                            override fun OnButtonClicked() {

                            }
                        }

                    )
                    hidepDialog()
                }
            }
        }
    }
    fun getMyPersonel(){
        if(isNetConnected()){
            if(!dialog.isShowing)
                showpDialog(this)
            val req = App.getRetofitApi()?.getMyPersonel(token)
            req?.enqueue {
                onResponse={
                    if(isResponseValid(it) == 2){
                        val myRrsponse = it.body()

                        if (Flag)
                        {
                            Del.visibility=View.GONE
                            Chang.visibility=View.GONE
                        }else{
                            if(myRrsponse?.data?.currentSecretary!=null)
                            {
                                Del.visibility=View.VISIBLE
                                Chang.visibility=View.GONE
                                textView62.setText(myRrsponse?.data?.currentSecretary?.fullName)
                            }else{
                                Del.visibility=View.GONE
                                Chang.visibility=View.VISIBLE
                                var data=ArrayList<String>()
                                list= myRrsponse?.data?.partnersForSecretary
                                myRrsponse?.data?.partnersForSecretary?.forEach {
                                    data.add(it.fullName.toString())
                                }
                                spin.setItems(data)
                            }
                        }

                        usersAdapter?.updateList(myRrsponse?.data?.partners)
                        hidepDialog()
                    }else if(isResponseValid(it) == 1){
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                getMyPersonel()
                            }
                        })
                    }
                }
                onFailure={
                    Log.i(TAG , it?.message?:"")
                    showAlertDialog(
                        R.drawable.ic_close_black_24dp,
                        "خطا",
                        "خطایی رخ داده است؛ لطفا بعدا تلاش نمایید.",
                        object : MyAlertDialog.OnButtonClickedListener {
                            override fun OnButtonClicked() {
                                finish()
                            }
                        }

                    )
                    hidepDialog()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.ADD_NEW_USER_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            getMyPersonel()
        }

        if (requestCode == 45 && resultCode == Activity.RESULT_OK){
            getMyPersonel()
        }
    }


}
