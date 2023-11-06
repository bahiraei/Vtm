package ir.adak.Vect.UI.Activities.ChooseMeetingActivity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ir.adak.Vect.Adapters.MeetingsRecyclerViewAdapter
import ir.adak.Vect.App
import ir.adak.Vect.Data.Enums.PeigiriChangeEnum
import ir.adak.Vect.Data.Models.*
import ir.adak.Vect.Listeners.OnLoginCompleted
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.BaseActivity
import ir.adak.Vect.Utils.PaginationScrollListener
import ir.adak.Vect.Utils.WrapContentLinearLayoutManager
import ir.adak.Vect.Utils.enqueue
import kotlinx.android.synthetic.main.activity_choose_meeting.*

class ChooseMeetingActivity : BaseActivity() {

    var filterMeetingModel: FilterMeetingModel? = null

    val meetingsAdapter = MeetingsRecyclerViewAdapter()
    private var highlighPosition: Int = -1
    private val PAGE_START = 1
    private var isLoading = false
    private var hasMoreItemsToLoad = false
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    var currentPage = PAGE_START
    var loadCompleted = false

    var note: Note? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_meeting)
        note = intent.extras?.getParcelable("note")
        setupSwipeRefreshLayout()
        setupRecyclerView()
        getMeetingsFirstPage()
        meetingsAdapter.isForChoosing = true
        meetingsAdapter.setOnItemClickedListener(object :
            MeetingsRecyclerViewAdapter.OnItemClicked {
            override fun OnItemClicked(position: Int) {
                meetingsAdapter.items!![position].isHighlight = true
                meetingsAdapter.notifyItemChanged(position, PeigiriChangeEnum.HIGHLIGHT)
                this@ChooseMeetingActivity.OnItemClicked(position)
            }
        })
    }


    fun setupRecyclerView() {
        val layoutManager = WrapContentLinearLayoutManager(this)
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

    fun getMeetingsFirstPage() {

        currentPage = PAGE_START

        if (srl_layout?.isRefreshing == true) {
            srl_layout.isRefreshing = false
        }

        if (isNetConnected()) {
            if (!dialog.isShowing)
                showpDialog(this)
            val req = App.getRetofitApi()
                ?.getMeetings(token, currentPage, filterMeetingModel)
            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {
                        val getMeetingResponse = it.body()?.data
                        meetingsAdapter.clear()
                        meetingsAdapter.addAll(getMeetingResponse?.meetings)
                        hasMoreItemsToLoad = getMeetingResponse?.moreDate ?: false
                        if (hasMoreItemsToLoad) {
                            meetingsAdapter.addLoadingFooter()
                            currentPage += 1
                        }

                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                getMeetingsFirstPage()

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


    fun getMeetingsByPage(page: Int) {
        loadCompleted = true
        if (srl_layout.isRefreshing) {
            srl_layout.isRefreshing = false
        }

        if (isNetConnected()) {
            val req = App.getRetofitApi()
                ?.getMeetings(token, page, filterMeetingModel)
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


    fun OnItemClicked(position: Int) {
        if (highlighPosition != -1) {
            if (highlighPosition != position) {
                closeLongTouch(highlighPosition)
            }
        }
        highlighPosition = position
        btn_submit.visibility = View.VISIBLE
        btn_submit.setOnClickListener {
            registerOrEditOffer(position)
        }

    }

    fun registerOrEditOffer(position: Int) {
        if (isNetConnected()) {
            if (!dialog.isShowing)
                showpDialog(this)
            val req = App.getRetofitApi()
                ?.registerOrEditOffer(
                    token, RegisterOfferModel(
                        note?.description,
                        null,
                        meetingsAdapter.items!![position].id
                    )
                )
            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                registerOrEditOffer(position)

                            }
                        })
                    }
                    hidepDialog()

                }
                onFailure = {
                    Log.i(TAG, it?.message ?: "")
                    hidepDialog()
                    finish()
                }
            }

        }

    }

    private fun closeLongTouch(position: Int) {
        highlighPosition = -1
        meetingsAdapter.items!![position].isHighlight = false
        meetingsAdapter.notifyItemChanged(
            position,
            PeigiriChangeEnum.HIGHLIGHT
        )
        btn_submit.visibility = View.INVISIBLE
    }
}
