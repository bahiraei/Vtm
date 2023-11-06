package ir.adak.Vect.UI.Fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import ir.adak.Vect.Adapters.JobsRecyclerViewAdapter
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.MainActivity.MainActivity
import ir.adak.Vect.UI.Activities.MainActivity.MainActivityViewModel

import kotlinx.android.synthetic.main.fragment_jobs.*
import ir.adak.Vect.App
import ir.adak.Vect.Data.Enums.PeigiriChangeEnum
import ir.adak.Vect.Listeners.OnItemLongClicked
import ir.adak.Vect.Listeners.OnLoginCompleted
import ir.adak.Vect.UI.Activities.MeetingActivity.MeetingActivity
import ir.adak.Vect.UI.Activities.PeigiriActivity.PeigiriActivity
import ir.adak.Vect.Utils.Constants
import ir.adak.Vect.Utils.PaginationScrollListener
import ir.adak.Vect.Utils.WrapContentLinearLayoutManager
import ir.adak.Vect.Utils.enqueue


class JobsFragment : BaseFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseFragment.MyAct=activity
        initDialog_2()

    }
    lateinit var layoutManager: WrapContentLinearLayoutManager
    var viewModel: ViewModel? = null
    var jobsAdapter: JobsRecyclerViewAdapter? = null
    private val PAGE_START = 1
    var MyAct: Activity? =null
    var dialog_2: AlertDialog?=null
    private var isLoading = false
    private var hasMoreItemsToLoad = false
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    var currentPage = PAGE_START
    var loadCompleted = false

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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_jobs, container, false)
        view.rotationY = 180f
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jobsAdapter = JobsRecyclerViewAdapter(object : JobsRecyclerViewAdapter.OnItemClickedListener {
                override fun OnItemCLicked(position: Int, projectId: String?) {
                    if (activity is MainActivity) {
                        if ((activity as MainActivity).highlighPosition < 0) {
                            val intent = Intent(context, PeigiriActivity::class.java)
                            intent.putExtra("position", position)
                            intent.putExtra("projectId", projectId)
                            (context as AppCompatActivity).startActivityForResult(
                                intent,
                                Constants.UPDATE_PROJECT_REQUEST_CODE
                            )
                            (context as AppCompatActivity).overridePendingTransition(
                                R.anim.slide_left_in,
                                R.anim.slide_left_out
                            )
                        } else {
                            (activity as MainActivity).closeLongTouch((activity as MainActivity).highlighPosition)
                        }
                    }
                    else if (activity is MeetingActivity){
                        val intent = Intent(context, PeigiriActivity::class.java)
                        intent.putExtra("position", position)
                        intent.putExtra("projectId", projectId)
                        (context as AppCompatActivity).startActivityForResult(
                            intent,
                            Constants.UPDATE_PROJECT_REQUEST_CODE
                        )
                        (context as AppCompatActivity).overridePendingTransition(
                            R.anim.slide_left_in,
                            R.anim.slide_left_out
                        )
                    }
                }
            })
        layoutManager = WrapContentLinearLayoutManager(context)
        setupRecyclerView()
        if (activity is MainActivity) {
            viewModel = (activity as MainActivity).viewModel
            rv_jobs.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
                override fun LoadMoreItems() {
                    if (!loadCompleted)
                        if (hasMoreItemsToLoad)
                        {
                            Log.i("vmbmkmkbgkkdmd","Ae")
                            Log.i("vmbmkmkbgkkdmd",currentPage.toString())
                            getJobsByPage(currentPage)
                        }

                }

                override fun IsLoading(): Boolean = isLoading

                override fun HasMoreItemsToLoad(): Boolean = hasMoreItemsToLoad
            })


            if (!(viewModel as MainActivityViewModel).areJobsLoaded)
                getJobsFirstPage()

        }
        else if (activity is PeigiriActivity)
        {
            Log.i("vmbmkmkbgkkdmd","Be")
        }
        setupSwipeRefreshLayout()
        jobsAdapter?.setOnItemLongClickedListener(object :
            OnItemLongClicked {
            override fun OnItemLongClicked(position: Int) {
                if (activity is MainActivity) {
                    jobsAdapter?.items!![position].isHighlight = true
                    jobsAdapter?.notifyItemChanged(position, PeigiriChangeEnum.HIGHLIGHT)
                    (activity as MainActivity).OnItemLongClicked(position)
                }
            }
        })
    }

    fun setupRecyclerView() {
        rv_jobs.layoutManager = layoutManager
        rv_jobs.adapter = jobsAdapter

    }



    fun getJobsFirstPage() {
        currentPage = PAGE_START
        if (srl_layout?.isRefreshing == true) {
            srl_layout?.isRefreshing = false
        }



        if (isNetConnected_2()) {
//            if (dialog_2?.isShowing!!)
                showpDialog_2()

            val req = App.getRetofitApi()
                ?.getProjects(
                    token,
                    currentPage,
                    MainActivity.selectedFilterProjectModel
                )
            req?.enqueue {
                onResponse = {
                    hidepDialog_2()
                    if (isResponseValid(it) == 2) {
                        val getProjectsResponseModel = it.body()?.data
                        (viewModel as MainActivityViewModel).areJobsLoaded = true
                        jobsAdapter?.clear()
                        jobsAdapter?.addAll(getProjectsResponseModel?.projects)
                        hasMoreItemsToLoad = getProjectsResponseModel?.moreDate ?: false
                        if (hasMoreItemsToLoad) {
                            jobsAdapter?.addLoadingFooter()
                            currentPage += 1
                        }

                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                Log.i("svkmsfvskdv","SD")
                                getJobsFirstPage()
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

    fun getJobsByPage(page: Int) {
        loadCompleted = true
        if (srl_layout.isRefreshing) {
            srl_layout.isRefreshing = false
        }

        if (isNetConnected_2()) {
            val req = App.getRetofitApi()
                ?.getProjects(
                    token,
                    page,
                    MainActivity.selectedFilterProjectModel
                )
            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {
                        val getProjectsResponseModel = it.body()?.data
                        jobsAdapter?.removeLoadingFooter()
                        isLoading = false
                        jobsAdapter?.addAll(getProjectsResponseModel?.projects)

                        hasMoreItemsToLoad = getProjectsResponseModel?.moreDate ?: false

                        if (hasMoreItemsToLoad) {
                            jobsAdapter?.addLoadingFooter()
                            currentPage += 1
                        }

                        loadCompleted = false
                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                getJobsByPage(page)
                            }
                        })
                    }
                }
                onFailure = {
                    Log.i(TAG, it?.message ?: "Some Error Happened")
                    hidepDialog_2()
                }
            }

        }

    }

    private fun setupSwipeRefreshLayout() {

        srl_layout.setColorSchemeResources(R.color.colorPrimary)
        srl_layout.setOnRefreshListener {
            if (isNetConnected_2()) {
                if (activity is MainActivity)
                {
                    Log.i("mmccmmcs","A")
                    getJobsFirstPage()
                }


                else if (activity is MeetingActivity) {

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
