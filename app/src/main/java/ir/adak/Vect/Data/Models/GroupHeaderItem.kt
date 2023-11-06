package ir.adak.Vect.Data.Models

import android.view.View
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import ir.adak.Vect.R
import kotlinx.android.synthetic.main.group_header_layout.view.*

data class GroupHeaderItem(
    val groupLevel: Int,
    val groupId: String,
    val groupMembers: List<GroupItem>,
    val groupTitle: String,
    val groupType: Int

) : com.xwray.groupie.kotlinandroidextensions.Item(), ExpandableItem {


    private lateinit var expandableGroup: ExpandableGroup

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        expandableGroup = onToggleListener
    }

    override fun bind(
        viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder,
        position: Int
    ) {

        viewHolder.itemView.mExpandButton.visibility = View.VISIBLE
        viewHolder.itemView.mExpandIcon.setImageResource(getRotatedIconResId())
        viewHolder.itemView.mTitle.text = groupTitle
        viewHolder.itemView.setOnClickListener {
            expandableGroup.onToggleExpanded()
            viewHolder.itemView.mExpandIcon.setImageResource(getRotatedIconResId())
        }
    }

    override fun getLayout(): Int = R.layout.group_header_layout

    private fun getRotatedIconResId() =
        if (expandableGroup.isExpanded)
            R.drawable.ic_keyboard_arrow_down_black_24dp
        else
            R.drawable.ic_keyboard_arrow_up_black_24dp

}