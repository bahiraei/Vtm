package ir.adak.Vect.Data.Models

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ir.adak.Vect.R
import kotlinx.android.synthetic.main.user_entered_item_layout.view.*
import kotlinx.android.synthetic.main.user_exited_item_layout.view.*

data class MemberLogModel(
    val access: Int?=0,
    val date: String?="",
    val id: String?="",
    val logType: Int?=0,
    val memberOrgLevelId: Int,
    val memberTitle: String,
    val memberFullName: String,
    val profileImage: String?=""
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        when (logType) {
            1 -> {
                viewHolder.itemView.txt_username_user_entered.text = memberFullName
                viewHolder.itemView.txt_semat_user_entered.text = memberTitle
                viewHolder.itemView.txt_date_user_entered.text = date
            }

            2 -> {
                viewHolder.itemView.txt_username_user_exited.text = memberFullName
                viewHolder.itemView.txt_semat_user_exited.text = memberTitle
                viewHolder.itemView.txt_date_user_exited.text = date
            }

        }
    }

    override fun getLayout(): Int = when (logType) {
        1 -> R.layout.user_entered_item_layout
        2 -> R.layout.user_exited_item_layout
        else -> 0
    }
}