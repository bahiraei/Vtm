package ir.adak.Vect.Adapters

import android.app.Activity
import android.content.Intent
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.adak.Vect.Data.Models.partners
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.EditUserActivity
import kotlinx.android.synthetic.main.users_item_layout_2.view.*


class UsersAdapter(var C :Activity) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {
    private var items: ArrayList<partners> ?=null
    init {
        items=ArrayList<partners>()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.users_item_layout, parent, false)
            .inflate(R.layout.users_item_layout_2, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items!![position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items?.size!!

    fun updateList(personel: ArrayList<partners>?) {
        this.items = personel ?:ArrayList<partners>()
        notifyDataSetChanged()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        fun bind(item: partners?) {

//            if (!item?.profileImage.isNullOrEmpty()) {
//                Glide.with(mView.context)
//                    .load(Constants.AVATAR_BASE_URL + item?.profileImage)
//                    .into(mView.img_avatar)
//            }
            mView.imageView52.setOnClickListener {
                var vs= Intent(C, EditUserActivity::class.java)
                vs.putExtra("data_2",item)
                C.startActivityForResult(vs,45)
            }



            mView.textView35.setText(item?.firstName+" "+item?.lastName)
            mView.textView37.setText(item?.personId)
            mView.textView38.setText(item?.lastLoginDate)
            mView.textView36.setText(item?.orgTitle)

            if (!item?.isConfirmSms!!)
            {
                mView.erttx.setBackgroundResource(R.drawable.bbb_users_2)
            }


//            mView.txt_users_first_name.text = item?.firstName
//            mView.txt_users_last_name.text = item?.lastName+"  "+item?.orgTitle
//            mView.txt_phone_number.text = item?.phone
//            mView.txt_personel_number.text = item?.personId
//            mView.textView17.setText("آخزین ورود: ${item?.lastLoginDate}")
        }

        fun dp2px(px: Int): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, px.toFloat(), mView.context.resources
                    .displayMetrics
            ).toInt()
        }

    }
}
