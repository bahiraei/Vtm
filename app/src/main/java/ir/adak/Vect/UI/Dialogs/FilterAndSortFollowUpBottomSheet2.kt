package ir.adak.Vect.UI.Dialogs

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ir.adak.Vect.Adapters.MyViewpagerAdapter
import ir.adak.Vect.Data.Enums.FileTypeEnum
import ir.adak.Vect.Data.Enums.FollowUpType
import ir.adak.Vect.Data.Models.PeigiriItem.PeigiriItem
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.BaseActivity.Companion.userInfo
import ir.adak.Vect.UI.Activities.PeigiriActivity.PeigiriActivity
import ir.adak.Vect.UI.Fragments.FilterFragment
import ir.adak.Vect.UI.Fragments.SortFragment
import ir.adak.Vect.UI.CustomViews.NonSwipeableViewPager
import kotlinx.android.synthetic.main.filter_and_sort_follwup_bottom_sheet.*


class FilterAndSortFollowUpBottomSheet(var isSort: Boolean) :
    BottomSheetDialogFragment() {


    enum class SelectedTab {
        FILTER_TAB,
        SORT_TAB
    }




    private val tabSelectionAnimationDuration = 300L
    var selectedTab = SelectedTab.FILTER_TAB
    val filterFragment = FilterFragment()
    val sortFragment = SortFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SheetDialog)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return View.inflate(context, R.layout.filter_and_sort_follwup_bottom_sheet, null)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myViewpagerAdapter = MyViewpagerAdapter(
            childFragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        myViewpagerAdapter.addFragment(filterFragment, "")
        myViewpagerAdapter.addFragment(sortFragment, "")
        view_pager.adapter = myViewpagerAdapter

        if (isSort) {
            selectSortTab(
                view_pager,
                guideline_start,
                guideline_end,
                btn_filter_tab,
                btn_sort_tab,
                false
            )
        }

        btn_filter_tab.setOnClickListener {
            if (selectedTab != SelectedTab.FILTER_TAB) {
                selectFilterTab(
                    view_pager,
                    guideline_start,
                    guideline_end,
                    btn_filter_tab,
                    btn_sort_tab,
                    true
                )

            }

        }
        btn_sort_tab.setOnClickListener {

            if (selectedTab != SelectedTab.SORT_TAB) {
                selectSortTab(
                    view_pager,
                    guideline_start,
                    guideline_end,
                    btn_filter_tab,
                    btn_sort_tab,
                    true
                )
            }

        }

        btn_confirm.setOnClickListener {
            if (selectedTab == SelectedTab.FILTER_TAB) {
                (activity as PeigiriActivity).filterdFollowUpList.clear()
                if (((activity as PeigiriActivity).viewModel?.MY_FOLLOWUPS == false &&
                            (activity as PeigiriActivity).viewModel?.OTHERS_FOLLOWUPS == false &&
                            (activity as PeigiriActivity).viewModel?.IMAGES_FOLLOWUPS == false &&
                            (activity as PeigiriActivity).viewModel?.VIDEOS_FOLLOWUPS == false &&
                            (activity as PeigiriActivity).viewModel?.AUDIO_FOLLOWUPS == false &&
                            (activity as PeigiriActivity).viewModel?.FILES_FOLLOWUPS == false &&
                            (activity as PeigiriActivity).viewModel?.Jobs == false&&
                            (activity as PeigiriActivity).viewModel?.Meeting == false)&&
                            (activity as PeigiriActivity).viewModel?.Action==false ) {
                    (activity as PeigiriActivity).filterdFollowUpList.addAll((activity as PeigiriActivity).mainFollowUpList)
                } else {
                    val filesList = mutableListOf<PeigiriItem>()
                    if ((activity as PeigiriActivity).viewModel?.IMAGES_FOLLOWUPS == true) {
                        filesList.addAll((activity as PeigiriActivity).mainFollowUpList.filter {
                            it.followUp.followUpType == FollowUpType.File && it.followUp.fileTypeEnum == FileTypeEnum.Image
                        })
                    }
                    if ((activity as PeigiriActivity).viewModel?.VIDEOS_FOLLOWUPS == true) {
                        filesList.addAll((activity as PeigiriActivity).mainFollowUpList.filter {
                            it.followUp.followUpType == FollowUpType.File && it.followUp.fileTypeEnum == FileTypeEnum.Video
                        })
                    }
                    if ((activity as PeigiriActivity).viewModel?.AUDIO_FOLLOWUPS == true) {
                        filesList.addAll((activity as PeigiriActivity).mainFollowUpList.filter {
                            it.followUp.followUpType == FollowUpType.File && it.followUp.fileTypeEnum == FileTypeEnum.Audio
                        })
                    }
                    if ((activity as PeigiriActivity).viewModel?.FILES_FOLLOWUPS == true) {
                        filesList.addAll((activity as PeigiriActivity).mainFollowUpList.filter {
                            it.followUp.followUpType == FollowUpType.File && it.followUp.fileTypeEnum == FileTypeEnum.Unknown
                        })
                    }
                    if ((activity as PeigiriActivity).viewModel?.Jobs == true) {
                        filesList.addAll((activity as PeigiriActivity).mainFollowUpList.filter {
//                            it.followUp.followUpType == FollowUpType.Jobs
                            it.followUp.followUpType == FollowUpType.NewProject||
                                    it.followUp.followUpType == FollowUpType.SummaryEndProject||
                                    it.followUp.followUpType == FollowUpType.CreatedProject
//                                    && it.followUp.fileTypeEnum == FileTypeEnum.FaceStatus
                        })
                    }
                    if ((activity as PeigiriActivity).viewModel?.Meeting == true) {
                        filesList.addAll((activity as PeigiriActivity).mainFollowUpList.filter {
                            it.followUp.followUpType == FollowUpType.NewMeeting
//                                    && it.followUp.fileTypeEnum == FileTypeEnum.FaceStatus
                        })
                    }
                    if ((activity as PeigiriActivity).viewModel?.Action == true) {
                        filesList.addAll((activity as PeigiriActivity).mainFollowUpList.filter {
                            it.followUp.followUpType == FollowUpType.ActionTime
//                                    && it.followUp.fileTypeEnum == FileTypeEnum.FaceStatus
                        })
                    }
                    if ((activity as PeigiriActivity).viewModel?.IMAGES_FOLLOWUPS == true ||
                        (activity as PeigiriActivity).viewModel?.VIDEOS_FOLLOWUPS == true ||
                        (activity as PeigiriActivity).viewModel?.AUDIO_FOLLOWUPS == true ||
                        (activity as PeigiriActivity).viewModel?.FILES_FOLLOWUPS == true ||
//                        (activity as PeigiriActivity).viewModel?.SORAT_JALASE_FOLLOWUPS == true
                        (activity as PeigiriActivity).viewModel?.Jobs == true||
                        (activity as PeigiriActivity).viewModel?.Meeting == true||
                        (activity as PeigiriActivity).viewModel?.Action == true
                    )
                    {
                        if ((activity as PeigiriActivity).viewModel?.MY_FOLLOWUPS == true ||
                            (activity as PeigiriActivity).viewModel?.OTHERS_FOLLOWUPS == true
                        ) {
                            if ((activity as PeigiriActivity).viewModel?.MY_FOLLOWUPS == true) {
                                (activity as PeigiriActivity).filterdFollowUpList.addAll(filesList.filter {
                                    it.followUp.orgLevelId == userInfo?.orgLevelId
                                })
                            } else {
                                (activity as PeigiriActivity).filterdFollowUpList.addAll(filesList.filter {
//                                    it.followUp.orgLevelId != (activity as PeigiriActivity).userInfo?.orgLevelId
                                    it.followUp.orgLevelId !=  userInfo?.orgLevelId
                                })
                            }
                        } else {
                            (activity as PeigiriActivity).filterdFollowUpList.addAll(filesList)
                        }
                    } else {
                        if ((activity as PeigiriActivity).viewModel?.MY_FOLLOWUPS == true) {
                            (activity as PeigiriActivity).filterdFollowUpList.addAll((activity as PeigiriActivity).mainFollowUpList.filter {
                                it.followUp.orgLevelId ==  userInfo?.orgLevelId
                            })
                        }
                        if ((activity as PeigiriActivity).viewModel?.OTHERS_FOLLOWUPS == true) {
                            (activity as PeigiriActivity).filterdFollowUpList.addAll((activity as PeigiriActivity).mainFollowUpList.filter {
                                it.followUp.orgLevelId !=  userInfo?.orgLevelId
                            })
                        }
                    }

                    (activity as PeigiriActivity).filterdFollowUpList.sortBy {
                        it.followUp.persianDate
                    }
                }
            }
            else if (selectedTab == SelectedTab.SORT_TAB) {
                if ((activity as PeigiriActivity).filterdFollowUpList.isNullOrEmpty()) {
                    (activity as PeigiriActivity).filterdFollowUpList.addAll((activity as PeigiriActivity).mainFollowUpList)
                }
                if ((activity as PeigiriActivity).viewModel?.DATE_SORT == true) {
                    if ((activity as PeigiriActivity).viewModel?.SOUDI_SORT == true) {
                        (activity as PeigiriActivity).filterdFollowUpList.sortBy { it.followUp.persianDate }
                    } else {
                        (activity as PeigiriActivity).filterdFollowUpList.sortByDescending { it.followUp.persianDate }
                    }
                }

            }
            dismiss()
            (activity as PeigiriActivity).updateList((activity as PeigiriActivity).filterdFollowUpList)
        }
    }

    private fun selectFilterTab(
        view_pager: NonSwipeableViewPager,
        guideline_start: Guideline,
        guideline_end: Guideline,
        btn_filter_tab: TextView,
        btn_sort_tab: TextView,
        animate: Boolean
    ) {
        selectedTab = SelectedTab.FILTER_TAB
        view_pager.currentItem = 0
        val start_params = guideline_start.layoutParams as ConstraintLayout.LayoutParams
        val end_params = guideline_end.layoutParams as ConstraintLayout.LayoutParams

        val start_valueAnimator = ValueAnimator.ofFloat(0f, 0.5f)
        val end_valueAnimator = ValueAnimator.ofFloat(0.5f, 1f)
        val color_valueAnimator = ValueAnimator.ofFloat(0f, 1f)

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
            btn_filter_tab.setTextColor(
                ColorUtils.blendARGB(
                    Color.parseColor("#707070"),
                    Color.parseColor("#ffffff"),
                    animatedValue
                )
            )
            btn_sort_tab.setTextColor(
                ColorUtils.blendARGB(
                    Color.parseColor("#ffffff"),
                    Color.parseColor("#707070"),
                    animatedValue
                )
            )
        }

        start_valueAnimator.start()
        end_valueAnimator.start()
        color_valueAnimator.start()
    }

    private fun selectSortTab(
        view_pager: NonSwipeableViewPager,
        guideline_start: Guideline,
        guideline_end: Guideline,
        btn_filter_tab: TextView,
        btn_sort_tab: TextView,
        animate: Boolean
    ) {
        selectedTab = SelectedTab.SORT_TAB
        view_pager.currentItem = 1
        val start_params = guideline_start.layoutParams as ConstraintLayout.LayoutParams
        val end_params = guideline_end.layoutParams as ConstraintLayout.LayoutParams

        val start_valueAnimator = ValueAnimator.ofFloat(0.5f, 0f)
        val end_valueAnimator = ValueAnimator.ofFloat(1f, 0.5f)
        val color_valueAnimator = ValueAnimator.ofFloat(0f, 1f)

        start_valueAnimator.duration = if (animate) tabSelectionAnimationDuration else 0
        end_valueAnimator.duration = if (animate) tabSelectionAnimationDuration else 0
        color_valueAnimator.duration = if (animate) tabSelectionAnimationDuration else 0

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
            btn_filter_tab.setTextColor(
                ColorUtils.blendARGB(
                    Color.parseColor("#ffffff"),
                    Color.parseColor("#707070"),
                    animatedValue
                )
            )
            btn_sort_tab.setTextColor(
                ColorUtils.blendARGB(
                    Color.parseColor("#707070"),
                    Color.parseColor("#ffffff"),
                    animatedValue
                )
            )
        }

        start_valueAnimator.start()
        end_valueAnimator.start()
        color_valueAnimator.start()
    }


}