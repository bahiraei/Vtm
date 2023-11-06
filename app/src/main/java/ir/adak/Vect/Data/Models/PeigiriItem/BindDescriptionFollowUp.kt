package ir.adak.Vect.Data.Models.PeigiriItem

import android.view.View
import com.bumptech.glide.Glide
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ir.adak.Vect.Utils.Constants
import kotlinx.android.synthetic.main.description_peigiri_layout.view.*

class BindDescriptionFollowUp {
    companion object {
        fun Bind(
            viewHolder: ViewHolder,
            peigiriItem: PeigiriItem
        ) {
            val followUp = peigiriItem.followUp

            if (!followUp.profileImage.isNullOrEmpty()) {
                Glide.with(viewHolder.itemView.context)
                    .load(Constants.AVATAR_BASE_URL + followUp.profileImage)
                    .into(viewHolder.itemView.img_avatar_description)
            }
            viewHolder.itemView.txt_username_description.text =
                followUp.createName
            viewHolder.itemView.txt_semat_description.text =
                followUp.orgLevelTitle
            viewHolder.itemView.txt_message_description.text =
                followUp.description
            viewHolder.itemView.txt_date_description.text = followUp.persianDate

            if (followUp.isEdited)
                viewHolder.itemView.txt_edited_description.visibility = View.VISIBLE
            else
                viewHolder.itemView.txt_edited_description.visibility = View.GONE


        }
    }

}