package ir.adak.Vect.Data.Models.PeigiriItem

import android.view.View
import android.widget.SeekBar
import androidx.core.content.FileProvider
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ir.adak.Vect.BuildConfig
import ir.adak.Vect.Data.Enums.FileTypeEnum
import ir.adak.Vect.Data.Enums.FollowUpType
import ir.adak.Vect.Listeners.OnCancelUploadClicked
import ir.adak.Vect.Listeners.OnDownloadButtonClicked
import ir.adak.Vect.Listeners.OnPlayPauseAudioClicked
import ir.adak.Vect.Listeners.OnPlayerSeekBarChangedListener
import ir.adak.Vect.R
import kotlinx.android.synthetic.main.my_audio_peigiri_layout.view.*
import kotlinx.android.synthetic.main.my_file_peigiri_layout.view.*
import kotlinx.android.synthetic.main.my_pic_peigiri_layout.view.*
import kotlinx.android.synthetic.main.my_video_peigiri_layout.view.*
import kotlinx.android.synthetic.main.others_audio_peigiri_layout.view.*
import kotlinx.android.synthetic.main.others_file_peigiri_layout.view.*
import kotlinx.android.synthetic.main.others_pic_peigiri_layout.view.*
import kotlinx.android.synthetic.main.my_face_status_peigiri_layout.view.*
import kotlinx.android.synthetic.main.others_face_status_peigiri_layout.view.*
import java.util.concurrent.TimeUnit


