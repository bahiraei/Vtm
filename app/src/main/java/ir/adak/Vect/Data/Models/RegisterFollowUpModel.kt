package ir.adak.Vect.Data.Models

data class RegisterFollowUpModel(
    val ProjectId: String?,
    val Description: String?,
    val type: Int?,
    val actionTime: String?,
    val actionDate: String?,
    val fileType: Int? = null,
    val followupId: String?
)