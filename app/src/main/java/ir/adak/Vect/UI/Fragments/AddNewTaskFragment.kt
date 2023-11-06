package ir.adak.Vect.UI.Fragments


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog
import ir.adak.Vect.Adapters.TinySelectedUsersRecyclerViewAdapter
import ir.adak.Vect.App
import ir.adak.Vect.Data.Enums.*
import ir.adak.Vect.Data.Models.*
import ir.adak.Vect.Listeners.OnLoginCompleted
import ir.adak.Vect.R
import ir.adak.Vect.Typess
import ir.adak.Vect.UI.Activities.AddProjectActivity.AddProjectActivity
import ir.adak.Vect.UI.Activities.AddScheduledJobActivity.AddScheduledJobActivity
import ir.adak.Vect.UI.Activities.AddUsersViewModel
import ir.adak.Vect.UI.Activities.BaseActivity
import ir.adak.Vect.UI.Dialogs.AddUsersBottomSheet
import ir.adak.Vect.UI.Dialogs.MyAlertDialog
import ir.adak.Vect.Utils.Constants
import ir.adak.Vect.Utils.RecyclerViewOverlapDecoration
import ir.adak.Vect.Utils.SolarCalendar
import ir.adak.Vect.Utils.enqueue
import ir.adak.Vect.adapter_project_type
import kotlinx.android.synthetic.main.add_new_task_layout.*
import okhttp3.MediaType
import okhttp3.RequestBody

/**
 * A simple [Fragment] subclass.
 */
class AddNewTaskFragment : BaseFragment() {
    val mojries = mutableListOf<UserModel>()
    var isTitleValid = false
    var isValidDate = true
    var isValidNameDate = false




    var projectStatus = ProjectStatus.Active



    private var y1: Int = 0
    private var m1: Int = 0
    private var d1: Int = 0
    private var y2: Int = 0
    private var m2: Int = 0
    private var d2: Int = 0
    private var yName: Int = 0
    private var mName: Int = 0
    private var dName: Int = 0
    private var startDate: String = ""
    private var selectedStartDate: String = ""
    private var endDate: String = ""
    private var selectedEndDate: String = ""
    private var nameDate: String = ""
    private var selectedNameDate: String = ""
    private var date: String = ""

