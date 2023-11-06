package ir.adak.Vect.UI.Fragments


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog
import ir.adak.Vect.Data.Models.ScheduledProjectModel

import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.AddScheduledJobActivity.AddScheduledJobActivity
import ir.adak.Vect.UI.Dialogs.WheelDialog
import ir.adak.Vect.Utils.SolarCalendar
import kotlinx.android.synthetic.main.activity_add_scheduled_job.*
import kotlinx.android.synthetic.main.fragment_schedule_task_fragment.*

/**
 * A simple [Fragment] subclass.
 */
class ScheduleTaskFragment : BaseFragment() {
    var jobForEdit: ScheduledProjectModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_schedule_task_fragment, container, false)
        view.rotationY = 180f
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jobForEdit = arguments?.getParcelable("job")
        fillSpinners()
        btn_specific_day_tab.setOnClickListener {
            selectSpecificDayTab()
        }
        btn_daily_tab.setOnClickListener {
            selectDailyTab()
        }
        btn_weekly_tab.setOnClickListener {
            selectWeeklyTab()
        }
        btn_monthly_tab.setOnClickListener {
            selectMonthlyTab()
        }


        setDatePickers(
            SolarCalendar.currentShamsidate
        )
        ConfigEndDateSpinner()
        ConfigWeekDaysSpinner()
        btn_time.setOnClickListener {
            val timePicker =
                TimePickerDialog.newInstance(
                    { view, hourOfDay, minute ->
                        (activity as AddScheduledJobActivity).SchedulerSelectedHour =
                            hourOfDay.toString()
                        (activity as AddScheduledJobActivity).SchedulerSelectedMinute =
                            minute.toString()
                        (activity as AddScheduledJobActivity).SchedulerHour = hourOfDay
                        (activity as AddScheduledJobActivity).SchedulerMinute = minute
                        (activity as AddScheduledJobActivity).SchedulerSelectedTime =
                            "${String.format(
                                "%02d",
                                (activity as AddScheduledJobActivity).SchedulerSelectedHour.toInt()
                            )}:${String.format(
                                "%02d",
                                (activity as AddScheduledJobActivity).SchedulerSelectedMinute.toInt()
                            )}"

                        txt_time.text = (activity as AddScheduledJobActivity).SchedulerSelectedTime
                    }, (activity as AddScheduledJobActivity).SchedulerHour,
                    (activity as AddScheduledJobActivity).SchedulerMinute,
                    true
                )
            timePicker.show(childFragmentManager, "")
        }
        btn_start_date.setOnClickListener {
            if ((activity as AddScheduledJobActivity).SchedulerStartDate.isNotEmpty()) {
                (activity as AddScheduledJobActivity).SchedulerDate =
                    (activity as AddScheduledJobActivity).SchedulerStartDate
            }

            val datePickerDialog = DatePickerDialog.newInstance(
                { _, year, monthOfYear, dayOfMonth ->
                    (activity as AddScheduledJobActivity).SchedulerY = year
                    (activity as AddScheduledJobActivity).SchedulerM = monthOfYear + 1
                    (activity as AddScheduledJobActivity).SchedulerD = dayOfMonth

                    (activity as AddScheduledJobActivity).SchedulerStartDate =
                        "$year/${convertTime((activity as AddScheduledJobActivity).SchedulerM)}/${convertTime(
                            dayOfMonth
                        )}"

                    txt_start_date.text =
                        dayOfMonth.toString() + " " + numToMonth((activity as AddScheduledJobActivity).SchedulerM) + " " + year

                    (activity as AddScheduledJobActivity).SchedulerSelectedStartDate =
                        (activity as AddScheduledJobActivity).SchedulerStartDate

                },
                Integer.parseInt((activity as AddScheduledJobActivity).SchedulerDate.split("/")[0]),
                Integer.parseInt((activity as AddScheduledJobActivity).SchedulerDate.split("/")[1]) - 1,
                Integer.parseInt((activity as AddScheduledJobActivity).SchedulerDate.split("/")[2])
            )

            datePickerDialog.show(childFragmentManager, "Datepickerdialog")

        }
        Log.i("adfgh", jobForEdit?.schedulerType.toString())
        if (jobForEdit != null && jobForEdit?.schedulerType != null) {
            Log.i("Type","W")
            when (jobForEdit?.schedulerType) {
                1 -> {
                    Log.i("Typeefefetrw","1")
                    selectSpecificDayTab(true)
                }
                2 -> {
                    Log.i("Typeefefetrw","2")
                    selectDailyTab(true)
                }
                3 -> {
                    Log.i("Typeefefetrw","3")
                    selectWeeklyTab(true)
                }
                4 -> {
                    Log.i("Typeefefetrw","4")
                    selectMonthlyTab(true)
                }
            }
        } else
            Log.i("Typeefefetrw","e")
        selectSpecificDayTab()


        btn_continue.setOnClickListener {

            if (edt_end_date.text.isNotBlank()) {
                (activity as AddScheduledJobActivity).endProjectNumber =
                    edt_end_date.text.toString().toInt()
            } else {
                (activity as AddScheduledJobActivity).endProjectNumber = 0
            }

            when ((activity as AddScheduledJobActivity).selectedTab) {
                AddScheduledJobActivity.SelectedTab.SPECIFIC_DAY -> {
                    if ((activity as AddScheduledJobActivity).SchedulerSelectedTime.isEmpty()
                        || (activity as AddScheduledJobActivity).SchedulerSelectedStartDate.isEmpty()
                        || (((activity as AddScheduledJobActivity).selectedEndDateType == AddScheduledJobActivity.ProjectSchedulerEndDateType.ToDay
                                || (activity as AddScheduledJobActivity).selectedEndDateType == AddScheduledJobActivity.ProjectSchedulerEndDateType.ToMonth)
                                && (activity as AddScheduledJobActivity).endProjectNumber <= 0)
                        || (activity as AddScheduledJobActivity).selectedEndDateType == AddScheduledJobActivity.ProjectSchedulerEndDateType.Unspecified
                    ) {
                        return@setOnClickListener
                    }

                }
                AddScheduledJobActivity.SelectedTab.DAILY -> {
                    if ((activity as AddScheduledJobActivity).SchedulerSelectedTime.isEmpty()
                        || (((activity as AddScheduledJobActivity).selectedEndDateType == AddScheduledJobActivity.ProjectSchedulerEndDateType.ToDay
                                || (activity as AddScheduledJobActivity).selectedEndDateType == AddScheduledJobActivity.ProjectSchedulerEndDateType.ToMonth)
                                && (activity as AddScheduledJobActivity).endProjectNumber <= 0)
                        || (activity as AddScheduledJobActivity).selectedEndDateType == AddScheduledJobActivity.ProjectSchedulerEndDateType.Unspecified
                    ) {
                        return@setOnClickListener
                    }
                }
                AddScheduledJobActivity.SelectedTab.WEEKLY -> {
                    if ((activity as AddScheduledJobActivity).SchedulerSelectedTime.isEmpty()
                        || (activity as AddScheduledJobActivity).selectedDayOfWeek.value < 0
                        || (((activity as AddScheduledJobActivity).selectedEndDateType == AddScheduledJobActivity.ProjectSchedulerEndDateType.ToDay
                                || (activity as AddScheduledJobActivity).selectedEndDateType == AddScheduledJobActivity.ProjectSchedulerEndDateType.ToMonth)
                                && (activity as AddScheduledJobActivity).endProjectNumber <= 0)
                        || (activity as AddScheduledJobActivity).selectedEndDateType == AddScheduledJobActivity.ProjectSchedulerEndDateType.Unspecified

                    ) {
                        return@setOnClickListener
                    }
                }
                AddScheduledJobActivity.SelectedTab.MONTHLY -> {
                    if ((activity as AddScheduledJobActivity).SchedulerSelectedTime.isEmpty()
                        || (activity as AddScheduledJobActivity).currentSelectedDay <= 0
                        || (((activity as AddScheduledJobActivity).selectedEndDateType == AddScheduledJobActivity.ProjectSchedulerEndDateType.ToDay
                                || (activity as AddScheduledJobActivity).selectedEndDateType == AddScheduledJobActivity.ProjectSchedulerEndDateType.ToMonth)
                                && (activity as AddScheduledJobActivity).endProjectNumber <= 0)
                        || (activity as AddScheduledJobActivity).selectedEndDateType == AddScheduledJobActivity.ProjectSchedulerEndDateType.Unspecified

                    ) {
                        return@setOnClickListener
                    }
                }
            }


            (activity as AddScheduledJobActivity).add_scheduled_task_viewpager.currentItem = 1
        }
    }

    private fun fillSpinners() {
        spinner_end_date_type.setItems("نوع تاریخ پایان", "آخر سال", "مدت به روز", "مدت به ماه")
        spinner_week_days.setItems(
            "روزهای هفته",
            "شنبه",
            "یکشنبه",
            "دوشنبه",
            "سه شنبه",
            "چهارشنبه",
            "پنجشنبه",
            "جمعه"
        )
    }

    fun clearAll() {
        btn_start_date.visibility = View.GONE
        month_days_layout.visibility = View.GONE
        week_days_layout.visibility = View.GONE
        end_date_layout.visibility = View.GONE
        edt_end_date.visibility = View.GONE
        (activity as AddScheduledJobActivity).currentSelectedDay = 0
        (activity as AddScheduledJobActivity).SchedulerHour = 0
        (activity as AddScheduledJobActivity).SchedulerMinute = 0
        Log.i("svvvvvvvvvvvvvvvsw","fd"+jobForEdit?.startTime)

        (activity as AddScheduledJobActivity).SchedulerSelectedHour = ""
        (activity as AddScheduledJobActivity).SchedulerSelectedMinute = ""
        (activity as AddScheduledJobActivity).SchedulerSelectedTime = ""
        (activity as AddScheduledJobActivity).selectedEndDateType =
            AddScheduledJobActivity.ProjectSchedulerEndDateType.Unspecified
        (activity as AddScheduledJobActivity).selectedDayOfWeek =
            AddScheduledJobActivity.DayOfWeekEnum.Unspecified
        (activity as AddScheduledJobActivity).endProjectNumber = 0
        setDatePickers(SolarCalendar.currentShamsidate)
        Log.i("svvvvvvvvvvvvvvvsw","fd"+jobForEdit?.startTime)
        txt_time.text = "ساعت"
        txt_start_date.text = "تاریخ شروع"
        txt_month_days.text = "روزهای ماه"
        edt_end_date.setText("")
        Log.i("svvvvvvvvvvvvvvvsw","fd"+jobForEdit?.startTime)
        spinner_week_days.selectedIndex = 0
        spinner_end_date_type.selectedIndex = 0

    }

    private fun selectDailyTab(firstLaunch: Boolean = false) {
        if ((activity as AddScheduledJobActivity).selectedTab != AddScheduledJobActivity.SelectedTab.DAILY) {
            (activity as AddScheduledJobActivity).selectedTab =
                AddScheduledJobActivity.SelectedTab.DAILY
            unSelectAllTabs()
            btn_daily_tab.setBackgroundResource(R.drawable.selected_tab_background_shape)
            btn_daily_tab.setTextColor(Color.WHITE)
            clearAll()
            end_date_layout.visibility = View.VISIBLE
//            if (firstLaunch) {
            if (jobForEdit != null && jobForEdit?.startTime != null) {
                (activity as AddScheduledJobActivity).SchedulerHour =
                    jobForEdit?.startTime!!.split(":")[0].toInt()

                (activity as AddScheduledJobActivity).SchedulerMinute =
                    jobForEdit?.startTime!!.split(":")[1].toInt()

                (activity as AddScheduledJobActivity).SchedulerSelectedTime =
                    "${String.format(
                        "%02d",
                        (activity as AddScheduledJobActivity).SchedulerHour
                    )}:${String.format(
                        "%02d",
                        (activity as AddScheduledJobActivity).SchedulerMinute
                    )}"

                txt_time.text = (activity as AddScheduledJobActivity).SchedulerSelectedTime

            }
            if (jobForEdit != null && jobForEdit?.endDateType != null) {
                spinner_end_date_type.selectedIndex = jobForEdit?.endDateType!!
                configureEndSpinner(spinner_end_date_type.selectedIndex)
                if (jobForEdit?.endProjectNumber != null)
                    edt_end_date.setText(jobForEdit?.endProjectNumber.toString())
            }
