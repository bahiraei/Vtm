package ir.adak.Vect.Data.Models.PeigiriItem

import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.my_deleted_followup_item.view.*
import kotlinx.android.synthetic.main.others_deleted_followup_item.view.*

class BindDeletedFollowUp {
    companion object {
        fun Bind(
            viewHolder: ViewHolder,
            peigiriItem: PeigiriItem
        ) {
            val followUp = peigiriItem.followUp
            val orgLevelId = peigiriItem.orgLevelId


            when (followUp.orgLevelId == orgLevelId) {
                true -> {

//                    if (!followUp.profileImage.isNullOrEmpty()) {
//                        Glide.with(viewHolder.itemView.context)
//                            .load(Constants.AVATAR_BASE_URL + followUp.profileImage)
//                            .into(viewHolder.itemView.img_avatar_my_deleted_followup)
//                    }

                    viewHolder.itemView.txt_message_my_deleted_followup.text =
                        "شما این پیگیری را پاک کردید"
                    viewHolder.itemView.txt_date_my_deleted_followup.text =
                        followUp.persianDate
                }
                false -> {

//                    if (!followUp.profileImage.isNullOrEmpty()) {
//                        Glide.with(viewHolder.itemView.context)
//                            .load(Constants.AVATAR_BASE_URL + followUp.profileImage)
//                            .into(viewHolder.itemView.img_avatar_others_deleted_followup)
//                    }

                    viewHolder.itemView.txt_message_others_deleted_followup.text =
                        "آقای ${followUp.createName} این پیگیری را پاک کرد"
                    viewHolder.itemView.txt_date_others_deleted_followup.text =
                        followUp.persianDate
                }
            }
        }
    }

}