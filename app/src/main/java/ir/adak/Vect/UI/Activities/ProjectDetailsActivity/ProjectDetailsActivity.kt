package ir.adak.Vect.UI.Activities.ProjectDetailsActivity

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import ir.adak.Vect.Adapters.MyViewpagerAdapter
import ir.adak.Vect.App
import ir.adak.Vect.Data.Enums.FileTypeEnum
import ir.adak.Vect.Data.Enums.FollowUpFilterEnum
import ir.adak.Vect.Data.Enums.ProjectMemberType
import ir.adak.Vect.Data.Models.*
import ir.adak.Vect.Listeners.OnLoginCompleted
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.AddProjectActivity.AddProjectActivity
import ir.adak.Vect.UI.Activities.AddUsersViewModel
import ir.adak.Vect.UI.Activities.BaseActivity
import ir.adak.Vect.UI.Dialogs.ChangeProgressDialog
import ir.adak.Vect.UI.Fragments.EnterExitHistoryFragment
import ir.adak.Vect.UI.Fragments.UsersFragment
import ir.adak.Vect.Utils.Constants
import ir.adak.Vect.Utils.enqueue
import kotlinx.android.synthetic.main.activity_project_details.btn_history_tab
import kotlinx.android.synthetic.main.activity_project_details.btn_more_menu
import kotlinx.android.synthetic.main.activity_project_details.btn_users_tab
import kotlinx.android.synthetic.main.activity_project_details.constraint_parent
import kotlinx.android.synthetic.main.activity_project_details.from_meeting_layout
import kotlinx.android.synthetic.main.activity_project_details.guideline_end
import kotlinx.android.synthetic.main.activity_project_details.guideline_start
import kotlinx.android.synthetic.main.activity_project_details.img_history_icon
import kotlinx.android.synthetic.main.activity_project_details.img_users_icon
import kotlinx.android.synthetic.main.activity_project_details.recy_types
import kotlinx.android.synthetic.main.activity_project_details.roundCornerProgressBar
import kotlinx.android.synthetic.main.activity_project_details.txt_creator_name
import kotlinx.android.synthetic.main.activity_project_details.txt_description
import kotlinx.android.synthetic.main.activity_project_details.txt_enddate
import kotlinx.android.synthetic.main.activity_project_details.txt_face_status_count
import kotlinx.android.synthetic.main.activity_project_details.txt_history_tab
import kotlinx.android.synthetic.main.activity_project_details.txt_meeting_name
import kotlinx.android.synthetic.main.activity_project_details.txt_name_date
import kotlinx.android.synthetic.main.activity_project_details.txt_name_date_title
import kotlinx.android.synthetic.main.activity_project_details.txt_name_number
import kotlinx.android.synthetic.main.activity_project_details.txt_name_number_title
import kotlinx.android.synthetic.main.activity_project_details.txt_percent
import kotlinx.android.synthetic.main.activity_project_details.txt_project_title
import kotlinx.android.synthetic.main.activity_project_details.txt_startdate
import kotlinx.android.synthetic.main.activity_project_details.txt_users_count
import kotlinx.android.synthetic.main.activity_project_details.txt_users_tab
import kotlinx.android.synthetic.main.activity_project_details.viewPager
import kotlinx.android.synthetic.main.activity_project_details_2.*
import kotlinx.android.synthetic.main.project_more_menu_layout.view.*
import okhttp3.MediaType
import okhttp3.RequestBody


class ProjectDetailsActivity : BaseActivity() {

    private var project: Project? = null
    lateinit var addUsersViewModel: AddUsersViewModel
    var members = mutableListOf<UserModel>()
    var memberLogs = mutableListOf<MemberLogDto>()
    var followUpFilterEnum = FollowUpFilterEnum.IMAGES
    var mypopupWindow: PopupWindow? = null
    var adapter_media : Adapter_recyMedia ? = null
    var Id="0"
    enum class SelectedTab {
        USERS,
        HISTORY
    }

    var selectedTab = SelectedTab.USERS
    private val tabSelectionAnimationDuration = 300L



