package ir.adak.Vect.Data.Models

data class ConfirmSmsRequestModel(
    var meliCode: String,
    var personId: String,
    var phone: String,
    var osType: Int,
    var seriall: String,
    var confirmCode: String?,
    var appVersion: Int
)
