package ir.adak.Vect.Data.Models

data class RegisterFollowUpResponseModel(
    val id: String,
    val persianCreatedDate: String,
    val profileImage: String?,
    val enumFollowUpType: Int,
    val fileName: String,
    val fileExtension: String,
    val fileSize: Long
)