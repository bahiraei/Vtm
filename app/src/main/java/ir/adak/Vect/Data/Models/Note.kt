package ir.adak.Vect.Data.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note(
    var id: String? = null,
    var intId: Int = 0,
    var title: String? = null,
    var description: String? = null,
    var date: String? = null,
    var dateReminder: String? = null,
    var dateReminderEn: String? = null,
    var timeReminder: String? = null
) : Parcelable