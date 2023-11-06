package ir.adak.Vect.Services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ir.adak.Vect.UI.Activities.ReminderActivity.ReminderActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ir.adak.Vect.Data.Models.ReminderModel


class AlarmReciver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val reminderModel: ReminderModel? =
            Gson().fromJson(
                intent?.extras?.getString("reminderModel"),
                object : TypeToken<ReminderModel>() {}.type
            )
        context?.startActivity(
            Intent(context, ReminderActivity::class.java).putExtra(
                "reminderModel",
                reminderModel
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            })
    }
}