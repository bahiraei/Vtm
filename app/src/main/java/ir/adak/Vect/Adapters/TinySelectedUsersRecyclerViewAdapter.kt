package ir.adak.Vect.Adapters

import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import ir.adak.Vect.Data.Enums.AccessToProjectEnum
import ir.adak.Vect.Data.Models.UserModel
import ir.adak.Vect.R
import ir.adak.Vect.Utils.Constants
import kotlinx.android.synthetic.main.selected_user_item_layout.view.img_avatar
import kotlinx.android.synthetic.main.tiny_selected_user_count_item_layout.view.*


class TinySelectedUsersRecyclerViewAdapter(
    private var items: MutableList<UserModel>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var onItemClickedListener: OnItemClickedListener
    private val USER_COUNT_VIEW_TYPE: Int = 11
    private val NORMAL_VIEW_TYPE: Int = 22

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> {
                USER_COUNT_VIEW_TYPE
            }
            else -> {
                NORMAL_VIEW_TYPE
            }
        }
    }

    fun updateList(items: MutableList<UserModel>?) {
        this.items?.clear()
        notifyDataSetChanged()
        items?.forEach {
            addOne(it)
            if (itemCount == 1) {
                addOne(0, UserModel("", false, -1, "",-1, AccessToProjectEnum.Full.value,"", false))
            }
        }
    }

    fun addOne(item: UserModel?) {
        this.items?.add(item!!)
        notifyDataSetChanged()
    }

    fun addOne(position: Int, item: UserModel?) {
        this.items?.add(position, item!!)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View? = null
        when (viewType) {
            USER_COUNT_VIEW_TYPE -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.tiny_selected_user_count_item_layout, parent, false)
                return UserCountViewHolder(view)
            }
            NORMAL_VIEW_TYPE -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.tiny_selected_user_item_layout, parent, false)
                return NormalViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.tiny_selected_user_item_layout, parent, false)
                return NormalViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items?.get(position)
        when (holder.itemViewType) {
            USER_COUNT_VIEW_TYPE -> {
                (holder as UserCountViewHolder).bind(item)
            }
            NORMAL_VIEW_TYPE -> {
                (holder as NormalViewHolder).bind(item)
            }
        }
    }

    override fun getItemCount(): Int = items?.size ?: 0

    inner class UserCountViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        init {
            mView.setOnClickListener {
                onItemClickedListener.onItemClicked(items!![adapterPosition])
            }
        }

        fun bind(item: UserModel?) {
            if (items?.size!! > 1)
                mView.txt_user_count.text = "${items?.size?.minus(1)}"

            (mView.layoutParams as RecyclerView.LayoutParams).marginEnd = dp2px(24)
        }

        fun dp2px(px: Int): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, px.toFloat(), mView.context.resources
                    .displayMetrics
            ).toInt()
        }

    }

    inner class NormalViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        init {
            mView.setOnClickListener {
                onItemClickedListener.onItemClicked(items!![adapterPosition])
            }
        }

        fun bind(item: UserModel?) {

            if (!item?.profileImage.isNullOrEmpty()) {
                Glide.with(mView.context)
                    .load(Constants.AVATAR_BASE_URL + item?.profileImage)
                    .into(mView.img_avatar)
            }

            if (adapterPosition == itemCount - 1) {
                (mView.layoutParams as RecyclerView.LayoutParams).marginStart = dp2px(40)
            } else {
                (mView.layoutParams as RecyclerView.LayoutParams).marginStart = dp2px(0)
            }
        }

        fun dp2px(dp: Int): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), mView.context.resources
                    .displayMetrics
            ).toInt()
        }

    }

    fun setOnItemClickedListener(onItemClickedListener: OnItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener
    }

    interface OnItemClickedListener {
        fun onItemClicked(userModel: UserModel)
    }

}
