package ir.adak.Vect.UI.Dialogs

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.AddMeetingActivity.AddMeetingActivity
import ir.adak.Vect.UI.Activities.AddProjectActivity.AddProjectActivity
import ir.adak.Vect.UI.Activities.PeigiriActivity.PeigiriActivity
import ir.adak.Vect.Utils.Constants
import kotlinx.android.synthetic.main.add_to_project_bottom_sheet.*

class AddToProjectBottomSheet : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SheetDialog)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = View.inflate(context, R.layout.add_to_project_bottom_sheet, null)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_add_task.setOnClickListener {
            dismiss()
            val intent = Intent(activity, AddProjectActivity::class.java)
            intent.putExtra("parentId", (activity as PeigiriActivity).project?.id)
            activity?.startActivityForResult(
                intent,
                Constants.ADD_PROJECT_REQUEST_CODE
            )
            activity?.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
        }
        btn_add_meeting.setOnClickListener {
            activity?.startActivityForResult(
                Intent(
                    activity, AddMeetingActivity
                    ::class.java
                ).putExtra("parentId", (activity as PeigiriActivity).project?.id),
                Constants.ADD_MEETING_REQUEST_CODE
            )
            activity?.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.ADD_PROJECT_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            (activity as PeigiriActivity).getFollowUps()
        } else if (requestCode == Constants.ADD_MEETING_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            (activity as PeigiriActivity).getFollowUps()
        }
        dismiss()
    }
}