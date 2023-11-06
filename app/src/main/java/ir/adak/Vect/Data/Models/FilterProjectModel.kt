package ir.adak.Vect.Data.Models

import android.os.Parcelable
import ir.adak.Vect.Data.Enums.ProjectMemberType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterProjectModel(
    var DateType: Int = -1,
    var DateRangeType: Int = 1,
    var DateFrom: String? = null,
    var DateTo: String? = null,
    var Day: Int = -1,
    var Priority: Int = -1,
    var step: Int = ProjectStep.NoFilter.value,
    var memberType: Int = ProjectMemberType.NoFilter.value,
    var ProjectStatus: Int = -1,
    var ProjectTypes: MutableList<Int>? = null,
    var FromMeeting: Int = -1,
    var Title: String? = null,
    var actionStatusPending : Boolean =false,
    var MeetingIds: MutableList<String>? = null
) : Parcelable