//            }

        }
    }
    private fun selectSpecificDayTab(firstLaunch: Boolean = false) {
        Log.i("ddjvsbjlbv","ddd4")
//        if ((activity as AddScheduledJobActivity).selectedTab
//            != AddScheduledJobActivity.SelectedTab.SPECIFIC_DAY)
            (activity as AddScheduledJobActivity).selectedTab =
                AddScheduledJobActivity.SelectedTab.SPECIFIC_DAY
            Log.i("ddjvsbjlbv","ddd2")
            unSelectAllTabs()
            clearAll()
            Log.i("ddjvsbjlbv","ddd")
            btn_specific_day_tab.setBackgroundResource(R.drawable.selected_tab_background_shape)
            btn_specific_day_tab.setTextColor(Color.WHITE)
            btn_start_date.visibility = View.VISIBLE
            end_date_layout.visibility = View.VISIBLE
//            if (firstLaunch) {

            if (jobForEdit != null && jobForEdit?.startTime!=null) {
                (activity as AddScheduledJobActivity).SchedulerHour =
                    jobForEdit?.startTime!!.split(":")[0].toInt()

                (activity as AddScheduledJobActivity).SchedulerMinute =
                    jobForEdit?.startTime!!.split(":")[1].toInt()

                (activity as AddScheduledJobActivity).SchedulerSelectedTime =
                    "${String.format(
                        "%02d",
                        (activity as AddScheduledJobActivity).SchedulerHour
                    )}:${String.format(
                        "%02d",
                        (activity as AddScheduledJobActivity).SchedulerMinute
                    )}"
                txt_time.text = (activity as AddScheduledJobActivity).SchedulerSelectedTime
            }


            if (jobForEdit != null && !jobForEdit?.persianStartDate.isNullOrEmpty()) {
                (activity as AddScheduledJobActivity).SchedulerStartDate =
                    "${jobForEdit?.persianStartDate!!.split("/")[0]}/${convertTime(
                        jobForEdit?.persianStartDate!!.split(
                            "/"
                        )[1].toInt()
                    )}/${convertTime(
                        jobForEdit?.persianStartDate!!.split("/")[2].toInt()
                    )}"
                txt_start_date.text =
                    jobForEdit?.persianStartDate!!.split("/")[2] + " " + numToMonth(
                        jobForEdit?.persianStartDate!!.split("/")[1].toInt()
                    ) + " " + jobForEdit?.persianStartDate!!.split("/")[0]

                (activity as AddScheduledJobActivity).SchedulerSelectedStartDate =
                    (activity as AddScheduledJobActivity).SchedulerStartDate
            }
            if (jobForEdit != null && jobForEdit?.endDateType != null) {
                spinner_end_date_type.selectedIndex = jobForEdit?.endDateType!!
                configureEndSpinner(spinner_end_date_type.selectedIndex)
                if (jobForEdit?.endProjectNumber != null)
                    edt_end_date.setText(jobForEdit?.endProjectNumber.toString())
            }
