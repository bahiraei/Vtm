package ir.adak.Vect.UI.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
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
import kotlinx.android.synthetic.main.fragment_personel.*

/**
 * A simple [Fragment] subclass.
 */
class PersonelFragment(var className: String) : Fragment() {

    lateinit var personelsAdapter: UsersRecyclerViewAdapter
    private lateinit var onUsersChange: OnUsersChange
    lateinit var viewModel: AddUsersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_personel, container, false)
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


        personelsAdapter = UsersRecyclerViewAdapter(
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

        rv_all_users.layoutManager = LinearLayoutManager(activity)
        rv_all_users.adapter = personelsAdapter
    }

    fun setOnUsersChangeListener(onUsersChange: OnUsersChange) {
        this.onUsersChange = onUsersChange
    }

    fun updateItem(position: Int) {
        personelsAdapter.notifyItemChanged(position)
    }

    fun updateAll(personel: MutableList<UserModel>?) {
        personelsAdapter.updateList(personel)
        personelsAdapter.notifyDataSetChanged()
    }

    interface OnUsersChange {
        fun OnUserAdded(userModel: UserModel)
        fun OnUserDeleted(userModel: UserModel)
    }
}
