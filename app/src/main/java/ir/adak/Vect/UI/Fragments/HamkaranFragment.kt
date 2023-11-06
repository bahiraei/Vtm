package ir.adak.Vect.UI.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.adak.Vect.Adapters.UsersRecyclerViewAdapter
import ir.adak.Vect.Data.Models.UserModel
import ir.adak.Vect.Listeners.OnUserAddedAndDeleted

import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.AddMeetingActivity.AddMeetingActivity
import ir.adak.Vect.UI.Activities.AddProjectActivity.AddProjectActivity
import ir.adak.Vect.UI.Activities.AddScheduledJobActivity.AddScheduledJobActivity
import ir.adak.Vect.UI.Activities.AddUsersViewModel
import ir.adak.Vect.UI.Activities.MeetingDetailsActivity.MeetingDetailsActivity
import ir.adak.Vect.UI.Activities.ProjectDetailsActivity.ProjectDetailsActivity
import ir.adak.Vect.Utils.WrapContentLinearLayoutManager
import kotlinx.android.synthetic.main.fragment_hamkaran.*

/**
 * A simple [Fragment] subclass.
 */
class HamkaranFragment(var className: String) : BaseFragment() {


    lateinit var partnersAdapter: UsersRecyclerViewAdapter
    private lateinit var onUsersChange: OnUsersChange

    lateinit var viewModel: AddUsersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_hamkaran, container, false)
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

        partnersAdapter = UsersRecyclerViewAdapter(
            object : OnUserAddedAndDeleted {
                override fun OnUserAdded(userModel: UserModel) {
                    val temp = viewModel.tempSelectedUsers.value
                    temp?.add(userModel)
                    viewModel.tempSelectedUsers.value = temp
                    onUsersChange.OnUserAdded(userModel)
                }

                override fun OnUserDeleted(userModel: UserModel) {
                    val temp = viewModel.tempSelectedUsers.value
                    with(temp!!.iterator()) {
                        forEach {
                            if (it.orgLevelId == userModel.orgLevelId) {
                                remove()
                            }
                        }

                    }
                    viewModel.tempSelectedUsers.value = temp
                    onUsersChange.OnUserDeleted(userModel)

                }
            })

        rv_partners.layoutManager = WrapContentLinearLayoutManager(activity)
        rv_partners.adapter = partnersAdapter

//        viewModel.tempPartners.forEach { tempPartner ->
//            viewModel.selectedUsers.value?.forEach { userModel ->
//                if (tempPartner.orgLevelId == userModel.orgLevelId) {
//                    tempPartner.selected = true
//                }
//            }
//        }
//        val iterator2 =
//            viewModel.tempPartners.iterator()
//        while (iterator2.hasNext()) {
//            val item2 = iterator2.next()
//            if (item2.type == ProjectMemberType.Mojry.value) {
//                iterator2.remove()
//            }
//        }
        partnersAdapter.updateList(viewModel.tempPartners)
    }

    fun setOnUsersChangeListener(onUsersChange: OnUsersChange) {
        this.onUsersChange = onUsersChange
    }

    fun updateItem(position: Int) {
        partnersAdapter.notifyItemChanged(position)
    }

    interface OnUsersChange {
        fun OnUserAdded(userModel: UserModel)
        fun OnUserDeleted(userModel: UserModel)
    }

    fun updateAll(partners: MutableList<UserModel>?) {
        partnersAdapter.updateList(partners)
        partnersAdapter.notifyDataSetChanged()
    }
}
