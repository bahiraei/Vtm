package ir.adak.Vect

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.devs.vectorchildfinder.VectorChildFinder
import com.devs.vectorchildfinder.VectorDrawableCompat
import com.google.gson.Gson
import ir.adak.Vect.Data.Models.FilterProjectModel
import ir.adak.Vect.Data.Models.ProjectStep
import ir.adak.Vect.Data.Models.UserInfo
import ir.adak.Vect.Listeners.OnLoginCompleted
import ir.adak.Vect.UI.Activities.AddProjectActivity.AddProjectActivity
import ir.adak.Vect.UI.Activities.Amar_Activity
import ir.adak.Vect.UI.Activities.BaseActivity
import ir.adak.Vect.UI.Activities.MainActivity.MainActivity
import ir.adak.Vect.UI.Activities.ScheduledJobsListActivity.ScheduledJobsListActivity
import ir.adak.Vect.UI.Activities.UsersActivity.UsersActivity
import ir.adak.Vect.UI.Dialogs.Data_Admin
import ir.adak.Vect.UI.Dialogs.filter_Admin
import ir.adak.Vect.Utils.Constants
import ir.adak.Vect.Utils.enqueue
import kotlinx.android.synthetic.main.activity_main__new.*
import kotlinx.android.synthetic.main.typess.view.*


class MainActivity_New : BaseActivity() {
    var Flag_ADMIN:String="0"
    var S_1=""
    var S_1_2=""
    var S_1_3=""
    var S_1_4=""

    var S_2=""
    var    mMediaBrowser : MediaBrowserCompat ?=null
    var S_2_1=""
    var S_2_2=""
    var S_2_3=""
    var usercopy:UserInfo ?=null
    var Flag_Anti=false
    companion object{
        var User:UserInfo ?=null
    }
    fun OpenModal_Admin()
    {
        Log.i("adkmskvmskd", "Asc")
        var admin_1=Data_Admin()
        Log.i("acacsfv", "B")
        var v=App.sharedPreferences?.getString(Constants.USER_INFO_Copy, "")
        var  data: UserInfo = Gson().fromJson(v, ir.adak.Vect.Data.Models.UserInfo::class.java)
        Log.i("dadfsb_2", data.fullName.toString())
        Log.i("dadfsb_2", userInfo_Copy?.fullName.toString())
//        admin_1.name= userInfo_Copy?.fullName
        admin_1.name= S_1
//        admin_1.orgleveltitle= userInfo_Copy?.orgLevelTitle
        admin_1.orgleveltitle= S_1_2
//        admin_1.orglevelid= userInfo_Copy?.orgLevelId.toString()
        admin_1.orglevelid= S_1_3
//        admin_1.profile= userInfo_Copy?.profileImage
        admin_1.profile=S_1_4
        var admin_2=Data_Admin()
//        admin_2.name= adminAccountInfo?.fullName
        admin_2.name=S_2
//        admin_2.orgleveltitle= adminAccountInfo?.orgLevelTitle
        admin_2.orgleveltitle=S_2_1
//        admin_2.orglevelid= adminAccountInfo?.orgLevelId.toString()
        admin_2.orglevelid= S_2_2
//        admin_2.profile= adminAccountInfo?.profileImage
        admin_2.profile= S_2_3
        Log.i("dadfsb_2", adminAccountInfo?.fullName.toString())
        var aarry=ArrayList<Data_Admin>()
        if (Flag_Admin)
        {
            Flag_ADMIN="B"
            admin_2.isadmin=true
        }else{
            Flag_ADMIN="A"
            admin_1.isadmin=true
        }
        aarry.add(admin_1)
        aarry.add(admin_2)
        var btn= filter_Admin(aarry)
        btn.Clicl(object : filter_Admin.data_7 {
            override fun DATA(S: String) {
                if (Flag_ADMIN.equals(S)) {
                    return
                }
                if (S.equals("A")) {
                    App.sharedPreferences?.edit()?.putBoolean(Constants.Flag_Admin, false)?.apply()
//                recreate()
                    Log.i("svsfbsfs", "B")
                    startActivity(Intent(this@MainActivity_New, SplashScreenActivity::class.java))
                    finish()
                } else if (S.equals("B")) {
                    App.sharedPreferences?.edit()?.putBoolean(Constants.Flag_Admin, true)?.apply()
//                recreate()
                    Log.i("svsfbsfs", "A")
                    startActivity(Intent(this@MainActivity_New, SplashScreenActivity::class.java))
                    finish()

                }
            }

        })




        btn.show(supportFragmentManager, "a")
    }



