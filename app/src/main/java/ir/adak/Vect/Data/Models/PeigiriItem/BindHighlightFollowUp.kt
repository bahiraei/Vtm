package ir.adak.Vect.Data.Models.PeigiriItem

import android.animation.ValueAnimator
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ir.adak.Vect.Data.Enums.FileTypeEnum
import ir.adak.Vect.Data.Enums.FollowUpType
import ir.adak.Vect.UI.Activities.PeigiriActivity.PeigiriActivity
import kotlinx.android.synthetic.main.my_audio_peigiri_layout.view.*
import kotlinx.android.synthetic.main.my_face_status_peigiri_layout.view.*
import kotlinx.android.synthetic.main.my_file_peigiri_layout.view.*
import kotlinx.android.synthetic.main.my_normal_text_peigiri_layout.view.*
import kotlinx.android.synthetic.main.my_pic_peigiri_layout.view.*
import kotlinx.android.synthetic.main.my_video_peigiri_layout.view.*
import kotlinx.android.synthetic.main.others_audio_peigiri_layout.view.*
import kotlinx.android.synthetic.main.others_face_status_peigiri_layout.view.*
import kotlinx.android.synthetic.main.others_file_peigiri_layout.view.*
import kotlinx.android.synthetic.main.others_normal_text_peigiri_layout.view.*
import kotlinx.android.synthetic.main.others_pic_peigiri_layout.view.*
import kotlinx.android.synthetic.main.others_video_peigiri_layout.view.*

