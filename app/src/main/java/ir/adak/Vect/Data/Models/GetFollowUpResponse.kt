package ir.adak.Vect.Data.Models

data class GetFollowUpResponse(
    val followUps: List<FollowUp>?,
    val project: Project?,
    val pinFollowUp: FollowUp?
)