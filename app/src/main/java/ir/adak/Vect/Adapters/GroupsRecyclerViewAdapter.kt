//package ir.heydroid.kasb_v3.Adapters
//
//import android.content.Context
//import android.graphics.Color
//import android.util.TypedValue
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import androidx.recyclerview.widget.RecyclerView
//import com.multilevelview.MultiLevelAdapter
//import com.multilevelview.models.RecyclerViewItem
//import ir.heydroid.kasb_v3.Data.Models.GetMembersAndGroupsResponseModel
//import ir.heydroid.kasb_v3.Data.Models.GroupHeaderItem
//import ir.heydroid.kasb_v3.Data.Models.UserModel
//import ir.heydroid.kasb_v3.Listeners.OnUserAddedAndDeleted
//import ir.heydroid.kasb_v3.R
//import kotlinx.android.synthetic.main.group_item_layout.view.*
//import kotlinx.android.synthetic.main.user_item_layout.view.*
//import java.text.FieldPosition
//
//class GroupsRecyclerViewAdapter(
//    var items: MutableList<RecyclerViewItem>,
//    var onUserAddedAndDeleted: OnUserAddedAndDeleted
//) :
//    MultiLevelAdapter(items) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.NormalViewHolder {
//        return Holder(
//            LayoutInflater.from(parent.context).inflate(
//                R.layout.group_item_layout,
//                parent,
//                false
//            ), items
//        )
//
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.NormalViewHolder, position: Int) {
//        val mItem = items[position] as GroupHeaderItem
//
//        when (getItemViewType(position)) {
//            1 -> {
//                (holder as Holder).mView.setBackgroundColor(Color.parseColor("#DFDFDF"))
//            }
//            else -> {
//                (holder as Holder).mView.setBackgroundColor(Color.parseColor("#F3F3F3"))
//            }
//        }
//
//        if (mItem.hasChildren() && mItem.children.size > 0) {
//            setExpandButton((holder as Holder).mView.mExpandIcon, mItem.isExpanded)
//            holder.mView.mExpandButton.visibility = View.VISIBLE
//            (holder as Holder).mView.mTitle.text = mItem.groupTitle
//        } else {
//            (holder as Holder).mView.mExpandButton.visibility = View.GONE
//            (holder as Holder).mView.mTitle.text = mItem.fullName
//        }
//
//
//        val density = holder.mView.context.resources.displayMetrics.density
//        (holder.mView.mTextBox.layoutParams as ViewGroup.MarginLayoutParams).marginEnd =
//            (getItemViewType(position) * 20 * density + 0.5f).toInt()
//
//
//        if (position == itemCount - 1 && position != 0 && position != 1) {
//            val params = (holder as Holder).mView.layoutParams as RecyclerView.LayoutParams
//            params.bottomMargin = dp2px(holder.mView.context, 40f)
//            (holder as Holder).mView.layoutParams = params
//        } else {
//            val params = (holder as Holder).mView.layoutParams as RecyclerView.LayoutParams
//            params.bottomMargin = dp2px(holder.mView.context, 4f)
//            (holder as Holder).mView.layoutParams = params
//        }
//
//        if (mItem.selected) {
//            (holder as Holder).mView.setBackgroundColor(Color.parseColor("#A62A2A"))
//        } else {
//            (holder as Holder).mView.setBackgroundColor(Color.parseColor("#F3F3F3"))
//
//        }
//
//    }
//
//
//    inner class Holder(
//        val mView: View,
//        items: MutableList<RecyclerViewItem>
//    ) : RecyclerView.NormalViewHolder(mView) {
//
//        init {
//            mView.setOnClickListener {
//                val item = items[adapterPosition] as GroupHeaderItem
//
//                if (mView.mExpandButton.visibility == View.VISIBLE) run {
//                    mView.mExpandIcon.animate()
//                        .rotation((if (items[adapterPosition].isExpanded) 0 else -180).toFloat())
//                        .start()
//
//                    print(item.isExpanded.toString())
//                } else {
//                    if (item.selected) {
//                        item.selected = false
//                        mView.setBackgroundColor(Color.parseColor("#F3F3F3"))
//                        onUserAddedAndDeleted.OnUserDeleted(item)
//                    } else {
//                        item.selected = true
//                        mView.setBackgroundColor(Color.parseColor("#A62A2A"))
//                        onUserAddedAndDeleted.OnUserAdded(item)
//                    }
//
//                }
//            }
//        }
//
//
//    }
//
//    private fun setExpandButton(expandButton: ImageView, isExpanded: Boolean) {
//        // set the icon based on the current state
//        expandButton.setImageResource(if (isExpanded) R.drawable.ic_keyboard_arrow_up_black_24dp else R.drawable.ic_keyboard_arrow_down_black_24dp)
//    }
//
//    fun dp2px(context: Context, dp: Float): Int {
//        return TypedValue.applyDimension(
//            TypedValue.COMPLEX_UNIT_DIP, dp, context.resources
//                .displayMetrics
//        ).toInt()
//    }
//
//    interface OnUserAddedAndDeleted {
//        fun OnUserAdded(groupItem: GroupHeaderItem)
//        fun OnUserDeleted(groupItem: GroupHeaderItem)
//    }
//}