//            }


        Log.i("NIVMDMORADI","fd"+jobForEdit?.startTime+"NIMA")
    }

    private fun selectWeeklyTab(firstLaunch: Boolean = false) {
        if ((activity as AddScheduledJobActivity).selectedTab != AddScheduledJobActivity.SelectedTab.WEEKLY) {
            (activity as AddScheduledJobActivity).selectedTab =
                AddScheduledJobActivity.SelectedTab.WEEKLY
            unSelectAllTabs()
            btn_weekly_tab.setBackgroundResource(R.drawable.selected_tab_background_shape)
            btn_weekly_tab.setTextColor(Color.WHITE)
            clearAll()
            week_days_layout.visibility = View.VISIBLE
            end_date_layout.visibility = View.VISIBLE
            if (jobForEdit != null && jobForEdit?.startTime != null) {
                (activity as AddScheduledJobActivity).SchedulerHour =
                    jobForEdit?.startTime!!.split(":")[0].toInt()

                (activity as AddScheduledJobActivity).SchedulerMinute =
                    jobForEdit?.startTime!!.split(":")[1].toInt()

                (activity as AddScheduledJobActivity).SchedulerSelectedTime =
                    "${String.format(
                        "%02d",
                        (activity as AddScheduledJobActivity).SchedulerHour
                    )}:${String.format(
                        "%02d",
                        (activity as AddScheduledJobActivity).SchedulerMinute
                    )}"

                txt_time.text = (activity as AddScheduledJobActivity).SchedulerSelectedTime

            }
            if (jobForEdit != null && jobForEdit?.endDateType != null) {
                spinner_end_date_type.selectedIndex = jobForEdit?.endDateType!!
                configureEndSpinner(spinner_end_date_type.selectedIndex)
                if (jobForEdit?.endProjectNumber != null)
                    edt_end_date.setText(jobForEdit?.endProjectNumber.toString())
            }
            if (jobForEdit != null && !jobForEdit?.daysOfWeekOrMonth.isNullOrEmpty()
                && jobForEdit?.schedulerType == 3
            ) {

                when (jobForEdit?.daysOfWeekOrMonth!![0]) {

                    0 -> {
                        (activity as AddScheduledJobActivity).selectedDayOfWeek =
                            AddScheduledJobActivity.DayOfWeekEnum.Sunday
                        spinner_week_days.selectedIndex = 2
                    }
                    1 -> {
                        (activity as AddScheduledJobActivity).selectedDayOfWeek =
                            AddScheduledJobActivity.DayOfWeekEnum.Monday
                        spinner_week_days.selectedIndex = 3
                    }
                    2 -> {
                        (activity as AddScheduledJobActivity).selectedDayOfWeek =
                            AddScheduledJobActivity.DayOfWeekEnum.Tuesday
                        spinner_week_days.selectedIndex = 4
                    }
                    3 -> {
                        (activity as AddScheduledJobActivity).selectedDayOfWeek =
                            AddScheduledJobActivity.DayOfWeekEnum.Wednesday
                        spinner_week_days.selectedIndex = 5
                    }
                    4 -> {
                        (activity as AddScheduledJobActivity).selectedDayOfWeek =
                            AddScheduledJobActivity.DayOfWeekEnum.Thursday
                        spinner_week_days.selectedIndex = 6
                    }
                    5 -> {
                        (activity as AddScheduledJobActivity).selectedDayOfWeek =
                            AddScheduledJobActivity.DayOfWeekEnum.Friday
                        spinner_week_days.selectedIndex = 7
                    }
                    6 -> {
                        (activity as AddScheduledJobActivity).selectedDayOfWeek =
                            AddScheduledJobActivity.DayOfWeekEnum.Saturday
                        spinner_week_days.selectedIndex = 1
                    }
                }

            }

        }
    }

    private fun selectMonthlyTab(firstLaunch: Boolean = false) {
        if ((activity as AddScheduledJobActivity).selectedTab != AddScheduledJobActivity.SelectedTab.MONTHLY) {
            (activity as AddScheduledJobActivity).selectedTab =
                AddScheduledJobActivity.SelectedTab.MONTHLY
            unSelectAllTabs()
            clearAll()
            btn_monthly_tab.setBackgroundResource(R.drawable.selected_tab_background_shape)
            btn_monthly_tab.setTextColor(Color.WHITE)
            month_days_layout.visibility = View.VISIBLE
            month_days_layout.setOnClickListener {
                val wheelDialog =
                    WheelDialog(
                        (activity as AddScheduledJobActivity).currentSelectedDay,
                        object : WheelDialog.OnSubmitListener {
                            override fun OnSubmit(selectedDay: Int) {
                                (activity as AddScheduledJobActivity).currentSelectedDay =
                                    selectedDay
                                txt_month_days.text = "$selectedDay"
                            }
                        })
                wheelDialog.show(childFragmentManager, "")
            }
            end_date_layout.visibility = View.VISIBLE
//            if (firstLaunch) {
            if (jobForEdit != null && jobForEdit?.startTime != null) {
                (activity as AddScheduledJobActivity).SchedulerHour =
                    jobForEdit?.startTime!!.split(":")[0].toInt()

                (activity as AddScheduledJobActivity).SchedulerMinute =
                    jobForEdit?.startTime!!.split(":")[1].toInt()

                (activity as AddScheduledJobActivity).SchedulerSelectedTime =
                    "${String.format(
                        "%02d",
                        (activity as AddScheduledJobActivity).SchedulerHour
                    )}:${String.format(
                        "%02d",
                        (activity as AddScheduledJobActivity).SchedulerMinute
                    )}"

                txt_time.text = (activity as AddScheduledJobActivity).SchedulerSelectedTime

            }
            if (jobForEdit != null && jobForEdit?.endDateType != null) {
                spinner_end_date_type.selectedIndex = jobForEdit?.endDateType!!
                configureEndSpinner(spinner_end_date_type.selectedIndex)
                if (jobForEdit?.endProjectNumber != null)
                    edt_end_date.setText(jobForEdit?.endProjectNumber.toString())
            }
            if (jobForEdit != null && !jobForEdit?.daysOfWeekOrMonth.isNullOrEmpty()
                && jobForEdit?.schedulerType == 4
            ) {
                (activity as AddScheduledJobActivity).currentSelectedDay =
                    jobForEdit?.daysOfWeekOrMonth!![0]
                txt_month_days.text = "${jobForEdit?.daysOfWeekOrMonth!![0]}"
            }
