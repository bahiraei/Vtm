package ir.adak.Vect.UI.Activities.MainActivity
import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.ColorUtils
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProviders
import ir.adak.Vect.Adapters.MyViewpagerAdapter
import ir.adak.Vect.App
import ir.adak.Vect.Data.Enums.FilterProjectRegisterType
import ir.adak.Vect.Data.Enums.PeigiriChangeEnum
import ir.adak.Vect.Data.Enums.ProjectStatus
import ir.adak.Vect.Data.Models.*
import ir.adak.Vect.Listeners.OnLoginCompleted
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.AddMeetingActivity.AddMeetingActivity
import ir.adak.Vect.UI.Activities.AddNoteActivity.AddNoteActivity
import ir.adak.Vect.UI.Activities.AddProjectActivity.AddProjectActivity
import ir.adak.Vect.UI.Activities.BaseActivity
import ir.adak.Vect.UI.Activities.ChooseMeetingActivity.ChooseMeetingActivity
import ir.adak.Vect.UI.Activities.MainActivity.MainActivity.SelectedTab.*
import ir.adak.Vect.UI.Dialogs.FilterMeetingsBottomSheet
import ir.adak.Vect.UI.Dialogs.FilterTasksBottomSheet
import ir.adak.Vect.UI.Dialogs.MyQuestionDialog
import ir.adak.Vect.UI.Fragments.JobsFragment
import ir.adak.Vect.UI.Fragments.MeetingsFragment
import ir.adak.Vect.UI.Fragments.NotesFragment
import ir.adak.Vect.Utils.Constants
import ir.adak.Vect.Utils.enqueue
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.edt_search
import kotlinx.android.synthetic.main.activity_main.guideline_end
import kotlinx.android.synthetic.main.activity_main.guideline_start
import kotlinx.android.synthetic.main.activity_main.view_pager
import okhttp3.MediaType
import okhttp3.RequestBody
class MainActivity : BaseActivity() {

//    var tempFilterProjectModel: FilterProjectModel? = FilterProjectModel()
     var mytempFilterProjectModel: FilterProjectModel? = FilterProjectModel()
    var tempFilterMeetingModel: FilterMeetingModel? = FilterMeetingModel()

    var fabX = 0f
    var fabY = 0f
    lateinit var gestureDetector: GestureDetector


    enum class SelectedTab {
        JOBS,
        MEETINGS,
        NOTES
    }
    companion object{
        var Flag_filtte=false
        var tempFilterProjectModel: FilterProjectModel? = FilterProjectModel()
        var selectedFilterMeetingModel: FilterMeetingModel? = FilterMeetingModel()
        var selectedFilterProjectModel: FilterProjectModel? = FilterProjectModel()
        var Flag_Project:Boolean =true
        var Counter_2:Int=0
    }
    enum class SelectedFilter {
        NO_FILTER,
        MY_PROJECTS,
        OTHERS_PROJECTS
    }


