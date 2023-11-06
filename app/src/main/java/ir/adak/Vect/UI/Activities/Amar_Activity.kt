package ir.adak.Vect.UI.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import ir.adak.Vect.App
import ir.adak.Vect.Listeners.OnLoginCompleted
import ir.adak.Vect.R
import ir.adak.Vect.UI.Dialogs.MyAlertDialog
import ir.adak.Vect.Utils.enqueue
import kotlinx.android.synthetic.main.activity_amar_.*
import kotlinx.android.synthetic.main.activity_users_2.*
import okhttp3.MediaType
import okhttp3.RequestBody

class Amar_Activity : BaseActivity() {
    var adater:Adapter_Amar ?= null
    var adater_2:Adapter_Amar_hamkaran ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amar_)
        adater=Adapter_Amar()
        adater_2=Adapter_Amar_hamkaran()
        recy_itemmmms.adapter=adater
        recy_3.adapter=adater_2
        getAmar()
    }
  fun  getAmar()
    {
        if(isNetConnected()){
            if(!dialog.isShowing)
                showpDialog(this)
            Log.i("avkadvmka", "A")
              var Body=RequestBody.create(MediaType.parse("text/plain"),"1399")
            val req = App.getRetofitApi()?.GetAmarMaster(token,Body)
            req?.enqueue {
                onResponse={
                    hidepDialog()
                    Log.i("avkadvmka", it.code().toString())
                    if(isResponseValid(it) == 2){
                        val myRrsponse = it.body()?.data
                        if (myRrsponse?.statusOfMyProjects?.allProject?.creator!=null)
                        {
                            textView73.setText(myRrsponse?.statusOfMyProjects?.allProject?.creator.toString())
                        }
                        if (myRrsponse?.statusOfMyProjects?.allProject?.mojry!=null)
                        {
                            textView74.setText(myRrsponse?.statusOfMyProjects?.allProject?.mojry.toString())
                        }


                        if (myRrsponse?.statusOfMyProjects?.allProject?.hamkar!=null)
                        {
                            textView75.setText(myRrsponse?.statusOfMyProjects?.allProject?.hamkar.toString())
                        }

                        if (myRrsponse?.statusOfMyProjects?.activeProject?.creator!=null)
                        {
                            textView77.setText(myRrsponse?.statusOfMyProjects?.activeProject?.creator.toString())
                        }



                        if (myRrsponse?.statusOfMyProjects?.activeProject?.mojry!=null)
                        {
                            textView78.setText(myRrsponse?.statusOfMyProjects?.activeProject?.mojry.toString())
                        }



                        if (myRrsponse?.statusOfMyProjects?.activeProject?.creator!=null)
                        {
                            textView77.setText(myRrsponse?.statusOfMyProjects?.activeProject?.creator.toString())
                        }




                        if (myRrsponse?.statusOfMyProjects?.activeProject?.hamkar!=null)
                        {
                            textView79.setText(myRrsponse?.statusOfMyProjects?.activeProject?.hamkar.toString())
                        }



                        if (myRrsponse?.statusOfMyProjects?.delayProject?.creator!=null)
                        {
                            textView81.setText(myRrsponse?.statusOfMyProjects?.delayProject?.creator.toString())
                        }


                        if (myRrsponse?.statusOfMyProjects?.delayProject?.mojry!=null)
                        {
                            textView82.setText(myRrsponse?.statusOfMyProjects?.delayProject?.mojry.toString())
                        }


                        if (myRrsponse?.statusOfMyProjects?.delayProject?.hamkar!=null)
                        {
                            textView83.setText(myRrsponse?.statusOfMyProjects?.delayProject?.hamkar.toString())
                        }



                        if (myRrsponse?.statusOfMyProjects?.endProject?.creator!=null)
                        {
                            textView84.setText(myRrsponse?.statusOfMyProjects?.endProject?.creator.toString())
                        }



                        if (myRrsponse?.statusOfMyProjects?.endProject?.mojry!=null)
                        {
                            textView85.setText(myRrsponse?.statusOfMyProjects?.endProject?.mojry.toString())
                        }



                        if (myRrsponse?.statusOfMyProjects?.endProject?.hamkar!=null)
                        {
                            textView86.setText(myRrsponse?.statusOfMyProjects?.endProject?.hamkar.toString())
                        }





                        if (myRrsponse?.statusOfMyProjects?.projectTypes!=null)
                        {
                            Log.i("acjbaa","A")
                            Log.i("acjbaa",myRrsponse.statusOfMyProjects?.projectTypes?.size.toString())
                            adater?.list?.clear()
                            adater?.list=myRrsponse.statusOfMyProjects?.projectTypes
                            adater?.notifyDataSetChanged()
                        }else{
                            Log.i("acjbaa","B")
                            card2.visibility=View.GONE
                        }



                        if (myRrsponse?.statusOfMyProjects?.projectTypes!=null)
                        {
                            Log.i("acjbaa","A")
                            Log.i("acjbaa",myRrsponse.statusOfMyProjects?.projectTypes?.size.toString())
                            adater_2?.list=myRrsponse.statusOfHamkars
                            adater_2?.notifyDataSetChanged()
                        }else{
                            Log.i("acjbaa","B")
                            card3.visibility=View.GONE
                        }







                    }else if(isResponseValid(it) == 1){
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                getAmar()
                            }
                        })
                    }
                }
                onFailure={
                    Log.i("avkadvmka", it?.message?:"")
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
}