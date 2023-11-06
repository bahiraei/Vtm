package ir.adak.Vect.UI.Activities.AddActionActivity

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.adak.Vect.Data.Retrofit.data__file_action
import ir.adak.Vect.R
import kotlinx.android.synthetic.main.vb.view.*
import java.io.File

class Adapter_File : RecyclerView.Adapter<Adapter_File.view>() {
    var array_data= ArrayList<data_file>()
    var array_data_2= ArrayList<data__file_action>()
    var dd : data__file_action?=null
    var ddd:D ? =null
    public class view(itemView: View) : RecyclerView.ViewHolder(itemView)




    public fun Click(v:D)
    {
        this.ddd=v
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): view {
        var vb=LayoutInflater.from(parent.context).inflate(R.layout.vb,parent,false)
        return view(vb)
    }

    override fun getItemCount(): Int {
        if (AddActionActivity.Flag)
        {
            return array_data_2.size
        }else
       return array_data.size
    }
    override fun onBindViewHolder(holder: view, position: Int) {
        if (AddActionActivity.Flag)
        {
              dd=array_data_2.get(position)
            var i2=(array_data_2.get(position).fileSize)?.toInt()
            var size=((i2!! / 1024) / 1024)
            if (size.toInt()>0)
            {
                Log.i("fbxknsdd", size.toString())
                holder.itemView.textView18.setText(array_data_2.get(position).fileNameForShow+"  مگابایت $size  ")
            }else{
                var dd=array_data_2.get(position)
                var i2=(array_data_2.get(position).fileSize)?.toInt()
                var size=(i2!! / 1024)
                holder.itemView.textView18.setText(size.toString() +" کیلوبایت  "+array_data_2.get(position).fileNameForShow)
                Log.i("fbxknsddbfgngn", size.toString())
            }
        }else{
            var dd=array_data.get(position)
            var size=(getFileSize(File(array_data.get(position).Address))) / 1024 / 1024
            if (size.toInt()>0)
            {
                Log.i("fbxknsdd", size.toString())
                holder.itemView.textView18.setText(array_data.get(position).Name+"  مگابایت $size  ")
            }
            else{
                var size=((getFileSize(File(array_data.get(position).Address))) / 1024).toFloat()
                holder.itemView.textView18.setText(size.toString() +" کیلوبایت  "+array_data.get(position).Name)
                Log.i("fbxknsddbfgngn", size.toString())
            }
        }




       holder.itemView.imageView7.setOnClickListener {
           if (AddActionActivity.Flag)
           {
               ddd?.D(array_data_2.get(position),position)
           }else{
               var d33=array_data.get(position)
               array_data.remove(d33!!)
               notifyDataSetChanged()
               AddActionActivity.Counter=AddActionActivity.Counter+1
           }

       }

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

   public interface  D{
       public fun D(d:data__file_action,i:Int)

   }
}
