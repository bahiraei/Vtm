package ir.adak.Vect.UI.Activities

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.gson.Gson
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import ir.adak.Vect.App
import ir.adak.Vect.BuildConfig
import ir.adak.Vect.Data.Enums.FileTypeEnum
import ir.adak.Vect.Data.Models.*
import ir.adak.Vect.Data.Retrofit.StandardServerResponse
import ir.adak.Vect.Listeners.OnLoginCompleted
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.ActivityLogin.ActivityLogin
import ir.adak.Vect.UI.Activities.ActivityNewUser.ActivityNewUser
import ir.adak.Vect.UI.Activities.MainActivity.MainActivity
import ir.adak.Vect.UI.Activities.ScheduledJobsListActivity.ScheduledJobsListActivity
import ir.adak.Vect.UI.Dialogs.MyAlertDialog
import ir.adak.Vect.UI.Dialogs.MyQuestionDialog
import ir.adak.Vect.Utils.Constants
import ir.adak.Vect.Utils.enqueue
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer.*
import okio.Buffer
import org.json.JSONObject
import org.modelmapper.TypeToken
import retrofit2.Response
import java.io.*
import java.nio.channels.FileChannel


open class BaseActivity() : AppCompatActivity() {
    interface ChangeTabListener {
        fun changeTab(tab: String)
    }
    var changeTabListener: ChangeTabListener? = null
    lateinit var dialog: AlertDialog
    var token: String? = ""
    var token_2: String? = ""
    var token_copy: String? = ""
    var adminAccountInfo: adminAccountInfo ?=null
    var Flag_Admin=false
    var securityKey: String? = ""
    val TAG = "MyTagg"
    var reminders: MutableList<ReminderModel>? = mutableListOf()
    var Melicode:String ? =""
    var Perseneli:String ? =""
    var Number:String ? =""
    companion object
    {
        var projectTypes :ArrayList<projectTypes> ?= null
        var userInfo: UserInfo? = null
        var userInfo_Copy: UserInfo? = null
        var MyAct:Activity ?= null
        val APP_FOLDER = "${Environment.getExternalStorageDirectory()}${File.separator}JBase${File.separator}"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDialog()
        updateSharedReferences()

    }


