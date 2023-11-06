package ir.adak.Vect.UI.Activities.MeetingActivity
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionManager
import com.google.gson.Gson
import ir.adak.Vect.Adapters.MyViewpagerAdapter
import ir.adak.Vect.App
import ir.adak.Vect.Data.Models.Meeting
import ir.adak.Vect.Data.Models.OfferItem
import ir.adak.Vect.Data.Models.Project
import ir.adak.Vect.Data.Models.RegisterOfferModel
import ir.adak.Vect.Listeners.OnLoginCompleted
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.AddProjectActivity.AddProjectActivity
import ir.adak.Vect.UI.Activities.BaseActivity
import ir.adak.Vect.UI.Activities.MeetingDetailsActivity.MeetingDetailsActivity
import ir.adak.Vect.UI.Dialogs.AddOfferBottomSheet
import ir.adak.Vect.UI.Dialogs.MyQuestionDialog
import ir.adak.Vect.UI.Fragments.JobsFragment
import ir.adak.Vect.UI.Fragments.OffersFragment
import ir.adak.Vect.Utils.Constants
import ir.adak.Vect.Utils.enqueue
import kotlinx.android.synthetic.main.activity_meeting.*
import okhttp3.MediaType
import okhttp3.RequestBody

class MeetingActivity : BaseActivity() {


    enum class SelectedTab {
        JOBS_TAB,
        OFFERS_TAB
    }

    var needToUpdate: Boolean = false
    lateinit var viewModel: MeetingActivityViewModel
    val tabSelectionAnimationDuration = 300L
    var selectedTab = SelectedTab.JOBS_TAB
    val jobsFragment = JobsFragment()
    val offersFragment = OffersFragment()
    var meeting: Meeting? = null
    var meetingPosition = -1
    var flag=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting)
        viewModel = ViewModelProviders.of(this).get(MeetingActivityViewModel::class.java)
        meeting = intent.extras?.getParcelable("meeting")
        meetingPosition = intent.extras?.getInt("position")?:-1
        txt_meeting_title.text = meeting?.title
        txt_users_count.text = "${meeting?.memberCount} نفر"
        val myViewpagerAdapter = MyViewpagerAdapter(
            supportFragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )


        myViewpagerAdapter.addFragment(jobsFragment, "")
        myViewpagerAdapter.addFragment(offersFragment, "")
