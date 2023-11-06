package ir.adak.Vect.UI.Activities

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.adak.Vect.Data.Retrofit.data_12
import ir.adak.Vect.R
import kotlinx.android.synthetic.main.custome_amar.view.*

class Adapter_Amar : RecyclerView.Adapter<Adapter_Amar.view>() {
    var list:ArrayList<data_12> ?= null
    init {
        list=ArrayList<data_12>()
    }
    public  class view(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): view {
         var V=LayoutInflater.from(parent.context).inflate( R.layout.custome_amar,parent,false)
        return  view(V)
    }

    override fun onBindViewHolder(holder: view, position: Int) {
        var Ite=list?.get(position)


        Log.i("dkvasdasd", list?.size.toString())
        if (Ite?.userRole?.creator!=null)
        {
            Log.i("asadvgsb0",Ite?.userRole?.creator.toString())
            holder.itemView.textView84.setText(Ite?.userRole?.creator.toString())
        }


        if (Ite?.userRole?.mojry!=null)
        {
            Log.i("asadvgsb0",Ite?.userRole?.mojry.toString())
            holder.itemView.textView76.setText(Ite?.userRole?.mojry.toString())
        }



        if (Ite?.userRole?.hamkar!=null)
        {
            Log.i("asadvgsb0",Ite?.userRole?.hamkar.toString())
            holder.itemView.textView80.setText(Ite?.userRole?.hamkar.toString())
        }



        if (!Ite?.projectTypeName.isNullOrEmpty())
        {

            holder.itemView.textView65.setText(Ite?.projectTypeName)
        }



        if (!Ite?.projectTypeColor.isNullOrEmpty())
        {
            holder.itemView.textView65.setTextColor(Color.parseColor("${Ite?.projectTypeColor}"))
        }


        if (position== list?.size!! -1)
        {
           holder.itemView.imageView45.visibility=View.GONE
        }



    }

    override fun getItemCount(): Int {
       return list?.size!!
    }

}
