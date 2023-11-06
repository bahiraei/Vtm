package ir.adak.Vect.UI.Dialogs

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.FragmentStatePagerAdapter
import ir.adak.Vect.Adapters.MyViewpagerAdapter
import ir.adak.Vect.R
import ir.adak.Vect.UI.Fragments.*
import kotlinx.android.synthetic.main.add_users_bottom_sheet.*
import kotlinx.android.synthetic.main.add_users_bottom_sheet.guideline_end
import kotlinx.android.synthetic.main.add_users_bottom_sheet.guideline_start
import kotlinx.android.synthetic.main.add_users_bottom_sheet.view_pager
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ir.adak.Vect.Adapters.SelectedUsersRecyclerViewAdapter
import ir.adak.Vect.App
import ir.adak.Vect.Data.Enums.ProjectMemberType
import ir.adak.Vect.Data.Models.UserModel
import ir.adak.Vect.Listeners.OnLoginCompleted
import ir.adak.Vect.UI.Activities.AddMeetingActivity.AddMeetingActivity
import ir.adak.Vect.UI.Activities.AddProjectActivity.AddProjectActivity
import ir.adak.Vect.UI.Activities.AddScheduledJobActivity.AddScheduledJobActivity
import ir.adak.Vect.UI.Activities.AddUsersViewModel
import ir.adak.Vect.UI.Activities.BaseActivity
import ir.adak.Vect.UI.Activities.MeetingDetailsActivity.MeetingDetailsActivity
import ir.adak.Vect.UI.Activities.ProjectDetailsActivity.ProjectDetailsActivity
import ir.adak.Vect.Utils.Constants
import ir.adak.Vect.Utils.WrapContentLinearLayoutManager
import ir.adak.Vect.Utils.enqueue
import okhttp3.MediaType
import okhttp3.RequestBody


class AddUsersBottomSheet(var token: String?, var className: String) : BaseDialogFragment() {

    enum class SelectedTab {
        HAMKARAN,
        GROUPS,
        PERSONEL
    }

    private lateinit var viewModel: AddUsersViewModel
    private val hamkaranFragment = HamkaranFragment(className)
    private val groupsFragment = GroupsFragment(className)
    private val personelFragment = PersonelFragment(className)
    lateinit var selectedUsersAdapter: SelectedUsersRecyclerViewAdapter
    var selectedTab = SelectedTab.HAMKARAN
    private val tabSelectionAnimationDuration = 300L
    lateinit var start_valueAnimator: ValueAnimator
    lateinit var end_valueAnimator: ValueAnimator
    lateinit var onDismiss: OnDismissListener
    val color_valueAnimator = ValueAnimator.ofFloat(0f, 1f)
    var confirmed = false




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = View.inflate(context, R.layout.add_users_bottom_sheet, null)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

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
        selectedUsersAdapter = SelectedUsersRecyclerViewAdapter(mutableListOf())
        val iterator2 =
            viewModel.tempSelectedUsers.value?.iterator()
        while (iterator2?.hasNext() == true) {
            val item2 = iterator2.next()
            if (item2.type == ProjectMemberType.Mojry.value) {
                iterator2.remove()
            }
        }

        if (viewModel.tempSelectedUsers.value?.isNullOrEmpty()!!) {
            rv_selected_users.visibility = View.GONE
        } else {
            rv_selected_users.visibility = View.VISIBLE
        }

