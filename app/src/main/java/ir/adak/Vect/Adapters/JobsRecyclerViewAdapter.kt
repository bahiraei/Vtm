package ir.adak.Vect.Adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.adak.Vect.Data.Enums.PeigiriChangeEnum
import ir.adak.Vect.Data.Enums.ProjectPriority
import ir.adak.Vect.Data.Enums.ProjectStatus
import ir.adak.Vect.Data.Models.Project
import ir.adak.Vect.Listeners.OnItemLongClicked
import ir.adak.Vect.R
import kotlinx.android.synthetic.main.job_item_layout.view.*


class JobsRecyclerViewAdapter(var onItemClickedListener: OnItemClickedListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: MutableList<Project>? = mutableListOf()
    private var isLoadingAdded = false
    var recyclerView: RecyclerView? = null

    private val NORMAL_VIEW_TYPE = 11
    private val LOADING_VIEW_TYPE = 22
    var context: Context? = null

    var onItemLongClicked: OnItemLongClicked? = null

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
                    .inflate(R.layout.job_item_layout, parent, false)
                return NormalViewHolder(view)
            }
            LOADING_VIEW_TYPE -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.loadng_item_layout, parent, false)
                return LoadingViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.job_item_layout, parent, false)
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
                        holder.itemView.img_highlight_project.alpha = 1f
                    } else {
                        holder.itemView.img_highlight_project.alpha = 0f
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

    fun addAll(projects: List<Project>?) {
        projects?.forEach {
            addItem(it)
        }
//        val currentSize = itemCount
//        this.items?.addAll(projects ?: mutableListOf())
//        notifyItemRangeInserted(currentSize, itemCount - 1)
    }


    fun addItem(item: Project) {
        items?.add(item)
        recyclerView?.post { notifyItemInserted(itemCount - 1) }

    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        val loadingItem = Project(loading = true)
        addItem(loadingItem)
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            removeItem(getItem(0))
        }
    }

    private fun removeItem(item: Project?) {
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

    fun getItem(position: Int): Project? {
        return items?.get(position)
    }

    fun updateList(projects: List<Project>?) {
        this.items = projects?.toMutableList()
        notifyDataSetChanged()
    }

    inner class NormalViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        init {

            mView.setOnLongClickListener {
                if (items!![adapterPosition].isMyProject) {
                    if (items!![adapterPosition].projectStatus != ProjectStatus.Closed.value) {
                        onItemLongClicked?.OnItemLongClicked(adapterPosition)
                    }
                }
                true
            }

            mView.setOnClickListener {
                if (items != null && items!!.size > 0 && adapterPosition != -1) {
                    onItemClickedListener.OnItemCLicked(
                        adapterPosition,
                        items!![adapterPosition].id
                    )
                }
            }
        }

        fun bind(item: Project?) {

            if (adapterPosition == 0) {
                val params = mView.layoutParams as RecyclerView.LayoutParams
                params.topMargin = dp2px(16)
                mView.layoutParams = params
            } else {
                val params = mView.layoutParams as RecyclerView.LayoutParams
                params.topMargin = dp2px(0)
                mView.layoutParams = params
            }



            var vv=mView.img_job_type.drawable
            if (!item?.projectTypeColor.isNullOrEmpty()&&!item?.projectTypeColor.equals("null"))
            {
                vv.setColorFilter(Color.parseColor(item?.projectTypeColor), PorterDuff.Mode.SRC_ATOP)
            }




            mView.txt_title.text = item?.title
            if(!item?.mojryOrgLevelId.toString().isNullOrEmpty()&&!item?.mojryOrgLevelId.toString().equals("null"))
            {
                mView.textView14.setText("مجری : ${item?.mojryFullName}  ${item?.mojryOrgLevelTitle}")
            }else{
                mView.textView14.setText("مجری : نامشخص")
            }
            if (item?.meetingId != null) {
                mView.from_meeting_layout.visibility = View.VISIBLE
                if (item.projectStatus == ProjectStatus.Deactive.value) {
                    mView.from_meeting_layout.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor("#707070"))
                } else {
                    mView.from_meeting_layout.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor("#284FF4"))
                }
            } else {
                mView.from_meeting_layout.visibility = View.GONE
            }
            when (item?.priority) {
                ProjectPriority.VeryImportant.value -> {
                    mView.txt_priority.text = "خیلی مهم"
                    mView.txt_priority.setTextColor(Color.parseColor("#FF0000"))
                    mView.img_priority_background.setBackgroundResource(R.drawable.very_important_priority_background_shape)
                }
                ProjectPriority.Important.value -> {
                    mView.txt_priority.text = "مهم"
                    mView.txt_priority.setTextColor(Color.parseColor("#F57333"))
                    mView.img_priority_background.setBackgroundResource(R.drawable.important_priority_background_shape)
                }
                ProjectPriority.Normal.value -> {
                    mView.txt_priority.text = "عادی"
                    mView.txt_priority.setTextColor(Color.parseColor("#278FF7"))
                    mView.img_priority_background.setBackgroundResource(R.drawable.normal_priority_background_shape)
                }
            }
            mView.txt_username.text = item?.personFullName
            mView.txt_semat.text = item?.orgLevelTitle
            mView.txt_start_date.text = item?.persianStartDate

            mView.txt_end_date.text = item?.persianEndDate
            mView.roundCornerProgressBar.progress = item?.progress?.toFloat() ?: 0f
            mView.txt_progress.text = "${item?.progress ?: 0}%"
            if (item?.persianEndDate.toString().isNullOrBlank())
            {
                mView.txt_end_date.setText("نامشخص")
            }else{
                mView.txt_end_date.setText(item?.persianEndDate)
            }
