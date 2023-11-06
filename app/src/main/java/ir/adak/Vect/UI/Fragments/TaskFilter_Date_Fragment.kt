package ir.adak.Vect.UI.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.adak.Vect.Data.Models.FilterProjectModel

import ir.adak.Vect.R
import kotlinx.android.synthetic.main.fragment_task_filter_date.*
import android.text.Editable
import android.text.TextWatcher
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog
import ir.adak.Vect.UI.Activities.MainActivity.MainActivity
import ir.adak.Vect.UI.Dialogs.FilterTasksBottomSheet
import ir.adak.Vect.Utils.SolarCalendar


/**
 * A simple [Fragment] subclass.
 */
class TaskFilter_Date_Fragment : BaseFragment() {

    var tempFilterProjectModel: FilterProjectModel? = null


    private var y1: Int = 0
    private var m1: Int = 0
    private var d1: Int = 0
    private var y2: Int = 0
    private var m2: Int = 0
    private var d2: Int = 0

    private var startDate: String = ""
    private var selectedStartDate: String = ""
    private var endDate: String = ""
    private var selectedEndDate: String = ""
    private var date: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_filter_date, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tempFilterProjectModel = Gson().fromJson(
            Gson().toJson(MainActivity.tempFilterProjectModel),
            object : TypeToken<FilterProjectModel>() {}.type
        )
        spinner_date_type_filter.setItems(
            "هیچکدام",
            "تاریخ شروع وظیفه",
            "تاریخ پایان وظیفه",
            "چند روز تا اتمام وظیفه",
            "چند روز گذشته از آخرین پیگیری"
        )


        spinner_date_type_filter.setOnItemSelectedListener { view, position, id, item ->
            when (position) {
                0 -> {
                    btn_start_date.visibility = View.GONE
                    btn_end_date.visibility = View.GONE
                    edt_days_count.visibility = View.GONE
                    tempFilterProjectModel?.DateType = -1
                }
                1 -> {
                    btn_start_date.visibility = View.VISIBLE
                    btn_end_date.visibility = View.VISIBLE
                    edt_days_count.visibility = View.GONE
                    tempFilterProjectModel?.DateType = 0
                    tempFilterProjectModel?.DateRangeType = 1
                }
                2 -> {
                    btn_start_date.visibility = View.VISIBLE
                    btn_end_date.visibility = View.VISIBLE
                    edt_days_count.visibility = View.GONE
                    tempFilterProjectModel?.DateType = 0
                    tempFilterProjectModel?.DateRangeType = 2
                }
                3 -> {
                    btn_start_date.visibility = View.GONE
                    btn_end_date.visibility = View.GONE
                    edt_days_count.visibility = View.VISIBLE
                    tempFilterProjectModel?.DateType = 1
                }
                4 -> {
                    btn_start_date.visibility = View.GONE
                    btn_end_date.visibility = View.GONE
                    edt_days_count.visibility = View.VISIBLE
                    tempFilterProjectModel?.DateType = 2
                }
            }
        }

        setDatePickers(
            SolarCalendar.currentShamsidate,
            SolarCalendar.currentShamsidate
        )


        btn_start_date.setOnClickListener {
            if (startDate.isNotEmpty()) {
                date = startDate
            }

            val datePickerDialog = DatePickerDialog.newInstance(
                { _, year, monthOfYear, dayOfMonth ->
                    y1 = year
                    m1 = monthOfYear + 1
                    d1 = dayOfMonth


                    startDate = "$year/${convertTime(m1)}/${convertTime(dayOfMonth)}"

                    txt_start_date.text = dayOfMonth.toString() + " " + numToMonth(m1) + " " + year

                    selectedStartDate = startDate
                    tempFilterProjectModel?.DateFrom =
                        selectedStartDate

                },
                Integer.parseInt(date.split("/")[0]),
                Integer.parseInt(date.split("/")[1]) - 1,
                Integer.parseInt(date.split("/")[2])
            )

            datePickerDialog.show(childFragmentManager, "Datepickerdialog")

        }

