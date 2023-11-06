package ir.adak.Vect.Utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.google.gson.Gson
import ir.adak.Vect.Data.Models.ReminderModel
import ir.adak.Vect.Services.AlarmReciver
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import saman.zamani.persiandate.PersianDate
import java.util.*


fun <T> Call<T>.enqueue(callback: CallBackKt<T>.() -> Unit) {
    val callBackKt = CallBackKt<T>()
    callback.invoke(callBackKt)
    this.enqueue(callBackKt)
}

class CallBackKt<T> : Callback<T> {

    var onResponse: ((Response<T>) -> Unit)? = null
    var onFailure: ((t: Throwable?) -> Unit)? = null

    override fun onFailure(call: Call<T>, t: Throwable) {
        onFailure?.invoke(t)
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        onResponse?.invoke(response)

    }

}


fun setAlarmManager(
    context: Context?,
    year: Int,
    month: Int,
    day: Int,
    hour: Int,
    minute: Int,
    reminderModel: ReminderModel?
) {
    val am = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val date = PersianDate().toGregorian(year, month, day)

    val calendar = Calendar.getInstance()

    calendar.timeInMillis = System.currentTimeMillis()

    calendar.clear()

    calendar.set(date[0], date[1] - 1, date[2], hour, minute, 0)


    val intent = Intent(context, AlarmReciver::class.java).putExtra(
        "reminderModel",
        Gson().toJson(reminderModel)
    )
    intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
    val pi = PendingIntent.getBroadcast(
        context,
        reminderModel?.intId ?: 0, intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    if (calendar.timeInMillis > System.currentTimeMillis()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)
        } else {
            am.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)
        }
    }

}

fun setAlarmManager(
    context: Context?,
    reminderModels: MutableList<ReminderModel>
) {
    val am = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    reminderModels.forEach {
        if (!it.date.isNullOrEmpty() && !it.time.isNullOrEmpty()) {
            val year = Integer.parseInt(it.date!!.split("/")[0])
            val month: Int = Integer.parseInt(it.date!!.split("/")[1])
            val day: Int = Integer.parseInt(it.date!!.split("/")[2])
            val hour: Int = Integer.parseInt(it.time!!.split(":")[0])
            val minute: Int = Integer.parseInt(it.time!!.split(":")[1])

            val date = PersianDate().toGregorian(year, month, day)

            val calendar = Calendar.getInstance()

            calendar.timeInMillis = System.currentTimeMillis()

            calendar.clear()

            calendar.set(date[0], date[1] - 1, date[2], hour, minute, 0)

            val intent =
                Intent(context, AlarmReciver::class.java).putExtra(
                    "reminderModel",
                    Gson().toJson(it)
                )
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
            val pi = PendingIntent.getBroadcast(
                context,
                it?.intId ?: 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            if (calendar.timeInMillis > System.currentTimeMillis()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    am.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)
                } else {
                    am.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)
                }
            }
        }

    }

}


fun cancelAlarm(context: Context?, deletedIds: List<Int>) {

    val am = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    deletedIds.forEach {
        val intent = Intent(context, AlarmReciver::class.java)
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
        val pi = PendingIntent.getBroadcast(
            context,
            it, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        am.cancel(pi)
        pi.cancel()
    }

}

fun cancelAlarm(context: Context?, id: Int) {

    val am = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val intent = Intent(context, AlarmReciver::class.java)
    intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
    val pi = PendingIntent.getBroadcast(
        context,
        id, intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    am.cancel(pi)
    pi.cancel()

}