package ir.adak.Vect.Data.Models.PeigiriItem

import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.created_project_item_layout.view.*

class BindCreatedProjectFollowUp {
    companion object {
        fun Bind(
            viewHolder: ViewHolder,
            peigiriItem: PeigiriItem
        ) {
            val followUp = peigiriItem.followUp
            val orgLevelId = peigiriItem.orgLevelId

            viewHolder.itemView.txt_created_project.text =
                "وظیفه در تاریخ ${followUp.persianDate} ایجاد شد"
        }
    }

}