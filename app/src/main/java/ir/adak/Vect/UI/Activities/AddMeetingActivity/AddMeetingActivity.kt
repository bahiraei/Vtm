package ir.adak.Vect.UI.Activities.AddMeetingActivity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog
import ir.adak.Vect.Adapters.TinySelectedUsersRecyclerViewAdapter
import ir.adak.Vect.App
import ir.adak.Vect.Data.Models.*
import ir.adak.Vect.Listeners.OnLoginCompleted
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.AddUsersViewModel
import ir.adak.Vect.UI.Activities.BaseActivity
import ir.adak.Vect.UI.Dialogs.AddUsersBottomSheet
import ir.adak.Vect.Utils.RecyclerViewOverlapDecoration
import ir.adak.Vect.Utils.SolarCalendar
import ir.adak.Vect.Utils.enqueue
import kotlinx.android.synthetic.main.activity_add_meeting.*
import kotlinx.android.synthetic.main.activity_add_meeting.constraint_parent
import kotlinx.android.synthetic.main.activity_add_meeting.txt_description_title
import kotlinx.android.synthetic.main.activity_add_meeting.txt_end_time_title
import kotlinx.android.synthetic.main.activity_add_meeting.txt_meeting_boss_title
import kotlinx.android.synthetic.main.activity_add_meeting.txt_meeting_dabir_title
import kotlinx.android.synthetic.main.activity_add_meeting.txt_meeting_date
import kotlinx.android.synthetic.main.activity_add_meeting.txt_meeting_date_title
import kotlinx.android.synthetic.main.activity_add_meeting.txt_meeting_location_title
import kotlinx.android.synthetic.main.activity_add_meeting.txt_start_time_title
import okhttp3.MediaType
import okhttp3.RequestBody
import java.text.DecimalFormat

class AddMeetingActivity : BaseActivity() {


    var f: DecimalFormat = DecimalFormat("00")

    var isTitleValid = false
    var isUsersValid = false


    private var y1: Int = 0
    private var m1: Int = 0
    private var d1: Int = 0

    var H1 = 0
    var M1 = 0
    var H2 = 0
    var M2 = 0

    private var heldDate: String = ""
    private var selectedHour: String = ""
    private var selectedMinute: String = ""
    private var selectedHeldDate: String = ""
    private var selectedStartTime: String = ""
    private var selectedEndTime: String = ""
    private var date: String = ""

    private val usersAdapter = TinySelectedUsersRecyclerViewAdapter(mutableListOf())
    var parentId: String? = null
    var meeting: Meeting? = null
    var position = -1
    var isEdit: Boolean = false
    lateinit var addUsersViewModel: AddUsersViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_meeting)
        addUsersViewModel = ViewModelProviders.of(this).get(AddUsersViewModel::class.java)
        parentId = intent.extras?.getString("parentId")
        meeting = intent.extras?.getParcelable("meeting")
        position = intent.extras?.getInt("position")?:-1
        isEdit = intent.extras?.getBoolean("isEdit") ?: false


        getMeetingMemberAndGroups()


        edt_title.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                isTitleValid = p0.toString().isNotEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
        setDatePickers(SolarCalendar.currentShamsidate)

        btn_meeting_date.setOnClickListener {
            if (heldDate.isNotEmpty()) {
                date = heldDate
            }

            val datePickerDialog = DatePickerDialog.newInstance(
                { _, year, monthOfYear, dayOfMonth ->
                    y1 = year
                    m1 = monthOfYear + 1
                    d1 = dayOfMonth


                    heldDate = "$year/${convertTime(m1)}/${convertTime(dayOfMonth)}"

                    txt_meeting_date.text =
                        dayOfMonth.toString() + " " + numToMonth(m1) + " " + year

                    selectedHeldDate = heldDate

                },
                Integer.parseInt(date.split("/")[0]),
                Integer.parseInt(date.split("/")[1]) - 1,
                Integer.parseInt(date.split("/")[2])
            )

            datePickerDialog.show(supportFragmentManager, "Datepickerdialog")

        }

        btn_start_time.setOnClickListener {
            val timePicker =
                TimePickerDialog.newInstance({ view, hourOfDay, minute ->
                    selectedHour = hourOfDay.toString()
                    selectedMinute = minute.toString()
                    H1 = hourOfDay
                    M1 = minute
                    selectedStartTime =
                        "${String.format("%02d", selectedHour.toInt())}:${String.format(
                            "%02d",
                            selectedMinute.toInt()
                        )}"

                    txt_meeting_start_time.text = selectedStartTime
                }, H1, M1, true)
            timePicker.show(supportFragmentManager, "")
        }
        btn_end_time.setOnClickListener {
            val timePicker =
                TimePickerDialog.newInstance({ view, hourOfDay, minute ->
                    selectedHour = hourOfDay.toString()
                    selectedMinute = minute.toString()
                    H2 = hourOfDay
                    M2 = minute
                    selectedEndTime =
                        "${String.format("%02d", selectedHour.toInt())}:${String.format(
                            "%02d",
                            selectedMinute.toInt()
                        )}"
                    txt_meeting_end_time.text = selectedEndTime
                }, H2, M2, true)
            timePicker.show(supportFragmentManager, "")
        }

