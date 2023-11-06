package ir.adak.Vect.Data.Models

data class GetMyPersonelResponse(

    var partners: ArrayList<partners>?= null,
    var partnersForSecretary: ArrayList<partnersForSecretary>?= null,
    var currentSecretary: currentSecretary?= null
//    val birthDayFa: String?,
//    val firstName: String?,
//    val gender: Int?,
//    val isActive: Boolean?,
//    val lastName: String?,
//    val meliCode: String?,
//    val orgTitle: String?,
//    val personId: String?,
//    val isConfirmSms: Boolean?,
//    val lastLoginDate: String?,
//    val phone: String?
)