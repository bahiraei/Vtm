package ir.adak.Vect.UI.Activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import ir.adak.Vect.App
import ir.adak.Vect.Data.Models.FollowUp
import ir.adak.Vect.Data.Models.Project
import ir.adak.Vect.Data.Retrofit.Data_Accept_Action_State
import ir.adak.Vect.Data.Retrofit.Data_Action
import ir.adak.Vect.Data.Retrofit.data__file_action
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.AddActionActivity.AddActionActivity
import ir.adak.Vect.Utils.enqueue
import ir.adak.Vect.adapter_file_deal
import kotlinx.android.synthetic.main.activity_detail_action.*
import kotlinx.android.synthetic.main.layout_reject.view.*
import okhttp3.MediaType
import okhttp3.RequestBody

class DetailActionActivity : BaseActivity() {
    var project: Project? =null
    var followUp: FollowUp ?= null
    var followUp_22: Data_Action?= null
    var array_data_2= ArrayList<data__file_action>()
    var type=""

    companion object
    {
        var IdDOWN:Long=556
        var Pos:Int=0
    }
    var a:adapter_file_deal ?= null
    var broadCastReceiver:BroadcastReceiver ? =null

    public fun Status() :Int
    {
        var D =DownloadManager.Query()
        D.setFilterById(IdDOWN)
        var p:DownloadManager= getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        var dw=p.query(D)
        if (dw.moveToFirst())
        {
            var ppo=dw.getColumnIndex(DownloadManager.COLUMN_STATUS)
            var s=dw.getInt(ppo)
            return  s

        }

        return  DownloadManager.ERROR_UNKNOWN
    }
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_action)
        var I=IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
         broadCastReceiver = object : BroadcastReceiver() {
            override fun onReceive(contxt: Context?, intent: Intent?) {
                var DD =intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (DD== IdDOWN)
                {
                        if (Status()==DownloadManager.STATUS_SUCCESSFUL)
                        {
                            var f=a?.array_data?.get(Pos)
                            f?.Down=true
                            a?.array_data?.set(Pos, f!!)
                            a?.notifyDataSetChanged()
                        }
                }
            }
        }
        registerReceiver(broadCastReceiver, I)


        project=intent.getParcelableExtra("project")
        followUp= intent.getBundleExtra("folowup").getSerializable("scsc") as FollowUp?
        type=intent.getStringExtra("type")
        Log.i("dvmsvmsbb", project.toString())
        Log.i("dvmsvmsbb", followUp.toString())
        array_data_2= ArrayList<data__file_action>()
        a= adapter_file_deal(this)
        recy_files.adapter=a
        a?.Clicking(object : adapter_file_deal.Data {
            override fun Clicl(I: Int) {
                Pos = I
            }

        })

//        followUp?.actionFiles?.forEach {
//            var i =data_file()
//            i.Name=it.fileNameForShow
//            i.Size=it.fileSize
//            array_data_2.add(i)
//        }
        Log.i("vdfvfffvfbgb", followUp?.actionFiles?.size.toString())
        a?.array_data= followUp?.actionFiles!!
        a?.notifyDataSetChanged()
        btn_3.setOnClickListener {
            var i=Intent(applicationContext, AddActionActivity::class.java)
            var b=Bundle()
            b.putSerializable("folloup", followUp)
            i.putExtra("Data_project", b)
            i.putExtra("project", project)
            i.putExtra("IsEdit", true)
            startActivityForResult(i, 1354)
        }
         SetData();
//
//
//