//            mView.img_job_type.imageTintList =
//                        ColorStateList.valueOf(Color.parseColor("#284FF4"))

                    mView.txt_job_type.text =  item?.projectTypeName
                         mView.txt_job_type.setTextColor(Color.WHITE)
//            when (item?.projectType) {
//            when (item?.projectType) {
//                ProjectType.Job.value -> {
//                    mView.img_job_type.imageTintList =
//                        ColorStateList.valueOf(Color.parseColor("#284FF4"))
//                    mView.txt_job_type.text = "کار"
//                    mView.txt_job_type.setTextColor(Color.WHITE)
//                }
//                ProjectType.Incident.value -> {
//                    mView.img_job_type.imageTintList =
//                        ColorStateList.valueOf(Color.parseColor("#FF0000"))
//                    mView.txt_job_type.text = "حادثه"
//                    mView.txt_job_type.setTextColor(Color.WHITE)
//                }
//                ProjectType.Purpose.value -> {
//                    mView.img_job_type.imageTintList =
//                        ColorStateList.valueOf(Color.parseColor("#2DC76D"))
//                    mView.txt_job_type.text = "هدف"
//                    mView.txt_job_type.setTextColor(Color.WHITE)
//                }
//                ProjectType.Project.value -> {
//                    mView.img_job_type.imageTintList =
//                        ColorStateList.valueOf(Color.parseColor("#19A8FC"))
//                    mView.txt_job_type.text = "پروژه"
//                    mView.txt_job_type.setTextColor(Color.WHITE)
//                }
//                ProjectType.Problem.value -> {
//                    mView.img_job_type.imageTintList =
//                        ColorStateList.valueOf(Color.parseColor("#f57333"))
//                    mView.txt_job_type.text = "مشکل"
//                    mView.txt_job_type.setTextColor(Color.WHITE)
//                }
//                ProjectType.Offer.value -> {
//                    mView.img_job_type.imageTintList =
//                        ColorStateList.valueOf(Color.parseColor("#d128f4"))
//                    mView.txt_job_type.text = "پیشنهاد"
//                    mView.txt_job_type.setTextColor(Color.WHITE)
//                }
//            }


            when (item?.projectStatus) {
                ProjectStatus.Active.value -> {

                    if (item.isExpired) {
                        mView.txt_end_date_title.setTextColor(Color.parseColor("#FF0000"))
                        mView.txt_end_date.setTextColor(Color.parseColor("#FF0000"))
                        mView.roundCornerProgressBar.progressColor = Color.parseColor("#FF0000")
                        mView.roundCornerProgressBar.progressBackgroundColor =
                            Color.parseColor("#FEEAEC")
                        mView.txt_progress.setTextColor(Color.parseColor("#FF0000"))
                    } else {
                        mView.txt_end_date_title.setTextColor(Color.parseColor("#707070"))
                        mView.txt_end_date.setTextColor(Color.parseColor("#707070"))
                        mView.roundCornerProgressBar.progressColor = Color.parseColor("#284FF4")
                        mView.roundCornerProgressBar.progressBackgroundColor =
                            Color.parseColor("#E7ECFF")
                        mView.txt_progress.setTextColor(Color.parseColor("#284FF4"))
                    }

                    mView.img_start_date.setImageResource(R.drawable.blue_border_circle_shape)
                    mView.img_end_date.setImageResource(R.drawable.blue_circle_shape)
                    mView.img_date_line.setImageResource(R.drawable.blue_line_shape)


                }
                ProjectStatus.Deactive.value -> {
                    mView.txt_end_date_title.setTextColor(Color.parseColor("#707070"))
                    mView.txt_end_date.setTextColor(Color.parseColor("#707070"))
                    mView.img_job_type.imageTintList =
                        ColorStateList.valueOf(Color.parseColor("#707070"))
                    mView.roundCornerProgressBar.progressBackgroundColor =
                        Color.parseColor("#E7E7E7")
                    mView.roundCornerProgressBar.progressColor = Color.parseColor("#707070")
                    mView.txt_progress.setTextColor(Color.parseColor("#707070"))
                    mView.img_priority_background.setBackgroundResource(R.drawable.priority_background_shape_deactive)
                    mView.txt_priority.setTextColor(Color.parseColor("#707070"))
                    mView.img_start_date.setImageResource(R.drawable.grey_border_circle_shape)
                    mView.img_end_date.setImageResource(R.drawable.grey_circle_shape)
                    mView.img_date_line.setImageResource(R.drawable.grey_line_shape)

                }
                ProjectStatus.Closed.value -> {
                    mView.txt_end_date_title.setTextColor(Color.parseColor("#2DC76D"))
                    mView.txt_end_date.setTextColor(Color.parseColor("#2DC76D"))
                    mView.roundCornerProgressBar.progressColor = Color.parseColor("#2DC76D")
                    mView.roundCornerProgressBar.progressBackgroundColor =
                        Color.parseColor("#CDF3DC")
                    mView.txt_progress.setTextColor(Color.parseColor("#2DC76D"))
                    mView.img_start_date.setImageResource(R.drawable.blue_border_circle_shape)
                    mView.img_end_date.setImageResource(R.drawable.blue_circle_shape)
                    mView.img_date_line.setImageResource(R.drawable.blue_line_shape)
                }
            }

            if (item?.projectStatus == ProjectStatus.Closed.value) {
                mView.img_done_task.visibility = View.VISIBLE
            } else {
                mView.img_done_task.visibility = View.GONE
            }

            if (items!![adapterPosition].isHighlight) {
                mView.img_highlight_project.alpha = 1f
            } else {
                mView.img_highlight_project.alpha = 0f
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

    interface OnItemClickedListener {
        fun OnItemCLicked(position: Int, projectId: String?)
    }
}
