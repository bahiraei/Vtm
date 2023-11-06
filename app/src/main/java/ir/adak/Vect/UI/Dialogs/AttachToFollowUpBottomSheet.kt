package ir.adak.Vect.UI.Dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.AddActionActivity.AddActionActivity
import ir.adak.Vect.UI.Activities.PeigiriActivity.PeigiriActivity
import kotlinx.android.synthetic.main.attach_to_follwup_bottom_sheet.*

class AttachToFollowUpBottomSheet : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SheetDialog)

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = View.inflate(context, R.layout.attach_to_follwup_bottom_sheet, null)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity is AddActionActivity)
        {
            btn_add_action.visibility=View.GONE
        }
        btn_attach_files.setOnClickListener {
            dismiss()
            if (activity is PeigiriActivity)
            {
                (activity as PeigiriActivity).openFilePicker()
            } else if (activity is AddActionActivity)
            {
                (activity as AddActionActivity).openFilePicker()
            }

        }
        btn_attach_images.setOnClickListener {
            dismiss()
            if (activity is PeigiriActivity)
            {
                (activity as PeigiriActivity).openMediaPicker()
            } else if (activity is AddActionActivity)
            {
                (activity as AddActionActivity).openMediaPicker()
            }

        }
        btn_attach_videos.setOnClickListener {
            dismiss()
            if (activity is PeigiriActivity)
            {
                (activity as PeigiriActivity).openMediaPicker()
            }else if (activity is AddActionActivity){
                (activity as AddActionActivity).openMediaPicker()
            }

        }
        btn_attach_audio.setOnClickListener {
            dismiss()
            if (activity is PeigiriActivity)
            {
                (activity as PeigiriActivity).openVoiceRecorder()
            }
            else if (activity is AddActionActivity)
        {
            (activity as AddActionActivity).openVoiceRecorder()
        }
        }
        btn_attach_soorat_vaziat.setOnClickListener {
            dismiss()
            if (activity is PeigiriActivity)
            {
                (activity as PeigiriActivity).openFilePicker(true)
            } else if (activity is AddActionActivity)
            {
                (activity as AddActionActivity).openFilePicker(true)
            }


        }
        btn_add_action.setOnClickListener {
            dismiss()
            (activity as PeigiriActivity).openAddActionActivity()
        }
    }
}