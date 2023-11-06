package ir.adak.Vect.UI.Fragments


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import ir.adak.Vect.Adapters.UsersRecyclerViewAdapter
import ir.adak.Vect.App
import ir.adak.Vect.Data.Enums.ProjectMemberType
import ir.adak.Vect.Data.Models.*
import ir.adak.Vect.Listeners.OnLoginCompleted

import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.AddMeetingActivity.AddMeetingActivity
import ir.adak.Vect.UI.Activities.AddUsersViewModel
import ir.adak.Vect.UI.Activities.MeetingDetailsActivity.MeetingDetailsActivity
import ir.adak.Vect.UI.Activities.PeigiriActivity.PeigiriActivity
import ir.adak.Vect.UI.Activities.ProjectDetailsActivity.ProjectDetailsActivity
import ir.adak.Vect.UI.Dialogs.AddUsersBottomSheet
import ir.adak.Vect.Utils.Constants
import ir.adak.Vect.Utils.WrapContentLinearLayoutManager
import ir.adak.Vect.Utils.enqueue
import kotlinx.android.synthetic.main.fragment_users.*

/**
 * A simple [Fragment] subclass.
 */
class UsersFragment : BaseFragment() {

    var usersAdapter: UsersRecyclerViewAdapter? = null
    lateinit var viewModel: AddUsersViewModel
    var project: Project? = null
    var meeting: Meeting? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_users, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        project = arguments?.getParcelable("project")
        meeting = arguments?.getParcelable("meeting")

        if (activity is ProjectDetailsActivity) {
            viewModel = (activity as ProjectDetailsActivity).addUsersViewModel

            if (project?.orgLevelId == userInfo?.orgLevelId) {
                txt_manage_users.text = "مدیریت اعضا"
            } else {
                txt_manage_users.text = "افزودن اعضا"
            }

        } else if (activity is MeetingDetailsActivity) {
            viewModel = (activity as MeetingDetailsActivity).addUsersViewModel
            if (meeting?.orgLevelId == userInfo?.orgLevelId) {
                btn_manage_users.visibility = View.VISIBLE
                txt_manage_users.text = "مدیریت اعضا"
                if (meeting?.isHeld == true){
                    btn_manage_users.visibility = View.GONE
                }
            } else {
                btn_manage_users.visibility = View.GONE
            }
        }

        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager
            .defaultDisplay
            .getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val params = rv_users.layoutParams
        params.height = height - dp2px(80)
        rv_users.layoutParams = params


        usersAdapter = UsersRecyclerViewAdapter(null)
        rv_users.apply {
            layoutManager = WrapContentLinearLayoutManager(activity)
            adapter = usersAdapter
        }
        viewModel.selectedUsers.observe(this, Observer<MutableList<UserModel>> { items ->
            usersAdapter?.updateList(items)
        })

        usersAdapter?.updateList(viewModel.selectedUsers.value)

        btn_manage_users.setOnClickListener {
            if (activity is ProjectDetailsActivity)
                openAddUsersBottomSheet()
            else if (activity is MeetingDetailsActivity) {
                val intent = Intent(activity, AddMeetingActivity::class.java)
                intent.putExtra(
                    "meeting",
                    meeting
                )
                intent.putExtra("isEdit", true)
                startActivityForResult(intent, Constants.UPDATE_MEETING_REQUEST_CODE)
                activity?.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
            }
        }

    }


    fun openAddUsersBottomSheet() {

        viewModel.tempPersonels = mutableListOf()
        viewModel.tempPartners = mutableListOf()
        viewModel.tempGroups = mutableListOf()
        viewModel.tempSelectedUsers.value = mutableListOf()


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
                    if (project?.orgLevelId == userInfo?.orgLevelId) it.selected else false

                )
            )
        }

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
                    if (project?.orgLevelId == userInfo?.orgLevelId) it.selected else false
                )
            )
        }
        viewModel.selectedGroups.forEach {
            viewModel.tempGroups.add(
                GroupHeaderItem(
                    it.groupLevel,
                    it.groupId,
                    it.groupMembers.apply {
                        this.forEach { groupItem ->
                            if (project?.orgLevelId != userInfo?.orgLevelId) groupItem.selected =
                                false

                        }
                    },
                    it.groupTitle,
                    it.groupType
                )
            )
        }

        if (userInfo?.orgLevelId == project?.orgLevelId) {
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
        }

        var addUsersBottomSheet: AddUsersBottomSheet? = null

        addUsersBottomSheet =
            AddUsersBottomSheet(token, ProjectDetailsActivity::class.java.simpleName)
        addUsersBottomSheet.setOnDismissListener(object :
            AddUsersBottomSheet.OnDismissListener {
            override fun Ondismiss(confirmed: Boolean) {
                if (!confirmed) {
                    viewModel.tempSelectedUsers.value = mutableListOf()
                    viewModel.tempPartners = mutableListOf()
                    viewModel.tempPersonels = mutableListOf()
                    viewModel.tempGroups = mutableListOf()
                } else {
                    val members = mutableListOf<MemberDto>()
                    viewModel.tempSelectedUsers.value!!.forEach {
                        members.add(MemberDto(it.orgLevelId, it.access,ProjectMemberType.Hamkar.value))
                    }

                    if (userInfo?.orgLevelId == project?.orgLevelId) {
                        ProjectChangeMember(members)

                    } else {
                        if (members.isNotEmpty()) {
                            ProjectAddMember(members)
                        }
                    }
                }
            }
        })

        addUsersBottomSheet?.show(childFragmentManager, "")
    }

    private fun ProjectAddMember(members: MutableList<MemberDto>) {
        if (dialog?.isShowing!!)
            showpDialog()
        val req = App.getRetofitApi()?.projectAddMember(
            App.sharedPreferences?.getString(Constants.TOKEN,"NULL"),
            MemberChangedDto(members, project?.id ?: "")
        )

        req?.enqueue {
            onResponse = {

                if (isResponseValid(it) == 2) {

                    val count = it.body()?.data ?: 0
                    (activity as ProjectDetailsActivity).getProjectDetails()

                    PeigiriActivity.needToRefresh = true
                    PeigiriActivity.count = count
                }
                else if (isResponseValid(it) == 1) {
                    silentLogin(object : OnLoginCompleted {
                        override fun OnSuccess() {
                            ProjectAddMember(members)
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

    private fun ProjectChangeMember(members: MutableList<MemberDto>) {



        if (dialog?.isShowing!!)
            showpDialog()
        val req = App.getRetofitApi()?.projectChangeMember(
            token,
            MemberChangedDto(members, project?.id ?: "")
        )
        req?.enqueue {
            onResponse = {
                Log.i("sdvsvwqq33", it.code().toString())
                if (isResponseValid(it) == 2) {
                    val count = it.body()?.data
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
                                false
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

                    viewModel.tempSelectedUsers.value = mutableListOf()
                    viewModel.tempPersonels = mutableListOf()
                    viewModel.tempPartners = mutableListOf()
                    viewModel.tempGroups = mutableListOf()

                    (activity as ProjectDetailsActivity).getProjectDetails()

                    PeigiriActivity.needToRefresh = true
                    PeigiriActivity.count = count ?: 0
                } else if (isResponseValid(it) == 1) {
                    silentLogin(object : OnLoginCompleted {
                        override fun OnSuccess() {
                            ProjectChangeMember(members)
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
