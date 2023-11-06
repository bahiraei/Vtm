package ir.adak.Vect.UI.Activities.ReminderActivity

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import ir.adak.Vect.App
import ir.adak.Vect.Data.Models.RegisterNoteModel
import ir.adak.Vect.Data.Models.ReminderModel
import ir.adak.Vect.Listeners.OnLoginCompleted
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.BaseActivity
import ir.adak.Vect.Utils.cancelAlarm
import ir.adak.Vect.Utils.enqueue
import ir.adak.Vect.Utils.setAlarmManager
import kotlinx.android.synthetic.main.activity_reminder.*
import saman.zamani.persiandate.PersianDate

class ReminderActivity : BaseActivity() {

    private var reminderModel: ReminderModel? = null
    lateinit var mp: MediaPlayer
    val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)
        mp = MediaPlayer.create(this, R.raw.alarm)
        mp.isLooping = true
        mp.start()

        reminderModel = intent.extras?.getParcelable("reminderModel")
        txt_note_title.text = reminderModel?.title
        txt_note_description.text = reminderModel?.description

        ripple_background.startRippleAnimation()

        btn_snooze.setOnClickListener {
            Snooze()
        }

        btn_dismiss.setOnClickListener {
            cancelAlarm(this, reminderModel?.intId ?: 0)
            reminders?.removeAll {
                it.id == reminderModel?.id
            }
            updateSharedReferences()
            finish()
        }
        handler.postDelayed({
            runOnUiThread {
                btn_snooze.performClick()
            }
        }, 60 * 1000)
    }

    private fun Snooze() {
        var newTime = ""
        val persianDate = PersianDate()

        val year = persianDate.shYear
        val month = persianDate.shMonth
        val day = persianDate.shDay
        val hour = persianDate.hour
        var minute = persianDate.minute

        minute += 5
        newTime = "$hour:$minute"
        if (isNetConnected()) {
            val req = App.getRetofitApi()?.registerOrEditNote(
                token = token,
                registerNoteModel = RegisterNoteModel(
                    reminderModel?.id,
                    reminderModel?.title,
                    reminderModel?.description,
                    "$year/$month/$day",
                    newTime
                )
            )
            req?.enqueue {
                onResponse = {
                    if (isResponseValid(it) == 2) {
                        if (!reminderModel?.date.isNullOrEmpty() && !reminderModel?.time.isNullOrEmpty()) {

                            val reminderModel = ReminderModel(
                                it.body()?.data?.id,
                                it.body()?.data?.intId,
                                it.body()?.data?.title,
                                it.body()?.data?.description,
                                it.body()?.data?.dateReminder,
                                it.body()?.data?.timeReminder
                            )
                            setAlarmManager(
                                this@ReminderActivity,
                                year,
                                month,
                                day,
                                hour,
                                minute,
                                reminderModel
                            )
                            reminders?.forEach {
                                if (it.id == reminderModel.id) {
                                    it.title = reminderModel.title
                                    it.description = reminderModel.description
                                    it.date = reminderModel.date
                                    it.time = reminderModel.time
                                    return@forEach
                                }
                            }
                            updateSharedReferences()
                        }
                        finish()
                    } else if (isResponseValid(it) == 1) {
                        silentLogin(object : OnLoginCompleted {
                            override fun OnSuccess() {
                                Snooze()
                            }
                        })
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        mp.stop()
        mp.release()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        reminderModel = intent?.extras?.getParcelable("reminderModel")
        txt_note_title.text = reminderModel?.title
        txt_note_description.text = reminderModel?.description
    }
}
