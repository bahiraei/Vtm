package ir.adak.Vect.Data.Models

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ir.adak.Vect.R

data class HistoryItem(
    var isEntered: Boolean
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }

    override fun getLayout(): Int = when (isEntered) {
        true -> R.layout.user_entered_item_layout
        false -> R.layout.user_exited_item_layout
    }
}