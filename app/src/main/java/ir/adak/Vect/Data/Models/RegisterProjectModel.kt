package ir.adak.Vect.Data.Models

data class RegisterProjectModel(
    var Title: String?,
    var Description: String?,
    var projectStatus: Int,
    var ProjectParentId: String?,
    var StartDate: String?,
    var EndDate: String?,
    val letterDatefa: String? = null,
    val letterNumber: String? = null,
    var Priority: Int,
    var ProjectTypeId: Int,
    var PrimaryCredit: Int,
    var MeetingId: String?,
    var OfferId: String?,
    var Members: List<MemberDto>
)