package ir.adak.Vect.Data.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Project(
    val description: String? = null,
    val countFollowUp: Int = -1,
    val mojryOrgLevelId: Int = -1,
    val persianEndDate: String? = null,
    val id: String? = null,
    val isMyProject: Boolean = false,
    val lastFollowUpId: String? = null,
    val stepTitle: String? = null,
    val mojryOrgLevelTitle: String? = null,
    val mojryFullName: String? = null,
    val projectStep: Int? = null,
    val lastFollowUpdate: Long = -1,
    val orgLevelTitle: String? = null,
    val orgLevelId: Int = -1,
    val personFullName: String? = null,
    val letterDatefa: String? = null,
    val letterNumber: String? = null,
    val priority: Int = -1,
    var progress: Int = -1,
    var projectStatus: Int = -1,
    var memberCount: Int = -1,
    var projectTypeId: Int = -1,
    var projectTypeName: String ? =null,
    var projectTypeColor: String ?= null,
    var projectTypeIconOrFont: String ?= null,
    val persianStartDate: String? = null,
    val title: String? = null,
    val meetingId: String? = null,
    val meetingTitle: String? = null,
    val isExpired: Boolean = false,
    var loading: Boolean = false,
    var isHighlight: Boolean = false
) : Parcelable