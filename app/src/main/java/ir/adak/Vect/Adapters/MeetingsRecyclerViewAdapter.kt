package ir.adak.Vect.Adapters

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.ImageViewCompat
import ir.adak.Vect.Data.Enums.PeigiriChangeEnum
import ir.adak.Vect.Data.Models.Meeting
import ir.adak.Vect.Listeners.OnItemLongClicked
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.MeetingActivity.MeetingActivity
import ir.adak.Vect.Utils.Constants
import kotlinx.android.synthetic.main.meeting_item_layout.view.*


class MeetingsRecyclerViewAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var isForChoosing = false
    var items: MutableList<Meeting>? = mutableListOf()
    private var isLoadingAdded = false
    var recyclerView: RecyclerView? = null

    private val NORMAL_VIEW_TYPE = 11
    private val LOADING_VIEW_TYPE = 22
    var context: Context? = null

    var onItemLongClicked: OnItemLongClicked? = null
    var onItemClicked: OnItemClicked? = null

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1 && isLoadingAdded) LOADING_VIEW_TYPE
        else NORMAL_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View? = null
        context = parent.context
        when (viewType) {
            NORMAL_VIEW_TYPE -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.meeting_item_layout, parent, false)
                return NormalViewHolder(view)
            }
            LOADING_VIEW_TYPE -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.loadng_item_layout, parent, false)
                return LoadingViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.meeting_item_layout, parent, false)
                return NormalViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            when (payloads[0]) {
                PeigiriChangeEnum.HIGHLIGHT -> {
                    if (items!![position].isHighlight) {
                        if (isForChoosing) {
                            holder.itemView.img_highlight_project2.alpha = 1f
                        } else {
                            holder.itemView.img_highlight_project.alpha = 1f
                        }
                    } else {
                        if (isForChoosing) {
                            holder.itemView.img_highlight_project2.alpha = 0f
                        } else {
                            holder.itemView.img_highlight_project.alpha = 0f
                        }
                    }
                }
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }

    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val item = items!![position]

        if (viewHolder.itemViewType == NORMAL_VIEW_TYPE) {
            (viewHolder as NormalViewHolder).bind(item)
        } else if (viewHolder.itemViewType == LOADING_VIEW_TYPE) {
            val params = viewHolder.itemView.layoutParams as RecyclerView.LayoutParams
            params.topMargin = dp2px(24f)
            params.bottomMargin = dp2px(24f)
            viewHolder.itemView.layoutParams = params

        }
    }

    override fun getItemCount(): Int = items?.size ?: 0

    fun dp2px(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, context?.resources?.displayMetrics
        ).toInt()
    }

    fun addAll(meetings: List<Meeting>?) {
        meetings?.forEach {
            addItem(it)
        }
//        val currentSize = itemCount
//        this.items?.addAll(projects ?: mutableListOf())
//        notifyItemRangeInserted(currentSize, itemCount - 1)
    }


    fun addItem(item: Meeting) {
        items?.add(item)
        recyclerView?.post { notifyItemInserted(itemCount - 1) }

    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        val loadingItem = Meeting(loading = true)
        addItem(loadingItem)
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            removeItem(getItem(0))
        }
    }

    private fun removeItem(item: Meeting?) {
        val position = items?.indexOf(item) ?: 0
        if (position > -1) {
            items?.removeAt(position)
            recyclerView?.post { notifyItemRemoved(position) }
        }
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        val position = itemCount - 1
        val result = getItem(position)

        if (result?.loading!!) {
            items?.removeAt(position)
            notifyItemRemoved(position)
        }

    }

    fun getItem(position: Int): Meeting? {
        return items?.get(position)
    }

    fun updateList(meetings: List<Meeting>?) {
        this.items = meetings?.toMutableList()
        notifyDataSetChanged()
    }

    inner class NormalViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        init {
            mView.setOnLongClickListener {
                if (items!![adapterPosition].isMyMeeting && !items!![adapterPosition].isHeld) {
                    onItemLongClicked?.OnItemLongClicked(adapterPosition)
                }
                true
            }

            mView.setOnClickListener {
                if (isForChoosing) {
                    onItemClicked?.OnItemClicked(adapterPosition)
                } else {
                    if (items != null && items!!.size > 0 && adapterPosition != -1) {
                        val intent = Intent(context, MeetingActivity::class.java)
                        intent.putExtra("position", adapterPosition)
                        intent.putExtra("meeting", items!![adapterPosition])
                        (context as AppCompatActivity).startActivityForResult(
                            intent,
                            Constants.UPDATE_MEETING_REQUEST_CODE
                        )
                        (context as AppCompatActivity).overridePendingTransition(
                            R.anim.slide_left_in,
                            R.anim.slide_left_out
                        )
                    }
                }

            }
        }

        fun bind(item: Meeting?) {

            if (adapterPosition == 0) {
                val params = mView.layoutParams as RecyclerView.LayoutParams
                params.topMargin = dp2px(16)
                mView.layoutParams = params
            } else {
                val params = mView.layoutParams as RecyclerView.LayoutParams
                params.topMargin = dp2px(0)
                mView.layoutParams = params
            }

            if(!item?.title.isNullOrEmpty())
            mView.txt_title.text = item?.title
            if(!item?.personelFullName.isNullOrEmpty())
            mView.txt_username.text = item?.personelFullName
            if(!item?.orgLevelTitle.isNullOrEmpty())
            mView.txt_semat.text = item?.orgLevelTitle
            if(!item?.persianHeldDate.isNullOrEmpty())
            mView.txt_meeting_date.text = item?.persianHeldDate
            if(!item?.startTime.isNullOrEmpty() || !item?.endTime.isNullOrEmpty())
            mView.txt_time.text = "${item?.startTime} تا ${item?.endTime}"

            if(!item?.location.isNullOrEmpty())
            mView.txt_meeting_location.text = item?.location

            if (item?.isHeld == true) {
                mView.constraint_meeting_layout.setBackgroundResource(R.drawable.deactivr_meetings_item_background_shape)
                mView.img_side_bar.setImageResource(R.drawable.deactive_job_side_shape)
                ImageViewCompat.setImageTintList(
                    mView.img_time_icon,
                    ColorStateList.valueOf(Color.parseColor("#707070"))
                )
                ImageViewCompat.setImageTintList(
                    mView.img_date_icon,
                    ColorStateList.valueOf(Color.parseColor("#707070"))
                )
                ImageViewCompat.setImageTintList(
                    mView.img_location_icon,
                    ColorStateList.valueOf(Color.parseColor("#707070"))
                )

            } else {
                mView.constraint_meeting_layout.setBackgroundResource(R.drawable.meetings_item_background_shape)
                mView.img_side_bar.setImageResource(R.drawable.goal_side_shape)
                ImageViewCompat.setImageTintList(
                    mView.img_time_icon,
                    ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            context?.resources!!,
                            R.color.colorPrimary, null
                        )
                    )
                )
                ImageViewCompat.setImageTintList(
                    mView.img_date_icon,
                    ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            context?.resources!!,
                            R.color.colorPrimary, null
                        )
                    )
                )
                ImageViewCompat.setImageTintList(
                    mView.img_location_icon,
                    ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            context?.resources!!,
                            R.color.colorPrimary, null
                        )
                    )
                )

            }

            if (items!![adapterPosition].isHighlight) {
                mView.img_highlight_project.alpha = 1f
                if (isForChoosing) {
                    mView.img_highlight_project2.alpha = 1f
                } else {
                    mView.img_highlight_project.alpha = 1f
                }
            } else {
                if (isForChoosing) {
                    mView.img_highlight_project2.alpha = 0f
                } else {
                    mView.img_highlight_project.alpha = 0f
                }
            }


        }

        fun dp2px(px: Int): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, px.toFloat(), mView.context.resources
                    .displayMetrics
            ).toInt()
        }

    }

    inner class LoadingViewHolder(val mView: View) : RecyclerView.ViewHolder(mView)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    fun setOnItemLongClickedListener(onItemLongClicked: OnItemLongClicked) {
        this.onItemLongClicked = onItemLongClicked
    }

    fun setOnItemClickedListener(onItemClicked: OnItemClicked) {
        this.onItemClicked = onItemClicked
    }

    interface OnItemClicked {
        fun OnItemClicked(position: Int)
    }
}
