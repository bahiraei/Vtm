package ir.adak.Vect.Data.Models.PeigiriItem

import android.content.Context
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ir.adak.Vect.Data.Enums.FileTypeEnum
import ir.adak.Vect.Data.Enums.FollowUpType
import ir.adak.Vect.Data.Enums.PeigiriChangeEnum
import ir.adak.Vect.R
import ir.adak.Vect.Data.Models.FollowUp
import ir.adak.Vect.Data.Models.Project
import ir.adak.Vect.Listeners.*
import kotlinx.android.synthetic.main.change_progress_green_item_layout.view.*
import kotlinx.android.synthetic.main.change_progress_red_item_layout.view.*
import kotlinx.android.synthetic.main.my_audio_peigiri_layout.view.*
import kotlinx.android.synthetic.main.others_audio_peigiri_layout.view.*
import java.util.concurrent.TimeUnit


data class PeigiriItem(
    var project: Project?,
    var orgLevelId: Int?,
    var followUp: FollowUp,
    var onDownloadButtonClicked: OnDownloadButtonClicked? = null,
    var onCancelUploadClicked: OnCancelUploadClicked? = null,
    var onPlayVideoClicked: OnPlayVideoClicked? = null,
    var C:Context,
    var onFollowUpClickListener: OnFollowUpClickListener? = null
) :
    com.xwray.groupie.kotlinandroidextensions.Item() {

    var onFollowupLongClickListener: OnFollowupLongClickListener? = null
    var onReplyFollowUpClicked: OnReplyFollowUpClicked? = null
    var onPlayPauseAudioClicked: OnPlayPauseAudioClicked? = null
    var onPlayerSeekBarChangedListener: OnPlayerSeekBarChangedListener? = null

    override fun bind(viewHolder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            when (payloads[0]) {
                PeigiriChangeEnum.PROGRESS_CHANGE -> {
                    BindProgressChangeFollowUp.Bind(
                        viewHolder,
                        this,
                        onDownloadButtonClicked,
                        onCancelUploadClicked,
                        onPlayPauseAudioClicked,
                        onPlayerSeekBarChangedListener
                    )
                }
                PeigiriChangeEnum.HIGHLIGHT -> {
                    BindHighlightFollowUp.Bind(viewHolder, this)
                }
                PeigiriChangeEnum.PLAY_PAUSE_AUDIO -> {
                    when (followUp.orgLevelId == orgLevelId) {
                        true -> {
                            if (followUp.isPlaying) {
                                viewHolder.itemView.img_play_pause_my_audio.setImageResource(R.drawable.bvp_action_pause)
                            } else {
                                viewHolder.itemView.img_play_pause_my_audio.setImageResource(R.drawable.bvp_action_play)
                            }
                        }
                        false -> {
                            if (followUp.isPlaying) {
                                viewHolder.itemView.img_play_pause_others_audio.setImageResource(R.drawable.bvp_action_pause)

                            } else {
                                viewHolder.itemView.img_play_pause_others_audio.setImageResource(R.drawable.bvp_action_play)
                            }
                        }
                    }

                }
                PeigiriChangeEnum.RESET_PROGRESS -> {
                    when (followUp.orgLevelId == orgLevelId) {
                        true -> {
                            viewHolder.itemView.seekbar_my_audio.progress =
                                followUp.currentPlayingPosition.toInt()
                            val currentPlayingPositionMinutes =
                                TimeUnit.MILLISECONDS.toMinutes(followUp.currentPlayingPosition)         // minutes ok
                            val currentPlayingPositionSecs =
                                (followUp.currentPlayingPosition / 1000 % 60)       // minutes ok

                            val trackDurationMinutes =
                                TimeUnit.MILLISECONDS.toMinutes(followUp.trackDuration)         // minutes ok
                            val trackDurationSecs =
                                (followUp.trackDuration / 1000 % 60)       // minutes ok
                            viewHolder.itemView.txt_track_position_my_audio.text =
                                "${currentPlayingPositionMinutes}:${currentPlayingPositionSecs} / ${trackDurationMinutes}:${trackDurationSecs}"
                            if (followUp.isPlaying) {
                                viewHolder.itemView.img_play_pause_my_audio.setImageResource(R.drawable.bvp_action_pause)
                            } else {
                                viewHolder.itemView.img_play_pause_my_audio.setImageResource(R.drawable.bvp_action_play)
                            }
                        }
                        false -> {
                            viewHolder.itemView.seekbar_others_audio.progress =
                                followUp.currentPlayingPosition.toInt()
                            val currentPlayingPositionMinutes =
                                TimeUnit.MILLISECONDS.toMinutes(followUp.currentPlayingPosition)         // minutes ok
                            val currentPlayingPositionSecs =
                                (followUp.currentPlayingPosition / 1000 % 60)       // minutes ok

                            val trackDurationMinutes =
                                TimeUnit.MILLISECONDS.toMinutes(followUp.trackDuration)         // minutes ok
                            val trackDurationSecs =
                                (followUp.trackDuration / 1000 % 60)       // minutes ok

                            viewHolder.itemView.txt_track_position_others_audio.text =
                                "${currentPlayingPositionMinutes}:${currentPlayingPositionSecs} / ${trackDurationMinutes}:${trackDurationSecs}"

                            if (followUp.isPlaying) {
                                viewHolder.itemView.img_play_pause_others_audio.setImageResource(R.drawable.bvp_action_pause)

                            } else {
                                viewHolder.itemView.img_play_pause_others_audio.setImageResource(R.drawable.bvp_action_play)
                            }
                        }
                    }
                }
            }

        } else {
            super.bind(viewHolder, position, payloads)
        }
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        when (followUp.followUpType) {
            FollowUpType.Text -> {
                BindTextFollowUp.Bind(
                    viewHolder, this,
                    onReplyFollowUpClicked,
                    onFollowupLongClickListener
                )
            }
            FollowUpType.File -> {
                BindFileFollowUp.Bind(
                    viewHolder, this,onReplyFollowUpClicked, onFollowupLongClickListener,
                    onDownloadButtonClicked,
                    onPlayVideoClicked,
                    onPlayPauseAudioClicked
                )
            }
            FollowUpType.Description -> {
                BindDescriptionFollowUp.Bind(viewHolder, this)
            }
            FollowUpType.ChangeProgress -> {
                if (followUp.tprogress > followUp.fprogress) {
                    viewHolder.itemView.txt_change_progress_up.text =
                        "${followUp.createName} (${followUp.orgLevelTitle}) در ${followUp.persianDate} درصد پیشرفت را افزایش داد"
                    viewHolder.itemView.txt_from_progress_up.text = "${followUp.fprogress}"
                    viewHolder.itemView.txt_to_progress_up.text = "${followUp.tprogress}"


                } else {
                    viewHolder.itemView.txt_change_progress_down.text =
                        "${followUp.createName} (${followUp.orgLevelTitle}) در ${followUp.persianDate} درصد پیشرفت را کاهش داد"
                    viewHolder.itemView.txt_from_progress_down.text = "${followUp.fprogress}"
                    viewHolder.itemView.txt_to_progress_down.text = "${followUp.tprogress}"
                }
            }
            FollowUpType.Deleted -> {
                BindDeletedFollowUp.Bind(viewHolder, this)
            }
            FollowUpType.EnterMember -> {
                BindEnterMemberFollowUp.Bind(viewHolder, this)
            }
            FollowUpType.ExitMember -> {
                BindExitMemberFollowUp.Bind(viewHolder, this)
            }
            FollowUpType.ActiveProject -> {
                BindActiveProjectFollowUp.Bind(viewHolder, this)
            }
            FollowUpType.DeactiveProject -> {
                BindDectiveProjectFollowUp.Bind(viewHolder, this)
            }
            FollowUpType.EditProject -> {
                BindProjectEditedFollowUp.Bind(viewHolder, this)
            }
            FollowUpType.NewProject -> {
                BindNewProjectFollowUp.Bind(viewHolder, this, onFollowUpClickListener)
            }
            FollowUpType.NewMeeting -> {
                BindNewMeetingFollowUp.Bind(viewHolder, this, onFollowUpClickListener)
            }
            FollowUpType.CreatedProject -> {
                BindCreatedProjectFollowUp.Bind(viewHolder, this)
            }
            FollowUpType.ClosedProject -> {
                BindClosedProjectFollowUp.Bind(viewHolder, this)
            }
            FollowUpType.SummaryProject -> {

            }
            FollowUpType.SummaryEndProject -> {
                BindSummaryEndProject.Bind(viewHolder, this, project)
            }
            FollowUpType.ActionTime->{
                BindActionFollowUp.Bind(C,viewHolder , this,onReplyFollowUpClicked,onFollowupLongClickListener)
            }
        }
    }

    override fun getLayout(): Int =
        when (followUp.followUpType) {
            FollowUpType.Text -> {
                when (followUp.orgLevelId == orgLevelId) {
                    true -> R.layout.my_normal_text_peigiri_layout
                    false -> R.layout.others_normal_text_peigiri_layout
                }
            }
            FollowUpType.File -> {
                when (followUp.orgLevelId == orgLevelId) {
                    true -> {
                        when (followUp.fileTypeEnum) {
                            FileTypeEnum.Unknown -> R.layout.my_file_peigiri_layout
                            FileTypeEnum.Image -> R.layout.my_pic_peigiri_layout
                            FileTypeEnum.Video -> R.layout.my_video_peigiri_layout
                            FileTypeEnum.Audio -> R.layout.my_audio_peigiri_layout
                            FileTypeEnum.FaceStatus -> R.layout.my_face_status_peigiri_layout
                        }
                    }
                    false -> {
                        when (followUp.fileTypeEnum) {
                            FileTypeEnum.Unknown -> R.layout.others_file_peigiri_layout
                            FileTypeEnum.Image -> R.layout.others_pic_peigiri_layout
                            FileTypeEnum.Video -> R.layout.others_video_peigiri_layout
                            FileTypeEnum.Audio -> R.layout.others_audio_peigiri_layout
                            FileTypeEnum.FaceStatus -> R.layout.others_face_status_peigiri_layout
                        }
                    }
                }
            }
            FollowUpType.Description -> R.layout.description_peigiri_layout
            FollowUpType.ChangeProgress -> if (followUp.tprogress > followUp.fprogress) R.layout.change_progress_green_item_layout else R.layout.change_progress_red_item_layout
            FollowUpType.Deleted -> {
                when (followUp.orgLevelId == orgLevelId) {
                    true -> R.layout.my_deleted_followup_item
                    false -> R.layout.others_deleted_followup_item
                }
            }
            FollowUpType.EnterMember -> R.layout.enter_member_item_layout
            FollowUpType.ExitMember -> R.layout.exit_member_item_layout
            FollowUpType.ActiveProject -> R.layout.active_project_item_layout
            FollowUpType.DeactiveProject -> R.layout.deactive_project_item_layout
            FollowUpType.EditProject -> R.layout.project_edited_item_layout
            FollowUpType.NewProject -> R.layout.new_project_item_layout
//            FollowUpType.Jobs -> R.layout.new_project_item_layout
            FollowUpType.NewMeeting -> R.layout.new_meeting_item_layout
//            FollowUpType.Meeting -> R.layout.new_meeting_item_layout
            FollowUpType.CreatedProject -> R.layout.created_project_item_layout
            FollowUpType.ClosedProject -> R.layout.closed_project_item_layout
            FollowUpType.SummaryProject -> 0
            FollowUpType.SummaryEndProject -> R.layout.closed_task_summary_layout
            FollowUpType.ActionTime-> R.layout.action_followup_layout
        }
}