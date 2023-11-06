package ir.adak.Vect.Adapters

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.adak.Vect.Data.Enums.PeigiriChangeEnum
import ir.adak.Vect.Data.Models.ScheduledProjectModel
import ir.adak.Vect.Listeners.OnItemLongClicked
import ir.adak.Vect.R
import kotlinx.android.synthetic.main.scheduled_job_item_layout.view.*


class ScheduledJobsRecyclerViewAdapter(var onItemClickedListener: OnItemClickedListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: MutableList<ScheduledProjectModel>? = mutableListOf()
    var recyclerView: RecyclerView? = null

    var context: Context? = null

    var onItemLongClicked: OnItemLongClicked? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var view: View? = null
        context = parent.context
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.scheduled_job_item_layout, parent, false)
        return NormalViewHolder(view)

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

        (viewHolder as NormalViewHolder).bind(item)

    }

    override fun getItemCount(): Int = items?.size ?: 0

    fun dp2px(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, context?.resources?.displayMetrics
        ).toInt()
    }


    fun updateList(scheduledProjectModels: List<ScheduledProjectModel>?) {
        this.items = scheduledProjectModels?.toMutableList()
        notifyDataSetChanged()
    }

    inner class NormalViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        init {

            mView.setOnLongClickListener {
                onItemLongClicked?.OnItemLongClicked(adapterPosition)
                true
            }

//            mView.setOnClickListener {
//                if (items != null && items!!.size > 0 && adapterPosition != -1) {
//                    onItemClickedListener.OnItemCLicked(
//                        adapterPosition,
//                        items!![adapterPosition].id
//                    )
//                }
//            }
        }

        fun bind(item: ScheduledProjectModel) {
            if (adapterPosition == 0) {
                val params = mView.layoutParams as RecyclerView.LayoutParams
                params.topMargin = dp2px(16)
                mView.layoutParams = params
            } else {
                val params = mView.layoutParams as RecyclerView.LayoutParams
                params.topMargin = dp2px(0)
                mView.layoutParams = params
            }
            mView.txt_title.text = item.title
            mView.txt_scheduler_type.text = item.schedulerTitle
            mView.txt_duration.text = item.timeLenForRunningProject
            mView.txt_next_date.text = item.nextRunTitle
            mView.txt_job_type.text = item.projectTypeName
            mView.txt_job_type.setTextColor(Color.WHITE)
            mView.textView16.setText("${item.mojryFullName}  ${item.mojryOrgLevelTitle} ")
//            when (item.projectType) {
//                ProjectType.Job.value -> {
//                    if (!item.isActive) {
//                        mView.img_job_type.imageTintList =
//                            ColorStateList.valueOf(Color.parseColor("#707070"))
//                    } else {
//                        mView.img_job_type.imageTintList =
//                            ColorStateList.valueOf(Color.parseColor("#284FF4"))
//                    }
//                    mView.txt_job_type.text = "کار"
//                    mView.txt_job_type.setTextColor(Color.WHITE)
//                }
//                ProjectType.Incident.value -> {
//                    if (!item.isActive) {
//                        mView.img_job_type.imageTintList =
//                            ColorStateList.valueOf(Color.parseColor("#707070"))
//                    } else {
//                        mView.img_job_type.imageTintList =
//                            ColorStateList.valueOf(Color.parseColor("#FF0000"))
//                    }
//
//                    mView.txt_job_type.text = "حادثه"
//                    mView.txt_job_type.setTextColor(Color.WHITE)
//                }
//                ProjectType.Purpose.value -> {
//                    if (!item.isActive) {
//                        mView.img_job_type.imageTintList =
//                            ColorStateList.valueOf(Color.parseColor("#707070"))
//                    } else {
//                        mView.img_job_type.imageTintList =
//                            ColorStateList.valueOf(Color.parseColor("#2DC76D"))
//                    }
//
//                    mView.txt_job_type.text = "هدف"
//                    mView.txt_job_type.setTextColor(Color.WHITE)
//                }
//                ProjectType.Project.value -> {
//                    if (!item.isActive) {
//                        mView.img_job_type.imageTintList =
//                            ColorStateList.valueOf(Color.parseColor("#707070"))
//                    } else {
//                        mView.img_job_type.imageTintList =
//                            ColorStateList.valueOf(Color.parseColor("#19A8FC"))
//                    }
//
//                    mView.txt_job_type.text = "پروژه"
//                    mView.txt_job_type.setTextColor(Color.WHITE)
//                }
//                ProjectType.Problem.value -> {
//                    if (!item.isActive) {
//                        mView.img_job_type.imageTintList =
//                            ColorStateList.valueOf(Color.parseColor("#707070"))
//                    } else {
//                        mView.img_job_type.imageTintList =
//                            ColorStateList.valueOf(Color.parseColor("#f57333"))
//                    }
//
//                    mView.txt_job_type.text = "مشکل"
//                    mView.txt_job_type.setTextColor(Color.WHITE)
//                }
//                ProjectType.Offer.value -> {
//                    if (!item.isActive) {
//                        mView.img_job_type.imageTintList =
//                            ColorStateList.valueOf(Color.parseColor("#707070"))
//                    } else {
//                        mView.img_job_type.imageTintList =
//                            ColorStateList.valueOf(Color.parseColor("#d128f4"))
//                    }
//
//                    mView.txt_job_type.text = "پیشنهاد"
//                    mView.txt_job_type.setTextColor(Color.WHITE)
//                }
//            }

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
