package ir.adak.Vect.Data.Models

data class GetMeetingResponse(
    val meetings: List<Meeting>,
    val moreDate: Boolean
)