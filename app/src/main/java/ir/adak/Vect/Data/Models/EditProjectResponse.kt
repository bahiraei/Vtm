package ir.adak.Vect.Data.Models

data class EditProjectResponse(
    val project: Project?,
    val followUps: List<FollowUp>?
)