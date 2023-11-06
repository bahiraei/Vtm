package ir.adak.Vect.UI.Activities.ActivityNewUser

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog
import ir.adak.Vect.App
import ir.adak.Vect.Data.Models.RegisterUserRequestModel
import ir.adak.Vect.Listeners.OnLoginCompleted
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.BaseActivity
import ir.adak.Vect.Utils.SolarCalendar
import ir.adak.Vect.Utils.enqueue
import kotlinx.android.synthetic.main.activity_add_user.*
import java.text.DecimalFormat

class ActivityNewUser : BaseActivity() {


    var f: DecimalFormat = DecimalFormat("00")

    private var y1: Int = 0
    private var m1: Int = 0
    private var d1: Int = 0

    private var birthDate: String = ""
    private var selectedbirthDate: String = ""
    private var date: String = ""


    var isFirstNameValid = false
    var isLastNameValid = false
    var isPhoneNumberValid = true
    var isMelliCodeValid = true
    var isPersonelCodeValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)


        setDatePickers(SolarCalendar.currentShamsidate)

        spinner_gender.setItems(
            "جنسیت .. ",
            "مرد",
            "زن"
        )


        edt_first_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                isFirstNameValid = p0.toString().length > 2

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
        edt_last_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                isLastNameValid = p0.toString().length > 2

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
        edt_phone_number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                isPhoneNumberValid = p0.toString().length == 11

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
        edt_melli_code.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                isMelliCodeValid = p0.toString().length == 10 || p0.toString().isEmpty()

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
        edt_personel_code.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                isPersonelCodeValid = p0.toString().isNotEmpty()

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })


        btn_birthday_date.setOnClickListener {
            if (birthDate.isNotEmpty()) {
                date = birthDate
            }

            val datePickerDialog = DatePickerDialog.newInstance(
                { _, year, monthOfYear, dayOfMonth ->
                    y1 = year
                    m1 = monthOfYear + 1
                    d1 = dayOfMonth


                    birthDate = "$year/${convertTime(m1)}/${convertTime(dayOfMonth)}"

                    txt_birthday_date.text =
                        dayOfMonth.toString() + " " + numToMonth(m1) + " " + year

                    selectedbirthDate = birthDate

                },
                Integer.parseInt(date.split("/")[0]),
                Integer.parseInt(date.split("/")[1]) - 1,
                Integer.parseInt(date.split("/")[2])
            )

            datePickerDialog.show(supportFragmentManager, "Datepickerdialog")

        }
        btn_submit.setOnClickListener {
            if (!isFirstNameValid) {
                Toast.makeText(this, "نام کاربر باید از دو کاراکتر بیشتر باشد", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (!isLastNameValid) {
                Toast.makeText(this, "نام خانوادگی کاربر باید از دو کاراکتر بیشتر باشد", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (!isPhoneNumberValid) {
                Toast.makeText(this, "شماره تلفن کاربر باید یازده رقمی باشد", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (!isMelliCodeValid) {
                Toast.makeText(this, "کد ملی کاربر باید در رقمی باشد", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (!isPersonelCodeValid) {
                Toast.makeText(this, "کد پرسنلی کاربر را وارد کنید", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            registerNewUser()
        }
    }


    fun registerNewUser() {
        if (isNetConnected()) {
            if (!dialog.isShowing)
                showpDialog(this)
            val req = App.getRetofitApi()?.registerUser(
                token = token,
                registerUserRequestModel = RegisterUserRequestModel(
                    firstName = edt_first_name.text.toString(),
                    lastName = edt_last_name.text.toString(),
                    meliCode = edt_melli_code.text.toString(),
                    personId = edt_personel_code.text.toString(),
                    orgTitle = edt_semat.text.toString(),
                    phone = edt_phone_number.text.toString(),
                    birthDayFa = selectedbirthDate,
                    gender = if (spinner_gender.selectedIndex > 0) spinner_gender.selectedIndex else 1
                )
            )
            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {

                        Toast.makeText(
                            this@ActivityNewUser,
                            "کاربر جدید با موفقیت اضافه شد.",
                            Toast.LENGTH_SHORT
                        ).show()
                        setResult(Activity.RESULT_OK)
                        finish()

                    }
                    else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                registerNewUser()
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


    private fun setDatePickers(date: String?) {
        if (date != null) {
            y1 = Integer.parseInt(date.split("/")[0])
            m1 = Integer.parseInt(date.split("/")[1])
            d1 = Integer.parseInt(date.split("/")[2])
            birthDate = "$y1/${convertTime(m1)}/${convertTime(d1)}"
            selectedbirthDate = birthDate
        }
    }
}
