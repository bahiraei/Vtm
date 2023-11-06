package ir.adak.Vect.UI.Fragments


import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ir.adak.Vect.Adapters.MeetingsRecyclerViewAdapter
import ir.adak.Vect.App
import ir.adak.Vect.Data.Enums.PeigiriChangeEnum
import ir.adak.Vect.Listeners.OnItemLongClicked
import ir.adak.Vect.Listeners.OnLoginCompleted

import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.MainActivity.MainActivity
import ir.adak.Vect.UI.Activities.MainActivity.MainActivityViewModel
import ir.adak.Vect.Utils.PaginationScrollListener
import ir.adak.Vect.Utils.WrapContentLinearLayoutManager
import ir.adak.Vect.Utils.enqueue
import kotlinx.android.synthetic.main.fragment_meetings.*

/**
 * A simple [Fragment] subclass.
 */
class MeetingsFragment : BaseFragment() {


    lateinit var mainActivityViewModel: MainActivityViewModel
    val meetingsAdapter = MeetingsRecyclerViewAdapter()
    var MyAct:Activity ? =null
    var dialog_2: AlertDialog ?=null
    private val PAGE_START = 1
    private var isLoading = false
    private var hasMoreItemsToLoad = false
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    var currentPage = PAGE_START
    var loadCompleted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseFragment.MyAct=activity
        initDialog_2()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_meetings, container, false)
        view.rotationY = 180f
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivityViewModel = (activity as MainActivity).viewModel
        setupSwipeRefreshLayout()
        setupRecyclerView()
        meetingsAdapter.setOnItemLongClickedListener(object :
            OnItemLongClicked {
            override fun OnItemLongClicked(position: Int) {
                if (activity is MainActivity) {
                    meetingsAdapter.items!![position].isHighlight = true
                    meetingsAdapter.notifyItemChanged(position, PeigiriChangeEnum.HIGHLIGHT)
                    (activity as MainActivity).OnItemLongClicked(position)
                }
            }
        })
    }
    fun setupRecyclerView() {
        val layoutManager = WrapContentLinearLayoutManager(context)
        rv_meetings.layoutManager = layoutManager
        rv_meetings.adapter = meetingsAdapter
        rv_meetings.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun LoadMoreItems() {
                if (!loadCompleted)
                    getMeetingsByPage(currentPage)
            }
            override fun IsLoading(): Boolean = isLoading

            override fun HasMoreItemsToLoad(): Boolean = hasMoreItemsToLoad
        })
    }

    fun isNetConnected_2(): Boolean {
        val cn = MyAct?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nf: NetworkInfo?
        nf = cn.activeNetworkInfo
        if (nf != null && nf.isConnected) {
            return true
        } else {
            Toast.makeText(activity, "اینترت شما وصل نیست", Toast.LENGTH_LONG).show()
            return false
        }
    }
    fun showpDialog_2() {
        try {
            val builder = AlertDialog.Builder(MyAct!!)
            val layoutInflater = layoutInflater
            builder.setView(layoutInflater.inflate(R.layout.loading, null))
            dialog_2 = builder.create()
            dialog_2?.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog_2?.setCancelable(false)
            dialog_2?.show()

        } catch (e: Exception) {
            //nothing
            Log.i("showpDialog", e.toString() + "")
        }

    }
    fun initDialog_2() {
        try {
            val builder = AlertDialog.Builder(MyAct!!)
            val layoutInflater = layoutInflater
            builder.setView(layoutInflater.inflate(R.layout.loading, null))
            dialog_2 = builder.create()
            dialog_2?.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog_2?.setCancelable(false)

        } catch (e: Exception) {
            //nothing
            Log.i("showpDialog", e.toString() + "")
        }
    }
    fun hidepDialog_2() {
        try {
            dialog_2?.dismiss()

        } catch (e: Exception) {
            //nothing
            Log.i("HidePDialog", e.toString() + "")
        }


    }
    fun getMeetingsFirstPage() {
        currentPage = PAGE_START
        if (srl_layout?.isRefreshing == true) {
            srl_layout.isRefreshing = false
        }
        Log.i("svnavknadvsaZZZ",securityKey)
        Log.i("svnavknadvsaZZZ",token)
        Log.i("svnavknadvsaZZZ",currentPage.toString())
        Log.i("svnavknadvsaZZZ",securityKey)
        if (isNetConnected_2()) {
//            if (dialog_2?.isShowing!!)
                showpDialog_2()
            var req = App.getRetofitApi()
                 ?.getMeetings(token, currentPage, MainActivity.selectedFilterMeetingModel)
            req?.enqueue {
                onResponse = {
                    hidepDialog_2()
                    if (isResponseValid(it) == 2) {
                        val getMeetingResponse = it.body()?.data
                        mainActivityViewModel.areMeetingLoaded = true
                        meetingsAdapter.clear()
                        meetingsAdapter.addAll(getMeetingResponse?.meetings)
                        hasMoreItemsToLoad = getMeetingResponse?.moreDate ?: false
                        if (hasMoreItemsToLoad) {
                            meetingsAdapter.addLoadingFooter()
                            currentPage += 1
                        }

                    }
                    else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                getMeetingsFirstPage()
                            }
                        })
                    }
                    hidepDialog_2()

                }
                onFailure = {
                    Log.i(TAG, it?.message ?: "Some Error Happened")
                    hidepDialog_2()
                }
            }


        }

    }


    fun getMeetingsByPage(page: Int) {
        loadCompleted = true
        if (srl_layout.isRefreshing) {
            srl_layout.isRefreshing = false
        }

        if (isNetConnected()) {
            val req = App.getRetofitApi()
                ?.getMeetings(token, page, MainActivity.selectedFilterMeetingModel)
            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {
                        val getMeetingResponse = it.body()?.data
                        meetingsAdapter.removeLoadingFooter()
                        isLoading = false
                        meetingsAdapter.addAll(getMeetingResponse?.meetings)

                        hasMoreItemsToLoad = getMeetingResponse?.moreDate ?: false

                        if (hasMoreItemsToLoad) {
                            meetingsAdapter.addLoadingFooter()
                            currentPage += 1
                        }

                        loadCompleted = false
                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                getMeetingsByPage(page)
                            }
                        })
                    }
                }
                onFailure = {
                    Log.i(TAG, it?.message ?: "Some Error Happened")

                }
            }

        }


    }


    private fun setupSwipeRefreshLayout() {

        srl_layout.setColorSchemeResources(R.color.colorPrimary)

        srl_layout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            if (isNetConnected()) {
                getMeetingsFirstPage()
            }
        })

    }
}
