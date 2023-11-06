package ir.adak.Vect.UI.Dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.adak.Vect.R
import kotlinx.android.synthetic.main.my_question_dialog_layout.*

class MyQuestionDialog(
    var icon: Int,
    var title: String, var question: String, var onButtonsClicked: OnButtonsClicked
) : BaseDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setGravity(Gravity.CENTER)
        return inflater.inflate(R.layout.my_question_dialog_layout, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.MyDialogStyle)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        img_icon.setImageResource(icon)
        txt_title.text = title
        txt_question.text = question

        btn_positive.setOnClickListener {
            dismiss()
            onButtonsClicked.OnPositiveClicked()
        }

        btn_negative.setOnClickListener {
            dismiss()
            onButtonsClicked.OnNegativeClicked()
        }
    }

    interface OnButtonsClicked {
        fun OnPositiveClicked()
        fun OnNegativeClicked()

    }
}