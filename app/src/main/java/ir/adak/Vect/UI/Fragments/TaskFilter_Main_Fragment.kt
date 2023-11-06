package ir.adak.Vect.UI.Fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ir.adak.Vect.Data.Models.FilterProjectModel

import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.MainActivity.MainActivity
import ir.adak.Vect.UI.Dialogs.FilterTasksBottomSheet
import kotlinx.android.synthetic.main.fragment_task_filter_main.*
import androidx.core.content.res.ResourcesCompat
import ir.adak.Vect.UI.Activities.BaseActivity
import ir.adak.Vect.UI.Activities.MainActivity.MainActivity.Companion.Counter_2


/**
 * A simple [Fragment] subclass.
 */
class TaskFilter_Main_Fragment : BaseFragment() {
    var mAlreadyLoaded: Boolean = false
    var Flag_Change=false

    var Data:ArrayList<String> ?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_filter_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null && !mAlreadyLoaded){
            mAlreadyLoaded = true
            // Do this code only first time, not after rotation or reuse fragment from backstack
            MainActivity.tempFilterProjectModel = Gson().fromJson(
                Gson().toJson(MainActivity.selectedFilterProjectModel),
                object : TypeToken<FilterProjectModel>() {}.type
            )
        }
        Data= ArrayList()
        btn_date_filter.setOnClickListener {
            (parentFragment as FilterTasksBottomSheet).chooseDateFilterFragment()
        }

        if (!MainActivity.Flag_filtte)
        {
            MainActivity.Flag_filtte=true
            clearFilter_2()
        }

        (activity as MainActivity).mytempFilterProjectModel=FilterProjectModel()
        Log.i("svkskbsjvns", MainActivity.selectedFilterProjectModel.toString())
//        MainActivity.tempFilterProjectModel=(activity as MainActivity).selectedFilterProjectModel
        (activity as MainActivity).mytempFilterProjectModel=MainActivity.selectedFilterProjectModel
//        mytempFilterProjectModel=(activity as MainActivity).selectedFilterProjectModel
        Log.i("yetuwryweriwet",
            (activity as MainActivity).mytempFilterProjectModel?.ProjectStatus.toString()
        )




        Action_Ch.setOnClickListener {
            Log.i("svfewwer", Action_Ch.isChecked.toString())
            MainActivity.tempFilterProjectModel?.actionStatusPending=Action_Ch.isChecked
            (activity as MainActivity).mytempFilterProjectModel?.actionStatusPending=Action_Ch.isChecked
            Log.i("svfewwer",  MainActivity.tempFilterProjectModel?.actionStatusPending.toString())
            Log.i("svfewwer",  (activity as MainActivity).mytempFilterProjectModel?.actionStatusPending.toString())
//            (activity as MainActivity).selectedFilterProjectModel?.actionStatusPending=Action_Ch.isChecked
        }



        Action_State.setOnClickListener {
            Action_Ch.isChecked=!Action_Ch.isChecked
            Log.i("sccdaca", Action_Ch.isChecked.toString())
            MainActivity.tempFilterProjectModel?.actionStatusPending=Action_Ch.isChecked
            (activity as MainActivity).mytempFilterProjectModel?.actionStatusPending=Action_Ch.isChecked
//            (activity as MainActivity).tempFilterProjectModel?.actionStatusPending=Action_Ch.isChecked
        }

//        spinner_step_filter.setItems("همه", "BackLog", "To Do", "Doing", "Done")


        spinner_user_role_filter.setItems("همه", "ایجاد کننده", "مجری", "همکار")
        spinner_priority_filter.setItems("همه", "عادی", "مهم", "خیلی مهم")
        spinner_status_filter.setItems("همه", "فعال", "غیر فعال", "تاخیر یافته", "اتمام شده")
        spinner_from_meeting_filter.setItems("همه", "جلسه ای", "غیر جلسه ای")

        if(MainActivity.tempFilterProjectModel?.actionStatusPending!=null)
        {
            Log.i("sccdaca","A")
            Log.i("sccdaca", Action_Ch.isChecked.toString())
            Action_Ch.isChecked = MainActivity.selectedFilterProjectModel?.actionStatusPending!!
        }else{
             Log.i("sccdaca","B")
            Log.i("sccdaca", Action_Ch.isChecked.toString())
        }


