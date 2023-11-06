package ir.adak.Vect.Data.Models

data class RegisterMeetingRequestModel(
    val description: String,
    val endTime: String,
    val headOf: String,
    val location: String,
    val meetingId: String?,
    val meetingCode: Int,
    val members: List<Int>,
    val persianHeldDate: String,
    val projectId: String?,
    val secretary: String,
    val startTime: String,
    val title: String
)