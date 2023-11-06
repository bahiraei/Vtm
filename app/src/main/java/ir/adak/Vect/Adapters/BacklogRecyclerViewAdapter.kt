package ir.adak.Vect.Adapters

import android.content.Context
import android.content.Intent
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.adak.Vect.Data.Enums.ProjectStatus
import ir.adak.Vect.Data.Models.Project
import ir.adak.Vect.Listeners.OnItemLongClicked
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.AddProjectActivity.AddProjectActivity
import kotlinx.android.synthetic.main.backlog_item_layout.view.*
import kotlinx.android.synthetic.main.new_main_activity.view.txt_backlog_title


class BacklogRecyclerViewAdapter(var onItemClickedListener: OnItemClickedListener) :
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
                    .inflate(R.layout.backlog_item_layout, parent, false)
                return NormalViewHolder(view)
            }
            LOADING_VIEW_TYPE -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.loadng_item_layout, parent, false)
                return LoadingViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.backlog_item_layout, parent, false)
                return NormalViewHolder(view)
            }
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

            mView.txt_backlog_title.text = item?.title

            mView.btn_edit.setOnClickListener {
                context?.startActivity(
                    Intent(
                        context,
                        AddProjectActivity::class.java
                    ).putExtra("project", item).putExtra("isEdit", true)
                )
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