//        when ((activity as MainActivity).tempFilterProjectModel?.step) {
//
//            1 -> {
//                spinner_step_filter.selectedIndex = 1
//            }
//            2 -> {
//                spinner_step_filter.selectedIndex = 2
//            }
//            3 -> {
//                spinner_step_filter.selectedIndex = 3
//            }
//            4 -> {
//                spinner_step_filter.selectedIndex = 4
//            }
//            else -> {
//                spinner_step_filter.selectedIndex = 0
//            }
//        }

//        Log.i("hgjgjfkd",(activity as MainActivity).tempFilterProjectModel?.memberType.toString())
//        Log.i("sccdaca",(activity as MainActivity).tempFilterProjectModel?.Priority.toString())
//        Log.i("sccdaca",(activity as MainActivity).tempFilterProjectModel?.ProjectStatus.toString())
//        Log.i("sccdaca",(activity as MainActivity).tempFilterProjectModel?.FromMeeting.toString())

//        when (MainActivity.tempFilterProjectModel?.memberType) {
        when ( (activity as MainActivity).mytempFilterProjectModel?.memberType) {
            1 -> {
                spinner_user_role_filter.selectedIndex = 1
            }
            2 -> {
                spinner_user_role_filter.selectedIndex = 2
            }
            3 -> {
                spinner_user_role_filter.selectedIndex = 3
            }
            else -> {
                spinner_user_role_filter.selectedIndex = 0
            }

        }
        when ( (activity as MainActivity).mytempFilterProjectModel?.Priority) {
            1 -> {
                spinner_priority_filter.selectedIndex = 1
            }
            2 -> {
                spinner_priority_filter.selectedIndex = 2
            }
            3 -> {
                spinner_priority_filter.selectedIndex = 3
            }
            else -> {
                spinner_priority_filter.selectedIndex = 0
            }
        }
        when ( (activity as MainActivity).mytempFilterProjectModel?.ProjectStatus) {
            0 -> {
                spinner_status_filter.selectedIndex = 3
            }
            1 -> {
                spinner_status_filter.selectedIndex = 1
            }
            2 -> {
                spinner_status_filter.selectedIndex = 2
            }
            3 -> {
                spinner_status_filter.selectedIndex = 4
            }
            else -> {
//                spinner_status_filter.selectedIndex = 0
                spinner_status_filter.selectedIndex = 1
            }
        }
        when ( (activity as MainActivity).mytempFilterProjectModel?.FromMeeting) {
            0 -> {
                spinner_from_meeting_filter.selectedIndex = 1
            }
            1 -> {
                spinner_from_meeting_filter.selectedIndex = 2
            }
            else -> {
                spinner_from_meeting_filter.selectedIndex = 0
            }
        }
        Log.i("dvvdvgbnh",
            MainActivity.tempFilterProjectModel?.ProjectTypes.toString())
        Data?.add("همه")
        when (Counter_2) {
            0 -> {
                BaseActivity.projectTypes?.forEach {
                Data?.add(it.name.toString())
            }
            spinner_from_project_type.setItems(Data!!)
                Log.i("acsdvsdvbfb", "0")
                spinner_from_project_type.selectedIndex = 0
            }
            else -> {
                BaseActivity.projectTypes?.forEach {
                    Data?.add(it.name.toString())
                }
                spinner_from_project_type.setItems(Data!!)
                Log.i("acsdvsdvbfb", Counter_2.toString())
                spinner_from_project_type.selectedIndex = Counter_2
//                Log.i("qwqw","sa")
//                Log.i("qwqw",(activity as MainActivity).tempFilterProjectModel?.ProjectTypes.toString())
//                var ii=(activity as MainActivity).tempFilterProjectModel?.ProjectTypes
//                spinner_from_project_type.selectedIndex = BaseActivity.projectTypes?.get(ii!!)?.id!!
            }
        }
        if ( (activity as MainActivity).mytempFilterProjectModel?.DateType != -1) {
            txt_date_filter.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.colorPrimary,
                    null
                )
            )
            txt_date_filter.text = "فیلتر شده"
        }
        if (!MainActivity.Flag_Project)
        {
            btn_from_project_type.visibility=View.GONE
        }