    override fun onBackPressed() {
        var i=Intent()
        i.putExtra("project",project)
        setResult(Activity.RESULT_OK,i)
        super.onBackPressed()
    }
    val usersFragment = UsersFragment()
    val enterExitHistoryFragment = EnterExitHistoryFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_project_details)
        setContentView(R.layout.activity_project_details_2)
        adapter_media= Adapter_recyMedia(this)
        recy_Media.layoutManager=LinearLayoutManager(this,RecyclerView.HORIZONTAL,true)
        recy_Media.adapter=adapter_media
        adapter_media?.Clicl(object :Adapter_recyMedia.Data{
            override fun Clicl(I: Int) {
                if (I==0)
                {
                    var i=Intent()
                    i.putExtra("project",project)
                    i.putExtra("Type","C")
                    setResult(Activity.RESULT_OK,i)
                    finish()
                }
                 if (I==1)
                {
                    var i=Intent()
                    i.putExtra("project",project)
                    i.putExtra("Type","D")
                    setResult(Activity.RESULT_OK,i)
                    finish()
                }
                 if (I==2)
                {
                    var i=Intent()
                    i.putExtra("project",project)
                    i.putExtra("Type","E")
                    setResult(Activity.RESULT_OK,i)
                    finish()
                }
                 if (I==3)
                {
                    var i=Intent()
                    i.putExtra("project",project)
                    i.putExtra("Type","F")
                    setResult(Activity.RESULT_OK,i)
                    finish()
                }
            }

        })
        roundCornerProgressBar.max = 100f
        roundCornerProgressBar.secondaryProgress = 100f
        roundCornerProgressBar.progress = 0f
        addUsersViewModel = ViewModelProviders.of(this).get(AddUsersViewModel::class.java)
        project = intent.extras?.getParcelable<Project>("project")
        setPopUpWindow()
        recy_types.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true)

        if (project?.isMyProject!!)
        {
            btn_more_menu.visibility = View.VISIBLE
        }else{
            btn_more_menu.visibility = View.GONE
        }

//        if (project?.orgLevelId == userInfo?.orgLevelId) {
//               Log.i("DCDSVMSFV","T")
//            if (project?.projectStatus == ProjectStatus.Active.value)
//            {
//                btn_more_menu.visibility = View.VISIBLE
//                Log.i("DCDSVMSFV","T_1")
//            }
//            else
//                btn_more_menu.visibility = View.GONE
//            Log.i("DCDSVMSFV","F")
//        } else {
//            btn_more_menu.visibility = View.GONE
//            Log.i("DCDSVMSFV","F_1")
//        }
        btn_more_menu.setOnClickListener {
            mypopupWindow?.showAsDropDown(it, 8, 16)

        }

        getProjectDetails()