        btn_end_date.setOnClickListener {
            if (endDate.isNotEmpty()) {
                date = endDate
            }

            val datePickerDialog = DatePickerDialog.newInstance(
                { _, year, monthOfYear, dayOfMonth ->
                    y2 = year
                    m2 = monthOfYear + 1
                    d2 = dayOfMonth

                    val date_display =
                        dayOfMonth.toString() + " " + numToMonth(m2) + " " + year

                    txt_end_date.text = dayOfMonth.toString() + " " + numToMonth(m1) + " " + year
                    endDate = "$year/${convertTime(m2)}/${convertTime(dayOfMonth)}"

                    selectedEndDate = endDate

                    tempFilterProjectModel?.DateTo =
                        selectedEndDate

                },
                Integer.parseInt(date.split("/")[0]),
                Integer.parseInt(date.split("/")[1]) - 1,
                Integer.parseInt(date.split("/")[2])
            )

            datePickerDialog.show(childFragmentManager, "Datepickerdialog")

        }

        edt_days_count.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (!p0.isNullOrEmpty()) {
                    tempFilterProjectModel?.Day =
                        p0.toString().toInt()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        when (tempFilterProjectModel?.DateType) {
            -1 -> {
                spinner_date_type_filter.selectedIndex = 0
                btn_start_date.visibility = View.GONE
                btn_end_date.visibility = View.GONE
                edt_days_count.visibility = View.GONE
            }
            0 -> {

                when (tempFilterProjectModel?.DateRangeType) {
                    1 -> {
                        spinner_date_type_filter.selectedIndex = 1
                    }

                    2 -> {
                        spinner_date_type_filter.selectedIndex = 2
                    }
                }

                btn_start_date.visibility = View.VISIBLE
                btn_end_date.visibility = View.VISIBLE
                setDatePickers(
                    tempFilterProjectModel?.DateFrom,
                    tempFilterProjectModel?.DateTo
                )
                if (tempFilterProjectModel?.DateFrom != null)
                    txt_start_date.text = d1.toString() + " " + numToMonth(m1) + " " + y1
                if (tempFilterProjectModel?.DateTo != null)
                    txt_end_date.text = d2.toString() + " " + numToMonth(m2) + " " + y2

                edt_days_count.visibility = View.GONE
            }
            1 -> {
                spinner_date_type_filter.selectedIndex = 3
                btn_start_date.visibility = View.GONE
                btn_end_date.visibility = View.GONE
                edt_days_count.visibility = View.VISIBLE
                edt_days_count.setText(tempFilterProjectModel?.Day.toString())
            }
            2 -> {
                spinner_date_type_filter.selectedIndex = 4
                btn_start_date.visibility = View.GONE
                btn_end_date.visibility = View.GONE
                edt_days_count.visibility = View.VISIBLE
                edt_days_count.setText(tempFilterProjectModel?.Day.toString())
            }
        }

        btn_submit.setOnClickListener {
            MainActivity.tempFilterProjectModel = Gson().fromJson(
                Gson().toJson(tempFilterProjectModel),
                object : TypeToken<FilterProjectModel>() {}.type
            )
            (parentFragment as FilterTasksBottomSheet).childFragmentManager.popBackStack()
        }
    }

    private fun setDatePickers(
        startDatee: String?,
        endDatee: String?
    ) {
        if (startDatee != null) {
            y1 = Integer.parseInt(startDatee.split("/")[0])
            m1 = Integer.parseInt(startDatee.split("/")[1])
            d1 = Integer.parseInt(startDatee.split("/")[2])
            startDate = "$y1/${convertTime(m1)}/${convertTime(d1)}"
        }
        if (endDatee != null) {
            y2 = Integer.parseInt(endDatee.split("/")[0])
            m2 = Integer.parseInt(endDatee.split("/")[1])
            d2 = Integer.parseInt(endDatee.split("/")[2])
            endDate = "$y2/${convertTime(m2)}/${convertTime(d2)}"
        }

    }

}
