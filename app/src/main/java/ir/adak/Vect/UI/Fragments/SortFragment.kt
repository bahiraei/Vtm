package ir.adak.Vect.UI.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import ir.adak.Vect.Data.Models.FollowUpSortItem

import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.PeigiriActivity.PeigiriActivity
import ir.adak.Vect.UI.Fragments.SortFragment.FollowUpSortEnum.*
import ir.adak.Vect.Utils.WrapContentLinearLayoutManager
import kotlinx.android.synthetic.main.fragment_sort.*

/**
 * A simple [Fragment] subclass.
 */
class SortFragment() : BaseFragment() {

    enum class FollowUpSortEnum {
        DATE
    }

    val sortAdapter = GroupAdapter<ViewHolder>()

    interface OnSortItemSelected {
        fun OnSortItemSelected(followUpSortItem: FollowUpSortItem)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sort, container, false)
        view.rotationY = 180f
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_sort.apply {
            layoutManager = WrapContentLinearLayoutManager(activity)
            adapter = sortAdapter
        }

        val onSortItemSelected = object : OnSortItemSelected {
            override fun OnSortItemSelected(followUpSortItem: FollowUpSortItem) {
                if (followUpSortItem.isSwitch) {
                    (activity as PeigiriActivity).viewModel?.SOUDI_SORT =
                        followUpSortItem.selected == true
                    return
                }
                if (
                    (activity as PeigiriActivity).viewModel?.currentSortItem?.followUpSortEnum != followUpSortItem.followUpSortEnum
                ) {
                    (activity as PeigiriActivity).viewModel?.currentSortItem?.selected = false
                    (activity as PeigiriActivity).viewModel?.currentSortItem?.notifyChanged()
                    (activity as PeigiriActivity).viewModel?.currentSortItem = followUpSortItem
                    followUpSortItem.notifyChanged()
                    when (followUpSortItem.followUpSortEnum) {
                        DATE -> {
                            (activity as PeigiriActivity).viewModel?.DATE_SORT = followUpSortItem.selected == true
                        }
                    }
                }

            }
        }

        val switchSort = FollowUpSortItem(
            "مرتب سازی صعودی",
            (activity as PeigiriActivity).viewModel?.SOUDI_SORT,
            null,
            onSortItemSelected
        )
        switchSort.isSwitch = true

        val sortList = mutableListOf<FollowUpSortItem>(
            FollowUpSortItem(
                "تاریخ", (activity as PeigiriActivity).viewModel?.DATE_SORT,
                DATE,
                onSortItemSelected
            )
        )
        if ((activity as PeigiriActivity).viewModel?.DATE_SORT == true)
            (activity as PeigiriActivity).viewModel?.currentSortItem = sortList[0]

        sortList.add(0, switchSort)
        sortAdapter.update(sortList)

    }


}
