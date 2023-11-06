package ir.adak.Vect.Data.Models.PeigiriItem

import com.xwray.groupie.ViewHolder
import ir.adak.Vect.Data.Models.Project
import kotlinx.android.synthetic.main.closed_task_summary_layout.view.*

class BindSummaryEndProject {
    companion object {
        fun Bind(
            viewHolder: ViewHolder,
            peigiriItem: PeigiriItem,
            project: Project?
        ) {
            val followUp = peigiriItem.followUp
            val orgLevelId = peigiriItem.orgLevelId

            viewHolder.itemView.txt_project_title.text = followUp.endProject_ProjectTitle
            viewHolder.itemView.txt_mojri.text = followUp.endProject_PersonFullName
            viewHolder.itemView.txt_start_date.text = followUp.endProject_ProjectStartDate
            viewHolder.itemView.txt_end_date.text = followUp.endProject_ProjectEndDate

            viewHolder.itemView.txt_duration.text = followUp.endProject_TimeActivity

            viewHolder.itemView.txt_followUps_count.text =
                followUp.endProject_FollowupCount.toString()

            viewHolder.itemView.txt_meetings_count.text =
                followUp.endProject_MeetingCount.toString()

            viewHolder.itemView.txt_tasks_count.text = followUp.endProject_ProjectCount.toString()

//            viewHolder.itemView.txt_members_count.text = followUp.endProject_ProjectCount.toString()


        }
    }
}
