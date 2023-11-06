package ir.adak.Vect.UI.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.MainActivity.MainActivity
import androidx.core.content.res.ResourcesCompat
import ir.adak.Vect.Data.Models.FilterMeetingModel
import ir.adak.Vect.UI.Dialogs.FilterMeetingsBottomSheet
import kotlinx.android.synthetic.main.fragment_meetings_filter_main.*


/**
 * A simple [Fragment] subclass.
 */
class MeetingsFilter_Main_Fragment : BaseFragment() {

    var mAlreadyLoaded: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meetings_filter_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null && !mAlreadyLoaded) {
            mAlreadyLoaded = true
            // Do this code only first time, not after rotation or reuse fragment from backstack

            (activity as MainActivity).tempFilterMeetingModel = Gson().fromJson(
//                Gson().toJson((activity as MainActivity).selectedFilterMeetingModel),
                Gson().toJson(MainActivity.selectedFilterMeetingModel),
                object : TypeToken<FilterMeetingModel>() {}.type
            )
        }

        btn_date_filter.setOnClickListener {
            (parentFragment as FilterMeetingsBottomSheet).chooseDateFilterFragment()
        }

        spinner_created_by_filter.setItems("همه", "من", "دیگران")
        spinner_status_filter.setItems("همه", "برگزار شده", "برگزار نشده")

        when ((activity as MainActivity).tempFilterMeetingModel?.RegisterType) {
            0 -> {
                spinner_created_by_filter.selectedIndex = 1
            }
            1 -> {
                spinner_created_by_filter.selectedIndex = 2
            }
            else -> {
                spinner_created_by_filter.selectedIndex = 0
            }
        }

        when ((activity as MainActivity).tempFilterMeetingModel?.HeldFilter) {
            0 -> {
                spinner_status_filter.selectedIndex = 1
            }
            1 -> {
                spinner_status_filter.selectedIndex = 2
            }
            else -> {
                spinner_status_filter.selectedIndex = 0
            }
        }
        if ((activity as MainActivity).tempFilterMeetingModel?.DateFrom != null
            || (activity as MainActivity).tempFilterMeetingModel?.DateTo != null) {
            txt_date_filter.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.colorPrimary,
                    null
                )
            )
            txt_date_filter.text = "فیلتر شده"
        }

        spinner_created_by_filter.setOnItemSelectedListener { view, position, id, item ->
            when (position) {
                0 -> {
                    (activity as MainActivity).tempFilterMeetingModel?.RegisterType =
                        -1
                }
                1 -> {
                    (activity as MainActivity).tempFilterMeetingModel?.RegisterType =
                        0
                }
                2 -> {
                    (activity as MainActivity).tempFilterMeetingModel?.RegisterType =
                        1
                }
            }
        }
        spinner_status_filter.setOnItemSelectedListener { view, position, id, item ->
            when (position) {
                0 -> {
                    (activity as MainActivity).tempFilterMeetingModel?.HeldFilter =
                        -1
                }
                1 -> {
                    (activity as MainActivity).tempFilterMeetingModel?.HeldFilter =
                        1
                }
                2 -> {
                    (activity as MainActivity).tempFilterMeetingModel?.HeldFilter =
                        0
                }
            }
        }

        btn_submit.setOnClickListener {
            MainActivity.selectedFilterMeetingModel = Gson().fromJson(
                Gson().toJson((activity as MainActivity).tempFilterMeetingModel),
                object : TypeToken<FilterMeetingModel>() {}.type
            )

            (activity as MainActivity).meetingsFragment.getMeetingsFirstPage()
            (parentFragment as FilterMeetingsBottomSheet).DismissBottomSheet()
        }

        btn_clear_filter.setOnClickListener {
            clearFilter()
        }
    }

    fun clearFilter() {
        val searchText = MainActivity.selectedFilterMeetingModel?.Title
        MainActivity.selectedFilterMeetingModel = FilterMeetingModel()
        MainActivity.selectedFilterMeetingModel?.Title = searchText
        (activity as MainActivity).meetingsFragment.getMeetingsFirstPage()
        (parentFragment as FilterMeetingsBottomSheet).dismiss()

    }
}
