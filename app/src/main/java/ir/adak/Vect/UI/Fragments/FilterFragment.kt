package ir.adak.Vect.UI.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import ir.adak.Vect.Data.Models.FollowUpFilterItem

import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.PeigiriActivity.PeigiriActivity
import ir.adak.Vect.Utils.WrapContentLinearLayoutManager
import kotlinx.android.synthetic.main.fragment_filter.*

/**
 * A simple [Fragment] subclass.
 */
class FilterFragment : BaseFragment() {

    enum class FollowUpFilterEnum {
        NO_FILTER,
        MY_FOLLOWUPS,
        OTHERS_FOLLOWUPS,
        IMAGES_FOLLOWUPS,
        VIDEOS_FOLLOWUPS,
        AUDIO_FOLLOWUPS,
        FILES_FOLLOWUPS,
//        SORAT_JALASE_FOLLOWUPS;
        Jobs,
        Menting,
        Action
    }


    val filterAdapter = GroupAdapter<ViewHolder>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_filter, container, false)
        view.rotationY = 180f
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_filter.apply {
            layoutManager = WrapContentLinearLayoutManager(activity)
            adapter = filterAdapter
        }
        val onFilterItemSelected = object : OnFilterItemSelected {
            override fun OnFilterItemSelected(followUpFilterItem: FollowUpFilterItem) {
                when (followUpFilterItem.followUpFilterEnum) {
                    FollowUpFilterEnum.NO_FILTER -> (activity as PeigiriActivity).viewModel?.NO_FILTER =
                        followUpFilterItem.selected
                    FollowUpFilterEnum.MY_FOLLOWUPS -> (activity as PeigiriActivity).viewModel?.MY_FOLLOWUPS =
                        followUpFilterItem.selected
                    FollowUpFilterEnum.OTHERS_FOLLOWUPS -> (activity as PeigiriActivity).viewModel?.OTHERS_FOLLOWUPS =
                        followUpFilterItem.selected
                    FollowUpFilterEnum.IMAGES_FOLLOWUPS -> (activity as PeigiriActivity).viewModel?.IMAGES_FOLLOWUPS =
                        followUpFilterItem.selected
                    FollowUpFilterEnum.VIDEOS_FOLLOWUPS -> (activity as PeigiriActivity).viewModel?.VIDEOS_FOLLOWUPS =
                        followUpFilterItem.selected
                    FollowUpFilterEnum.AUDIO_FOLLOWUPS -> (activity as PeigiriActivity).viewModel?.AUDIO_FOLLOWUPS =
                        followUpFilterItem.selected
                    FollowUpFilterEnum.FILES_FOLLOWUPS -> (activity as PeigiriActivity).viewModel?.FILES_FOLLOWUPS =
                        followUpFilterItem.selected
//                    FollowUpFilterEnum.SORAT_JALASE_FOLLOWUPS -> (activity as PeigiriActivity).viewModel?.SORAT_JALASE_FOLLOWUPS =
//                        followUpFilterItem.selected
                    FollowUpFilterEnum.Jobs -> (activity as PeigiriActivity).viewModel?.Jobs =
                        followUpFilterItem.selected
                    FollowUpFilterEnum.Menting -> (activity as PeigiriActivity).viewModel?.Meeting =
                        followUpFilterItem.selected
                    FollowUpFilterEnum.Action -> (activity as PeigiriActivity).viewModel?.Action =
                        followUpFilterItem.selected
                }
            }
        }
        val filterList = mutableListOf<FollowUpFilterItem>(
            FollowUpFilterItem(
                "پیگیری های خودم",
                (activity as PeigiriActivity).viewModel?.MY_FOLLOWUPS,
                FollowUpFilterEnum.MY_FOLLOWUPS,
                onFilterItemSelected
            ),
            FollowUpFilterItem(
                "پیگیری های دیگران",
                (activity as PeigiriActivity).viewModel?.OTHERS_FOLLOWUPS,
                FollowUpFilterEnum.OTHERS_FOLLOWUPS,
                onFilterItemSelected
            ),
            FollowUpFilterItem(
                "عکس ",
                (activity as PeigiriActivity).viewModel?.IMAGES_FOLLOWUPS,
                FollowUpFilterEnum.IMAGES_FOLLOWUPS,
                onFilterItemSelected
            ),
            FollowUpFilterItem(
                "فیلم ",
                (activity as PeigiriActivity).viewModel?.VIDEOS_FOLLOWUPS,
                FollowUpFilterEnum.VIDEOS_FOLLOWUPS,
                onFilterItemSelected
            ),
            FollowUpFilterItem(
                "صدا ",
                (activity as PeigiriActivity).viewModel?.AUDIO_FOLLOWUPS,
                FollowUpFilterEnum.AUDIO_FOLLOWUPS,
                onFilterItemSelected
            ),
            FollowUpFilterItem(
                "فایل ",
                (activity as PeigiriActivity).viewModel?.FILES_FOLLOWUPS,
                FollowUpFilterEnum.FILES_FOLLOWUPS,
                onFilterItemSelected
            ),
            FollowUpFilterItem(
                "وظایف",
                (activity as PeigiriActivity).viewModel?.Jobs,
//                FollowUpFilterEnum.SORAT_JALASE_FOLLOWUPS,
                FollowUpFilterEnum.Jobs,
                onFilterItemSelected
            ),
            FollowUpFilterItem(
                "جلسه",
                (activity as PeigiriActivity).viewModel?.Meeting,
//                FollowUpFilterEnum.SORAT_JALASE_FOLLOWUPS,
                FollowUpFilterEnum.Menting,
                onFilterItemSelected
            ),
            FollowUpFilterItem(
                "اقدام",
                (activity as PeigiriActivity).viewModel?.Action,
//                FollowUpFilterEnum.SORAT_JALASE_FOLLOWUPS,
                FollowUpFilterEnum.Action,
                onFilterItemSelected
            )
        )
        filterAdapter.update(filterList)
    }

    interface OnFilterItemSelected {
        fun OnFilterItemSelected(followUpFilterItem: FollowUpFilterItem)
    }


}
