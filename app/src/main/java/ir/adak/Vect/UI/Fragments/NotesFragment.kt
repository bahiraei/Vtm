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
import com.google.gson.Gson
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import ir.adak.Vect.App
import ir.adak.Vect.Data.Enums.PeigiriChangeEnum
import ir.adak.Vect.Data.Models.NoteItem

import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.MainActivity.MainActivity
import ir.adak.Vect.UI.Activities.MainActivity.MainActivityViewModel
import kotlinx.android.synthetic.main.fragment_notes.*
import ir.adak.Vect.Data.Models.ReminderModel
import ir.adak.Vect.Listeners.OnLoginCompleted
import ir.adak.Vect.Utils.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class NotesFragment : BaseFragment() {
    var dialog_2: AlertDialog ?=null
    lateinit var mainActivityViewModel: MainActivityViewModel
    var noteList = mutableListOf<NoteItem>()
    var MyAct: Activity? =null
    var filterdNoteList = mutableListOf<NoteItem>()
    val notesAdapter = GroupAdapter<ViewHolder>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notes, container, false)
        view.rotationY = 180f
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivityViewModel = (activity as MainActivity).viewModel

        rv_notes.apply {
            layoutManager = WrapContentLinearLayoutManager(activity)
            adapter = notesAdapter
        }

        setupSwipeRefreshLayout()
        notesAdapter.setOnItemLongClickListener { item, _ ->
            (item as NoteItem).isHighlight = true
            item.notifyChanged(PeigiriChangeEnum.HIGHLIGHT)
            (activity as MainActivity).OnItemLongClicked(notesAdapter.getAdapterPosition(item))
            true
        }
        notesAdapter.setOnItemClickListener { item, view ->
            //            startAlarm()
        }
    }

    private fun setupSwipeRefreshLayout() {
        srl_layout.setColorSchemeResources(R.color.colorPrimary)
        srl_layout.setOnRefreshListener {
            if (isNetConnected_2()) {
                getNotes()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseFragment.MyAct=activity
        initDialog_2()

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
    fun getNotes() {
        if (srl_layout?.isRefreshing == true) {
            srl_layout.isRefreshing = false
        }


        if (isNetConnected_2()) {
//            if(dialog?.isShowing!!)
            showpDialog_2()
            val req = App.getRetofitApi()
                ?.getNotes(token)
            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {
                        hidepDialog_2()
                        Log.i("sdvmsvas","NIMA3")
                        val notes = it.body()?.data ?: mutableListOf()
                        val reminderList = mutableListOf<ReminderModel>()
                        val localIds = mutableListOf<Int>()
                        val serverIds = mutableListOf<Int>()
                        mainActivityViewModel.areNotesLoaded = true
                        noteList.clear()
                        notes.forEach {
                            //                            reminderList.add(
//                                ReminderModel(
//                                    it.id,
//                                    it.intId,
//                                    it.title,
//                                    it.description,
//                                    it.dateReminder,
//                                    it.timeReminder
//                                )
//                            )
                            serverIds.add(it.intId)

                            noteList.add(
                                NoteItem(
                                    id = it.id,
                                    title = it.title,
                                    description = it.description,
                                    date = it.date,
                                    dateReminder = it.dateReminder,
                                    dateReminderEn = it.dateReminderEn,
                                    timeReminder = it.timeReminder
                                )
                            )
                            if (!it.dateReminderEn.isNullOrEmpty() && !it.dateReminder.isNullOrEmpty()) {

                                val format = SimpleDateFormat("yyyy/MM/dd")
                                try {
                                    val date = format.parse(it.dateReminderEn!!)
                                    val currentDate = Date()

                                    if (!currentDate.after(date)) {
                                        reminderList.add(
                                            ReminderModel(
                                                it.id,
                                                it.intId,
                                                it.title,
                                                it.description,
                                                it.dateReminder,
                                                it.timeReminder
                                            )
                                        )
                                        Log.i(TAG, "Added")

                                    }
                                } catch (e: ParseException) {
                                    Log.i(TAG, e.message)

                                }
                            }


                        }
                        reminders?.forEach {
                            localIds.add(it.intId ?: 0)
                        }
                        val deletedIds = localIds.filter {
                            !serverIds.contains(it)
                        }

                        cancelAlarm(activity, deletedIds)
                        setAlarmManager(activity, reminderList)
                        App.sharedPreferences?.edit()
                            ?.putString(Constants.REMINDERS, Gson().toJson(reminderList))?.apply()
                        updateSharedReferences()
                        notesAdapter.update(noteList)

                    }
                    else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                getNotes()
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

}