    fun updateSharedReferences() {
        securityKey = App.sharedPreferences?.getString(Constants.SECURITY_KEY, "")
        token = App.sharedPreferences?.getString(Constants.TOKEN, "")
        Flag_Admin = App.sharedPreferences?.getBoolean(Constants.Flag_Admin, false)!!
        var temp = App.sharedPreferences?.getString(Constants.Admin_TWO,"")!!
        Log.i("vslmvsddvb",temp.toString())
        userInfo = Gson().fromJson(
            App.sharedPreferences?.getString(Constants.USER_INFO, ""),
            object : TypeToken<UserInfo>() {}.type
        )

        var vv=App.sharedPreferences?.getString(Constants.USER_INFO_Copy,"")
        if (!vv.isNullOrEmpty()&&!vv.equals("null"))
        {
            var  data_2: UserInfo = Gson().fromJson(vv, ir.adak.Vect.Data.Models.UserInfo::class.java)
            userInfo_Copy=data_2
        }


        if (temp.equals("null"))
        {
            token=App.sharedPreferences?.getString(Constants.TOKEN_Copy,"")

            return
        }
        if (!temp.isNullOrEmpty())
        {
            var  data: adminAccountInfo = Gson().fromJson(temp, ir.adak.Vect.Data.Models.adminAccountInfo::class.java)
            Log.i("vslmvsddvb",data.toString())
            adminAccountInfo=data
            App.sharedPreferences?.edit()
                ?.putString(
                    Constants.Admin_TWO, Gson().toJson(adminAccountInfo)
                )?.apply()
            if (Flag_Admin)
            {
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
                userInfo?.fullName=adminAccountInfo?.fullName
                userInfo?.orgLevelTitle=adminAccountInfo?.orgLevelTitle
                userInfo?.orgLevelId=adminAccountInfo?.orgLevelId
                userInfo?.age=adminAccountInfo?.age
                userInfo?.birthDayFa=adminAccountInfo?.birthDayFa
                userInfo?.profileImage=adminAccountInfo?.profileImage
                App.sharedPreferences?.edit()
                    ?.putString(
                        Constants.USER_INFO,
                        Gson().toJson(userInfo)
                    )?.apply()
                App.sharedPreferences?.edit()
                    ?.putString(
                        Constants.TOKEN,
                        token
                    )?.apply()
//                userInfo_Copy= userInfo
            }else{
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
                    var  user_2: UserInfo = Gson().fromJson(user,UserInfo::class.java)

                    userInfo?.fullName=user_2?.fullName
                    userInfo?.orgLevelTitle=user_2?.orgLevelTitle
                    userInfo?.age=user_2?.age
                    userInfo?.orgLevelId=adminAccountInfo?.orgLevelId
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
            if (!vv.isNullOrEmpty()&&!vv.equals("null"))
            {
                var  data_2: UserInfo = Gson().fromJson(vv, ir.adak.Vect.Data.Models.UserInfo::class.java)
                userInfo_Copy=data_2
            }
            token=App.sharedPreferences?.getString(Constants.TOKEN_Copy,"")
        }

        Log.i("dsvsfdfyutuo", userInfo.toString())





        reminders = Gson().fromJson(
            App.sharedPreferences?.getString(Constants.REMINDERS, ""),
            object : TypeToken<MutableList<ReminderModel>>() {}.type
        )

    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    fun isNetConnected(): Boolean {
        val cn = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nf: NetworkInfo?
        nf = cn.activeNetworkInfo
        if (nf != null && nf.isConnected) {
            return true
        } else {
            Toast.makeText(this, "اینترت شما وصل نیست", Toast.LENGTH_LONG).show()
            return false
        }
    }

    fun initDialog() {
        try {
            val builder = AlertDialog.Builder(this)
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


    fun showpDialog(context: Context) {
        try {
            val builder = AlertDialog.Builder(context)
            val layoutInflater = layoutInflater
            builder.setView(layoutInflater.inflate(R.layout.loading, null))
            dialog = builder.create()
            dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.show()
        } catch (e: Exception) {
            //nothing
            Log.i("HidePDialog", e.toString() + "")
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


    fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources
                .displayMetrics
        ).toInt()
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


    fun convertTime(input: Int): String {
        return if (input >= 10) {
            input.toString()
        } else {
            "0$input"
        }
    }



    fun <T> isResponseValid(response: Response<StandardServerResponse<T>>): Int {
        if (!response.isSuccessful) {
            try {
                if (response.code() == 500) {
                    val errorBody = response.errorBody()?.string() ?: ""
                    if (errorBody.isNotEmpty()) {
                        val jsonObject = JSONObject(errorBody)
                        val statusCode = jsonObject.getInt("StatusCode")
                        if (statusCode == 2) {
                            val error = jsonObject.getString("Message")
                            Log.i("errror_loggg", error)
                            showAlertDialog(
                                R.drawable.ic_close_black_24dp,
                                "خطا",
                                error,
                                object : MyAlertDialog.OnButtonClickedListener {
                                    override fun OnButtonClicked() {

                                    }
                                }

                            )
//                            return 0
                            return 0
                        }

                    }
                }
                else if (response.code() == 401) {



                    return 1
                }
                else {
//                    showAlertDialog(
//                        R.drawable.ic_close_black_24dp,
//                        "خطا",
//                        "خطایی رخ داده است؛ لطفا بعدا تلاش نمایید.",
//                        object : MyAlertDialog.OnButtonClickedListener {
//                            override fun OnButtonClicked() {
//
//                            }
//                        }
//
//                    )

//                    return 0
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

//                return 0
                return 0
            }

        } else {
            val myResponse = response.body() ?: return 1

            if (!myResponse.isSuccess) {
                if (myResponse.message != "Not Fount")
                    showAlertDialog(
                        R.drawable.ic_close_black_24dp,
                        "خطا",
                        myResponse.message ?: "خطایی رخ داده است لطفا بعدا تلاش نمایید.",
                        object : MyAlertDialog.OnButtonClickedListener {
                            override fun OnButtonClicked() {

                            }
                        }

                    )

//                return 0
                return 0
            }

            if (myResponse.data == null) {

                return 0
            }
        }

        return 2
    }

    fun getFileSize(file: File): Long {
        var size: Long = 0
        if (file.exists()) {
            if (file.isDirectory) {
                for (child in file.listFiles()!!) {
                    size += getFileSize(child)
                }
            } else {
                size = file.length()
            }
        }
        return size
    }


    fun getFileSizeLabel(file: File?, fileSize: Long?): String {
        val size: Long = if (file != null) {
            getFileSize(file) / 1024 // Get size and convert bytes into Kb.
        } else {
            (fileSize ?: -1) / 1024
        }
        return if (size >= 1024) {
            (size / 1024).toString() + " Mb"
        } else {
            "$size Kb"
        }
    }

    /**
     * Returns `s` with control characters and non-ASCII characters replaced with '?'.
     */
    fun toHumanReadableAscii(s: String): String {
        var i = 0
        val length = s.length
        var c: Int
        while (i < length) {
            c = s.codePointAt(i)
            if (c > '\u001f'.toInt() && c < '\u007f'.toInt()) {
                i += Character.charCount(c)
                continue
            }

            val buffer = Buffer()
            buffer.writeUtf8(s, 0, i)
            var j = i
            while (j < length) {
                c = s.codePointAt(j)
                var ss = -1
                if (c > '\u001f'.toInt() && c < '\u007f'.toInt()) {
                    ss = c
                } else {
                    ss = '?'.toInt()
                }
                buffer.writeUtf8CodePoint(ss)
                j += Character.charCount(c)
            }
            return buffer.readUtf8()
            i += Character.charCount(c)
        }
        return s
    }

    fun copy(src: File, fileName: String?, deleteOriginalFile: Boolean) {
        val file = File("$APP_FOLDER$fileName")
        if (!file.parentFile?.exists()!!) {
            file.parentFile?.mkdirs()
        }
        file.createNewFile()

        FileInputStream(src).use { input ->
            FileOutputStream(file).use { out ->
                input.copyTo(out)
            }
        }
        if (deleteOriginalFile)
            src.delete()
    }

    fun getMimeType(url: String): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

    fun InstallApk(file: File) {
        if (file.exists()) {
            var intent: Intent? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val apkUri = FileProvider.getUriForFile(
                    this,
                    BuildConfig.APPLICATION_ID + ".fileprovider",
                    file
                )
                intent = Intent(Intent.ACTION_INSTALL_PACKAGE);
                intent.data = apkUri
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
            } else {
                val apkUri = Uri.fromFile(file)
                intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(
                    apkUri,
                    "application/vnd.android.package-archive"
                )
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }
    }

     fun OpenFile(file: File) {
        if (file.exists()) {
            val uri = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID + ".fileprovider",
                file
            )
            val mime = getMimeType(file.absolutePath)
            try {
                // your intent here
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.setDataAndType(uri, mime)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // show message to user
                Toast.makeText(
                    this,
                    "نرم افزار مناسبی برای باز کردن این نوع فایل پیدا نشد",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    fun getFileTypeEnum(extension: String): FileTypeEnum {

        var imgTypes = arrayOf(".jpg", ".jpeg", ".png", ".tiff", ".gif", ".bmp")

        var videoTypes = arrayOf(
            ".mkv", ".flv", ".vob", ".ogg", ".avi", ".mov", ".qt", ".wmv", ".mp4",
            ".mpg", ".mpeg", ".3gp", ".dvx", ".ogv", ".ts"
        )

        var soundTypes = arrayOf(".aac", ".m4a", ".mp3", ".mpc", ".ogg", ".wav", ".wma")


        var ext = "." + extension.toLowerCase()
        if (imgTypes.contains(ext)) {
            return FileTypeEnum.Image
        }
        if (videoTypes.contains(ext)) {
            return FileTypeEnum.Video
        }
        if (soundTypes.contains(ext)) {
            return FileTypeEnum.Audio
        }

        return FileTypeEnum.Unknown
    }

    fun convertToMutable(imgIn: Bitmap): Bitmap {
        var imageIn = imgIn
        try {
            //this is the file going to use temporally to save the bytes.
            // This file will not be a image, it will store the raw image data.
            var file = File("${Environment.getExternalStorageDirectory()}${File.separator}temp.tmp")

            //Open an RandomAccessFile
            //Make sure you have added uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
            //into AndroidManifest.xml file
            var randomAccessFile = RandomAccessFile(file, "rw")

            // get the width and height of the source bitmap.
            var width = imageIn.width
            var height = imageIn.height
            var type = imageIn.config

            //Copy the byte to the file
            //Assume source bitmap loaded using options.inPreferredConfig = Config.ARGB_8888;
            var channel = randomAccessFile.getChannel()
            var map =
                channel.map(FileChannel.MapMode.READ_WRITE, 0, (imageIn.rowBytes * height).toLong())
            imageIn.copyPixelsToBuffer(map);
            //recycle the source bitmap, this will be no longer used.
            imageIn.recycle();
            System.gc();// try to force the bytes from the imgIn to be released

            //Create a new bitmap to load the bitmap again. Probably the memory will be available.
            imageIn = Bitmap.createBitmap(width, height, type);
            map.position(0)
            //load it back from temporary
            imageIn.copyPixelsFromBuffer(map);
            //close the temporary file and channel , then delete that also
            channel.close();
            randomAccessFile.close();

            // delete the temp file
            file.delete();

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return imageIn
    }

    fun openKeyboard(activity: AppCompatActivity, editText: EditText) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    fun hideKeyboard(activity: AppCompatActivity) {
        val view = activity.getCurrentFocus()
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
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
        alertDialog.show(supportFragmentManager, "")
    }

    fun showQuestionDialog(
        icon: Int,
        title: String,
        text: String,
        onButtonsClicked: MyQuestionDialog.OnButtonsClicked
    ) {
        val alertDialog = MyQuestionDialog(icon, title, text, onButtonsClicked)
        alertDialog.show(supportFragmentManager, "")
    }

    override fun onBackPressed() {

        if (drawerLayout?.isDrawerOpen(GravityCompat.END) == true) {
            drawerLayout?.closeDrawer(GravityCompat.END)
        } else {
            super.onBackPressed()
        }
    }

    fun setupDrawer() {


        if (img_avatar_drawer != null) {
            Glide.with(this).load(Constants.AVATAR_BASE_URL + userInfo?.profileImage)
                .into(img_avatar_drawer)
        }


        txt_username_drawer?.text = userInfo?.fullName


        btn_menu.setOnClickListener {
            drawerLayout?.openDrawer(GravityCompat.END)
        }
        btn_jobs_drawer.setOnClickListener {
            if (drawerLayout?.isDrawerOpen(GravityCompat.END) == true) {
                drawerLayout?.closeDrawer(GravityCompat.END)
            }
            drawerLayout?.addDrawerListener(object : DrawerLayout.DrawerListener {
                override fun onDrawerStateChanged(newState: Int) {

                }

                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                }

                override fun onDrawerClosed(drawerView: View) {
                    if (this@BaseActivity !is MainActivity) {
                        val intent = Intent()
                        intent.putExtra("tab", "Jobs")
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } else {
                        changeTabListener?.changeTab("Jobs")
                    }
                }

                override fun onDrawerOpened(drawerView: View) {

                }
            })


        }
        btn_meetings_drawer.setOnClickListener {
            if (drawerLayout?.isDrawerOpen(GravityCompat.END) == true) {
                drawerLayout?.closeDrawer(GravityCompat.END)
            }
            drawerLayout?.addDrawerListener(object : DrawerLayout.DrawerListener {
                override fun onDrawerStateChanged(newState: Int) {

                }

                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                }

                override fun onDrawerClosed(drawerView: View) {
                    if (this@BaseActivity !is MainActivity) {
                        val intent = Intent()
                        intent.putExtra("tab", "Meetings")
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } else {
                        changeTabListener?.changeTab("Meetings")
                    }
                }

                override fun onDrawerOpened(drawerView: View) {

                }
            })


        }
        btn_notes_drawer.setOnClickListener {
//            Toast.makeText(this , "این ماژول فعال نمیباشد" , Toast.LENGTH_LONG).show()
            if (drawerLayout?.isDrawerOpen(GravityCompat.END) == true) {
                drawerLayout?.closeDrawer(GravityCompat.END)
            }
            drawerLayout?.addDrawerListener(object : DrawerLayout.DrawerListener {
                override fun onDrawerStateChanged(newState: Int) {

                }

                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                }

                override fun onDrawerClosed(drawerView: View) {
                    if (this@BaseActivity !is MainActivity) {
                        val intent = Intent()
                        intent.putExtra("tab", "Notes")
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } else {
                        changeTabListener?.changeTab("Notes")
                    }
                }

                override fun onDrawerOpened(drawerView: View) {

                }
            })


        }
        btn_scheduled_jobs_drawer?.setOnClickListener {
//            Toast.makeText(this , "این ماژول فعال نمیباشد" , Toast.LENGTH_LONG).show()
            if (drawerLayout?.isDrawerOpen(GravityCompat.END) == true) {
                drawerLayout?.closeDrawer(GravityCompat.END)
            }
            drawerLayout?.addDrawerListener(object : DrawerLayout.DrawerListener {
                override fun onDrawerStateChanged(newState: Int) {

                }

                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                }

                override fun onDrawerClosed(drawerView: View) {
                    if (this@BaseActivity !is ScheduledJobsListActivity) {
                        val intent =
                            Intent(this@BaseActivity, ScheduledJobsListActivity::class.java)
                        startActivityForResult(intent, Constants.OPEN_FROM_DRAWER)
                        overridePendingTransition(
                            R.anim.alpha_in,
                            R.anim.alpha_up
                        )
                        drawerLayout?.removeDrawerListener(this)
                    }
                }

                override fun onDrawerOpened(drawerView: View) {

                }
            })

        }
        btn_new_user_drawer?.setOnClickListener {
//            Toast.makeText(this , "این ماژول فعال نمیباشد" , Toast.LENGTH_LONG).show()
            if (drawerLayout?.isDrawerOpen(GravityCompat.END) == true) {
                drawerLayout?.closeDrawer(GravityCompat.END)
            }
            drawerLayout?.addDrawerListener(object : DrawerLayout.DrawerListener {
                override fun onDrawerStateChanged(newState: Int) {

                }

                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                }

                override fun onDrawerClosed(drawerView: View) {
                    if (this@BaseActivity !is ActivityNewUser) {
                        val intent =
                            Intent(this@BaseActivity, ActivityNewUser::class.java)
                        startActivityForResult(intent, Constants.OPEN_FROM_DRAWER)
                        overridePendingTransition(
                            R.anim.alpha_in,
                            R.anim.alpha_up
                        )
                        drawerLayout?.removeDrawerListener(this)
                    }
                }

                override fun onDrawerOpened(drawerView: View) {

                }
            })

        }

        btn_exit_drawer.setOnClickListener {
            App.sharedPreferences?.edit()?.putString(Constants.SECURITY_KEY,"")?.apply()
            finishAffinity()
        }
    }
    fun silentLogin(onLoginCompleted: OnLoginCompleted) {
        val result = App.getRetofitApi()
            ?.login(
                LoginRequestModel(
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
//                        userInfo_Copy= userInfo  Copy
//                          userInfo_Copy= userInfo
                        App.sharedPreferences?.edit()?.putString(Constants.TOKEN_Copy,it.body()?.data?.token)?.apply()
                        if (it.body()?.data?.adminAccountInfo!=null)
                        {
                            App.sharedPreferences?.edit()
                                ?.putString(
                                    Constants.Admin_TWO,
                                    Gson().toJson(it.body()?.data?.adminAccountInfo)
                                )?.apply()
                        }else {
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
                                Gson().toJson(userInfo)
                            )?.apply()
                        Log.i("vbvbvbvbs", result?.data?.projectTypes?.size.toString())
                        if (App.sharedPreferences?.getBoolean(Constants.Flag_Admin,false)!!)
                        {
                            token=adminAccountInfo?.token
                        }
                        projectTypes=result?.data?.projectTypes
                        App.sharedPreferences?.edit()
                            ?.putString(
                                Constants.USER_INFO,
                                Gson().toJson(userInfo)
                            )?.apply()
                        token =
                            "Bearer ${result?.data?.token}"
                        App.sharedPreferences?.edit()
                            ?.putString(
                                Constants.TOKEN, "Bearer ${result?.data?.token}"
                            )?.apply()
                        securityKey = result?.data?.securityKey
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
                    } else {
                        showAlertDialog(
                            R.drawable.ic_close_black_24dp,
                            "خطا",
                            "احراز هویت شما با مشکل مواجه شده است؛ لطفا دوباره لاگین کنید.",
                            object : MyAlertDialog.OnButtonClickedListener {
                                override fun OnButtonClicked() {
                                    startActivity(
                                        Intent(
                                            this@BaseActivity,
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
                onFailure = {
                    showAlertDialog(
                        R.drawable.ic_close_black_24dp,
                        "خطا",
                        "احراز هویت شما با مشکل مواجه شده است؛ لطفا دوباره لاگین کنید.",
                        object : MyAlertDialog.OnButtonClickedListener {
                            override fun OnButtonClicked() {
                                startActivity(Intent(this@BaseActivity, ActivityLogin::class.java))
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
    }
}