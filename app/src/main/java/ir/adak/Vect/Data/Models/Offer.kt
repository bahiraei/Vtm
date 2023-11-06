package ir.adak.Vect.Data.Models

data class Offer(
    val date: String? = null,
    val hasProject: Boolean = false,
    val id: String? = null,
    val isDiscussed: Boolean = false,
    val currentMeeting: Meeting? = null,
    val meetingId: String? = null,
    val orgLevelId: Int = -1,
    val orgLevelTitle: String? = null,
    val personelName: String? = null,
    val projects: List<Project>? = null,
    val title: String? = null
)