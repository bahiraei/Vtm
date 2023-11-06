package ir.adak.Vect.Adapters

import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import ir.adak.Vect.Data.Models.UserModel
import ir.adak.Vect.Listeners.OnUserAddedAndDeleted
import ir.adak.Vect.R
import ir.adak.Vect.Utils.Constants
import kotlinx.android.synthetic.main.user_item_layout.view.*


class UsersRecyclerViewAdapter(
    var onUserAddedAndDeleted: OnUserAddedAndDeleted?
) : RecyclerView.Adapter<UsersRecyclerViewAdapter.ViewHolder>() {

    private var items: List<UserModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item_layout, parent, false)
        val viewHolder = ViewHolder(view)

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    fun updateList(personel: MutableList<UserModel>?) {
        this.items = personel ?: mutableListOf()
        notifyDataSetChanged()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        init {
            if (onUserAddedAndDeleted != null) {
                mView.setOnClickListener {
                    if (items[adapterPosition].selected) {
                        items[adapterPosition].selected = false
                        mView.setBackgroundColor(Color.parseColor("#FFFFFF"))
                        mView.selected_icon.visibility = View.GONE
                        onUserAddedAndDeleted?.OnUserDeleted(items[adapterPosition])

                    } else {
                        items[adapterPosition].selected = true
                        mView.setBackgroundColor(Color.parseColor("#F4F6FF"))
                        mView.selected_icon.visibility = View.VISIBLE
                        onUserAddedAndDeleted?.OnUserAdded(items[adapterPosition])
                    }

                }
            }
        }

        fun bind(item: UserModel?) {

            if (!item?.profileImage.isNullOrEmpty()) {
                Log.i("niamdaqe","Yes")
                Log.i("niamdaqe",Constants.AVATAR_BASE_URL + item?.profileImage)
                Glide.with(mView.context)
                    .load(Constants.AVATAR_BASE_URL + item?.profileImage)
                    .into(mView.img_avatar)
            }else{
                Log.i("niamdaqe","Not")
                Glide.with(mView.context)
                    .load(R.drawable.img_avatar)
                    .into(mView.img_avatar)
            }
            mView.txt_users_name.text = item?.fullName
            mView.txt_semat.text = item?.orgLevelTitle
            if (items[adapterPosition].selected) {
                items[adapterPosition].selected = true
                mView.setBackgroundColor(Color.parseColor("#F4F6FF"))
                mView.selected_icon.visibility = View.VISIBLE
            } else {
                items[adapterPosition].selected = false
                mView.setBackgroundColor(Color.parseColor("#FFFFFF"))
                mView.selected_icon.visibility = View.GONE
            }
        }

        fun dp2px(px: Int): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, px.toFloat(), mView.context.resources
                    .displayMetrics
            ).toInt()
        }

    }
}
