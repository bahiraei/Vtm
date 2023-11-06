package ir.adak.Vect.Data.Models

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ir.adak.Vect.R
import ir.adak.Vect.UI.Fragments.SortFragment
import kotlinx.android.synthetic.main.sort_item_layout.view.*
import kotlinx.android.synthetic.main.sort_type_item_layout.view.*

data class FollowUpSortItem(
    var name: String,
    var selected: Boolean?,
    var followUpSortEnum: SortFragment.FollowUpSortEnum?,
    var onSortItemSelected: SortFragment.OnSortItemSelected
) : Item() {
    var isSwitch = false
    override fun bind(viewHolder: ViewHolder, position: Int) {
        when (isSwitch) {
            true -> {
                viewHolder.itemView.txt_sort_type_name.text = name
                viewHolder.itemView.switch_sort.isChecked = selected ?: false
                viewHolder.itemView.setOnClickListener {
                    if (selected == true) {
                        selected = false
                        viewHolder.itemView.switch_sort.isChecked = false
                    } else {
                        selected = true
                        viewHolder.itemView.switch_sort.isChecked = true
                    }
                    onSortItemSelected.OnSortItemSelected(this)
                }
            }

            false -> {
                viewHolder.itemView.txt_sort_name.text = name
                viewHolder.itemView.radio_sort.isChecked = selected ?: false
                viewHolder.itemView.setOnClickListener {
                    selected = true
                    viewHolder.itemView.radio_sort.isChecked = true
                    onSortItemSelected.OnSortItemSelected(this)
                }
            }
        }

    }

    override fun getLayout(): Int =
        if (isSwitch) R.layout.sort_type_item_layout else R.layout.sort_item_layout
}