//
//
//
        btn_4.setOnClickListener {
            var dd=Dialog(this)
            var V=LayoutInflater.from(this).inflate(R.layout.didel,null)
            dd.setContentView(V)
            dd.window?.setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
            dd.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            dd.show()
            V.btn_33.setOnClickListener {
                if (isNetConnected())
                {
                    dd.dismiss()
                    Del(followUp?.id.toString())
                }


            }
//            var d=Snackbar.make(coordinator_parent, "حذف شود?", Snackbar.LENGTH_SHORT)
////            var d=Snackbar.make(coordinator_parent, "AARR", Snackbar.LENGTH_SHORT)
//            d.setAction("بله", View.OnClickListener {
//                if (isNetConnected()) {
//                    Del(followUp?.id.toString())
//                }
//            })
//            d.show()


        }


        btn_11.setOnClickListener {
            AcOrDe(followUp?.id.toString(), 1, "")
        }
        btn_22.setOnClickListener {
//            AcOrDe(followUp?.id.toString(),2,"")
            Reject_Action()
        }








        Log.i("dvmslv", followUp?.id)
        Log.i("dvmslv", type)














    }
    private fun SetData() {


        if (type.equals("0"))
        {
            btn_4.visibility=View.VISIBLE
            btn_3.visibility=View.VISIBLE
            btn_2.visibility=View.VISIBLE
            btn_1.visibility=View.VISIBLE
        }else  if (type.equals("1"))
        {
            btn_4.visibility=View.VISIBLE
            btn_3.visibility=View.VISIBLE
            btn_2.visibility=View.GONE
            btn_1.visibility=View.GONE
        } else  if (type.equals("2"))
        {
            btn_4.visibility=View.GONE
            btn_3.visibility=View.GONE
            btn_2.visibility=View.VISIBLE
            btn_1.visibility=View.VISIBLE
        }else  if (type.equals("3"))
        {
            Log.i("vmlskvsf", "3")
            grops.isEnabled=false
            grops.visibility=View.INVISIBLE
        }

        if (followUp?.actionStatus!=null)
        {
            if(followUp?.actionStatus==1)
            {
                textView22.setText("در حال بررسی")
                textView22.setTextColor(Color.parseColor("#FF6F00"))
                textView22.setBackgroundResource(R.drawable.ffrfr)
                textView31.visibility=View.GONE
                textView32.visibility=View.GONE
                textView33.visibility=View.GONE
                textView34.visibility=View.GONE

            }else  if(followUp?.actionStatus==2)
            {
                textView22.setText("تایید شده")
                textView22.setTextColor(Color.parseColor("#66BB6A"))
                textView22.setBackgroundResource(R.drawable.ffrfr_2)
                btn_4.visibility=View.GONE
                btn_3.visibility=View.GONE
            }
            else  if(followUp?.actionStatus==3)
            {
                textView22.setText("رد شده")
                textView22.setTextColor(Color.parseColor("#D32F2F"))
                textView22.setBackgroundResource(R.drawable.ffrfr_3)
                btn_4.visibility=View.GONE
                btn_3.visibility=View.GONE
            }
        }else{
            textView22.setText("نامشخص")
            textView22.setTextColor(Color.parseColor("#FF6F00"))
            textView22.setBackgroundResource(R.drawable.ffrfr)
            grops.isEnabled=false
            grops.visibility=View.INVISIBLE
        }



        textView21.setText(followUp?.createName + " " + followUp?.orgLevelTitle)
        textView24.setText(followUp?.actionTitle)
        textView26.setText(followUp?.description)
        if (followUp?.actionFiles!=null)
        {
            if (followUp?.actionFiles!!.size>0)
            {
                textView30.setText("${followUp?.actionFiles?.size} فایل ")
            }else{
                textView30.setText("بدون فایل")
            }
        }else{
            textView30.setText("بدون فایل")
        }
        textView28.setText(followUp?.actionDatePersian + "   " + followUp?.actionTime)
        if (followUp?.actionStatus==2||followUp?.actionStatus==3)
        {
            btn_3.setVisibility(View.GONE)
            btn_4.setVisibility(View.GONE)
            grops.isEnabled=false
            grops.visibility=View.INVISIBLE
        }


        if (followUp?.actionStatus==3)
        {
            if (followUp?.rejectedDesc!=null&&!followUp?.rejectedDesc.isNullOrBlank())
            {
                Log.i("nnbnbb", "A")
                textView32.setText(followUp?.rejectedDesc)
            }else{
                Log.i("nnbnbb", "B")
                textView32.text="نامشخص"
            }


            textView33.visibility=View.GONE
            textView34.visibility=View.GONE
        }

        if (followUp?.actionStatus==2)
        {
            textView34.setText(followUp?.actionFullName + " " + followUp?.actionOrgLevelTitle)
            textView31.visibility=View.GONE
            textView32.visibility=View.GONE
        }
//        if (followUp?.actionStatus==2)
//        {
//            Log.i("dvmvncv","ASD2")
//            textView34.setText(followUp?.actionOrgLevelTitle)
//            textView31.visibility=View.GONE
//            textView32.visibility=View.GONE
//        }
    }
    private fun SetData_2() {
        textView21.setText(followUp_22?.createName + " " + followUp_22?.orgLevelTitle)
        textView24.setText(followUp_22?.actionTitle)
        textView26.setText(followUp_22?.description)
        followUp?.createName=followUp_22?.createName
        followUp?.actionTitle=followUp_22?.actionTitle
        followUp?.description=followUp_22?.description
        followUp?.actionDatePersian=followUp_22?.actionDatePersian
        followUp?.actionFullName=followUp_22?.actionFullName
        followUp?.actionTime=followUp_22?.actionTime
        if (followUp_22?.actionFiles!=null)
        {
            if (followUp?.actionFiles!!.size>0)
            {

                textView30.setText("${followUp_22?.actionFiles?.size} فایل ")
            }else{
                textView30.setText("بدون فایل")
            }
        }else{
            textView30.setText("بدون فایل")
        }
        textView28.setText(followUp_22?.actionDatePersian + "   " + followUp_22?.actionTime)
    }


    override fun onDestroy() {
        unregisterReceiver(broadCastReceiver)
        super.onDestroy()
    }
    fun  AcOrDe(Id: String, enum: Int, Desc: String)
    {
        showpDialog(this)
        var Dat= Data_Accept_Action_State()
        Dat.Status=enum
        Dat.RejectedDesc=Desc
        Dat.FollowupId=Id
        val req=App.getRetofitApi()?.Accept_Action_State(token, Dat)
        Log.i("dvvlmfv", enum.toString())
        Log.i("dvvlmfv", Id)
        req?.enqueue {
            onResponse={
                hidepDialog()
               if (isResponseValid(it)==2)
               {
                   Log.i("dvvlmfv", it.body().toString())
                   if (it.body()?.data!=null)
                   {
                       if (it.body()?.data==true)
                       {
                           setResult(Activity.RESULT_OK)
                           finish()
                       }
                   }
               }
               else if (isResponseValid(it)==1)
               {
                   AcOrDe(Id, enum, Desc)
               }
            }
            onFailure={
                Log.i("dvmslv", it?.message)
                hidepDialog()
            }
        }
    }


    fun  Del(Id: String)
    {
        showpDialog(this)
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), Id ?: "")
        val req=App.getRetofitApi()?.DeleteAction(token, requestBody)
        req?.enqueue {
            onResponse={
                hidepDialog()
                if (isResponseValid(it)==2)
                {
                    if (it.body()?.data!=null)
                    {
                        if (it.body()?.data==true)
                        {
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                    }
                }else if (isResponseValid(it)==1)
                {
                    Del(Id)
                }
            }
            onFailure={
                Log.i("dvmslv", it?.message)
                hidepDialog()
            }
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==1354&&resultCode== Activity.RESULT_OK)
        {
            if (data!=null)
            {
                Log.i("dvnsldjvnsd", "Ok")
                if (data.getBundleExtra("data2").getSerializable("data")!=null)
                {
                    followUp_22= data.getBundleExtra("data2").getSerializable("data") as Data_Action?
                    SetData_2()
                }


                array_data_2= data.getBundleExtra("data2").getSerializable("data3") as ArrayList<data__file_action>
                a?.array_data=array_data_2
                a?.notifyDataSetChanged()
                textView30.setText("${a?.array_data?.size} فایل ")
                followUp?.actionFiles=a?.array_data

            }
        }
    }



    public fun  Reject_Action()
    {
        val builder = Dialog(this)
        var layout=LayoutInflater.from(this).inflate(R.layout.layout_reject, null)
        builder.setContentView(layout)
        builder.window?.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )
        builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        builder.show()
        layout.btn_33.setOnClickListener {
            if (!layout.edt_description.text.toString().trim().isNullOrBlank())
            {
                builder.dismiss()
                AcOrDe(followUp?.id.toString(), 2, layout.edt_description.text.toString().trim())
            }

        }


    }

}