class BindProgressChangeFollowUp {
    companion object {
        fun Bind(
            viewHolder: ViewHolder,
            peigiriItem: PeigiriItem,
            onDownloadButtonClicked: OnDownloadButtonClicked?,
            onCancelUploadClicked: OnCancelUploadClicked?,
            onPlayPauseAudioClicked: OnPlayPauseAudioClicked?,
            onPlayerSeekBarChangedListener: OnPlayerSeekBarChangedListener?
        ) {
            val followUp = peigiriItem.followUp
            val orgLevelId = peigiriItem.orgLevelId
            if (followUp.followUpType == FollowUpType.File) {
                when (followUp.fileTypeEnum) {
                    FileTypeEnum.Unknown -> {
                        when (followUp.orgLevelId == orgLevelId) {
                            true -> {
                                viewHolder.itemView.circularProgressBar_my_file.progressMax =
                                    100f
                                if (followUp.isUploadType) {
                                    viewHolder.itemView.circularProgressBar_my_file.progress =
                                        followUp.fileUploadProgress.toFloat()
                                    if (followUp.fileUploadProgress != 0 && followUp.fileUploadProgress != 100) {
                                        viewHolder.itemView.btn_cancel_upload_my_file.setImageResource(
                                            R.drawable.ic_close_black_24dp
                                        )
                                        viewHolder.itemView.circularProgressBar_my_file.setOnClickListener {
                                            onCancelUploadClicked?.onCancelUploadClicked(
                                                peigiriItem
                                            )
                                        }
                                    } else {
                                        viewHolder.itemView.btn_cancel_upload_my_file.setImageResource(
                                            R.drawable.ic_tick
                                        )
                                    }
                                } else {
                                    viewHolder.itemView.circularProgressBar_my_file.progress =
                                        followUp.fileDownloadProgress.toFloat()
                                    if (followUp.fileDownloadProgress != 0 && followUp.fileDownloadProgress != 100) {
                                        viewHolder.itemView.btn_cancel_upload_my_file.setImageResource(
                                            R.drawable.ic_close_black_24dp
                                        )

                                        viewHolder.itemView.circularProgressBar_my_file.setOnClickListener {
                                            onDownloadButtonClicked?.onDownloadButtonClicked(
                                                peigiriItem,
                                                true
                                            )
                                        }

                                    } else {
                                        viewHolder.itemView.btn_cancel_upload_my_file.setImageResource(
                                            R.drawable.ic_tick
                                        )
                                    }

                                }
                            }
                            false -> {
                                viewHolder.itemView.circularProgressBar_others_file.progressMax =
                                    100f
                                viewHolder.itemView.circularProgressBar_others_file.progress =
                                    followUp.fileDownloadProgress.toFloat()

                                if (followUp.fileDownloadProgress != 0 && followUp.fileDownloadProgress != 100) {
                                    viewHolder.itemView.btn_cancel_download_others_file.setImageResource(
                                        R.drawable.ic_close_black_24dp
                                    )

                                    viewHolder.itemView.circularProgressBar_others_file.setOnClickListener {
                                        onDownloadButtonClicked?.onDownloadButtonClicked(
                                            peigiriItem,
                                            true
                                        )
                                    }

                                } else {
                                    viewHolder.itemView.btn_cancel_download_others_file.setImageResource(
                                        R.drawable.ic_tick
                                    )
                                }

                            }
                        }
                    }
                    FileTypeEnum.FaceStatus -> {
                        when (followUp.orgLevelId == orgLevelId) {
                            true -> {
                                viewHolder.itemView.circularProgressBar_my_face_status.progressMax =
                                    100f
                                if (followUp.isUploadType) {
                                    viewHolder.itemView.circularProgressBar_my_face_status.progress =
                                        followUp.fileUploadProgress.toFloat()
                                    if (followUp.fileUploadProgress != 0 && followUp.fileUploadProgress != 100) {
                                        viewHolder.itemView.btn_cancel_upload_my_face_status.setImageResource(
                                            R.drawable.ic_close_black_24dp
                                        )
                                        viewHolder.itemView.circularProgressBar_my_face_status.setOnClickListener {
                                            onCancelUploadClicked?.onCancelUploadClicked(
                                                peigiriItem
                                            )
                                        }
                                    } else {
                                        viewHolder.itemView.btn_cancel_upload_my_face_status.setImageResource(
                                            R.drawable.ic_tick
                                        )
                                    }
                                } else {
                                    viewHolder.itemView.circularProgressBar_my_face_status.progress =
                                        followUp.fileDownloadProgress.toFloat()
                                    if (followUp.fileDownloadProgress != 0 && followUp.fileDownloadProgress != 100) {
                                        viewHolder.itemView.btn_cancel_upload_my_face_status.setImageResource(
                                            R.drawable.ic_close_black_24dp
                                        )

                                        viewHolder.itemView.circularProgressBar_my_face_status.setOnClickListener {
                                            onDownloadButtonClicked?.onDownloadButtonClicked(
                                                peigiriItem,
                                                true
                                            )
                                        }

                                    } else {
                                        viewHolder.itemView.btn_cancel_upload_my_face_status.setImageResource(
                                            R.drawable.ic_tick
                                        )
                                    }

                                }
                            }
                            false -> {
                                viewHolder.itemView.circularProgressBar_others_face_status.progressMax =
                                    100f
                                viewHolder.itemView.circularProgressBar_others_face_status.progress =
                                    followUp.fileDownloadProgress.toFloat()

                                if (followUp.fileDownloadProgress != 0 && followUp.fileDownloadProgress != 100) {
                                    viewHolder.itemView.btn_cancel_download_others_face_status.setImageResource(
                                        R.drawable.ic_close_black_24dp
                                    )

                                    viewHolder.itemView.circularProgressBar_others_face_status.setOnClickListener {
                                        onDownloadButtonClicked?.onDownloadButtonClicked(
                                            peigiriItem,
                                            true
                                        )
                                    }

                                } else {
                                    viewHolder.itemView.btn_cancel_download_others_face_status.setImageResource(
                                        R.drawable.ic_tick
                                    )
                                }

                            }
                        }
                    }
                    FileTypeEnum.Image -> {
                        when (followUp.orgLevelId == orgLevelId) {
                            true -> {
                                viewHolder.itemView.circularProgressBar_my_pic.progressMax =
                                    100f
                                if (followUp.isUploadType) {
                                    val uri = FileProvider.getUriForFile(
                                        viewHolder.itemView.context,
                                        BuildConfig.APPLICATION_ID + ".fileprovider",
                                        followUp.file!!
                                    )
                                    viewHolder.itemView.img_my_pic.setImageURI(
                                        uri,
                                        viewHolder.itemView.context
                                    )
//                                            Glide.with(viewHolder.itemView.context)
//                                                .load(followUp.file)
//                                                .apply(App.requestOptions)
////                                        .thumbnail(0.01f)
//                                                .override(250, 200)
//                                                .centerCrop()
//                                                .into(viewHolder.itemView.img_my_pic)

                                    viewHolder.itemView.circularProgressBar_my_pic.progress =
                                        followUp.fileUploadProgress.toFloat()

                                    if (followUp.fileUploadProgress != 0 && followUp.fileUploadProgress != 100) {

                                        viewHolder.itemView.circularProgressBar_my_pic.visibility =
                                            View.VISIBLE
                                        viewHolder.itemView.btn_cancel_upload_my_pic.visibility =
                                            View.VISIBLE
                                        viewHolder.itemView.btn_cancel_upload_my_pic.setImageResource(
                                            R.drawable.ic_close_black_24dp
                                        )
                                        viewHolder.itemView.circularProgressBar_my_pic.setOnClickListener {
                                            onCancelUploadClicked?.onCancelUploadClicked(
                                                peigiriItem
                                            )
                                        }
                                    } else {
                                        viewHolder.itemView.circularProgressBar_my_pic.visibility =
                                            View.GONE
                                        viewHolder.itemView.btn_cancel_upload_my_pic.visibility =
                                            View.GONE
                                    }
                                } else {
                                    viewHolder.itemView.circularProgressBar_my_pic.progress =
                                        followUp.fileDownloadProgress.toFloat()

                                    if (followUp.fileDownloadProgress != 0 && followUp.fileDownloadProgress != 100) {
                                        viewHolder.itemView.circularProgressBar_my_pic.visibility =
                                            View.VISIBLE
                                        viewHolder.itemView.btn_cancel_upload_my_pic.visibility =
                                            View.VISIBLE
                                        viewHolder.itemView.btn_cancel_upload_my_pic.setImageResource(
                                            R.drawable.ic_close_black_24dp
                                        )

                                        viewHolder.itemView.circularProgressBar_my_pic.setOnClickListener {
                                            onDownloadButtonClicked?.onDownloadButtonClicked(
                                                peigiriItem,
                                                true
                                            )
                                        }
                                    } else {
                                        viewHolder.itemView.circularProgressBar_my_pic.visibility =
                                            View.VISIBLE
                                        viewHolder.itemView.btn_cancel_upload_my_pic.visibility =
                                            View.VISIBLE
                                        viewHolder.itemView.btn_cancel_upload_my_pic.setImageResource(
                                            R.drawable.ic_arrow_downward_black_24dp
                                        )
                                    }
                                }
                            }
                            false -> {

                                viewHolder.itemView.circularProgressBar_others_pic.progressMax =
                                    100f

                                viewHolder.itemView.circularProgressBar_others_pic.progress =
                                    followUp.fileDownloadProgress.toFloat()

                                if (followUp.fileDownloadProgress != 0 && followUp.fileDownloadProgress != 100) {
                                    viewHolder.itemView.circularProgressBar_others_pic.visibility =
                                        View.VISIBLE
                                    viewHolder.itemView.btn_cancel_download_others_pic.visibility =
                                        View.VISIBLE
                                    viewHolder.itemView.img_others_pic_foreground.visibility =
                                        View.VISIBLE

                                    viewHolder.itemView.btn_cancel_download_others_pic.setImageResource(
                                        R.drawable.ic_close_black_24dp
                                    )
                                    viewHolder.itemView.circularProgressBar_others_pic.setOnClickListener {
                                        onDownloadButtonClicked?.onDownloadButtonClicked(
                                            peigiriItem,
                                            true
                                        )
                                    }
                                } else {
                                    viewHolder.itemView.circularProgressBar_others_pic.visibility =
                                        View.GONE
                                    viewHolder.itemView.btn_cancel_download_others_pic.visibility =
                                        View.GONE
                                    viewHolder.itemView.img_others_pic_foreground.visibility =
                                        View.GONE
                                }

                            }
                        }
                    }
                    FileTypeEnum.Video -> {
                        when (followUp.orgLevelId == orgLevelId) {
                            true -> {
                                viewHolder.itemView.circularProgressBar_my_video.progressMax =
                                    100f
                                if (followUp.isUploadType) {
                                    viewHolder.itemView.circularProgressBar_my_video.progress =
                                        followUp.fileUploadProgress.toFloat()

                                    if (followUp.fileUploadProgress != 0 && followUp.fileUploadProgress != 100) {

                                        viewHolder.itemView.progress_my_video.visibility =
                                            View.VISIBLE
                                        viewHolder.itemView.btn_play_my_video.visibility =
                                            View.INVISIBLE
                                        viewHolder.itemView.btn_cancel_upload_my_video.setImageResource(
                                            R.drawable.ic_close_black_24dp
                                        )
                                        viewHolder.itemView.circularProgressBar_my_video.setOnClickListener {
                                            onCancelUploadClicked?.onCancelUploadClicked(
                                                peigiriItem
                                            )
                                        }
                                    } else {
                                        viewHolder.itemView.progress_my_video.visibility =
                                            View.INVISIBLE
                                        viewHolder.itemView.btn_play_my_video.visibility =
                                            View.VISIBLE
                                    }
                                }
                            }
                            false -> {

                            }
                        }

                    }
                    FileTypeEnum.Audio -> {
                        when (followUp.orgLevelId == orgLevelId) {
                            true -> {
                                if (followUp.isUploadType) {
                                    viewHolder.itemView.circularProgressBar_my_audio.visibility =
                                        View.VISIBLE
                                    viewHolder.itemView.btn_cancel_upload_my_audio.visibility =
                                        View.VISIBLE
                                    viewHolder.itemView.img_play_pause_my_audio.visibility =
                                        View.INVISIBLE

                                    viewHolder.itemView.circularProgressBar_my_audio.progress =
                                        followUp.fileUploadProgress.toFloat()

                                    if (followUp.fileUploadProgress != 0 && followUp.fileUploadProgress != 100) {
                                        viewHolder.itemView.btn_cancel_upload_my_audio.setImageResource(
                                            R.drawable.ic_close_black_24dp
                                        )
                                    } else {
                                        viewHolder.itemView.circularProgressBar_my_audio.visibility =
                                            View.INVISIBLE
                                        viewHolder.itemView.btn_cancel_upload_my_audio.visibility =
                                            View.INVISIBLE
                                        viewHolder.itemView.img_play_pause_my_audio.visibility =
                                            View.VISIBLE
                                    }
                                } else {
                                    viewHolder.itemView.circularProgressBar_my_audio.visibility =
                                        View.INVISIBLE
                                    viewHolder.itemView.btn_cancel_upload_my_audio.visibility =
                                        View.INVISIBLE
                                    viewHolder.itemView.img_play_pause_my_audio.visibility =
                                        View.VISIBLE


                                    var isSeeking = false

                                    if (!isSeeking) {
                                        viewHolder.itemView.seekbar_my_audio.max =
                                            followUp.trackDuration.toInt()
                                        viewHolder.itemView.seekbar_my_audio.progress =
                                            followUp.currentPlayingPosition.toInt()
                                    }
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

                                    viewHolder.itemView.img_play_pause_my_audio.setOnClickListener {
                                        onPlayPauseAudioClicked?.onPlayPauseAudioClicked(peigiriItem)
                                    }
                                    viewHolder.itemView.seekbar_my_audio.setOnSeekBarChangeListener(
                                        object : SeekBar.OnSeekBarChangeListener {
                                            override fun onProgressChanged(
                                                seekBar: SeekBar?,
                                                progress: Int,
                                                fromUser: Boolean
                                            ) {
                                                if (fromUser) {
                                                    onPlayerSeekBarChangedListener?.onPlayerSeekBarChangedListener(
                                                        peigiriItem,
                                                        progress.toLong()
                                                    )
                                                }
                                            }

                                            override fun onStartTrackingTouch(p0: SeekBar?) {
                                                isSeeking = true
                                            }

                                            override fun onStopTrackingTouch(p0: SeekBar?) {
                                                isSeeking = false
                                            }
                                        })

                                }

                            }
                            false -> {
                                var isSeeking = false
                                if (!isSeeking) {
                                    viewHolder.itemView.seekbar_others_audio.max =
                                        followUp.trackDuration.toInt()
                                    viewHolder.itemView.seekbar_others_audio.progress =
                                        followUp.currentPlayingPosition.toInt()
                                }
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


                                viewHolder.itemView.img_play_pause_others_audio.setOnClickListener {
                                    onPlayPauseAudioClicked?.onPlayPauseAudioClicked(peigiriItem)
                                }

                                viewHolder.itemView.seekbar_others_audio.setOnSeekBarChangeListener(
                                    object : SeekBar.OnSeekBarChangeListener {
                                        override fun onProgressChanged(
                                            seekBar: SeekBar?,
                                            progress: Int,
                                            fromUser: Boolean
                                        ) {
                                            if (fromUser) {
                                                onPlayerSeekBarChangedListener?.onPlayerSeekBarChangedListener(
                                                    peigiriItem,
                                                    progress.toLong()
                                                )
                                            }
                                        }

                                        override fun onStartTrackingTouch(p0: SeekBar?) {
                                            isSeeking = true
                                        }

                                        override fun onStopTrackingTouch(p0: SeekBar?) {
                                            isSeeking = false
                                        }
                                    })
                            }
                        }
                    }
                }
            }
        }
    }
}