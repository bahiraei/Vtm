package ir.adak.Vect.Adapters

import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import ir.adak.Vect.Data.Models.UserModel
import ir.adak.Vect.R
import ir.adak.Vect.Utils.Constants
import kotlinx.android.synthetic.main.selected_user_item_layout.view.*
import kotlinx.android.synthetic.main.selected_user_item_layout.view.img_avatar
import kotlinx.android.synthetic.main.selected_user_item_layout.view.txt_users_name


class SelectedUsersRecyclerViewAdapter(
    private val items: MutableList<UserModel>?
) : RecyclerView.Adapter<SelectedUsersRecyclerViewAdapter.ViewHolder>() {


    private lateinit var onUserChange: OnUsersChange

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.selected_user_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items?.get(position)
        holder.bind(item)
    }


    fun addOne(item: UserModel?) {
        this.items?.add(item!!)
        notifyItemInserted(itemCount - 1)
    }


    fun updateList(items: MutableList<UserModel>?) {
        this.items?.clear()
        notifyDataSetChanged()
        items?.forEach {
            addOne(it)
        }
    }

    override fun getItemCount(): Int = items?.size ?: 0

    fun deleteAt(position: Int) {
        items?.removeAt(position)
        notifyItemRemoved(position)
    }

    fun deleteById(id: Int) {
        with(items!!.iterator()) {
            forEach {
                if (it.orgLevelId == id) {
                    remove()
                }
            }
        }
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        init {
            mView.remove_icon.setOnClickListener {
                onUserChange.onUserDeleted(items!![adapterPosition], adapterPosition)
                items.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }

        }

        fun bind(item: UserModel?) {
            mView.txt_users_name.text = item?.fullName

            if (!item?.profileImage.isNullOrEmpty()) {
                Glide.with(mView.context)
                    .load(Constants.AVATAR_BASE_URL + item?.profileImage)
                    .into(mView.img_avatar)
            }

//            if (adapterPosition == 0) {
//                val params = mView.layoutParams as RecyclerView.LayoutParams
//                params.marginEnd = dp2px(16)
//                params.marginStart = dp2px(0)
//                mView.layoutParams = params
//            } else if (adapterPosition == itemCount - 1) {
//                val params = mView.layoutParams as RecyclerView.LayoutParams
//                params.marginStart = dp2px(16)
//                params.marginEnd = dp2px(0)
//                mView.layoutParams = params
//            } else {
//                val params = mView.layoutParams as RecyclerView.LayoutParams
//                params.marginStart = dp2px(0)
//                params.marginEnd = dp2px(0)
//                mView.layoutParams = params
//            }
        }

        fun dp2px(px: Int): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, px.toFloat(), mView.context.resources
                    .displayMetrics
            ).toInt()
        }

    }

    fun setOnUserChangedListener(onUsersChange: OnUsersChange) {
        this.onUserChange = onUsersChange

    }

    fun removedById(id: Int) {
        var removePosition = -1
        items?.forEachIndexed { index, userModel ->
            if (userModel.orgLevelId == id) {
                removePosition = index
            }
        }
        items?.removeAll { it.orgLevelId == id }
        notifyItemRemoved(removePosition)
    }


    interface OnUsersChange {
        fun onUserDeleted(
            userModel: UserModel?,
            position: Int
        )
    }
}
