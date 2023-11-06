package ir.adak.Vect.UI.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.MeetingActivity.MeetingActivity
import ir.adak.Vect.Utils.WrapContentLinearLayoutManager
import kotlinx.android.synthetic.main.fragment_offers.*

/**
 * A simple [Fragment] subclass.
 */
class OffersFragment : BaseFragment() {

    val offersAdapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_offers, container, false)
        view.rotationY = 180f
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_offers.apply {
            layoutManager = WrapContentLinearLayoutManager(activity)
            adapter = offersAdapter
        }

        setupSwipeRefreshLayout()

    }


    private fun setupSwipeRefreshLayout() {

        srl_layout.setColorSchemeResources(R.color.colorPrimary)
        srl_layout.setOnRefreshListener {
            if (isNetConnected()) {
                if (activity is MeetingActivity) {
                    if (srl_layout?.isRefreshing == true) {
                        srl_layout?.isRefreshing = false
                    }
                    if((activity as MeetingActivity).flag)
                    {
                        (activity as MeetingActivity).getProjectsAndOffers_2()
                    }else{
                        (activity as MeetingActivity).getProjectsAndOffers()
                    }


                }
            }
        }
    }

}