    var highlighPosition: Int = -1
    val color_valueAnimator = ValueAnimator.ofFloat(0f, 1f)
    lateinit var start_valueAnimator: ValueAnimator
    lateinit var end_valueAnimator: ValueAnimator
    var selectedFilter = SelectedFilter.NO_FILTER
      var selectedTab = JOBS
    private val tabSelectionAnimationDuration = 300L
    lateinit var viewModel: MainActivityViewModel
    val jobsFragment = JobsFragment()
    val meetingsFragment = MeetingsFragment()
    val notesFragment = NotesFragment()
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val tab = intent?.extras?.getString("tab")
        when (tab) {
            "Jobs" -> {
                selectJobsTab(true)
            }
            "Meetings" -> {
                selectMeetingsTab(true)
            }
            "Notes" -> {
                selectNotesTab(true)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            val window = window
//            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
//        }


        setContentView(R.layout.activity_main)

        selectedFilterProjectModel = intent.extras?.getParcelable("selectedFilterProjectModel")

        selectedFilterProjectModel=FilterProjectModel()


        changeTabListener = object : ChangeTabListener {
            override fun changeTab(tab: String) {
                when (tab) {
                    "Jobs" -> {
                        selectJobsTab(true)
                    }
                    "Meetings" -> {
                        selectMeetingsTab(true)
                    }
                    "Notes" -> {
                        selectNotesTab(true)
                    }
                }
            }
        }
        setupDrawer()
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        gestureDetector = GestureDetector(this, SingleTapConfirm())
        val myViewpagerAdapter = MyViewpagerAdapter(
            supportFragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        myViewpagerAdapter.addFragment(jobsFragment, "")
        myViewpagerAdapter.addFragment(meetingsFragment, "")
        myViewpagerAdapter.addFragment(notesFragment, "")
        notesFragment.MyAct=this
        meetingsFragment.MyAct=this
        jobsFragment.MyAct=this
        view_pager.offscreenPageLimit = 3
        view_pager.adapter = myViewpagerAdapter

        selectedFilterProjectModel?.ProjectStatus=1

        if (intent.getStringExtra("Type")!=null)
        {
            if (intent.getStringExtra("Type").equals("12"))
            {
                selectedFilterProjectModel = intent.extras?.getParcelable("selectedFilterProjectModel")
//                view_pager.currentItem=1
//                meetingsFragment.getMeetingsFirstPage()
                selectMeetingsTab(true)

            }else if (intent.getStringExtra("Type").equals("14"))
            {
                selectedFilterProjectModel = intent.extras?.getParcelable("selectedFilterProjectModel")
//                view_pager.currentItem=1
//                meetingsFragment.getMeetingsFirstPage()
                selectNotesTab(true)
            }
            else if (intent.getStringExtra("Type").equals("16"))
            {
                selectedFilterProjectModel?.ProjectStatus=0
                Log.i("svlmsfmbsml", selectedFilterProjectModel?.ProjectStatus.toString())
                jobsFragment.getJobsFirstPage()
//                view_pager.currentItem=1
//                meetingsFragment.getMeetingsFirstPage()

            }
            else if (intent.getStringExtra("Type").equals("18"))
            {
                selectedFilterProjectModel?.ProjectStatus=3
                jobsFragment.getJobsFirstPage()
//                view_pager.currentItem=1
//                meetingsFragment.getMeetingsFirstPage()

            }
            else if (intent.getStringExtra("Type").equals("20"))
            {
                selectedFilterProjectModel?.memberType=1
                jobsFragment.getJobsFirstPage()
//                view_pager.currentItem=1
//                meetingsFragment.getMeetingsFirstPage()

            }
            else if (intent.getStringExtra("Type").equals("22"))
            {
                selectedFilterProjectModel?.memberType=2
                jobsFragment.getJobsFirstPage()
//                view_pager.currentItem=1
//                meetingsFragment.getMeetingsFirstPage()

            }
            else if (intent.getStringExtra("Type").equals("24"))
            {
                selectedFilterProjectModel?.memberType=3
                jobsFragment.getJobsFirstPage()
//                view_pager.currentItem=1
//                meetingsFragment.getMeetingsFirstPage()

            }
            else if (intent.getStringExtra("Type").equals("30"))
            {
                selectedFilterProjectModel?.actionStatusPending=true
                jobsFragment.getJobsFirstPage()
//                view_pager.currentItem=1
//                meetingsFragment.getMeetingsFirstPage()

            }
        }
        btn_tasks.setOnClickListener {
            selectJobsTab(true)
        }
        btn_meetings.setOnClickListener {
            selectMeetingsTab(true)
        }

        btn_notes.setOnClickListener {
//            Toast.makeText(this, "این ماژول فعال نمیباشد", Toast.LENGTH_LONG).show()
            selectNotesTab(true)
        }

        edt_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val input = p0.toString()
                val params = guideline_filter_buttons.layoutParams as ConstraintLayout.LayoutParams


                if (input.length == 1) {
                    val valueAnimator = ValueAnimator.ofInt(params.guideEnd, dp2px(0))
                    valueAnimator.duration = 300
                    valueAnimator.addUpdateListener {
                        val animatedValue = it.animatedValue as Int
                        params.guideEnd = animatedValue
                        guideline_filter_buttons.layoutParams = params
                        guideline_filter_buttons.invalidate()
                    }
                    valueAnimator.start()
                } else if (input.length == 0) {
                    val valueAnimator = ValueAnimator.ofInt(params.guideEnd, dp2px(136))
                    valueAnimator.duration = 300
                    valueAnimator.addUpdateListener {
                        val animatedValue = it.animatedValue as Int
                        params.guideEnd = animatedValue
                        guideline_filter_buttons.layoutParams = params
                        guideline_filter_buttons.invalidate()
                    }
                    valueAnimator.start()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
        edt_search.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (textView.text.isEmpty() || textView.text.length > 1) {
                    when (selectedTab) {
                        JOBS -> {
                            selectedFilterProjectModel?.Title = textView.text.toString()
                            jobsFragment.getJobsFirstPage()
                        }
                        MEETINGS -> {
                            var text = textView.text.toString()
                            selectedFilterMeetingModel?.Title = text
                            meetingsFragment.getMeetingsFirstPage()
                        }
                        NOTES -> {
                            notesFragment.filterdNoteList.clear()
                            if (textView.text.isEmpty()) {
                                notesFragment.notesAdapter.update(notesFragment.noteList)
                            } else {
                                notesFragment.filterdNoteList.addAll(notesFragment.noteList.filter {
                                    it.title?.toLowerCase()
                                        ?.contains(textView.text.toString().toLowerCase()) ?: false
                                            || it.description?.toLowerCase()
                                        ?.contains(textView.text.toString().toLowerCase()) ?: false
                                })
                                notesFragment.notesAdapter.update(notesFragment.filterdNoteList)
                            }

                        }
                    }
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
        btn_filter_my_projects.setOnClickListener {
            when(selectedTab){
                JOBS -> {
//                    unselectAllFilters()
//                    if (selectedFilter != SelectedFilter.MY_PROJECTS) {
//                        selectedFilter = SelectedFilter.MY_PROJECTS
//                        ImageViewCompat.setImageTintList(
//                            btn_filter_my_projects,
//                            ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
//                        )
//                        selectedFilterProjectModel?.RegisterType =
//                            FilterProjectRegisterType.Myself.value
//                        jobsFragment.getJobsFirstPage()
//                    } else {
//                        selectedFilter = SelectedFilter.NO_FILTER
//                        selectedFilterProjectModel?.RegisterType =
//                            FilterProjectRegisterType.NoFilter.value
//                        jobsFragment.getJobsFirstPage()
//                    }
                }
                MEETINGS -> {
                    unselectAllFilters()
                    if (selectedFilter != SelectedFilter.MY_PROJECTS) {
                        selectedFilter = SelectedFilter.MY_PROJECTS
                        ImageViewCompat.setImageTintList(
                            btn_filter_my_projects,
                            ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                        )
                        selectedFilterMeetingModel?.RegisterType =
                            FilterProjectRegisterType.Myself.value
                        meetingsFragment.getMeetingsFirstPage()
                    } else {
                        selectedFilter = SelectedFilter.NO_FILTER
                        selectedFilterMeetingModel?.RegisterType =
                            FilterProjectRegisterType.NoFilter.value
                        meetingsFragment.getMeetingsFirstPage()
                    }
                }
                NOTES -> {

                }
            }


        }
        btn_filter_others_projects.setOnClickListener {
            when(selectedTab){
                JOBS -> {
//                    unselectAllFilters()
//                    if (selectedFilter != SelectedFilter.OTHERS_PROJECTS) {
//                        selectedFilter = SelectedFilter.OTHERS_PROJECTS
//                        ImageViewCompat.setImageTintList(
//                            btn_filter_others_projects,
//                            ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
//                        )
//                        selectedFilterProjectModel?.RegisterType =
//                            FilterProjectRegisterType.Other.value
//                        jobsFragment.getJobsFirstPage()
//                    } else {
//                        selectedFilter = SelectedFilter.NO_FILTER
//                        selectedFilterProjectModel?.RegisterType =
//                            FilterProjectRegisterType.NoFilter.value
//                        jobsFragment.getJobsFirstPage()
//                    }
                }
                MEETINGS -> {

                    unselectAllFilters()
                    if (selectedFilter != SelectedFilter.OTHERS_PROJECTS) {
                        selectedFilter = SelectedFilter.OTHERS_PROJECTS
                        ImageViewCompat.setImageTintList(
                            btn_filter_others_projects,
                            ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                        )
                        selectedFilterMeetingModel?.RegisterType =
                            FilterProjectRegisterType.Other.value
                        meetingsFragment.getMeetingsFirstPage()
                    } else {
                        selectedFilter = SelectedFilter.NO_FILTER
                        selectedFilterMeetingModel?.RegisterType =
                            FilterProjectRegisterType.NoFilter.value
                        meetingsFragment.getMeetingsFirstPage()
                    }
                }
                NOTES -> {

                }
            }

        }
        btn_filter.setOnClickListener {
            when (selectedTab) {
                JOBS -> {
                    val filterTasksBottomSheet = FilterTasksBottomSheet()
                    filterTasksBottomSheet.onDismissListener = object : FilterTasksBottomSheet.OnDismissListener{
                        override fun OnDismiss() {
//                            when(selectedFilterProjectModel?.RegisterType){
//                                FilterProjectRegisterType.NoFilter.value->{
//                                    unselectAllFilters()
//                                    selectedFilter = SelectedFilter.NO_FILTER
//                                }
//                                FilterProjectRegisterType.Myself.value->{
//                                    unselectAllFilters()
//                                    selectedFilter = SelectedFilter.MY_PROJECTS
//                                    ImageViewCompat.setImageTintList(
//                                        btn_filter_my_projects,
//                                        ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
//                                    )
//                                }
//                                FilterProjectRegisterType.Other.value->{
//                                    unselectAllFilters()
//                                    selectedFilter = SelectedFilter.OTHERS_PROJECTS
//                                    ImageViewCompat.setImageTintList(
//                                        btn_filter_others_projects,
//                                        ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
//                                    )
//                                }
//                            }
                        }
                    }
                    filterTasksBottomSheet.show(supportFragmentManager, "")
                }
                MEETINGS -> {
                    val filterMeetingsBottomSheet = FilterMeetingsBottomSheet()

                    filterMeetingsBottomSheet.onDismissListener = object : FilterMeetingsBottomSheet.OnDismissListener{
                        override fun OnDismiss() {

                            when(selectedFilterMeetingModel?.RegisterType){
                                FilterProjectRegisterType.NoFilter.value->{
                                    unselectAllFilters()
                                    selectedFilter = SelectedFilter.NO_FILTER
                                }
                                FilterProjectRegisterType.Myself.value->{
                                    unselectAllFilters()
                                    selectedFilter = SelectedFilter.MY_PROJECTS
                                    ImageViewCompat.setImageTintList(
                                        btn_filter_my_projects,
                                        ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                                    )
                                }
                                FilterProjectRegisterType.Other.value->{
                                    unselectAllFilters()
                                    selectedFilter = SelectedFilter.OTHERS_PROJECTS
                                    ImageViewCompat.setImageTintList(
                                        btn_filter_others_projects,
                                        ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                                    )
                                }
                            }
                        }
                    }

                    filterMeetingsBottomSheet.show(supportFragmentManager, "")

                }
                NOTES -> {

                }
            }
        }
        fab_add.setOnTouchListener { view, motionEvent ->


            if (gestureDetector.onTouchEvent(motionEvent)) {
                when (selectedTab) {
                    JOBS -> {
                        startActivityForResult(
                            Intent(this, AddProjectActivity::class.java),
                            Constants.ADD_PROJECT_REQUEST_CODE
                        )
                    }
                    MEETINGS -> {
                        startActivityForResult(
                            Intent(
                                this, AddMeetingActivity
                                ::class.java
                            ),
                            Constants.ADD_MEETING_REQUEST_CODE
                        )
                    }
                    NOTES -> {
                        startActivityForResult(
                            Intent(
                                this, AddNoteActivity
                                ::class.java
                            ),
                            Constants.ADD_NOTE_REQUEST_CODE
                        )
                    }
                }

                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
                true
            } else {
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        fabX = motionEvent.x
                        fabY = motionEvent.y
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        fab_shadow.x = fab_shadow.x + (motionEvent.x - fabX)
                        fab_shadow.y = fab_shadow.y + (motionEvent.y - fabY)
                        true
                    }
                    else -> {
                        false
                    }
                }

            }
        }
    }

    fun selectNotesTab(animate: Boolean) {
        if (!color_valueAnimator.isRunning) {
            if (selectedTab != NOTES) {
                Flag_Project=false;
                view_pager.currentItem = 2
                if (!viewModel.areNotesLoaded)
                    notesFragment.getNotes()
                if (highlighPosition != -1) {
                    closeLongTouch(highlighPosition)
                }
                val start_params =
                    guideline_start.layoutParams as ConstraintLayout.LayoutParams
                val end_params = guideline_end.layoutParams as ConstraintLayout.LayoutParams

                if (selectedTab == MEETINGS) {
                    start_valueAnimator = ValueAnimator.ofFloat(0.3333333333f, 0f)
                    end_valueAnimator = ValueAnimator.ofFloat(0.66666666666f, 0.3333333333f)
                } else {
                    start_valueAnimator = ValueAnimator.ofFloat(0.66666666666f, 0f)
                    end_valueAnimator = ValueAnimator.ofFloat(1f, 0.3333333333f)
                }

                start_valueAnimator.duration = if (animate) tabSelectionAnimationDuration else 0
                end_valueAnimator.duration = if (animate) tabSelectionAnimationDuration else 0
                color_valueAnimator.duration = if (animate) tabSelectionAnimationDuration else 0

                start_valueAnimator.addUpdateListener {
                    val animatedValue = it.animatedValue as Float
                    start_params.guidePercent = animatedValue
                    guideline_start.layoutParams = start_params
                    guideline_start.invalidate()
                }

                end_valueAnimator.addUpdateListener {
                    val animatedValue = it.animatedValue as Float
                    end_params.guidePercent = animatedValue
                    guideline_end.layoutParams = end_params
                    guideline_end.invalidate()

                }

                color_valueAnimator.addUpdateListener {
                    val animatedValue = it.animatedValue as Float
                    btn_notes.setTextColor(
                        ColorUtils.blendARGB(
                            Color.parseColor("#707070"),
                            Color.parseColor("#ffffff"),
                            animatedValue
                        )
                    )
                    if (selectedTab == MEETINGS) {
                        btn_meetings.setTextColor(
                            ColorUtils.blendARGB(
                                Color.parseColor("#ffffff"),
                                Color.parseColor("#707070"),
                                animatedValue
                            )
                        )
                        btn_tasks.setTextColor(
                            ColorUtils.blendARGB(
                                Color.parseColor("#707070"),
                                Color.parseColor("#707070"),
                                animatedValue
                            )
                        )
                    } else {
                        btn_tasks.setTextColor(
                            ColorUtils.blendARGB(
                                Color.parseColor("#ffffff"),
                                Color.parseColor("#707070"),
                                animatedValue
                            )
                        )
                        btn_meetings.setTextColor(
                            ColorUtils.blendARGB(
                                Color.parseColor("#707070"),
                                Color.parseColor("#707070"),
                                animatedValue
                            )
                        )
                    }

                }
                color_valueAnimator.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {

                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        selectedTab = NOTES
                    }

                    override fun onAnimationCancel(p0: Animator?) {

                    }

                    override fun onAnimationStart(p0: Animator?) {

                    }
                })
                start_valueAnimator.start()
                end_valueAnimator.start()
                color_valueAnimator.start()
            }

        }
    }
    fun selectMeetingsTab(animate: Boolean) {
        if (!color_valueAnimator.isRunning) {
            if (selectedTab != MEETINGS) {
                Flag_Project=false;
                view_pager.currentItem = 1
                if (!viewModel.areMeetingLoaded)
                    meetingsFragment.getMeetingsFirstPage()
                if (highlighPosition != -1) {
                    closeLongTouch(highlighPosition)
                }
                val start_params =
                    guideline_start.layoutParams as ConstraintLayout.LayoutParams
                val end_params = guideline_end.layoutParams as ConstraintLayout.LayoutParams



                if (selectedTab == NOTES) {
                    start_valueAnimator = ValueAnimator.ofFloat(0f, 0.3333333333f)
                    end_valueAnimator = ValueAnimator.ofFloat(0.3333333333f, 0.66666666666f)
                }
                else {
                    start_valueAnimator =
                        ValueAnimator.ofFloat(0.66666666666f, 0.3333333333f)
                    end_valueAnimator = ValueAnimator.ofFloat(1f, 0.66666666666f)
                }

                start_valueAnimator.duration = if (animate) tabSelectionAnimationDuration else 0
                end_valueAnimator.duration = if (animate) tabSelectionAnimationDuration else 0
                color_valueAnimator.duration = if (animate) tabSelectionAnimationDuration else 0

                start_valueAnimator.addUpdateListener {
                    val animatedValue = it.animatedValue as Float
                    start_params.guidePercent = animatedValue
                    guideline_start.layoutParams = start_params
                    guideline_start.invalidate()
                }

                end_valueAnimator.addUpdateListener {
                    val animatedValue = it.animatedValue as Float
                    end_params.guidePercent = animatedValue
                    guideline_end.layoutParams = end_params
                    guideline_end.invalidate()

                }

                color_valueAnimator.addUpdateListener {
                    val animatedValue = it.animatedValue as Float
                    btn_meetings.setTextColor(
                        ColorUtils.blendARGB(
                            Color.parseColor("#707070"),
                            Color.parseColor("#ffffff"),
                            animatedValue
                        )
                    )
                    if (selectedTab == NOTES) {
                        btn_notes.setTextColor(
                            ColorUtils.blendARGB(
                                Color.parseColor("#ffffff"),
                                Color.parseColor("#707070"),
                                animatedValue
                            )
                        )
                        btn_tasks.setTextColor(
                            ColorUtils.blendARGB(
                                Color.parseColor("#707070"),
                                Color.parseColor("#707070"),
                                animatedValue
                            )
                        )
                    } else {
                        btn_tasks.setTextColor(
                            ColorUtils.blendARGB(
                                Color.parseColor("#ffffff"),
                                Color.parseColor("#707070"),
                                animatedValue
                            )
                        )
                        btn_notes.setTextColor(
                            ColorUtils.blendARGB(
                                Color.parseColor("#707070"),
                                Color.parseColor("#707070"),
                                animatedValue
                            )
                        )
                    }

                }
                color_valueAnimator.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {

                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        selectedTab = MEETINGS
                    }

                    override fun onAnimationCancel(p0: Animator?) {

                    }

                    override fun onAnimationStart(p0: Animator?) {

                    }
                })
                start_valueAnimator.start()
                end_valueAnimator.start()
                color_valueAnimator.start()

                when(selectedFilterMeetingModel?.RegisterType){
                    FilterProjectRegisterType.NoFilter.value->{
                        unselectAllFilters()
                        selectedFilter = SelectedFilter.NO_FILTER
                    }
                    FilterProjectRegisterType.Myself.value->{
                        unselectAllFilters()
                        selectedFilter = SelectedFilter.MY_PROJECTS
                        ImageViewCompat.setImageTintList(
                            btn_filter_my_projects,
                            ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                        )
                    }
                    FilterProjectRegisterType.Other.value->{
                        unselectAllFilters()
                        selectedFilter = SelectedFilter.OTHERS_PROJECTS
                        ImageViewCompat.setImageTintList(
                            btn_filter_others_projects,
                            ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                        )
                    }
                }

            }

        }
    }
    fun selectJobsTab(animate: Boolean) {
        if (!color_valueAnimator.isRunning) {
            if (selectedTab != JOBS) {
                Flag_Project=true;
                view_pager.currentItem = 0
                if (highlighPosition != -1) {
                    closeLongTouch(highlighPosition)
                }
                val start_params =
                    guideline_start.layoutParams as ConstraintLayout.LayoutParams
                val end_params = guideline_end.layoutParams as ConstraintLayout.LayoutParams

                if (selectedTab == MEETINGS) {
                    start_valueAnimator =
                        ValueAnimator.ofFloat(0.3333333333f, 0.66666666666f)
                    end_valueAnimator = ValueAnimator.ofFloat(0.66666666666f, 1f)
                } else {
                    start_valueAnimator = ValueAnimator.ofFloat(0f, 0.66666666666f)
                    end_valueAnimator = ValueAnimator.ofFloat(0.3333333333f, 1f)
                }

                start_valueAnimator.duration = if (animate) tabSelectionAnimationDuration else 0
                end_valueAnimator.duration = if (animate) tabSelectionAnimationDuration else 0
                color_valueAnimator.duration = if (animate) tabSelectionAnimationDuration else 0

                start_valueAnimator.addUpdateListener {
                    val animatedValue = it.animatedValue as Float
                    start_params.guidePercent = animatedValue
                    guideline_start.layoutParams = start_params
                    guideline_start.invalidate()
                }

                end_valueAnimator.addUpdateListener {
                    val animatedValue = it.animatedValue as Float
                    end_params.guidePercent = animatedValue
                    guideline_end.layoutParams = end_params
                    guideline_end.invalidate()

                }

                color_valueAnimator.addUpdateListener {
                    val animatedValue = it.animatedValue as Float
                    btn_tasks.setTextColor(
                        ColorUtils.blendARGB(
                            Color.parseColor("#707070"),
                            Color.parseColor("#ffffff"),
                            animatedValue
                        )
                    )
                    if (selectedTab == MEETINGS) {
                        btn_meetings.setTextColor(
                            ColorUtils.blendARGB(
                                Color.parseColor("#ffffff"),
                                Color.parseColor("#707070"),
                                animatedValue
                            )
                        )
                        btn_notes.setTextColor(
                            ColorUtils.blendARGB(
                                Color.parseColor("#707070"),
                                Color.parseColor("#707070"),
                                animatedValue
                            )
                        )
                    } else {
                        btn_notes.setTextColor(
                            ColorUtils.blendARGB(
                                Color.parseColor("#ffffff"),
                                Color.parseColor("#707070"),
                                animatedValue
                            )
                        )
                        btn_meetings.setTextColor(
                            ColorUtils.blendARGB(
                                Color.parseColor("#707070"),
                                Color.parseColor("#707070"),
                                animatedValue
                            )
                        )
                    }

                }
                color_valueAnimator.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {

                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        selectedTab = JOBS
                    }

                    override fun onAnimationCancel(p0: Animator?) {

                    }

                    override fun onAnimationStart(p0: Animator?) {

                    }
                })
                start_valueAnimator.start()
                end_valueAnimator.start()
                color_valueAnimator.start()

                unselectAllFilters()

//                when(selectedFilterProjectModel?.RegisterType){
//                    FilterProjectRegisterType.NoFilter.value->{
//                        unselectAllFilters()
//                        selectedFilter = SelectedFilter.NO_FILTER
//                    }
//                    FilterProjectRegisterType.Myself.value->{
//                        unselectAllFilters()
//                        selectedFilter = SelectedFilter.MY_PROJECTS
//                        ImageViewCompat.setImageTintList(
//                            btn_filter_my_projects,
//                            ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
//                        )
//                    }
//                    FilterProjectRegisterType.Other.value->{
//                        unselectAllFilters()
//                        selectedFilter = SelectedFilter.OTHERS_PROJECTS
//                        ImageViewCompat.setImageTintList(
//                            btn_filter_others_projects,
//                            ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
//                        )
//                    }
//                }
            }
        }
    }
    fun unselectAllFilters() {
        ImageViewCompat.setImageTintList(
            btn_filter_my_projects,
            ColorStateList.valueOf(Color.parseColor("#BEBEBE"))
        )
        ImageViewCompat.setImageTintList(
            btn_filter_others_projects,
            ColorStateList.valueOf(Color.parseColor("#BEBEBE"))
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.ADD_PROJECT_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Log.i("cvfbfb","sas")
            selectJobsTab(false)
            jobsFragment.getJobsFirstPage()
        } else if (requestCode == Constants.ADD_MEETING_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectMeetingsTab(false)
            meetingsFragment.getMeetingsFirstPage()
        } else if (requestCode == Constants.ADD_NOTE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectNotesTab(false)
            notesFragment.getNotes()
        } else if (requestCode == Constants.ADD_NOTE_AS_OFFER_TO_MEETING && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "پیشنهاد شما در جلسه انتخابی ثبت شد", Toast.LENGTH_SHORT).show()
        } else if (requestCode == Constants.UPDATE_PROJECT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if ((jobsFragment.jobsAdapter?.items?.size ?: 0) > 0) {
                Log.i("cvfbfb","sas")
                Log.i("mivsdniqqq","Test")
                val position = data?.getIntExtra("position", -1)
                val project = data?.getParcelableExtra<Project>("project")
                if (position != null && position >= 0) {
                    jobsFragment.jobsAdapter?.items!![position] = project?: Project()
                    jobsFragment.jobsAdapter?.notifyItemChanged(position)
                }
            }
        } else if (requestCode == Constants.UPDATE_MEETING_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if ((meetingsFragment.meetingsAdapter.items?.size ?: 0) > 0) {
                val position = data?.getIntExtra("position", -1)
                val meeting = data?.getParcelableExtra<Meeting>("meeting")
                if (position != null && position >= 0) {
                    meetingsFragment.meetingsAdapter.items!![position] = meeting?: Meeting()
                    meetingsFragment.meetingsAdapter.notifyItemChanged(position)
                }
            }
        } else if (requestCode == Constants.OPEN_FROM_DRAWER && resultCode == Activity.RESULT_OK && data != null) {
            val tab = data.extras?.getString("tab")
            when (tab) {
                "Jobs" -> {
                    selectJobsTab(true)
                }
                "Meetings" -> {
                    selectMeetingsTab(true)
                }
                "Notes" -> {
                    selectNotesTab(true)
                }
            }
        }
    }

    fun activeDeactiveProject(position: Int) {
        val projectId = RequestBody.create(
            MediaType.parse("text/plain"),
            jobsFragment.jobsAdapter?.items!![position ?: 0].id ?: ""
        )
        val req =
            App.getRetofitApi()?.activeDeactiveProject(token, projectId)
        if (isNetConnected()) {
            if (!dialog.isShowing)
                showpDialog(this@MainActivity)
            req?.enqueue {
                onResponse = {
                    val myResponse = it.body()
                    if (isResponseValid(it) == 2) {
                        if (jobsFragment.jobsAdapter?.items!![position
                                ?: -1].projectStatus == ProjectStatus.Active.value
                        ) {
                            jobsFragment.jobsAdapter?.items!![position
                                ?: -1].projectStatus =
                                ProjectStatus.Deactive.value
                            txt_active_deactive.text = "فعال کردن"
                        } else if (jobsFragment.jobsAdapter?.items!![position
                                ?: -1].projectStatus == ProjectStatus.Deactive.value
                        ) {
                            jobsFragment.jobsAdapter?.items!![position
                                ?: -1].projectStatus =
                                ProjectStatus.Active.value
                            txt_active_deactive.text = "غیر فعال کردن"
                        }
                        jobsFragment.jobsAdapter?.notifyItemChanged(
                            position ?: -1
                        )
                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                activeDeactiveProject(position)
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


    fun closeProject(position: Int) {
        val projectId = RequestBody.create(
            MediaType.parse("text/plain"),
            jobsFragment.jobsAdapter?.items!![position ?: -1].id ?: ""
        )
        val req = App.getRetofitApi()?.closeProject(token, projectId)
        if (isNetConnected()) {
            if (!dialog.isShowing)
                showpDialog(this@MainActivity)
            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {
                        if (jobsFragment.jobsAdapter?.items!![position
                                ?: -1].projectStatus != ProjectStatus.Closed.value
                        ) {
                            jobsFragment.jobsAdapter?.items!![position
                                ?: -1].projectStatus =
                                ProjectStatus.Closed.value
                        }

                        jobsFragment.jobsAdapter?.notifyItemChanged(
                            position ?: -1
                        )
                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                closeProject(position)
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

    fun OnItemLongClicked(position: Int) {
        if (highlighPosition != -1) {
            if (highlighPosition != position) {
                closeLongTouch(highlighPosition)
            } else {
                closeLongTouch(highlighPosition)
                return
            }
        }
        highlighPosition = position
        constraint_search_and_filter_layout.visibility = View.INVISIBLE
        ll_long_touch.visibility = View.VISIBLE

        btn_close_long_touch.setOnClickListener {
            closeLongTouch(position)
        }
        when (selectedTab) {
            JOBS -> {
                btn_first_feature.visibility = View.GONE
                btn_second_feature.visibility = View.VISIBLE
                btn_third_feature.visibility = View.VISIBLE
                if (jobsFragment.jobsAdapter?.items!![position
                        ?: -1].projectStatus == ProjectStatus.Active.value

                ) {
                    btn_fourth_feature.visibility = View.VISIBLE
                    txt_active_deactive.text = "غیر فعال کردن"
                } else if (jobsFragment.jobsAdapter?.items!![position
                        ?: -1].projectStatus == ProjectStatus.Deactive.value
                ) {
                    btn_fourth_feature.visibility = View.VISIBLE

                    txt_active_deactive.text = "فعال کردن"
                } else {
                    btn_fourth_feature.visibility = View.GONE
                }
                btn_fifth_feature.visibility = View.GONE
                btn_sixth_feature.visibility = View.VISIBLE
                txt_close.text = "اتمام وظیفه"

                btn_second_feature.setOnClickListener {
                    val intent = Intent(this, AddProjectActivity::class.java)
                    intent.putExtra(
                        "project",
                        jobsFragment.jobsAdapter?.items!![position ?: 0]
                    )
                    startActivityForResult(intent, Constants.ADD_PROJECT_REQUEST_CODE)
                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)

                    closeLongTouch(position)
                }
                btn_third_feature.setOnClickListener {
                    val project = jobsFragment.jobsAdapter?.items!![position ?: 0]
                    val intent = Intent(this, AddProjectActivity::class.java)
                    intent.putExtra("project", project)
                    intent.putExtra("position", position)
                    intent.putExtra("isEdit", true)
                    startActivityForResult(intent, Constants.UPDATE_PROJECT_REQUEST_CODE)
                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
                    closeLongTouch(position)
                }
                btn_fourth_feature.setOnClickListener {

                    var dialogTitle = ""
                    var dialogQuestion = ""
                    if (jobsFragment.jobsAdapter?.items!![position
                            ?: -1].projectStatus == ProjectStatus.Active.value

                    ) {
                        dialogTitle = "غیر فعال کردن وظیفه"
                        dialogQuestion = "آیا از غیر فعال کردن این وظیفه مطمئن هستید؟"
                    } else if (jobsFragment.jobsAdapter?.items!![position
                            ?: -1].projectStatus == ProjectStatus.Deactive.value
                    ) {
                        dialogTitle = "فعال کردن وظیفه"
                        dialogQuestion = "آیا از فعال کردن این وظیفه مطمئن هستید؟"
                    }

                    val myQuestionDialog = MyQuestionDialog(
                        R.drawable.ic_message_black_24dp,
                        dialogTitle,
                        dialogQuestion,
                        object : MyQuestionDialog.OnButtonsClicked {
                            override fun OnPositiveClicked() {
                                activeDeactiveProject(position)
                            }

                            override fun OnNegativeClicked() {
                            }
                        })
                    myQuestionDialog.show(supportFragmentManager, "")
                    closeLongTouch(position)

                }
                btn_fifth_feature.setOnClickListener {
                    val myQuestionDialog = MyQuestionDialog(
                        R.drawable.ic_delete_black_24dp,
                        "حذف وظیفه",
                        "آیا از حذف این وظیفه مطمئن هستید؟",
                        object : MyQuestionDialog.OnButtonsClicked {
                            override fun OnPositiveClicked() {

                                deleteProject(position)
                            }

                            override fun OnNegativeClicked() {
                            }
                        })
                    myQuestionDialog.show(supportFragmentManager, "")
                    closeLongTouch(position)
                }
                btn_sixth_feature.setOnClickListener {
                    val myQuestionDialog = MyQuestionDialog(
                        R.drawable.ic_power_settings_new_black_24dp,
                        "اتمام وظیفه",
                        "آیا از اتمام این وظیفه مطمئن هستید؟",
                        object : MyQuestionDialog.OnButtonsClicked {
                            override fun OnPositiveClicked() {
                                closeProject(position)
                            }

                            override fun OnNegativeClicked() {
                            }
                        })
                    myQuestionDialog.show(supportFragmentManager, "")

                    closeLongTouch(position)
                }
            }
            MEETINGS -> {
                btn_first_feature.visibility = View.GONE
                btn_second_feature.visibility = View.VISIBLE
                btn_third_feature.visibility = View.VISIBLE
                btn_fourth_feature.visibility = View.GONE
                btn_fifth_feature.visibility = View.GONE
                btn_sixth_feature.visibility = View.VISIBLE
                txt_close.text = "اتمام جلسه"

                btn_second_feature.setOnClickListener {
                    val intent = Intent(this, AddMeetingActivity::class.java)
                    intent.putExtra(
                        "meeting",
                        meetingsFragment.meetingsAdapter.items!![position ?: 0]
                    )
                    startActivityForResult(intent, Constants.ADD_MEETING_REQUEST_CODE)
                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
                    closeLongTouch(position)
                }
                btn_third_feature.setOnClickListener {
                    val intent = Intent(this, AddMeetingActivity::class.java)
                    intent.putExtra(
                        "meeting",
                        meetingsFragment.meetingsAdapter.items!![position ?: 0]
                    )
                    intent.putExtra("position" , position)
                    intent.putExtra("isEdit", true)
                    startActivityForResult(intent, Constants.UPDATE_MEETING_REQUEST_CODE)
                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
                    closeLongTouch(position)
                }
                btn_sixth_feature.setOnClickListener {
                    val myQuestionDialog = MyQuestionDialog(
                        R.drawable.ic_power_settings_new_black_24dp,
                        "اتمام جلسه",
                        "آیا از اتمام این جلسه مطمئن هستید؟",
                        object : MyQuestionDialog.OnButtonsClicked {
                            override fun OnPositiveClicked() {
                                closeMeeting(position)
                            }

                            override fun OnNegativeClicked() {
                            }
                        })
                    myQuestionDialog.show(supportFragmentManager, "")


                    closeLongTouch(position)
                }
            }
            NOTES -> {
                val note = Note(
                    id = (notesFragment.notesAdapter.getItem(position) as NoteItem).id,
                    title = (notesFragment.notesAdapter.getItem(position) as NoteItem).title,
                    description = (notesFragment.notesAdapter.getItem(position) as NoteItem).description,
                    date = (notesFragment.notesAdapter.getItem(position) as NoteItem).date,
                    dateReminderEn = (notesFragment.notesAdapter.getItem(position) as NoteItem).dateReminderEn,
                    dateReminder = (notesFragment.notesAdapter.getItem(position) as NoteItem).dateReminder,
                    timeReminder = (notesFragment.notesAdapter.getItem(position) as NoteItem).timeReminder
                )

                btn_first_feature.visibility = View.VISIBLE
                btn_second_feature.visibility = View.VISIBLE
                btn_third_feature.visibility = View.VISIBLE
                btn_fourth_feature.visibility = View.GONE
                btn_fifth_feature.visibility = View.VISIBLE
                btn_sixth_feature.visibility = View.GONE
                btn_first_feature.setOnClickListener {
                    val intent = Intent(this, ChooseMeetingActivity::class.java)
                    intent.putExtra(
                        "note", note
                    )
                    startActivityForResult(intent, Constants.ADD_NOTE_AS_OFFER_TO_MEETING)
                    closeLongTouch(position)
                }
                btn_second_feature.setOnClickListener {
                    val intent = Intent(this, AddNoteActivity::class.java)
                    intent.putExtra(
                        "note",
                        note
                    )
                    startActivityForResult(intent, Constants.ADD_NOTE_REQUEST_CODE)
                    closeLongTouch(position)
                }
                btn_third_feature.setOnClickListener {
                    val intent = Intent(this, AddNoteActivity::class.java)
                    intent.putExtra(
                        "note",
                        note
                    )
                    intent.putExtra("isEdit", true)
                    startActivityForResult(intent, Constants.ADD_NOTE_REQUEST_CODE)

                    closeLongTouch(position)
                }
                btn_fifth_feature.setOnClickListener {

                    deleteNote(position)
                    closeLongTouch(position)
                }
            }
        }

    }

    private fun deleteNote(position: Int) {
        val noteId = RequestBody.create(
            MediaType.parse("text/plain"),
            (notesFragment.notesAdapter.getItem(position) as NoteItem).id ?: ""
        )
        val req = App.getRetofitApi()?.deleteNote(token, noteId)
        if (isNetConnected()) {
            if (!dialog.isShowing)
                showpDialog(this)
            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {
                        notesFragment.notesAdapter.remove(
                            notesFragment.notesAdapter.getItem(
                                position
                            )
                        )
                        Toast.makeText(
                            this@MainActivity,
                            "یادداشت شما با موفقیت حذف شد.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                deleteNote(position)
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

    private fun closeMeeting(position: Int) {
        val meetingId = RequestBody.create(
            MediaType.parse("text/plain"),
            meetingsFragment.meetingsAdapter.items!![position ?: -1].id ?: ""
        )
        val req = App.getRetofitApi()?.closeMeeting(token, meetingId)
        if (isNetConnected()) {
            if (!dialog.isShowing)
                showpDialog(this)
            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {
                        meetingsFragment.meetingsAdapter.items!![position
                            ?: -1].isHeld = true

                        meetingsFragment.meetingsAdapter.notifyItemChanged(
                            position ?: -1
                        )
                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                closeMeeting(position)
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

    fun closeLongTouch(position: Int) {
        highlighPosition = -1
        when (selectedTab) {
            JOBS -> {
                jobsFragment.jobsAdapter?.items!![position].isHighlight = false
                jobsFragment.jobsAdapter?.notifyItemChanged(position, PeigiriChangeEnum.HIGHLIGHT)
            }
            MEETINGS -> {
                meetingsFragment.meetingsAdapter.items!![position].isHighlight = false
                meetingsFragment.meetingsAdapter.notifyItemChanged(
                    position,
                    PeigiriChangeEnum.HIGHLIGHT
                )
            }
            NOTES -> {
                (notesFragment.notesAdapter.getItem(position) as NoteItem).isHighlight = false
                notesFragment.notesAdapter.getItem(position)
                    .notifyChanged(PeigiriChangeEnum.HIGHLIGHT)
            }
        }

        constraint_search_and_filter_layout.visibility = View.VISIBLE
        ll_long_touch.visibility = View.INVISIBLE
    }


    fun deleteProject(position: Int) {
        if (isNetConnected()) {
            if (!dialog.isShowing)
                showpDialog(this@MainActivity)
            val projectId = RequestBody.create(
                MediaType.parse("text/plain"),
                jobsFragment.jobsAdapter?.items!![position ?: 0].id ?: ""
            )
            val req = App.getRetofitApi()?.deleteProject(token, projectId)

            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {
                        jobsFragment.jobsAdapter?.items?.removeAt(
                            position ?: -1
                        )
                        jobsFragment.jobsAdapter?.notifyItemRemoved(
                            position ?: -1
                        )
                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                deleteProject(position)
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

    override fun onBackPressed() {
        if (highlighPosition != -1) {
            closeLongTouch(highlighPosition)
        } else {
            super.onBackPressed()
        }
    }

    class SingleTapConfirm : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            return true
        }
    }


}
