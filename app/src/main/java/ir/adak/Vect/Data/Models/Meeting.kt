package ir.adak.Vect.Data.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Meeting(
    val description: String? = null,
    val endTime: String? = null,
    val headOf: String? = null,
    var id: String? = null,
    var isHeld: Boolean = false,
    val isMyMeeting: Boolean = false,
    val location: String? = null,
    val meetingCode: Int = -1,
    val orgLevelId: Int = -1,
    var memberCount: Int = -1,
    val orgLevelTitle: String? = null,
    val persianHeldDate: String? = null,
    val personelFullName: String? = null,
    val personelId: String? = null,
    val projectId: String? = null,
    val secretary: String? = null,
    val startTime: String? = null,
    val title: String? = null,
    var loading: Boolean = false,
    var isHighlight: Boolean = false
) : Parcelable