        selectedUsersAdapter.updateList(viewModel.tempSelectedUsers.value)
        val myViewpagerAdapter = MyViewpagerAdapter(
            childFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        myViewpagerAdapter.addFragment(hamkaranFragment, "")
        myViewpagerAdapter.addFragment(groupsFragment, "")
        myViewpagerAdapter.addFragment(personelFragment, "")
        view_pager.offscreenPageLimit = 3
        view_pager.adapter = myViewpagerAdapter

        selectedUsersAdapter.setOnUserChangedListener(object :
            SelectedUsersRecyclerViewAdapter.OnUsersChange {
            override fun onUserDeleted(user: UserModel?, position: Int) {


                viewModel.tempPersonels?.forEachIndexed { index, userModel ->
                    if (user?.orgLevelId == userModel.orgLevelId) {
                        userModel.selected = false
                        personelFragment.updateItem(index)
                    }
                }

                viewModel.tempPartners.forEachIndexed { index, userModel ->
                    if (user?.orgLevelId == userModel.orgLevelId) {
                        userModel.selected = false
                        hamkaranFragment.updateItem(index)
                    }
                }

                viewModel.tempGroups.forEachIndexed { index, groupItem ->

                    groupItem.groupMembers.forEachIndexed { childIndex, childGroupItem ->
                        if (user?.orgLevelId == childGroupItem.orgLevelId) {
                            childGroupItem.selected = false
                            groupsFragment.updateItem(childGroupItem)
                        }
                    }
                }

                viewModel.tempSelectedUsers.value?.removeAt(position)
                if (viewModel.tempSelectedUsers.value?.size == 0) {
                    rv_selected_users.visibility = View.GONE
                }

            }
        })

        view_pager.currentItem = 0


        btn_personel_tab.setOnClickListener {
            SelectPersonelTab()
        }
        btn_groups_tab.setOnClickListener {
            SelectGroupsTab()
        }
        btn_hamkaran_tab.setOnClickListener {
            SelectHamkaranTab()
        }

        rv_selected_users.layoutManager =
            WrapContentLinearLayoutManager(activity!!, RecyclerView.HORIZONTAL, true)
        rv_selected_users.adapter = selectedUsersAdapter

        personelFragment.setOnUsersChangeListener(
            object : PersonelFragment.OnUsersChange {
                override fun OnUserAdded(userModel: UserModel) {
                    if (rv_selected_users != null) {
                        rv_selected_users.visibility = View.VISIBLE
                        selectedUsersAdapter.addOne(userModel)
                        viewModel.tempPartners.forEachIndexed { index, it ->
                            if (it.orgLevelId == userModel.orgLevelId) {
                                it.selected = true
                                hamkaranFragment.updateItem(index)
                            }
                        }
                        viewModel.tempGroups.forEachIndexed { index, groupItem ->

                            groupItem.groupMembers.forEachIndexed { childIndex, childGroupItem ->
                                if (userModel.orgLevelId == childGroupItem.orgLevelId) {
                                    childGroupItem.selected = true
                                    groupsFragment.updateItem(childGroupItem)
                                }
                            }
                        }

                        rv_selected_users.smoothScrollToPosition(selectedUsersAdapter.itemCount)
                    }
                }

                override fun OnUserDeleted(userModel: UserModel) {
                    if (rv_selected_users != null) {
                        selectedUsersAdapter.removedById(userModel.orgLevelId)
                        viewModel.tempPartners.forEachIndexed { index, it ->
                            if (it.orgLevelId == userModel.orgLevelId) {
                                it.selected = false
                                hamkaranFragment.updateItem(index)
                            }
                        }
                        viewModel.tempGroups.forEachIndexed { index, groupItem ->

                            groupItem.groupMembers.forEachIndexed { childIndex, childGroupItem ->
                                if (userModel.orgLevelId == childGroupItem.orgLevelId) {
                                    childGroupItem.selected = false
                                    groupsFragment.updateItem(childGroupItem)
                                }
                            }
                        }
                        if (viewModel.tempSelectedUsers.value?.size == 0) {
                            rv_selected_users.visibility = View.GONE
                        }
                    }
                }
            })
        hamkaranFragment.setOnUsersChangeListener(
            object : HamkaranFragment.OnUsersChange {
                override fun OnUserAdded(userModel: UserModel) {
                    if (rv_selected_users != null) {
                        rv_selected_users.visibility = View.VISIBLE
                        selectedUsersAdapter.addOne(userModel)
                        viewModel.tempPersonels?.forEachIndexed { index, it ->
                            if (it.orgLevelId == userModel.orgLevelId) {
                                it.selected = true
                                personelFragment.updateItem(index)
                            }
                        }
                        viewModel.tempGroups.forEachIndexed { index, groupItem ->

                            groupItem.groupMembers.forEachIndexed { childIndex, childGroupItem ->
                                if (userModel.orgLevelId == childGroupItem.orgLevelId) {
                                    childGroupItem.selected = true
                                    groupsFragment.updateItem(childGroupItem)
                                }
                            }
                        }
                        rv_selected_users.smoothScrollToPosition(selectedUsersAdapter.itemCount)
                    }
                }

                override fun OnUserDeleted(userModel: UserModel) {
                    if (rv_selected_users != null) {
                        selectedUsersAdapter.removedById(userModel.orgLevelId)
                        viewModel.tempPersonels?.forEachIndexed { index, it ->
                            if (it.orgLevelId == userModel.orgLevelId) {
                                it.selected = false
                                personelFragment.updateItem(index)
                            }
                        }
                        viewModel.tempGroups.forEachIndexed { index, groupItem ->

                            groupItem.groupMembers.forEachIndexed { childIndex, childGroupItem ->
                                if (userModel.orgLevelId == childGroupItem.orgLevelId) {
                                    childGroupItem.selected = false
                                    groupsFragment.updateItem(childGroupItem)
                                }
                            }
                        }
                        if (viewModel.tempSelectedUsers.value?.size == 0) {
                            rv_selected_users.visibility = View.GONE
                        }
                    }
                }
            })

        groupsFragment.setOnUsersChangeListener(
            object : GroupsFragment.OnUsersChange {
                override fun OnUserAdded(userModel: UserModel) {
                    if (rv_selected_users != null) {
                        rv_selected_users.visibility = View.VISIBLE
                        selectedUsersAdapter.addOne(userModel)
                        viewModel.tempPersonels?.forEachIndexed { index, it ->
                            if (it.orgLevelId == userModel.orgLevelId) {
                                it.selected = true
                                personelFragment.updateItem(index)
                            }
                        }
                        viewModel.tempPartners.forEachIndexed { index, it ->
                            if (it.orgLevelId == userModel.orgLevelId) {
                                it.selected = true
                                hamkaranFragment.updateItem(index)
                            }
                        }

                        rv_selected_users.smoothScrollToPosition(selectedUsersAdapter.itemCount)
                    }
                }

                override fun OnUserDeleted(userModel: UserModel) {
                    if (rv_selected_users != null) {
                        selectedUsersAdapter.removedById(userModel.orgLevelId)
                        viewModel.tempPersonels?.forEachIndexed { index, it ->
                            if (it.orgLevelId == userModel.orgLevelId) {
                                it.selected = false
                                personelFragment.updateItem(index)
                            }
                        }
                        viewModel.tempPartners.forEachIndexed { index, it ->
                            if (it.orgLevelId == userModel.orgLevelId) {
                                it.selected = false
                                hamkaranFragment.updateItem(index)
                            }
                        }
                        if (viewModel.tempSelectedUsers.value?.size == 0) {
                            rv_selected_users.visibility = View.GONE
                        }
                    }
                }
            })

        btn_confirm.setOnClickListener {
            confirmed = true
            dismiss()
        }

        edt_search.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (textView.text.isEmpty() || textView.text.length > 1) {
                    search()
                    false
                } else {
                    Toast.makeText(
                        context,
                        "لطفا برای جستجو بیش از یک کاراکتر وارد نمایید",
                        Toast.LENGTH_SHORT
                    ).show()
                    true
                }
            } else
                false
        }

    }

    private fun SelectHamkaranTab() {
        if (!color_valueAnimator.isRunning) {
            if (selectedTab != SelectedTab.HAMKARAN) {
                view_pager.currentItem = 0
                val start_params =
                    guideline_start.layoutParams as ConstraintLayout.LayoutParams
                val end_params = guideline_end.layoutParams as ConstraintLayout.LayoutParams

                if (selectedTab == SelectedTab.GROUPS) {
                    start_valueAnimator =
                        ValueAnimator.ofFloat(0.3333333333f, 0.66666666666f)
                    end_valueAnimator = ValueAnimator.ofFloat(0.66666666666f, 1f)
                } else {
                    start_valueAnimator = ValueAnimator.ofFloat(0f, 0.66666666666f)
                    end_valueAnimator = ValueAnimator.ofFloat(0.3333333333f, 1f)
                }

                start_valueAnimator.duration = tabSelectionAnimationDuration
                end_valueAnimator.duration = tabSelectionAnimationDuration
                color_valueAnimator.duration = tabSelectionAnimationDuration

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
                    btn_hamkaran_tab.setTextColor(
                        ColorUtils.blendARGB(
                            Color.parseColor("#707070"),
                            Color.parseColor("#ffffff"),
                            animatedValue
                        )
                    )
                    if (selectedTab == SelectedTab.GROUPS) {
                        btn_groups_tab.setTextColor(
                            ColorUtils.blendARGB(
                                Color.parseColor("#ffffff"),
                                Color.parseColor("#707070"),
                                animatedValue
                            )
                        )
                        btn_personel_tab.setTextColor(
                            ColorUtils.blendARGB(
                                Color.parseColor("#707070"),
                                Color.parseColor("#707070"),
                                animatedValue
                            )
                        )
                    } else {
                        btn_personel_tab.setTextColor(
                            ColorUtils.blendARGB(
                                Color.parseColor("#ffffff"),
                                Color.parseColor("#707070"),
                                animatedValue
                            )
                        )
                        btn_groups_tab.setTextColor(
                            ColorUtils.blendARGB(
                                Color.parseColor("#707070"),
                                Color.parseColor("#707070"),
                                animatedValue
                            )
                        )
                    }

                }
                color_valueAnimator.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {

                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        selectedTab = SelectedTab.HAMKARAN
                    }

                    override fun onAnimationCancel(p0: Animator?) {

                    }

                    override fun onAnimationStart(p0: Animator?) {

                    }
                })
                start_valueAnimator.start()
                end_valueAnimator.start()
                color_valueAnimator.start()
            }
        }
    }

    private fun SelectGroupsTab() {
        if (!color_valueAnimator.isRunning) {
            if (selectedTab != SelectedTab.GROUPS) {
                view_pager.currentItem = 1
                val start_params =
                    guideline_start.layoutParams as ConstraintLayout.LayoutParams
                val end_params = guideline_end.layoutParams as ConstraintLayout.LayoutParams



                if (selectedTab == SelectedTab.PERSONEL) {
                    start_valueAnimator = ValueAnimator.ofFloat(0f, 0.3333333333f)
                    end_valueAnimator = ValueAnimator.ofFloat(0.3333333333f, 0.66666666666f)
                } else {
                    start_valueAnimator =
                        ValueAnimator.ofFloat(0.66666666666f, 0.3333333333f)
                    end_valueAnimator = ValueAnimator.ofFloat(1f, 0.66666666666f)
                }

                start_valueAnimator.duration = tabSelectionAnimationDuration
                end_valueAnimator.duration = tabSelectionAnimationDuration
                color_valueAnimator.duration = tabSelectionAnimationDuration

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
                    btn_groups_tab.setTextColor(
                        ColorUtils.blendARGB(
                            Color.parseColor("#707070"),
                            Color.parseColor("#ffffff"),
                            animatedValue
                        )
                    )
                    if (selectedTab == SelectedTab.PERSONEL) {
                        btn_personel_tab.setTextColor(
                            ColorUtils.blendARGB(
                                Color.parseColor("#ffffff"),
                                Color.parseColor("#707070"),
                                animatedValue
                            )
                        )
                        btn_hamkaran_tab.setTextColor(
                            ColorUtils.blendARGB(
                                Color.parseColor("#707070"),
                                Color.parseColor("#707070"),
                                animatedValue
                            )
                        )
                    } else {
                        btn_hamkaran_tab.setTextColor(
                            ColorUtils.blendARGB(
                                Color.parseColor("#ffffff"),
                                Color.parseColor("#707070"),
                                animatedValue
                            )
                        )
                        btn_personel_tab.setTextColor(
                            ColorUtils.blendARGB(
                                Color.parseColor("#707070"),
                                Color.parseColor("#707070"),
                                animatedValue
                            )
                        )
                    }

                }
                color_valueAnimator.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {

                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        selectedTab = SelectedTab.GROUPS
                    }

                    override fun onAnimationCancel(p0: Animator?) {

                    }

                    override fun onAnimationStart(p0: Animator?) {

                    }
                })
                start_valueAnimator.start()
                end_valueAnimator.start()
                color_valueAnimator.start()
            }

        }
    }

    private fun SelectPersonelTab() {
        if (!color_valueAnimator.isRunning) {
            if (selectedTab != SelectedTab.PERSONEL) {
                view_pager.currentItem = 2
                val start_params =
                    guideline_start.layoutParams as ConstraintLayout.LayoutParams
                val end_params = guideline_end.layoutParams as ConstraintLayout.LayoutParams

                if (selectedTab == SelectedTab.GROUPS) {
                    start_valueAnimator = ValueAnimator.ofFloat(0.3333333333f, 0f)
                    end_valueAnimator = ValueAnimator.ofFloat(0.66666666666f, 0.3333333333f)
                } else {
                    start_valueAnimator = ValueAnimator.ofFloat(0.66666666666f, 0f)
                    end_valueAnimator = ValueAnimator.ofFloat(1f, 0.3333333333f)
                }

                start_valueAnimator.duration = tabSelectionAnimationDuration
                end_valueAnimator.duration = tabSelectionAnimationDuration
                color_valueAnimator.duration = tabSelectionAnimationDuration

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
                    btn_personel_tab.setTextColor(
                        ColorUtils.blendARGB(
                            Color.parseColor("#707070"),
                            Color.parseColor("#ffffff"),
                            animatedValue
                        )
                    )
                    if (selectedTab == SelectedTab.GROUPS) {
                        btn_groups_tab.setTextColor(
                            ColorUtils.blendARGB(
                                Color.parseColor("#ffffff"),
                                Color.parseColor("#707070"),
                                animatedValue
                            )
                        )
                        btn_hamkaran_tab.setTextColor(
                            ColorUtils.blendARGB(
                                Color.parseColor("#707070"),
                                Color.parseColor("#707070"),
                                animatedValue
                            )
                        )
                    } else {
                        btn_hamkaran_tab.setTextColor(
                            ColorUtils.blendARGB(
                                Color.parseColor("#ffffff"),
                                Color.parseColor("#707070"),
                                animatedValue
                            )
                        )
                        btn_groups_tab.setTextColor(
                            ColorUtils.blendARGB(
                                Color.parseColor("#707070"),
                                Color.parseColor("#707070"),
                                animatedValue
                            )
                        )
                    }

                }
                color_valueAnimator.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {

                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        selectedTab = SelectedTab.PERSONEL
                    }

                    override fun onAnimationCancel(p0: Animator?) {

                    }

                    override fun onAnimationStart(p0: Animator?) {

                    }
                })
                start_valueAnimator.start()
                end_valueAnimator.start()
                color_valueAnimator.start()
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onDismiss.Ondismiss(confirmed)
        confirmed = false
    }

    fun search() {
        when (selectedTab) {
            SelectedTab.HAMKARAN -> {
                if (edt_search.text.toString().isEmpty()) {
                    hamkaranFragment.updateAll(viewModel.tempPartners)
                } else {
                    val filteredList = viewModel.tempPartners.filter {
                        it.fullName?.contains(edt_search.text.toString()) == true
                    }
                    hamkaranFragment.updateAll(filteredList.toMutableList())

                }
            }
            SelectedTab.GROUPS -> {

            }
            SelectedTab.PERSONEL -> {
                if (edt_search.text.toString().isNotBlank())
                {
                    Log.i("fgifigrr",token)
                    Log.i("fgifigrr23",App.sharedPreferences?.getString(Constants.TOKEN,"null"))
                    val requestBody = RequestBody.create(MediaType.parse("text/plain"), edt_search.text.toString())
                    val req = App.getRetofitApi()?.searchInPersonel(App.sharedPreferences?.getString(Constants.TOKEN,"null"), requestBody)
                    req?.enqueue {
                        onResponse = {
                            if (isResponseValid(it) == 2) {
                                Log.i("nunub","SD")
                                val personel = it.body()?.data
                                personel?.forEach { person ->
                                    viewModel.tempSelectedUsers.value?.forEach { user ->
                                        if (person.orgLevelId == user.orgLevelId) {
                                            person.selected = true
                                        }
                                    }
                                }
                                viewModel.tempPersonels = personel?.toMutableList()
                                viewModel.selectedPersonel = personel?.toMutableList()
                                personelFragment.updateAll(personel?.toMutableList())
                            }
                            else if (isResponseValid(it) == 1) {
                                (activity as BaseActivity).silentLogin(object : OnLoginCompleted {
                                    override fun OnSuccess() {
                                        search()
                                    }
                                })
                            }
                        }
                        onFailure = {
                            Log.i("MyTagg", it?.message ?: "")
                        }
                    }
                }


            }
        }

    }

    fun setOnDismissListener(onDismissListener: OnDismissListener) {
        this.onDismiss = onDismissListener
    }


    interface OnDismissListener {
        fun Ondismiss(confirmed: Boolean)
    }


}