    override fun onDestroy() {
        super.onDestroy()
        mMediaBrowser?.disconnect();
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main__new)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
//            window.statusBarColor=Color.TRANSPARENT
//            window.addFlags(WindowManager.LayoutParams.FLAG)
        }




        imageView38.setOnClickListener {
            startActivity(
                Intent(this, Amar_Activity::class.java)
            )
        }






        if (!adminAccountInfo?.fullName.isNullOrEmpty())
        {
            S_2= adminAccountInfo?.fullName.toString()
            S_2_1= adminAccountInfo?.orgLevelTitle.toString()
            S_2_2= adminAccountInfo?.orgLevelId.toString()
            S_2_3= adminAccountInfo?.profileImage.toString()
            S_1= userInfo_Copy?.fullName.toString()
            S_1_2= userInfo_Copy?.orgLevelTitle.toString()
            S_1_3= userInfo_Copy?.orgLevelId.toString()
            S_1_4= userInfo_Copy?.profileImage.toString()
        }
        Log.i("vmladsmvldmlv", adminAccountInfo?.token.toString())
        roundedImageView.setOnClickListener {
            if (!adminAccountInfo?.token.isNullOrEmpty())
            {
                OpenModal_Admin()
            }
        }
        textView41.setOnClickListener {
            if (!adminAccountInfo?.token.isNullOrEmpty())
            {
                OpenModal_Admin()
            }

        }
        textView40.setOnClickListener {
            if (!adminAccountInfo?.token.isNullOrEmpty())
            {
                OpenModal_Admin()
            }
        }
        imageView41.setOnClickListener {
            if (!adminAccountInfo?.token.isNullOrEmpty())
            {
                OpenModal_Admin()
            }
        }


        textView40.setText(userInfo?.fullName)
        textView41.setText(userInfo?.orgLevelTitle)
        linearLayout5.setOnClickListener {
            startActivity(
                Intent(this, AddProjectActivity::class.java)
            )
        }
        img_1.setOnClickListener {
            val filterProjectModel = FilterProjectModel()
            filterProjectModel.step = ProjectStep.ToDo.value
            startActivity(Intent(this, MainActivity::class.java).apply {
//                putExtra("selectedFilterProjectModel", filterProjectModel)
            })
        }
        imageView25.setOnClickListener {
            val filterProjectModel = FilterProjectModel()
            filterProjectModel.step = ProjectStep.ToDo.value
            startActivity(Intent(this, MainActivity::class.java).apply {
//                putExtra("selectedFilterProjectModel", filterProjectModel)
                putExtra("Type", "30")
            })
        }
        vdkd.setOnClickListener {
            val filterProjectModel = FilterProjectModel()
            filterProjectModel.step = ProjectStep.ToDo.value
            startActivity(Intent(this, MainActivity::class.java).apply {
//                putExtra("selectedFilterProjectModel", filterProjectModel)
                putExtra("Type", "18")
            })
        }
        id_modir.setOnClickListener {
            val filterProjectModel = FilterProjectModel()
            filterProjectModel.step = ProjectStep.ToDo.value
            startActivity(Intent(this, MainActivity::class.java).apply {
//                putExtra("selectedFilterProjectModel", filterProjectModel)
                putExtra("Type", "20")
            })
        }
        id_mojri.setOnClickListener {
            val filterProjectModel = FilterProjectModel()
            filterProjectModel.step = ProjectStep.ToDo.value
            startActivity(Intent(this, MainActivity::class.java).apply {
//                putExtra("selectedFilterProjectModel", filterProjectModel)
                putExtra("Type", "22")
            })
        }
        id_hamkar.setOnClickListener {
            val filterProjectModel = FilterProjectModel()
            filterProjectModel.step = ProjectStep.ToDo.value
            startActivity(Intent(this, MainActivity::class.java).apply {
//                putExtra("selectedFilterProjectModel", filterProjectModel)
                putExtra("Type", "24")
            })
        }
        id_schajule.setOnClickListener {
//            Toast.makeText(this, "این ماژول غیر فعال است", Toast.LENGTH_LONG).show()
//            val filterProjectModel = FilterProjectModel()
//            filterProjectModel.step = ProjectStep.ToDo.value
//            startActivity(Intent(this, MainActivity::class.java).apply {
////                putExtra("selectedFilterProjectModel", filterProjectModel)
//                putExtra("Type","14")
//            })
            startActivity(Intent(this@MainActivity_New, ScheduledJobsListActivity::class.java))
        }
        img_2.setOnClickListener {
            val filterProjectModel = FilterProjectModel()
            filterProjectModel.step = ProjectStep.ToDo.value
            startActivity(Intent(this, MainActivity::class.java).apply {
//                putExtra("selectedFilterProjectModel", filterProjectModel)
                putExtra("Type", "16")
            })
        }
        linearLayout6.setOnClickListener {
//            val filterProjectModel = FilterProjectModel()
//            startActivity(Intent(this,MainActivity::class.java)
//                .apply {
//                    putExtra("Type","12")
//            })
            val filterProjectModel = FilterProjectModel()
            filterProjectModel.step = ProjectStep.ToDo.value
            startActivity(Intent(this, MainActivity::class.java).apply {
//                putExtra("selectedFilterProjectModel", filterProjectModel)
                putExtra("Type", "12")
            })



        }


        if (adminAccountInfo?.token.isNullOrEmpty())
        {
            imageView41.visibility=View.GONE
        }

        if (!userInfo?.profileImage.isNullOrEmpty()) {
            Log.i("dfmlsvadv", Constants.AVATAR_BASE_URL + userInfo?.profileImage)
            Glide.with(this).load(Constants.AVATAR_BASE_URL + userInfo?.profileImage)
                .into(roundedImageView)
        }
        linearLayout7.setOnClickListener {
            startActivity(Intent(this, UsersActivity::class.java))
        }
        vdvs.setOnClickListener {
//            Toast.makeText(this, "این ماژول غیر فعال است", Toast.LENGTH_LONG).show()
            val filterProjectModel = FilterProjectModel()
            filterProjectModel.step = ProjectStep.ToDo.value
            startActivity(Intent(this, MainActivity::class.java).apply {
//                putExtra("selectedFilterProjectModel", filterProjectModel)
                putExtra("Type","14")
            })
        }
    }

    override fun onResume() {
        super.onResume()
        GetHOME()
    }
    private fun GetHOME() {
//        showpDialog(this)
        var Req=App.getRetofitApi()?.GetHomeAmar(token)
        Log.i("dacadvvd", token)
        Req?.enqueue {
            onResponse={
                Log.i("dvmsdmlv", it.code().toString())
                Log.i("dvmsdmlv", it.body().toString())
//                hidepDialog()
                if (isResponseValid(it)==2)
                {
                    var v=it.body()?.data
                    if(v?.proExpired==0)
                    {
                        textView48.setTextColor(Color.parseColor("#8A8A8A"))
                        val vector = VectorChildFinder(
                            this@MainActivity_New, R.drawable.ic_narengi, img_2
                        )
                        val path1: VectorDrawableCompat.VFullPath = vector.findPathByName("Mypath")
                        var ss= Color.parseColor("#8A8A8A")
                        path1.setFillColor(ss);

                    }


                    Log.i("dvmzxv", v.toString())
                    if (it.body()?.data?.actionStatusPending!! > 0)
                    {
                        textView57.setText(v?.actionStatusPending.toString())
                    }else{
                        textView57.visibility=View.GONE
                        imageView25.visibility=View.GONE
                    }
                    textView51.setText(v?.proAllAndNotClose.toString())
                    textView48.setText(v?.proExpired.toString())
                    textView46.setText(v?.proClosed.toString())
                    bt.setText(v?.proCreator.toString())
                    bvc.setText(v?.proMojry.toString())
                    mbmf.setText(v?.proHamkar.toString())
                }
                else if (isResponseValid(it)==1)
                {
                    silentLogin(object : OnLoginCompleted {
                        override fun OnSuccess() {
                            GetHOME()
                        }
                    })
                }
            }
            onFailure={
//              hidepDialog()
            }
        }
    }
}