//        edt_description.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(p0: Editable?) {
//                if (edt_description.lineCount > 4) {
//                    edt_description.setOnTouchListener { v, event ->
//
//                        if (v.id == R.id.edt_description) {
//                            v.parent.requestDisallowInterceptTouchEvent(true)
//                            when (event.action and MotionEvent.ACTION_MASK) {
//                                MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(
//                                    false
//                                )
//                            }
//                        }
//                        false
//                    }
//                } else {
//                    edt_description.setOnTouchListener { v, event ->
//                        false
//                    }
//                }
//
//                if (p0?.length == 1) {
//                    TransitionManager.beginDelayedTransition(constraint_parent)
//
//                    txt_description_title.visibility = View.VISIBLE
//
//                    val constraintSet = ConstraintSet()
//                    constraintSet.clone(constraint_parent)
//                    constraintSet.connect(
//                        edt_description.id,
//                        ConstraintSet.TOP,
//                        txt_description_title.id,
//                        ConstraintSet.BOTTOM,
//                        dp2px(16)
//                    )
//                    constraint_parent.setConstraintSet(constraintSet)
//                } else if (p0?.length == 0) {
//                    TransitionManager.beginDelayedTransition(constraint_parent)
//
//                    txt_description_title.visibility = View.GONE
//
//                    val constraintSet = ConstraintSet()
//                    constraintSet.clone(constraint_parent)
//                    constraintSet.connect(
//                        edt_description.id,
//                        ConstraintSet.TOP,
//                        edt_meeting_dabir.id,
//                        ConstraintSet.BOTTOM,
//                        dp2px(24)
//                    )
//                    constraint_parent.setConstraintSet(constraintSet)
//                }
//
//            }
//
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//        })


        btn_add_user_icon.setOnClickListener {
            openAddUsersBottomSheet()
        }

        txt_add_user.setOnClickListener {
            openAddUsersBottomSheet()
        }

        rv_users.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, true)
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



        if (meeting != null) {
            txt_title_title.visibility = View.VISIBLE
            txt_meeting_date_title.visibility = View.VISIBLE
            txt_start_time_title.visibility = View.VISIBLE
            txt_end_time_title.visibility = View.VISIBLE
            txt_meeting_location_title.visibility = View.VISIBLE
            txt_meeting_boss_title.visibility = View.VISIBLE
            txt_meeting_dabir_title.visibility = View.VISIBLE
            txt_description_title.visibility = View.VISIBLE

//            var constraintSet = ConstraintSet()
//            constraintSet.clone(constraint_parent)
//
//            constraintSet.connect(
//                edt_title.id,
//                ConstraintSet.TOP,
//                txt_title_title.id,
//                ConstraintSet.BOTTOM,
//                dp2px(16)
//            )
//            constraintSet.connect(
//                btn_meeting_date.id,
//                ConstraintSet.TOP,
//                txt_meeting_date_title.id,
//                ConstraintSet.BOTTOM,
//                dp2px(16)
//            )
//            constraintSet.connect(
//                btn_start_time.id,
//                ConstraintSet.TOP,
//                txt_start_time_title.id,
//                ConstraintSet.BOTTOM,
//                dp2px(16)
//            )
//            constraintSet.connect(
//                btn_end_time.id,
//                ConstraintSet.TOP,
//                txt_end_time_title.id,
//                ConstraintSet.BOTTOM,
//                dp2px(16)
//            )
//
//            constraintSet.connect(
//                edt_meeting_location.id,
//                ConstraintSet.TOP,
//                txt_meeting_location_title.id,
//                ConstraintSet.BOTTOM,
//                dp2px(16)
//            )
//            constraintSet.connect(
//                edt_meeting_boss.id,
//                ConstraintSet.TOP,
//                txt_meeting_boss_title.id,
//                ConstraintSet.BOTTOM,
//                dp2px(16)
//            )
//            constraintSet.connect(
//                edt_meeting_dabir.id,
//                ConstraintSet.TOP,
//                txt_meeting_dabir_title.id,
//                ConstraintSet.BOTTOM,
//                dp2px(16)
//            )
//            constraintSet.connect(
//                edt_description.id,
//                ConstraintSet.TOP,
//                txt_description_title.id,
//                ConstraintSet.BOTTOM,
//                dp2px(16)
//            )
//
//            constraint_parent.setConstraintSet(constraintSet)

            edt_title.setText(meeting?.title)
            edt_description.setText(meeting?.description)


            setDatePickers(
                meeting?.persianHeldDate
            )
            if (!meeting?.persianHeldDate.isNullOrEmpty())
                txt_meeting_date.text = d1.toString() + " " + numToMonth(m1) + " " + y1
            if (!meeting?.startTime.isNullOrEmpty())
                txt_meeting_start_time.text = selectedStartTime
            if (!meeting?.endTime.isNullOrEmpty())
                txt_meeting_end_time.text = selectedEndTime
            if (!meeting?.location.isNullOrEmpty())
                edt_meeting_location.setText(meeting?.location)
            if (!meeting?.headOf.isNullOrEmpty())
                edt_meeting_boss.setText(meeting?.headOf)
            if (!meeting?.secretary.isNullOrEmpty())
                edt_meeting_dabir.setText(meeting?.secretary)

            if (isEdit) {
                btn_submit.text = "ویرایش جلسه"
            }
        }

        btn_submit.setOnClickListener {
            if (isTitleValid && isUsersValid) {
                val members = mutableListOf<Int>()
                addUsersViewModel.selectedUsers.value!!.forEach {
                    members.add(it.orgLevelId)
                }
                registerMeeting(members)
            }
        }
    }

    fun registerMeeting(members: MutableList<Int>) {
        if (isNetConnected()) {
            if (!dialog.isShowing)
                showpDialog(this)
            val req = App.getRetofitApi()?.registerMeeting(
                token = token, registerMeetingRequestModel = RegisterMeetingRequestModel(
                    edt_description.text.toString(),
                    selectedEndTime,
                    edt_meeting_boss.text.toString(),
                    edt_meeting_location.text.toString(),
                    if (isEdit) meeting?.id else null,
                    0,
                    members,
                    selectedHeldDate,
                    parentId,
                    edt_meeting_dabir.text.toString(),
                    selectedStartTime,
                    edt_title.text.toString()
                )
            )
            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {
                        meeting = it.body()?.data?.meeting
                        val str = if (!isEdit) "ایجاد" else "ویرایش"
                        Toast.makeText(
                            this@AddMeetingActivity,
                            "جلسه با موفقیت $str شد.",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent()
                        intent.putExtra("position" , position)
                        intent.putExtra("meeting" , meeting)
                        setResult(RESULT_OK, intent)
                        finish()
                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                registerMeeting(members)
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

    }

    private fun setDatePickers(datee: String?) {
        if (datee != null) {
            y1 = Integer.parseInt(datee.split("/")[0])
            m1 = Integer.parseInt(datee.split("/")[1])
            d1 = Integer.parseInt(datee.split("/")[2])
        }
        heldDate = "$y1/${convertTime(m1)}/${convertTime(d1)}"
        selectedHeldDate = heldDate


        if (meeting?.startTime != null && meeting?.startTime?.isEmpty() == false && meeting
                ?.startTime?.length == 5 /* 00:00 Format*/) {
            H1 = Integer.parseInt(meeting?.startTime!!.split(":")[0])
            M1 = Integer.parseInt(meeting?.startTime!!.split(":")[1])
            selectedStartTime = f.format(Integer.valueOf(H1)) + ":" + f.format(Integer.valueOf(M1))
        }
        if (meeting?.endTime != null && meeting?.endTime?.isEmpty() == false && meeting
                ?.endTime?.length == 5 /* 00:00 Format*/) {
            H2 = Integer.parseInt(meeting?.endTime!!.split(":")[0])
            M2 = Integer.parseInt(meeting?.endTime!!.split(":")[1])
            selectedEndTime = f.format(Integer.valueOf(H2)) + ":" + f.format(Integer.valueOf(M2))
        }
    }

    fun ObserveModels() {
        addUsersViewModel.selectedUsers.observe(this,
            Observer<MutableList<UserModel>> { items ->
                TransitionManager.beginDelayedTransition(constraint_parent)
                usersAdapter.updateList(items)
                isUsersValid = !items.isNullOrEmpty()
            })


    }

    fun openAddUsersBottomSheet() {

        addUsersViewModel.tempPersonels = mutableListOf()
        addUsersViewModel.selectedPersonel?.forEach {
            addUsersViewModel.tempPersonels?.add(
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
        addUsersViewModel.tempPartners = mutableListOf()
        addUsersViewModel.selectedPartners.forEach {
            addUsersViewModel.tempPartners.add(
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
        addUsersViewModel.tempGroups = mutableListOf()
        addUsersViewModel.selectedGroups.forEach {
            addUsersViewModel.tempGroups.add(
                GroupHeaderItem(
                    it.groupLevel,
                    it.groupId,
                    it.groupMembers,
                    it.groupTitle,
                    it.groupType
                )
            )
        }
        addUsersViewModel.tempSelectedUsers.value = mutableListOf()
        addUsersViewModel.selectedUsers.value!!.forEach {
            addUsersViewModel.tempSelectedUsers.value?.add(
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
                    addUsersViewModel.tempSelectedUsers.value = mutableListOf()
                    addUsersViewModel.tempPartners = mutableListOf()
                    addUsersViewModel.tempPersonels = mutableListOf()
                    addUsersViewModel.tempGroups = mutableListOf()
                } else {
                    val temp = mutableListOf<UserModel>()
                    addUsersViewModel.tempSelectedUsers.value!!.forEach {
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
                    addUsersViewModel.selectedUsers.value = temp

                    addUsersViewModel.selectedPartners = mutableListOf()
                    addUsersViewModel.tempPartners.forEach {
                        addUsersViewModel.selectedPartners.add(
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

                    addUsersViewModel.selectedPersonel = mutableListOf()
                    addUsersViewModel.tempPersonels?.forEach {
                        addUsersViewModel.selectedPersonel?.add(
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
                    addUsersViewModel.selectedGroups = mutableListOf()
                    addUsersViewModel.tempGroups.forEach {
                        addUsersViewModel.selectedGroups.add(
                            GroupHeaderItem(
                                it.groupLevel,
                                it.groupId,
                                it.groupMembers,
                                it.groupTitle,
                                it.groupType
                            )
                        )
                    }

                    addUsersViewModel.tempSelectedUsers.value = mutableListOf()
                    addUsersViewModel.tempPersonels = mutableListOf()
                    addUsersViewModel.tempPartners = mutableListOf()
                    addUsersViewModel.tempGroups = mutableListOf()
                }
            }
        })

        addUsersBottomSheet.show(supportFragmentManager, "")

    }


    fun getMeetingMemberAndGroups() {
        if (isNetConnected()) {
            if (!dialog.isShowing)
                showpDialog(this)

            var meetingIdIdRequestBody =
                RequestBody.create(MediaType.parse("text/plain"), meeting?.id ?: "")
            val req = App.getRetofitApi()?.getMeetingMemberAndGroups(token, meetingIdIdRequestBody)
            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {
                        val getProjectMembersAndGroupsResponseModel = it.body()?.data
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
                                        groupMembers = members,
                                        groupTitle = it.title,
                                        groupType = it.type
                                    )
                                )
                            }
                        }
                        if (getProjectMembersAndGroupsResponseModel?.partners != null) {
                            addUsersViewModel.selectedPartners =
                                getProjectMembersAndGroupsResponseModel.partners
                        }
                        if (getProjectMembersAndGroupsResponseModel?.currentMembers != null) {
                            addUsersViewModel.selectedUsers.value =
                                getProjectMembersAndGroupsResponseModel.currentMembers.toMutableList()
                        }
                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                getMeetingMemberAndGroups()
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