//        if (MainActivity.Flag_Project&& Counter_2==0)
//        {
//            BaseActivity.projectTypes?.forEach {
//                Data?.add(it.name.toString())
//            }
//            spinner_from_project_type.setItems(Data!!)
//        }else if (MainActivity.Flag_Project&& Counter_2!=0){
//            btn_from_project_type.visibility=View.GONE
//        }else{
//            btn_from_project_type.visibility=View.GONE
//        }
        spinner_from_project_type.setOnItemSelectedListener { view, position, id, item ->
            if (position==0)
            {
                (activity as MainActivity).mytempFilterProjectModel?.ProjectTypes=null
                (activity as MainActivity).mytempFilterProjectModel?.ProjectTypes=null
                MainActivity.Counter_2=0
            }else{
                var s =BaseActivity.projectTypes?.get(position-1)?.id
                MainActivity.Counter_2=position
                var d=ArrayList<Int>()
                d.add(s!!)
                (activity as MainActivity).mytempFilterProjectModel?.ProjectTypes=d
            }


        }


//        spinner_step_filter.setOnItemSelectedListener { view, position, id, item ->
//            when (position) {
//                0 -> {
//                    (activity as MainActivity).tempFilterProjectModel?.step =
//                        -1
//                }
//                1 -> {
//                    (activity as MainActivity).tempFilterProjectModel?.step =
//                        1
//                }
//                2 -> {
//                    (activity as MainActivity).tempFilterProjectModel?.step =
//                        2
//                }
//                3 -> {
//                    (activity as MainActivity).tempFilterProjectModel?.step =
//                        3
//                }
//                4 -> {
//                    (activity as MainActivity).tempFilterProjectModel?.step =
//                        4
//                }
//            }
//        }

        spinner_user_role_filter.setOnItemSelectedListener { view, position, id, item ->
            when (position) {
                0 -> {
                    (activity as MainActivity).mytempFilterProjectModel?.memberType =
                        1
                    MainActivity.tempFilterProjectModel?.memberType =
                        -1
                }
                1 -> {
                    (activity as MainActivity).mytempFilterProjectModel?.memberType =
                        1
                    MainActivity.tempFilterProjectModel?.memberType =
                        1
                }
                2 -> {
                    (activity as MainActivity).mytempFilterProjectModel?.memberType =
                        2
                    MainActivity.tempFilterProjectModel?.memberType =
                        2
                }
                3 -> {
                    (activity as MainActivity).mytempFilterProjectModel?.memberType =
                        3
                    MainActivity.tempFilterProjectModel?.memberType =
                        3
                }
            }
        }
        spinner_priority_filter.setOnItemSelectedListener { view, position, id, item ->
            when (position) {
                0 -> {
                    (activity as MainActivity).mytempFilterProjectModel?.Priority = -1
                    -1
                    MainActivity.tempFilterProjectModel?.Priority = -1
                }
                1 -> {
                    (activity as MainActivity).mytempFilterProjectModel?.Priority = 1
                    MainActivity.tempFilterProjectModel?.Priority = 1
                }
                2 -> {
                    (activity as MainActivity).mytempFilterProjectModel?.Priority = 2
                    MainActivity.tempFilterProjectModel?.Priority = 2
                }
                3 -> {
                    (activity as MainActivity).mytempFilterProjectModel?.Priority = 3
                    MainActivity.tempFilterProjectModel?.Priority = 3
                }
            }
        }
        spinner_status_filter.setOnItemSelectedListener { view, position, id, item ->
            when (position) {
                0 -> {
                    (activity as MainActivity).mytempFilterProjectModel?.ProjectStatus =
                       1
                    MainActivity.tempFilterProjectModel?.ProjectStatus =
                      1
                }
                1 -> {
                    (activity as MainActivity).mytempFilterProjectModel?.ProjectStatus =
                        1
                    MainActivity.tempFilterProjectModel?.ProjectStatus =
                        1
                }
                2 -> {
                    (activity as MainActivity).mytempFilterProjectModel?.ProjectStatus =
                        2
                    MainActivity.tempFilterProjectModel?.ProjectStatus =
                        2
                }
                3 -> {
                    (activity as MainActivity).mytempFilterProjectModel?.ProjectStatus =
                        0
                    MainActivity.tempFilterProjectModel?.ProjectStatus =
                        0
                }
                4 -> {
                    (activity as MainActivity).mytempFilterProjectModel?.ProjectStatus =
                        3
                    MainActivity.tempFilterProjectModel?.ProjectStatus =
                        3
                }
            }
        }
        spinner_from_meeting_filter.setOnItemSelectedListener { view, position, id, item ->
            when (position) {
                0 -> {
                    (activity as MainActivity).mytempFilterProjectModel?.FromMeeting =
                        -1
                    MainActivity.tempFilterProjectModel?.FromMeeting =
                        -1
                }
                1 -> {
                    (activity as MainActivity).mytempFilterProjectModel?.FromMeeting = 1
                    MainActivity.tempFilterProjectModel?.FromMeeting = 1
                }
                2 -> {
                    (activity as MainActivity).mytempFilterProjectModel?.FromMeeting = 0
                    MainActivity.tempFilterProjectModel?.FromMeeting = 0
                }
            }
        }
        btn_submit.setOnClickListener {

            Log.i("sccdaca_2", (activity as MainActivity).mytempFilterProjectModel.toString())

//            mytempFilterProjectModel=MainActivity.tempFilterProjectModel

//            // TODO Asli
//            (activity as MainActivity).selectedFilterProjectModel = Gson().fromJson(
//                Gson().toJson((activity as MainActivity).tempFilterProjectModel), object : TypeToken<FilterProjectModel>() {}.type
//            )
//             // TODO Asli


//            (activity as MainActivity).selectedFilterProjectModel = Gson().fromJson(
//                Gson().toJson((activity as MainActivity).tempFilterProjectModel), object : TypeToken<FilterProjectModel>() {}.type
//            )
//            if (mytempFilterProjectModel==null)
//            {
//                 Log.i("dsvssacaca","A")
            MainActivity.selectedFilterProjectModel = Gson().fromJson(
                    Gson().toJson((activity as MainActivity).mytempFilterProjectModel), object : TypeToken<FilterProjectModel>() {}.type
                )
//            }else{
//                Log.i("dsvssacaca","B")
//                (activity as MainActivity).selectedFilterProjectModel = Gson().fromJson(
//                    Gson().toJson(mytempFilterProjectModel), object : TypeToken<FilterProjectModel>() {}.type
//                )
//            }

            Log.i("sudgayfaf",MainActivity.tempFilterProjectModel?.memberType.toString())

//            (activity as MainActivity).selectedFilterProjectModel = Gson().fromJson(
//                Gson().toJson(mytempFilterProjectModel), object : TypeToken<FilterProjectModel>() {}.type
//            )



//            (activity as MainActivity).selectedFilterProjectModel = Gson().fromJson(
//                Gson().toJson(mytempFilterProjectModel), object : TypeToken<FilterProjectModel>() {}.type
//            )






//            (activity as MainActivity).selectedFilterProjectModel=(activity as MainActivity).tempFilterProjectModel

//            Log.i("sccdaca_3",(activity as MainActivity).selectedFilterProjectModel?.ProjectStatus.toString())
//            Log.i("sccdaca_3",(activity as MainActivity).selectedFilterProjectModel?.ProjectTypes.toString())
//            Log.i("sccdaca_3",(activity as MainActivity).selectedFilterProjectModel?.actionStatusPending.toString())


            (activity as MainActivity).jobsFragment.getJobsFirstPage()
            (parentFragment as FilterTasksBottomSheet).DismissBottomSheet()
        }
        btn_clear_filter.setOnClickListener {
            clearFilter()
        }
    }
    fun clearFilter() {
        val searchText = MainActivity.selectedFilterProjectModel?.Title
        (activity as MainActivity).mytempFilterProjectModel=FilterProjectModel()
        MainActivity.selectedFilterProjectModel = FilterProjectModel()
//        (activity as MainActivity).selectedFilterProjectModel?.actionStatusPending!=false
        MainActivity.selectedFilterProjectModel?.Title = searchText
        MainActivity.selectedFilterProjectModel?.ProjectTypes = null
        MainActivity.Counter_2=0
        (activity as MainActivity).jobsFragment.getJobsFirstPage()
        (parentFragment as FilterTasksBottomSheet).dismiss()

    }
    fun clearFilter_2() {
        val searchText = MainActivity.selectedFilterProjectModel?.Title
        (activity as MainActivity).mytempFilterProjectModel=FilterProjectModel()
        MainActivity.selectedFilterProjectModel = FilterProjectModel()
//        (activity as MainActivity).selectedFilterProjectModel?.actionStatusPending!=false
        MainActivity.selectedFilterProjectModel?.Title = searchText
        MainActivity.selectedFilterProjectModel?.ProjectTypes = null
        MainActivity.Counter_2=0
        (activity as MainActivity).jobsFragment.getJobsFirstPage()
//        (parentFragment as FilterTasksBottomSheet).dismiss()

    }
}
