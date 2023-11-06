package ir.adak.Vect.Data.Models.PeigiriItem

import android.util.Log
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ir.adak.Vect.Listeners.OnFollowUpClickListener
import kotlinx.android.synthetic.main.new_meeting_item_layout.view.*

class BindNewMeetingFollowUp {

    companion object {

        fun Bind(
            viewHolder: ViewHolder,
            peigiriItem: PeigiriItem,
            onfollowUpClickListener: OnFollowUpClickListener?
        ) {
            val followUp = peigiriItem.followUp
            val orgLevelId = peigiriItem.orgLevelId
            viewHolder.itemView.txt_new_meeting.text =
                "یک جلسه به نام \"${followUp.newMeetingTitle}\" توسط ${followUp.createName} (${followUp.orgLevelTitle}) در تاریخ ${followUp.persianDate} ایجاد شد"

            viewHolder.itemView.setOnClickListener {
                if (followUp.newMeetingAllowToAccess == true)
                {
                    Log.i("dlmsbkndss","A")
//                    var i= Intent(c, MeetingActivity::class.java)
//                    c?.startActivity(i)
                    onfollowUpClickListener?.OnClick(followUp)
                }else{
                    Log.i("dlmsbkndss","B")
                }

            }
        }
    }

}