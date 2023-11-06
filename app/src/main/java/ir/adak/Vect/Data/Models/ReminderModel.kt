package ir.adak.Vect.Data.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReminderModel(
    var id: String?,
    var intId: Int?,
    var title: String?,
    var description: String?,
    var date: String?,
    var time: String?
) : Parcelable