package ir.adak.Vect.UI.Activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog
import ir.adak.Vect.App
import ir.adak.Vect.Data.Models.RegisterUserRequestModel
import ir.adak.Vect.Data.Models.partners
import ir.adak.Vect.Listeners.OnLoginCompleted
import ir.adak.Vect.R
import ir.adak.Vect.Utils.SolarCalendar
import ir.adak.Vect.Utils.enqueue
import kotlinx.android.synthetic.main.activity_edit_user.*
import java.text.DecimalFormat

class EditUserActivity : BaseActivity() {
    var data: partners?=null
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
    var Gender=-1
    private fun setDatePickers(date: String?) {
        if (date != null) {
            y1 = Integer.parseInt(date.split("/")[0])
            m1 = Integer.parseInt(date.split("/")[1])
            d1 = Integer.parseInt(date.split("/")[2])
            birthDate = "$y1/${convertTime(m1)}/${convertTime(d1)}"
            selectedbirthDate = birthDate
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)
        if (intent.getSerializableExtra("data_2")!=null)
        {
            data= intent.getSerializableExtra("data_2") as partners?
        }

        setDatePickers(SolarCalendar.currentShamsidate)

        spinner_gender.setItems(
            "جنسیت .. ",
            "مرد",
            "زن"
        )

        if (data?.gender==1)
        {
            spinner_gender.selectedIndex=1
        }
        if (data?.gender==2)
        {
            spinner_gender.selectedIndex=2
        }

        data_Set()
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
        edt_phone_number_2.addTextChangedListener(object : TextWatcher {
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


        txt_birthday_date_2.setOnClickListener {

            if (birthDate.isNotEmpty()) {
                date = birthDate
            }

            val datePickerDialog = DatePickerDialog.newInstance(
                { _, year, monthOfYear, dayOfMonth ->
                    y1 = year
                    m1 = monthOfYear + 1
                    d1 = dayOfMonth


                    birthDate = "$year/${convertTime(m1)}/${convertTime(dayOfMonth)}"

                    txt_birthday_date_2.text =
                        dayOfMonth.toString() + " " + numToMonth(m1) + " " + year

                    selectedbirthDate = birthDate

                },
                Integer.parseInt(date.split("/")[0]),
                Integer.parseInt(date.split("/")[1]) - 1,
                Integer.parseInt(date.split("/")[2])
            )
            datePickerDialog.show(supportFragmentManager, "Datepickerdialog")

        }

        btn_submit_2.setOnClickListener {
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


            if (spinner_gender.selectedIndex==0) {
                Toast.makeText(this, "جنسیت را وارد کنید", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }


            registerNewUser()
        }
    }
    fun registerNewUser() {
        if (isNetConnected()) {

            Log.i("ajvnajsdvbsdv","${spinner_gender.selectedIndex}")
            if (spinner_gender.selectedIndex>0)
            {
                if (spinner_gender.selectedIndex==1)
                {
                    Gender=1
                }
                if (spinner_gender.selectedIndex==2)
                {
                    Gender=2
                }
            }

            if (!dialog.isShowing)
                showpDialog(this)
            val req = App.getRetofitApi()?.EditUser(
                token = token,
                registerUserRequestModel = RegisterUserRequestModel(
                    id = data?.id.toString(),
                    firstName = edt_first_name.text.toString(),
                    lastName = edt_last_name.text.toString(),
                    meliCode = edt_melli_code.text.toString(),
                    personId = edt_personel_code.text.toString(),
                    orgTitle = edt_semat.text.toString(),
                    phone = edt_phone_number_2.text.toString(),
                    birthDayFa = selectedbirthDate,
                    gender = Gender
                )
            )


            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {

                        Toast.makeText(
                            this@EditUserActivity,
                            "ویرایش اطلاعات کاربر  با موفقیت انجام شد.",
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
    private fun data_Set() {
        edt_first_name.setText(data?.firstName)
        isFirstNameValid = data?.firstName.toString().length > 2
        isLastNameValid = data?.lastName.toString().length > 2
        isPhoneNumberValid = data?.phone.toString().length == 11
        isMelliCodeValid = data?.meliCode.toString().length == 10 || data?.meliCode.toString().isEmpty()
        isPersonelCodeValid = data?.personId.toString().isNotEmpty()

        edt_last_name.setText(data?.lastName)
        edt_semat.setText(data?.orgTitle)
        if (!data?.birthDayFa.isNullOrEmpty())
        {
            txt_birthday_date_2.setText(data?.birthDayFa)
        }
        edt_melli_code.setText(data?.meliCode.toString())
        edt_personel_code.setText(data?.personId)
        edt_phone_number_2.setText(data?.phone)



//        edt_phone_number.setText(data?.)
    }
}