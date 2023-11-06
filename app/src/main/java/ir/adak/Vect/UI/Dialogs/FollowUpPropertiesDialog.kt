package ir.adak.Vect.UI.Dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import ir.adak.Vect.Data.Enums.FollowUpType
import ir.adak.Vect.Data.Models.PeigiriItem.PeigiriItem
import ir.adak.Vect.Listeners.OnFollowUpPropertiesDialogButtonsClicked
import ir.adak.Vect.R
import kotlinx.android.synthetic.main.followup_properties_dialog.*

class FollowUpPropertiesDialog(
    var peigiriItem: PeigiriItem?,
    var isMine: Boolean,
    var isMyProject: Boolean,
    var isActive: Boolean,
    var onFollowUpPropertiesDialogButtonsClicked: OnFollowUpPropertiesDialogButtonsClicked?
) :
    DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val params = dialog?.window?.attributes
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setStyle(STYLE_NO_TITLE, R.style.MyDialogStyle)
        return inflater.inflate(R.layout.followup_properties_dialog, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_reply_followUp.setOnClickListener {
            dismiss()
            onFollowUpPropertiesDialogButtonsClicked?.onBtnReplyFollowUpClicked(peigiriItem)
        }
        if (peigiriItem?.followUp?.followUpType == FollowUpType.Text || peigiriItem?.followUp?.followUpType == FollowUpType.Description) {
            btn_copy_followUp_text.visibility = View.VISIBLE
            copy_followUp_text_divider.visibility = View.VISIBLE
            btn_copy_followUp_text.setOnClickListener {
                dismiss()
                onFollowUpPropertiesDialogButtonsClicked?.onBtnCopyFollowUpTextClicked(peigiriItem)
            }
        } else {
            btn_copy_followUp_text.visibility = View.GONE
            copy_followUp_text_divider.visibility = View.GONE
        }
        if (isMyProject) {
            if (peigiriItem?.followUp?.followUpType == FollowUpType.Text ||
                peigiriItem?.followUp?.followUpType == FollowUpType.File ||
                peigiriItem?.followUp?.followUpType == FollowUpType.Description
            ) {
                btn_pin_followUp.visibility = View.VISIBLE
                pin_followUp_divider.visibility = View.VISIBLE
                btn_pin_followUp.setOnClickListener {
                    dismiss()
                    onFollowUpPropertiesDialogButtonsClicked?.onBtnPinFollowUpClicked(
                        peigiriItem
                    )
                }
            } else {
                btn_pin_followUp.visibility = View.GONE
                pin_followUp_divider.visibility = View.GONE
            }
        } else {
            btn_pin_followUp.visibility = View.GONE
            pin_followUp_divider.visibility = View.GONE
        }

        if (isMine) {

            btn_reply_followUp.visibility = View.VISIBLE
            if (peigiriItem?.followUp?.followUpType == FollowUpType.Text) {
                btn_edit_followUp.visibility = View.VISIBLE
                edit_followUp_divider.visibility = View.VISIBLE
                btn_edit_followUp.setOnClickListener {
                    dismiss()
                    onFollowUpPropertiesDialogButtonsClicked?.onBtnEditFollowUpClicked(
                        peigiriItem
                    )
                }
            } else {
                btn_edit_followUp.visibility = View.GONE
                edit_followUp_divider.visibility = View.GONE

            }

            reply_followUp_divider.visibility = View.VISIBLE
            btn_delete_followUp.visibility = View.VISIBLE
            btn_delete_followUp.setOnClickListener {
                dismiss()
                onFollowUpPropertiesDialogButtonsClicked?.onBtnDeleteFollowUpClicked(peigiriItem)
            }

        } else {
            btn_delete_followUp.visibility = View.GONE
            btn_edit_followUp.visibility = View.GONE
        }
        if (!isActive) {
            btn_reply_followUp.visibility = View.GONE
            btn_edit_followUp.visibility = View.GONE
            btn_delete_followUp.visibility = View.GONE
            btn_pin_followUp.visibility = View.GONE
            pin_followUp_divider.visibility = View.GONE

        }

    }
}