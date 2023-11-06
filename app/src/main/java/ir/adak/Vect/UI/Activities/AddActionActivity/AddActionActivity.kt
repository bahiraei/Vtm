package ir.adak.Vect.UI.Activities.AddActionActivity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder
import cafe.adriel.androidaudiorecorder.model.AudioChannel
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate
import cafe.adriel.androidaudiorecorder.model.AudioSource
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import droidninja.filepicker.models.sort.SortingTypes
import ir.adak.Vect.App
import ir.adak.Vect.Data.Models.FollowUp
import ir.adak.Vect.Data.Models.Project
import ir.adak.Vect.Data.Models.addactionViewModel
import ir.adak.Vect.Data.Retrofit.RegisteroreditFollowUpModel
import ir.adak.Vect.Data.Retrofit.RemoveFileActionDto
import ir.adak.Vect.Data.Retrofit.data__file_action
import ir.adak.Vect.Listeners.OnLoginCompleted
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.BaseActivity
import ir.adak.Vect.UI.Dialogs.AttachToFollowUpBottomSheet
import ir.adak.Vect.UI.Dialogs.MyAlertDialog
import ir.adak.Vect.Utils.Constants
import ir.adak.Vect.Utils.ProgressRequestBody
import ir.adak.Vect.Utils.SolarCalendar
import ir.adak.Vect.Utils.enqueue
import kotlinx.android.synthetic.main.activity_add_action.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.net.URLEncoder
import java.text.DecimalFormat

class AddActionActivity : BaseActivity() {


    var f: DecimalFormat = DecimalFormat("00")

    private var y1: Int = 0
    private var m1: Int = 0
    private var d1: Int = 0
    var maxFilesCount = 5
    var H1 = 0
    var M1 = 0
    private var actionDate: String = ""
    private var selectedActionHour: String = ""
    private var selectedActionMinute: String = ""
    private var selectedActionDate: String = ""
    private var selectedActionTime: String = ""
    private var date: String = ""
    var project: Project? = null
    var fileBodyPart: MultipartBody.Part? = null
    var viewModel: addactionViewModel? = null
    var Daata: ArrayList<data_file>? = null
    var Daata_2: ArrayList<data__file_action>? = null
    var isValidDate = false
    var isValidTime = false
    var FlID: Int? = null
    var MAX_MEDIA_SIZE_SUPPORTED = 10
    var file2: ArrayList<MultipartBody.Part>? = null
    var followUp: FollowUp? = null
    var arrr_temp: ArrayList<data_file>? = null

    companion object {
        var Flag = false
        var Counter = 5
    }



    var adapter_file: Adapter_File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_action)
        viewModel = ViewModelProviders.of(this).get(addactionViewModel::class.java)
        Daata = ArrayList<data_file>()
        Daata_2 = ArrayList<data__file_action>()
        arrr_temp = ArrayList<data_file>()
        adapter_file = Adapter_File()
        adapter_file?.array_data = Daata as ArrayList<data_file>
        recy_files.adapter = adapter_file
        adapter_file?.Click(object :Adapter_File.D{
            override fun D(d: data__file_action,i:Int) {
                 if (Flag)
                 {
                     Del_file(d,i)
                 }
            }

        })

