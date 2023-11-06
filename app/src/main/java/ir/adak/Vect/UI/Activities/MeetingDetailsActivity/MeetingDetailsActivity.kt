package ir.adak.Vect.UI.Activities.MeetingDetailsActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import ir.adak.Vect.Adapters.MyViewpagerAdapter
import ir.adak.Vect.App
import ir.adak.Vect.Data.Models.*
import ir.adak.Vect.Listeners.OnLoginCompleted
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.AddUsersViewModel
import ir.adak.Vect.UI.Activities.BaseActivity
import ir.adak.Vect.UI.Fragments.UsersFragment
import ir.adak.Vect.Utils.Constants
import ir.adak.Vect.Utils.enqueue
import kotlinx.android.synthetic.main.activity_meeting_details.*
import kotlinx.android.synthetic.main.activity_meeting_details.txt_meeting_title
import kotlinx.android.synthetic.main.activity_meeting_details.txt_users_count
import okhttp3.MediaType
import okhttp3.RequestBody
import org.modelmapper.TypeToken

class MeetingDetailsActivity : BaseActivity() {

    var meeting: Meeting? = null

    lateinit var addUsersViewModel: AddUsersViewModel
    var members: MutableList<UserModel>? = mutableListOf()
    val usersFragment = UsersFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_details)

        addUsersViewModel = ViewModelProviders.of(this).get(AddUsersViewModel::class.java)
        meeting = intent.extras?.getParcelable("meeting")
        members = Gson().fromJson(
            intent.extras?.getString("meetingMembers"),
            object : TypeToken<MutableList<UserModel>>() {}.type
        )





        if (!userInfo?.profileImage.isNullOrEmpty())
        {
//            Picasso.get().load(Constants.AVATAR_BASE_URL+ meeting.).placeholder(R.drawable.img_avatar).into(img_avatar)
        }

        PrepareMeetingDetails()
    }

    fun PrepareMeetingDetails() {

        txt_creator_name.text = meeting?.personelFullName
        txt_meeting_title.text = meeting?.title
        txt_users_count.text = "${meeting?.memberCount} نفر"
        txt_description.text = meeting?.description
        txt_meeting_date.text = meeting?.persianHeldDate
        txt_start_time.text = meeting?.startTime
        txt_end_time.text = meeting?.endTime
        txt_meeting_boss.text = meeting?.headOf
        txt_meeting_dabir.text = meeting?.secretary
        txt_meeting_location.text = meeting?.location


        addUsersViewModel.selectedUsers.observe(this,
            Observer<MutableList<UserModel>> { items ->
                usersFragment.usersAdapter?.updateList(items)
            })

        setupViewPager(viewPager, members)

        getMeetingMemberAndGroups()

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

    private fun setupViewPager(
        viewPager: ViewPager,
        members: MutableList<UserModel>?
    ) {
        val adapter = MyViewpagerAdapter(
            supportFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        addUsersViewModel.selectedUsers.value = members

        val bundle = Bundle()
        bundle.putParcelable("meeting", meeting)
        usersFragment.arguments = bundle
        adapter.addFragment(usersFragment, "اعضا")
        viewPager.adapter = adapter
        viewPager.currentItem = 1
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.UPDATE_MEETING_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            PrepareMeetingDetails()
        }
    }
}
