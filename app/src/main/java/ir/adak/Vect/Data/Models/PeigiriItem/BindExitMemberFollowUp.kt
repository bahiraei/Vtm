package ir.adak.Vect.Data.Models.PeigiriItem

import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.exit_member_item_layout.view.*


class BindExitMemberFollowUp {
    companion object {
        fun Bind(
            viewHolder: ViewHolder,
            peigiriItem: PeigiriItem
        ) {
            val followUp = peigiriItem.followUp
            val orgLevelId = peigiriItem.orgLevelId

            viewHolder.itemView.txt_exit_member_name.text =
                "${followUp.memberFullName} (${followUp.memberTitle}) از مجموعه خارج شد"

        }
    }

}