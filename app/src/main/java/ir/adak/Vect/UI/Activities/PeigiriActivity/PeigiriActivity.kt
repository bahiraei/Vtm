package ir.adak.Vect.UI.Activities.PeigiriActivity
import android.Manifest
import android.animation.ValueAnimator
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.ViewModelProviders
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import ir.adak.Vect.Data.Models.*
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.BaseActivity
import android.app.Activity
import android.content.*
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.*
import android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder
import cafe.adriel.androidaudiorecorder.model.AudioChannel
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate
import cafe.adriel.androidaudiorecorder.model.AudioSource
import com.bumptech.glide.Glide
import com.example.android.uamp.media.IPlayer
import com.example.android.uamp.media.Player
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.koushikdutta.ion.Ion
import com.vincan.medialoader.MediaLoader
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import droidninja.filepicker.models.sort.SortingTypes
import ir.adak.Vect.App
import ir.adak.Vect.BuildConfig
import ir.adak.Vect.Data.Enums.*
import ir.adak.Vect.Data.Models.PeigiriItem.PeigiriItem
import ir.adak.Vect.Listeners.*
import ir.adak.Vect.UI.Activities.AddActionActivity.AddActionActivity
import ir.adak.Vect.UI.Activities.MeetingActivity.MeetingActivity
import ir.adak.Vect.UI.Activities.ProjectDetailsActivity.ProjectDetailsActivity
import ir.adak.Vect.UI.Activities.VideoPlayerActivity.VideoPlayerActivity
import ir.adak.Vect.UI.Dialogs.*
import ir.adak.Vect.Utils.*
import kotlinx.android.synthetic.main.activity_peigiri.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.net.URLConnection
import java.net.URLEncoder


class PeigiriActivity : BaseActivity() {
    private lateinit var counter: CountDownTimer
    val audioTracks = mutableListOf<IPlayer.Track>()
    val player by lazy {
        Player.init(application)
        Player
    }
    var currentPlayingAudioPeigiri: PeigiriItem? = null
    var currentHighlightedPeigiri: PeigiriItem? = null
    var isReplying: Boolean = false
    var isEditing: Boolean = false
    var replyingFollowUp: FollowUp? = null
    var editingPeigiri: PeigiriItem? = null
    val mainFollowUpList = mutableListOf<PeigiriItem>()
    val audioFollowUpList = mutableListOf<PeigiriItem>()
    var filterdFollowUpList = mutableListOf<PeigiriItem>()

    val MAX_FILE_SIZE_SUPPORTED: Long = 10

    val MAX_MEDIA_SIZE_SUPPORTED: Long = 10

    lateinit var peigiriAdapter: GroupAdapter<ViewHolder>

    var viewModel: PeigiriActivityViewModel? = null

    val layoutMgr = WrapContentLinearLayoutManager(this@PeigiriActivity)
    var project: Project? = null
    var projectId: String? = null
    var isValidText = false
    internal var maxMediaCount = 5
    internal var maxFilesCount = 5
    companion object {
        var needToRefresh = false
        var position = -1
        var count = -1
        var highlightPosition: Int = -1
        var c:Activity ? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_peigiri)
        viewModel = ViewModelProviders.of(this).get(PeigiriActivityViewModel::class.java)
        if (intent.extras != null) {
            projectId = intent.extras?.getString("projectId")
            position = intent.extras?.getInt("position", -1) ?: -1
        }


        c=this



