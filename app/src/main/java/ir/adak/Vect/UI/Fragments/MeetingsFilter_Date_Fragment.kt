package ir.adak.Vect.UI.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ir.adak.Vect.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog
import ir.adak.Vect.Data.Models.FilterMeetingModel
import ir.adak.Vect.UI.Activities.MainActivity.MainActivity
import ir.adak.Vect.UI.Dialogs.FilterMeetingsBottomSheet
import ir.adak.Vect.Utils.SolarCalendar
import kotlinx.android.synthetic.main.fragment_meetings_filter_date.*


/**
 * A simple [Fragment] subclass.
 */
class MeetingsFilter_Date_Fragment : BaseFragment() {

    var tempFilterMeetingModel: FilterMeetingModel? = null


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
        return inflater.inflate(R.layout.fragment_meetings_filter_date, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tempFilterMeetingModel = Gson().fromJson(
            Gson().toJson((activity as MainActivity).tempFilterMeetingModel),
            object : TypeToken<FilterMeetingModel>() {}.type
        )
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
                    tempFilterMeetingModel?.DateFrom =
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

                    tempFilterMeetingModel?.DateTo =
                        selectedEndDate

                },
                Integer.parseInt(date.split("/")[0]),
                Integer.parseInt(date.split("/")[1]) - 1,
                Integer.parseInt(date.split("/")[2])
            )

            datePickerDialog.show(childFragmentManager, "Datepickerdialog")

        }

        if ((activity as MainActivity).tempFilterMeetingModel?.DateFrom != null
            ||  (activity as MainActivity).tempFilterMeetingModel?.DateTo != null){

            setDatePickers(
                tempFilterMeetingModel?.DateFrom,
                tempFilterMeetingModel?.DateTo
            )


            if ((activity as MainActivity).tempFilterMeetingModel?.DateFrom != null)
                txt_start_date.text = d1.toString() + " " + numToMonth(m1) + " " + y1
            if ((activity as MainActivity).tempFilterMeetingModel?.DateTo != null)
                txt_end_date.text = d2.toString() + " " + numToMonth(m2) + " " + y2


        }




        btn_submit.setOnClickListener {
            (activity as MainActivity).tempFilterMeetingModel = Gson().fromJson(
                Gson().toJson(tempFilterMeetingModel),
                object : TypeToken<FilterMeetingModel>() {}.type
            )
            (parentFragment as FilterMeetingsBottomSheet).childFragmentManager.popBackStack()
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
