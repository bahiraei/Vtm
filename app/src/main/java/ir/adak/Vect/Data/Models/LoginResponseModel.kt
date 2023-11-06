package ir.adak.Vect.Data.Models

data class LoginResponseModel(
    val age: Int,
    val appVersion: AppVersion,
    val birthDayFa: String,
    val fullName: String,
    val gender: Int,
    val profileImage: String,
    val title: String,
    val token: String,
    var securityKey: String?,
    val orgLevelId: Int,
    val orgLevelTitle: String,
    var projectTypes :ArrayList<projectTypes> ?=null,
    var adminAccountInfo: adminAccountInfo ?=null
)