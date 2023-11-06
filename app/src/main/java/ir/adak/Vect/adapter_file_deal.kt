package ir.adak.Vect

import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import ir.adak.Vect.Data.Retrofit.data__file_action
import ir.adak.Vect.UI.Activities.DetailActionActivity
import ir.adak.Vect.Utils.Constants
import kotlinx.android.synthetic.main.vb_2.view.*
import java.io.File

class adapter_file_deal(var C:Context) : RecyclerView.Adapter<adapter_file_deal.view>() {
    var array_data= ArrayList<data__file_action>()
    var CC:Data ?=null
    public  class  view(itemView: View) : RecyclerView.ViewHolder(itemView)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): view {
        var vb= LayoutInflater.from(parent.context).inflate(R.layout.vb_2,parent,false)
        return view(vb)
    }

    override fun getItemCount(): Int {
     return   array_data.size
    }

    public fun Clicking(dd:Data)
    {
        this.CC
    }

    public  interface  Data{
        public fun Clicl(I:Int)
    }


    override fun onBindViewHolder(holder: view, position: Int) {
        holder.itemView.textView18.setText(array_data.get(position).fileNameForShow)
        if (File(Environment.
            getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString()+File.separator+array_data.get(position).fileName?.split("/")?.get(1)).exists())
        {
            holder.itemView.imageView13.visibility=View.GONE
        }else{
            holder.itemView.imageView13.visibility=View.VISIBLE
        }


        Log.i("vmdvk",array_data.get(position).fileExtension)


        if (array_data.get(position).fileType==-1)
        {
            holder.itemView.imageView16.setImageResource(R.drawable.file_a_24)
        }else if(array_data.get(position).fileType==1){
            holder.itemView.imageView16.setImageResource(R.drawable.img_2_24)
        }
        else if(array_data.get(position).fileType==2){
            holder.itemView.imageView16.setImageResource(R.drawable.move_3_24)
        }else if(array_data.get(position).fileType==3){
            holder.itemView.imageView16.setImageResource(R.drawable.voice_4_24)
        }





        holder.itemView.setOnClickListener {
            if (File(Environment.
                getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString()+File.separator+array_data.get(position).fileName?.split("/")?.get(1)).exists())
            {
                OpenFile(File(Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_DOWNLOADS).toString()+File.separator+array_data.get(position).fileName?.split("/")?.get(1)))
            }
        }



        holder.itemView.imageView13.setOnClickListener {
            file_download(array_data.get(position).fileName,position)
        }

    }
    fun file_download(uRl: String?,i:Int) {
        var direct = File(
            Environment.DIRECTORY_DOWNLOADS
        )



//        if (!direct.exists())
//        {
//            direct.mkdir()
//        }



        Log.i("bjjjbbjbj",direct.path)











//        Log.i("nimamoradi", File(direct.path+uRl).toString())
//
//
//
//        Log.i("bnnbnbnbnbne",uRl)
//        Log.i("nimamoradi", File(direct.path+uRl).toString())



        var b =false
//        if (!direct.exists()) {
//            Log.i("dvlmsfvmsmfv","As")
//            direct.mkdirs()
//        }



        var dds= uRl.toString().split("/").get(1)

        if (File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+File.separator+dds).exists())
        {
            Log.i("vmdmvm","A")
            b=true
        }else{
            Log.i("vmdmvm","B")
        }
        if (!b)
        {
            val mgr: DownloadManager =
                C.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            Log.i("vvnnvvbvbbcs",uRl)
            Log.i("vvnnvvbvbbcs", Environment.getExternalStorageDirectory().toString())
            val downloadUri: Uri = Uri.parse("${Constants.BASE_URL_Temp}Followup/ActionFile/"+uRl)
            val request: DownloadManager.Request = DownloadManager.Request(downloadUri
            )
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE
            )
                .setAllowedOverRoaming(false).setTitle(uRl.toString().split("/").get(1))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uRl.toString().split("/").get(1));
                   DetailActionActivity.IdDOWN=    mgr.enqueue(request)
                  CC?.Clicl(i)
        }else{
            OpenFile(File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+File.separator+uRl.toString().split("/").get(1)))

        }
    }
    final fun OpenFile(file: File) {
        if (file.exists()) {
            Log.i("ssscvb","yES")
            val uri = FileProvider.getUriForFile(
                C,
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
                C.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // show message to user
                Toast.makeText(
                    C,
                    "نرم افزار مناسبی برای باز کردن این نوع فایل پیدا نشد",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }
    fun getMimeType(url: String): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }
}