//            }


        }
    }

    fun unSelectAllTabs() {
        Log.i("NIVMDMORADI","TEA1")
        btn_specific_day_tab.setBackgroundResource(R.drawable.unselected_tab_background_shape)
        btn_daily_tab.setBackgroundResource(R.drawable.unselected_tab_background_shape)
        btn_weekly_tab.setBackgroundResource(R.drawable.unselected_tab_background_shape)
        btn_monthly_tab.setBackgroundResource(R.drawable.unselected_tab_background_shape)
        btn_specific_day_tab.setTextColor(Color.parseColor("#A9A9A9"))
        Log.i("NIVMDMORADI","TEA2")
        btn_daily_tab.setTextColor(Color.parseColor("#A9A9A9"))
        btn_weekly_tab.setTextColor(Color.parseColor("#A9A9A9"))
        btn_monthly_tab.setTextColor(Color.parseColor("#A9A9A9"))
        Log.i("NIVMDMORADI","TEA3")
    }

    fun configureEndSpinner(position: Int) {
        when (position) {
            0 -> {
                edt_end_date.visibility = View.GONE
                (activity as AddScheduledJobActivity).selectedEndDateType =
                    AddScheduledJobActivity.ProjectSchedulerEndDateType.Unspecified
                edt_end_date.setText("")
            }
            1 -> {
                edt_end_date.visibility = View.GONE
                (activity as AddScheduledJobActivity).selectedEndDateType =
                    AddScheduledJobActivity.ProjectSchedulerEndDateType.LastYear
                edt_end_date.setText("")
            }
            2 -> {
                edt_end_date.visibility = View.VISIBLE
                edt_end_date.hint = "مدت به روز"
                (activity as AddScheduledJobActivity).selectedEndDateType =
                    AddScheduledJobActivity.ProjectSchedulerEndDateType.ToDay
            }
            3 -> {
                edt_end_date.visibility = View.VISIBLE
                edt_end_date.hint = "مدت به ماه"
                (activity as AddScheduledJobActivity).selectedEndDateType =
                    AddScheduledJobActivity.ProjectSchedulerEndDateType.ToMonth
            }
        }
    }

    fun ConfigEndDateSpinner() {

        spinner_end_date_type.setOnItemSelectedListener { view, position, id, item ->
            configureEndSpinner(position)
        }
    }

    fun ConfigWeekDaysSpinner() {

        spinner_week_days.setOnItemSelectedListener { view, position, id, item ->
            when (position) {
                0 -> {
                    (activity as AddScheduledJobActivity).selectedDayOfWeek =
                        AddScheduledJobActivity.DayOfWeekEnum.Unspecified

                }
                1 -> {
                    (activity as AddScheduledJobActivity).selectedDayOfWeek =
                        AddScheduledJobActivity.DayOfWeekEnum.Saturday
                }
                2 -> {
                    (activity as AddScheduledJobActivity).selectedDayOfWeek =
                        AddScheduledJobActivity.DayOfWeekEnum.Sunday
                }
                3 -> {
                    (activity as AddScheduledJobActivity).selectedDayOfWeek =
                        AddScheduledJobActivity.DayOfWeekEnum.Monday
                }
                4 -> {
                    (activity as AddScheduledJobActivity).selectedDayOfWeek =
                        AddScheduledJobActivity.DayOfWeekEnum.Tuesday
                }
                5 -> {
                    (activity as AddScheduledJobActivity).selectedDayOfWeek =
                        AddScheduledJobActivity.DayOfWeekEnum.Wednesday
                }
                6 -> {
                    (activity as AddScheduledJobActivity).selectedDayOfWeek =
                        AddScheduledJobActivity.DayOfWeekEnum.Thursday
                }
                7 -> {
                    (activity as AddScheduledJobActivity).selectedDayOfWeek =
                        AddScheduledJobActivity.DayOfWeekEnum.Friday
                }
            }

        }
    }

    private fun setDatePickers(
        startDatee: String?
    ) {

        if (startDatee != null) {
            (activity as AddScheduledJobActivity).SchedulerY =
                Integer.parseInt(startDatee.split("/")[0])
            (activity as AddScheduledJobActivity).SchedulerM =
                Integer.parseInt(startDatee.split("/")[1])
            (activity as AddScheduledJobActivity).SchedulerD =
                Integer.parseInt(startDatee.split("/")[2])
            (activity as AddScheduledJobActivity).SchedulerStartDate =
                "${(activity as AddScheduledJobActivity).SchedulerY}/${convertTime((activity as AddScheduledJobActivity).SchedulerM)}/${convertTime(
                    (activity as AddScheduledJobActivity).SchedulerD
                )}"
            (activity as AddScheduledJobActivity).SchedulerStartDate =
                "${(activity as AddScheduledJobActivity).SchedulerY}/${convertTime((activity as AddScheduledJobActivity).SchedulerM)}/${convertTime(
                    (activity as AddScheduledJobActivity).SchedulerD
                )}"
        }
    }

}