//        val vector = VectorChildFinder(this, R.drawable.ic_job_circle_background, img_job_icon)
//        val path1: VectorDrawableCompat.VFullPath = vector.findPathByName("path1")
//        path1.setFillColor(Color.RED);
        btn_users_tab.setOnClickListener {
            if (selectedTab != SelectedTab.USERS) {
                selectedTab = SelectedTab.USERS
                viewPager.currentItem = 1
                val start_params = guideline_start.layoutParams as ConstraintLayout.LayoutParams
                val end_params = guideline_end.layoutParams as ConstraintLayout.LayoutParams

                val start_valueAnimator = ValueAnimator.ofFloat(0f, 0.5f)
                val end_valueAnimator = ValueAnimator.ofFloat(0.5f, 1f)
                val color_valueAnimator = ValueAnimator.ofFloat(0f, 1f)

                start_valueAnimator.duration = tabSelectionAnimationDuration
                end_valueAnimator.duration = tabSelectionAnimationDuration
                color_valueAnimator.duration = tabSelectionAnimationDuration

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
                    txt_users_tab.setTextColor(
                        ColorUtils.blendARGB(
                            Color.parseColor("#707070"),
                            resources.getColor(R.color.colorPrimary),
                            animatedValue
                        )
                    )
                    txt_history_tab.setTextColor(
                        ColorUtils.blendARGB(
                            resources.getColor(R.color.colorPrimary),
                            Color.parseColor("#707070"),
                            animatedValue
                        )
                    )
                    ImageViewCompat.setImageTintList(
                        img_users_icon,
                        ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary))
                    )
                    ImageViewCompat.setImageTintList(
                        img_history_icon,
                        ColorStateList.valueOf(Color.parseColor("#707070"))
                    )

                }

                start_valueAnimator.start()
                end_valueAnimator.start()
                color_valueAnimator.start()

            }

        }
        btn_history_tab.setOnClickListener {
            if (selectedTab != SelectedTab.HISTORY) {
                selectedTab = SelectedTab.HISTORY
                viewPager.currentItem = 0
                val start_params = guideline_start.layoutParams as ConstraintLayout.LayoutParams
                val end_params = guideline_end.layoutParams as ConstraintLayout.LayoutParams
                val start_valueAnimator = ValueAnimator.ofFloat(0.5f, 0f)
                val end_valueAnimator = ValueAnimator.ofFloat(1f, 0.5f)
                val color_valueAnimator = ValueAnimator.ofFloat(0f, 1f)

                start_valueAnimator.duration = tabSelectionAnimationDuration
                end_valueAnimator.duration = tabSelectionAnimationDuration
                color_valueAnimator.duration = tabSelectionAnimationDuration

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
                    txt_users_tab.setTextColor(
                        ColorUtils.blendARGB(
                            ContextCompat.getColor(this, R.color.colorPrimary),
                            Color.parseColor("#707070"),
                            animatedValue
                        )
                    )
                    txt_history_tab.setTextColor(
                        ColorUtils.blendARGB(
                            Color.parseColor("#707070"),
                            ContextCompat.getColor(this, R.color.colorPrimary),
                            animatedValue
                        )
                    )

                    ImageViewCompat.setImageTintList(
                        img_history_icon,
                        ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary))
                    )
                    ImageViewCompat.setImageTintList(
                        img_users_icon,
                        ColorStateList.valueOf(Color.parseColor("#707070"))
                    )

                }

                start_valueAnimator.start()
                end_valueAnimator.start()
                color_valueAnimator.start()
            }

        }

        addUsersViewModel.selectedUsers.observe(this,
            Observer<MutableList<UserModel>> { items ->
                usersFragment.usersAdapter?.updateList(items)
            })


        textView60.setOnClickListener {
//            val intent = Intent(this, PeigiriActivity::class.java)
//            intent.putExtra("projectId", Id)
//            startActivity(intent)
        }
        textView59.setOnClickListener {
//            val intent = Intent(this, PeigiriActivity::class.java)
//            intent.putExtra("projectId", Id)
//            startActivity(intent)
        }

//        btn_filter_images.setOnClickListener {
//            followUpFilterEnum = FollowUpFilterEnum.IMAGES
//            val intent = Intent()
//            intent.putExtra("followUpFilterEnum", followUpFilterEnum.value)
//            setResult(RESULT_OK, intent)
//            finish()
//        }
//        btn_filter_files.setOnClickListener {
//            followUpFilterEnum = FollowUpFilterEnum.FILES
//            val intent = Intent()
//            intent.putExtra("followUpFilterEnum", followUpFilterEnum.value)
//            setResult(RESULT_OK, intent)
//            finish()
//        }
//        btn_filter_videos.setOnClickListener {
//            followUpFilterEnum = FollowUpFilterEnum.VIDEOS
//            val intent = Intent()
//            intent.putExtra("followUpFilterEnum", followUpFilterEnum.value)
//            setResult(RESULT_OK, intent)
//            finish()
//        }
//        btn_filter_audio.setOnClickListener {
//            followUpFilterEnum = FollowUpFilterEnum.AUDIO
//            val intent = Intent()
//            intent.putExtra("followUpFilterEnum", followUpFilterEnum.value)
//            setResult(RESULT_OK, intent)
//            finish()
//        }
    }
    fun setPopUpWindow() {
        val inflater =
            applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.project_more_menu_layout, null)

        mypopupWindow = PopupWindow(
            view,
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            true
        )
        val typeFace = Typeface.createFromAsset(assets, "fonts/Shabnam.ttf")
        view.txt_edit_project.typeface = typeFace
        view.txt_edit_progress.typeface = typeFace
