package ir.adak.Vect.Data.Models.PeigiriItem

import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ir.adak.Vect.Data.Enums.FileTypeEnum
import ir.adak.Vect.Listeners.*

class BindFileFollowUp {
    companion object {
        fun Bind(
            viewHolder: ViewHolder,
            peigiriItem: PeigiriItem,
            onReplyFollowUpClicked: OnReplyFollowUpClicked?,
            onFollowupLongClickListener: OnFollowupLongClickListener?,
            onDownloadButtonClicked: OnDownloadButtonClicked?,
            onPlayVideoClicked: OnPlayVideoClicked?,
            onPlayPauseAudioClicked: OnPlayPauseAudioClicked?
        ) {
            val followUp = peigiriItem.followUp
            val orgLevelId = peigiriItem.orgLevelId
            when (followUp.fileTypeEnum) {
                FileTypeEnum.Unknown -> {
                    BindUnknownFileFollowUp.Bind(
                        viewHolder,
                        peigiriItem,
                        onReplyFollowUpClicked,
                        onDownloadButtonClicked,
                        onFollowupLongClickListener
                    )

                }
                FileTypeEnum.Image -> {
                    BindImageFileFollowUp.Bind(
                        viewHolder,
                        peigiriItem,
                        onReplyFollowUpClicked,
                        onDownloadButtonClicked,
                        onFollowupLongClickListener
                    )

                }
                FileTypeEnum.Video -> {
                    BindVideoFileFollowUp.Bind(
                        viewHolder,
                        peigiriItem,
                        onReplyFollowUpClicked,
                        onFollowupLongClickListener,
                        onPlayVideoClicked
                    )
                }
                FileTypeEnum.Audio -> {
                    BindAudioFileFollowUp.Bind(
                        viewHolder,
                        peigiriItem,
                        onPlayPauseAudioClicked,
                        onReplyFollowUpClicked,
                        onFollowupLongClickListener
                    )
                }
                FileTypeEnum.FaceStatus -> {
                    BindFaceStatusFileFollowUp.Bind(
                        viewHolder,
                        peigiriItem,
                        onReplyFollowUpClicked,
                        onDownloadButtonClicked,
                        onFollowupLongClickListener
                    )
                }
            }

        }
    }

}