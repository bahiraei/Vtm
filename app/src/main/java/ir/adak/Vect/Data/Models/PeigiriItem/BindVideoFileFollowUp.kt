package ir.adak.Vect.Data.Models.PeigiriItem

import android.view.View
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ir.adak.Vect.BuildConfig
import ir.adak.Vect.Data.Enums.FileTypeEnum
import ir.adak.Vect.Data.Enums.FollowUpType
import ir.adak.Vect.Listeners.OnFollowupLongClickListener
import ir.adak.Vect.Listeners.OnPlayVideoClicked
import ir.adak.Vect.Listeners.OnReplyFollowUpClicked
import ir.adak.Vect.Utils.Constants
import kotlinx.android.synthetic.main.my_video_peigiri_layout.view.*
import kotlinx.android.synthetic.main.others_video_peigiri_layout.view.*

class BindVideoFileFollowUp {
    companion object {
        fun Bind(
            viewHolder: ViewHolder,
            peigiriItem: PeigiriItem,
            onReplyFollowUpClicked: OnReplyFollowUpClicked?,
            onFollowupLongClickListener: OnFollowupLongClickListener?,
            onPlayVideoClicked: OnPlayVideoClicked?
        ) {
            val followUp = peigiriItem.followUp
            val orgLevelId = peigiriItem.orgLevelId

            when (followUp.orgLevelId == orgLevelId) {
                true -> {
                    if (followUp.isFixedHighLight) {
                        viewHolder.itemView.img_highlight_my_video.alpha = 1f
                    } else {
                        viewHolder.itemView.img_highlight_my_video.alpha = 0f
                    }

//                    if (!followUp.profileImage.isNullOrEmpty()) {
//                        Glide.with(viewHolder.itemView.context)
//                            .load(Constants.AVATAR_BASE_URL + followUp.profileImage)
//                            .into(viewHolder.itemView.img_avatar_my_video)
//                    }

                    if (followUp.isReply) {
                        viewHolder.itemView.constraint_my_video_reply_layout.visibility =
                            View.VISIBLE
                        viewHolder.itemView.txt_my_video_reply_username.text =
                            followUp.replyFollowUp?.createName

                        when (followUp.replyFollowUp?.followUpType) {
                            FollowUpType.Text -> {

                                if (followUp.replyFollowUp?.followUpType == FollowUpType.Text) {
                                    viewHolder.itemView.txt_my_video_reply_text.text =
                                        followUp.replyFollowUp?.description
                                }
                            }
                            FollowUpType.File -> {
                                when (followUp.replyFollowUp?.fileTypeEnum) {
                                    FileTypeEnum.Unknown -> {

                                        viewHolder.itemView.txt_my_video_reply_text.text =
                                            "فایل  ${followUp.replyFollowUp?.fileExtension}"
                                        viewHolder.itemView.img_my_video_reply_thumbnail.visibility =
                                            View.INVISIBLE
                                    }
                                    FileTypeEnum.FaceStatus -> {

                                        viewHolder.itemView.txt_my_video_reply_text.text =
                                            "صورت وضعيت  ${followUp.replyFollowUp?.fileExtension}"
                                        viewHolder.itemView.img_my_video_reply_thumbnail.visibility =
                                            View.INVISIBLE
                                    }
                                    FileTypeEnum.Image
                                    -> {

                                        viewHolder.itemView.txt_my_video_reply_text.text =
                                            "تصویر"
                                        viewHolder.itemView.img_my_video_reply_thumbnail.visibility =
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
                                                .into(viewHolder.itemView.img_my_video_reply_thumbnail)
                                        } else {
                                            Glide.with(viewHolder.itemView.context)
                                                .load(Constants.FILE_BASE_URL + followUp.replyFollowUp?.fileName + "-.jpg")
                                                .centerCrop()
                                                .into(viewHolder.itemView.img_my_video_reply_thumbnail)
                                        }
                                    }
                                    FileTypeEnum.Video -> {

                                        viewHolder.itemView.txt_my_video_reply_text.text =
                                            "ویدئو"
                                        viewHolder.itemView.img_my_video_reply_thumbnail.visibility =
                                            View.VISIBLE
                                    }
                                    FileTypeEnum.Audio -> {

                                        viewHolder.itemView.txt_my_video_reply_text.text =
                                            "فایل صوتی"
                                        viewHolder.itemView.img_my_video_reply_thumbnail.visibility =
                                            View.INVISIBLE
                                    }
                                }
                            }
                        }

                        viewHolder.itemView.constraint_my_video_reply_layout.setOnClickListener {
                            onReplyFollowUpClicked?.OnReplyFollowUpClicked(followUp.replyFollowUp)
                        }
                    } else {
                        viewHolder.itemView.constraint_my_video_reply_layout.visibility =
                            View.GONE
                    }


                    viewHolder.itemView.progress_my_video.visibility =
                        View.INVISIBLE
                    viewHolder.itemView.btn_play_my_video.visibility =
                        View.VISIBLE


                    viewHolder.itemView.txt_username_my_video.text = followUp.createName
                    viewHolder.itemView.txt_semat_my_video.text = followUp.orgLevelTitle
                    viewHolder.itemView.txt_date_my_video.text = followUp.persianDate
                    viewHolder.itemView.txt_file_size_my_video.text =
                        followUp.fileSizeLabel

                    if (followUp.isUploadType) {
                        viewHolder.itemView.btn_play_my_video.setOnClickListener {
                            onPlayVideoClicked?.onPlayVideoClicked(
                                followUp.file,
                                followUp.file?.nameWithoutExtension ?: "",
                                Constants.FILE_BASE_URL + followUp.fileName
                            )
                        }

                    } else {
                        if (followUp.file?.exists() == true) {
                            viewHolder.itemView.btn_play_my_video.setOnClickListener {
                                onPlayVideoClicked?.onPlayVideoClicked(
                                    followUp.file,
                                    followUp.file?.nameWithoutExtension ?: "",
                                    Constants.FILE_BASE_URL + followUp.fileName
                                )
                            }
                        } else {
                            viewHolder.itemView.btn_play_my_video.setOnClickListener {
                                onPlayVideoClicked?.onPlayVideoClicked(
                                    null,
                                    (followUp.fileName ?: "".split("/")[1]).split(".")[0],
                                    Constants.FILE_BASE_URL + followUp.fileName
                                )
                            }
                        }
                    }

                    viewHolder.itemView.setOnLongClickListener {
                        onFollowupLongClickListener?.onFollowupLongClickListener(peigiriItem, true)
                        true
                    }
                }
                false -> {
                    if (followUp.isFixedHighLight) {
                        viewHolder.itemView.img_highlight_others_video.alpha = 1f
                    } else {
                        viewHolder.itemView.img_highlight_others_video.alpha = 0f
                    }

//                    if (!followUp.profileImage.isNullOrEmpty()) {
//                        Glide.with(viewHolder.itemView.context)
//                            .load(Constants.AVATAR_BASE_URL + followUp.profileImage)
//                            .into(viewHolder.itemView.img_avatar_others_video)
//                    }

                    if (followUp.isReply) {
                        viewHolder.itemView.constraint_others_video_reply_layout.visibility =
                            View.VISIBLE
                        viewHolder.itemView.txt_others_video_reply_username.text =
                            followUp.replyFollowUp?.createName

                        when (followUp.replyFollowUp?.followUpType) {
                            FollowUpType.Text -> {

                                if (followUp.replyFollowUp?.followUpType == FollowUpType.Text) {
                                    viewHolder.itemView.txt_others_video_reply_text.text =
                                        followUp.replyFollowUp?.description
                                }
                            }
                            FollowUpType.File -> {
                                when (followUp.replyFollowUp?.fileTypeEnum) {
                                    FileTypeEnum.Unknown -> {

                                        viewHolder.itemView.txt_others_video_reply_text.text =
                                            "فایل  ${followUp.replyFollowUp?.fileExtension}"
                                        viewHolder.itemView.img_others_video_reply_thumbnail.visibility =
                                            View.INVISIBLE
                                    }
                                    FileTypeEnum.FaceStatus -> {

                                        viewHolder.itemView.txt_others_video_reply_text.text =
                                            "صورت وضعيت  ${followUp.replyFollowUp?.fileExtension}"
                                        viewHolder.itemView.img_others_video_reply_thumbnail.visibility =
                                            View.INVISIBLE
                                    }
                                    FileTypeEnum.Image
                                    -> {

                                        viewHolder.itemView.txt_others_video_reply_text.text =
                                            "تصویر"
                                        viewHolder.itemView.img_others_video_reply_thumbnail.visibility =
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
                                                .into(viewHolder.itemView.img_others_video_reply_thumbnail)
                                        } else {
                                            Glide.with(viewHolder.itemView.context)
                                                .load(Constants.FILE_BASE_URL + followUp.replyFollowUp?.fileName + "-.jpg")
                                                .centerCrop()
                                                .into(viewHolder.itemView.img_others_video_reply_thumbnail)
                                        }
                                    }
                                    FileTypeEnum.Video -> {

                                        viewHolder.itemView.txt_others_video_reply_text.text =
                                            "ویدئو"
                                        viewHolder.itemView.img_others_video_reply_thumbnail.visibility =
                                            View.VISIBLE
                                    }
                                    FileTypeEnum.Audio -> {

                                        viewHolder.itemView.txt_others_video_reply_text.text =
                                            "فایل صوتی"
                                        viewHolder.itemView.img_others_video_reply_thumbnail.visibility =
                                            View.INVISIBLE
                                    }
                                }
                            }
                        }

                        viewHolder.itemView.constraint_others_video_reply_layout.setOnClickListener {
                            onReplyFollowUpClicked?.OnReplyFollowUpClicked(followUp.replyFollowUp)
                        }
                    } else {
                        viewHolder.itemView.constraint_others_video_reply_layout.visibility =
                            View.GONE
                    }


                    viewHolder.itemView.txt_username_others_video.text =
                        followUp.createName
                    viewHolder.itemView.txt_semat_others_video.text =
                        followUp.orgLevelTitle
                    viewHolder.itemView.txt_date_others_video.text =
                        followUp.persianDate
                    viewHolder.itemView.txt_file_size_others_video.text =
                        followUp.fileSizeLabel

                    if (followUp.file?.exists() == true) {
                        viewHolder.itemView.btn_play_others_video.setOnClickListener {
                            onPlayVideoClicked?.onPlayVideoClicked(
                                followUp.file,
                                followUp.file?.nameWithoutExtension ?: "",
                                Constants.FILE_BASE_URL + followUp.fileName
                            )
                        }
                    } else {
                        viewHolder.itemView.btn_play_others_video.setOnClickListener {
                            onPlayVideoClicked?.onPlayVideoClicked(
                                null,
                                (followUp.fileName ?: "".split("/")[1]).split(".")[0],
                                Constants.FILE_BASE_URL + followUp.fileName
                            )
                        }
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
