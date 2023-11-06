package ir.adak.Vect.UI.Activities.AddNoteActivity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog
import ir.adak.Vect.App
import ir.adak.Vect.Data.Models.Note
import ir.adak.Vect.Data.Models.RegisterNoteModel
import ir.adak.Vect.Data.Models.ReminderModel
import ir.adak.Vect.Listeners.OnLoginCompleted
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.BaseActivity
import ir.adak.Vect.Utils.SolarCalendar
import ir.adak.Vect.Utils.enqueue
import ir.adak.Vect.Utils.setAlarmManager
import kotlinx.android.synthetic.main.activity_add_note.*
import java.text.DecimalFormat

class AddNoteActivity : BaseActivity() {

    var f: DecimalFormat = DecimalFormat("00")

    private var y1: Int = 0
    private var m1: Int = 0
    private var d1: Int = 0

    var H1 = 0
    var M1 = 0
    private var reminderDate: String = ""
    private var selectedReminderHour: String = ""
    private var selectedReminderMinute: String = ""
    private var selectedReminderDate: String = ""
    private var selectedReminderTime: String = ""
    private var date: String = ""

    var isTitleValid = false
    var isDescriptionValid = false

    var note: Note? = null
    var isEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        note = intent.extras?.getParcelable("note")
        isEdit = intent.extras?.getBoolean("isEdit") ?: false

        setDatePickers(SolarCalendar.currentShamsidate, "00:00")

        edt_title.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                isTitleValid = p0.toString().isNotEmpty()

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
        edt_description.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                isDescriptionValid = p0.toString().isNotEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        btn_submit.text = if (isEdit) "ویرایش یادداشت" else "ثبت یادداشت"

        btn_reminder_date.setOnClickListener {
            if (reminderDate.isNotEmpty()) {
                date = reminderDate
            }

            val datePickerDialog = DatePickerDialog.newInstance(
                { _, year, monthOfYear, dayOfMonth ->
                    y1 = year
                    m1 = monthOfYear + 1
                    d1 = dayOfMonth


                    reminderDate = "$year/${convertTime(m1)}/${convertTime(dayOfMonth)}"

                    txt_reminder_date.text =
                        dayOfMonth.toString() + " " + numToMonth(m1) + " " + year

                    selectedReminderDate = reminderDate

                },
                Integer.parseInt(date.split("/")[0]),
                Integer.parseInt(date.split("/")[1]) - 1,
                Integer.parseInt(date.split("/")[2])
            )

            datePickerDialog.show(supportFragmentManager, "Datepickerdialog")

        }

        btn_reminder_time.setOnClickListener {
            val timePicker =
                TimePickerDialog.newInstance({ view, hourOfDay, minute ->
                    selectedReminderHour = hourOfDay.toString()
                    selectedReminderMinute = minute.toString()
                    H1 = hourOfDay
                    M1 = minute
                    selectedReminderTime =
                        "${String.format("%02d", selectedReminderHour.toInt())}:${String.format(
                            "%02d",
                            selectedReminderMinute.toInt()
                        )}"

                    txt_reminder_time.text = selectedReminderTime
                }, H1, M1, true)
            timePicker.show(supportFragmentManager, "")
        }

        if (isEdit) {
            edt_title.setText(note?.title)
            edt_description.setText(note?.description)

            setDatePickers(note?.dateReminder, note?.timeReminder)
            txt_reminder_date.text =
                d1.toString() + " " + numToMonth(m1) + " " + y1

            txt_reminder_time.text = selectedReminderTime
        }

        btn_submit.setOnClickListener {
            if (isTitleValid && isDescriptionValid) {
                registerOrEditNote()
            }
        }
    }

    fun registerOrEditNote() {
        if (isNetConnected()) {
            if (!dialog.isShowing)
                showpDialog(this)
            val req = App.getRetofitApi()?.registerOrEditNote(
                token = token,
                registerNoteModel = RegisterNoteModel(
                    if (isEdit) note?.id else null,
                    edt_title.text.toString(),
                    edt_description.text.toString(),
                    selectedReminderDate,
                    selectedReminderTime
                )
            )
            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {
                        val str = if (!isEdit) "ثبت" else "ویرایش"
                        Toast.makeText(
                            this@AddNoteActivity,
                            "یادداشت شما با موفقیت $str شد.",
                            Toast.LENGTH_SHORT
                        ).show()
                        val reminderModel = ReminderModel(
                            it.body()?.data?.id,
                            it.body()?.data?.intId,
                            it.body()?.data?.title,
                            it.body()?.data?.description,
                            it.body()?.data?.dateReminder,
                            it.body()?.data?.timeReminder
                        )
                        setAlarmManager(
                            this@AddNoteActivity,
                            y1,
                            m1,
                            d1,
                            H1,
                            M1,
                            reminderModel
                        )
                        var isExist = false
                        reminders?.forEach {
                            if (it.id == reminderModel.id) {
                                it.title = reminderModel.title
                                it.description = reminderModel.description
                                it.date = reminderModel.date
                                it.time = reminderModel.time
                                isExist = true
                                return@forEach
                            }
                        }
                        if (!isExist)
                            reminders?.add(reminderModel)
                        updateSharedReferences()
                        val intent = Intent()
                        setResult(RESULT_OK, intent)
                        finish()
                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                registerOrEditNote()
                            }
                        })
                    }
                    hidepDialog()
                }
                onFailure = {
                    Log.i(TAG, it?.message!!)
                    hidepDialog()
                }
            }
        }

    }

    private fun setDatePickers(date: String?, time: String?) {
        if (date != null) {
            y1 = Integer.parseInt(date.split("/")[0])
            m1 = Integer.parseInt(date.split("/")[1])
            d1 = Integer.parseInt(date.split("/")[2])
            reminderDate = "$y1/${convertTime(m1)}/${convertTime(d1)}"
            selectedReminderDate = reminderDate
        }

        if (time != null && time.isNotEmpty() && time.length == 5 /* 00:00 Format*/) {
            H1 = Integer.parseInt(time.split(":")[0])
            M1 = Integer.parseInt(time.split(":")[1])
            selectedReminderTime =
                f.format(Integer.valueOf(H1)) + ":" + f.format(Integer.valueOf(M1))
        }
    }
}
