package ir.adak.Vect.Data.Models

import androidx.recyclerview.widget.RecyclerView
import com.warkiz.widget.SizeUtils.dp2px
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ir.adak.Vect.R
import android.view.View
import ir.adak.Vect.Data.Enums.PeigiriChangeEnum
import kotlinx.android.synthetic.main.note_item_layout.view.*

data class NoteItem(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    var dateReminder: String? = null,
    var dateReminderEn: String? = null,
    var timeReminder: String? = null,
    val date: String? = null,
    var isHighlight: Boolean = false

) : com.xwray.groupie.kotlinandroidextensions.Item() {

    override fun bind(viewHolder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (!payloads.isNullOrEmpty()) {
            when (payloads[0]) {
                PeigiriChangeEnum.HIGHLIGHT -> {
                    if (isHighlight) {
                        viewHolder.itemView.img_highlight_note.alpha = 1f
                    } else {
                        viewHolder.itemView.img_highlight_note.alpha = 0f
                    }
                }
            }
        } else {
            super.bind(viewHolder, position, payloads)
        }
    }

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

        if (isHighlight) {
            viewHolder.itemView.img_highlight_note.alpha = 1f
        } else {
            viewHolder.itemView.img_highlight_note.alpha = 0f
        }

        viewHolder.itemView.txt_title.text = title
        viewHolder.itemView.txt_description.text = description
        viewHolder.itemView.txt_note_date.text = date
        if (timeReminder?.isNotEmpty() == true && dateReminder?.isNotEmpty() == true && dateReminderEn?.isNotEmpty() == true) {
            viewHolder.itemView.reminder_layout.visibility = View.VISIBLE
            viewHolder.itemView.txt_note_time_reminder.text = timeReminder
            viewHolder.itemView.txt_note_date_reminder.text = dateReminder
        } else {
            viewHolder.itemView.reminder_layout.visibility = View.INVISIBLE
        }

    }

    override fun getLayout(): Int = R.layout.note_item_layout

}