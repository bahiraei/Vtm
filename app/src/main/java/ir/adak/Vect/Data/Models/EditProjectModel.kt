package ir.adak.Vect.Data.Models

data class EditProjectModel(
    var Title: String?,
    var Description: String?,
    var ProjectId: String?,
    var StartDate: String?,
    val letterDatefa: String? = null,
    val letterNumber: String? = null,
    var EndDate: String?,
    var Priority: Int,
    var ProjectTypeId: Int,
    var PrimaryCredit: Int,
    var Members: List<MemberDto>
)