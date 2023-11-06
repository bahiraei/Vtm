package ir.adak.Vect.UI.Activities.ProjectDetailsActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.adak.Vect.R
import kotlinx.android.synthetic.main.custome_media.view.*

class Adapter_recyMedia(var C:Context) : RecyclerView.Adapter<Adapter_recyMedia.View>() {
    var Zero="0"
    var One="0"
    var Two="0"
    var Theere="0"
    var Four="0"
    var DD:Data?=null

    public  class View(itemView: android.view.View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): View {
     var i=LayoutInflater.from(parent.context).inflate(R.layout.custome_media,parent,false)

        return  View(i)
    }

    override fun getItemCount(): Int {
      return 4
    }

    override fun onBindViewHolder(holder: View, position: Int) {
         if (position==0)
         {

              holder.itemView.txt_files_count.setText(Zero)

         }
        if (position==1)
        {
            holder.itemView.txt_pics_title.setText("فایل")
            holder.itemView.imageView8.setImageResource(R.drawable.file_icon)
            holder.itemView.txt_files_count.setText(One)
//            file_icon
        }
        if (position==2)
        {
            holder.itemView.txt_files_count.setText(Two)
            holder.itemView.txt_pics_title.setText("ویدیو")
            holder.itemView.imageView8.setImageResource(R.drawable.ic_play_button)
        }
        if (position==3)
        {
            holder.itemView.txt_files_count.setText(Four)
            holder.itemView.txt_pics_title.setText("صدا")
            holder.itemView.imageView8.setImageResource(R.drawable.ic_voice_message_microphone_button)
        }


        holder.itemView.setOnClickListener {
                if (position==0)
                {

                    if (Zero!="0")
                    {
                        DD?.Clicl(0)
                    }
                }
            if (position==1)
            {
                if (Two!="0")
                {
                    DD?.Clicl(1)
                }
            }
            if (position==2)
            {
                if (Theere!="0")
                {
                    DD?.Clicl(2)
                }
            }
            if (position==3)
            {
                if (Four!="0")
                {
                    DD?.Clicl(3)
                }
            }

        }

    }
    public fun  Clicl(D:Data)
    {
        this.DD=D;
    }

public interface  Data{
    public fun Clicl(I:Int)
}

}
