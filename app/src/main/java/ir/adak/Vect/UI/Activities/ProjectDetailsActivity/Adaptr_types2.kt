package ir.adak.Vect.UI.Activities.ProjectDetailsActivity

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devs.vectorchildfinder.VectorChildFinder
import com.devs.vectorchildfinder.VectorDrawableCompat
import ir.adak.Vect.Data.Models.CreateProjectTypeCountDto
import ir.adak.Vect.R
import kotlinx.android.synthetic.main.typess.view.*

class Adaptr_types2(var V:List<CreateProjectTypeCountDto>,var Co:Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
   var D:Data ?=null
    public class v2(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var V=LayoutInflater.from(parent.context).inflate(R.layout.typess,parent,false)
        return  v2(V)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var i=V.get(position)
         holder.itemView.type.text=i.type.name
        holder.itemView.txt_jobs_count.text= i.count.toString()
        val vector = VectorChildFinder(Co, R.drawable.ic_job_circle_background, holder.itemView.img_job_icon)
        val path1: VectorDrawableCompat.VFullPath = vector.findPathByName("path1")

        holder.itemView.img_job_icon.setOnClickListener {
            if (i.type.id==-90)
            {
                if (V.get(position).count>0)
                {
                    D?.Type("A")
                }

            }else{
                if (V.get(position).count>0)
                {
                    D?.Type("B")
                }

            }
        }

        if (i.type.id==-90)
        {

        }else{
//                Log.i("ywiryeiort","#"+i.type.color.toString())
            var ss=Color.parseColor(i.type.color.toString())
//            var ss=Color.parseColor("#284FF4")
            path1.setFillColor(ss);
        }

    }

    override fun getItemCount(): Int {
         return V.size
    }

    fun  Clicl(D:Data)
    {
        this.D=D

    }

    public interface Data{
        public fun Type(S:String)
    }

}
