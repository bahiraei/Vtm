package ir.adak.Vect.UI.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import ir.adak.Vect.Data.Models.GroupItem
import ir.adak.Vect.Data.Models.UserModel

import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.AddMeetingActivity.AddMeetingActivity
import ir.adak.Vect.UI.Activities.AddProjectActivity.AddProjectActivity
import ir.adak.Vect.UI.Activities.AddScheduledJobActivity.AddScheduledJobActivity
import ir.adak.Vect.UI.Activities.AddUsersViewModel
import ir.adak.Vect.UI.Activities.MeetingDetailsActivity.MeetingDetailsActivity
import ir.adak.Vect.UI.Activities.ProjectDetailsActivity.ProjectDetailsActivity
import kotlinx.android.synthetic.main.fragment_groups.*

/**
 * A simple [Fragment] subclass.
 */
class GroupsFragment(var className: String) : BaseFragment() {

    lateinit var groupsAdapter: GroupAdapter<ViewHolder>
    private lateinit var onUsersChange: OnUsersChange
    lateinit var viewModel: AddUsersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_groups, container, false)
        view.rotationY = 180f
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity is AddProjectActivity) {
            viewModel = (activity as AddProjectActivity).viewModel
        } else if (activity is ProjectDetailsActivity) {
            viewModel = (activity as ProjectDetailsActivity).addUsersViewModel
        } else if (activity is MeetingDetailsActivity) {
            viewModel = (activity as MeetingDetailsActivity).addUsersViewModel
        } else if (activity is AddMeetingActivity) {
            viewModel = (activity as AddMeetingActivity).addUsersViewModel
        } else if (activity is AddScheduledJobActivity)
            viewModel = (activity as AddScheduledJobActivity).viewModel

        initGroupsAdapter()
    }


    fun setOnUsersChangeListener(onUsersChange: OnUsersChange) {
        this.onUsersChange = onUsersChange
    }

    //
    fun updateItem(groupItem: GroupItem) {
        groupsAdapter.notifyItemChanged(groupsAdapter.getAdapterPosition(groupItem))
    }

    //
    fun notifyDatasetChanged() {
//    groupsAdapter.notifyDataSetChanged()
    }

    private fun initGroupsAdapter() {

        groupsAdapter = GroupAdapter()
        viewModel.tempGroups.forEachIndexed { index, groupHeaderItem ->
            ExpandableGroup(groupHeaderItem, false).apply {
//                groupHeaderItem.groupMembers.forEach { groupItem ->
//                    viewModel.selectedUsers.value?.forEach { userModel ->
//                        if (groupItem.orgLevelId == userModel.orgLevelId) {
//                            groupItem.selected = true
//                        }
//                    }
//                }

                add(Section(groupHeaderItem.groupMembers))
                groupsAdapter.add(this)
            }
        }

        groupsAdapter.setOnItemClickListener { item, view ->
            if ((item as GroupItem).selected) {
                val temp = viewModel.tempSelectedUsers.value
                with(temp!!.iterator()) {
                    forEach {
                        if (it.orgLevelId == item.orgLevelId) {
                            remove()
                        }
                    }

                }
                viewModel.tempSelectedUsers.value = temp

                item.selected = false
                groupsAdapter.notifyItemChanged(groupsAdapter.getAdapterPosition(item))
                onUsersChange.OnUserDeleted(
                    UserModel(
                        fullName = item.fullName,
                        isChild = item.isChild,
                        orgLevelId = item.orgLevelId,
                        orgLevelTitle = item.orgLevelTitle,
                        type = item.type,
                        access = item.access,
                        profileImage = item.profileImage,
                        selected = item.selected
                    )
                )
                viewModel.tempGroups.forEach {
                    it.groupMembers.forEach { groupItem ->
                        if (groupItem.orgLevelId == item.orgLevelId) {
                            groupItem.selected = false
                            groupsAdapter.notifyItemChanged(
                                groupsAdapter.getAdapterPosition(
                                    groupItem
                                )
                            )
                        }
                    }
                }
            } else {
                val temp = viewModel.tempSelectedUsers.value
                val userModel = UserModel(
                    fullName = item.fullName ?: "",
                    isChild = item.isChild,
                    orgLevelTitle = item.orgLevelTitle ?: "",
                    orgLevelId = item.orgLevelId,
                    profileImage = item.profileImage ?: "",
                    type = item.type,
                    access = item.access,
                    selected = item.selected
                )
                temp?.add(userModel)
                viewModel.tempSelectedUsers.value = temp

                item.selected = true
                groupsAdapter.notifyItemChanged(groupsAdapter.getAdapterPosition(item))
                onUsersChange.OnUserAdded(userModel)

                viewModel.tempGroups.forEach {
                    it.groupMembers.forEach { groupItem ->
                        if (groupItem.orgLevelId == item.orgLevelId) {
                            groupItem.selected = true
                            groupsAdapter.notifyItemChanged(
                                groupsAdapter.getAdapterPosition(
                                    groupItem
                                )
                            )
                        }
                    }
                }
            }
        }

        //set up the layout manager and set the adapter
        rv_groups.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = groupsAdapter
        }

    }

    interface OnUsersChange {
        fun OnUserAdded(userModel: UserModel)
        fun OnUserDeleted(userModel: UserModel)
    }

}
