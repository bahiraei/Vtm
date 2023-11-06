package ir.adak.Vect.UI.Fragments


import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import ir.adak.Vect.Data.Models.MemberLogDto
import ir.adak.Vect.Data.Models.MemberLogModel

import ir.adak.Vect.R
import ir.adak.Vect.Utils.WrapContentLinearLayoutManager
import kotlinx.android.synthetic.main.fragment_enter_exit_history.*
import org.modelmapper.TypeToken

/**
 * A simple [Fragment] subclass.
 */
class EnterExitHistoryFragment : BaseFragment() {

    val historyAdapter = GroupAdapter<ViewHolder>()
    var memberLogs: MutableList<MemberLogDto>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_enter_exit_history, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        memberLogs = Gson().fromJson(
            arguments?.getString("memberLogs"),
            object : TypeToken<MutableList<MemberLogDto>>() {}.type
        )

        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager
            .defaultDisplay
            .getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val params = rv_history.layoutParams
        params.height = height - dp2px(80)
        rv_history.layoutParams = params


        rv_history.apply {
            layoutManager = WrapContentLinearLayoutManager(activity)
            adapter = historyAdapter
        }

        val list = mutableListOf<MemberLogModel>()
        memberLogs?.forEach {
            list.add(
                MemberLogModel(
                    it.access,
                    it.date,
                    it.id,
                    it.logType,
                    it.memberOrgLevelId,
                    it.memberTitle,
                    it.memberFullName,
                    it.profileImage
                )
            )
        }
        historyAdapter.update(list)

    }
}
