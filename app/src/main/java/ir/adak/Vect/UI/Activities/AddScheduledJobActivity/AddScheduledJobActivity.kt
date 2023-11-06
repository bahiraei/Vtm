package ir.adak.Vect.UI.Activities.AddScheduledJobActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProviders
import ir.adak.Vect.Adapters.MyViewpagerAdapter
import ir.adak.Vect.App
import ir.adak.Vect.Data.Models.MemberDto
import ir.adak.Vect.Data.Models.RegOrEditProjectSchedulerModel
import ir.adak.Vect.Data.Models.ScheduledProjectModel
import ir.adak.Vect.Listeners.OnLoginCompleted
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.AddUsersViewModel
import ir.adak.Vect.UI.Activities.BaseActivity
import ir.adak.Vect.UI.Activities.ScheduledJobsListActivity.ScheduledJobsListActivity
import ir.adak.Vect.UI.Fragments.AddNewTaskFragment
import ir.adak.Vect.UI.Fragments.ScheduleTaskFragment
import ir.adak.Vect.Utils.enqueue
import kotlinx.android.synthetic.main.activity_add_scheduled_job.*

class AddScheduledJobActivity : BaseActivity() {


    enum class DayOfWeekEnum {
        Unspecified(-1),
        Sunday,
        Monday,
        Tuesday,
        Wednesday,
        Thursday,
        Friday,
        Saturday;

        val value: Int

        private object Counter {
            var value: Int = 0
        }

        constructor() {
            Counter.value++
            value = Counter.value.toInt()
        }

        private constructor(value: Int) {
            this.value = value
            Counter.value = value
        }

    }

    enum class ProjectSchedulerEndDateType {
        Unspecified(0),
        LastYear,//آخر سال
        ToDay,//مدت به روز
        ToMonth;//مدت به ماه
        val value: Int

        private object Counter {
            var value: Int = 0
        }

        constructor() {
            Counter.value++
            value = Counter.value.toInt()
        }

        private constructor(value: Int) {
            this.value = value
            Counter.value = value
        }

    }

    enum class SelectedTab {
        SPECIFIC_DAY(1),
        DAILY,
        WEEKLY,
        MONTHLY;
        val value: Int

        private object Counter {
            var value: Int = 0
        }

        constructor() {
            Counter.value++
            value = Counter.value.toInt()
        }

        private constructor(value: Int) {
            this.value = value
            Counter.value = value
        }
    }

    var currentSelectedDay: Int = 0
    var selectedTab = SelectedTab.SPECIFIC_DAY
    var selectedDayOfWeek = DayOfWeekEnum.Unspecified
    var selectedEndDateType = ProjectSchedulerEndDateType.Unspecified


    var endProjectNumber = 0
    var SchedulerHour = 0
    var SchedulerMinute = 0
    var SchedulerY: Int = 0
    var SchedulerM: Int = 0
    var SchedulerD: Int = 0
    var SchedulerStartDate: String = ""
    var SchedulerSelectedStartDate: String = ""
    var SchedulerDate: String = ""


    var SchedulerSelectedHour: String = ""
    var SchedulerSelectedMinute: String = ""
    var SchedulerSelectedTime: String = ""




    val scheduleTaskFragment = ScheduleTaskFragment()
    val addNewTaskFragment = AddNewTaskFragment()

    lateinit var viewModel: AddUsersViewModel

    var jobForEdit: ScheduledProjectModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_scheduled_job)


        jobForEdit = intent.extras?.getParcelable("job")
        val bundle = Bundle()
        bundle.putParcelable("job", jobForEdit)
        scheduleTaskFragment.arguments = bundle
        addNewTaskFragment.arguments = bundle







        viewModel = ViewModelProviders.of(this).get(AddUsersViewModel::class.java)


        val myViewpagerAdapter = MyViewpagerAdapter(
            supportFragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        myViewpagerAdapter.addFragment(scheduleTaskFragment, "")
        myViewpagerAdapter.addFragment(addNewTaskFragment, "")
        add_scheduled_task_viewpager.adapter = myViewpagerAdapter
        txt_activity_title.setOnClickListener{
            startActivity(Intent(this, ScheduledJobsListActivity::class.java))
            overridePendingTransition(
                R.anim.slide_left_in,
                R.anim.slide_left_out
            )
        }
    }




    fun RegisterOrEditScheduledProject(

        id: String,
        title: String,
        description: String,
        letterDateFa: String?,
        letterNumber: String,
        priority: Int,
        projectTypeId: Int,
        PrimaryCredit: Int,
        members: List<MemberDto>,
        isEdit: Boolean
    ) {
        if (isNetConnected()) {
            var dayOfWeekOrMonth: Int? =
                if (selectedTab == SelectedTab.WEEKLY) selectedDayOfWeek.value else if (selectedTab == SelectedTab.MONTHLY) currentSelectedDay else null
            val req = App.getRetofitApi()
                ?.registerOrEditScheduledProject(
                    token,
                    RegOrEditProjectSchedulerModel(
                        id,
                        title,
                        description,
                        SchedulerSelectedStartDate,
                        letterDateFa ?: "",
                        letterNumber,
                        SchedulerSelectedTime,
                        priority,
                        projectTypeId,
                        selectedTab.value,
                        selectedEndDateType.value,
                        endProjectNumber,
                        if (dayOfWeekOrMonth == null) mutableListOf() else mutableListOf(
                            dayOfWeekOrMonth
                        ),
                        PrimaryCredit,
                        members
                    )
                )
            if (!dialog.isShowing)
                showpDialog(this)
            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {
                        var text = if (isEdit) "ویرایش" else "افزوده"
                        Toast.makeText(
                            this@AddScheduledJobActivity,
                            "پروژه با موفقیت $text شد",
                            Toast.LENGTH_LONG
                        ).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                RegisterOrEditScheduledProject(
                                    id,
                                    title,
                                    description,
                                    letterDateFa,
                                    letterNumber,
                                    priority,
                                    projectTypeId,
                                    PrimaryCredit,
                                    members,
                                    isEdit
                                )
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
        if (add_scheduled_task_viewpager.currentItem == 1) {
            add_scheduled_task_viewpager.currentItem = 0
        } else
            super.onBackPressed()
    }

}
