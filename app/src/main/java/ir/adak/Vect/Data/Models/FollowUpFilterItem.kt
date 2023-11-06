package ir.adak.Vect.Data.Models

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ir.adak.Vect.R
import ir.adak.Vect.UI.Fragments.FilterFragment
import kotlinx.android.synthetic.main.filter_item_layout.view.*

data class FollowUpFilterItem(
    var name: String,
    var selected: Boolean?,
    var followUpFilterEnum: FilterFragment.FollowUpFilterEnum,
    var onFilterItemSelected: FilterFragment.OnFilterItemSelected
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.txt_filter_name.text = name
        viewHolder.itemView.checkbox_filter.isChecked = selected ?: false

        viewHolder.itemView.setOnClickListener {
            if (selected == true) {
                selected = false
                viewHolder.itemView.checkbox_filter.isChecked = false
            } else {
                selected = true
                viewHolder.itemView.checkbox_filter.isChecked = true
            }
            onFilterItemSelected.OnFilterItemSelected(this)
        }
    }

    override fun getLayout(): Int = R.layout.filter_item_layout
}