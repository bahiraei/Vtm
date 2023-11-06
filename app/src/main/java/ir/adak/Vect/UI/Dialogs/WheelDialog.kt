package ir.adak.Vect.UI.Dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.adak.Vect.R
import kotlinx.android.synthetic.main.my_alert_dialog_layout.btn_dialog
import kotlinx.android.synthetic.main.wheel_dialog_layout.*

class WheelDialog(var currentSelectedDay: Int, var onSubmitListener: OnSubmitListener) :
    BaseDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setGravity(Gravity.CENTER)
        return inflater.inflate(R.layout.wheel_dialog_layout, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.MyDialogStyle)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wheel_date_picker_day.selectedDay = currentSelectedDay
        btn_dialog.setOnClickListener {
            dismiss()
            onSubmitListener.OnSubmit(wheel_date_picker_day.currentDay)
        }
    }

    interface OnSubmitListener {
        fun OnSubmit(selectedDay: Int)
    }

}