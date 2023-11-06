package ir.adak.Vect.UI.Activities.VideoPlayerActivity

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.FileProvider
import com.halilibo.bvpkotlin.BetterVideoPlayer
import com.halilibo.bvpkotlin.VideoCallback
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import ir.adak.Vect.BuildConfig
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.BaseActivity
import kotlinx.android.synthetic.main.activity_video_player.*
import java.io.File
import com.vincan.medialoader.MediaLoader


class VideoPlayerActivity : BaseActivity() {
    private var id: Int? = 0
    private var videoName: String? = ""
    private var videoUrl: String? = ""
    private var file: File? = null
    var onPlayVideoError: OnPlayVideoError? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContentView(R.layout.activity_video_player)

        try {
            file =
                if (intent.extras?.getString("file") != null) File(intent.extras?.getString("file")) else null

            videoUrl = intent.extras?.getString("videoUrl")
            videoName = intent.extras?.getString("videoName")
            id = intent.extras?.getInt("id")

            if (file != null) {
                val uri = FileProvider.getUriForFile(
                    this,
                    BuildConfig.APPLICATION_ID + ".fileprovider",
                    file!!
                )
                player.setSource(uri)
            } else {
                val proxyUrl = MediaLoader.getInstance(this).getProxyUrl(videoUrl)
                player.setSource(Uri.parse(proxyUrl))
            }

            player.enableSwipeGestures()
            player.setCallback(object : VideoCallback {
                override fun onStarted(player: BetterVideoPlayer) {

                }

                override fun onPaused(player: BetterVideoPlayer) {

                }

                override fun onPreparing(player: BetterVideoPlayer) {

                }

                override fun onPrepared(player: BetterVideoPlayer) {

                }

                override fun onBuffering(percent: Int) {

                }

                override fun onError(player: BetterVideoPlayer, e: Exception) {
                    onPlayVideoError?.onError(e)
                    finish()
                }

                override fun onCompletion(player: BetterVideoPlayer) {

                }

                override fun onToggleControls(player: BetterVideoPlayer, isShowing: Boolean) {

                }
            })
            player.start()



            btn_download.setOnClickListener {
                Dexter.withActivity(this@VideoPlayerActivity).withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            var url = videoUrl ?: ""
                            if (!url.startsWith("http://") && !url.startsWith("https://"))
                                url = "http://$url"
                            val request = DownloadManager.Request(Uri.parse(url))
                            request.setDescription("در حال دانلود ...")
                            request.setTitle("دانلود ویدئو")
                            request.allowScanningByMediaScanner()
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

                            request.setDestinationInExternalPublicDir(
                                Environment.DIRECTORY_DOWNLOADS,
                                videoName
                            )

                            // get download service and enqueue file
                            val manager =
                                getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                            manager.enqueue(request)
                            Toast.makeText(
                                this@VideoPlayerActivity,
                                "در حال دانلود ...",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            if (report.isAnyPermissionPermanentlyDenied) {
//                                try {
//                                    showAlertDialog(R.drawable.ic_icons_8_cancel,
//                                        0,
//                                        "با توجه به اینکه شما مجوزهای دسترسی را قبول نکردید ، امکان استفاده از اپلیکیشن وجود ندارد . لطفا از بخش تنظیمات مجوزها را تایید کنید",
//                                        object : InfoAlertDialog.onDismiss() {
//                                            fun ondismiss(dialog: InfoAlertDialog) {
//                                                val intent =
//                                                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                                                val uri =
//                                                    Uri.fromParts("package", packageName, null)
//                                                intent.data = uri
//                                                startActivityForResult(intent, 2345)
//                                            }
//                                        })
//                                } catch (e: Exception) {
//
//                                }

                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
//                        try {
//                            showAlertDialog(
//                                R.drawable.alert_icon,
//                                0,
//                                "برای اینکه اپلیکیشن به درستی کار کند و پاسخگوی نیازهای شما باشد ، باید تمام مجوزهای دسترسی را قبول کنید .",
//                                object : InfoAlertDialog.onDismiss() {
//                                    fun ondismiss(dialog: InfoAlertDialog) {
//                                        dialog.dismiss()
//                                        token.continuePermissionRequest()
//                                    }
//                                })
//                        } catch (e: Exception) {
//
//                        }

                    }


                }).check()
            }
            btn_back.setOnClickListener { onBackPressed() }

        } catch (e: java.lang.Exception) {
            finish()
        }
    }


    fun setOnVideoErrorListener(onPlayVideoError: OnPlayVideoError) {
        this.onPlayVideoError = onPlayVideoError
    }

    interface OnPlayVideoError {
        fun onError(e: Exception)
    }


}