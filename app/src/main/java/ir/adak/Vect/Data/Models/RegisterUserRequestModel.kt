package ir.adak.Vect.Data.Models

data class RegisterUserRequestModel(
    var id:String ?=null,
    val birthDayFa: String,
    val firstName: String,
    val gender: Int,
    val lastName: String,
    val meliCode: String,
    val orgTitle: String,
    val personId: String,
    val phone: String
)