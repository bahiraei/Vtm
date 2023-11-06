package ir.adak.Vect.Data.Models

data class MemberLogDto(
    val access: Int,
    val date: String,
    val id: String,
    val logType: Int,
    val memberOrgLevelId: Int,
    val memberTitle: String,
    val memberFullName: String,
    val profileImage: String
)