package ir.adak.Vect.UI.Activities.ScheduledJobsListActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import ir.adak.Vect.Adapters.ScheduledJobsRecyclerViewAdapter
import ir.adak.Vect.App
import ir.adak.Vect.Data.Enums.PeigiriChangeEnum
import ir.adak.Vect.Listeners.OnItemLongClicked
import ir.adak.Vect.Listeners.OnLoginCompleted
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.AddScheduledJobActivity.AddScheduledJobActivity
import ir.adak.Vect.UI.Activities.BaseActivity
import ir.adak.Vect.UI.Dialogs.MyQuestionDialog
import ir.adak.Vect.Utils.Constants
import ir.adak.Vect.Utils.WrapContentLinearLayoutManager
import ir.adak.Vect.Utils.enqueue
import kotlinx.android.synthetic.main.activity_scheduled_jobs_list.*
import okhttp3.MediaType
import okhttp3.RequestBody

class ScheduledJobsListActivity : BaseActivity() {

    var jobsAdapter: ScheduledJobsRecyclerViewAdapter? = null

    lateinit var gestureDetector: GestureDetector

    var fabX = 0f
    var fabY = 0f

    var highlighPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scheduled_jobs_list)
        setupDrawer()
        gestureDetector = GestureDetector(this, SingleTapConfirm())

        getJobs()
        refresh.setOnRefreshListener {

                getJobs()

        }
        fab_add.setOnTouchListener { view, motionEvent ->
            if (gestureDetector.onTouchEvent(motionEvent)) {

                startActivityForResult(
                    Intent(this, AddScheduledJobActivity::class.java),
                    Constants.ADD_SCHEDULED_PROJECT_REQUEST_CODE
                )
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

    fun getJobs() {
        if (isNetConnected()) {
            val req = App.getRetofitApi()?.getScheduledProject(token)
            showpDialog(this)
            req?.enqueue {
                onResponse = {
                    refresh.isRefreshing=false
                    if (isResponseValid(it) == 2) {
                        val myResponse = it.body()
                        jobsAdapter = ScheduledJobsRecyclerViewAdapter(object :
                            ScheduledJobsRecyclerViewAdapter.OnItemClickedListener {
                            override fun OnItemCLicked(position: Int, projectId: String?) {

                            }
                        })
                        jobsAdapter?.onItemLongClicked = object : OnItemLongClicked {
                            override fun OnItemLongClicked(position: Int) {
                                jobsAdapter?.items!![position].isHighlight = true
                                jobsAdapter?.notifyItemChanged(
                                    position,
                                    PeigiriChangeEnum.HIGHLIGHT
                                )

                                if (highlighPosition != -1) {
                                    if (highlighPosition != position) {
                                        closeLongTouch(highlighPosition)
                                    } else {
                                        closeLongTouch(highlighPosition)
                                        return
                                    }
                                }
                                highlighPosition = position
                                ll_long_touch.visibility = View.VISIBLE

                                btn_edit_job.setOnClickListener {
                                    startActivityForResult(
                                        Intent(
                                            this@ScheduledJobsListActivity,
                                            AddScheduledJobActivity::class.java
                                        ).putExtra(
                                            "job",
                                            jobsAdapter?.items!![position]
                                        ), Constants.EDIT_SCHEDULED_PROJECT_REQUEST_CODE
                                    )
                                    overridePendingTransition(
                                        R.anim.slide_left_in,
                                        R.anim.slide_left_out
                                    )
                                    closeLongTouch(position)
                                }

                                btn_active_deactive_job.setOnClickListener {
                                    var dialogTitle = ""
                                    var dialogQuestion = ""
                                    if (jobsAdapter?.items!![position].isActive

                                    ) {
                                        dialogTitle = "غیر فعال کردن وظیفه"
                                        dialogQuestion =
                                            "آیا از غیر فعال کردن این وظیفه مطمئن هستید؟"
                                    } else {
                                        dialogTitle = "فعال کردن وظیفه"
                                        dialogQuestion = "آیا از فعال کردن این وظیفه مطمئن هستید؟"
                                    }

                                    val myQuestionDialog = MyQuestionDialog(
                                        R.drawable.ic_message_black_24dp,
                                        dialogTitle,
                                        dialogQuestion,
                                        object : MyQuestionDialog.OnButtonsClicked {
                                            override fun OnPositiveClicked() {

                                                ActiveDeactiveJob(position)
                                            }

                                            override fun OnNegativeClicked() {
                                            }
                                        })
                                    myQuestionDialog.show(supportFragmentManager, "")
                                    closeLongTouch(position)
                                }

                                btn_delete_job.setOnClickListener {

                                    val myQuestionDialog = MyQuestionDialog(
                                        R.drawable.ic_delete_black_24dp,
                                        "حذف وظیفه",
                                        "آیا از حذف این وظیفه مطمئن هستید؟",
                                        object : MyQuestionDialog.OnButtonsClicked {
                                            override fun OnPositiveClicked() {
                                                DeleteProject(position)
                                            }

                                            override fun OnNegativeClicked() {
                                            }
                                        })
                                    myQuestionDialog.show(supportFragmentManager, "")

                                    closeLongTouch(position)
                                }

                                btn_close_long_touch.setOnClickListener {
                                    closeLongTouch(position)
                                }

                            }
                        }
                        rv_scheduled_jobs.layoutManager =
                            WrapContentLinearLayoutManager(this@ScheduledJobsListActivity)
                        rv_scheduled_jobs.adapter = jobsAdapter
                        jobsAdapter?.updateList(myResponse?.data)
                        jobsAdapter?.notifyDataSetChanged()
                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                getJobs()
                            }
                        })
                    }
                    hidepDialog()
                }
                onFailure = {
                    refresh.isRefreshing=false
                    Log.i(TAG, it?.message ?: "")
                    hidepDialog()
                }
            }
        }

    }

    private fun DeleteProject(position: Int) {
        val projectId = RequestBody.create(
            MediaType.parse("text/plain"),
            jobsAdapter?.items!![position].id
        )
        val req = App.getRetofitApi()
            ?.DeleteScheduledProject(token, projectId)
        if (isNetConnected()) {
            if (!dialog.isShowing)
                showpDialog(this@ScheduledJobsListActivity)
            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {
                        jobsAdapter?.items?.removeAt(
                            position
                        )
                        jobsAdapter?.notifyItemRemoved(
                            position
                        )
                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                DeleteProject(position)
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


    private fun ActiveDeactiveJob(position: Int) {
        val projectId = RequestBody.create(
            MediaType.parse("text/plain"),
            jobsAdapter?.items!![position].id
        )
        val req =
            App.getRetofitApi()
                ?.ActiveDeactiveScheduledProject(
                    token,
                    projectId
                )
        if (isNetConnected()) {
            if (!dialog.isShowing)
                showpDialog(this@ScheduledJobsListActivity)
            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {
                        if (jobsAdapter?.items!![position].isActive
                        ) {
                            jobsAdapter?.items!![position].isActive =
                                false
                            txt_active_deactive.text =
                                "فعال کردن"
                        } else {
                            jobsAdapter?.items!![position].isActive =
                                true
                            txt_active_deactive.text =
                                "غیر فعال کردن"
                        }
                        jobsAdapter?.notifyItemChanged(
                            position
                        )
                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                ActiveDeactiveJob(position)
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
        jobsAdapter?.items!![position].isHighlight = false
        jobsAdapter?.notifyItemChanged(position, PeigiriChangeEnum.HIGHLIGHT)

        ll_long_touch.visibility = View.INVISIBLE
    }

    class SingleTapConfirm : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            return true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constants.ADD_SCHEDULED_PROJECT_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    getJobs()
                }
            }
            Constants.EDIT_SCHEDULED_PROJECT_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    getJobs()
                }
            }
        }
    }
}
