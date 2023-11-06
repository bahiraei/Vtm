package ir.adak.Vect.Data.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ScheduledProjectModel(
    val daysOfWeekOrMonth: List<Int>,
    val description: String?,
    val endDateType: Int,
    val endProjectNumber: Int,
    val id: String,
    var isActive: Boolean,
    val letterDatefa: String?,
    val letterNumber: String?,
    val memberCount: Int,
    val nextRunTitle: String?,
    val orgLevelId: Int,
    val orgLevelTitle: String?,
    val persianStartDate: String?,
    val personelFullName: String?,
    val priority: Int,
    val projectStatus: Int,
    var projectTypeId: Int = -1,
    var projectTypeName: String ? =null,
    var projectTypeColor: String ?= null,
    var projectTypeIconOrFont: String ?= null,
    var mojryOrgLevelId: Int ?= null,
    var mojryFullName: String ?= null,
    var mojryOrgLevelTitle: String ?= null,
    val runSchedulerCount: Int,
    val schedulerTitle: String?,
    val schedulerType: Int,
    val startTime: String?,
    val timeLenForRunningProject: String?,
    val title: String?,
    var isHighlight: Boolean
) : Parcelable