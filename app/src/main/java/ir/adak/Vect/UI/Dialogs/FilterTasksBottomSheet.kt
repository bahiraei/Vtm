package ir.adak.Vect.UI.Dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.fragment.app.DialogFragment
import ir.adak.Vect.R
import ir.adak.Vect.UI.Fragments.TaskFilter_Date_Fragment
import ir.adak.Vect.UI.Fragments.TaskFilter_Main_Fragment
import kotlinx.android.synthetic.main.filter_tasks_bottom_sheet.*
import android.view.*


class FilterTasksBottomSheet : DialogFragment() {

    val taskfilterMainFragment = TaskFilter_Main_Fragment()
    val taskfilterDateFragment = TaskFilter_Date_Fragment()
    var onDismissListener : OnDismissListener?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomDialogTheme)

    }

    override fun onResume() {
        super.onResume()
        val params: WindowManager.LayoutParams? = dialog?.window?.attributes
        params?.width = MATCH_PARENT
        params?.height = MATCH_PARENT
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.attributes = params as WindowManager.LayoutParams

        if (view != null)
            view!!.isFocusableInTouchMode = true
        view?.requestFocus()
        view?.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //TODO: Put here your action or method
                    if (childFragmentManager.backStackEntryCount > 0) {
                        childFragmentManager.popBackStack()
                    } else {
                        dismiss()
                    }
                    true
                } else {
                    false
                }
            } else {
                false
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.filter_tasks_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction()
            .replace(fragment_container.id, taskfilterMainFragment).commit()

    }

    fun chooseDateFilterFragment() {
        childFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
            .replace(
                fragment_container.id, taskfilterDateFragment
            ).addToBackStack(null)
            .commit()
    }

    fun DismissBottomSheet(){
        onDismissListener?.OnDismiss()
        dismiss()
    }
    interface OnDismissListener{
        fun OnDismiss()
    }
}