    private val usersAdapter = TinySelectedUsersRecyclerViewAdapter(mutableListOf())
    var selectedProjectType = ProjectType.Job
    var selectedPriority = ProjectPriority.Normal
    var parentId: String? = null
    var meetingId: String? = null
    var project: Project? = null
    var position = -1
    var jobForEdit: ScheduledProjectModel? = null
    var isEdit: Boolean = false
    var T_2:ArrayList<Typess> ?=null
    var Type="-80"
    var CB:Boolean=false
    lateinit var viewModel: AddUsersViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.add_new_task_layout, container, false)
        view.rotationY = 180f
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity is AddProjectActivity)
            viewModel = (activity as AddProjectActivity).viewModel
        else if (activity is AddScheduledJobActivity)
            viewModel = (activity as AddScheduledJobActivity).viewModel
        T_2=ArrayList<Typess>()
        recyclerView.layoutManager=LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            true
        )
        parentId = arguments?.getString("parentId")
        meetingId = arguments?.getString("meetingId")
        project = arguments?.getParcelable("project")
        position = arguments?.getInt("position") ?: -1
        jobForEdit = arguments?.getParcelable("job")
        isEdit = arguments?.getBoolean("isEdit") ?: false
        Log.i("testtwwwa",BaseActivity.projectTypes?.size.toString())
        var ada=adapter_project_type(T_2!!)
            BaseActivity.projectTypes?.forEach {
                var T=Typess()
                T.Id= it.id?.toString()
                T.Name=it.name
                T.Selected=false
                T_2?.add(T)
            }



        if (isEdit)
        {
            Log.i("sdnsldndvjla", project?.projectTypeId.toString())
            BaseActivity.projectTypes?.forEachIndexed { index, projectTypes ->
                Log.i("sdnsldndvjlawwf", projectTypes.id.toString())
                if (project?.projectTypeId==projectTypes.id)
                {
                    Log.i("adcadv","A")
                    ada.Hold=index
                }else{
                    Log.i("adcadv","B")
                }
            }

        }else{
            BaseActivity.projectTypes?.forEachIndexed { index, projectTypes ->
                Log.i("sdnsldndvjlawwf", projectTypes.id.toString())
                if (jobForEdit?.projectTypeId==projectTypes.id)
                {
                    Log.i("adcadv","A")
                    ada.Hold=index
                }else{
                    Log.i("adcadv","B")
                }
            }
        }
        ada.Click(object :adapter_project_type.Data{
            override fun data(S: String,I:Int) {
                Log.i("sdsdffbgd",S)
                Type=S
                recyclerView.scrollToPosition(I)
            }
        })
        recyclerView.adapter=ada
        setDatePickers(
            SolarCalendar.currentShamsidate,
            SolarCalendar.currentShamsidate,
            SolarCalendar.currentShamsidate
        )
        if (ada.Hold==-1)
        {
            recyclerView.scrollToPosition(0)
        }




        if (activity is AddScheduledJobActivity) {
            if (jobForEdit?.id==null)
            {
                Log.i("dcdvwwww","Null")
                getmembers_scheduler("")
            }else{
                Log.i("dcdvwwww","Null22")
                getmembers_scheduler(jobForEdit?.id.toString())
            }

            Log.i("dddcwwqqaacvp","Hi")
            Log.i("dddcwwqqaacvp", jobForEdit?.mojryOrgLevelId.toString())
            Log.i("dddcwwqqaacvp","Hi")
            constraint_date_layout.visibility = View.GONE
            if (ada.Hold==-1)
            {
                recyclerView.scrollToPosition(0)
            }
            if (jobForEdit != null) {
                txt_title_title.visibility = View.VISIBLE
                txt_name_number_title.visibility = View.VISIBLE
                txt_name_date_title.visibility = View.VISIBLE
//                txt_etebar_title.visibility = View.VISIBLE
                txt_etebar_title.visibility = View.GONE
                txt_description_title.visibility = View.VISIBLE

                var constraintSet = ConstraintSet()
                constraintSet.clone(constraint_parent)
                constraintSet.connect(
                    edt_title.id,
                    ConstraintSet.TOP,
                    txt_title_title.id,
                    ConstraintSet.BOTTOM,
                    dp2px(16)
                )
                constraintSet.connect(
                    btn_name_date.id,
                    ConstraintSet.TOP,
                    txt_name_date_title.id,
                    ConstraintSet.BOTTOM,
                    dp2px(16)
                )
                constraintSet.connect(
                    edt_name_number.id,
                    ConstraintSet.TOP,
                    txt_name_number_title.id,
                    ConstraintSet.BOTTOM,
                    dp2px(16)
                )
                constraintSet.connect(
                    edt_etebar.id,
                    ConstraintSet.TOP,
                    txt_etebar_title.id,
                    ConstraintSet.BOTTOM,
                    dp2px(16)
                )
                constraintSet.connect(
                    edt_description.id,
                    ConstraintSet.TOP,
                    txt_description_title.id,
                    ConstraintSet.BOTTOM,
                    dp2px(16)
                )

                constraint_parent.setConstraintSet(constraintSet)

                edt_title.setText(jobForEdit?.title)
                isTitleValid = true
                setDatePickers(
                    null,
                    null,
                    jobForEdit?.letterDatefa
                )
                if (jobForEdit?.letterDatefa != null) {
                    txt_name_date.text = dName.toString() + " " + numToMonth(mName) + " " + yName
                    isValidNameDate = true
                }
                edt_name_number.setText(jobForEdit?.letterNumber)
//                when (jobForEdit?.projectType) {
//                    ProjectType.Job.value -> {
//                        selectJobTab()
//                    }
//                    ProjectType.Incident.value -> {
//                        selectAccidentTab()
//                    }
//                    ProjectType.Project.value -> {
//                        selectProjectTab()
//                    }
//                    ProjectType.Purpose.value -> {
//                        selectGoalTab()
//                    }
//                    ProjectType.Problem.value -> {
//                        selectProblemTab()
//                    }
//                    ProjectType.Offer.value -> {
//                        selectOfferTab()
//                    }
//                }
                when (jobForEdit?.priority) {
                    ProjectPriority.Normal.value -> {
                        selectNormalPriorityTab()
                    }
                    ProjectPriority.Important.value -> {
                        selectImportantPriorityTab()
                    }
                    ProjectPriority.VeryImportant.value -> {
                        selectVeryImportantTab()
                    }
                }
                //todo edt_etebar.setText()
                edt_description.setText(jobForEdit?.description)
                btn_submit.text = "ویرایش پروژه"

                isValidDate = true

            }
        }
        else{
            getProjectMembersAndGroups()
        }








        edt_title.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 1) {
                    TransitionManager.beginDelayedTransition(constraint_parent)

                    txt_title_title.visibility = View.VISIBLE

                    val constraintSet = ConstraintSet()
                    constraintSet.clone(constraint_parent)
                    constraintSet.connect(
                        edt_title.id,
                        ConstraintSet.TOP,
                        txt_title_title.id,
                        ConstraintSet.BOTTOM,
                        dp2px(16)
                    )
                    constraint_parent.setConstraintSet(constraintSet)
                } else if (p0?.length == 0) {
                    TransitionManager.beginDelayedTransition(constraint_parent)

                    txt_title_title.visibility = View.GONE

                    val constraintSet = ConstraintSet()
                    constraintSet.clone(constraint_parent)
                    constraintSet.connect(
                        edt_title.id,
                        ConstraintSet.TOP,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.TOP,
                        dp2px(24)
                    )
                    constraint_parent.setConstraintSet(constraintSet)
                }

                isTitleValid = p0.toString().isNotEmpty()

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })



        edt_name_number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 1) {
                    TransitionManager.beginDelayedTransition(constraint_parent)

                    txt_name_date_title.visibility = View.VISIBLE
                    txt_name_number_title.visibility = View.VISIBLE

                    val constraintSet = ConstraintSet()
                    constraintSet.clone(constraint_parent)
                    constraintSet.connect(
                        btn_name_date.id,
                        ConstraintSet.TOP,
                        txt_name_date_title.id,
                        ConstraintSet.BOTTOM,
                        dp2px(16)
                    )
                    constraintSet.connect(
                        edt_name_number.id,
                        ConstraintSet.TOP,
                        txt_name_number_title.id,
                        ConstraintSet.BOTTOM,
                        dp2px(16)
                    )
                    constraint_parent.setConstraintSet(constraintSet)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
        edt_etebar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 1) {
                    TransitionManager.beginDelayedTransition(constraint_parent)

//                    txt_etebar_title.visibility = View.VISIBLE
                    txt_etebar_title.visibility = View.GONE

                    val constraintSet = ConstraintSet()
                    constraintSet.clone(constraint_parent)
                    constraintSet.connect(
                        edt_etebar.id,
                        ConstraintSet.TOP,
                        txt_etebar_title.id,
                        ConstraintSet.BOTTOM,
                        dp2px(16)
                    )
                    constraint_parent.setConstraintSet(constraintSet)
                } else if (p0?.length == 0) {
                    TransitionManager.beginDelayedTransition(constraint_parent)

                    txt_etebar_title.visibility = View.GONE

                    val constraintSet = ConstraintSet()
                    constraintSet.clone(constraint_parent)
                    constraintSet.connect(
                        edt_etebar.id,
                        ConstraintSet.TOP,
                        lyt_mojri_spinner.id,
                        ConstraintSet.BOTTOM,
                        dp2px(24)
                    )
                    constraint_parent.setConstraintSet(constraintSet)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        btn_job_tab.setOnClickListener {
//            selectJobTab()
        }
        btn_accident_tab.setOnClickListener {
//            selectAccidentTab()
        }
        btn_project_tab.setOnClickListener {
//            selectProjectTab()
        }
        btn_goal_tab.setOnClickListener {
//            selectGoalTab()
        }
        btn_problem_tab.setOnClickListener {
//            selectProblemTab()
        }
        btn_offer_tab.setOnClickListener {
//            selectOfferTab()
        }
        btn_normal_priority_tab.setOnClickListener {
            selectNormalPriorityTab()
        }
        btn_important_tab.setOnClickListener {
            selectImportantPriorityTab()
        }




        btn_very_important_tab.setOnClickListener {
            selectVeryImportantTab()
        }

        btn_start_date.setOnClickListener {
            if (startDate.isNotEmpty()) {
                date = startDate
            }

            val datePickerDialog = DatePickerDialog.newInstance(
                { _, year, monthOfYear, dayOfMonth ->
                    y1 = year
                    m1 = monthOfYear + 1
                    d1 = dayOfMonth
                    startDate = "$year/${convertTime(m1)}/${convertTime(dayOfMonth)}"

                    txt_start_date.text = dayOfMonth.toString() + " " + numToMonth(m1) + " " + year

//                    TransitionManager.beginDelayedTransition(constraint_parent)
//                    txt_start_date_title.visibility = View.VISIBLE
//                    txt_end_date_title.visibility = View.VISIBLE
//
//                    val constraintSet = ConstraintSet()
//                    constraintSet.clone(constraint_parent)
//                    constraintSet.connect(
//                        btn_start_date.id,
//                        ConstraintSet.TOP,
//                        txt_start_date_title.id,
//                        ConstraintSet.BOTTOM,
//                        dp2px(16)
//                    )
//                    constraint_parent.setConstraintSet(constraintSet)

                    selectedStartDate = startDate
                    Log.i("svknsmvAA", selectedStartDate)
                    if (!selectedEndDate.isNullOrEmpty()) {
                        isValidDate =
                            !(y1 > y2 || y1 == y2 && m1 > m2 || y1 == y2 && m1 == m2 && d1 > d2)
                    }
                },
                Integer.parseInt(date.split("/")[0]),
                Integer.parseInt(date.split("/")[1]) - 1,
                Integer.parseInt(date.split("/")[2])
            )

            datePickerDialog.show(childFragmentManager, "Datepickerdialog")

        }

        btn_end_date.setOnClickListener {
            if (endDate.isNotEmpty()) {
                date = endDate
            }

            val datePickerDialog = DatePickerDialog.newInstance(
                { _, year, monthOfYear, dayOfMonth ->
                    y2 = year
                    m2 = monthOfYear + 1
                    d2 = dayOfMonth

                    val date_display =
                        dayOfMonth.toString() + " " + numToMonth(m2) + " " + year

                    endDate = "$year/${convertTime(m2)}/${convertTime(dayOfMonth)}"
                    txt_end_date.text = date_display

//                    TransitionManager.beginDelayedTransition(constraint_parent)
//
//
//                    txt_start_date_title.visibility = View.VISIBLE
//                    txt_end_date_title.visibility = View.VISIBLE
//
//                    val constraintSet = ConstraintSet()
//                    constraintSet.clone(constraint_parent)
//                    constraintSet.connect(
//                        btn_end_date.id,
//                        ConstraintSet.TOP,
//                        txt_end_date_title.id,
//                        ConstraintSet.BOTTOM,
//                        dp2px(16)
//                    )
//                    constraintSet.connect(
//                        btn_start_date.id,
//                        ConstraintSet.TOP,
//                        txt_start_date_title.id,
//                        ConstraintSet.BOTTOM,
//                        dp2px(16)
//                    )
//                    constraint_parent.setConstraintSet(constraintSet)

                    selectedEndDate = endDate

                    if (!selectedStartDate.isNullOrEmpty()) {
                        isValidDate =
                            !(y1 > y2 || y1 == y2 && m1 > m2 || y1 == y2 && m1 == m2 && d1 > d2)
                    }
                },
                Integer.parseInt(date.split("/")[0]),
                Integer.parseInt(date.split("/")[1]) - 1,
                Integer.parseInt(date.split("/")[2])
            )

            datePickerDialog.show(childFragmentManager, "Datepickerdialog")

        }

        btn_name_date.setOnClickListener {
            if (nameDate.isNotEmpty()) {
                date = nameDate
            }

            val datePickerDialog = DatePickerDialog.newInstance(
                { _, year, monthOfYear, dayOfMonth ->
                    yName = year
                    mName = monthOfYear + 1
                    dName = dayOfMonth

                    val date_display =
                        dayOfMonth.toString() + " " + numToMonth(mName) + " " + year

                    nameDate = "$year/${convertTime(mName)}/${convertTime(dayOfMonth)}"

                    TransitionManager.beginDelayedTransition(constraint_parent)

                    txt_name_date.text = date_display

                    txt_name_date_title.visibility = View.VISIBLE
                    txt_name_number_title.visibility = View.VISIBLE

                    val constraintSet = ConstraintSet()
                    constraintSet.clone(constraint_parent)
                    constraintSet.connect(
                        btn_name_date.id,
                        ConstraintSet.TOP,
                        txt_name_date_title.id,
                        ConstraintSet.BOTTOM,
                        dp2px(16)
                    )
                    constraintSet.connect(
                        edt_name_number.id,
                        ConstraintSet.TOP,
                        txt_name_number_title.id,
                        ConstraintSet.BOTTOM,
                        dp2px(16)
                    )
                    constraint_parent.setConstraintSet(constraintSet)

                    selectedNameDate = nameDate

                    isValidNameDate = true

                },
                Integer.parseInt(date.split("/")[0]),
                Integer.parseInt(date.split("/")[1]) - 1,
                Integer.parseInt(date.split("/")[2])
            )

            datePickerDialog.show(childFragmentManager, "Datepickerdialog")

        }


        btn_add_user_icon.setOnClickListener {
            openAddUsersBottomSheet()
        }

        txt_add_user.setOnClickListener {
            openAddUsersBottomSheet()
        }

        edt_description.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (edt_description.lineCount > 4) {
                    edt_description.setOnTouchListener { v, event ->

                        if (v.id == R.id.edt_description) {
                            v.parent.requestDisallowInterceptTouchEvent(true)
                            when (event.action and MotionEvent.ACTION_MASK) {
                                MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(
                                    false
                                )
                            }
                        }
                        false
                    }
                } else {
                    edt_description.setOnTouchListener { v, event ->
                        false
                    }
                }

                if (p0?.length == 1) {
                    TransitionManager.beginDelayedTransition(constraint_parent)

                    txt_description_title.visibility = View.VISIBLE

                    val constraintSet = ConstraintSet()
                    constraintSet.clone(constraint_parent)
                    constraintSet.connect(
                        edt_description.id,
                        ConstraintSet.TOP,
                        txt_description_title.id,
                        ConstraintSet.BOTTOM,
                        dp2px(16)
                    )
                    constraint_parent.setConstraintSet(constraintSet)
                } else if (p0?.length == 0) {
                    TransitionManager.beginDelayedTransition(constraint_parent)

                    txt_description_title.visibility = View.GONE

                    val constraintSet = ConstraintSet()
                    constraintSet.clone(constraint_parent)
                    constraintSet.connect(
                        edt_description.id,
                        ConstraintSet.TOP,
                        constraint_date_layout.id,
                        ConstraintSet.BOTTOM,
                        dp2px(24)
                    )
                    constraint_parent.setConstraintSet(constraintSet)
                }

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })


        rv_users.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, true)
        rv_users.addItemDecoration(RecyclerViewOverlapDecoration(dp2px(-16), 0, 0, 0))
        usersAdapter.setOnItemClickedListener(
            object :
                TinySelectedUsersRecyclerViewAdapter.OnItemClickedListener {
                override fun onItemClicked(userModel: UserModel) {
                    openAddUsersBottomSheet()
                }
            })
        rv_users.adapter = usersAdapter

        ObserveModels()

        btn_submit.setOnClickListener {
            if (Type.equals("-80"))
            {
                Toast.makeText(context,"نوع پروژه را انتخاب کنید",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (activity is AddProjectActivity) {
                if (!isTitleValid){
                    showAlertDialog(
                        R.drawable.ic_close_black_24dp,
                        "خطا",
                        "لطفا عنوان را وارد کنید",
                        object : MyAlertDialog.OnButtonClickedListener {
                            override fun OnButtonClicked() {

                            }
                        }

                    )
                    return@setOnClickListener
                }
                if (!isValidDate){
                    showAlertDialog(
                        R.drawable.ic_close_black_24dp,
                        "خطا",
                        "لطفا تاریخ ها را درست وارد نمایید ",
                        object : MyAlertDialog.OnButtonClickedListener {
                            override fun OnButtonClicked() {

                            }
                        }

                    )
                    return@setOnClickListener
                }

                    val members = mutableListOf<MemberDto>()
                    if (spinner_mojri.selectedIndex > 0)
                    {
                        members.add(
                            MemberDto(
                                mojries[spinner_mojri.selectedIndex - 1].orgLevelId,
                                AccessToProjectEnum.Full.value,
                                ProjectMemberType.Mojry.value
                            )
                        )
                    }else{
                        showAlertDialog(
                            R.drawable.ic_close_black_24dp,
                            "خطا",
                            "مجری را انتخاب کنید ",
                            object : MyAlertDialog.OnButtonClickedListener {
                                override fun OnButtonClicked() {

                                }
                            }

                        )
                        return@setOnClickListener

//                        members.add(
//                            MemberDto(
////                                project?.mojryOrgLevelId!!,
//                                -548,
//                                AccessToProjectEnum.Full.value,
//                                ProjectMemberType.Mojry.value
//                            )
//                        )

                    }

                    viewModel.selectedUsers.value!!.forEach {
                        members.add(
                            MemberDto(
                                it.orgLevelId,
                                AccessToProjectEnum.Full.value,
                                ProjectMemberType.Hamkar.value
                            )
                        )
                    }
                    if (isNetConnected()) {
                        showpDialog()
                        if (isEdit) {
                            EditProject(members)
                        } else {
                            CreateProject(members)
                        }
                    }

            }
            else if (activity is AddScheduledJobActivity) {
                var I:Int=Type.toInt();
                if (isTitleValid && isValidDate) {
                    val members = mutableListOf<MemberDto>()
                    if (spinner_mojri.selectedIndex > 0)
                        members.add(
                            MemberDto(
//                                viewModel.selectedPartners[spinner_mojri.selectedIndex - 1].orgLevelId,
                                mojries[spinner_mojri.selectedIndex - 1].orgLevelId,
                                AccessToProjectEnum.Full.value,
                                ProjectMemberType.Mojry.value
                            )
                        )
                    viewModel.selectedUsers.value!!.forEach {
                        members.add(
                            MemberDto(
                                it.orgLevelId,
                                AccessToProjectEnum.Full.value,
                                ProjectMemberType.Hamkar.value
                            )
                        )
                    }
                    (activity as AddScheduledJobActivity).RegisterOrEditScheduledProject(
                        if (jobForEdit != null) jobForEdit!!.id else "00000000-0000-0000-0000-000000000000",
                        edt_title.text.toString(),
                        edt_description.text.toString(),
                        if (isValidNameDate) selectedNameDate else null,
                        edt_name_number.text.toString(),
                        selectedPriority.value,
                        I,
                        if (edt_etebar.text.isNullOrEmpty()) 0 else edt_etebar.text.toString()
                            .toInt(),
                        members,
                        jobForEdit != null
                    )
                }
            }
        }

        if (project != null) {
            txt_title_title.visibility = View.VISIBLE
            txt_name_number_title.visibility = View.VISIBLE
            txt_name_date_title.visibility = View.VISIBLE
//            txt_etebar_title.visibility = View.VISIBLE
            txt_etebar_title.visibility = View.GONE
            txt_description_title.visibility = View.VISIBLE

            var constraintSet = ConstraintSet()
            constraintSet.clone(constraint_parent)

            constraintSet.connect(
                edt_title.id,
                ConstraintSet.TOP,
                txt_title_title.id,
                ConstraintSet.BOTTOM,
                dp2px(16)
            )
            constraintSet.connect(
                btn_name_date.id,
                ConstraintSet.TOP,
                txt_name_date_title.id,
                ConstraintSet.BOTTOM,
                dp2px(16)
            )
            constraintSet.connect(
                edt_name_number.id,
                ConstraintSet.TOP,
                txt_name_number_title.id,
                ConstraintSet.BOTTOM,
                dp2px(16)
            )
            constraintSet.connect(
                edt_etebar.id,
                ConstraintSet.TOP,
                txt_etebar_title.id,
                ConstraintSet.BOTTOM,
                dp2px(16)
            )
            constraintSet.connect(
                edt_description.id,
                ConstraintSet.TOP,
                txt_description_title.id,
                ConstraintSet.BOTTOM,
                dp2px(16)
            )

            constraint_parent.setConstraintSet(constraintSet)

            edt_title.setText(project?.title)
            edt_name_number.setText(project?.letterNumber)
//            when (project?.projectType) {
//                ProjectType.Job.value -> {
//                    selectJobTab()
//                }
//                ProjectType.Incident.value -> {
//                    selectAccidentTab()
//                }
//                ProjectType.Project.value -> {
//                    selectProjectTab()
//                }
//                ProjectType.Purpose.value -> {
//                    selectGoalTab()
//                }
//                ProjectType.Problem.value -> {
//                    selectProblemTab()
//                }
//                ProjectType.Offer.value -> {
//                    selectOfferTab()
//                }
//            }
            when (project?.priority) {
                ProjectPriority.Normal.value -> {
                    selectNormalPriorityTab()
                }
                ProjectPriority.Important.value -> {
                    selectImportantPriorityTab()
                }
                ProjectPriority.VeryImportant.value -> {
                    selectVeryImportantTab()
                }
            }
            //todo edt_etebar.setText()
            edt_description.setText(project?.description)

            btn_submit.text = "ویرایش پروژه"


            setDatePickers(
                project?.persianStartDate,
                project?.persianEndDate,
                project?.letterDatefa
            )


            if (!project?.persianStartDate.isNullOrEmpty())
                txt_start_date.text = d1.toString() + " " + numToMonth(m1) + " " + y1
            if (!project?.persianEndDate.isNullOrEmpty()) {
                txt_end_date.text = d2.toString() + " " + numToMonth(m2) + " " + y2
                selectedEndDate = "$y2/${convertTime(m2)}/${convertTime(d2)}"
            }
            if (!project?.letterDatefa.isNullOrEmpty()) {
                txt_name_date.text = dName.toString() + " " + numToMonth(mName) + " " + yName
                isValidNameDate = true
            }
            isValidDate = true


        }
    }

    private fun CreateProject(members: MutableList<MemberDto>) {
        Log.i("tag22",Type)
        var I:Int=Type.toInt();
        Log.i("tag22",I.toString())
        val req = App.getRetofitApi()?.registerProject(
            token = App.sharedPreferences?.getString(Constants.TOKEN, "null"),
            registerProjectModel = RegisterProjectModel(
                edt_title.text.toString(),
                edt_description.text.toString(),
                projectStatus.value,
                parentId,
                selectedStartDate,
                selectedEndDate,
                if (isValidNameDate) selectedNameDate else null,
                edt_name_number.text.toString(),
                selectedPriority.value,
                I,
                if (edt_etebar.text.isNullOrEmpty()) 0 else edt_etebar.text.toString().toInt(),
                meetingId,
                null, members
            )
        )
        req?.enqueue {
            onResponse = {
                if (isResponseValid(it) == 2) {
                    Toast.makeText(
                        activity,
                        "پروژه با موفقیت ایجاد شد.",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent()
                    activity?.setResult(AppCompatActivity.RESULT_OK, intent)
                    activity?.finish()
                } else if (isResponseValid(it) == 1) {
                    silentLogin(object : OnLoginCompleted {
                        override fun OnSuccess() {
                            CreateProject(members)
                        }
                    })
                }
                hidepDialog()
            }

            onFailure = {
                Log.i(TAG, it?.message!!)
                hidepDialog()
            }
        }
    }
    private fun EditProject(members: MutableList<MemberDto>) {
        Log.i("tag22",Type)

        var I:Int=Type.toInt();
        Log.i("tag22",I.toString())
        val req = App.getRetofitApi()?.editProject(
            token = App.sharedPreferences?.getString(Constants.TOKEN, "null"),
            editProjectModel = EditProjectModel(
                edt_title.text.toString(),
                edt_description.text.toString(),
                project?.id,
                selectedStartDate,
                if (isValidNameDate) selectedNameDate else null,
                edt_name_number.text.toString(),
                selectedEndDate,
                selectedPriority.value,
                I,
                if (edt_etebar.text.isNullOrEmpty()) 0 else edt_etebar.text.toString().toInt(),
                members
            )
        )
        req?.enqueue {
            onResponse = {
                Log.i("dvfbf", it.code().toString())
                if (isResponseValid(it) == 2) {
                    val myRespose = it.body()?.data
                    project = myRespose?.project
                    Toast.makeText(activity, "پروژه با موفقیت ویرایش شد.", Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent()
                    Log.i("dcdcddeewe",project?.projectTypeName)
                    intent.putExtra("project", project)
                    intent.putExtra("position", position)
                    activity?.setResult(AppCompatActivity.RESULT_OK, intent)
                    activity?.finish()
                } else
                    if (isResponseValid(it) == 1) {
                        Log.i("cdvdvvvvb", it.body().toString())
                    if (it.code()==500)
                    {
                        Log.i("sfvsfqv", "here")
                    }else{
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                Log.i(
                                    "Loginggg", App.sharedPreferences?.getString(
                                        Constants.TOKEN,
                                        "null"
                                    ) + "NIMA MORADI"
                                )
                                Log.i(
                                    "Loginggg", App.sharedPreferences?.getString(
                                        Constants.SECURITY_KEY,
                                        "null"
                                    ) + "ANDROID DEVELOPER"
                                )
                                EditProject(members)
                            }
                        })
                    }

                }
                hidepDialog()
            }

            onFailure = {
                Log.i(TAG, it?.message!!)
                hidepDialog()
            }
        }
    }

    private fun setDatePickers(
        startDatee: String?,
        endDatee: String?,
        letterDatefa: String?
    ) {
        if (!startDatee.isNullOrEmpty()) {
            y1 = Integer.parseInt(startDatee.split("/")[0])
            m1 = Integer.parseInt(startDatee.split("/")[1])
            d1 = Integer.parseInt(startDatee.split("/")[2])
            startDate = "$y1/${convertTime(m1)}/${convertTime(d1)}"
            selectedStartDate = "$y1/${convertTime(m1)}/${convertTime(d1)}"
            txt_start_date.text = d1.toString() + " " + numToMonth(m1) + " " + y1

        }

        if (!endDatee.isNullOrEmpty()) {
            y2 = Integer.parseInt(endDatee.split("/")[0])
            m2 = Integer.parseInt(endDatee.split("/")[1])
            d2 = Integer.parseInt(endDatee.split("/")[2])
            endDate = "$y2/${convertTime(m2)}/${convertTime(d2)}"
        }

        if (!letterDatefa.isNullOrEmpty()) {
            yName = Integer.parseInt(letterDatefa.split("/")[0])
            mName = Integer.parseInt(letterDatefa.split("/")[1])
            dName = Integer.parseInt(letterDatefa.split("/")[2])
            nameDate = "$yName/${convertTime(mName)}/${convertTime(dName)}"
            selectedNameDate = "$yName/${convertTime(mName)}/${convertTime(dName)}"
        }
    }

    private fun selectVeryImportantTab() {
        if (selectedPriority != ProjectPriority.VeryImportant) {
            selectedPriority = ProjectPriority.VeryImportant
            unSelectAllProjectPriorities()
            btn_very_important_tab.setBackgroundResource(R.drawable.selected_very_important_tab_background_shape)
            btn_very_important_tab.setTextColor(Color.WHITE)
        }
    }

    private fun selectImportantPriorityTab() {
        if (selectedPriority != ProjectPriority.Important) {
            selectedPriority = ProjectPriority.Important
            unSelectAllProjectPriorities()
            btn_important_tab.setBackgroundResource(R.drawable.selected_important_tab_background_shape)
            btn_important_tab.setTextColor(Color.WHITE)
        }
    }

    private fun selectNormalPriorityTab() {
        if (selectedPriority != ProjectPriority.Normal) {
            selectedPriority = ProjectPriority.Normal
            unSelectAllProjectPriorities()
            btn_normal_priority_tab.setBackgroundResource(R.drawable.selected_normal_tab_background_shape)
            btn_normal_priority_tab.setTextColor(Color.WHITE)
        }
    }

    private fun selectGoalTab() {
        if (selectedProjectType != ProjectType.Purpose) {
            selectedProjectType = ProjectType.Purpose
            unSelectAllProjectTypes()
            btn_goal_tab.setBackgroundResource(R.drawable.selected_tab_background_shape)
            btn_goal_tab.setTextColor(Color.WHITE)
        }
    }

    private fun selectProblemTab() {
        if (selectedProjectType != ProjectType.Problem) {
            selectedProjectType = ProjectType.Problem
            unSelectAllProjectTypes()
            btn_problem_tab.setBackgroundResource(R.drawable.selected_tab_background_shape)
            btn_problem_tab.setTextColor(Color.WHITE)
        }
    }

    private fun selectOfferTab() {
        if (selectedProjectType != ProjectType.Offer) {
            selectedProjectType = ProjectType.Offer
            unSelectAllProjectTypes()
            btn_offer_tab.setBackgroundResource(R.drawable.selected_tab_background_shape)
            btn_offer_tab.setTextColor(Color.WHITE)
        }
    }

    private fun selectProjectTab() {
        if (selectedProjectType != ProjectType.Project) {
            selectedProjectType = ProjectType.Project
            unSelectAllProjectTypes()
            btn_project_tab.setBackgroundResource(R.drawable.selected_tab_background_shape)
            btn_project_tab.setTextColor(Color.WHITE)
        }
    }

    private fun selectAccidentTab() {
        if (selectedProjectType != ProjectType.Incident) {
            selectedProjectType = ProjectType.Incident
            unSelectAllProjectTypes()
            btn_accident_tab.setBackgroundResource(R.drawable.selected_tab_background_shape)
            btn_accident_tab.setTextColor(Color.WHITE)
        }
    }

    private fun selectJobTab() {
        if (selectedProjectType != ProjectType.Job) {
            selectedProjectType = ProjectType.Job
            unSelectAllProjectTypes()
            btn_job_tab.setBackgroundResource(R.drawable.selected_tab_background_shape)
            btn_job_tab.setTextColor(Color.WHITE)
        }
    }


    fun ObserveModels() {
        viewModel.selectedUsers.observe(this,
            Observer<MutableList<UserModel>> { items ->
                TransitionManager.beginDelayedTransition(constraint_parent)
                usersAdapter.updateList(items)
            })


    }

    fun unSelectAllProjectTypes() {
        btn_accident_tab.setBackgroundResource(R.drawable.unselected_tab_background_shape)
        btn_goal_tab.setBackgroundResource(R.drawable.unselected_tab_background_shape)
        btn_project_tab.setBackgroundResource(R.drawable.unselected_tab_background_shape)
        btn_job_tab.setBackgroundResource(R.drawable.unselected_tab_background_shape)
        btn_problem_tab.setBackgroundResource(R.drawable.unselected_tab_background_shape)
        btn_offer_tab.setBackgroundResource(R.drawable.unselected_tab_background_shape)
        btn_accident_tab.setTextColor(Color.parseColor("#A9A9A9"))
        btn_goal_tab.setTextColor(Color.parseColor("#A9A9A9"))
        btn_project_tab.setTextColor(Color.parseColor("#A9A9A9"))
        btn_job_tab.setTextColor(Color.parseColor("#A9A9A9"))
        btn_problem_tab.setTextColor(Color.parseColor("#A9A9A9"))
        btn_offer_tab.setTextColor(Color.parseColor("#A9A9A9"))
    }

    fun unSelectAllProjectPriorities() {
        btn_normal_priority_tab.setBackgroundResource(R.drawable.unselected_tab_background_shape)
        btn_important_tab.setBackgroundResource(R.drawable.unselected_tab_background_shape)
        btn_very_important_tab.setBackgroundResource(R.drawable.unselected_tab_background_shape)
        btn_normal_priority_tab.setTextColor(Color.parseColor("#A9A9A9"))
        btn_important_tab.setTextColor(Color.parseColor("#A9A9A9"))
        btn_very_important_tab.setTextColor(Color.parseColor("#A9A9A9"))
    }

    fun openAddUsersBottomSheet() {
        viewModel.tempPersonels = mutableListOf()
        viewModel.selectedPersonel?.forEach {
            viewModel.tempPersonels?.add(
                UserModel(
                    it.fullName,
                    it.isChild,
                    it.orgLevelId,
                    it.orgLevelTitle,
                    it.type,
                    it.access,
                    it.profileImage,
                    it.selected
                )
            )
        }
        viewModel.tempPartners = mutableListOf()
        viewModel.selectedPartners.forEach {
            viewModel.tempPartners.add(
                UserModel(
                    it.fullName,
                    it.isChild,
                    it.orgLevelId,
                    it.orgLevelTitle,
                    it.type,
                    it.access,
                    it.profileImage,
                    it.selected
                )
            )
        }
        viewModel.tempGroups = mutableListOf()
        viewModel.selectedGroups.forEach {
            viewModel.tempGroups.add(
                GroupHeaderItem(
                    it.groupLevel,
                    it.groupId,
                    it.groupMembers,
                    it.groupTitle,
                    it.groupType
                )
            )
        }
        viewModel.tempSelectedUsers.value = mutableListOf()
        viewModel.selectedUsers.value!!.forEach {
            viewModel.tempSelectedUsers.value?.add(
                UserModel(
                    it.fullName,
                    it.isChild,
                    it.orgLevelId,
                    it.orgLevelTitle,
                    it.type,
                    it.access,
                    it.profileImage,
                    it.selected
                )
            )
        }

        val addUsersBottomSheet = AddUsersBottomSheet(token, this::class.java.simpleName)

        addUsersBottomSheet.setOnDismissListener(object :
            AddUsersBottomSheet.OnDismissListener {
            override fun Ondismiss(confirmed: Boolean) {
                if (!confirmed) {
                    viewModel.tempSelectedUsers.value =
                        mutableListOf()
                    viewModel.tempPartners = mutableListOf()
                    viewModel.tempPersonels = mutableListOf()
                    viewModel.tempGroups = mutableListOf()
                } else {
                    val temp = mutableListOf<UserModel>()
                    viewModel.tempSelectedUsers.value!!.forEach {
                        temp.add(
                            UserModel(
                                it.fullName,
                                it.isChild,
                                it.orgLevelId,
                                it.orgLevelTitle,
                                it.type,
                                it.access,
                                it.profileImage,
                                it.selected
                            )
                        )
                    }
                    viewModel.selectedUsers.value = temp

                    viewModel.selectedPartners = mutableListOf()
                    viewModel.tempPartners.forEach {
                        viewModel.selectedPartners.add(
                            UserModel(
                                it.fullName,
                                it.isChild,
                                it.orgLevelId,
                                it.orgLevelTitle,
                                it.type,
                                it.access,
                                it.profileImage,
                                it.selected
                            )
                        )
                    }

                    viewModel.selectedPersonel = mutableListOf()
                    viewModel.tempPersonels?.forEach {
                        viewModel.selectedPersonel?.add(
                            UserModel(
                                it.fullName,
                                it.isChild,
                                it.orgLevelId,
                                it.orgLevelTitle,
                                it.type,
                                it.access,
                                it.profileImage,
                                it.selected
                            )
                        )
                    }
                    viewModel.selectedGroups = mutableListOf()
                    viewModel.tempGroups.forEach {
                        viewModel.selectedGroups.add(
                            GroupHeaderItem(
                                it.groupLevel,
                                it.groupId,
                                it.groupMembers,
                                it.groupTitle,
                                it.groupType
                            )
                        )
                    }

                    viewModel.tempSelectedUsers.value =
                        mutableListOf()
                    viewModel.tempPersonels = mutableListOf()
                    viewModel.tempPartners = mutableListOf()
                    viewModel.tempGroups = mutableListOf()
                }
            }
        })

        addUsersBottomSheet.show(childFragmentManager, "")
    }

    fun getProjectMembersAndGroups() {
        if (isNetConnected()) {
            Log.i("Loginggg", App.sharedPreferences?.getString(Constants.TOKEN, "null"))
            showpDialog()
            var projectIdRequestBody =
                RequestBody.create(MediaType.parse("text/plain"), project?.id ?: "")
            val req = App.getRetofitApi()?.getProjectMemberAndGroups(
                App.sharedPreferences?.getString(
                    Constants.TOKEN,
                    "null"
                ), projectIdRequestBody
            )
            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {
                        val getProjectMembersAndGroupsResponseModel = it.body()?.data
                            Log.i(
                                "zcsw",
                                getProjectMembersAndGroupsResponseModel?.groups?.size.toString()
                            )
                            Log.i(
                                "zcsw2",
                                getProjectMembersAndGroupsResponseModel?.partners?.size.toString()
                            )
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
                                viewModel.selectedGroups.add(
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
                            val mojrisNames = mutableListOf<String>()
                            val me = UserModel(
                                userInfo_Copy?.fullName,
                                false,
                                userInfo_Copy?.orgLevelId ?: 0,
                                userInfo_Copy?.orgLevelTitle,
                                AccessToProjectEnum.Full.value,
                                0,
                                "",
                                false
                            )
                            Log.i("sknvsjbvs",me.toString())
                            mojrisNames.add("مجری را انتخاب کنید ...")
                            mojries.add(me)
                            mojrisNames.add(me.fullName ?: "")
                            getProjectMembersAndGroupsResponseModel.partners.forEach { userModel1 ->
                                mojrisNames.add(userModel1.fullName ?: "")
                                mojries.add(userModel1)
                            }

                            spinner_mojri.setItems(mojrisNames)
                            if (getProjectMembersAndGroupsResponseModel?.currentMembers != null) {

                                getProjectMembersAndGroupsResponseModel.currentMembers.forEachIndexed { index1, it ->
                                    if (it.type == ProjectMemberType.Mojry.value) {
                                        Log.i(TAG, it.fullName.toString())

                                        getProjectMembersAndGroupsResponseModel.partners.forEachIndexed { index2, userModel ->
                                            if (it.orgLevelId == userModel.orgLevelId) {
                                                Log.i(TAG, index2.toString())
                                                spinner_mojri.selectedIndex = index2 + 1
                                            }
                                        }
                                    }
                                }


                                val iterator =
                                    getProjectMembersAndGroupsResponseModel.currentMembers.iterator()
                                while (iterator.hasNext()) {
                                    val item = iterator.next()
                                    if (item.type == ProjectMemberType.Mojry.value) {
                                        val iterator2 =
                                            getProjectMembersAndGroupsResponseModel.partners.iterator()
                                        while (iterator2.hasNext()) {
                                            val item2 = iterator2.next()
                                            if (item.orgLevelId == item2.orgLevelId) {
                                                iterator2.remove()
                                            }
                                        }

                                        iterator.remove()
                                    }
                                }

                                viewModel.selectedUsers.value =
                                    getProjectMembersAndGroupsResponseModel.currentMembers.toMutableList()

                                getProjectMembersAndGroupsResponseModel.partners.forEach { userModel1 ->
                                    getProjectMembersAndGroupsResponseModel.currentMembers?.forEach { userModel2 ->
                                        if (userModel1.orgLevelId == userModel2.orgLevelId) {
                                            userModel1.selected = true
                                        }
                                    }
                                }





                            }

                        }
                        viewModel.selectedPartners =
                            getProjectMembersAndGroupsResponseModel?.partners ?: mutableListOf()
                        if (activity is AddProjectActivity) {
                            mojries.forEachIndexed { index, userModel ->
                                Log.i("adaadvvvvvv", "A")
                                if (project != null) {
                                    if (project?.mojryOrgLevelId == userModel.orgLevelId) {
                                        Log.i("adaadvvvvvv", "Ok")
                                        Log.i("adaadvvvvvv", "$index")
                                        spinner_mojri.selectedIndex = index + 1
                                    }
                                }

                            }

                        }
                        else if (activity is AddScheduledJobActivity)
                        {

                            Log.i("adaadvvvvvv2", "Ok333")
                            mojries.forEachIndexed {index, userModel ->
                                if (jobForEdit?.mojryOrgLevelId!=null)
                                {
                                    Log.i("adaadvvvvvv", "Ok")
                                    Log.i("adaadvvvvvv", "$index")
                                    if (jobForEdit?.mojryOrgLevelId==userModel.orgLevelId)
                                    {
                                        spinner_mojri.selectedIndex=index+1
                                    }
                                }

                            }
                        }
                    }
                    else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                Log.i("dvmsvkadeerr", "AS")
                                getProjectMembersAndGroups()
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


    fun getmembers_scheduler(Id: String)
    {

        showpDialog()
        var Id2 =
            RequestBody.create(MediaType.parse("text/plain"), Id)


        var Req=App.getRetofitApi()?.GetMemberScheduledProject(token,Id2)
        Req?.enqueue {
            onResponse={
             hidepDialog()
                if (isResponseValid(it) == 2) {
                    val getProjectMembersAndGroupsResponseModel = it.body()?.data
                    Log.i(
                        "zcsw",
                        getProjectMembersAndGroupsResponseModel?.groups?.size.toString()
                    )
                    Log.i(
                        "zcsw2",
                        getProjectMembersAndGroupsResponseModel?.partners?.size.toString()
                    )
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
                            viewModel.selectedGroups.add(
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
                        val mojrisNames = mutableListOf<String>()
                        val me = UserModel(
                            userInfo?.fullName,
                            false,
                            userInfo?.orgLevelId ?: 0,
                            userInfo?.orgLevelTitle,
                            AccessToProjectEnum.Full.value,
                            0,
                            "",
                            false
                        )
                        mojrisNames.add("مجری را انتخاب کنید ...")
                        mojries.add(me)
                        mojrisNames.add(me.fullName ?: "")
                        getProjectMembersAndGroupsResponseModel.partners.forEach { userModel1 ->
                            mojrisNames.add(userModel1.fullName ?: "")
                            mojries.add(userModel1)
                        }

                        spinner_mojri.setItems(mojrisNames)
                        if (getProjectMembersAndGroupsResponseModel?.currentMembers != null) {

                            getProjectMembersAndGroupsResponseModel.currentMembers.forEachIndexed { index1, it ->
                                if (it.type == ProjectMemberType.Mojry.value) {
                                    Log.i(TAG, it.fullName.toString())

                                    getProjectMembersAndGroupsResponseModel.partners.forEachIndexed { index2, userModel ->
                                        if (it.orgLevelId == userModel.orgLevelId) {
                                            Log.i(TAG, index2.toString())
                                            spinner_mojri.selectedIndex = index2 + 1
                                        }
                                    }
                                }
                            }


                            val iterator =
                                getProjectMembersAndGroupsResponseModel.currentMembers.iterator()
                            while (iterator.hasNext()) {
                                val item = iterator.next()
                                if (item.type == ProjectMemberType.Mojry.value) {
                                    val iterator2 =
                                        getProjectMembersAndGroupsResponseModel.partners.iterator()
                                    while (iterator2.hasNext()) {
                                        val item2 = iterator2.next()
                                        if (item.orgLevelId == item2.orgLevelId) {
                                            iterator2.remove()
                                        }
                                    }

                                    iterator.remove()
                                }
                            }

                            viewModel.selectedUsers.value =
                                getProjectMembersAndGroupsResponseModel.currentMembers.toMutableList()

                            getProjectMembersAndGroupsResponseModel.partners.forEach { userModel1 ->
                                getProjectMembersAndGroupsResponseModel.currentMembers?.forEach { userModel2 ->
                                    if (userModel1.orgLevelId == userModel2.orgLevelId) {
                                        userModel1.selected = true
                                    }
                                }
                            }





                        }
                    }
                    viewModel.selectedPartners =
                        getProjectMembersAndGroupsResponseModel?.partners ?: mutableListOf()
                    if (activity is AddProjectActivity) {
                        mojries.forEachIndexed { index, userModel ->
                            Log.i("adaadvvvvvv", "A")
                            if (project != null) {
                                if (project?.mojryOrgLevelId == userModel.orgLevelId) {
                                    Log.i("adaadvvvvvv", "Ok")
                                    Log.i("adaadvvvvvv", "$index")
                                    spinner_mojri.selectedIndex = index + 1
                                }
                            }

                        }

                    }
                    else if (activity is AddScheduledJobActivity)
                    {

                        Log.i("adaadvvvvvv2", "Ok333")
                        Log.i("adaadvvvvvv2", jobForEdit?.mojryOrgLevelId.toString())
                        mojries.forEachIndexed {index, userModel ->

                            if (jobForEdit?.mojryOrgLevelId!=null)
                            {
                                Log.i("adaadvvvvvv", "Ok")
                                Log.i("adaadvvvvvv", "$index")
                                if (jobForEdit?.mojryOrgLevelId==userModel.orgLevelId)
                                {
                                    spinner_mojri.selectedIndex=index+1
                                }
                            }

                        }
                    }
                }
                else if (isResponseValid(it)==1)
                {
                    silentLogin(object : OnLoginCompleted {
                        override fun OnSuccess() {
                            getmembers_scheduler(Id)
                        }
                    })
                }
            }
            onFailure={
                hidepDialog()
            }
        }
    }
}
