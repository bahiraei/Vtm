package ir.adak.Vect.Data.Models.PeigiriItem
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ir.adak.Vect.Listeners.OnFollowupLongClickListener
import ir.adak.Vect.Listeners.OnReplyFollowUpClicked
import ir.adak.Vect.UI.Activities.BaseActivity
import ir.adak.Vect.UI.Activities.DetailActionActivity
import ir.adak.Vect.UI.Activities.PeigiriActivity.PeigiriActivity
import ir.adak.Vect.Utils.Constants
import kotlinx.android.synthetic.main.action_followup_layout.view.*

class BindActionFollowUp {
    companion object {
        fun Bind(
            C:Context,
            viewHolder: ViewHolder,
            peigiriItem: PeigiriItem,
            onReplyFollowUpClicked: OnReplyFollowUpClicked?,
            onFollowupLongClickListener: OnFollowupLongClickListener?){
            val followUp = peigiriItem.followUp
            val orgLevelId = peigiriItem.orgLevelId
            if (!followUp.profileImage.isNullOrEmpty()) {
                Glide.with(viewHolder.itemView.context)
                    .load(Constants.AVATAR_BASE_URL + followUp.profileImage)
                    .into(viewHolder.itemView.img_avatar_action_followup)
            }
            if (followUp.isFixedHighLight) {
                Log.i("sffmvskmvskf","A")
                viewHolder.itemView.back_abi.setBackgroundColor(Color.parseColor("#604B94F0"))
            } else {
                Log.i("sffmvskmvskf","B")
                viewHolder.itemView.back_abi.setBackgroundColor(Color.TRANSPARENT)
            }
            viewHolder.itemView.txt_username_action_followup.text = followUp.createName
            viewHolder.itemView.txt_semat_action_followup.text = followUp.orgLevelTitle
            viewHolder.itemView.txt_action_date.text = followUp.actionDatePersian
            viewHolder.itemView.txt_action_time.text = followUp.actionTime
            viewHolder.itemView.txt_message_action_followup2.text = followUp.actionTitle

            if (followUp.actionStatus==1)
            {
                viewHolder.itemView.txt_message_action_followup3.text="در حال بررسی"
            }else if (followUp.actionStatus==2)
            {
                viewHolder.itemView.txt_message_action_followup3.text="تایید شده"
            }else if (followUp.actionStatus==3)
            {
                viewHolder.itemView.txt_message_action_followup3.text="رد شده"
            }else{
                viewHolder.itemView.txt_message_action_followup3.text="نامشخص"
            }

            if (followUp.actionFiles!=null)
            {
                if (followUp.actionFiles!!.size==0)
                {
                    viewHolder.itemView.txt_message_action_followup4.text="بدون فایل"
                }else{
                    var i=followUp.actionFiles?.size?.toInt()
                    viewHolder.itemView.txt_message_action_followup4.text= "  $i   فایل   "
                }
            }else{
                viewHolder.itemView.txt_message_action_followup4.text="بدون فایل"
            }

            viewHolder.itemView.textView19.setOnClickListener {
                if (peigiriItem.project?.isMyProject!!&&followUp.orgLevelId==BaseActivity.userInfo?.orgLevelId)
                {
                    var i=Intent(C,DetailActionActivity::class.java)
                    i.putExtra("project",peigiriItem.project)
                    var b=Bundle()
                    b.putSerializable("scsc",peigiriItem.followUp)
                    i.putExtra("folowup",b)
                    i.putExtra("type","0")
                    PeigiriActivity.c?.startActivityForResult(i,13989)
                }
                else if (!peigiriItem.project?.isMyProject!!&&followUp.orgLevelId==BaseActivity.userInfo?.orgLevelId)
                {
                    var i=Intent(C,DetailActionActivity::class.java)
                    i.putExtra("project",peigiriItem.project)
                    var b=Bundle()
                    b.putSerializable("scsc",peigiriItem.followUp)
                    i.putExtra("folowup",b)
                    i.putExtra("type","1")
                    PeigiriActivity.c?.startActivityForResult(i,13989)
                }else if (peigiriItem.project?.isMyProject!!&&followUp.orgLevelId!=BaseActivity.userInfo?.orgLevelId)
                {
                    var i=Intent(C,DetailActionActivity::class.java)
                    i.putExtra("project",peigiriItem.project)
                    var b=Bundle()
                    b.putSerializable("scsc",peigiriItem.followUp)
                    i.putExtra("folowup",b)
                    i.putExtra("type","2")
                    PeigiriActivity.c?.startActivityForResult(i,13989)
                }
                else if (!peigiriItem.project?.isMyProject!!&&followUp.orgLevelId!=BaseActivity.userInfo?.orgLevelId)
                {
                    var i=Intent(C,DetailActionActivity::class.java)
                    i.putExtra("project",peigiriItem.project)
                    var b=Bundle()
                    b.putSerializable("scsc",peigiriItem.followUp)
                    i.putExtra("folowup",b)
                    i.putExtra("type","3")
                    PeigiriActivity.c?.startActivityForResult(i,13989)
                }
            }


            viewHolder.itemView.txt_date_action_followup.text = followUp.persianDate
                Log.i("svmskvaaaaaa", orgLevelId.toString())
                Log.i("svmskvaaaaaa", followUp.orgLevelId.toString())
                viewHolder.itemView.setOnLongClickListener {
                    if (followUp.orgLevelId==BaseActivity.userInfo?.orgLevelId)
                    {
                        if (followUp.actionStatus==1)
                        {
                            onFollowupLongClickListener?.onFollowupLongClickListener(peigiriItem, true)
                        }

                    }
                   true
                }
        }
    }
}