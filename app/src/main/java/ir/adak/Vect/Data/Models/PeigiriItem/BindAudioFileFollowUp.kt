package ir.adak.Vect.Data.Models.PeigiriItem

import android.view.View
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ir.adak.Vect.BuildConfig
import ir.adak.Vect.Data.Enums.FileTypeEnum
import ir.adak.Vect.Data.Enums.FollowUpType
import ir.adak.Vect.Listeners.OnFollowupLongClickListener
import ir.adak.Vect.Listeners.OnPlayPauseAudioClicked
import ir.adak.Vect.Listeners.OnReplyFollowUpClicked
import ir.adak.Vect.R
import ir.adak.Vect.Utils.Constants
import kotlinx.android.synthetic.main.my_audio_peigiri_layout.view.*
import kotlinx.android.synthetic.main.others_audio_peigiri_layout.view.*
import java.util.concurrent.TimeUnit

class BindAudioFileFollowUp {
    companion object {
        fun Bind(
            viewHolder: ViewHolder,
            peigiriItem: PeigiriItem,
            onPlayPauseAudioClicked: OnPlayPauseAudioClicked?,
            onReplyFollowUpClicked: OnReplyFollowUpClicked?,
            onFollowupLongClickListener: OnFollowupLongClickListener?
        ) {
            val followUp = peigiriItem.followUp
            val orgLevelId = peigiriItem.orgLevelId

            when (followUp.orgLevelId == orgLevelId) {
                true -> {
                    if (followUp.isFixedHighLight){
                        viewHolder.itemView.img_highlight_my_audio.alpha = 1f
                    }else{
                        viewHolder.itemView.img_highlight_my_audio.alpha = 0f
                    }


//                    if (!followUp.profileImage.isNullOrEmpty()) {
//                        Glide.with(viewHolder.itemView.context)
//                            .load(Constants.AVATAR_BASE_URL + followUp.profileImage)
//                            .into(viewHolder.itemView.img_avatar_my_audio)
//                    }

                    if (followUp.isReply) {
                        viewHolder.itemView.constraint_my_audio_reply_layout.visibility =
                            View.VISIBLE
                        viewHolder.itemView.txt_my_audio_reply_username.text =
                            followUp.replyFollowUp?.createName

                        when (followUp.replyFollowUp?.followUpType) {
                            FollowUpType.Text -> {
                                if (followUp.replyFollowUp.followUpType == FollowUpType.Text) {
                                    viewHolder.itemView.txt_my_audio_reply_text.text =
                                        followUp.replyFollowUp.description
                                }
                            }
                            FollowUpType.File -> {
                                when (followUp.replyFollowUp.fileTypeEnum) {
                                    FileTypeEnum.Unknown -> {
                                        viewHolder.itemView.txt_my_audio_reply_text.text =
                                            "فایل  ${followUp.replyFollowUp?.fileExtension}"
                                        viewHolder.itemView.img_my_audio_reply_thumbnail.visibility =
                                            View.GONE
                                    }
                                    FileTypeEnum.FaceStatus -> {
                                        viewHolder.itemView.txt_my_audio_reply_text.text =
                                            "صورت وضعيت  ${followUp.replyFollowUp?.fileExtension}"
                                        viewHolder.itemView.img_my_audio_reply_thumbnail.visibility =
                                            View.GONE
                                    }

                                    FileTypeEnum.Image
                                    -> {
                                      
                                        viewHolder.itemView.txt_my_audio_reply_text.text =
                                            "تصویر"
                                        viewHolder.itemView.img_my_audio_reply_thumbnail.visibility =
                                            View.VISIBLE
                                        if (followUp.replyFollowUp?.file?.exists() == true) {
                                            val uri = FileProvider.getUriForFile(
                                                viewHolder.itemView.context,
                                                BuildConfig.APPLICATION_ID + ".fileprovider",
                                                followUp.replyFollowUp?.file!!
                                            )
                                            Glide.with(viewHolder.itemView.context)
                                                .load(uri)
                                                .centerCrop()
                                                .into(viewHolder.itemView.img_my_audio_reply_thumbnail)
                                        } else {
                                            Glide.with(viewHolder.itemView.context)
                                                .load(Constants.FILE_BASE_URL + followUp.replyFollowUp?.fileName + "-.jpg")
                                                .centerCrop()
                                                .into(viewHolder.itemView.img_my_audio_reply_thumbnail)
                                        }
                                    }
                                    FileTypeEnum.Video -> {
                                     
                                        viewHolder.itemView.txt_my_audio_reply_text.text =
                                            "ویدئو"
                                        viewHolder.itemView.img_my_audio_reply_thumbnail.visibility =
                                            View.VISIBLE
                                    }
                                    FileTypeEnum.Audio -> {
                                      
                                        viewHolder.itemView.txt_my_audio_reply_text.text =
                                            "فایل صوتی"
                                        viewHolder.itemView.img_my_audio_reply_thumbnail.visibility =
                                            View.GONE
                                    }
                                }
                            }
                        }

                        viewHolder.itemView.constraint_my_audio_reply_layout.setOnClickListener {
                            onReplyFollowUpClicked?.OnReplyFollowUpClicked(followUp.replyFollowUp)
                        }
                    } else {
                        viewHolder.itemView.constraint_my_audio_reply_layout.visibility =
                            View.GONE
                    }

                    if (followUp.isUploadType) {
                        viewHolder.itemView.circularProgressBar_my_audio.visibility = View.VISIBLE
                        viewHolder.itemView.btn_cancel_upload_my_audio.visibility = View.VISIBLE
                        viewHolder.itemView.img_play_pause_my_audio.visibility = View.GONE

                        viewHolder.itemView.circularProgressBar_my_audio.progress =
                            followUp.fileUploadProgress.toFloat()

                        if (followUp.fileUploadProgress != 0 && followUp.fileUploadProgress != 100) {
                            viewHolder.itemView.btn_cancel_upload_my_audio.setImageResource(
                                R.drawable.ic_close_black_24dp
                            )
                        } else {
                            viewHolder.itemView.circularProgressBar_my_audio.visibility = View.GONE
                            viewHolder.itemView.btn_cancel_upload_my_audio.visibility = View.GONE
                            viewHolder.itemView.img_play_pause_my_audio.visibility = View.VISIBLE
                        }
                    } else {
                        viewHolder.itemView.circularProgressBar_my_audio.visibility = View.GONE
                        viewHolder.itemView.btn_cancel_upload_my_audio.visibility = View.GONE
                        viewHolder.itemView.img_play_pause_my_audio.visibility = View.VISIBLE
                    }
                    viewHolder.itemView.txt_username_my_audio.text = followUp.createName
                    viewHolder.itemView.txt_semat_my_audio.text = followUp.orgLevelTitle
                    viewHolder.itemView.txt_date_my_audio.text = followUp.persianDate

                    viewHolder.itemView.seekbar_my_audio.max =
                        followUp.trackDuration.toInt()
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
                    viewHolder.itemView.img_play_pause_my_audio.setOnClickListener {
                        onPlayPauseAudioClicked?.onPlayPauseAudioClicked(peigiriItem)
                    }
                    viewHolder.itemView.setOnLongClickListener {
                        onFollowupLongClickListener?.onFollowupLongClickListener(peigiriItem, true)
                        true
                    }
                }
                false -> {
                    if (followUp.isFixedHighLight){
                    viewHolder.itemView.img_highlight_others_audio.alpha = 1f
                }else{
                    viewHolder.itemView.img_highlight_others_audio.alpha = 0f
                }


//                    if (!followUp.profileImage.isNullOrEmpty()) {
//                        Glide.with(viewHolder.itemView.context)
//                            .load(Constants.AVATAR_BASE_URL + followUp.profileImage)
//                            .into(viewHolder.itemView.img_avatar_others_audio)
//                    }

                    if (followUp.isReply) {
                        viewHolder.itemView.constraint_others_audio_reply_layout.visibility =
                            View.VISIBLE
                        viewHolder.itemView.txt_others_audio_reply_username.text =
                            followUp.replyFollowUp?.createName

                        when (followUp.replyFollowUp?.followUpType) {
                            FollowUpType.Text -> {
                               
                                if (followUp.replyFollowUp?.followUpType == FollowUpType.Text) {
                                    viewHolder.itemView.txt_others_audio_reply_text.text =
                                        followUp.replyFollowUp?.description
                                }
                            }
                            FollowUpType.File -> {
                                when (followUp.replyFollowUp?.fileTypeEnum) {
                                    FileTypeEnum.Unknown -> {
                                       
                                        viewHolder.itemView.txt_others_audio_reply_text.text =
                                            "فایل  ${followUp.replyFollowUp?.fileExtension}"
                                        viewHolder.itemView.img_others_audio_reply_thumbnail.visibility =
                                            View.GONE
                                    }
                                    FileTypeEnum.FaceStatus -> {

                                        viewHolder.itemView.txt_others_audio_reply_text.text =
                                            "صورت وضعيت  ${followUp.replyFollowUp?.fileExtension}"
                                        viewHolder.itemView.img_others_audio_reply_thumbnail.visibility =
                                            View.GONE
                                    }
                                    FileTypeEnum.Image
                                    -> {
                                       
                                        viewHolder.itemView.txt_others_audio_reply_text.text =
                                            "تصویر"
                                        viewHolder.itemView.img_others_audio_reply_thumbnail.visibility =
                                            View.VISIBLE
                                        if (followUp.replyFollowUp?.file?.exists() == true) {
                                            val uri = FileProvider.getUriForFile(
                                                viewHolder.itemView.context,
                                                BuildConfig.APPLICATION_ID + ".fileprovider",
                                                followUp.replyFollowUp?.file!!
                                            )
                                            Glide.with(viewHolder.itemView.context)
                                                .load(uri)
                                                .centerCrop()
                                                .into(viewHolder.itemView.img_others_audio_reply_thumbnail)
                                        } else {
                                            Glide.with(viewHolder.itemView.context)
                                                .load(Constants.FILE_BASE_URL + followUp.replyFollowUp?.fileName + "-.jpg")
                                                .centerCrop()
                                                .into(viewHolder.itemView.img_others_audio_reply_thumbnail)
                                        }
                                    }
                                    FileTypeEnum.Video -> {
                                       
                                        viewHolder.itemView.txt_others_audio_reply_text.text =
                                            "ویدئو"
                                        viewHolder.itemView.img_others_audio_reply_thumbnail.visibility =
                                            View.VISIBLE
                                    }
                                    FileTypeEnum.Audio -> {
                                     
                                        viewHolder.itemView.txt_others_audio_reply_text.text =
                                            "فایل صوتی"
                                        viewHolder.itemView.img_others_audio_reply_thumbnail.visibility =
                                            View.GONE
                                    }
                                }
                            }
                        }

                        viewHolder.itemView.constraint_others_audio_reply_layout.setOnClickListener {
                            onReplyFollowUpClicked?.OnReplyFollowUpClicked(followUp.replyFollowUp)
                        }
                    } else {
                        viewHolder.itemView.constraint_others_audio_reply_layout.visibility =
                            View.GONE
                    }

                    viewHolder.itemView.txt_username_others_audio.text = followUp.createName
                    viewHolder.itemView.txt_semat_others_audio.text = followUp.orgLevelTitle
                    viewHolder.itemView.txt_date_others_audio.text = followUp.persianDate
                    viewHolder.itemView.seekbar_others_audio.max =
                        followUp.trackDuration.toInt()
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
                    viewHolder.itemView.img_play_pause_others_audio.setOnClickListener {
                        onPlayPauseAudioClicked?.onPlayPauseAudioClicked(peigiriItem)
                    }
                    viewHolder.itemView.setOnLongClickListener {
                        onFollowupLongClickListener?.onFollowupLongClickListener(peigiriItem, false)
                        true
                    }
                }
            }

        }
    }
}
