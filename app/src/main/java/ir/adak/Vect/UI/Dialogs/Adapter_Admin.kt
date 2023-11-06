package ir.adak.Vect.UI.Dialogs

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.BaseActivity
import ir.adak.Vect.Utils.Constants
import kotlinx.android.synthetic.main.custome_admin.view.*

class Adapter_Admin(var C:Context,var list:ArrayList<Data_Admin>) : RecyclerView.Adapter<Adapter_Admin.view>() {

    interface  DATA_2{
        fun  data_3(S:String)
    }


    var dd:DATA_2 ?=null

    fun  click(sd:DATA_2)
    {
        this.dd=sd
    }
    public  class  view(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): view {
        var vv=LayoutInflater.from(parent.context).inflate(R.layout.custome_admin,parent,false)
        return  view(vv)
    }
    override fun onBindViewHolder(holder: view, position: Int) {
        var item=list?.get(position)
        if(position==list?.size-1)
        {
            holder.itemView.view11.visibility=View.GONE
        }

        if (!item?.profile.isNullOrEmpty()) {
            Log.i("dfmlsvadv", Constants.AVATAR_BASE_URL + BaseActivity.userInfo?.profileImage)
            Glide.with(C).load(Constants.AVATAR_BASE_URL + item?.profile)
                .placeholder(R.drawable.image_placeholder)
                .into(holder.itemView.circularImageView2)
        }

        if (item.isadmin)
        {
            holder.itemView.imageView40.visibility=View.VISIBLE
        }else{
            holder.itemView.imageView40.visibility=View.GONE
        }
        holder.itemView.textView63.setText(item?.name)
        holder.itemView.textView64.setText(item?.orgleveltitle)

        holder.itemView.setOnClickListener {

            Log.i("Advsvsbsfb",position.toString())
            if (position==0)
            {
                dd?.data_3("A")
                Log.i("Advsvsbsfb","A")
            }else if (position==1){
                Log.i("Advsvsbsfb","B")
                dd?.data_3("B")
            }
        }
    }

    override fun getItemCount(): Int {
        return list?.size!!
    }

}
