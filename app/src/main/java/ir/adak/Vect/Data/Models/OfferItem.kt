package ir.adak.Vect.Data.Models

import androidx.recyclerview.widget.RecyclerView
import com.warkiz.widget.SizeUtils.dp2px
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ir.adak.Vect.R
import kotlinx.android.synthetic.main.offer_item_layout.view.*

data class OfferItem(
    val date: String? = null,
    val hasProject: Boolean? = false,
    val id: String? = null,
    val isDiscussed: Boolean? = false,
    val currentMeeting: Meeting? = null,
    val meetingId: String? = null,
    val orgLevelId: Int? = -1,
    val orgLevelTitle: String? = null,
    val personelName: String? = null,
    val projects: List<Project>? = null,
    val title: String? = null

) : com.xwray.groupie.kotlinandroidextensions.Item() {


    override fun bind(viewHolder: ViewHolder, position: Int) {

        if (position == 0) {
            val params = viewHolder.itemView.layoutParams as RecyclerView.LayoutParams
            params.topMargin = dp2px(viewHolder.itemView.context, 16f)
            viewHolder.itemView.layoutParams = params
        } else {
            val params = viewHolder.itemView.layoutParams as RecyclerView.LayoutParams
            params.topMargin = dp2px(viewHolder.itemView.context, 0f)
            viewHolder.itemView.layoutParams = params
        }

        viewHolder.itemView.txt_username.text = personelName
        viewHolder.itemView.txt_semat.text = orgLevelTitle
        viewHolder.itemView.txt_offer.text = title
        viewHolder.itemView.txt_offer_date.text = date

    }

    override fun getLayout(): Int = R.layout.offer_item_layout

}