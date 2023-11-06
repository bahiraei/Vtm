package ir.adak.Vect.UI.Activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.adak.Vect.Data.Retrofit.data_14
import ir.adak.Vect.R
import kotlinx.android.synthetic.main.custome_hamkaran.view.*

class Adapter_Amar_hamkaran  : RecyclerView.Adapter<Adapter_Amar_hamkaran.view>() {


    var list:ArrayList<data_14> ?=null
    init {
        list=ArrayList<data_14> ()
    }
    public  class  view(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): view {
    var View=LayoutInflater.from(parent.context).inflate(R.layout.custome_hamkaran,parent,false)
        return view(View)
    }

    override fun onBindViewHolder(holder: view, position: Int) {
    var Item=list?.get(position)


        if (!Item?.fullName.isNullOrEmpty())
        {
            holder.itemView.textView69.setText(Item?.fullName)
        }



        if (Item?.allProject!=null)
        {
            holder.itemView.textView74.setText(Item?.allProject.toString())
        }

        if (Item?.creator!=null)
        {
            holder.itemView.textView92.setText(Item?.creator.toString())
        }


        if (Item?.mojry!=null)
        {
            holder.itemView.textView94.setText(Item?.mojry.toString())
        }

        if (Item?.hamkar!=null)
        {
            holder.itemView.textView96.setText(Item?.hamkar.toString())
        }


        if (Item?.activeProject!=null)
        {
            holder.itemView.textView98.setText(Item?.activeProject.toString())
        }

        if (Item?.delayProject!=null)
        {
            holder.itemView.textView100.setText(Item?.delayProject.toString())
        }


        if (Item?.endProject!=null)
        {
            holder.itemView.textView102.setText(Item?.endProject.toString())
        }



    }

    override fun getItemCount(): Int {
       return list?.size!!
    }

}
