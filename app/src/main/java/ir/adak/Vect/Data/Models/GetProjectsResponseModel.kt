package ir.adak.Vect.Data.Models

data class GetProjectsResponseModel(
    val moreDate: Boolean,
    val projects: List<Project>
)