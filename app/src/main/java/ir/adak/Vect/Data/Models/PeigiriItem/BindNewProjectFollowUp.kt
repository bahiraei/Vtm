package ir.adak.Vect.Data.Models.PeigiriItem

import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ir.adak.Vect.Data.Enums.ProjectType
import ir.adak.Vect.Listeners.OnFollowUpClickListener
import kotlinx.android.synthetic.main.new_project_item_layout.view.*

class BindNewProjectFollowUp {
    companion object {
        fun Bind(
            viewHolder: ViewHolder,
            peigiriItem: PeigiriItem,
            onfollowUpClickListener: OnFollowUpClickListener?
        ) {
            val followUp = peigiriItem.followUp
            val orgLevelId = peigiriItem.orgLevelId
            ProjectType.Purpose.value
            viewHolder.itemView.txt_new_project.text =
                "یک ${followUp.newProjectTypeName} به نام \"${followUp.newProjectTitle}\" توسط ${followUp.createName} (${followUp.orgLevelTitle}) در تاریخ ${followUp.persianDate} ایجاد شد"
//            when (followUp.newProjectType) {
//                ProjectType.Purpose.value -> {
//                    viewHolder.itemView.txt_new_project.text =
//                        "یک هدف به نام \"${followUp.newProjectTitle}\" توسط ${followUp.createName} (${followUp.orgLevelTitle}) در تاریخ ${followUp.persianDate} ایجاد شد"
//
//                }
//                ProjectType.Project.value -> {
//                    viewHolder.itemView.txt_new_project.text =
//                        "یک پروژه به نام \"${followUp.newProjectTitle}\" توسط ${followUp.createName} (${followUp.orgLevelTitle}) در تاریخ ${followUp.persianDate} ایجاد شد"
//
//
//                }
//                ProjectType.Incident.value -> {
//                    viewHolder.itemView.txt_new_project.text =
//                        "یک حادثه به نام \"${followUp.newProjectTitle}\" توسط ${followUp.createName} (${followUp.orgLevelTitle}) در تاریخ ${followUp.persianDate} ایجاد شد"
//
//                }
//                ProjectType.Job.value -> {
//                    viewHolder.itemView.txt_new_project.text =
//                        "یک کار به نام \"${followUp.newProjectTitle}\" توسط ${followUp.createName} (${followUp.orgLevelTitle}) در تاریخ ${followUp.persianDate} ایجاد شد"
//
//                }
//                ProjectType.Offer.value -> {
//                    viewHolder.itemView.txt_new_project.text =
//                        "یک پیشنهاد به نام \"${followUp.newProjectTitle}\" توسط ${followUp.createName} (${followUp.orgLevelTitle}) در تاریخ ${followUp.persianDate} ایجاد شد"
//
//                }
//                ProjectType.Problem.value -> {
//                    viewHolder.itemView.txt_new_project.text =
//                        "یک مشکل به نام \"${followUp.newProjectTitle}\" توسط ${followUp.createName} (${followUp.orgLevelTitle}) در تاریخ ${followUp.persianDate} ایجاد شد"
//
//                }
//            }
//            viewHolder.itemView.setOnClickListener {
//                if (followUp.newProjectAllowToAccess == true)
//                    onfollowUpClickListener?.OnClick(followUp)
//            }
//
//        }
        }


    }
}