        edt_peigiri.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val input = p0.toString()
                isValidText = !input.isNullOrEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
        edt_search.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (textView.text.length > 1) {
                    if (filterdFollowUpList.isNullOrEmpty()) {
                        filterdFollowUpList.clear()
                        filterdFollowUpList.addAll(mainFollowUpList.filter {
                            it.followUp.description?.contains(textView.text) == true
                        }
                        )
                        updateList(filterdFollowUpList)
                    } else {
                        filterdFollowUpList = filterdFollowUpList.filter {
                            it.followUp.description?.contains(textView.text) == true
                        }.toMutableList()
                        updateList(filterdFollowUpList)
                    }
                    false
                } else if (textView.text.isEmpty()) {
                    updateList(mainFollowUpList)
                    false
                } else {
                    Toast.makeText(
                        this,
                        "لطفا برای جستجو بیش از یک کاراکتر وارد نمایید",
                        Toast.LENGTH_SHORT
                    ).show()
                    true
                }
            } else {
                false
            }

        }



        peigiriAdapter = GroupAdapter<ViewHolder>()
        with(rv_peigiri) {
            layoutManager = layoutMgr
            adapter = peigiriAdapter
            itemAnimator = MyItemAnimator()
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (highlightPosition != -1) {
                            (peigiriAdapter.getItem(highlightPosition) as PeigiriItem).notifyChanged(
                                PeigiriChangeEnum.HIGHLIGHT
                            )
                        }
                    }
                }
            })
        }

        getFollowUps()
        btn_send.setOnClickListener {
            sendTextMessage()
        }

        btn_project_details.setOnClickListener {
            val intent = Intent(this, ProjectDetailsActivity::class.java)
            intent.putExtra("project", project)
            startActivityForResult(intent, Constants.UPDATE_PROJECT_REQUEST_CODE)
        }

        btn_attach.setOnClickListener {
            val dialog = AttachToFollowUpBottomSheet()
            dialog.show(supportFragmentManager, "")
        }

        peigiriAdapter.setOnItemClickListener { item, view ->
            Log.i(TAG, view.javaClass.name)
            val peigiriItem = item as PeigiriItem
            when (peigiriItem.followUp.followUpType) {
                FollowUpType.Text -> {

                }
                FollowUpType.File -> {
                    val file =
                        File("$APP_FOLDER${peigiriItem.followUp.fileName}")
                    if (peigiriItem.followUp.isUploadType) {
                        if (peigiriItem.followUp.fileUploadProgress == 0 || peigiriItem.followUp.fileUploadProgress == 100) {
                            if (peigiriItem.followUp.file?.exists() == true) {
                                OpenFile(file)
                            }
                        }
                    } else {
                        if (peigiriItem.followUp.fileTypeEnum == FileTypeEnum.Unknown || peigiriItem.followUp.fileTypeEnum == FileTypeEnum.FaceStatus || peigiriItem.followUp.fileTypeEnum == FileTypeEnum.Image)
                            if (peigiriItem.followUp.fileDownloadProgress == 0 || peigiriItem.followUp.fileDownloadProgress == 100) {
                                if (peigiriItem.followUp.file?.exists() == true) {
                                    OpenFile(file)
                                }
                            }
                    }

                }
                FollowUpType.Description -> {

                }
                FollowUpType.ChangeProgress -> {

                }
                FollowUpType.Deleted -> {

                }
                FollowUpType.EnterMember -> {

                }
                FollowUpType.ExitMember -> {

                }
                FollowUpType.ActiveProject -> {

                }
                FollowUpType.DeactiveProject -> {

                }
                FollowUpType.EditProject -> {

                }
                FollowUpType.NewProject -> {
                    startActivity(
                        Intent(
                            this@PeigiriActivity,
                            PeigiriActivity::class.java
                        ).putExtra("projectId",item.followUp.newProjectId)
                    )
                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
                }
                FollowUpType.NewMeeting -> {

                }
                FollowUpType.CreatedProject -> {
                }
            }
        }



        btn_add_project.setOnClickListener {
            val addToProjectBottomSheet = AddToProjectBottomSheet()
            addToProjectBottomSheet.show(supportFragmentManager, "")
        }
        btn_filter.setOnClickListener {
            val filterAndSortFollowUpBottomSheet = FilterAndSortFollowUpBottomSheet(false)
            filterAndSortFollowUpBottomSheet.show(supportFragmentManager, "")
        }
        btn_sort.setOnClickListener {
            val filterAndSortFollowUpBottomSheet = FilterAndSortFollowUpBottomSheet(true)
            filterAndSortFollowUpBottomSheet.show(supportFragmentManager, "")
        }

        btn_close_project.setOnClickListener {

            Log.i("skdvsNKnksffbkas", project?.orgLevelId.toString())
            Log.i("skdvsNKnksffbkas", project?.orgLevelTitle.toString())
            Log.i("skdvsNKnksffbkas", project?.personFullName.toString())
            Log.i("skdvsNKnksffbkas", userInfo?.orgLevelId.toString())
            Log.i("skdvsNKnksffbkas", userInfo?.orgLevelTitle.toString())
            Log.i("skdvsNKnksffbkas", userInfo?.fullName.toString())
            if (project?.orgLevelId== userInfo?.orgLevelId)
            {
                 val myQuestionDialog = MyQuestionDialog(
                R.drawable.ic_power_settings_new_black_24dp,
                "اتمام وظیفه",
                "آیا از اتمام این وظیفه مطمئن هستید؟",
                object : MyQuestionDialog.OnButtonsClicked {
                    override fun OnPositiveClicked() {
                        closeProject()
                    }

                    override fun OnNegativeClicked() {
                    }
                })
            myQuestionDialog.show(supportFragmentManager, "")

            }


        }

        btn_close_reply.setOnClickListener {
            if (isReplying) {
                isReplying = false
                replyingFollowUp = null
            } else if (isEditing) {
                isEditing = false
                editingPeigiri = null
                edt_peigiri.text.clear()
            }
            CollapseSendLayout(true)
//            animatieFab(false)
        }

        player.liveDataPlayNow.observe(this, Observer { track ->

            Handler().postDelayed({
                var currentPercent =
                    (currentPlayingAudioPeigiri?.followUp?.currentPlayingPosition ?: 0) + 2010L
                if (currentPercent >= (currentPlayingAudioPeigiri?.followUp?.trackDuration ?: 0))
                    currentPlayingAudioPeigiri?.followUp?.currentPlayingPosition = 0
                currentPlayingAudioPeigiri?.followUp?.isPlaying = false
                currentPlayingAudioPeigiri?.notifyChanged(PeigiriChangeEnum.RESET_PROGRESS)
                var isCurrentTrackNotNull = currentPlayingAudioPeigiri != null
                audioFollowUpList.forEach { peigiriItem ->
                    if (peigiriItem.followUp.id == track?.id) {
                        currentPlayingAudioPeigiri = peigiriItem
                        if (isCurrentTrackNotNull) {
                            currentPlayingAudioPeigiri?.followUp?.isPlaying = true
                            currentPlayingAudioPeigiri?.notifyChanged(PeigiriChangeEnum.PLAY_PAUSE_AUDIO)
                        }
                    }
                }
            }, 28)

        })

        player.liveDataPlayerState.observe(this, Observer {
            if (it != null) {
                when (it) {
                    IPlayer.State.PLAY -> {
                        currentPlayingAudioPeigiri?.followUp?.isPlaying = true
                        player.seekTo(
                            currentPlayingAudioPeigiri?.followUp?.currentPlayingPosition ?: 0
                        )
                    }
                    IPlayer.State.PAUSE -> {
                        currentPlayingAudioPeigiri?.followUp?.isPlaying = false
                    }
                    IPlayer.State.STOP -> {
                        currentPlayingAudioPeigiri?.followUp?.isPlaying = false
                    }
                    IPlayer.State.PREPARING -> {

                    }
                    IPlayer.State.ERROR -> {
                        currentPlayingAudioPeigiri?.followUp?.isPlaying = false
                    }
                }
                currentPlayingAudioPeigiri?.notifyChanged(PeigiriChangeEnum.PLAY_PAUSE_AUDIO)
            }
        })

        counter = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onFinish() {
            }

            override fun onTick(millisUntilFinished: Long) {
                if (currentPlayingAudioPeigiri != null) {
                    if (currentPlayingAudioPeigiri?.followUp?.isPlaying == true) {
                        currentPlayingAudioPeigiri?.followUp?.trackDuration =
                            player.trackDuration
                        currentPlayingAudioPeigiri?.followUp?.currentPlayingPosition =
                            player.currentPosition
                        currentPlayingAudioPeigiri?.notifyChanged(PeigiriChangeEnum.PROGRESS_CHANGE)
                    }
                }
            }

        }
        counter.start()

        btn_search.setOnClickListener {
            val params = guideline_features.layoutParams as ConstraintLayout.LayoutParams
            var valueAnimator: ValueAnimator?
            if (edt_search.visibility == View.VISIBLE) {
                valueAnimator = ValueAnimator.ofFloat(params.guidePercent, 0.15f)
                edt_search.visibility = View.GONE
                img_search_icon.setImageResource(R.drawable.ic_search_icon)
                hideKeyboard(this)
            } else {
                valueAnimator = ValueAnimator.ofFloat(params.guidePercent, 1f)
                edt_search.visibility = View.VISIBLE
                edt_search.requestFocus()
                img_search_icon.setImageResource(R.drawable.ic_close_black_24dp)
                openKeyboard(this, edt_search)
            }

            valueAnimator.duration = 300
            valueAnimator.addUpdateListener {
                val animatedValue = it.animatedValue as Float
                params.guidePercent = animatedValue
                guideline_features.layoutParams = params
                guideline_features.invalidate()
            }
            valueAnimator.start()

        }
    }

    private fun closeProject() {
        val projectId = RequestBody.create(
            MediaType.parse("text/plain"),
            project?.id ?: ""
        )
        val req = App.getRetofitApi()?.closeProject(token, projectId)
        if (isNetConnected()) {
            if (!dialog.isShowing)
                showpDialog(this@PeigiriActivity)
            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {

                        Toast.makeText(
                            this@PeigiriActivity,
                            "پروژه با موفقیت به اتمام رسید.",
                            Toast.LENGTH_SHORT
                        ).show()

                        project?.projectStatus =
                            3

                        updateItemInList()

                        constraint_send.visibility = View.GONE
                        btn_close_project.visibility = View.GONE
                        btn_add_project.visibility = View.GONE
                        getFollowUps(false)
                        progress_bar.setOnClickListener {

                        }

                    } else if (isResponseValid(it) == 1) {

                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                closeProject()
                            }
                        })
                    }
                    hidepDialog()
                }
                onFailure = {
                    Log.i(TAG, it?.message ?: "")
                    hidepDialog()
                }
            }
        }
    }

    private fun CollapseSendLayout(animate: Boolean) {

        if (animate)
            TransitionManager.beginDelayedTransition(constraint_parent)
        constraint_reply_layout.visibility = View.GONE

        val gradientDrawable = constraint_send_peigiri.background as GradientDrawable
        val valueAnimator1 = ValueAnimator.ofFloat(dp2px(8).toFloat(), dp2px(360).toFloat())
        val valueAnimator2 = ValueAnimator.ofFloat(dp2px(32).toFloat(), dp2px(360).toFloat())
        var topRight = 0f
        var topLeft = 0f
        var bottomRight = 0f
        var bottomLeft = 0f
        valueAnimator1.duration = if (animate) 1800 else 0
        valueAnimator2.duration = if (animate) 1800 else 0
        valueAnimator1.addUpdateListener {
            val animatedValue = it.animatedValue as Float
            topRight = animatedValue
            topLeft = animatedValue
            gradientDrawable.cornerRadii = floatArrayOf(
                topLeft,
                topLeft,
                topRight,
                topRight,
                bottomRight,
                bottomRight,
                bottomLeft,
                bottomLeft
            )
        }
        valueAnimator2.addUpdateListener {
            val animatedValue = it.animatedValue as Float
            bottomRight = animatedValue
            bottomLeft = animatedValue
            gradientDrawable.cornerRadii = floatArrayOf(
                topLeft,
                topLeft,
                topRight,
                topRight,
                bottomRight,
                bottomRight,
                bottomLeft,
                bottomLeft
            )
        }

        valueAnimator1.start()
        valueAnimator2.start()
    }

    private fun ExpandSendLayout(animate: Boolean) {
        if (animate)
            TransitionManager.beginDelayedTransition(constraint_parent)
        constraint_reply_layout.visibility = View.VISIBLE

        val gradientDrawable = constraint_send_peigiri.background as GradientDrawable
        val valueAnimator1 = ValueAnimator.ofFloat(dp2px(360).toFloat(), dp2px(8).toFloat())
        val valueAnimator2 = ValueAnimator.ofFloat(dp2px(360).toFloat(), dp2px(32).toFloat())
        var topRight = 0f
        var topLeft = 0f
        var bottomRight = 0f
        var bottomLeft = 0f
        valueAnimator1.duration = if (animate) 200 else 0
        valueAnimator2.duration = if (animate) 200 else 0
        valueAnimator1.addUpdateListener {
            val animatedValue = it.animatedValue as Float
            topRight = animatedValue
            topLeft = animatedValue
            gradientDrawable.cornerRadii = floatArrayOf(
                topLeft,
                topLeft,
                topRight,
                topRight,
                bottomRight,
                bottomRight,
                bottomLeft,
                bottomLeft
            )
        }
        valueAnimator2.addUpdateListener {
            val animatedValue = it.animatedValue as Float
            bottomRight = animatedValue
            bottomLeft = animatedValue
            gradientDrawable.cornerRadii = floatArrayOf(
                topLeft,
                topLeft,
                topRight,
                topRight,
                bottomRight,
                bottomRight,
                bottomLeft,
                bottomLeft
            )
        }

        valueAnimator1.start()
        valueAnimator2.start()
    }


    private fun filterFilesFollowUps() {
        filterdFollowUpList.clear()
        filterdFollowUpList.addAll(mainFollowUpList.filter {
            (it.followUp.followUpType == FollowUpType.File) && (it.followUp.fileTypeEnum == FileTypeEnum.Unknown)
        }
        )
        updateList(filterdFollowUpList)
    }

    private fun filterSoratJalaseFollowUps() {

        filterdFollowUpList.clear()
        filterdFollowUpList.addAll(mainFollowUpList.filter {
            (it.followUp.followUpType == FollowUpType.File) && (it.followUp.fileTypeEnum == FileTypeEnum.FaceStatus)
        }
        )
        updateList(filterdFollowUpList)
    }

    //
    private fun filterAudioFollowUps() {
        filterdFollowUpList.clear()
        filterdFollowUpList.addAll(mainFollowUpList.filter {
            (it.followUp.followUpType == FollowUpType.File) && (it.followUp.fileTypeEnum == FileTypeEnum.Audio)
        }
        )
        updateList(filterdFollowUpList)
    }

    //
    private fun filterVideosFollowUps() {

        filterdFollowUpList.clear()
        filterdFollowUpList.addAll(mainFollowUpList.filter {
            (it.followUp.followUpType == FollowUpType.File) && (it.followUp.fileTypeEnum == FileTypeEnum.Video)
        }
        )
        updateList(filterdFollowUpList)
    }

    //
    private fun filterImagesFollowUps() {


        filterdFollowUpList.clear()
        filterdFollowUpList.addAll(mainFollowUpList.filter {
            (it.followUp.followUpType == FollowUpType.File) && (it.followUp.fileTypeEnum == FileTypeEnum.Image)
        }
        )
        updateList(filterdFollowUpList)
    }

    //
    private fun filterOthersFollowUps() {


        filterdFollowUpList.clear()
        filterdFollowUpList.addAll(mainFollowUpList.filter {
            it.followUp.orgLevelId != userInfo?.orgLevelId
        }
        )
        updateList(filterdFollowUpList)
    }

    //
    private fun filterMyFollowUps() {


        filterdFollowUpList.clear()
        filterdFollowUpList.addAll(mainFollowUpList.filter {
            it.followUp.orgLevelId == userInfo?.orgLevelId
        }
        )
        updateList(filterdFollowUpList)
    }

