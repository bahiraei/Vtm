package ir.adak.Vect.UI.Dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import ir.adak.Vect.Data.Models.Project
import ir.adak.Vect.R
import kotlinx.android.synthetic.main.change_progress_dialog.*

class ChangeProgressDialog(
    var project: Project?,
    var onChangeProgressCompleted: OnChangeProgressCompleted? = null
) :
    DialogFragment() {

    interface OnChangeProgressCompleted {
        fun OnChangeProgress(progress: Int)
    }

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
        return inflater.inflate(R.layout.change_progress_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progress_bar.max = 100f
        progress_bar.setProgress(project?.progress?.toFloat() ?: 0f)
        txt_current_progress.text = "${project?.progress?.toString()}%"
        progress_bar.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams?) {

            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar?) {
                txt_current_progress.text = "${seekBar?.progress}%"
            }
        }
        btn_confirm.setOnClickListener {
            dismiss()
            if (project?.progress != progress_bar.progress) {
                onChangeProgressCompleted?.OnChangeProgress(progress_bar.progress)
            }
        }
    }

}