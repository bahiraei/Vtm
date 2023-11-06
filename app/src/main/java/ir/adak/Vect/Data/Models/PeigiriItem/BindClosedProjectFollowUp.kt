package ir.adak.Vect.Data.Models.PeigiriItem

import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.closed_project_item_layout.view.*

class BindClosedProjectFollowUp {
    companion object {
        fun Bind(
            viewHolder: ViewHolder,
            peigiriItem: PeigiriItem
        ) {
            val followUp = peigiriItem.followUp
            val orgLevelId = peigiriItem.orgLevelId

            viewHolder.itemView.txt_closed_project.text =
                "وظیفه در تاریخ ${followUp.persianDate} به اتمام رسید"
        }
    }

}