//    fun animatieFab(toUp: Boolean) {
//        var delay = 0L
//        var toMarging = 0
//        val currentMarginBottom =
//            (fab_shadow.layoutParams as CoordinatorLayout.LayoutParams).bottomMargin
//        when (toUp) {
//            true -> {
//                if (txt_reply_text.lineCount > 1)
//                    toMarging = 150
//                else
//                    toMarging = 128
//            }
//            false -> {
//                toMarging = 45
//                delay = 400L
//            }
//        }
//        val valueAnimator = ValueAnimator.ofInt(currentMarginBottom, dp2px(toMarging))
//        valueAnimator.duration = 330
//
//        valueAnimator.addUpdateListener {
//            val animatedValue = it.animatedValue as Int
//            (fab_shadow.layoutParams as CoordinatorLayout.LayoutParams).bottomMargin = animatedValue
//            fab_shadow.invalidateShadow()
//        }
//        valueAnimator.startDelay = delay
//        valueAnimator.start()
//    }
    fun updateList(peigiriItems: MutableList<PeigiriItem>) {
        peigiriAdapter.clear()
        peigiriAdapter.addAll(peigiriItems)
        if (peigiriItems.size > 0) {
//            if (animate)
//                rv_peigiri.smoothScrollToPosition(peigiriItems.size - 1)
//            else
            rv_peigiri.scrollToPosition(peigiriItems.size - 1)
        }
    }


    fun getFollowUps(showLoading: Boolean = true) {
        Log.i("weew",projectId)
        if (isNetConnected()) {
            if (showLoading)
                showpDialog(this)
            val requestBody = RequestBody.create(MediaType.parse("text/plain"), projectId ?: "")
            val req = App.getRetofitApi()?.getFollowUps(token, requestBody)
            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {
                        val getFollowUpResponse = it.body()?.data
                        project = getFollowUpResponse?.project
                        btn_expand.setOnClickListener {
                            TransitionManager.beginDelayedTransition(constraint_parent)
                            if (ll_features.visibility == View.GONE) {
                                btn_expand.rotation = 180f
                                ll_features.visibility = View.VISIBLE

                                btn_search.visibility = View.VISIBLE
                            } else {
                                btn_expand.rotation = 0f
                                ll_features.visibility = View.GONE
                                btn_search.visibility = View.GONE
                            }
                        }
                        txt_project_title.text = project?.title
                        txt_users_count.text = "${project?.memberCount} نفر"
                        count = project?.memberCount ?: 0
                        progress_bar.progressMax = 100f
                        progress_bar.progress = project?.progress?.toFloat() ?: 0f
                        txt_percent.text = "${project?.progress?.toString()}%"
                        progress_bar.setOnClickListener {
                            if (project?.orgLevelId == userInfo?.orgLevelId) {
                                val dialog =
                                    ChangeProgressDialog(
                                        project,
                                        object : ChangeProgressDialog.OnChangeProgressCompleted {
                                            override fun OnChangeProgress(progress: Int) {
                                                changeprogress(progress)

                                            }
                                        })
                                dialog.show(supportFragmentManager, "")
                            }
                        }

//                        if (project?.projectStatus != 1 || project?.projectStep == ProjectStep.ToDo.value) {

                        if (project?.projectStatus ==3||project?.orgLevelId!= userInfo?.orgLevelId) {
                            btn_close_project.visibility = View.GONE
                        }


                        if (project?.projectStatus != 1) {
                            Log.i("smvksvvaugas","Ak")
                            constraint_send.visibility = View.GONE
                            btn_add_project.visibility = View.GONE
//                            if (project?.projectStatus == 3||project?.orgLevelId!= userInfo?.orgLevelId) {  Nima
//                                btn_close_project.visibility = View.GONE
//                            }
//                            if (project?.projectStatus!=3&& project?.isMyProject!!)  Nima
                            if (project?.projectStatus!=3&& project?.isMyProject!!)
                            {
                                 progress_bar.setOnClickListener {
                                    val dialog =
                                        ChangeProgressDialog(
                                            project,
                                            object :
                                                ChangeProgressDialog.OnChangeProgressCompleted {
                                                override fun OnChangeProgress(progress: Int) {
                                                    changeprogress(progress)

                                                }
                                            })
                                    dialog.show(supportFragmentManager, "")
                                }
                            }
//                            progress_bar.setOnClickListener {
//                                Log.i("smvksvvaugas","Aq")
//                                Log.i("smvksvvaugas",project?.orgLevelTitle)
//                                Log.i("smvksvvaugas", userInfo?.orgLevelTitle)
//                                Log.i("smvksvvaugas", userInfo_Copy?.orgLevelTitle)
//                            }


                            if (project?.projectStep == ProjectStep.ToDo.value) {
                                btn_expand.visibility = View.INVISIBLE
                                rv_peigiri.visibility = View.INVISIBLE
                                lyt_start_action.visibility = View.VISIBLE
                                txt_action_date.text =
                                    "پروژه طبق برنامه ریزی در تاریخ ${project?.persianStartDate} شروع میشود. "

                                if (project?.persianStartDate.isNullOrEmpty()) {
                                    txt_action_date.visibility = View.GONE
                                }

                                btn_start_action.setOnClickListener {
                                    val myQuestionDialog = MyQuestionDialog(
                                        R.drawable.ic_access_time_black_24dp,
                                        "شروع فعالیت",
                                        "آیا از شروع این فعالیت مطمئن هستید؟",
                                        object : MyQuestionDialog.OnButtonsClicked {
                                            override fun OnPositiveClicked() {
                                                StartProject(projectId)
                                            }

                                            override fun OnNegativeClicked() {
                                            }
                                        })
                                    myQuestionDialog.show(supportFragmentManager, "")

                                }
                            }
                        }
                        else if (project?.projectStatus == 1 && project?.projectStep == ProjectStep.Doing.value) {
                            Log.i("smvksvvaugas","A")
                            rv_peigiri.visibility = View.VISIBLE
                            btn_expand.visibility = View.VISIBLE
                            lyt_start_action.visibility = View.GONE
                            btn_close_project.visibility = View.VISIBLE
                            constraint_send.visibility = View.VISIBLE
                            btn_add_project.visibility = View.VISIBLE
                            progress_bar.setOnClickListener {
                                Log.i("smvksvvaugas","Ay")
                                if (project?.orgLevelId == userInfo?.orgLevelId) {
                                    Log.i("smvksvvaugas",project?.orgLevelTitle)
                                    Log.i("smvksvvaugas", userInfo?.orgLevelTitle)
                                    Log.i("smvksvvaugas", userInfo_Copy?.orgLevelTitle)
                                    val dialog =
                                        ChangeProgressDialog(
                                            project,
                                            object :
                                                ChangeProgressDialog.OnChangeProgressCompleted {
                                                override fun OnChangeProgress(progress: Int) {
                                                    changeprogress(progress)

                                                }
                                            })
                                    dialog.show(supportFragmentManager, "")
                                }
                            }
                        }


                        if (project?.projectStatus!=3&& project?.isMyProject!!)
                        {
                            progress_bar.setOnClickListener {
                                val dialog =
                                    ChangeProgressDialog(
                                        project,
                                        object :
                                            ChangeProgressDialog.OnChangeProgressCompleted {
                                            override fun OnChangeProgress(progress: Int) {
                                                changeprogress(progress)

                                            }
                                        })
                                dialog.show(supportFragmentManager, "")
                            }
                        }



                        val peigiriItems = mutableListOf<PeigiriItem>()
                        getFollowUpResponse?.followUps?.forEachIndexed { index, it ->
                            Log.i("ddcde", FollowUpType.fromInt(it.type).toString() )
                            it.followUpType = FollowUpType.fromInt(it.type) ?: FollowUpType.Text
//                        if (it.followUpType == FollowUpType.Description) it.followUpType = FollowUpType.Text
                            it.fileSizeLabel = getFileSizeLabel(null, it.fileSize)
                            Log.i("fileSizeLabel",it.fileSizeLabel)
                            it.fileTypeEnum =
                                FileTypeEnum.fromInt(it.fileType) ?: FileTypeEnum.Unknown
                            Log.i("fileTypeEnum", it.fileTypeEnum.toString())
//                        if (it.fileTypeEnum == FileTypeEnum.Audio) it.fileTypeEnum =
//                            FileTypeEnum.Unknown
                            if (!it.fileName.isNullOrEmpty())
                                it.file = File(APP_FOLDER + it.fileName)
                            if (it.replyFollowUp != null) {
                                it.replyFollowUp.followUpType =
                                    FollowUpType.fromInt(it.replyFollowUp.type) ?: FollowUpType.Text
                                it.replyFollowUp.fileTypeEnum =
                                    FileTypeEnum.fromInt(it.replyFollowUp.fileType)
                                        ?: FileTypeEnum.Unknown
                            }
                            val peigiriItem = PeigiriItem(
                                project,
                                orgLevelId = userInfo?.orgLevelId,
                                followUp = it,
                                onDownloadButtonClicked = onDownloadButtonClicked,
                                onPlayVideoClicked = onPlayVideoClicked,
                                C = applicationContext
                            )

                            peigiriItem.onFollowupLongClickListener = onFollowupLongClickListener
                            peigiriItem.onReplyFollowUpClicked = onReplyFollowUpClicked
                            peigiriItem.onPlayPauseAudioClicked = onPlayPauseAudioClicked
                            peigiriItem.onPlayerSeekBarChangedListener = onPlayerSeekBarChangedListener
                            peigiriItem.onFollowUpClickListener = onFollowUpClickListener
                            peigiriItems.add(peigiriItem)
                        }
                        txt_users_count.text = if (count > 0) "$count نفر" else "بدون عضو"
                        mainFollowUpList.clear()
                        mainFollowUpList.addAll(peigiriItems)
                        mainFollowUpList.forEachIndexed { index, it ->
                            if (it.followUp.followUpType == FollowUpType.File && it.followUp.fileTypeEnum == FileTypeEnum.Audio) {
                                audioFollowUpList.add(it)
                                audioTracks.add(
                                    IPlayer.Track(
                                        it.followUp.id,
                                        MediaLoader.getInstance(this@PeigiriActivity).getProxyUrl(
                                            Constants.FILE_BASE_URL + it.followUp.fileName
                                        ),
                                        "فایل صوتی " + (audioFollowUpList.size),
                                        it.followUp.createName
                                    )
                                )
                            }
                        }
                        player.playList = audioTracks
                        updateList(peigiriItems)
                        rv_peigiri.scrollToPosition(peigiriAdapter.itemCount - 1)

                        if (getFollowUpResponse?.pinFollowUp != null) {
                            getFollowUpResponse?.pinFollowUp.followUpType =
                                FollowUpType.fromInt(getFollowUpResponse?.pinFollowUp.type)
                                    ?: FollowUpType.Text
//                        if (it.followUpType == FollowUpType.Description) it.followUpType = FollowUpType.Text
                            getFollowUpResponse?.pinFollowUp.fileSizeLabel =
                                getFileSizeLabel(null, getFollowUpResponse?.pinFollowUp.fileSize)
                            getFollowUpResponse?.pinFollowUp.fileTypeEnum =
                                FileTypeEnum.fromInt(getFollowUpResponse?.pinFollowUp.fileType)
                                    ?: FileTypeEnum.Unknown
                            PinFollowUp(getFollowUpResponse.pinFollowUp, false)
                        } else {
                            TransitionManager.beginDelayedTransition(constraint_header)
                            constraint_pin_layout.visibility = View.GONE
                        }

                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                getFollowUps()
                            }
                        })
                    }

                    if (showLoading)
                        hidepDialog()

                }
                onFailure = {
                    if (showLoading)
                        hidepDialog()
                }
            }


        }
    }
    private fun changeprogress(progress: Int) {
        val req = App.getRetofitApi()
            ?.projectChangeProgress(
                token,
                ChangeProgressModel(
                    project?.id ?: "",
                    progress
                )
            )
        req?.enqueue {
            onResponse = {
                if (isResponseValid(it) == 2) {
                    project?.progress = progress
                    progress_bar.progress =
                        progress.toFloat()
                    txt_percent.text = "$progress%"
                    getFollowUps()
                } else if (isResponseValid(it) == 1) {
                    silentLogin(object : OnLoginCompleted {
                        override fun OnSuccess() {
                            changeprogress(progress)
                        }
                    })
                }
            }
            onFailure = {
                Log.i(TAG, it?.message ?: "")

            }
        }
    }
    private fun PinFollowUp(pinFollowUp: FollowUp?, withAnimation: Boolean) {
        if (withAnimation)
            TransitionManager.beginDelayedTransition(constraint_parent)
        constraint_pin_layout.visibility = View.VISIBLE
//        val constraintSet1 = ConstraintSet()
//        constraintSet1.clone(constraint_parent)
//        constraintSet1.connect(
//            constraint_pin_layout.id,
//            ConstraintSet.TOP, constraint_header.id, ConstraintSet.BOTTOM
//        )
//        constraint_parent.setConstraintSet(constraintSet1)

        txt_pin_username.text = pinFollowUp?.createName
        when (pinFollowUp?.followUpType) {
            FollowUpType.Text -> {
                img_pin_thumbnail.visibility =
                    View.GONE


                txt_pin_text.text = pinFollowUp.description
            }
            FollowUpType.File -> {

                when (pinFollowUp.fileTypeEnum) {
                    FileTypeEnum.Unknown -> {
                        txt_pin_text.text =
                            "فایل  ${pinFollowUp.fileExtension}"
                        img_pin_thumbnail.visibility =
                            View.GONE
                    }
                    FileTypeEnum.FaceStatus -> {
                        txt_pin_text.text =
                            "صورت وضعيت  ${pinFollowUp.fileExtension}"
                        img_pin_thumbnail.visibility =
                            View.GONE
                    }
                    FileTypeEnum.Image -> {
                        txt_pin_text.text =
                            "تصویر"
                        img_pin_thumbnail.visibility =
                            View.VISIBLE
                        if (pinFollowUp.file?.exists() == true) {
                            val uri =
                                FileProvider.getUriForFile(
                                    this@PeigiriActivity,
                                    BuildConfig.APPLICATION_ID + ".fileprovider",
                                    pinFollowUp.file!!
                                )
                            Glide.with(this@PeigiriActivity)
                                .load(uri)
                                .centerCrop()
                                .into(
                                    img_pin_thumbnail
                                )
                        } else {
                            Glide.with(this@PeigiriActivity)
                                .load(Constants.FILE_BASE_URL + pinFollowUp.fileName + "-.jpg")
                                .centerCrop()
                                .into(
                                    img_pin_thumbnail
                                )
                        }
                    }
                    FileTypeEnum.Video -> {
                        txt_pin_text.text =
                            "ویدئو"
                        img_pin_thumbnail.visibility =
                            View.GONE

                    }
                    FileTypeEnum.Audio -> {

                        txt_pin_text.text =
                            "فایل صوتی"
                        img_pin_thumbnail.visibility =
                            View.GONE
                    }
                }
            }
        }

        constraint_pin_layout.setOnClickListener {
            ScrollAndHighlightFollowUp(pinFollowUp)
        }

        btn_close_pin.setOnClickListener {
            ClosePinFollowUp()
        }

    }

    private fun ClosePinFollowUp() {
        val body = RequestBody.create(MediaType.parse("text/plain"), project?.id ?: "")
        val req = App.getRetofitApi()?.closePinFollowUp(token, body)
        req?.enqueue {
            onResponse = {
                if (isResponseValid(it) == 2) {
                    TransitionManager.beginDelayedTransition(constraint_parent)
                    constraint_pin_layout.visibility = View.GONE
                } else if (isResponseValid(it) == 1) {
                    silentLogin(object : OnLoginCompleted {
                        override fun OnSuccess() {
                            ClosePinFollowUp()
                        }
                    })
                }
            }
            onFailure = {
                Log.i(TAG, it?.message ?: "")

            }
        }

    }


    fun sendTextMessage() {
        if (edt_peigiri.text.toString().isNullOrBlank())
        {
          return
        }
        Log.i("SCD","AA")
        if (isValidText) {
            btn_send.isEnabled=false
            if (isEditing) {
                Log.i("vnvnbdhgjgke","A")
                val req = App.getRetofitApi()?.editTextFollowUp(
                    token, RegisterFollowUpModel(
                        project?.id,
                        edt_peigiri.text.toString(),
                        FollowUpType.Text.value,
                        null,
                        null,
                        null,
                        editingPeigiri?.followUp?.id
                    )
                )
                req?.enqueue {
                    onResponse = {
//                        btn_send.isClickable=true
//                        edt_peigiri.text.clear()
                        if (isResponseValid(it) == 2) {
                            editingPeigiri?.followUp?.description = edt_peigiri.text.toString()
                            editingPeigiri?.followUp?.isEdited = true
                            editingPeigiri?.notifyChanged()
                            if (editingPeigiri != null)
                                highlightPosition =
                                    peigiriAdapter.getAdapterPosition(editingPeigiri!!)

                            if (highlightPosition != -1) {
                                (peigiriAdapter.getItem(highlightPosition) as PeigiriItem).followUp.isHighlight =
                                    true
//                                Handler().postDelayed({
                                (peigiriAdapter.getItem(highlightPosition) as PeigiriItem).notifyChanged(
                                    PeigiriChangeEnum.HIGHLIGHT
                                )
//                                }, 28)
                                rv_peigiri.smoothScrollToPosition(highlightPosition)
                            }
                            if (isEditing || isReplying) {
                                editingPeigiri = null
                                isEditing = false
                                isReplying = false
                                CollapseSendLayout(true)
                            }
//                            animatieFab(false)
                            edt_peigiri.text.clear()
                        } else if (isResponseValid(it) == 1) {
                            silentLogin(object : OnLoginCompleted {
                                override fun OnSuccess() {
                                    sendTextMessage()
                                }
                            })
                        }
                    }
                    onFailure = {
                        btn_send.isClickable=true
                        if (isEditing || isReplying) {
                            editingPeigiri = null
                            isEditing = false
                            isReplying = false
                            CollapseSendLayout(false)
                        }
//                            animatieFab(false)
                    }
                }

            } else {
                Log.i("vnvnbdhgjgke","B")
                val req = App.getRetofitApi()?.registerTextFollowUp(
                    token, RegisterFollowUpModel(
                        project?.id,
                        edt_peigiri.text.toString(),
                        FollowUpType.Text.value,
                        null,
                        null,
                        null,
                        replyingFollowUp?.id
                    )
                )
                req?.enqueue {
                    onResponse = {
                        if (isResponseValid(it) == 2) {
                            btn_send.isEnabled=true
                            val followUp = it.body()?.data
                            val it = FollowUp(
                                id = followUp?.id ?: "",
                                projectId = project?.id ?: "",
                                persianDate = followUp?.persianDate
                                    ?: "",
                                orgLevelId = userInfo?.orgLevelId ?: 0,
                                orgLevelTitle = userInfo?.orgLevelTitle ?: "",
                                type = FollowUpType.Text.value,
                                profileImage = followUp?.profileImage,
                                isReply = isReplying,
                                replyFollowUp = replyingFollowUp,
                                followUpType = FollowUpType.Text,
                                createName = userInfo?.fullName ?: "",
                                description = edt_peigiri.text.toString()
                            )
                            if (!it.fileName.isNullOrEmpty())
                                it.file = File(APP_FOLDER + it.fileName)

                            val peigiriItem = PeigiriItem(
                                project = project,
                                orgLevelId = userInfo?.orgLevelId,
                                followUp = it,
                                C = applicationContext
                            )
                            peigiriItem.onFollowupLongClickListener = onFollowupLongClickListener
                            peigiriItem.onReplyFollowUpClicked = onReplyFollowUpClicked
                            peigiriItem.C=applicationContext
                            peigiriItem.onFollowUpClickListener = onFollowUpClickListener
                            mainFollowUpList.add(peigiriItem)
                            peigiriAdapter.add(peigiriItem)
                            edt_peigiri.text.clear()
                            rv_peigiri.scrollToPosition(peigiriAdapter.itemCount - 1)
                            isReplying = false
//                          replyingFollowUp=null   Nima Moradi
                            replyingFollowUp=null
                            CollapseSendLayout(true)
//                            animatieFab(false)
                        } else if (isResponseValid(it) == 1) {
                            silentLogin(object : OnLoginCompleted {
                                override fun OnSuccess() {
                                    sendTextMessage()
                                }
                            })
                        }
                    }
                    onFailure = {
                        btn_send.isEnabled=true
                        isReplying = false
                        CollapseSendLayout(false)
//                            animatieFab(false)
                    }
                }

            }

        }
    }

    override fun onBackPressed() {
        if (isReplying) {
            isReplying = false
            CollapseSendLayout(true)
//            animatieFab(false)
            return
        } else if (isEditing) {
            isEditing = false
            CollapseSendLayout(true)
//            animatieFab(false)
            return
        }

        if (ll_long_touch.visibility == View.VISIBLE) {
            btn_close_long_touch.performClick()
            return
        }
        updateItemInList()
        super.onBackPressed()
    }

    fun updateItemInList() {
        val data = Intent()
        data.putExtra("position", position)
        data.putExtra("project", project)
        setResult(Activity.RESULT_OK, data)
    }

    override fun onResume() {
        super.onResume()
        if (needToRefresh) {
            getFollowUps(false)
            needToRefresh = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==13989&&resultCode== Activity.RESULT_OK)
        {
            getFollowUps()
        }
        if (requestCode == Constants.UPLOAD_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null)
        {

            val filePaths =
                ArrayList(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS)!!)
            filePaths.forEach {
                if ((getFileSize(File(it)) / 1024 / 1024) > MAX_FILE_SIZE_SUPPORTED) {
                    return
                }
            }

            filePaths.forEach {
                val file = File(it)
                uploadFile(file, false)
            }
        }
        else if (requestCode == Constants.UPLOAD_FACE_STATUS_REQUEST_CODE
            && resultCode == Activity.RESULT_OK && data != null) {
            val filePaths =
                ArrayList(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS)!!)
            filePaths.forEach {
                if ((getFileSize(File(it)) / 1024 / 1024) > MAX_FILE_SIZE_SUPPORTED) {
                    return
                }
            }

            filePaths.forEach {
                val file = File(it)
                uploadFile(file, false, true)
            }
        }
        else if (requestCode == Constants.UPLOAD_MEDIA_REQUEST_CODE
            && resultCode == Activity.RESULT_OK && data != null) {

            val mediaPaths =
                ArrayList(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)!!)
            mediaPaths.forEach {
                if ((getFileSize(File(it)) / 1024 / 1024) > MAX_MEDIA_SIZE_SUPPORTED) {
                    return
                }
            }
            mediaPaths.forEach {
                val file = File(it)
                uploadFile(file, false)
            }
        }
        else if (requestCode == Constants.ADD_PROJECT_REQUEST_CODE &&
            resultCode == Activity.RESULT_OK && data != null) {
            getFollowUps()
        }
        else if (requestCode == Constants.ADD_MEETING_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null)
        {
            getFollowUps()
        }
        else if (requestCode == Constants.RECORD_AUDIO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val filePath =
                "${Environment.getExternalStorageDirectory()}${File.separator}kasbAudioRecorded.wav"
            val recordedFile = File(filePath)
            if (recordedFile.exists()) {
                uploadFile(recordedFile, true)
            }
        }
        else if (requestCode == Constants.UPDATE_PROJECT_REQUEST_CODE
            && resultCode == Activity.RESULT_OK && data != null) {
            project = data.extras?.getParcelable("project")
            Log.i("xcdacvbb", project.toString())
            if (data.extras?.getBoolean("isProgress") == true) {
                changeprogress(project?.progress ?: 0)
                return
            }
            var isActive = project?.projectStatus == 1
            if (!isActive) {
                constraint_send.visibility = View.GONE
                btn_add_project.visibility = View.GONE
                if (project?.projectStatus == 3) {
                    btn_close_project.visibility = View.GONE
                }

                progress_bar.setOnClickListener { }
            }
            val followUpFilterEnum =
                FollowUpFilterEnum.fromInt((data.extras?.getInt("followUpFilterEnum")) ?: 0)
            when (followUpFilterEnum) {
                FollowUpFilterEnum.IMAGES -> {
                    filterImagesFollowUps()
                }
                FollowUpFilterEnum.FILES -> {
                    filterFilesFollowUps()
                }
                FollowUpFilterEnum.FaceStatus -> {
                    filterSoratJalaseFollowUps()
                }
                FollowUpFilterEnum.VIDEOS -> {
                    filterVideosFollowUps()
                }
                FollowUpFilterEnum.AUDIO -> {
                    filterAudioFollowUps()
                }
            }
//            getFollowUps(true)
            if(data!=null&&data.getStringExtra("Type")!=null)
            {
                var Type=data.getStringExtra("Type")
                if (Type.equals("A"))
                {
                    filterdFollowUpList.clear()
                    viewModel?.Meeting =true
                    val filesList = mutableListOf<PeigiriItem>()
                    if (viewModel?.Meeting == true) {
                        filesList.addAll(mainFollowUpList.filter {
                            it.followUp.followUpType == FollowUpType.NewMeeting
//                                    && it.followUp.fileTypeEnum == FileTypeEnum.FaceStatus
                        })
                    }
                    filterdFollowUpList=filesList
                    Log.i("ututututututut", filterdFollowUpList.size.toString())
                    updateList(filterdFollowUpList)
                }else if(Type.equals("B")){
                    filterdFollowUpList.clear()
                    viewModel?.Jobs =true
                    val filesList = mutableListOf<PeigiriItem>()
                    filesList.addAll(mainFollowUpList.filter {
                        Log.i("ututututututut","A")
//                            it.followUp.followUpType == FollowUpType.Jobs
                        it.followUp.followUpType == FollowUpType.NewProject||
                                it.followUp.followUpType == FollowUpType.SummaryEndProject||
                                it.followUp.followUpType == FollowUpType.CreatedProject
//                                    && it.followUp.fileTypeEnum == FileTypeEnum.FaceStatus
                    })
                    filterdFollowUpList=filesList
                    Log.i("ututututututut", filterdFollowUpList.size.toString())
                    updateList(filterdFollowUpList)
                }
                else if(Type.equals("C")){
                    filterdFollowUpList.clear()
                    viewModel?.IMAGES_FOLLOWUPS =true
                    val filesList = mutableListOf<PeigiriItem>()
                    if (viewModel?.IMAGES_FOLLOWUPS == true) {
                        filesList.addAll(mainFollowUpList.filter {
                            it.followUp.followUpType == FollowUpType.File && it.followUp.fileTypeEnum == FileTypeEnum.Image
                        })
                    }
                    filterdFollowUpList=filesList
                    Log.i("ututututututut", filterdFollowUpList.size.toString())
                    updateList(filterdFollowUpList)
                }
                else if(Type.equals("D")){
                    filterdFollowUpList.clear()
                    viewModel?.FILES_FOLLOWUPS =true
                    val filesList = mutableListOf<PeigiriItem>()
                    if (viewModel?.FILES_FOLLOWUPS == true) {
                        filesList.addAll(mainFollowUpList.filter {
                            it.followUp.followUpType == FollowUpType.File && it.followUp.fileTypeEnum == FileTypeEnum.Unknown
                        })
                    }
                    filterdFollowUpList=filesList
                    Log.i("ututututututut", filterdFollowUpList.size.toString())
                    updateList(filterdFollowUpList)
                }
                else if(Type.equals("E")){
                    filterdFollowUpList.clear()
                    viewModel?.VIDEOS_FOLLOWUPS =true
                    val filesList = mutableListOf<PeigiriItem>()
                    if ( viewModel?.VIDEOS_FOLLOWUPS == true) {
                        filesList.addAll(mainFollowUpList.filter {
                            it.followUp.followUpType == FollowUpType.File && it.followUp.fileTypeEnum == FileTypeEnum.Video
                        })
                    }
                    filterdFollowUpList=filesList
                    Log.i("ututututututut", filterdFollowUpList.size.toString())
                    updateList(filterdFollowUpList)
                }
                else if(Type.equals("F")){
                    filterdFollowUpList.clear()
                    viewModel?.AUDIO_FOLLOWUPS =true
                    val filesList = mutableListOf<PeigiriItem>()
                    if ( viewModel?.AUDIO_FOLLOWUPS == true) {
                        filesList.addAll( mainFollowUpList.filter {
                            it.followUp.followUpType == FollowUpType.File && it.followUp.fileTypeEnum == FileTypeEnum.Audio
                        })
                    }
                    filterdFollowUpList=filesList
                    Log.i("ututututututut", filterdFollowUpList.size.toString())
                    updateList(filterdFollowUpList)
                }
            }
        }
        else if (requestCode == Constants.ADD_ACTION_FOLLOWUP_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            getFollowUps(false)
        }



    }

    fun uploadFile(file: File, deleteOriginalFile: Boolean, isFaceStatus: Boolean = false) {
        val it = FollowUp(
            projectId = project?.id ?: "",
            persianDate = "هم اکنون",
            orgLevelId = userInfo?.orgLevelId ?: 0,
            orgLevelTitle = userInfo?.orgLevelTitle ?: "",
            type = FollowUpType.File.value,
            followUpType = FollowUpType.File,
            createName = userInfo?.fullName ?: "",
            file = file,
            isReply = isReplying,
            replyFollowUp = replyingFollowUp,
            fileName = file.name,
            fileNameForShow = file.name,
            fileType = if (isFaceStatus) FileTypeEnum.FaceStatus.value else getFileTypeEnum(file.extension).value,
            fileTypeEnum = if (isFaceStatus) FileTypeEnum.FaceStatus else getFileTypeEnum(file.extension),
            fileExtension = file.extension,
            fileSize = getFileSize(file),
            fileSizeLabel = getFileSizeLabel(file, null),
            isUploadType = true
        )
        val peigiriItem = PeigiriItem(
            project = project,
            orgLevelId = userInfo?.orgLevelId,
            followUp = it,
            onCancelUploadClicked = onCancelUploadClicked,
            onPlayVideoClicked = onPlayVideoClicked,
            C = applicationContext
        )
        peigiriItem.onFollowupLongClickListener = onFollowupLongClickListener
        peigiriItem.onReplyFollowUpClicked = onReplyFollowUpClicked
        peigiriItem.onPlayPauseAudioClicked = onPlayPauseAudioClicked
        peigiriItem.onPlayerSeekBarChangedListener = onPlayerSeekBarChangedListener
        peigiriItem.onFollowUpClickListener = onFollowUpClickListener

        mainFollowUpList.add(peigiriItem)
        peigiriAdapter.add(peigiriItem)
        rv_peigiri.scrollToPosition(peigiriAdapter.itemCount - 1)
        isReplying = false
        CollapseSendLayout(true)
//        animatieFab(false)
        val progressRequestBody = ProgressRequestBody(
            file,
            "multipart/form-data",
            object : ProgressRequestBody.UploadCallbacks {
                override fun onProgressUpdate(percentage: Int) {
                    Log.i(TAG, "onProgress: percentage : $percentage")
                    peigiriItem.followUp.fileUploadProgress = percentage
                    peigiriItem.notifyChanged(PeigiriChangeEnum.PROGRESS_CHANGE)
                }

                override fun onError() {
                    Log.i(TAG, "Some Error Occurred")
                }

                override fun onFinish() {
                }
            })
        val fileBodyPart = MultipartBody.Part.createFormData(
//            "file",   Heydaa
//            toHumanReadableAscii(file.name), Heydar
            "android",
            URLEncoder.encode(file.name.toString().trim(),"utf-8"),
            progressRequestBody
        )
        peigiriItem.followUp.req = App.getRetofitApi()?.registerFileFollowUp(
            token,
            RegisterFollowUpModel(
                project?.id,
                "",
                FollowUpType.File.value,
                null,
                null,
                if (isFaceStatus) FileTypeEnum.FaceStatus.value else null,
                replyingFollowUp?.id
            ),
            fileBodyPart
        )

        peigiriItem.followUp.req?.enqueue {
            onResponse = {
                if (isResponseValid(it) == 2) {
                    val followUp = it.body()?.data
                    Log.i(TAG, "Completed")
                    peigiriItem.followUp.profileImage = followUp?.profileImage
                    peigiriItem.followUp.persianDate =
                        followUp?.persianDate ?: ""
                    peigiriItem.followUp.id = followUp?.id ?: ""
                    peigiriItem.followUp.fileName =
                        followUp?.fileName ?: ""
                    peigiriItem.followUp.fileNameForShow = file.name

                    if (!peigiriItem.followUp.fileName.isNullOrEmpty())
                        peigiriItem.followUp.file =
                            File(APP_FOLDER + peigiriItem.followUp.fileName)
                    peigiriItem.followUp.isUploadType = false
                    peigiriItem.followUp.fileUploadProgress = 0
                    if (peigiriItem.followUp.fileTypeEnum == FileTypeEnum.Audio) {
                        audioFollowUpList.add(peigiriItem)
                        audioTracks.add(
                            IPlayer.Track(
                                peigiriItem.followUp.id,
                                MediaLoader.getInstance(this@PeigiriActivity).getProxyUrl(
                                    Constants.FILE_BASE_URL + followUp?.fileName
                                ),
                                "فایل صوتی " + (audioTracks.size + 1),
                                peigiriItem.followUp.createName
                            )
                        )
                    }
                    peigiriItem.notifyChanged()
                    copy(file, followUp?.fileName, deleteOriginalFile)
                } else if (isResponseValid(it) == 1) {
                    silentLogin(object : OnLoginCompleted {
                        override fun OnSuccess() {
                            uploadFile(file, deleteOriginalFile)
                        }
                    })
                }
            }
            onFailure = {
                peigiriAdapter.remove(peigiriItem)
                Toast.makeText(
                    this@PeigiriActivity,
                    "خطایی در ارسال فایل رخ داده است",
                    Toast.LENGTH_LONG
                ).show()
                Log.i(TAG, it?.message ?: "")
            }
        }

    }

    fun ScrollAndHighlightFollowUp(followUp: FollowUp?) {
        if (filterdFollowUpList.isNullOrEmpty()) {
            mainFollowUpList.forEachIndexed { index, peigiriItem ->
                if (peigiriItem.followUp.id == followUp?.id) {
                    highlightPosition = index
                }
            }
        } else {
            filterdFollowUpList.forEachIndexed { index, peigiriItem ->
                if (peigiriItem.followUp.id == followUp?.id) {
                    highlightPosition = index
                }
            }
        }

        if (highlightPosition != -1) {
            (peigiriAdapter.getItem(highlightPosition) as PeigiriItem).followUp.isHighlight =
                true
            (peigiriAdapter.getItem(highlightPosition) as PeigiriItem).notifyChanged(
                PeigiriChangeEnum.HIGHLIGHT
            )
            rv_peigiri.smoothScrollToPosition(highlightPosition)
        }
    }
    fun HighlightFollowUp(peigiriItem: PeigiriItem) {
        Log.i("sffmvskmvskf","C")
        progress_bar.visibility=View.GONE
        btn_project_details.visibility=View.GONE
        currentHighlightedPeigiri?.followUp?.isHighlight = false
        currentHighlightedPeigiri?.followUp?.isFixedHighLight = false
        currentHighlightedPeigiri?.notifyChanged()
        currentHighlightedPeigiri = peigiriItem
        peigiriItem.followUp.isHighlight = true
        peigiriItem.followUp.isFixedHighLight = true
        peigiriItem.notifyChanged(PeigiriChangeEnum.HIGHLIGHT)
    }
    val onFollowupLongClickListener = object : OnFollowupLongClickListener {
        override fun onFollowupLongClickListener(peigiriItem: PeigiriItem, isMine: Boolean) {
            HighlightFollowUp(peigiriItem)
            ll_long_touch.visibility = View.VISIBLE
            btn_close_long_touch.setOnClickListener {
                progress_bar.visibility=View.VISIBLE
                btn_project_details.visibility=View.VISIBLE
                ll_long_touch.visibility = View.GONE
                currentHighlightedPeigiri?.followUp?.isHighlight = false
                currentHighlightedPeigiri?.followUp?.isFixedHighLight = false
                currentHighlightedPeigiri?.notifyChanged()
                currentHighlightedPeigiri = null
                highlightPosition = -1
            }
            btn_reply_followUp.setOnClickListener {
                btn_close_long_touch.performClick()
                isReplying = true
                isEditing = false
                edt_peigiri.text.clear()
                replyingFollowUp = peigiriItem?.followUp
                ExpandSendLayout(true)
                txt_reply_username.text = peigiriItem?.followUp?.createName
                when (peigiriItem?.followUp?.followUpType) {
                    FollowUpType.Text -> {
                        img_reply_file_icon.visibility =
                            View.GONE
                        img_reply_thumbnail.visibility =
                            View.INVISIBLE
                        txt_reply_text.text = peigiriItem.followUp.description
                    }
                    FollowUpType.File -> {
                        img_reply_file_icon.visibility =
                            View.VISIBLE
                        when (peigiriItem.followUp.fileTypeEnum) {
                            FileTypeEnum.Unknown -> {
                                img_reply_file_icon.setImageResource(
                                    R.drawable.ic_rar
                                )
                                txt_reply_text.text =
                                    "فایل  ${peigiriItem?.followUp?.fileExtension}"
                                img_reply_thumbnail.visibility =
                                    View.INVISIBLE
                            }
                            FileTypeEnum.FaceStatus -> {
                                img_reply_file_icon.setImageResource(
                                    R.drawable.ic_rar
                                )
                                txt_reply_text.text =
                                    "صورت وضعيت  ${peigiriItem?.followUp?.fileExtension}"
                                img_reply_thumbnail.visibility =
                                    View.INVISIBLE
                            }
                            FileTypeEnum.Image -> {
                                img_reply_file_icon.setImageResource(
                                    R.drawable.ic_frame_landscape
                                )
                                txt_reply_text.text =
                                    "تصویر"
                                img_reply_thumbnail.visibility =
                                    View.VISIBLE
                                if (peigiriItem.followUp.file?.exists() == true) {
                                    val uri =
                                        FileProvider.getUriForFile(
                                            this@PeigiriActivity,
                                            BuildConfig.APPLICATION_ID + ".fileprovider",
                                            peigiriItem.followUp.file!!
                                        )
                                    Glide.with(this@PeigiriActivity)
                                        .load(uri)
                                        .centerCrop()
                                        .into(
                                            img_reply_thumbnail
                                        )
                                } else {
                                    Glide.with(this@PeigiriActivity)
                                        .load(Constants.FILE_BASE_URL + peigiriItem.followUp.fileName + "-.jpg")
                                        .centerCrop()
                                        .into(
                                            img_reply_thumbnail
                                        )
                                }
                            }
                            FileTypeEnum.Video -> {
                                img_reply_file_icon.setImageResource(
                                    R.drawable.ic_play_button
                                )
                                txt_reply_text.text =
                                    "ویدئو"
                                img_reply_thumbnail.visibility =
                                    View.INVISIBLE

                            }
                            FileTypeEnum.Audio -> {
                                img_reply_file_icon.setImageResource(
                                    R.drawable.ic_voice_message_microphone_button
                                )
                                txt_reply_text.text =
                                    "فایل صوتی"
                                img_reply_thumbnail.visibility =
                                    View.INVISIBLE
                            }
                        }
                    }
                    FollowUpType.ActionTime ->
                    {
                        img_reply_file_icon.visibility =
                            View.GONE
                        img_reply_thumbnail.visibility =
                            View.INVISIBLE
                        txt_reply_text.text = peigiriItem.followUp.description
                    }
                }
//                        animatieFab(true)
            }
            btn_copy_followUp_text.setOnClickListener {
                btn_close_long_touch.performClick()
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("ABC", peigiriItem.followUp?.description)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(this@PeigiriActivity, "متن کپی شد", Toast.LENGTH_SHORT)
                    .show()
            }
            if (peigiriItem.followUp.orgLevelId == userInfo?.orgLevelId) {
                if (project?.isMyProject == true) {
                    btn_pin_followUp.visibility = View.VISIBLE
                }
                btn_delete_followUp.visibility = View.VISIBLE

                if (peigiriItem.followUp.followUpType == FollowUpType.Text) {
                    btn_edit_followUp.visibility = View.VISIBLE
                    btn_edit_followUp.setOnClickListener {
                        btn_close_long_touch.performClick()
                        isReplying = false
                        isEditing = true
                        editingPeigiri = peigiriItem
                        edt_peigiri.setText(peigiriItem.followUp.description)
                        ExpandSendLayout(true)
//                        animatieFab(true)
                        txt_reply_username.text = "ویرایش پیگیری"
                        txt_reply_text.text = peigiriItem.followUp?.description
                        img_reply_file_icon.visibility = View.GONE
                    }
                }

                btn_pin_followUp.setOnClickListener {
                    btn_close_long_touch.performClick()
                    RequestPinFollowUp(peigiriItem)
                }
                btn_delete_followUp.setOnClickListener {

                    btn_close_long_touch.performClick()

                    val myQuestionDialog = MyQuestionDialog(
                        R.drawable.ic_delete_black_24dp,
                        "حذف پیگیری",
                        "آیا از حذف این پیگیری مطمئن هستید؟",
                        object : MyQuestionDialog.OnButtonsClicked {
                            override fun OnPositiveClicked() {
                                DeleteFollowUp(peigiriItem)

                            }

                            override fun OnNegativeClicked() {
                            }
                        })
                    myQuestionDialog.show(supportFragmentManager, "")

                }
            }
            else {
                btn_pin_followUp.visibility = View.GONE
                btn_delete_followUp.visibility = View.GONE
                btn_edit_followUp.visibility = View.GONE
            }
            if (peigiriItem.followUp.followUpType == FollowUpType.File) {
                btn_share_followUp.visibility = View.VISIBLE
                if (peigiriItem.followUp.file?.exists() == true)
                {
                    btn_share_followUp.setOnClickListener {
                        val uri = FileProvider.getUriForFile(
                            this@PeigiriActivity,
                            BuildConfig.APPLICATION_ID + ".fileprovider",
                            peigiriItem.followUp.file!!
                        )
                        val sendIntent = Intent()
                        sendIntent.action = Intent.ACTION_SEND
                        sendIntent.type =
                            URLConnection.guessContentTypeFromName(peigiriItem.followUp.file!!.name)
                        sendIntent.putExtra(Intent.EXTRA_STREAM, uri)
                        startActivity(Intent.createChooser(sendIntent, "اشتراک گزاری فایل"))
                    }
                } else {
                    btn_share_followUp.setOnClickListener {
                        Toast.makeText(
                            this@PeigiriActivity,
                            "برای اشتراک گزاری فایل ابتدا آن را دانلود کنید",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } else {
                btn_share_followUp.visibility = View.GONE
            }

        }
    }

    private fun DeleteFollowUp(peigiriItem: PeigiriItem) {
        if (isNetConnected()) {
            if (!dialog.isShowing)
                showpDialog(this@PeigiriActivity)
            val body = RequestBody.create(
                MediaType.parse("text/plain"),
                peigiriItem.followUp.id
            )
            val req = App.getRetofitApi()?.deleteFollowUp(token, body)
            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {
                        if (peigiriItem.followUp.followUpType == FollowUpType.File &&
                            peigiriItem.followUp.fileTypeEnum == FileTypeEnum.Audio
                        ) {
                            audioFollowUpList.remove(peigiriItem)
                            audioTracks.forEach { track ->
                                if (peigiriItem.followUp.id == track.id) {
                                    if (player.liveDataPlayNow.value?.id == track.id) {
                                        currentPlayingAudioPeigiri = null
                                        player.stop()
                                    }
                                    audioTracks.remove(track)
                                }
                            }
                        }
                        peigiriItem.followUp.type =
                            FollowUpType.Deleted.value
                        peigiriItem.followUp.followUpType =
                            FollowUpType.Deleted
                        peigiriItem.notifyChanged()
                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                DeleteFollowUp(peigiriItem)
                            }
                        })
                    }
                    hidepDialog()

                }
                onFailure = {
                    hidepDialog()
                    Log.i(TAG, it?.message ?: "")
                }
            }

        }
    }

    private fun RequestPinFollowUp(peigiriItem: PeigiriItem) {
        val body =
            RequestBody.create(MediaType.parse("text/plain"), peigiriItem.followUp.id)
        val req = App.getRetofitApi()?.pinFollowUp(token, body)
        req?.enqueue {
            onResponse = {
                if (isResponseValid(it) == 2) {

                    PinFollowUp(peigiriItem.followUp, true)
                    Toast.makeText(
                        this@PeigiriActivity,
                        "پیگیری شما با موفقیت پین شد",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (isResponseValid(it) == 1) {
                    silentLogin(object : OnLoginCompleted {
                        override fun OnSuccess() {
                            RequestPinFollowUp(peigiriItem)
                        }
                    })
                }
            }
            onFailure = {
                Log.i(TAG, it?.message ?: "")

            }
        }
    }

    val onReplyFollowUpClicked = object : OnReplyFollowUpClicked {
        override fun OnReplyFollowUpClicked(followUp: FollowUp?) {
            ScrollAndHighlightFollowUp(followUp)
        }
    }
    val onDownloadButtonClicked = object : OnDownloadButtonClicked {
        override fun onDownloadButtonClicked(
            peigiriItem: PeigiriItem,
            cancel: Boolean
        ) {
            when (cancel) {
                true -> {
                    peigiriItem.followUp.future?.cancel(true)
                    peigiriItem.followUp.fileDownloadProgress = 0
                    peigiriItem.notifyChanged()
                }
                false -> {
                    try {
                        val file =
                            File("$APP_FOLDER${peigiriItem.followUp.fileName}")
                        if (!file.exists()) {
                            if (!file.parentFile?.exists()!!) {
                                file.parentFile?.mkdirs()
                            }
                            file.createNewFile()
                            peigiriItem.followUp.future =
                                Ion.with(this@PeigiriActivity)
                                    .load(Constants.FILE_BASE_URL + peigiriItem.followUp.fileName)
                                    .progressHandler { downloaded, total ->
                                        val percent =
                                            (100 * downloaded / total).toInt()
                                        Log.i(
                                            TAG,
                                            "onProgress: $percent"
                                        )
                                        peigiriItem.followUp.fileDownloadProgress =
                                            percent

                                        peigiriItem.notifyChanged(
                                            PeigiriChangeEnum.PROGRESS_CHANGE
                                        )

                                    }.write(file)
                                    .setCallback { e, result ->
                                        if (e != null) {
                                            Log.i(
                                                "MyTagg",
                                                "onCompleted: " + e.message
                                            )
                                        } else {
                                            peigiriItem.followUp.fileDownloadProgress =
                                                0
                                            peigiriItem.notifyChanged()
                                            if (peigiriItem.followUp.fileTypeEnum != FileTypeEnum.Image) {
                                                OpenFile(result)
                                            }
                                        }
                                    }
                        } else {
                            if (peigiriItem.followUp.file?.exists() == true) {
                                if (file.extension == "apk") {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        if (!packageManager.canRequestPackageInstalls()) {
                                            startActivityForResult(
                                                Intent(
                                                    ACTION_MANAGE_UNKNOWN_APP_SOURCES
                                                ).setData(
                                                    Uri.parse(
                                                        String.format(
                                                            "package:%s",
                                                            packageName
                                                        )
                                                    )
                                                ), 1234
                                            )
                                        } else {
                                            InstallApk(file)
                                        }
                                    } else {
                                        InstallApk(file)
                                    }

                                } else {
                                    OpenFile(file)
                                }
                            } else {
                                //todo download file
                            }
                        }
                    } catch (e: ActivityNotFoundException) {
                        // no Activity to handle this kind of files
                        Log.i(TAG, e.message ?: "")
                    }
                }
            }

        }
    }
    val onPlayVideoClicked = object : OnPlayVideoClicked {
        override fun onPlayVideoClicked(
            file: File?,
            videoName: String,
            videoUrl: String
        ) {
            val intent = Intent(
                this@PeigiriActivity,
                VideoPlayerActivity::class.java
            )
            intent.putExtra("file", file?.path)
            intent.putExtra("videoName", videoName)
            intent.putExtra("videoUrl", videoUrl)
            startActivity(intent)
        }
    }

    val onCancelUploadClicked = object : OnCancelUploadClicked {
        override fun onCancelUploadClicked(peigiriItem: PeigiriItem) {
            peigiriItem.followUp.req?.cancel()
            peigiriAdapter.remove(peigiriItem)
        }
    }

    val onPlayPauseAudioClicked = object : OnPlayPauseAudioClicked {
        override fun onPlayPauseAudioClicked(peigiriItem: PeigiriItem) {

            if (currentPlayingAudioPeigiri == null) {
                currentPlayingAudioPeigiri = peigiriItem
                player.start(peigiriItem.followUp.id)
                return
            }
            if (currentPlayingAudioPeigiri?.followUp?.id == peigiriItem.followUp.id) {
                if (currentPlayingAudioPeigiri?.followUp?.isPlaying == true) {
                    player.pause()
                    return
                }
                player.play()
                return
            }

            player.start(peigiriItem.followUp.id)

        }
    }
    val onPlayerSeekBarChangedListener = object : OnPlayerSeekBarChangedListener {
        override fun onPlayerSeekBarChangedListener(peigiriItem: PeigiriItem, progress: Long) {
            if (currentPlayingAudioPeigiri?.followUp?.id == peigiriItem.followUp.id) {
                player.seekTo(progress)
                currentPlayingAudioPeigiri?.followUp?.trackDuration = player.trackDuration
                currentPlayingAudioPeigiri?.followUp?.currentPlayingPosition =
                    player.currentPosition
                currentPlayingAudioPeigiri?.notifyChanged(PeigiriChangeEnum.PROGRESS_CHANGE)
            } else {
                peigiriItem.followUp.currentPlayingPosition = progress
                peigiriItem.notifyChanged(PeigiriChangeEnum.PROGRESS_CHANGE)
            }
        }
    }
    val onFollowUpClickListener = object : OnFollowUpClickListener {
        override fun OnClick(followUp: FollowUp?) {
            if (currentHighlightedPeigiri == null) {
                Log.i("fb,sfblms","Aa")
                if (followUp?.followUpType == FollowUpType.NewProject||
                    followUp?.followUpType == FollowUpType.CreatedProject||
                    followUp?.followUpType == FollowUpType.SummaryProject) {
                    Log.i("fb,sfblms","ddd")
                    startActivity(
                        Intent(
                            this@PeigiriActivity,
                            PeigiriActivity::class.java
                        ).putExtra("projectId", followUp.newProjectId)
                    )
                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
                }
                else if (followUp?.followUpType == FollowUpType.NewMeeting) {
                startActivity(
                    Intent(
                        this@PeigiriActivity,
                        MeetingActivity::class.java
                    ).putExtra("projectId", followUp?.newMeetingId)
                )
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
                }
            } else {
                if (ll_long_touch.visibility == View.VISIBLE) {
                    btn_close_long_touch.performClick()
                    return
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        counter.cancel()
        player.stop()
    }

    fun openFilePicker(isFaceStatus: Boolean = false) {
        Dexter.withActivity(this).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                if (report.areAllPermissionsGranted()) {
                    val zipTypes = arrayOf(".zip", ".rar")
                    val audioTypes = arrayOf(
                        ".wav",
                        ".pcm",
                        ".aiff",
                        ".mp3",
                        ".aac",
                        ".ogg ",
                        ".wma ",
                        ".flac",
                        ".alac"
                    )
                    if ((viewModel?.currentFilesCount?.value ?: 0) < maxFilesCount) {
                        FilePickerBuilder.instance.setMaxCount(
                            maxFilesCount - (viewModel?.currentFilesCount?.value ?: 0)
                        )
                            .setActivityTheme(R.style.LibAppTheme2)
                            .setActivityTitle("فایلهای خود را انتخاب کنید")
                            .addFileSupport("ZIP", zipTypes)
                            .addFileSupport("APK", arrayOf(".apk"))
                            .addFileSupport("AUDIO", audioTypes)
                            .enableDocSupport(true)
                            .sortDocumentsBy(SortingTypes.name)
                            .pickFile(
                                this@PeigiriActivity,
                                if (isFaceStatus) Constants.UPLOAD_FACE_STATUS_REQUEST_CODE
                                else Constants.UPLOAD_FILE_REQUEST_CODE
                            )
                    }
                } else {
                    if (report.isAnyPermissionPermanentlyDenied) {
//                            try {
//                                showAlertDialog(R.drawable.ic_icons_8_cancel,
//                                    0,
//                                    "با توجه به اینکه شما مجوزهای دسترسی را قبول نکردید ، امکان استفاده از اپلیکیشن وجود ندارد . لطفا از بخش تنظیمات مجوزها را تایید کنید",
//                                    object : InfoAlertDialog.onDismiss() {
//                                        fun ondismiss(dialog: InfoAlertDialog) {
//                                            val intent =
//                                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                                            val uri = Uri.fromParts("package", packageName, null)
//                                            intent.data = uri
//                                            startActivityForResult(
//                                                intent,
//                                                REQUEST_PERMISSION_SETTING
//                                            )
//                                        }
//                                    })
//                            } catch (e: Exception) {
//
//                            }

                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: List<PermissionRequest>,
                token: PermissionToken
            ) {

//                    try {
//                        showAlertDialog(
//                            R.drawable.alert_icon,
//                            0,
//                            "برای اینکه اپلیکیشن به درستی کار کند و پاسخگوی نیازهای شما باشد ، باید تمام مجوزهای دسترسی قبول کنید .",
//                            object : InfoAlertDialog.onDismiss() {
//                                fun ondismiss(dialog: InfoAlertDialog) {
//                                    dialog.dismiss()
//                                    token.continuePermissionRequest()
//                                }
//                            })
//                    } catch (e: Exception) {
//
//                    }

            }
        }).check()
    }



//    "android",
//    URLEncoder.encode(dataFile.Name.toString().trim(),"utf-8"),

    fun openAddActionActivity() {
        startActivityForResult(Intent(this, AddActionActivity::class.java).apply {
            putExtra("project", project)
            putExtra("IsEdit",false)
        }, Constants.ADD_ACTION_FOLLOWUP_REQUEST_CODE)
    }

    fun openMediaPicker() {
        if (viewModel?.currentMediaCount?.value ?: 0 < maxMediaCount) {
            FilePickerBuilder.instance.setMaxCount(
                maxMediaCount - (viewModel?.currentMediaCount?.value ?: 0)
            )
                .setActivityTheme(R.style.LibAppTheme2)
                .setActivityTitle("عکس یا ویدئوی خود را انتخاب کنید")
                .enableCameraSupport(true)
                .enableVideoPicker(true)
                .enableImagePicker(true)
                .showGifs(true)
                .pickPhoto(this@PeigiriActivity, Constants.UPLOAD_MEDIA_REQUEST_CODE)
        }
    }

    fun openVoiceRecorder() {
        if (player.liveDataPlayerState.value == IPlayer.State.PLAY) {
            player.pause()
        }
        val filePath =
            "${Environment.getExternalStorageDirectory()}${File.separator}kasbAudioRecorded.wav"
        val color = resources.getColor(R.color.colorPrimaryDark)
        AndroidAudioRecorder.with(this)
            // Required
            .setFilePath(filePath)
            .setColor(color)
            .setRequestCode(Constants.RECORD_AUDIO_REQUEST_CODE)
            // Optional
            .setSource(AudioSource.MIC)
            .setChannel(AudioChannel.STEREO)
            .setSampleRate(AudioSampleRate.HZ_48000)
            .setAutoStart(true)
            .setKeepDisplayOn(true)
            // Start recording
            .record()
    }

    fun StartProject(projectId: String?) {
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), projectId ?: "")
        val req = App.getRetofitApi()?.startProject(token, requestBody)
        if (isNetConnected()) {
            if (!dialog.isShowing)
                showpDialog(this)
            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {
                        getFollowUps(false)
                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                StartProject(projectId)
                            }
                        })
                    }
                    hidepDialog()
                }
                onFailure = {
                    Log.i(TAG, it?.message ?: "")
                    hidepDialog()
                }
            }
        }
    }
}
