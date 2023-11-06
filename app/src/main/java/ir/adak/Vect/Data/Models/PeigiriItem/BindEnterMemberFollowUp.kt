package ir.adak.Vect.Data.Models.PeigiriItem

import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.enter_member_item_layout.view.*

class BindEnterMemberFollowUp {
    companion object {
        fun Bind(
            viewHolder: ViewHolder,
            peigiriItem: PeigiriItem
        ) {
            val followUp = peigiriItem.followUp
            val orgLevelId = peigiriItem.orgLevelId

            viewHolder.itemView.txt_enter_member_name.text =
                "${followUp.memberFullName} (${followUp.memberTitle}) به مجموعه اضافه شد"
            viewHolder.itemView.txt_enter_member_name_by.text =
                "توسط : ${followUp.createName} (${followUp.orgLevelTitle})"
        }
    }

}