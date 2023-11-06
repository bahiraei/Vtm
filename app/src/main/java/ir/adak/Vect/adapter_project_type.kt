package ir.adak.Vect

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.custome_layout_type.view.*

class adapter_project_type(var Types:ArrayList<Typess>) : RecyclerView.Adapter<adapter_project_type.view>() {
   var Hold=-1
   var Hold2=-1
    var D:Data ?=null
    public class view(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    fun Click(D:Data)
    {
        this.D=D
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): view {
     var V=LayoutInflater.from(parent.context).inflate(R.layout.custome_layout_type,parent,false)
    return view(V)
    }

    override fun onBindViewHolder(holder: view, position: Int) {
        Log.i("bvbvbd",""+position)
      var item=Types.get(position)

        if (item.Name!=null)
        {
            Log.i("testtw",item.Name)
            holder.itemView.btn_normal_priority_tab2.setText(item.Name)
        }

        holder.itemView.btn_normal_priority_tab2.setOnClickListener {

            Hold=position;
            notifyDataSetChanged()
//            if (Hold==-1)
//            {
//                Log.i("aadc","A")
//                Hold=position
//                holder.itemView.btn_normal_priority_tab2.setBackgroundResource(R.drawable.selected_normal_tab_background_shape)
//                holder.itemView.btn_normal_priority_tab2.setTextColor(Color.WHITE)
//                D?.data(item.Id.toString())
//                item.Selected=true
//            }else{
//                notifyItemMoved(position,0)

                Log.i("aadc","V")
//                var v=Types.get(Hold)
//                v.Selected=false
//                Types.set(Hold,v)
//                var v2=Types.get(position)
//                v.Selected=true
//                Types.set(position,v2)
//                Log.i("aadc","$Hold  AS")
////                notifyDataSetChanged()
//                notifyItemMoved(Hold,position)
//                Log.i("popopo","$position p")
//                Log.i("popopo","$Hold p")
//                Hold=position
//                Log.i("aadc","$Hold  AB")

//            }



//            if (!item.Selected)
//        {
//                holder.itemView.btn_normal_priority_tab2.setBackgroundResource(R.drawable.selected_normal_tab_background_shape)
//                holder.itemView.btn_normal_priority_tab2.setTextColor(Color.WHITE)
//                item.Selected=true
//
//
//            }else{
//                holder.itemView.btn_normal_priority_tab2.setBackgroundResource(R.drawable.unselected_tab_background_shape)
//                holder.itemView.btn_normal_priority_tab2.setTextColor(Color.parseColor("#A9A9A9"))
//                item.Selected=false
//
//            }
        }

//        if (item.Selected)
////        {
////            holder.itemView.btn_normal_priority_tab2.setBackgroundResource(R.drawable.selected_normal_tab_background_shape)
////            holder.itemView.btn_normal_priority_tab2.setTextColor(Color.WHITE)
////            D?.data(item.Id.toString())
////        }else{
////            holder.itemView.btn_normal_priority_tab2.setBackgroundResource(R.drawable.unselected_tab_background_shape)
////            holder.itemView.btn_normal_priority_tab2.setTextColor(Color.parseColor("#A9A9A9"))
////        }




        if (position==Hold)
        {
            holder.itemView.btn_normal_priority_tab2.setBackgroundResource(R.drawable.selected_normal_tab_background_shape)
            holder.itemView.btn_normal_priority_tab2.setTextColor(Color.WHITE)
            D?.data(item.Id.toString(),position)

        }else{
            holder.itemView.btn_normal_priority_tab2.setBackgroundResource(R.drawable.unselected_tab_background_shape)
            holder.itemView.btn_normal_priority_tab2.setTextColor(Color.parseColor("#A9A9A9"))
        }



    }

    override fun getItemCount(): Int {
         return Types.size
    }

    public  interface  Data{
        public fun  data(S:String,I:Int)
    }
}