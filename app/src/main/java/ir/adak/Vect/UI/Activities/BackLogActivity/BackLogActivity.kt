package ir.adak.Vect.UI.Activities.BackLogActivity

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import ir.adak.Vect.Adapters.BacklogRecyclerViewAdapter
import ir.adak.Vect.App
import ir.adak.Vect.Data.Models.FilterProjectModel
import ir.adak.Vect.Listeners.OnLoginCompleted
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.BaseActivity
import ir.adak.Vect.Utils.PaginationScrollListener
import ir.adak.Vect.Utils.WrapContentLinearLayoutManager
import ir.adak.Vect.Utils.enqueue
import kotlinx.android.synthetic.main.activity_back_log.*

class BackLogActivity : BaseActivity() {


    lateinit var layoutManager: WrapContentLinearLayoutManager
    var viewModel: ViewModel? = null
    var backlogAdapter: BacklogRecyclerViewAdapter? = null

    private val PAGE_START = 1
    private var isLoading = false
    private var hasMoreItemsToLoad = false

    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    var currentPage = PAGE_START
    var loadCompleted = false


    var selectedFilterProjectModel: FilterProjectModel? = FilterProjectModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_back_log)


        selectedFilterProjectModel = intent.extras?.getParcelable("selectedFilterProjectModel")
        layoutManager = WrapContentLinearLayoutManager(this)
        backlogAdapter =
            BacklogRecyclerViewAdapter(object : BacklogRecyclerViewAdapter.OnItemClickedListener {
                override fun OnItemCLicked(position: Int, projectId: String?) {

                }
            })

        rv_backlog.apply {
            layoutManager = this@BackLogActivity.layoutManager
            adapter = backlogAdapter
        }

        rv_backlog.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun LoadMoreItems() {
                if (!loadCompleted)
                    getBackLogByPage(currentPage)
            }

            override fun IsLoading(): Boolean = isLoading

            override fun HasMoreItemsToLoad(): Boolean = hasMoreItemsToLoad
        })

        getBackLogFirstPage()
        setupSwipeRefreshLayout()
    }


    private fun setupSwipeRefreshLayout() {

        srl_layout.setColorSchemeResources(R.color.colorPrimary)

        srl_layout.setOnRefreshListener {
            if (isNetConnected()) {
                getBackLogFirstPage()
            }
        }

    }

    fun getBackLogFirstPage() {

        currentPage = PAGE_START

        if (srl_layout?.isRefreshing == true) {
            srl_layout?.isRefreshing = false
        }

        if (isNetConnected()) {
            if (!dialog.isShowing)
                showpDialog(this)
            val req = App.getRetofitApi()
                ?.getProjects(
                    token,
                    currentPage,
                    selectedFilterProjectModel
                )
            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {
                        val getProjectsResponseModel = it.body()?.data
                        backlogAdapter?.updateList(getProjectsResponseModel?.projects?.toMutableList())
                        hasMoreItemsToLoad = getProjectsResponseModel?.moreDate ?: false
                        if (hasMoreItemsToLoad) {
                            backlogAdapter?.addLoadingFooter()
                            currentPage += 1
                        }

                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                getBackLogFirstPage()
                            }
                        })
                    }
                    hidepDialog()

                }
                onFailure = {
                    Log.i(TAG, it?.message ?: "Some Error Happened")
                    hidepDialog()
                }
            }


        }

    }


    fun getBackLogByPage(page: Int) {
        loadCompleted = true
        if (srl_layout.isRefreshing) {
            srl_layout.isRefreshing = false
        }

        if (isNetConnected()) {
            val req = App.getRetofitApi()
                ?.getProjects(
                    token,
                    page,
                    selectedFilterProjectModel
                )
            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {
                        val getProjectsResponseModel = it.body()?.data
                        backlogAdapter?.removeLoadingFooter()
                        isLoading = false
                        backlogAdapter?.addAll(getProjectsResponseModel?.projects)

                        hasMoreItemsToLoad = getProjectsResponseModel?.moreDate ?: false

                        if (hasMoreItemsToLoad) {
                            backlogAdapter?.addLoadingFooter()
                            currentPage += 1
                        }

                        loadCompleted = false
                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                getBackLogByPage(page)
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
}
