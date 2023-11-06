package ir.adak.Vect.UI.Dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ir.adak.Vect.Data.Models.Meeting
import ir.adak.Vect.Data.Models.OfferItem
import ir.adak.Vect.R
import kotlinx.android.synthetic.main.add_offer_bottom_sheet.*


class AddOfferBottomSheet(
    var token: String?,
    var offerItem: OfferItem? = null,
    var meeting: Meeting? = null
) :
    BottomSheetDialogFragment() {

    interface OnSubmitClickedListener {
        fun OnSubmitClicked(offerText: String)
    }

    var onSubmitClickedListener: OnSubmitClickedListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SheetDialog)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return View.inflate(context, R.layout.add_offer_bottom_sheet, null)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_submit.setOnClickListener {
            if (edt_offer.text.isNullOrEmpty()) {
                Toast.makeText(activity, "لطفا متن موضوع خود را وارد کنید", Toast.LENGTH_SHORT)
                    .show()
            } else {
                onSubmitClickedListener?.OnSubmitClicked(edt_offer.text.toString())
                dismiss()
            }
        }
    }

}