class BindHighlightFollowUp {
    companion object {
        fun Bind(
            viewHolder: ViewHolder,
            peigiriItem: PeigiriItem
        ) {
            val followUp = peigiriItem.followUp
            val orgLevelId = peigiriItem.orgLevelId
            val alphaAnimator: ValueAnimator = ValueAnimator.ofFloat(1f, 0f)

            when (followUp.followUpType) {
                FollowUpType.Text -> {
                    when (followUp.orgLevelId == orgLevelId) {
                        true -> {
                            if (followUp.isHighlight) {
                                viewHolder.itemView.img_highlight_my_normal_text.alpha = 1f
                                if (!followUp.isFixedHighLight) {
                                    if (!alphaAnimator.isRunning) {
                                        alphaAnimator.duration = 2000
                                        alphaAnimator.addUpdateListener {
                                            val animatedValue = it.animatedValue as Float
                                            viewHolder.itemView.img_highlight_my_normal_text.alpha =
                                                animatedValue
                                        }
                                        followUp.isHighlight = false
                                        PeigiriActivity.highlightPosition = -1
                                        alphaAnimator.startDelay = 1000
                                        alphaAnimator.start()
                                    }
                                }
                                followUp.isFixedHighLight = false
                            }else{
                                viewHolder.itemView.img_highlight_my_normal_text.alpha = 0f
                            }
                        }
                        false -> {

                            if (followUp.isHighlight) {
                                viewHolder.itemView.img_highlight_others_normal_text.alpha =
                                    1f
                                if (!followUp.isFixedHighLight) {
                                    if (!alphaAnimator.isRunning) {
                                        alphaAnimator.duration = 2000
                                        alphaAnimator.addUpdateListener {
                                            val animatedValue = it.animatedValue as Float
                                            viewHolder.itemView.img_highlight_others_normal_text.alpha =
                                                animatedValue
                                        }
                                        followUp.isHighlight = false
                                        PeigiriActivity.highlightPosition = -1
                                        alphaAnimator.startDelay = 1000
                                        alphaAnimator.start()
                                    }
                                }
                                followUp.isFixedHighLight = false
                            }else{
                                viewHolder.itemView.img_highlight_others_normal_text.alpha = 0f
                            }
                        }
                    }
                }
                FollowUpType.File -> {
                    when (followUp.fileTypeEnum) {
                        FileTypeEnum.Unknown -> {
                            when (followUp.orgLevelId == orgLevelId) {
                                true -> {

                                    if (followUp.isHighlight) {
                                        viewHolder.itemView.img_highlight_my_file.alpha = 1f
                                        if (!followUp.isFixedHighLight) {
                                            if (!alphaAnimator.isRunning) {
                                                alphaAnimator.duration = 2000
                                                alphaAnimator.addUpdateListener {
                                                    val animatedValue = it.animatedValue as Float
                                                    viewHolder.itemView.img_highlight_my_file.alpha =
                                                        animatedValue
                                                }
                                                followUp.isHighlight = false
                                                PeigiriActivity.highlightPosition = -1
                                                alphaAnimator.startDelay = 1000
                                                alphaAnimator.start()
                                            }
                                        }
                                        followUp.isFixedHighLight = false
                                    }else{
                                        viewHolder.itemView.img_highlight_my_file.alpha = 0f
                                    }
                                }
                                false -> {

                                    if (followUp.isHighlight) {
                                        viewHolder.itemView.img_highlight_others_file.alpha =
                                            1f
                                        if (!followUp.isFixedHighLight) {
                                            if (!alphaAnimator.isRunning) {
                                                alphaAnimator.duration = 2000
                                                alphaAnimator.addUpdateListener {
                                                    val animatedValue = it.animatedValue as Float
                                                    viewHolder.itemView.img_highlight_others_file.alpha =
                                                        animatedValue
                                                }
                                                followUp.isHighlight = false
                                                PeigiriActivity.highlightPosition = -1
                                                alphaAnimator.startDelay = 1000
                                                alphaAnimator.start()
                                            }
                                        }
                                        followUp.isFixedHighLight = false
                                    }else{
                                        viewHolder.itemView.img_highlight_others_file.alpha = 0f
                                    }
                                }
                            }
                        }
                        FileTypeEnum.FaceStatus -> {
                            when (followUp.orgLevelId == orgLevelId) {
                                true -> {

                                    if (followUp.isHighlight) {
                                        viewHolder.itemView.img_highlight_my_face_status.alpha = 1f
                                        if (!followUp.isFixedHighLight) {
                                            if (!alphaAnimator.isRunning) {
                                                alphaAnimator.duration = 2000
                                                alphaAnimator.addUpdateListener {
                                                    val animatedValue = it.animatedValue as Float
                                                    viewHolder.itemView.img_highlight_my_face_status.alpha =
                                                        animatedValue
                                                }
                                                followUp.isHighlight = false
                                                PeigiriActivity.highlightPosition = -1
                                                alphaAnimator.startDelay = 1000
                                                alphaAnimator.start()
                                            }
                                        }
                                        followUp.isFixedHighLight = false
                                    }else{
                                        viewHolder.itemView.img_highlight_my_face_status.alpha = 0f
                                    }
                                }
                                false -> {

                                    if (followUp.isHighlight) {
                                        viewHolder.itemView.img_highlight_others_face_status.alpha =
                                            1f
                                        if (!followUp.isFixedHighLight) {
                                            if (!alphaAnimator.isRunning) {
                                                alphaAnimator.duration = 2000
                                                alphaAnimator.addUpdateListener {
                                                    val animatedValue = it.animatedValue as Float
                                                    viewHolder.itemView.img_highlight_others_face_status.alpha =
                                                        animatedValue
                                                }
                                                followUp.isHighlight = false
                                                PeigiriActivity.highlightPosition = -1
                                                alphaAnimator.startDelay = 1000
                                                alphaAnimator.start()
                                            }
                                        }
                                        followUp.isFixedHighLight = false
                                    }else{
                                        viewHolder.itemView.img_highlight_others_face_status.alpha = 0f
                                    }
                                }
                            }
                        }
                        FileTypeEnum.Image -> {
                            when (followUp.orgLevelId == orgLevelId) {
                                true -> {
                                    if (followUp.isHighlight) {
                                        viewHolder.itemView.img_highlight_my_pic.alpha = 1f
                                        if (!followUp.isFixedHighLight) {
                                            if (!alphaAnimator.isRunning) {
                                                alphaAnimator.duration = 2000
                                                alphaAnimator.addUpdateListener {
                                                    val animatedValue = it.animatedValue as Float
                                                    viewHolder.itemView.img_highlight_my_pic.alpha =
                                                        animatedValue
                                                }
                                                followUp.isHighlight = false
                                                PeigiriActivity.highlightPosition = -1
                                                alphaAnimator.startDelay = 1000
                                                alphaAnimator.start()
                                            }
                                        }
                                        followUp.isFixedHighLight = false
                                    }else{
                                        viewHolder.itemView.img_highlight_my_pic.alpha = 0f
                                    }
                                }
                                false -> {

                                    if (followUp.isHighlight) {
                                        viewHolder.itemView.img_highlight_others_pic.alpha =
                                            1f
                                        if (!followUp.isFixedHighLight) {
                                            if (!alphaAnimator.isRunning) {
                                                alphaAnimator.duration = 2000
                                                alphaAnimator.addUpdateListener {
                                                    val animatedValue = it.animatedValue as Float
                                                    viewHolder.itemView.img_highlight_others_pic.alpha =
                                                        animatedValue
                                                }
                                                followUp.isHighlight = false
                                                PeigiriActivity.highlightPosition = -1
                                                alphaAnimator.startDelay = 1000
                                                alphaAnimator.start()
                                            }
                                        }
                                        followUp.isFixedHighLight = false
                                    }else{
                                        viewHolder.itemView.img_highlight_others_pic.alpha = 0f
                                    }
                                }
                            }
                        }
                        FileTypeEnum.Video -> {
                            when (followUp.orgLevelId == orgLevelId) {
                                true -> {
                                    if (followUp.isHighlight) {
                                        viewHolder.itemView.img_highlight_my_video.alpha =
                                            1f
                                        if (!followUp.isFixedHighLight) {
                                            if (!alphaAnimator.isRunning) {
                                                alphaAnimator.duration = 2000
                                                alphaAnimator.addUpdateListener {
                                                    val animatedValue = it.animatedValue as Float
                                                    viewHolder.itemView.img_highlight_my_video.alpha =
                                                        animatedValue
                                                }
                                                followUp.isHighlight = false
                                                PeigiriActivity.highlightPosition = -1
                                                alphaAnimator.startDelay = 1000
                                                alphaAnimator.start()
                                            }
                                        }
                                        followUp.isFixedHighLight = false
                                    }else{
                                        viewHolder.itemView.img_highlight_my_video.alpha = 0f
                                    }
                                }
                                false -> {
                                    if (followUp.isHighlight) {
                                        viewHolder.itemView.img_highlight_others_video.alpha =
                                            1f
                                        if (!followUp.isFixedHighLight) {
                                            if (!alphaAnimator.isRunning) {
                                                alphaAnimator.duration = 2000
                                                alphaAnimator.addUpdateListener {
                                                    val animatedValue = it.animatedValue as Float
                                                    viewHolder.itemView.img_highlight_others_video.alpha =
                                                        animatedValue
                                                }
                                                followUp.isHighlight = false
                                                PeigiriActivity.highlightPosition = -1
                                                alphaAnimator.startDelay = 1000
                                                alphaAnimator.start()
                                            }
                                        }
                                        followUp.isFixedHighLight = false
                                    }else{
                                        viewHolder.itemView.img_highlight_others_video.alpha = 0f
                                    }
                                }
                            }
                        }
                        FileTypeEnum.Audio -> {
                            when (followUp.orgLevelId == orgLevelId) {
                                true -> {
                                    if (followUp.isHighlight) {
                                        viewHolder.itemView.img_highlight_my_audio.alpha =
                                            1f
                                        if (!followUp.isFixedHighLight) {
                                            if (!alphaAnimator.isRunning) {
                                                alphaAnimator.duration = 2000
                                                alphaAnimator.addUpdateListener {
                                                    val animatedValue = it.animatedValue as Float
                                                    viewHolder.itemView.img_highlight_my_audio.alpha =
                                                        animatedValue
                                                }
                                                followUp.isHighlight = false
                                                PeigiriActivity.highlightPosition = -1
                                                alphaAnimator.startDelay = 1000
                                                alphaAnimator.start()
                                            }
                                        }
                                        followUp.isFixedHighLight = false
                                    }else{
                                        viewHolder.itemView.img_highlight_my_audio.alpha = 0f
                                    }
                                }
                                false -> {
                                    if (followUp.isHighlight) {
                                        viewHolder.itemView.img_highlight_others_audio.alpha =
                                            1f
                                        if (!followUp.isFixedHighLight) {
                                            if (!alphaAnimator.isRunning) {
                                                alphaAnimator.duration = 2000
                                                alphaAnimator.addUpdateListener {
                                                    val animatedValue = it.animatedValue as Float
                                                    viewHolder.itemView.img_highlight_others_audio.alpha =
                                                        animatedValue
                                                }
                                                followUp.isHighlight = false
                                                PeigiriActivity.highlightPosition = -1
                                                alphaAnimator.startDelay = 1000
                                                alphaAnimator.start()
                                            }
                                        }
                                        followUp.isFixedHighLight = false
                                    }else{
                                        viewHolder.itemView.img_highlight_others_audio.alpha = 0f
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}