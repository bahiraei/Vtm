package ir.adak.Vect.Data.Models.PeigiriItem

import android.view.View
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ir.adak.Vect.BuildConfig
import ir.adak.Vect.Data.Enums.FileTypeEnum
import ir.adak.Vect.Data.Enums.FollowUpType
import ir.adak.Vect.Listeners.OnDownloadButtonClicked
import ir.adak.Vect.Listeners.OnFollowupLongClickListener
import ir.adak.Vect.Listeners.OnReplyFollowUpClicked
import ir.adak.Vect.R
import ir.adak.Vect.Utils.Constants
import kotlinx.android.synthetic.main.my_face_status_peigiri_layout.view.*
import kotlinx.android.synthetic.main.others_face_status_peigiri_layout.view.*

class BindFaceStatusFileFollowUp {

    companion object {
        fun Bind(
            viewHolder: ViewHolder,
            peigiriItem: PeigiriItem,
            onReplyFollowUpClicked: OnReplyFollowUpClicked?,
            onDownloadButtonClicked: OnDownloadButtonClicked?,
            onFollowupLongClickListener: OnFollowupLongClickListener?

        ) {
            val followUp = peigiriItem.followUp
            val orgLevelId = peigiriItem.orgLevelId

            when (followUp.orgLevelId == orgLevelId) {
                true -> {
                    if (followUp.isFixedHighLight) {
                        viewHolder.itemView.img_highlight_my_face_status.alpha = 1f
                    } else {
                        viewHolder.itemView.img_highlight_my_face_status.alpha = 0f
                    }


//                    if (!followUp.profileImage.isNullOrEmpty()) {
//                        Glide.with(viewHolder.itemView.context)
//                            .load(Constants.AVATAR_BASE_URL + followUp.profileImage)
//                            .into(viewHolder.itemView.img_avatar_my_face_status)
//                    }

                    if (followUp.isReply) {
                        viewHolder.itemView.constraint_my_face_status_reply_layout.visibility =
                            View.VISIBLE
                        viewHolder.itemView.txt_my_face_status_reply_username.text =
                            followUp.replyFollowUp?.createName

                        when (followUp.replyFollowUp?.followUpType) {
                            FollowUpType.Text -> {

                                if (followUp.replyFollowUp?.followUpType == FollowUpType.Text) {
                                    viewHolder.itemView.txt_my_face_status_reply_text.text =
                                        followUp.replyFollowUp?.description
                                }
                            }
                            FollowUpType.File -> {
                                when (followUp.replyFollowUp?.fileTypeEnum) {
                                    FileTypeEnum.Unknown -> {

                                        viewHolder.itemView.txt_my_face_status_reply_text.text =
                                            "فایل  ${followUp.replyFollowUp?.fileExtension}"
                                        viewHolder.itemView.img_my_face_status_reply_thumbnail.visibility =
                                            View.GONE
                                    }
                                    FileTypeEnum.FaceStatus -> {

                                        viewHolder.itemView.txt_my_face_status_reply_text.text =
                                            "صورت وضعيت  ${followUp.replyFollowUp?.fileExtension}"
                                        viewHolder.itemView.img_my_face_status_reply_thumbnail.visibility =
                                            View.GONE
                                    }
                                    FileTypeEnum.Image
                                    -> {

                                        viewHolder.itemView.txt_my_face_status_reply_text.text =
                                            "تصویر"
                                        viewHolder.itemView.img_my_face_status_reply_thumbnail.visibility =
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
                                                .into(viewHolder.itemView.img_my_face_status_reply_thumbnail)
                                        } else {
                                            Glide.with(viewHolder.itemView.context)
                                                .load(Constants.FILE_BASE_URL + followUp.replyFollowUp?.fileName + "-.jpg")
                                                .centerCrop()
                                                .into(viewHolder.itemView.img_my_face_status_reply_thumbnail)
                                        }
                                    }
                                    FileTypeEnum.Video -> {

                                        viewHolder.itemView.txt_my_face_status_reply_text.text =
                                            "ویدئو"
                                        viewHolder.itemView.img_my_face_status_reply_thumbnail.visibility =
                                            View.VISIBLE
                                    }
                                    FileTypeEnum.Audio -> {

                                        viewHolder.itemView.txt_my_face_status_reply_text.text =
                                            "فایل صوتی"
                                        viewHolder.itemView.img_my_face_status_reply_thumbnail.visibility =
                                            View.GONE
                                    }
                                }
                            }
                        }
                        viewHolder.itemView.constraint_my_face_status_reply_layout.setOnClickListener {
                            onReplyFollowUpClicked?.OnReplyFollowUpClicked(followUp.replyFollowUp)
                        }
                    } else {
                        viewHolder.itemView.constraint_my_face_status_reply_layout.visibility =
                            View.GONE
                    }


                    viewHolder.itemView.txt_username_my_face_status.text = followUp.createName
                    viewHolder.itemView.txt_semat_my_face_status.text = followUp.orgLevelTitle
                    viewHolder.itemView.txt_date_my_face_status.text = followUp.persianDate
                    viewHolder.itemView.txt_file_name_my_face_status.text = followUp.fileNameForShow
                    viewHolder.itemView.txt_file_size_my_face_status.text =
                        followUp.fileSizeLabel
                    viewHolder.itemView.circularProgressBar_my_face_status.progressMax = 100f
//                    viewHolder.itemView.txt_file_extension_my_face_status.text =
//                        followUp.fileExtension

                    if (followUp.isUploadType) {

                        viewHolder.itemView.circularProgressBar_my_face_status.progress =
                            followUp.fileUploadProgress.toFloat()

                        if (followUp.fileUploadProgress != 0 && followUp.fileUploadProgress != 100) {
                            viewHolder.itemView.btn_cancel_upload_my_face_status.setImageResource(
                                R.drawable.ic_close_black_24dp
                            )
                        } else {
                            viewHolder.itemView.btn_cancel_upload_my_face_status.setImageResource(
                                R.drawable.ic_tick
                            )
                        }
                    } else {
                        viewHolder.itemView.circularProgressBar_my_face_status.progress =
                            followUp.fileDownloadProgress.toFloat()

                        if (followUp.file?.exists() == true) {
                            if (followUp.fileDownloadProgress != 0 && followUp.fileDownloadProgress != 100) {
                                viewHolder.itemView.btn_cancel_upload_my_face_status.setImageResource(
                                    R.drawable.ic_close_black_24dp
                                )
                            } else {
                                viewHolder.itemView.btn_cancel_upload_my_face_status.setImageResource(
                                    R.drawable.ic_tick
                                )
                            }
                        } else {
                            viewHolder.itemView.btn_cancel_upload_my_face_status.setImageResource(
                                R.drawable.ic_arrow_downward_black_24dp
                            )

                            viewHolder.itemView.circularProgressBar_my_face_status.setOnClickListener {
                                onDownloadButtonClicked?.onDownloadButtonClicked(
                                    peigiriItem,
                                    false
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
                        viewHolder.itemView.img_highlight_others_face_status.alpha = 1f
                    } else {
                        viewHolder.itemView.img_highlight_others_face_status.alpha = 0f
                    }

//                    if (!followUp.profileImage.isNullOrEmpty()) {
//                        Glide.with(viewHolder.itemView.context)
//                            .load(Constants.AVATAR_BASE_URL + followUp.profileImage)
//                            .into(viewHolder.itemView.img_avatar_others_face_status)
//                    }

                    if (followUp.isReply) {
                        viewHolder.itemView.constraint_others_face_status_reply_layout.visibility =
                            View.VISIBLE
                        viewHolder.itemView.txt_others_face_status_reply_username.text =
                            followUp.replyFollowUp?.createName

                        when (followUp.replyFollowUp?.followUpType) {
                            FollowUpType.Text -> {

                                if (followUp.replyFollowUp?.followUpType == FollowUpType.Text) {
                                    viewHolder.itemView.txt_others_face_status_reply_text.text =
                                        followUp.replyFollowUp?.description
                                }
                            }
                            FollowUpType.File -> {
                                when (followUp.replyFollowUp?.fileTypeEnum) {
                                    FileTypeEnum.Unknown -> {

                                        viewHolder.itemView.txt_others_face_status_reply_text.text =
                                            "فایل  ${followUp.replyFollowUp?.fileExtension}"
                                        viewHolder.itemView.img_others_face_status_reply_thumbnail.visibility =
                                            View.GONE
                                    }
                                    FileTypeEnum.FaceStatus -> {

                                        viewHolder.itemView.txt_others_face_status_reply_text.text =
                                            "صورت وضعيت  ${followUp.replyFollowUp?.fileExtension}"
                                        viewHolder.itemView.img_others_face_status_reply_thumbnail.visibility =
                                            View.GONE
                                    }
                                    FileTypeEnum.Image -> {

                                        viewHolder.itemView.txt_others_face_status_reply_text.text =
                                            "تصویر"
                                        viewHolder.itemView.img_others_face_status_reply_thumbnail.visibility =
                                            View.VISIBLE
                                    }
                                    FileTypeEnum.Video -> {

                                        viewHolder.itemView.txt_others_face_status_reply_text.text =
                                            "ویدئو"
                                        viewHolder.itemView.img_others_face_status_reply_thumbnail.visibility =
                                            View.VISIBLE
                                    }
                                    FileTypeEnum.Audio -> {

                                        viewHolder.itemView.txt_others_face_status_reply_text.text =
                                            "فایل صوتی"
                                        viewHolder.itemView.img_others_face_status_reply_thumbnail.visibility =
                                            View.GONE
                                    }
                                }
                            }
                        }

                        viewHolder.itemView.constraint_others_face_status_reply_layout.setOnClickListener {
                            onReplyFollowUpClicked?.OnReplyFollowUpClicked(followUp.replyFollowUp)
                        }
                    } else {
                        viewHolder.itemView.constraint_others_face_status_reply_layout.visibility =
                            View.GONE
                    }


                    viewHolder.itemView.txt_username_others_face_status.text =
                        followUp.createName
                    viewHolder.itemView.txt_semat_others_face_status.text =
                        followUp.orgLevelTitle
                    viewHolder.itemView.txt_date_others_face_status.text = followUp.persianDate
                    viewHolder.itemView.txt_file_name_others_face_status.text =
                        followUp.fileNameForShow
                    viewHolder.itemView.txt_file_size_others_face_status.text =
                        followUp.fileSizeLabel
                    viewHolder.itemView.circularProgressBar_others_face_status.progressMax =
                        100f
//                    viewHolder.itemView.txt_file_extension_others_face_status.text =
//                        followUp.fileExtension

                    viewHolder.itemView.circularProgressBar_others_face_status.progress =
                        followUp.fileDownloadProgress.toFloat()

                    if (followUp.file?.exists() == true) {


                        if (followUp.fileDownloadProgress != 0 && followUp.fileDownloadProgress != 100) {
                            viewHolder.itemView.btn_cancel_download_others_face_status.setImageResource(
                                R.drawable.ic_close_black_24dp
                            )
                        } else {
                            viewHolder.itemView.btn_cancel_download_others_face_status.setImageResource(
                                R.drawable.ic_tick
                            )
                        }
                    } else {
                        viewHolder.itemView.btn_cancel_download_others_face_status.setImageResource(
                            R.drawable.ic_arrow_downward_black_24dp
                        )

                        viewHolder.itemView.circularProgressBar_others_face_status.setOnClickListener {
                            onDownloadButtonClicked?.onDownloadButtonClicked(
                                peigiriItem,
                                false
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