//        view.txt_deactive_project.typeface = typeFace
//        view.txt_close_project.typeface = typeFace

        view.btn_edit_progress.setOnClickListener {
            mypopupWindow?.dismiss()

            val dialog = ChangeProgressDialog(
                project,
                object : ChangeProgressDialog.OnChangeProgressCompleted {
                    override fun OnChangeProgress(progress: Int) {
                        val intent = Intent()
                        project?.progress = progress
                        intent.putExtra("project", project)
                        intent.putExtra("isProgress", true)
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                })
            dialog.show(supportFragmentManager, "")
        }

//        view.btn_deactive_project.setOnClickListener {
//            view.switch_active_deactive_project.toggle()
//        }

        view.btn_edit_project.setOnClickListener {
            mypopupWindow?.dismiss()
            val intent = Intent(this, AddProjectActivity::class.java)
            intent.putExtra("project", project)
            intent.putExtra("isEdit", true)
            startActivityForResult(intent, Constants.UPDATE_PROJECT_REQUEST_CODE)
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
        }

//        view.switch_active_deactive_project.setOnCheckedChangeListener { compoundButton, isChecked ->
//            val projectId = RequestBody.create(
//                MediaType.parse("text/plain"),
//                project?.id ?: ""
//            )
//            val req = App.getRetofitApi()?.activeDeactiveProject(token, projectId)
//            if (isNetConnected()) {
//                req?.enqueue {
//                    onResponse = {
//                        if (isResponseValid(it)) {
//                            if (isChecked) {
//                                project?.projectStatus = ProjectStatus.Active.value
//                            } else {
//                                project?.projectStatus = ProjectStatus.Deactive.value
//                            }
//                            val intent = Intent()
//                            intent.putExtra(
//                                "isActive",
//                                project?.projectStatus == ProjectStatus.Active.value
//                            )
//                            setResult(RESULT_OK, intent)
//
//                        }
//                    }
//                    onFailure = {
//                        Log.i(TAG, it?.message ?: "")
//                    }
//                }
//            }
//        }

//        view.txt_close_project.setOnClickListener {
//            val projectId = RequestBody.create(
//                MediaType.parse("text/plain"),
//                project?.id ?: ""
//            )
//            val req = App.getRetofitApi()?.closeProject(token, projectId)
//            if (isNetConnected()) {
//                showpDialog(this)
//                req?.enqueue {
//                    onResponse = {
//                        if (isResponseValid(it)) {
//                            if (project?.projectStatus != ProjectStatus.Closed.value
//                            ) {
//                                project?.projectStatus =
//                                    ProjectStatus.Closed.value
//                            }
//                            val intent = Intent()
//                            intent.putExtra(
//                                "isActive",
//                                project?.projectStatus == ProjectStatus.Active.value
//                            )
//                            setResult(RESULT_OK, intent)
//                        }
//                        hidepDialog()
//                    }
//                    onFailure = {
//                        Log.i(TAG, it?.message ?: "")
//                        hidepDialog()
//                    }
//                }
//            }
//        }
    }
    fun getProjectDetails() {
        if (isNetConnected()) {
            if (!dialog.isShowing)
                showpDialog(this)
            val requestBody = RequestBody.create(MediaType.parse("text/plain"), project?.id ?: "")
            val req = App.getRetofitApi()?.getProjectDetails(token, requestBody)
            req?.enqueue {
                onResponse = {
                    Log.i("Tagong", it.code().toString())
                    if (isResponseValid(it) == 2) {
                        val getProjectDetailsResponse = it.body()?.data

//                        Picasso.get()
//                            .load(Constants.AVATAR_BASE_URL+getProjectDetailsResponse.)
//                            .into(img_avatar)

                        if (getProjectDetailsResponse?.projectParent!=null)
                        {
                            Log.i("dvmksvksv",getProjectDetailsResponse?.projectParent.title)
                            textView60.visibility=View.VISIBLE
                            Id= getProjectDetailsResponse?.projectParent.id.toString()
                            textView60.setText("پروژه مرجع : "+getProjectDetailsResponse?.projectParent.title)
                        }else{
                            Id="0"
                            Log.i("dvmksvksv","B")
                            textView60.visibility=View.GONE
                            textView59.visibility=View.GONE
                        }
                        Log.i("zvzvz",
                            getProjectDetailsResponse?.projectAmarCount?.get(0)?.count.toString()
                        )

                        var i=Type_data()
                        i.name="جلسه"
                        i.id=-90;
                        var c=CreateProjectTypeCountDto(i,
                            getProjectDetailsResponse?.meetingCount!!
                        )
                        getProjectDetailsResponse?.projectAmarCount.add(c)
                        var d=Adaptr_types2(getProjectDetailsResponse?.projectAmarCount!!,applicationContext)
                        recy_types.adapter=d
                        d.Clicl(object :Adaptr_types2.Data{
                            var I=Intent()
                            override fun Type(S: String) {
                                if (S.equals("A"))
                                {
                                    I.putExtra("Type","A")
                                    I.putExtra("project",project)
                                    setResult(Activity.RESULT_OK,I)
                                    finish()
                                }else if (S.equals("B")){
                                    I.putExtra("Type","B")
                                    I.putExtra("project",project)
                                    setResult(Activity.RESULT_OK,I)
                                    finish()
                                }
                            }

                        })

                        project = getProjectDetailsResponse?.projectInfo
                        members =
                            getProjectDetailsResponse?.members?.toMutableList() ?: mutableListOf()
                        memberLogs =
                            getProjectDetailsResponse?.memberLog?.toMutableList() ?: mutableListOf()

                        Handler().postDelayed({
                            TransitionManager.beginDelayedTransition(constraint_parent)

                            txt_creator_name.text =
                                "ایجاد کننده: ${project?.personFullName} (${project?.orgLevelTitle})"

                            txt_users_count.text =
                                if (members.size > 0) "${members.size} نفر" else "بدون عضو"
                            txt_project_title.text = project?.title
                            txt_description.text = project?.description
                            txt_startdate.text = project?.persianStartDate
                            if(project?.persianEndDate==null||project?.persianEndDate.isNullOrBlank())
                            {
                                txt_enddate.text="نامشخص"
                            }else{
                                txt_enddate.text = project?.persianEndDate
                            }

                            if (!project?.letterDatefa.isNullOrEmpty() || !project?.letterNumber.isNullOrEmpty()) {
                                txt_name_date_title.visibility = View.VISIBLE
                                txt_name_date.visibility = View.VISIBLE
                                txt_name_number_title.visibility = View.VISIBLE
                                txt_name_number.visibility = View.VISIBLE
                                txt_name_date.text = project?.letterDatefa
                                txt_name_number.text = project?.letterNumber
                            }

                            roundCornerProgressBar.progress = project?.progress?.toFloat() ?: 0f

                            txt_percent.text = "${project?.progress?.toString()}%"

                            if (project?.progress?.toFloat() ?: 0f > 45f) {
                                txt_percent.setTextColor(Color.WHITE)
                                var params =
                                    txt_percent.layoutParams as ConstraintLayout.LayoutParams
                                params.horizontalBias = 0.25f
                                txt_percent.layoutParams = params
                            }

                            if (project?.meetingId != null) {
                                from_meeting_layout.visibility = View.VISIBLE
                                txt_meeting_name.text = project?.meetingTitle
                            } else {
                                from_meeting_layout.visibility = View.GONE
                            }
                            getProjectDetailsResponse?.projectAmarCount?.forEach {

//                                when (it.type) {
//                                    ProjectType.Purpose.value -> {
//                                        txt_goal_count.text = it.count.toString()
//                                    }
//                                    ProjectType.Project.value -> {
//                                        txt_project_count.text = it.count.toString()
//                                    }
//                                    ProjectType.Job.value -> {
//                                        txt_jobs_count.text = it.count.toString()
//                                    }
//                                    ProjectType.Incident.value -> {
//                                        txt_accident_count.text = it.count.toString()
//                                    }
//                                }
                            }
//                            txt_meeting_count.text =

                                getProjectDetailsResponse?.meetingCount.toString()
                            getProjectDetailsResponse?.followupFileCount?.forEach {
                                when (it.type) {
                                    FileTypeEnum.Unknown.value ->
//                                        txt_files_count.text =
//                                        it.count.toString()
                                        adapter_media?.One=it.count.toString()
                                    FileTypeEnum.FaceStatus.value -> txt_face_status_count.text =
                                        it.count.toString()
                                    FileTypeEnum.Image.value ->
//                                        txt_pics_count.text =
//                                        it.count.toString()
                                        adapter_media?.Zero=it.count.toString()
                                    FileTypeEnum.Video.value ->
//                                        txt_vidoes_count.text =
//                                        it.count.toString()
                                        adapter_media?.Theere=it.count.toString()
                                    FileTypeEnum.Audio.value ->
//                                        txt_voices_count.text =
//                                        it.count.toString()
                                        adapter_media?.Four=it.count.toString()
                                }
                            }
                            adapter_media?.notifyDataSetChanged()
                            val intent = Intent()
                            intent.putExtra("project", project)
                            setResult(Activity.RESULT_OK, intent)

                            getProjectMemberAndGroups()


                        }, 500)
                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                getProjectDetails()
                            }
                        })
                    }
                }
                onFailure = {
                    Log.i(TAG, it?.message ?: "")
                    hidepDialog()
                }
            }


        }
    }

    private fun getProjectMemberAndGroups() {
        var projectIdRequestBody =
            RequestBody.create(MediaType.parse("text/plain"), project?.id ?: "")
        val req = App.getRetofitApi()
            ?.getProjectMemberAndGroups(token, projectIdRequestBody)
        req?.enqueue {
            onResponse = {
                if (isResponseValid(it) == 2) {
                    val getProjectMembersAndGroupsResponseModel =
                        it.body()?.data
                    if (getProjectMembersAndGroupsResponseModel?.groups != null) {
                        getProjectMembersAndGroupsResponseModel.groups.forEach {
                            val members = mutableListOf<GroupItem>()
                            it.members.forEach { userModel ->

                                members.add(
                                    GroupItem(
                                        fullName = userModel.fullName,
                                        isChild = userModel.isChild,
                                        orgLevelId = userModel.orgLevelId,
                                        orgLevelTitle = userModel.orgLevelTitle,
                                        type = userModel.type,
                                        access = userModel.access,
                                        profileImage = userModel.profileImage,
                                        selected = false
                                    )
                                )
                            }
                            addUsersViewModel.selectedGroups.add(
                                GroupHeaderItem(
                                    groupLevel = -1,
                                    groupId = it.groupId,
                                    groupMembers = members.apply {
                                        forEach { groupItem ->
                                            getProjectMembersAndGroupsResponseModel.currentMembers?.forEach { userModel ->
                                                if (groupItem.orgLevelId == userModel.orgLevelId) {
                                                    groupItem.selected = true
                                                }
                                            }
                                        }
                                    },
                                    groupTitle = it.title,
                                    groupType = it.type
                                )
                            )
                        }
                    }
                    if (getProjectMembersAndGroupsResponseModel?.partners != null) {




                        if (getProjectMembersAndGroupsResponseModel.currentMembers != null) {


                        val iterator =
                            getProjectMembersAndGroupsResponseModel.currentMembers.iterator()
                        while (iterator.hasNext()) {
                            val item = iterator.next()
                            if (item.type == ProjectMemberType.Mojry.value) {

                                val iterator2 =
                                    getProjectMembersAndGroupsResponseModel.partners.iterator()
                                while (iterator2.hasNext()) {
                                    val item2 = iterator2.next()
                                    if (item2.orgLevelId == item.orgLevelId) {
                                        iterator2.remove()
                                    }
                                }
                                iterator.remove()
                            }
                        }
                    }

                        addUsersViewModel.selectedPartners =
                            getProjectMembersAndGroupsResponseModel.partners.apply {
                                forEach { userModel1 ->
                                    getProjectMembersAndGroupsResponseModel.currentMembers?.forEach { userModel2 ->
                                        if (userModel1.orgLevelId == userModel2.orgLevelId) {
                                            userModel1.selected = true
                                        }
                                    }
                                }
                            }
                    }
                    setupViewPager(viewPager, members, memberLogs)
                } else if (isResponseValid(it) == 1) {
                    silentLogin(object : OnLoginCompleted {
                        override fun OnSuccess() {
                            getProjectMemberAndGroups()
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


    private fun setupViewPager(
        viewPager: ViewPager,
        members: MutableList<UserModel>?,
        memberLogs: MutableList<MemberLogDto>?
    ) {
        val adapter = MyViewpagerAdapter(
            supportFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        val bun = Bundle()
        bun.putString("memberLogs", Gson().toJson(memberLogs))
        enterExitHistoryFragment.arguments = bun
        adapter.addFragment(enterExitHistoryFragment, "تاریخچه")
        addUsersViewModel.selectedUsers.value = members
        val bundle = Bundle()
        bundle.putParcelable("project", project)
        usersFragment.arguments = bundle
        adapter.addFragment(usersFragment, "اعضا")
        viewPager.adapter = adapter
        viewPager.currentItem = 1
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.UPDATE_PROJECT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            getProjectDetails()
        }
    }
}
