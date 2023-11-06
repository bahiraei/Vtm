package ir.adak.Vect.Data.Models.PeigiriItem

import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.active_project_item_layout.view.*

class BindActiveProjectFollowUp {
    companion object {
        fun Bind(
            viewHolder: ViewHolder,
            peigiriItem: PeigiriItem
        ) {
            val followUp = peigiriItem.followUp
            val orgLevelId = peigiriItem.orgLevelId

            viewHolder.itemView.txt_active_project.text =
                "وظیفه در تاریخ ${followUp.persianDate} فعال شد"
        }
    }

}