//        offersFragment.MyAct=this
        jobsFragment.MyAct=this
        view_pager.adapter = myViewpagerAdapter
        if (meeting?.isHeld == true) {
            btn_expand.visibility = View.INVISIBLE
        }

        if (intent.getStringExtra("projectId")!=null)
        {
            flag=true
            Log.i("slcmdkvsfln",intent.getStringExtra("projectId"))
            getProjectsAndOffers_2()
        }else{
            getProjectsAndOffers()
        }


        btn_expand.setOnClickListener {
            TransitionManager.beginDelayedTransition(constraint_parent)
            if (ll_features.visibility == View.GONE) {
                btn_expand.rotation = 180f
                ll_features.visibility = View.VISIBLE
            } else {
                btn_expand.rotation = 0f
                ll_features.visibility = View.GONE
            }
        }

        btn_projects_tab.setOnClickListener {
            if (selectedTab != SelectedTab.JOBS_TAB) {
                selectJobsTab(
                    true
                )

            }

        }
        btn_offers_tab.setOnClickListener {

            if (selectedTab != SelectedTab.OFFERS_TAB) {
                selectOffersTab(
                    true
                )
            }

        }








        btn_add_project.setOnClickListener {
            val intent = Intent(this, AddProjectActivity::class.java)
            intent.putExtra("meetingId", meeting?.id)
            startActivityForResult(
                intent,
                Constants.ADD_PROJECT_REQUEST_CODE
            )
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
        }

        btn_add_offer.setOnClickListener {
            val dialog = AddOfferBottomSheet(token)
            dialog.onSubmitClickedListener = object : AddOfferBottomSheet.OnSubmitClickedListener {
                override fun OnSubmitClicked(offerText: String) {
                    registerOrEditOffer(offerText)
                }
            }
            dialog.show(supportFragmentManager, "")
        }

        btn_close_meeting.setOnClickListener {
            val myQuestionDialog = MyQuestionDialog(
                R.drawable.ic_power_settings_new_black_24dp,
                "اتمام جلسه",
                "آیا از اتمام این جلسه مطمئن هستید؟",
                object : MyQuestionDialog.OnButtonsClicked {
                    override fun OnPositiveClicked() {
                        closeMeeting()
                    }

                    override fun OnNegativeClicked() {
                    }
                })
            myQuestionDialog.show(supportFragmentManager, "")
        }

        btn_meeting_details.setOnClickListener {
            val intent = Intent(this, MeetingDetailsActivity::class.java)
            intent.putExtra("meeting", meeting)
            intent.putExtra("meetingMembers", Gson().toJson(viewModel.meetingMembers))
            startActivityForResult(intent, Constants.MEETING_DETAILS_REQUEST_CODE)
        }

        if (meeting?.orgLevelId != userInfo?.orgLevelId){
            btn_add_project.visibility = View.GONE
            btn_close_meeting.visibility = View.GONE
        }
    }

    private fun registerOrEditOffer(offerText: String) {

        val req = App.getRetofitApi()?.registerOrEditOffer(
            token, RegisterOfferModel(
                offerText,
                null,
                meeting?.id
            )
        )
        if (isNetConnected()) {
            if (!this@MeetingActivity.dialog.isShowing)
                showpDialog(this@MeetingActivity)
            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {
                        val myResponse = it.body()
                        Toast.makeText(
                            this@MeetingActivity,
                            "موضوع شما با موفقیت افزوده شد",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        offersFragment.offersAdapter.add(
                            OfferItem(
                                myResponse?.data?.date,
                                myResponse?.data?.hasProject,
                                myResponse?.data?.id,
                                myResponse?.data?.isDiscussed,
                                myResponse?.data?.currentMeeting,
                                myResponse?.data?.meetingId,
                                myResponse?.data?.orgLevelId,
                                myResponse?.data?.orgLevelTitle,
                                myResponse?.data?.personelName,
                                myResponse?.data?.projects,
                                myResponse?.data?.title
                            )
                        )
                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                registerOrEditOffer(offerText)
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
    fun getProjectsAndOffers() {
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), meeting?.id ?: "")
        val req = App.getRetofitApi()?.getMeetingProjectsAndOffers(token, requestBody)
        req?.enqueue {
            onResponse = {
                if (isResponseValid(it) == 2) {
                    val getMeetingProjectsAndOffersResponse = it.body()?.data
                    viewModel.meetingProjects =
                        getMeetingProjectsAndOffersResponse?.projects?.toMutableList()
                    viewModel.meetingMembers =
                        getMeetingProjectsAndOffersResponse?.members?.toMutableList()
                    viewModel.meetingOffers = mutableListOf()
                    viewModel.meetingOfferItems = mutableListOf()
                    getMeetingProjectsAndOffersResponse?.offers?.forEach {
                        viewModel.meetingOfferItems?.add(
                            OfferItem(
                                it.date,
                                it.hasProject,
                                it.id,
                                it.isDiscussed,
                                it.currentMeeting,
                                it.meetingId,
                                it.orgLevelId,
                                it.orgLevelTitle,
                                it.personelName,
                                it.projects,
                                it.title
                            )
                        )

                    }
                    jobsFragment.jobsAdapter?.updateList(viewModel.meetingProjects)
                    offersFragment.offersAdapter.update(
                        viewModel.meetingOfferItems ?: mutableListOf()
                    )
                } else if (isResponseValid(it) == 1) {
                    silentLogin(object : OnLoginCompleted {
                        override fun OnSuccess() {
                            getProjectsAndOffers()
                        }
                    })
                }
            }
            onFailure = {
                Log.i(TAG, it?.message ?: "")

            }
        }

    }
    fun getProjectsAndOffers_2() {
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), intent.getStringExtra("projectId"))
        val req = App.getRetofitApi()?.getMeetingProjectsAndOffers(token, requestBody)
        req?.enqueue {
            onResponse = {
                if (isResponseValid(it) == 2) {
                    val getMeetingProjectsAndOffersResponse = it.body()?.data
                    viewModel.meetingProjects =
                        getMeetingProjectsAndOffersResponse?.projects?.toMutableList()
                    if (!getMeetingProjectsAndOffersResponse?.meeting?.isMyMeeting!!){
                        Log.i("dvmsdk;vsd","A")
                        btn_add_project.visibility = View.GONE
                        btn_close_meeting.visibility = View.GONE
                    }else{
                        btn_add_project.visibility = View.VISIBLE
                        btn_close_meeting.visibility = View.VISIBLE
                        Log.i("dvmsdk;vsd","B")
                    }
                    viewModel.meetingMembers =
                        getMeetingProjectsAndOffersResponse?.members?.toMutableList()
                    txt_meeting_title.text = getMeetingProjectsAndOffersResponse?.meeting?.title
                    txt_users_count.text = "${getMeetingProjectsAndOffersResponse?.meeting?.memberCount} نفر"
                    viewModel.meetingOffers = mutableListOf()
                    viewModel.meetingOfferItems = mutableListOf()
                    getMeetingProjectsAndOffersResponse?.offers?.forEach {
                        viewModel.meetingOfferItems?.add(
                            OfferItem(
                                it.date,
                                it.hasProject,
                                it.id,
                                it.isDiscussed,
                                it.currentMeeting,
                                it.meetingId,
                                it.orgLevelId,
                                it.orgLevelTitle,
                                it.personelName,
                                it.projects,
                                it.title
                            )
                        )

                    }

                    jobsFragment.jobsAdapter?.updateList(viewModel.meetingProjects)
                    offersFragment.offersAdapter.update(
                        viewModel.meetingOfferItems ?: mutableListOf()
                    )
                } else if (isResponseValid(it) == 1) {
                    silentLogin(object : OnLoginCompleted {
                        override fun OnSuccess() {
                            getProjectsAndOffers_2()
                        }
                    })
                }
            }
            onFailure = {
                Log.i(TAG, it?.message ?: "")

            }
        }

    }



    private fun selectJobsTab(animate: Boolean) {
        selectedTab = SelectedTab.JOBS_TAB
        view_pager.currentItem = 0
        val start_params = guideline_start.layoutParams as ConstraintLayout.LayoutParams
        val end_params = guideline_end.layoutParams as ConstraintLayout.LayoutParams

        val start_valueAnimator = ValueAnimator.ofFloat(0f, 0.5f)
        val end_valueAnimator = ValueAnimator.ofFloat(0.5f, 1f)
        val color_valueAnimator = ValueAnimator.ofFloat(0f, 1f)

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
            btn_projects_tab.setTextColor(
                ColorUtils.blendARGB(
                    Color.parseColor("#707070"),
                    Color.parseColor("#ffffff"),
                    animatedValue
                )
            )
            btn_offers_tab.setTextColor(
                ColorUtils.blendARGB(
                    Color.parseColor("#ffffff"),
                    Color.parseColor("#707070"),
                    animatedValue
                )
            )
        }

        start_valueAnimator.start()
        end_valueAnimator.start()
        color_valueAnimator.start()
    }


    private fun selectOffersTab(animate: Boolean) {
        selectedTab = SelectedTab.OFFERS_TAB
        view_pager.currentItem = 1
        val start_params = guideline_start.layoutParams as ConstraintLayout.LayoutParams
        val end_params = guideline_end.layoutParams as ConstraintLayout.LayoutParams

        val start_valueAnimator = ValueAnimator.ofFloat(0.5f, 0f)
        val end_valueAnimator = ValueAnimator.ofFloat(1f, 0.5f)
        val color_valueAnimator = ValueAnimator.ofFloat(0f, 1f)

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
            btn_projects_tab.setTextColor(
                ColorUtils.blendARGB(
                    Color.parseColor("#ffffff"),
                    Color.parseColor("#707070"),
                    animatedValue
                )
            )
            btn_offers_tab.setTextColor(
                ColorUtils.blendARGB(
                    Color.parseColor("#707070"),
                    Color.parseColor("#ffffff"),
                    animatedValue
                )
            )
        }

        start_valueAnimator.start()
        end_valueAnimator.start()
        color_valueAnimator.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.ADD_PROJECT_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            getProjectsAndOffers()
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
        }
        else if (requestCode == Constants.UPDATE_PROJECT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if ((jobsFragment.jobsAdapter?.items?.size ?: 0) > 0) {
                val position = data?.getIntExtra("position", -1)
                val project = data?.getParcelableExtra<Project>("project")
                if (position != null && position >= 0) {
                    jobsFragment.jobsAdapter?.items!![position] = project ?: Project()
                    jobsFragment.jobsAdapter?.notifyItemChanged(position)
                }
                val intent=Intent()
                intent.putExtra("project" , project)
                setResult(Activity.RESULT_OK , intent)
            }
        }
    }


    private fun closeMeeting() {
        val meetingId = RequestBody.create(
            MediaType.parse("text/plain"),
            meeting?.id ?: ""
        )
        val req = App.getRetofitApi()?.closeMeeting(token, meetingId)
        if (isNetConnected()) {
            if (!dialog.isShowing)
                showpDialog(this)
            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {
                        meeting?.isHeld = true
                            btn_expand.rotation = 0f
                            ll_features.visibility = View.GONE
                            btn_expand.visibility = View.INVISIBLE
                            setResult(Activity.RESULT_OK)

                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                closeMeeting()
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
