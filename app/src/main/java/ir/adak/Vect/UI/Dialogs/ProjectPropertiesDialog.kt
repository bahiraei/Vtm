package ir.adak.Vect.UI.Dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import ir.adak.Vect.Data.Enums.ProjectStatus
import ir.adak.Vect.Data.Models.Project
import ir.adak.Vect.R
import kotlinx.android.synthetic.main.project_properties_dialog.*

class ProjectPropertiesDialog(
    var project: Project?
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
        return inflater.inflate(R.layout.project_properties_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (project?.projectStatus == ProjectStatus.Active.value) {
            txt_active_deactive.text = "غیر فعال کردن"
        } else if (project?.projectStatus == ProjectStatus.Deactive.value) {
            txt_active_deactive.text = "فعال کردن"
        }
    }
}