//        viewModel?.liveData?.observeForever {
//            Log.i("avsvmksmsWF22","eee")
//            adapter_file?.array_data=it
//            adapter_file?.notifyDataSetChanged()
//        }
        project = intent.extras?.getParcelable("project")
        Log.i("scvvf", project?.id)
        edt_til.requestFocus()
        if (intent.getBooleanExtra("IsEdit", false)) {
            Log.i("glsfksnvsnv", "A")
            Flag = true
            followUp = intent.getBundleExtra("Data_project").getSerializable("folloup") as FollowUp?
            edt_til.setText(followUp?.actionTitle)
            edt_description.setText(followUp?.description)
            selectedActionTime = followUp?.actionTime.toString()
            Log.i("dvsdvk", selectedActionTime)
            Log.i("dvsdvk", followUp?.actionDatePersian.toString())
            txt_action_time.setText(followUp?.actionTime.toString())
            txt_action_date.setText(followUp?.actionDatePersian.toString())
            selectedActionDate = followUp?.actionDatePersian.toString()
            isValidDate = true
            isValidTime = true
//            followUp?.actionFiles?.forEach {
//                var i = data_file()
//                i.Name = it.fileName
//                i.Name_forshow = it.fileNameForShow
//                i.Size = it.fileSize
//                arrr_temp?.add(i)
//            }
            Counter = 5 - followUp?.actionFiles?.size!!
            Log.i("wwweeffv", Counter.toString())
            Daata_2?.addAll(followUp?.actionFiles!!)
            adapter_file?.array_data_2 = Daata_2 as ArrayList<data__file_action>
            adapter_file?.notifyDataSetChanged()
        } else {
            Log.i("glsfksnvsnv", "B")
        }



        btn_action_date.setOnClickListener {
            if (actionDate.isNotEmpty()) {
                date = actionDate
            }

            val datePickerDialog = DatePickerDialog.newInstance(
                { _, year, monthOfYear, dayOfMonth ->
                    y1 = year
                    m1 = monthOfYear + 1
                    d1 = dayOfMonth
                    actionDate = "$year/${convertTime(m1)}/${convertTime(dayOfMonth)}"
                    txt_action_date.text =
                        dayOfMonth.toString() + " " + numToMonth(m1) + " " + year

                    selectedActionDate = actionDate
                    isValidDate = true

                },
                Integer.parseInt(date.split("/")[0]),
                Integer.parseInt(date.split("/")[1]) - 1,
                Integer.parseInt(date.split("/")[2])
            )

            datePickerDialog.show(supportFragmentManager, "Datepickerdialog")

        }
        btn_addfile.setOnClickListener {
            OpenFile()
        }
        btn_action_time.setOnClickListener {
            val timePicker =
                TimePickerDialog.newInstance({ view, hourOfDay, minute ->
                    selectedActionHour = hourOfDay.toString()
                    selectedActionMinute = minute.toString()
                    H1 = hourOfDay
                    M1 = minute
                    selectedActionTime =
                        "${String.format("%02d", selectedActionHour.toInt())}:${String.format(
                            "%02d",
                            selectedActionMinute.toInt()
                        )}"

                    txt_action_time.text = selectedActionTime
                    isValidTime = true

                }, H1, M1, true)
            timePicker.show(supportFragmentManager, "")
        }
        btn_submit.setOnClickListener {
            if (!isValidDate) {
                Toast.makeText(this, "لطفا تاریخ اقدام را انتخب کنید", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (!isValidTime) {
                Toast.makeText(this, "لطفا ساعت اقدام را انتخب کنید", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (Flag) {
                sendActionFollowUp_2()
            } else {
                sendActionFollowUp()
            }

        }
        setDatePickers(SolarCalendar.currentShamsidate, "00:00")
    }

    private fun setDatePickers(date: String?, time: String?) {
        if (date != null) {
            y1 = Integer.parseInt(date.split("/")[0])
            m1 = Integer.parseInt(date.split("/")[1])
            d1 = Integer.parseInt(date.split("/")[2])
            actionDate = "$y1/${convertTime(m1)}/${convertTime(d1)}"
            selectedActionDate = actionDate
        }

        if (time != null && time.isNotEmpty() && time.length == 5 /* 00:00 Format*/) {
            H1 = Integer.parseInt(time.split(":")[0])
            M1 = Integer.parseInt(time.split(":")[1])
            selectedActionTime =
                f.format(Integer.valueOf(H1)) + ":" + f.format(Integer.valueOf(M1))
        }
    }

    fun OpenFile() {
        if (Flag) {
            if (Counter > 0 && Counter < 5) {
                Log.i("SXSCDV", Counter.toString())
                val dialog = AttachToFollowUpBottomSheet()
                dialog.show(supportFragmentManager, "")
            } else if (Counter == 0) {
                Log.i("SXSCDV", Counter.toString())
                Snackbar.make(
                    coordinator_parent,
                    "فقط میتوانید 5 فایل انتخاب کنید",
                    Snackbar.LENGTH_LONG
                ).show()
            }else if(Counter==5)
            {
                Log.i("SXSCDV", Counter.toString())
                val dialog = AttachToFollowUpBottomSheet()
                dialog.show(supportFragmentManager, "")
            }
        } else {
            if (Counter > 0) {
                Log.i("SXSCDV", Counter.toString())
                val dialog = AttachToFollowUpBottomSheet()
                dialog.show(supportFragmentManager, "")
            } else {
                Log.i("SXSCDV", Counter.toString())
                Snackbar.make(
                    coordinator_parent,
                    "فقط میتوانید 5 فایل انتخاب کنید",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

    }

    fun sendActionFollowUp() {
        Log.i("dvsdvk", selectedActionDate)
        if (isNetConnected()) {
            if (!dialog.isShowing)
                showpDialog(this)
            Log.i("scvvf", project?.id)
            Log.i("scvvf", selectedActionTime)
            Log.i("scvvf", selectedActionDate)
            file2 = ArrayList<MultipartBody.Part>()
            adapter_file?.array_data?.forEachIndexed { index, dataFile ->
                val progressRequestBody = ProgressRequestBody(
                    File(dataFile.Address),
                    "multipart/form-data",
                    object : ProgressRequestBody.UploadCallbacks {
                        override fun onProgressUpdate(percentage: Int) {

                            Log.i(TAG, "onProgress: percentage : $percentage")
                        }

                        override fun onError() {
                            Log.i(TAG, "Some Error Occurred")
                        }
                        override fun onFinish() {
                        }
                    })
                val fileBodyPart = MultipartBody.Part.createFormData(
                    "android",
                    URLEncoder.encode(dataFile.Name.toString().trim(),"utf-8"),
                    progressRequestBody
                )
                file2?.add(fileBodyPart)
            }


            val req = App.getRetofitApi()?.RegOrEditAction(
                token, RegisteroreditFollowUpModel(
                    project?.id.toString(),
                    null,
                    edt_til.text.toString(),
                    edt_description.text.toString(),
                    selectedActionTime,
                    selectedActionDate
                ), file2!!
            )
            req?.enqueue {
                onResponse = {
                    hidepDialog()
                    Log.i("dvjdvnjnvd", it.code().toString())
                    if (isResponseValid(it) == 2) {
                        var dat = it.body()
                        if (dat?.isSuccess!!) {
                            setResult(Activity.RESULT_OK)
                            finish()
                        }


                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                sendActionFollowUp()
                            }
                        })
                    }
                }
                onFailure = {
                    Log.i("dvjdvnjnvd", "Error")
                    Log.i("dvjdvnjnvd", it?.message ?: "")
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

    fun sendActionFollowUp_2() {
        Log.i("dvsdvk", selectedActionDate)
        if (isNetConnected()) {
            if (!dialog.isShowing)
                showpDialog(this)
            Log.i("scvvf", project?.id)
            Log.i("scvvf", selectedActionTime)
            Log.i("scvvf", selectedActionDate)
            val req = App.getRetofitApi()?.RegOrEditAction_2(
                token, RegisteroreditFollowUpModel_2(
                    project?.id.toString(),
                    followUp?.id.toString(),
                    edt_til.text.toString(),
                    edt_description.text.toString(),
                    selectedActionTime,
                    selectedActionDate
                )
            )
            req?.enqueue {
                onResponse = {
                    hidepDialog()
                    Log.i("dvjdvnjnvd", it.code().toString())
                    if (isResponseValid(it) == 2) {
                        var dat = it.body()
                        if (dat?.isSuccess!!) {
                            var i = Intent()
                            var i2 = Bundle()
                            i2.putSerializable("data", dat.data)
                            i2.putSerializable("data3", adapter_file?.array_data_2)
                            i.putExtra("data2", i2)
                            setResult(Activity.RESULT_OK, i)
                            finish()
                        }
                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                sendActionFollowUp_2()
                            }
                        })
                    }
                }
                onFailure = {
                    Log.i("dvjdvnjnvd", "Error")
                    Log.i("dvjdvnjnvd", it?.message ?: "")
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

    fun AddFileAction(F: File) {
        if (isNetConnected()) {
            if (!dialog.isShowing)
                showpDialog(this)
            val progressRequestBody = ProgressRequestBody(
                F,
                "multipart/form-data",
                object : ProgressRequestBody.UploadCallbacks {
                    override fun onProgressUpdate(percentage: Int) {

                        Log.i(TAG, "onProgress: percentage : $percentage")
                    }

                    override fun onError() {
                        Log.i(TAG, "Some Error Occurred")
                    }

                    override fun onFinish() {
                    }
                })
            fileBodyPart = MultipartBody.Part.createFormData(
                "android",
                URLEncoder.encode(F.name.toString().trim(),"utf-8"),
                progressRequestBody
            )
            val requestBody = RequestBody.create(MediaType.parse("text/plain"), followUp?.id ?: "")
            val req = App.getRetofitApi()?.AddFileAction(
                token, requestBody, this!!.fileBodyPart!!
            )
            req?.enqueue {
                onResponse = {
                    hidepDialog()
                    Log.i("dvjdvnjnvd", it.code().toString())
                    if (isResponseValid(it) == 2) {
                        var dat = it.body()
                        if (dat?.isSuccess!!) {
                            Log.i("dvjdvnjnvd", it.body()?.data.toString())
                            Log.i("vdghgnhnjmjmm", "Ok")
//                            var i = data_file()
//                            i.Name = F.name
//                            i.Size = getFileSize(F).toString()
//                           Counter--;
                            Daata_2?.clear()
//                            it.body()?.data?.actionFiles?.forEach {
//                                var i = data_file()
//                                i.Name = it.fileName
//                                i.Name_forshow = it.fileNameForShow
//                                i.Size = it.fileSize
//                                Daata?.add(i)
//                            }
                            Counter=5-Daata_2?.size!!
                            Daata_2=dat.data?.actionFiles
                            adapter_file?.array_data_2 =Daata_2 as ArrayList<data__file_action>
                            adapter_file?.notifyDataSetChanged()
                        }
                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                AddFileAction(F)
                            }
                        })
                    }
                }
                onFailure = {
                    Log.i("dvjdvnjnvd", "Error")
                    Log.i("dvjdvnjnvd", it?.message ?: "")
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

    fun openMediaPicker() {
        if (Flag) {
            FilePickerBuilder.instance.setMaxCount(Counter)
                .setActivityTheme(R.style.LibAppTheme2)
                .setActivityTitle("عکس یا ویدئوی خود را انتخاب کنید")
                .enableCameraSupport(true)
                .enableVideoPicker(true)
                .enableImagePicker(true)
                .showGifs(true)
                .pickPhoto(this@AddActionActivity, Constants.UPLOAD_MEDIA_REQUEST_CODE)
        } else {
            FilePickerBuilder.instance.setMaxCount(Counter)
                .setActivityTheme(R.style.LibAppTheme2)
                .setActivityTitle("عکس یا ویدئوی خود را انتخاب کنید")
                .enableCameraSupport(true)
                .enableVideoPicker(true)
                .enableImagePicker(true)
                .showGifs(true)
                .pickPhoto(this@AddActionActivity, Constants.UPLOAD_MEDIA_REQUEST_CODE)
        }


    }

    fun Del_file(S:data__file_action,I:Int)
    {
        if (isNetConnected()) {
            if (!dialog.isShowing)
                showpDialog(this)
            val req = App.getRetofitApi()?.DeleteFileAction(
                token, RemoveFileActionDto(
                    followUp?.id,S.fileName
                )
            )
            req?.enqueue {
                onResponse = {
                    hidepDialog()
                    Log.i("dvjdvnjnvd", S.fileName)
                    Log.i("dvjdvnjnvd", it.code().toString())
                    Log.i("dvjdvnjnvd", it.body().toString())
                    if (isResponseValid(it) == 2) {
                        var dat = it.body()
                        if (dat?.isSuccess!!) {
                                 Log.i("fbmfbgmbgbkmgk","Del")
                                 adapter_file?.array_data_2?.removeAt(I)
                                 adapter_file?.notifyDataSetChanged()
                                 Counter=Counter+1
                        }
                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                Del_file(S,I)
                            }
                        })
                    }
                }
                onFailure = {
                    Log.i("dvjdvnjnvd", "Error")
                    Log.i("dvjdvnjnvd", it?.message ?: "")
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

    fun openVoiceRecorder() {
        val filePath =
            "${Environment.getExternalStorageDirectory()}${File.separator}kasbAudioRecorded.wav"
        val color = resources.getColor(R.color.colorPrimaryDark)
        AndroidAudioRecorder.with(this)
            // Required
            .setFilePath(filePath)
            .setColor(color)
            .setRequestCode(Constants.RECORD_AUDIO_REQUEST_CODE)
            // Optional
            .setSource(AudioSource.MIC)
            .setChannel(AudioChannel.STEREO)
            .setSampleRate(AudioSampleRate.HZ_48000)
            .setAutoStart(true)
            .setKeepDisplayOn(true)
            // Start recording
            .record()
    }
    fun openFilePicker(isFaceStatus: Boolean = false) {
        Dexter.withActivity(this).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                if (report.areAllPermissionsGranted()) {
                    val zipTypes = arrayOf(".zip", ".rar")
                    val audioTypes = arrayOf(
                        ".wav",
                        ".pcm",
                        ".aiff",
                        ".mp3",
                        ".aac",
                        ".ogg ",
                        ".wma ",
                        ".flac",
                        ".alac"
                    )
                    if (Flag)
                    {
                        FilePickerBuilder.instance.setMaxCount(
                            Counter
                        )
                            .setActivityTheme(R.style.LibAppTheme2)
                            .setActivityTitle("فایلهای خود را انتخاب کنید")
                            .addFileSupport("ZIP", zipTypes)
                            .addFileSupport("APK", arrayOf(".apk"))
                            .addFileSupport("AUDIO", audioTypes)
                            .enableDocSupport(true)
                            .sortDocumentsBy(SortingTypes.name)
                            .pickFile(
                                this@AddActionActivity,89
                            )
                    }else{

                        FilePickerBuilder.instance.setMaxCount(
                            Counter
                        )
                            .setActivityTheme(R.style.LibAppTheme2)
                            .setActivityTitle("فایلهای خود را انتخاب کنید")
                            .addFileSupport("ZIP", zipTypes)
                            .addFileSupport("APK", arrayOf(".apk"))
                            .addFileSupport("AUDIO", audioTypes)
                            .enableDocSupport(true)
                            .sortDocumentsBy(SortingTypes.name)
                            .pickFile(
                                this@AddActionActivity,89
                            )
                    }


                } else {
                    if (report.isAnyPermissionPermanentlyDenied) {
//                            try {
//                                showAlertDialog(R.drawable.ic_icons_8_cancel,
//                                    0,
//                                    "با توجه به اینکه شما مجوزهای دسترسی را قبول نکردید ، امکان استفاده از اپلیکیشن وجود ندارد . لطفا از بخش تنظیمات مجوزها را تایید کنید",
//                                    object : InfoAlertDialog.onDismiss() {
//                                        fun ondismiss(dialog: InfoAlertDialog) {
//                                            val intent =
//                                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                                            val uri = Uri.fromParts("package", packageName, null)
//                                            intent.data = uri
//                                            startActivityForResult(
//                                                intent,
//                                                REQUEST_PERMISSION_SETTING
//                                            )
//                                        }
//                                    })
//                            } catch (e: Exception) {
//
//                            }

                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: List<PermissionRequest>,
                token: PermissionToken
            ) {

//                    try {
//                        showAlertDialog(
//                            R.drawable.alert_icon,
//                            0,
//                            "برای اینکه اپلیکیشن به درستی کار کند و پاسخگوی نیازهای شما باشد ، باید تمام مجوزهای دسترسی قبول کنید .",
//                            object : InfoAlertDialog.onDismiss() {
//                                fun ondismiss(dialog: InfoAlertDialog) {
//                                    dialog.dismiss()
//                                    token.continuePermissionRequest()
//                                }
//                            })
//                    } catch (e: Exception) {
//
//                    }

            }
        }).check()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.UPLOAD_MEDIA_REQUEST_CODE
            && resultCode == Activity.RESULT_OK && data != null) {
            val mediaPaths = ArrayList(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)!!)
            Log.i("sssvfgbg", mediaPaths.size.toString())
            Log.i("sssvfgbg", Counter.toString())
            Counter= Counter-mediaPaths.size
            Log.i("sssvfgbg", Counter.toString())
            mediaPaths.forEachIndexed { index, s ->
                Log.i("AASVFB","AAS"+ Counter)
                if ((getFileSize(File(mediaPaths.get(index))) / 1024 / 1024) < MAX_MEDIA_SIZE_SUPPORTED)
                {
                    if (Flag)
                    {

                        Log.i("mdmkvmkkfvkmfmkv","A")
                         if (Daata?.size!!>0)
                         {
                             var F=false
                             var i2=data_file()
                             Daata_2?.forEach {
                                 if (it.fileNameForShow.equals(File(mediaPaths.get(index)).name))
                                 {
                                     F=true
                                     Log.i("dgnfhmnhgjm","B")
                                     Counter++

                                 }else{
                                     var f_2=File(mediaPaths.get(index))
                                     i2.Name=f_2.name
                                     i2.Name_forshow=f_2.name
                                     i2.Address=f_2.path
                                     i2.Size= getFileSize(f_2).toString()
                                     i2.Sel="B"
                                 }
                             }
                             if (!F)
                             {
                                 var f_2=File(mediaPaths.get(index))
                                 i2.Name=f_2.name
                                 i2.Name_forshow=f_2.name
                                 i2.Address=f_2.path
                                 i2.Size= getFileSize(f_2).toString()
                                 i2.Sel="B"
                                 AddFileAction(File(i2.Address))
                             }
//                             if (i2.Sel.equals("B"))
//                             {
//                                 Log.i("dvdvfbnnnn",i2.Name)
////                            Counter--;
//
////                                 Daata?.add(i2)
////                                 adapter_file?.array_data= Daata as ArrayList<data_file>
////                                 adapter_file?.notifyDataSetChanged()
//                             }
                         }else{
                             var f=File(mediaPaths.get(index))
                             AddFileAction(f)
                         }
                    }else{
                        Log.i("mdmkvmkkfvkmfmkv","B")
                        if (Daata?.size!! >0)
                        {
                            var i2=data_file()
                            var Flaing=false
                            var f_2=File(mediaPaths.get(index))
                            Daata?.forEach {
                                Log.i("vmdvmknimampar",File(mediaPaths.get(index)).name)
                                Log.i("vmdvmknimampar",it.Name_forshow)
                                if (it.Name_forshow.equals(File(mediaPaths.get(index)).name))
                                {
                                    Log.i("nvnvnvnd","FIND")
                                    Flaing=true
                                    Log.i("dgnfhmnhgjm","B")
                                    Counter++
                                }else{
                                    Log.i("dgnfhmnhgjm","A")
                                    i2.Address=f_2.path
                                    i2.Name=f_2.name
                                    i2.Name_forshow=f_2.name
                                    i2.Sel="B"
                                }
                            }


                            if (!Flaing)
                            {
                                i2.Address=f_2.path
                                i2.Name=f_2.name
                                i2.Name_forshow=f_2.name
                                i2.Sel="B"
                                Daata?.add(i2)
                                adapter_file?.array_data= Daata as ArrayList<data_file>
                                adapter_file?.notifyDataSetChanged()
                            }
//                            if (i2.Sel.equals("B"))
//                            {
//                                Log.i("dvdvfbnnnn",i2.Name)
////                            Counter--;
//
//                            }
                        }else{
                            var f=File(mediaPaths.get(index))
                            var i=data_file()
                            i.Name=f.name
                            i.Name_forshow=f.name
                            i.Address=f.path
                            Log.i("dvdvfbnnnn",i.Name)
//                        Counter--;
                            Daata?.add(i)
                            adapter_file?.array_data= Daata as ArrayList<data_file>
                            adapter_file?.notifyDataSetChanged()
                        }
                    }

                }
                else{
                    Counter++;
                }
            }
        }
       else if (requestCode == 89 && resultCode == Activity.RESULT_OK && data != null) {
//            val filePaths = ArrayList(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS)!!)
//            val mediaPaths = ArrayList(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)!!)
            val mediaPaths = ArrayList(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS)!!)
            Log.i("sssvfgbg", mediaPaths.size.toString())
            Log.i("sssvfgbg", Counter.toString())
            Counter= Counter-mediaPaths.size
            Log.i("sssvfgbg", Counter.toString())
            mediaPaths.forEachIndexed { index, s ->
                if ((getFileSize(File(mediaPaths.get(index))) / 1024 / 1024) < MAX_MEDIA_SIZE_SUPPORTED)
                {
                    if (Flag)
                    {

                        Log.i("mdmkvmkkfvkmfmkv","A")
                        if (Daata?.size!!>0)
                        {
                            var F=false
                            var i2=data_file()
                            Daata_2?.forEach {
                                if (it.fileNameForShow.equals(File(mediaPaths.get(index)).name))
                                {
                                    F=true
                                    Log.i("dgnfhmnhgjm","B")
                                    Counter++

                                }else{
                                    var f_2=File(mediaPaths.get(index))
                                    i2.Name=f_2.name
                                    i2.Name_forshow=f_2.name
                                    i2.Address=f_2.path
                                    i2.Size= getFileSize(f_2).toString()
                                    i2.Sel="B"
                                }
                            }
                            if (!F)
                            {
                                var f_2=File(mediaPaths.get(index))
                                i2.Name=f_2.name
                                i2.Name_forshow=f_2.name
                                i2.Address=f_2.path
                                i2.Size= getFileSize(f_2).toString()
                                i2.Sel="B"
                                AddFileAction(File(i2.Address))
                            }
//                             if (i2.Sel.equals("B"))
//                             {
//                                 Log.i("dvdvfbnnnn",i2.Name)
////                            Counter--;
//
////                                 Daata?.add(i2)
////                                 adapter_file?.array_data= Daata as ArrayList<data_file>
////                                 adapter_file?.notifyDataSetChanged()
//                             }
                        }else{
                            var f=File(mediaPaths.get(index))
                            AddFileAction(f)
                        }
                    }else{
                        Log.i("mdmkvmkkfvkmfmkv","B")
                        if (Daata?.size!! >0)
                        {
                            var i2=data_file()
                            var Flaing=false
                            var f_2=File(mediaPaths.get(index))
                            Daata?.forEach {
                                Log.i("vmdvmknimampar",File(mediaPaths.get(index)).name)
                                Log.i("vmdvmknimampar",it.Name_forshow)
                                if (it.Name_forshow.equals(File(mediaPaths.get(index)).name))
                                {
                                    Log.i("nvnvnvnd","FIND")
                                    Flaing=true
                                    Log.i("dgnfhmnhgjm","B")
                                    Counter++
                                }else{
                                    Log.i("dgnfhmnhgjm","A")
                                    i2.Address=f_2.path
                                    i2.Name=f_2.name
                                    i2.Name_forshow=f_2.name
                                    i2.Sel="B"
                                }
                            }


                            if (!Flaing)
                            {
                                i2.Address=f_2.path
                                i2.Name=f_2.name
                                i2.Name_forshow=f_2.name
                                i2.Sel="B"
                                Daata?.add(i2)
                                adapter_file?.array_data= Daata as ArrayList<data_file>
                                adapter_file?.notifyDataSetChanged()
                            }
//                            if (i2.Sel.equals("B"))
//                            {
//                                Log.i("dvdvfbnnnn",i2.Name)
////                            Counter--;
//
//                            }
                        }else{
                            var f=File(mediaPaths.get(index))
                            var i=data_file()
                            i.Name=f.name
                            i.Name_forshow=f.name
                            i.Address=f.path
                            Log.i("dvdvfbnnnn",i.Name)
//                        Counter--;
                            Daata?.add(i)
                            adapter_file?.array_data= Daata as ArrayList<data_file>
                            adapter_file?.notifyDataSetChanged()
                        }
                    }

                }
                else{
                    Counter++;
                }
            }
        }
        else if (requestCode == Constants.RECORD_AUDIO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val filePath =
                "${Environment.getExternalStorageDirectory()}${File.separator}kasbAudioRecorded.wav"
               val recordedFile = File(filePath)
                 Counter= Counter-1
                if ((getFileSize(recordedFile) / 1024 / 1024) < MAX_MEDIA_SIZE_SUPPORTED)
                {
                    if (Flag)
                    {

                        Log.i("mdmkvmkkfvkmfmkv","A")
                        if (Daata?.size!!>0)
                        {
                            var F=false
                            var i2=data_file()
                            Daata_2?.forEach {
                                if (it.fileNameForShow.equals(recordedFile.name))
                                {
                                    F=true
                                    Log.i("dgnfhmnhgjm","B")
                                    Counter++

                                }else{
                                    var f_2=recordedFile
                                    i2.Name=f_2.name
                                    i2.Name_forshow=f_2.name
                                    i2.Address=f_2.path
                                    i2.Size= getFileSize(f_2).toString()
                                    i2.Sel="B"
                                }
                            }
                            if (!F)
                            {
                                var f_2=recordedFile
                                i2.Name=f_2.name
                                i2.Name_forshow=f_2.name
                                i2.Address=f_2.path
                                i2.Size= getFileSize(f_2).toString()
                                i2.Sel="B"
                                AddFileAction(File(i2.Address))
                            }
//                             if (i2.Sel.equals("B"))
//                             {
//                                 Log.i("dvdvfbnnnn",i2.Name)
////                            Counter--;
//
////                                 Daata?.add(i2)
////                                 adapter_file?.array_data= Daata as ArrayList<data_file>
////                                 adapter_file?.notifyDataSetChanged()
//                             }
                        }else{
                            var f=recordedFile
                            AddFileAction(f)
                        }
                    }else{
                        Log.i("mdmkvmkkfvkmfmkv","B")
                        if (Daata?.size!! >0)
                        {
                            var i2=data_file()
                            var Flaing=false
                            var f_2=recordedFile
                            Daata?.forEach {
                                Log.i("vmdvmknimampar",f_2.name)
                                Log.i("vmdvmknimampar",it.Name_forshow)
                                if (it.Name_forshow.equals(f_2.name))
                                {
                                    Log.i("nvnvnvnd","FIND")
                                    Flaing=true
                                    Log.i("dgnfhmnhgjm","B")
                                    Counter++
                                }else{
                                    Log.i("dgnfhmnhgjm","A")
                                    i2.Address=f_2.path
                                    i2.Name=f_2.name
                                    i2.Name_forshow=f_2.name
                                    i2.Sel="B"
                                }
                            }


                            if (!Flaing)
                            {
                                i2.Address=f_2.path
                                i2.Name=f_2.name
                                i2.Name_forshow=f_2.name
                                i2.Sel="B"
                                Daata?.add(i2)
                                adapter_file?.array_data= Daata as ArrayList<data_file>
                                adapter_file?.notifyDataSetChanged()
                            }
//                            if (i2.Sel.equals("B"))
//                            {
//                                Log.i("dvdvfbnnnn",i2.Name)
////                            Counter--;
//
//                            }
                        }else{
                            var f=File(filePath)
                            var i=data_file()
                            i.Name=f.name
                            i.Name_forshow=f.name
                            i.Address=f.path
                            Log.i("dvdvfbnnnn",i.Name)
//                        Counter--;
                            Daata?.add(i)
                            adapter_file?.array_data= Daata as ArrayList<data_file>
                            adapter_file?.notifyDataSetChanged()
                        }
                    }

                }
                else{
                    Counter++;
                }

            }
        }
    override fun onBackPressed() {
        Counter=5
        Flag=false
        var i = Intent()
        var i2 = Bundle()
        i2.putSerializable("data3", adapter_file?.array_data_2)
        i.putExtra("data2", i2)
        setResult(Activity.RESULT_OK, i)
        super.onBackPressed()
    }
    }

